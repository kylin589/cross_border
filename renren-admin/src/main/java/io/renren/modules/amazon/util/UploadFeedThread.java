package io.renren.modules.amazon.util;

import com.amazonaws.mws.MarketplaceWebService;
import com.amazonaws.mws.model.GetFeedSubmissionListRequest;
import com.amazonaws.mws.model.IdList;
import io.renren.modules.amazon.dto.FeedSubmissionInfoDto;
import io.renren.modules.amazon.service.SubmitFeedService;
import io.renren.modules.product.entity.UploadEntity;
import io.renren.modules.product.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 上传xml线程
 *
 * @author zjr
 */
public class UploadFeedThread extends Thread {

    /**
     * 上传id
     */
    private Long uploadId;

    /**
     * 请求接口网站
     */
    private String serviceURL;

    /**
     * 店铺id
     */
    private String merchantId;

    /**
     * 授权令牌
     */
    private String sellerDevAuthToken;

    /**
     * 上传xml类型
     */
    private Map<String, String> uploadTypeMap;

    /**
     * xml文件路径
     */
    private Map<String, String> filePathMap;

    /**
     * 国家端点
     */
    private List<String> marketplaceIdList;

    @Autowired
    private SubmitFeedService submitFeedService;

    @Autowired
    private UploadService uploadService;

    public UploadFeedThread(Long uploadId, String serviceURL, String merchantId, String sellerDevAuthToken, Map<String, String> uploadTypeMap, Map<String, String> filePathMap, List<String> marketplaceIdList) {
        this.uploadId = uploadId;
        this.serviceURL = serviceURL;
        this.merchantId = merchantId;
        this.sellerDevAuthToken = sellerDevAuthToken;
        this.uploadTypeMap = uploadTypeMap;
        this.filePathMap = filePathMap;
        this.marketplaceIdList = marketplaceIdList;
    }

    @Override
    public void run() {

        // 上传xml
        String productKey = "0";
        if (filePathMap.containsKey(productKey)) {
            // 产品信息上传
            FeedSubmissionInfoDto feedSubmissionInfoDto = submitFeedService.submitProductFeed(uploadId, serviceURL, merchantId, sellerDevAuthToken, uploadTypeMap.get(productKey), filePathMap.get(productKey), marketplaceIdList).get(0);
            //使用FeedSubmissionId获取的亚马逊对于xml的处理状态
            while (true) {
                try {
                    List<String> feedSubmissionList = new ArrayList<>();
                    feedSubmissionList.add(feedSubmissionInfoDto.getFeedSubmissionId());
                    feedSubmissionInfoDto = submitFeedService.getFeedSubmissionListAsync(uploadId, serviceURL,merchantId,sellerDevAuthToken,feedSubmissionList).get(0);

                    if ("_DONE_".equals(feedSubmissionInfoDto.getFeedType())) {
                        break;
                    }
                    // 出现如下三种情况，总状态变失败。
                    if ("_IN_SAFETY_NET_".equals(feedSubmissionInfoDto.getFeedType()) || "_CANCELLED_".equals(feedSubmissionInfoDto.getFeedType()) || "_AWAITING_ASYNCHRONOUS_REPLY_".equals(feedSubmissionInfoDto.getFeedType())){
                        List<FeedSubmissionInfoDto> tempList = new ArrayList<>();
                        tempList.add(feedSubmissionInfoDto);
                        submitFeedService.updateFeedUpload(tempList,3);
                        // TODO: 2018/12/22 能否把xml放入数据库？
                    }
                    // 设置睡眠的时间 60 秒
                    sleep(60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            List<FeedSubmissionInfoDto> feedSubmissionInfoDtoList = submitFeedService.submitFeedAsync(uploadId, serviceURL, merchantId, sellerDevAuthToken, uploadTypeMap, filePathMap, marketplaceIdList);
            feedSubmissionInfoDtoList.add(feedSubmissionInfoDto);

            // FeedSubmissionInfoDto 数据存放，等待上传
            submitFeedService.updateFeedUpload(feedSubmissionInfoDtoList,0);

            List<String> feedSubmissionIdList = new ArrayList<>();
            for (int i = 0; i < feedSubmissionInfoDtoList.size(); i++) {
                feedSubmissionIdList.add(feedSubmissionInfoDtoList.get(i).getFeedSubmissionId());
            }


            // TODO: 2018/12/22 zjr 先上传基本信息 ，状态和总状态改为等待上传，

            // TODO: 2018/12/22 zjr 再上传剩余文件

            // TODO: 2018/12/22 zjr 状态和总状态改为正在上传

            // TODO: 2018/12/22 zjr 等所有状态为 _Done_的时候，获取报告

            // TODO: 2018/12/22 zjr 获取报告解析。放入xml表 ，如果都成功，总状态也变成成功，如果失败，总状态失败，失败xml的状态也失败。线程关闭

            // 当所有状态都为_DONE_时，执行下一步
            boolean b = false;
            int count;
            while (!b) {
                try {
                    // 设置睡眠的时间 2 分钟
                    sleep(2 * 60 * 1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count = 0;
                feedSubmissionInfoDtoList = submitFeedService.getFeedSubmissionListAsync(uploadId,serviceURL,merchantId,sellerDevAuthToken,feedSubmissionIdList);
                for (int i = 0; i < feedSubmissionInfoDtoList.size(); i++) {
                    if ("_DONE_".equals(feedSubmissionInfoDtoList.get(0).getFeedProcessingStatus())){
                        count++;
                        if (count==5){
                            b = true;
                            break;
                        }
                    }
                }
            }

            // 总状态改为正在上传
            submitFeedService.updateFeedUpload(feedSubmissionInfoDtoList,1);



        } else {
            // 关系，库存，图片，价格上传
            List<FeedSubmissionInfoDto> feedSubmissionInfoDtoList = submitFeedService.submitFeedAsync(uploadId, serviceURL, merchantId, sellerDevAuthToken, uploadTypeMap, filePathMap, marketplaceIdList);
            // FeedSubmissionInfoDto 数据存放

        }
    }
}
