package io.renren.modules.amazon.service.impl;

import com.amazonaws.mws.MarketplaceWebService;
import com.amazonaws.mws.MarketplaceWebServiceClient;
import com.amazonaws.mws.MarketplaceWebServiceConfig;
import com.amazonaws.mws.MarketplaceWebServiceException;
import com.amazonaws.mws.model.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.modules.amazon.dto.AnalysisFeedSubmissionResultDto;
import io.renren.modules.amazon.dto.FeedSubmissionInfoDto;
import io.renren.modules.amazon.dto.FeedSubmissionResultDto;
import io.renren.modules.amazon.entity.AmazonGrantEntity;
import io.renren.modules.amazon.entity.AmazonGrantShopEntity;
import io.renren.modules.amazon.entity.ResultXmlEntity;
import io.renren.modules.amazon.service.AmazonGrantService;
import io.renren.modules.amazon.service.AmazonGrantShopService;
import io.renren.modules.amazon.service.ResultXmlService;
import io.renren.modules.amazon.service.SubmitFeedService;
import io.renren.modules.amazon.util.ContentMD5Util;
import io.renren.modules.amazon.util.FileUtil;
import io.renren.modules.amazon.util.XMLUtil;
import io.renren.modules.product.entity.*;
import io.renren.modules.product.service.*;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.Future;

@Service
@Component
public class SubmitFeedServiceImpl implements SubmitFeedService {

    @Autowired
    private ImageAddressService imageAddressService;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private IntroductionService introductionService;

    @Autowired
    private VariantsInfoService variantsInfoService;

    @Autowired
    private AmazonGrantService amazonGrantService;

    @Autowired
    private AmazonGrantShopService amazonGrantShopService;

    @Autowired
    private VariantParameterService variantParameterService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private FreightCostService freightCostService;

    @Autowired
    private ResultXmlService resultXmlService;

    @Autowired
    private FieldMiddleService fieldMiddleService;

    @Autowired
    private AmazonCategoryService amazonCategoryService;

    @Autowired
    private GenerateProductXML generateProductXML;

    @Autowired
    private TemplateService templateService;

    @Value(("${mws-config.access-key}"))
    private String accessKey;

    @Value(("${mws-config.secret-key}"))
    private String secretKey;

    @Value(("${mws-config.app-name}"))
    private String appName;

    @Value(("${mws-config.app-version}"))
    private String appVersion;

    @Value(("${file.path}"))
    private String fileStoragePath;

    private static Map<String, String> uploadTypeMap;

    static {
        Map map = new HashMap<String, String>();
        // 0 基本信息
        map.put("0", "_POST_PRODUCT_DATA_");
        // 1 关系
        map.put("1", "_POST_PRODUCT_RELATIONSHIP_DATA_");
        // 2 图片
        map.put("2", "_POST_PRODUCT_IMAGE_DATA_");
        // 3 库存
        map.put("3", "_POST_INVENTORY_AVAILABILITY_DATA_");
        // 4 价格
        map.put("4", "_POST_PRODUCT_PRICING_DATA_");
        uploadTypeMap = Collections.unmodifiableMap(map);
    }

    @Override
    @Async("taskExecutor")
    public void submitFeed(UploadEntity uploadEntity) {
        System.out.println("submitFeed被调用！");

        //上传id
        Long uploadId = uploadEntity.getUploadId();

        // 商品列表
        List<ProductsEntity> productsEntityList;
        if (uploadEntity.getUploadProductsList() != null) {
            productsEntityList = uploadEntity.getUploadProductsList();
        } else {
            List<String> ids = Arrays.asList(uploadEntity.getUploadProductsIds().split(","));
            productsEntityList = productsService.selectBatchIds(ids);
        }

        // 授权店铺信息
        AmazonGrantShopEntity amazonGrantShopEntity = amazonGrantShopService.selectById(uploadEntity.getGrantShopId());
        // 请求接口网站
        String serviceURL = amazonGrantShopEntity.getMwsPoint();
        // 国家端点
        String marketplaceId = amazonGrantShopEntity.getMarketplaceId();
        List<String> marketplaceIdList = new ArrayList<>();
        marketplaceIdList.add(marketplaceId);

        AmazonGrantEntity amazonGrantEntity = amazonGrantService.selectById(amazonGrantShopEntity.getGrantId());
        // 授权令牌
        String sellerDevAuthToken = amazonGrantEntity.getGrantToken();
        // 店铺id
        String merchantId = amazonGrantEntity.getMerchantId();
        // 国家代码
        String countryCode = amazonGrantShopEntity.getCountryCode();
        // 操作项
        String[] operateItemStr = uploadEntity.getOperateItem().split(",");
        // 模板名称
        String templateName = "";
        if (uploadEntity.getAmazonTemplateId() == 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("template_display_name", uploadEntity.getAmazonTemplate());
            templateName = templateService.selectByMap(map).get(0).getTemplateName();
        } else {
            templateName = templateService.selectById(uploadEntity.getAmazonTemplateId()).getTemplateName();
        }

        // 判断是否是单商品上传
        if (productsEntityList.size()==1){
            Long pId = productsEntityList.get(0).getProductId();
            List<VariantsInfoEntity> variantsInfoEntityList = variantsInfoService.selectList(new EntityWrapper<VariantsInfoEntity>().eq("product_id", pId).orderBy(true, "variant_sort", true));
            if (variantsInfoEntityList.size() == 0) {
                //没有变体，不生成关系XML
                for (int i = 0; i < operateItemStr.length; i++) {
                    if ("1".equals(operateItemStr[i])){
                        operateItemStr[i] = "-1";
                    }
                }
            }
        }

        // 生成xml文件路径
        Map<String, String> filePathMap = new HashMap<>();
        for (int i = 0; i < operateItemStr.length; i++) {
            switch (operateItemStr[i]) {
                // 0 基本信息
                case "0":
                    String productPath = switchCountry(templateName, uploadId, merchantId, productsEntityList, countryCode);
                    filePathMap.put("0", productPath);
                    break;
                // 1 关系
                case "1":
                    String relationshipsPath = generateProductXML.generateRelationshipsXML(productsEntityList, merchantId);
                    filePathMap.put("1", relationshipsPath);
                    break;
                // 2 图片
                case "2":
                    String imagesPath = generateProductXML.generateImagesXML(productsEntityList, merchantId);
                    filePathMap.put("2", imagesPath);
                    break;
                // 3 库存
                case "3":
                    String inventoryPath = generateProductXML.generateInventoryXML(productsEntityList, merchantId);
                    filePathMap.put("3", inventoryPath);
                    break;
                // 4 价格
                case "4":
                    String pricesPath = generateProductXML.generatePricesXML(countryCode, productsEntityList, merchantId);
                    filePathMap.put("4", pricesPath);
                    break;
                default:
            }
        }

        // 上传xml

        UploadEntity updateUploadEntity = new UploadEntity();
        updateUploadEntity.setUpdateTime(new Date());
        updateUploadEntity.setUploadId(uploadId);

        FeedSubmissionInfoDto productFeedSubmissionInfoDto = null;

        // 0 是产品基本信息xml
        if (filePathMap.containsKey("0")) {
            // 产品信息上传
            productFeedSubmissionInfoDto = submitProductFeed(uploadId, serviceURL, merchantId, sellerDevAuthToken, uploadTypeMap.get("0"), filePathMap.get("0"), marketplaceIdList);
            //使用FeedSubmissionId获取的亚马逊对于xml的处理状态

            // 处理总状态--正在上传
            updateUploadEntity.setUploadState(1);
            uploadService.updateById(updateUploadEntity);

            while (true) {
                try {
                    List<String> feedSubmissionList = new ArrayList<>();
                    feedSubmissionList.add(productFeedSubmissionInfoDto.getFeedSubmissionId());
                    productFeedSubmissionInfoDto = getFeedSubmissionListAsync(uploadId, serviceURL, merchantId, sellerDevAuthToken, feedSubmissionList).get(0);
                    // 成功
                    if (productFeedSubmissionInfoDto.getFeedProcessingStatus().equals(1)) {
                        break;
                    }
                    // 出现如下三种情况，总状态变失败。
                    if (productFeedSubmissionInfoDto.getFeedProcessingStatus().equals(3)) {
                        List<FeedSubmissionInfoDto> tempList = new ArrayList<>();
                        tempList.add(productFeedSubmissionInfoDto);
                        updateFeedUpload(uploadId, tempList, 3);
                        break;
                    }

                    // 设置睡眠的时间 120 秒
                    Thread.sleep(2 * 60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        List<FeedSubmissionInfoDto> feedSubmissionInfoDtoList;
        // 剩余xml的上传
        while (true) {
            feedSubmissionInfoDtoList = submitFeedAsync(uploadId, serviceURL, merchantId, sellerDevAuthToken, uploadTypeMap, filePathMap, marketplaceIdList);

            if (productFeedSubmissionInfoDto != null) {
                feedSubmissionInfoDtoList.add(productFeedSubmissionInfoDto);
            }

            if (feedSubmissionInfoDtoList.size() == filePathMap.size()) {
                break;
            }

            // 设置睡眠的时间 60 秒
            try {
                Thread.sleep(2 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // FeedSubmissionInfoDto 数据存放，正在上传
        updateFeedUpload(uploadId, feedSubmissionInfoDtoList, 1);

        List<String> feedSubmissionIdList = new ArrayList<>();
        for (int i = 0; i < feedSubmissionInfoDtoList.size(); i++) {
            feedSubmissionIdList.add(feedSubmissionInfoDtoList.get(i).getFeedSubmissionId());
        }

        // 当所有状态都为_DONE_时，执行下一步
        boolean b = false;
        int count;
        while (!b) {
            try {
                // 设置睡眠的时间 2 分钟
                Thread.sleep(2 * 60 * 1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count = 0;
            feedSubmissionInfoDtoList = getFeedSubmissionListAsync(uploadId, serviceURL, merchantId, sellerDevAuthToken, feedSubmissionIdList);
            for (int i = 0; i < feedSubmissionInfoDtoList.size(); i++) {
                if (feedSubmissionInfoDtoList.get(0).getFeedProcessingStatus() == 1) {
                    count++;
                    if (count == 5) {
                        b = true;
                        break;
                    }
                }
            }
        }

        // 总状态改为正在上传
        updateFeedUpload(uploadId, feedSubmissionInfoDtoList, 1);

        // 获取报告
        List<FeedSubmissionResultDto> feedSubmissionResultDtos;
        while (true) {
            feedSubmissionResultDtos = new ArrayList<>();
            feedSubmissionResultDtos = getFeedSubmissionResultAsync(uploadId, fileStoragePath, serviceURL, merchantId, sellerDevAuthToken, feedSubmissionInfoDtoList);

            if (feedSubmissionResultDtos != null || feedSubmissionResultDtos.size() != feedSubmissionInfoDtoList.size()) {
                break;
            }

            try {
                // 设置睡眠的时间 2 分钟
                Thread.sleep(2 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        String tempPath;
        List<Integer> typeStatus = new ArrayList();
        for (int i = 0; i < feedSubmissionInfoDtoList.size(); i++) {
            // 返回结果路径
            tempPath = "";
            String submissionId = feedSubmissionInfoDtoList.get(i).getFeedSubmissionId();
            Calendar calendar = Calendar.getInstance();
            String year = calendar.get(Calendar.YEAR) + "/";
            String month = (calendar.get(Calendar.MONTH)) + 1 + "/";
            tempPath = fileStoragePath + year + month + "FeedSubmissionResult/" + submissionId + "_SubmissionResult.xml";

            for (int j = 0; j < feedSubmissionResultDtos.size(); j++) {
                if (submissionId.equals(feedSubmissionResultDtos.get(j).getFeedSubmissionId())) {

                    // 解析xml
                    AnalysisFeedSubmissionResultDto analysisFeedSubmissionResultDto = XMLUtil.analysisFeedSubmissionResult(tempPath);

                    int tempStatus = judgementState(analysisFeedSubmissionResultDto);
                    String tempResultXml = analysisFeedSubmissionResultDto.getMessageContent();
                    typeStatus.add(tempStatus);

                    ResultXmlEntity resultXmlEntity = new ResultXmlEntity();
                    resultXmlEntity.setUploadId(uploadId);
                    resultXmlEntity.setCreationTime(new Date());
                    resultXmlEntity.setState(tempStatus);
                    resultXmlEntity.setXml(tempResultXml);
                    // 分辨上传类型，在不同的字段中插入xml结果
                    ResultXmlEntity resultXmlEntity1;
                    switch (feedSubmissionInfoDtoList.get(i).getFeedType()) {
                        case "_POST_PRODUCT_DATA_":
                            resultXmlEntity1 = isExist(uploadId, "基本信息");
                            if (resultXmlEntity1 != null) {
                                resultXmlEntity.setId(resultXmlEntity1.getId());
                            }
                            resultXmlEntity.setType("基本信息");
                            updateUploadEntity.setProductsResultStatus(tempStatus);
                            break;
                        case "_POST_PRODUCT_RELATIONSHIP_DATA_":
                            resultXmlEntity1 = isExist(uploadId, "关系");
                            if (resultXmlEntity1 != null) {
                                resultXmlEntity.setId(resultXmlEntity1.getId());
                            }
                            resultXmlEntity.setType("关系");
                            updateUploadEntity.setProductsResultStatus(tempStatus);
                            break;
                        case "_POST_PRODUCT_IMAGE_DATA_":
                            resultXmlEntity1 = isExist(uploadId, "图片");
                            if (resultXmlEntity1 != null) {
                                resultXmlEntity.setId(resultXmlEntity1.getId());
                            }
                            resultXmlEntity.setType("图片");
                            updateUploadEntity.setProductsResultStatus(tempStatus);
                            break;
                        case "_POST_INVENTORY_AVAILABILITY_DATA_":
                            resultXmlEntity1 = isExist(uploadId, "库存");
                            if (resultXmlEntity1 != null) {
                                resultXmlEntity.setId(resultXmlEntity1.getId());
                            }
                            resultXmlEntity.setType("库存");
                            updateUploadEntity.setProductsResultStatus(tempStatus);
                            break;
                        case "_POST_PRODUCT_PRICING_DATA_":
                            resultXmlEntity1 = isExist(uploadId, "价格");
                            if (resultXmlEntity1 != null) {
                                resultXmlEntity.setId(resultXmlEntity1.getId());
                            }
                            resultXmlEntity.setType("价格");
                            updateUploadEntity.setProductsResultStatus(tempStatus);
                            break;
                    }
                    resultXmlService.insertOrUpdate(resultXmlEntity);
                    feedSubmissionResultDtos.get(j).setResultXmlPath(tempPath);
                    feedSubmissionResultDtos.get(j).setFeedType(feedSubmissionInfoDtoList.get(i).getFeedType());
                }
            }
        }

        // 处理总状态
        updateUploadEntity.setUploadState(judgingTheTotalState(typeStatus));

        //保存xml结果，保存状态
        uploadService.updateById(updateUploadEntity);

    }

    @Override
    public FeedSubmissionInfoDto submitProductFeed(Long uploadId, String serviceURL, String merchantId, String sellerDevAuthToken, String feedType, String filePath, List<String> marketplaceIdList) {

        List<SubmitFeedRequest> submitFeedRequestList = new ArrayList<>();

        MarketplaceWebService service = getService(serviceURL);

        IdList marketplaces = new IdList(marketplaceIdList);

        SubmitFeedRequest request = new SubmitFeedRequest();
        request.setMerchant(merchantId);
        request.setMWSAuthToken(sellerDevAuthToken);
        request.setMarketplaceIdList(marketplaces);
        request.setFeedType(feedType);
        FileInputStream fileInputStream = null;
        String md5 = "";
        try {
            fileInputStream = new FileInputStream(filePath);
            md5 = ContentMD5Util.computeContentMD5HeaderValue(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        request.setContentMD5(md5);
        request.setFeedContent(fileInputStream);
        submitFeedRequestList.add(request);
        List<FeedSubmissionInfoDto> feedSubmissionInfoDtoList = invokeSubmitFeedAsync(uploadId, service, submitFeedRequestList);
        while (feedSubmissionInfoDtoList.size() == 0) {
            feedSubmissionInfoDtoList = invokeSubmitFeedAsync(uploadId, service, submitFeedRequestList);
        }
        return feedSubmissionInfoDtoList.get(0);
    }

    @Override
    public List<FeedSubmissionInfoDto> submitFeedAsync(Long uploadId, String serviceURL, String merchantId, String sellerDevAuthToken, Map<String, String> uploadTypeMap, Map<String, String> filePathMap, List<String> marketplaceIdList) {
        IdList marketplaces = new IdList(marketplaceIdList);
        MarketplaceWebService service = getAsyncService(serviceURL);
        List<SubmitFeedRequest> submitFeedRequestList = new ArrayList<>();
        for (int i = 1; i < uploadTypeMap.size(); i++) {
            if (filePathMap.containsKey(String.valueOf(i))) {
                SubmitFeedRequest submitFeedRequest = new SubmitFeedRequest();
                submitFeedRequest.setMerchant(merchantId);
                submitFeedRequest.setMWSAuthToken(sellerDevAuthToken);
                submitFeedRequest.setMarketplaceIdList(marketplaces);
                submitFeedRequest.setFeedType(uploadTypeMap.get(String.valueOf(i)));
                FileInputStream fileInputStream = null;
                String md5 = null;
                try {
                    fileInputStream = new FileInputStream(filePathMap.get(String.valueOf(i)));
                    md5 = ContentMD5Util.computeContentMD5HeaderValue(fileInputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                submitFeedRequest.setContentMD5(md5);
                submitFeedRequest.setFeedContent(fileInputStream);
                submitFeedRequestList.add(submitFeedRequest);
            }
        }

        return invokeSubmitFeedAsync(uploadId, service, submitFeedRequestList);
    }

    @Override
    public List<FeedSubmissionInfoDto> invokeSubmitFeedAsync(Long uploadId, MarketplaceWebService service, List<SubmitFeedRequest> requests) {
        List<FeedSubmissionInfoDto> feedSubmissionInfoDtoList = new ArrayList<>();
        List<Future<SubmitFeedResponse>> responses = new ArrayList<Future<SubmitFeedResponse>>();
        for (SubmitFeedRequest request : requests) {
            responses.add(service.submitFeedAsync(request));
        }
        for (Future<SubmitFeedResponse> future : responses) {
            while (!future.isDone()) {
                Thread.yield();
            }
            try {
                SubmitFeedResponse response = future.get();
                if (response.isSetSubmitFeedResult()) {
                    if (response.getSubmitFeedResult().isSetFeedSubmissionInfo()) {
                        FeedSubmissionInfo feedSubmissionInfo = response.getSubmitFeedResult().getFeedSubmissionInfo();
                        FeedSubmissionInfoDto feedSubmissionInfoDto = new FeedSubmissionInfoDto();
                        feedSubmissionInfoDto.setUploadId(uploadId);
                        if (feedSubmissionInfo.isSetFeedSubmissionId()) {
                            feedSubmissionInfoDto.setFeedSubmissionId(feedSubmissionInfo.getFeedSubmissionId());
                        }
                        if (feedSubmissionInfo.isSetFeedType()) {
                            feedSubmissionInfoDto.setFeedType(feedSubmissionInfo.getFeedType());
                        }
                        if (feedSubmissionInfo.isSetFeedProcessingStatus()) {
                            int temp = 0;
                            switch (feedSubmissionInfo.getFeedProcessingStatus()) {
                                case "_DONE_":
                                    temp = 1;
                                    break;
                                case "_SUBMITTED_":
                                    temp = 0;
                                    break;
                                case "_IN_SAFETY_NET_":
                                    temp = 3;
                                    break;
                                case "_IN_PROGRESS_":
                                    temp = 0;
                                    break;
                                case "_CANCELLED_":
                                    temp = 3;
                                    break;
                                case "_AWAITING_ASYNCHRONOUS_REPLY_":
                                    temp = 3;
                                    break;
                                case "_UNCONFIRMED_":
                                    temp = 0;
                                    break;
                                default:
                                    break;
                            }
                            feedSubmissionInfoDto.setFeedProcessingStatus(temp);
                        }
                        feedSubmissionInfoDtoList.add(feedSubmissionInfoDto);
                    }
                }
            } catch (Exception e) {
                if (e.getCause() instanceof MarketplaceWebServiceException) {
                    MarketplaceWebServiceException exception = MarketplaceWebServiceException.class.cast(e.getCause());
                    System.out.println("Caught Exception: " + exception.getMessage());
                    System.out.println("Response Status Code: " + exception.getStatusCode());
                    System.out.println("Error Code: " + exception.getErrorCode());
                    System.out.println("Error Type: " + exception.getErrorType());
                    System.out.println("Request ID: " + exception.getRequestId());
                    System.out.print("XML: " + exception.getXML());
                    System.out.println("ResponseHeaderMetadata: " + exception.getResponseHeaderMetadata());
                } else {
                    e.printStackTrace();
                }
            }
        }
        return feedSubmissionInfoDtoList;
    }

    @Override
    public List<FeedSubmissionResultDto> getFeedSubmissionResultAsync(Long uploadId, String path, String serviceURL, String merchantId, String sellerDevAuthToken, List<FeedSubmissionInfoDto> feedSubmissionInfoDtoList) {
        Calendar calendar = Calendar.getInstance();
        String year = calendar.get(Calendar.YEAR) + "/";
        String month = (calendar.get(Calendar.MONTH)) + 1 + "/";
        String tempPath = fileStoragePath + year + month + "FeedSubmissionResult/";
        File file = new File(tempPath);
        if (!file.exists()) {
            file.mkdirs();
        }


        MarketplaceWebService service = getAsyncService(serviceURL);
        List<GetFeedSubmissionResultRequest> requests = new ArrayList<>();
        for (int i = 0; i < feedSubmissionInfoDtoList.size(); i++) {
            GetFeedSubmissionResultRequest request = new GetFeedSubmissionResultRequest();
            request.setMerchant(merchantId);
            request.setMWSAuthToken(sellerDevAuthToken);
            request.setFeedSubmissionId(feedSubmissionInfoDtoList.get(i).getFeedSubmissionId());
            OutputStream processingResult;
            String tempPath2 = tempPath + feedSubmissionInfoDtoList.get(i).getFeedSubmissionId() + "_SubmissionResult.xml";
            try {
                processingResult = new FileOutputStream(tempPath2);
                request.setFeedSubmissionResultOutputStream(processingResult);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            requests.add(request);
        }
        return invokeGetFeedSubmissionResult(uploadId, service, requests);
    }

    @Override
    public List<FeedSubmissionResultDto> invokeGetFeedSubmissionResult(Long uploadId, MarketplaceWebService service, List<GetFeedSubmissionResultRequest> requests) {
        List<FeedSubmissionResultDto> feedSubmissionResultDtos = new ArrayList<>();
        List<Future<GetFeedSubmissionResultResponse>> responses = new ArrayList<Future<GetFeedSubmissionResultResponse>>();
        for (GetFeedSubmissionResultRequest request : requests) {
            responses.add(service.getFeedSubmissionResultAsync(request));
        }
        for (Future<GetFeedSubmissionResultResponse> future : responses) {
            while (!future.isDone()) {
                Thread.yield();
            }
            try {
                GetFeedSubmissionResultResponse response = future.get();
                String MD5Checksum = response.getGetFeedSubmissionResultResult().getMD5Checksum();
                GetFeedSubmissionResultRequest originalRequest = requests.get(responses.indexOf(future));
                FeedSubmissionResultDto feedSubmissionResultDto = new FeedSubmissionResultDto();
                feedSubmissionResultDto.setUploadId(uploadId);
                feedSubmissionResultDto.setMd5Checksum(MD5Checksum);
                feedSubmissionResultDto.setFeedSubmissionId(originalRequest.getFeedSubmissionId());
                feedSubmissionResultDtos.add(feedSubmissionResultDto);
            } catch (Exception e) {
                if (e.getCause() instanceof MarketplaceWebServiceException) {
                    MarketplaceWebServiceException exception = MarketplaceWebServiceException.class.cast(e.getCause());
                    System.out.println("Caught Exception: " + exception.getMessage());
                    System.out.println("Response Status Code: " + exception.getStatusCode());
                    System.out.println("Error Code: " + exception.getErrorCode());
                    System.out.println("Error Type: " + exception.getErrorType());
                    System.out.println("Request ID: " + exception.getRequestId());
                    System.out.print("XML: " + exception.getXML());
                    System.out.println("ResponseHeaderMetadata: " + exception.getResponseHeaderMetadata());
                } else {
                    e.printStackTrace();
                }
            }
        }
        return feedSubmissionResultDtos;
    }

    @Override
    public List<FeedSubmissionInfoDto> getFeedSubmissionListAsync(Long uploadId, String serviceURL, String merchantId, String sellerDevAuthToken, List<String> feedSubmissionIdList) {
        IdList idList = new IdList(feedSubmissionIdList);
        MarketplaceWebService service = getAsyncService(serviceURL);
        List<GetFeedSubmissionListRequest> requests = new ArrayList<>();
        for (int i = 0; i < feedSubmissionIdList.size(); i++) {
            GetFeedSubmissionListRequest request = new GetFeedSubmissionListRequest();
            request.setMerchant(merchantId);
            request.setFeedSubmissionIdList(idList);
            request.setMWSAuthToken(sellerDevAuthToken);
            requests.add(request);
        }

        return invokeGetFeedSubmissionList(uploadId, service, requests);
    }

    @Override
    public List<FeedSubmissionInfoDto> invokeGetFeedSubmissionList(Long uploadId, MarketplaceWebService service, List<GetFeedSubmissionListRequest> requests) {
        List<FeedSubmissionInfoDto> feedSubmissionInfoDtoList = new ArrayList<>();
        List<Future<GetFeedSubmissionListResponse>> responses = new ArrayList<Future<GetFeedSubmissionListResponse>>();
        for (GetFeedSubmissionListRequest request : requests) {
            responses.add(service.getFeedSubmissionListAsync(request));
        }
        for (Future<GetFeedSubmissionListResponse> future : responses) {
            while (!future.isDone()) {
                Thread.yield();
            }
            try {
                GetFeedSubmissionListResponse response = future.get();
                if (response.isSetGetFeedSubmissionListResult()) {
                    GetFeedSubmissionListResult getFeedSubmissionListResult = response.getGetFeedSubmissionListResult();
                    java.util.List<FeedSubmissionInfo> feedSubmissionInfoList = getFeedSubmissionListResult.getFeedSubmissionInfoList();
                    for (FeedSubmissionInfo feedSubmissionInfo : feedSubmissionInfoList) {
                        FeedSubmissionInfoDto feedSubmissionInfoDto = new FeedSubmissionInfoDto();
                        feedSubmissionInfoDto.setUploadId(uploadId);
                        if (feedSubmissionInfo.isSetFeedSubmissionId()) {
                            feedSubmissionInfoDto.setFeedSubmissionId(feedSubmissionInfo.getFeedSubmissionId());
                        }
                        if (feedSubmissionInfo.isSetFeedType()) {
                            feedSubmissionInfoDto.setFeedType(feedSubmissionInfo.getFeedType());
                        }
                        if (feedSubmissionInfo.isSetFeedProcessingStatus()) {
                            int temp = 0;
                            switch (feedSubmissionInfo.getFeedProcessingStatus()) {
                                case "_DONE_":
                                    temp = 1;
                                    break;
                                case "_SUBMITTED_":
                                    temp = 0;
                                    break;
                                case "_IN_SAFETY_NET_":
                                    temp = 3;
                                    break;
                                case "_IN_PROGRESS_":
                                    temp = 0;
                                    break;
                                case "_CANCELLED_":
                                    temp = 3;
                                    break;
                                case "_AWAITING_ASYNCHRONOUS_REPLY_":
                                    temp = 3;
                                    break;
                                case "_UNCONFIRMED_":
                                    temp = 0;
                                    break;
                                default:
                                    break;
                            }
                            feedSubmissionInfoDto.setFeedProcessingStatus(temp);
                        }
                        feedSubmissionInfoDtoList.add(feedSubmissionInfoDto);
                    }
                }
            } catch (Exception e) {
                if (e.getCause() instanceof MarketplaceWebServiceException) {
                    MarketplaceWebServiceException exception = MarketplaceWebServiceException.class.cast(e.getCause());
                    System.out.println("Caught Exception: " + exception.getMessage());
                    System.out.println("Response Status Code: " + exception.getStatusCode());
                    System.out.println("Error Code: " + exception.getErrorCode());
                    System.out.println("Error Type: " + exception.getErrorType());
                    System.out.println("Request ID: " + exception.getRequestId());
                    System.out.print("XML: " + exception.getXML());
                    System.out.println("ResponseHeaderMetadata: " + exception.getResponseHeaderMetadata());
                } else {
                    e.printStackTrace();
                }
            }
        }
        return feedSubmissionInfoDtoList;
    }

    @Override
    public MarketplaceWebService getService(String serviceURL) {
        MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();
        config.setServiceURL(serviceURL);
        config.setConnectionTimeout(120000);
        config.setSoTimeout(120000);
        MarketplaceWebService service = new MarketplaceWebServiceClient(accessKey, secretKey, appName, appVersion, config);
        return service;
    }

    @Override
    public MarketplaceWebService getAsyncService(String serviceURL) {
        MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();
        config.setServiceURL(serviceURL);
        config.setMaxAsyncThreads(35);
        config.setConnectionTimeout(120000);
        config.setSoTimeout(120000);
        MarketplaceWebService service = new MarketplaceWebServiceClient(accessKey, secretKey, appName, appVersion, config);
        return service;
    }

    @Override
    public void updateFeedUpload(Long uploadId, List<FeedSubmissionInfoDto> feedSubmissionInfoDtoList, int uploadState) {
        UploadEntity uploadEntity = new UploadEntity();
        uploadEntity.setUploadId(uploadId);
        // 总状态改变
        uploadEntity.setUploadState(uploadState);
        uploadEntity.setUpdateTime(new Date());
        uploadService.updateById(uploadEntity);
        // 分状态改变
        for (int i = 0; i < feedSubmissionInfoDtoList.size(); i++) {
            String feedSubmissionId = feedSubmissionInfoDtoList.get(i).getFeedSubmissionId();
            Integer status = feedSubmissionInfoDtoList.get(i).getFeedProcessingStatus();
            switch (feedSubmissionInfoDtoList.get(i).getFeedType()) {
                case "_POST_PRODUCT_DATA_":
                    uploadEntity.setProductsSubmitId(feedSubmissionId);
                    uploadEntity.setProductsResultStatus(status);
                    break;
                case "_POST_PRODUCT_RELATIONSHIP_DATA_":
                    uploadEntity.setRelationshipsSubmitId(feedSubmissionId);
                    uploadEntity.setRelationshipsResultStatus(status);
                    break;
                case "_POST_PRODUCT_IMAGE_DATA_":
                    uploadEntity.setImagesSubmitId(feedSubmissionId);
                    uploadEntity.setImagesResultStatus(status);
                    break;
                case "_POST_INVENTORY_AVAILABILITY_DATA_":
                    uploadEntity.setInventorySubmitId(feedSubmissionId);
                    uploadEntity.setInventoryResultStatus(status);
                    break;
                case "_POST_PRODUCT_PRICING_DATA_":
                    uploadEntity.setPricesSubmitId(feedSubmissionId);
                    uploadEntity.setPricesResultStatus(status);
                    break;
                default:
                    break;
            }
        }
        uploadService.updateById(uploadEntity);
    }

    @Override
    public int judgementState(AnalysisFeedSubmissionResultDto analysisFeedSubmissionResultDto) {
        int temp = 3;
        if (!analysisFeedSubmissionResultDto.getMessagesWithError().equals(0)) {
            temp = 3;
        } else {
            if (analysisFeedSubmissionResultDto.getMessagesProcessed().equals(analysisFeedSubmissionResultDto.getMessagesSuccessful())) {
                if (!analysisFeedSubmissionResultDto.getMessagesWithWarning().equals(0)) {
                    temp = 4;
                } else if (!analysisFeedSubmissionResultDto.getMessagesWithWarning().equals(1)) {
                    temp = 1;
                }
                temp = 2;
            }
        }
        return temp;
    }

    @Override
    public int judgingTheTotalState(List<Integer> substate) {
        int temp;
        if (substate.contains(3)) {
            temp = 3;
        } else if (substate.contains(4)) {
            temp = 4;
        } else if (substate.contains(1)) {
            temp = 1;
        } else if (substate.contains(0)) {
            temp = 0;
        } else {
            temp = 2;
        }
        return temp;
    }

    @Override
    public ResultXmlEntity isExist(Long uploadId, String tpye) {
        EntityWrapper<ResultXmlEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("upload_id", uploadId);
        wrapper.eq("type", tpye);
        ResultXmlEntity resultXmlEntity = resultXmlService.selectOne(wrapper);
        return resultXmlEntity;
    }

    @Override
    public String switchCountry(String templateName, Long uploadId, String merchantIdentifierText, List<ProductsEntity> productsList, String countryCode) {
        String filePath = "";
        switch (templateName) {
            // 服装/ProductClothing
            case "ProductClothing":
                filePath = generateProductXML.generateProductXMLByClothing(uploadId, merchantIdentifierText, productsList, countryCode);
                break;
            // 汽车配件/AutoAccessory
            case "AutoAccessory":


                // 宝宝/Baby
            case "Baby":


                // 美妆/Beauty
            case "Beauty":


                // 相机照片/CameraPhoto
            case "CameraPhoto":


                // 服装饰品/ClothingAccessories
            case "ClothingAccessories":


                // 电脑/Computers
            case "Computers":


                // 消费类电子产品/CE
            case "CE":


                // 美食/Gourmet
            case "Gourmet":


                // 食品和饮料/FoodAndBeverages
            case "FoodAndBeverages":


                // 健康/Health
            case "Health":


                // 家居装修/HomeImprovement
            case "HomeImprovement":


                // 食品服务和门卫，卫生与安全/FoodServiceAndJanSan
            case "FoodServiceAndJanSan":


                // 实验室和科学用品/LabSupplies
            case "LabSupplies":


                // 电力传输/PowerTransmission
            case "PowerTransmission":


                // 原材料/RawMaterials
            case "RawMaterials":


                // 首饰/Jewelry
            case "Jewelry":


                // 大家电/LargeAppliances
            case "LargeAppliances":


                // 灯/Lighting
            case "Lighting":


                // 乐器/MusicalInstruments
            case "MusicalInstruments":


                // 宠物用品/PetSupplies
            case "PetSupplies":


                // 体育用品/Sports
            case "Sports":


                // 体育纪念品/SportsMemorabilia
            case "SportsMemorabilia":


                // 轮胎和车轮/TiresAndWheels
            case "TiresAndWheels":


                // 工具/Tools
            case "Tools":


                // 玩具和游戏/Toys
            case "Toys":


                // 无线/Wireless
            case "Wireless":
                // 除了衣服，剩下的分类都用默认的模板
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;
            default:
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
        }
        return filePath;
    }
}