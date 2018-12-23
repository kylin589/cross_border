package io.renren.modules.amazon.service;

import com.amazonaws.mws.MarketplaceWebService;
import com.amazonaws.mws.model.GetFeedSubmissionListRequest;
import com.amazonaws.mws.model.GetFeedSubmissionResultRequest;
import com.amazonaws.mws.model.SubmitFeedRequest;
import io.renren.modules.amazon.dto.AnalysisFeedSubmissionResultDto;
import io.renren.modules.amazon.dto.FeedSubmissionInfoDto;
import io.renren.modules.amazon.dto.FeedSubmissionResultDto;
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
     * 生成产品基础信息xml
     *
     * @param merchantIdentifierText 卖家记号
     * @param productsList           产品列表
     * @param countryCode            国家代码
     * @param categoryNodeId         分类Id
     * @return XML路径
     * @author zjr
     */
    String generateProductXML(String merchantIdentifierText, List<ProductsEntity> productsList, String countryCode, String categoryNodeId);

    /**
     * 生成产品图片信息xml
     *
     * @param productsList           产品列表
     * @param merchantIdentifierText 卖家记号
     * @return XML路径
     * @author zjr
     */
    String generateImagesXML(List<ProductsEntity> productsList, String merchantIdentifierText);

    /**
     * 生成库存信息xml
     *
     * @param productsList           产品列表
     * @param merchantIdentifierText 卖家记号
     * @return xml路径
     * @author zjr
     */
    String generateInventoryXML(List<ProductsEntity> productsList, String merchantIdentifierText);

    /**
     * 生成价格信息xml
     *
     * @param countryCode            国家代码
     * @param productsList           产品列表
     * @param merchantIdentifierText 卖家记号
     * @return xml路径
     * @author zjr
     */
    String generatePricesXML(String countryCode, List<ProductsEntity> productsList, String merchantIdentifierText);

    /**
     * 生成关系信息xml
     *
     * @param productsList           产品列表
     * @param merchantIdentifierText 卖家记号
     * @return xml路径
     * @author zjr
     */
    String generateRelationshipsXML(List<ProductsEntity> productsList, String merchantIdentifierText);

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
     * @param uploadState               总状态，默认0：等待上传；1：正在上传；2：上传成功；3：上传失败； 4？
     */
    void updateFeedUpload(List<FeedSubmissionInfoDto> feedSubmissionInfoDtoList, int uploadState);

    // 上传产品
    @Async
    void submitFeed(UploadEntity uploadEntity);

    // 同步上传数据
    List<FeedSubmissionInfoDto> submitProductFeed(Long uploadId, String serviceURL, String merchantId, String sellerDevAuthToken, String feedType, String filePath, List<String> marketplaceIdList);

    /**
     * 返回提交的上传数据提交列表
     */
    List<FeedSubmissionInfoDto> getFeedSubmissionListAsync(Long uploadId, String serviceURL, String merchantId, String sellerDevAuthToken, List<String> feedSubmissionIdList);

    List<FeedSubmissionInfoDto> invokeGetFeedSubmissionList(Long uploadId, MarketplaceWebService service, List<GetFeedSubmissionListRequest> requests);

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

    /**
     * 重新提交
     * @param uploadEntity
     */
    void ReUploadFeed(UploadEntity uploadEntity);
}
