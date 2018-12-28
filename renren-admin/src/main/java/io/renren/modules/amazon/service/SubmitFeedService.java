package io.renren.modules.amazon.service;

import com.amazonaws.mws.MarketplaceWebService;
import com.amazonaws.mws.model.GetFeedSubmissionListRequest;
import com.amazonaws.mws.model.GetFeedSubmissionResultRequest;
import com.amazonaws.mws.model.SubmitFeedRequest;
import io.renren.modules.amazon.dto.AnalysisFeedSubmissionResultDto;
import io.renren.modules.amazon.dto.FeedSubmissionInfoDto;
import io.renren.modules.amazon.dto.FeedSubmissionResultDto;
import io.renren.modules.amazon.entity.ResultXmlEntity;
import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.entity.UploadEntity;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;

/**
 * 上传产品接口实现类
 *
 * @author zjr
 */
public interface SubmitFeedService {

    /**
     * 产品异步上传
     *
     * @param uploadId           上传id
     * @param serviceURL         服务器接口网址
     * @param merchantId         店铺id
     * @param sellerDevAuthToken 授权令牌
     * @param uploadTypeMap      上传类型map
     * @param filePathMap        文件路径map 用来对应文件类型
     * @param marketplaceIdList  国家端点列表
     * @return 上传id
     * @author zjr
     */
    List<FeedSubmissionInfoDto> submitFeedAsync(Long uploadId, String serviceURL, String merchantId, String sellerDevAuthToken, Map<String, String> uploadTypeMap, Map<String, String> filePathMap, List<String> marketplaceIdList);

    /**
     * 获取同步上传Service
     *
     * @param serviceURL 服务器接口网址
     * @return MarketplaceWebService service
     * @author zjr
     */
    MarketplaceWebService getService(String serviceURL);

    /**
     * 获取异步上传Service
     *
     * @param serviceURL 服务器接口网址
     * @return MarketplaceWebService service
     * @author zjr
     */
    MarketplaceWebService getAsyncService(String serviceURL);

    /**
     * 保存上传id和处理状态
     *
     * @param feedSubmissionInfoDtoList
     * @param uploadState               总状态，默认0：等待上传；1：正在上传；2：上传成功；3：上传失败； 4：有警告
     */
    void updateFeedUpload(Long uploadId,List<FeedSubmissionInfoDto> feedSubmissionInfoDtoList, int uploadState);

    /**
     * 上传产品
     *
     * @param uploadEntity
     */
    @Async
    void submitFeed(UploadEntity uploadEntity);

    /**
     * 上传商品基本信息xml
     *
     * @param uploadId           上传id
     * @param serviceURL         服务器接口网址
     * @param merchantId         店铺id
     * @param sellerDevAuthToken 授权令牌
     * @param feedType           文件上传类型
     * @param filePath           文件路径
     * @param marketplaceIdList  国家端点
     * @return
     */
    FeedSubmissionInfoDto submitProductFeed(Long uploadId, String serviceURL, String merchantId, String sellerDevAuthToken, String feedType, String filePath, List<String> marketplaceIdList);

    /**
     * 返回提交的上传数据提交列表
     */
    List<FeedSubmissionInfoDto> getFeedSubmissionListAsync(Long uploadId, String serviceURL, String merchantId, String sellerDevAuthToken, List<String> feedSubmissionIdList);

    List<FeedSubmissionInfoDto> invokeGetFeedSubmissionList(Long uploadId, MarketplaceWebService service, List<GetFeedSubmissionListRequest> requests);

    /**
     * 请求商品上传报告
     *
     * @param uploadId                  上传id
     * @param path                      文件存放路径
     * @param serviceURL                服务器接口网址
     * @param merchantId                店铺id
     * @param sellerDevAuthToken        授权令牌
     * @param feedSubmissionInfoDtoList xml信息列表
     * @return List<FeedSubmissionResultDto>
     */
    List<FeedSubmissionResultDto> getFeedSubmissionResultAsync(Long uploadId, String path, String serviceURL, String merchantId, String sellerDevAuthToken, List<FeedSubmissionInfoDto> feedSubmissionInfoDtoList);

    List<FeedSubmissionResultDto> invokeGetFeedSubmissionResult(Long uploadId, MarketplaceWebService service, List<GetFeedSubmissionResultRequest> requests);

    /**
     * 子分类状态判断
     *
     * @param analysisFeedSubmissionResultDto
     * @return
     */
    int judgementState(AnalysisFeedSubmissionResultDto analysisFeedSubmissionResultDto);

    /**
     * 总分类状态判断
     *
     * @param substate 子状态列表
     * @return
     */
    int judgingTheTotalState(List<Integer> substate);

    List<FeedSubmissionInfoDto> invokeSubmitFeedAsync(Long uploadId, MarketplaceWebService service, List<SubmitFeedRequest> requests);

    ResultXmlEntity isExist(Long uploadId, String tpye);

    String switchCountry(String templateName, Long uploadId, String merchantIdentifierText, List<ProductsEntity> productsList, String countryCode);
}
