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
import io.renren.modules.amazon.dto.ResultXMLDto;
import io.renren.modules.amazon.entity.AmazonGrantEntity;
import io.renren.modules.amazon.entity.AmazonGrantShopEntity;
import io.renren.modules.amazon.entity.ResultXmlEntity;
import io.renren.modules.amazon.service.AmazonGrantService;
import io.renren.modules.amazon.service.AmazonGrantShopService;
import io.renren.modules.amazon.service.ResultXmlService;
import io.renren.modules.amazon.service.SubmitFeedService;
import io.renren.modules.amazon.util.ContentMD5Util;
import io.renren.modules.amazon.util.XMLUtil;
import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.entity.UploadEntity;
import io.renren.modules.product.entity.VariantsInfoEntity;
import io.renren.modules.product.service.*;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@Component
public class SubmitFeedServiceImpl implements SubmitFeedService {

    @Autowired
    private ProductsService productsService;

    @Autowired
    private VariantsInfoService variantsInfoService;

    @Autowired
    private AmazonGrantService amazonGrantService;

    @Autowired
    private AmazonGrantShopService amazonGrantShopService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private ResultXmlService resultXmlService;

    @Autowired
    private GenerateProductXML generateProductXML;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private SysUserService userService;

    // 欧洲
    @Value(("${mws-config.eu-access-key}"))
    private String euAccessKey;

    @Value(("${mws-config.eu-secret-key}"))
    private String euSecretKey;

    // 日本
    @Value(("${mws-config.jp-access-key}"))
    private String jpAccessKey;

    @Value(("${mws-config.jp-secret-key}"))
    private String jpSecretKey;

    // 北美
    @Value(("${mws-config.na-access-key}"))
    private String naAccessKey;

    @Value(("${mws-config.na-secret-key}"))
    private String naSecretKey;

    // 澳大利亚
    @Value(("${mws-config.au-access-key}"))
    private String auAccessKey;

    @Value(("${mws-config.au-secret-key}"))
    private String auSecretKey;

    @Value(("${mws-config.app-name}"))
    private String appName;

    @Value(("${mws-config.app-version}"))
    private String appVersion;

    @Value(("${file.path}"))
    private String fileStoragePath;

    private static Map<String, String> uploadTypeMap;

    private static <E>  List<E> transferArrayToList(E[] array){
        List<E> transferedList = new ArrayList<>();
        Arrays.stream(array).forEach(arr -> transferedList.add(arr));
        return transferedList;
    }

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
        //上传id
        Long uploadId = uploadEntity.getUploadId();

        System.out.println("用户" + uploadEntity.getUserId() + "，上传编号为" + uploadId + "的submitFeed方法子线程被执行！");

        uploadEntity.setUploadState(1);
        uploadEntity.setUpdateTime(new Date());
        uploadService.updateById(uploadEntity);

        long startTime = System.currentTimeMillis();
        long endTime;
        try{
            // 商品列表
            List<ProductsEntity> productsEntityList;
            if (uploadEntity.getUploadProductsList() != null) {
                productsEntityList = uploadEntity.getUploadProductsList();
            } else {
                List<String> ids = transferArrayToList(uploadEntity.getUploadProductsIds().split(","));
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
            List<String> operateItemList = new ArrayList<String>();
            operateItemList = transferArrayToList(operateItemStr);
            // 模板名称
            String templateName = "";
            if (uploadEntity.getAmazonTemplateId() == 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("template_display_name", uploadEntity.getAmazonTemplate());
                templateName = templateService.selectByMap(map).get(0).getTemplateName();
            } else {
                templateName = templateService.selectById(uploadEntity.getAmazonTemplateId()).getTemplateName();
            }
            boolean haveV = false;
            // 判断是否有变体
            int vCount = 0;
            for(ProductsEntity pro : productsEntityList){
                vCount = variantsInfoService.selectCount(new EntityWrapper<VariantsInfoEntity>().eq("product_id",pro.getProductId()));
                if(vCount > 0){
                    haveV = true;
                    break;
                }
            }
            if(!haveV){
                operateItemList.remove("1");
//                uploadTypeMap.remove("1");
            }




            // 生成xml文件路径
            Map<String, String> filePathMap = new HashMap<>();
            for (int i = 0; i < operateItemList.size(); i++) {
                switch (operateItemList.get(i)) {
                    // 0 基本信息
                    case "0":
                        String productPath = switchCountry(templateName, uploadId, merchantId, productsEntityList, countryCode);
                        filePathMap.put("0", productPath);
                        break;
                    // 1 关系
                    case "1":
                        String relationshipsPath = generateProductXML.generateRelationshipsXML(productsEntityList, merchantId, uploadId);
                        filePathMap.put("1", relationshipsPath);
                        break;
                    // 2 图片
                    case "2":
                        String imagesPath = generateProductXML.generateImagesXML(productsEntityList, merchantId, uploadId);
                        filePathMap.put("2", imagesPath);
                        break;
                    // 3 库存
                    case "3":
                        String inventoryPath = generateProductXML.generateInventoryXML(productsEntityList, merchantId, uploadId);
                        filePathMap.put("3", inventoryPath);
                        break;
                    // 4 价格
                    case "4":
                        String pricesPath = generateProductXML.generatePricesXML(countryCode, productsEntityList, merchantId, uploadId);
                        filePathMap.put("4", pricesPath);
                        break;
                    default:
                }
            }

            FeedSubmissionInfoDto productFeedSubmissionInfoDto = null;

            // 0 是产品基本信息xml
            if (filePathMap.containsKey("0")) {
                // 产品信息上传
                productFeedSubmissionInfoDto = submitProductFeed(uploadId, serviceURL, merchantId, sellerDevAuthToken, uploadTypeMap.get("0"), filePathMap.get("0"), marketplaceIdList);
                if(productFeedSubmissionInfoDto != null){
                    uploadEntity.setProductsResultStatus(1);
                    uploadEntity.setProductsSubmitId(productFeedSubmissionInfoDto.getFeedSubmissionId());
                    uploadService.updateById(uploadEntity);
                    //使用FeedSubmissionId获取的亚马逊对于xml的处理状态
                    while (true) {
                        try {
                            // 设置睡眠的时间 120 秒
                            Thread.sleep(2 * 60 * 1000);

                            List<String> feedSubmissionList = new ArrayList<>();
                            feedSubmissionList.add(productFeedSubmissionInfoDto.getFeedSubmissionId());
                            productFeedSubmissionInfoDto = getFeedSubmissionListAsync(uploadId, serviceURL, merchantId, sellerDevAuthToken, feedSubmissionList).get(0);
                            // 成功
                            if (productFeedSubmissionInfoDto.getFeedProcessingStatus().equals(1)) {
                                break;
                            } else if (productFeedSubmissionInfoDto.getFeedProcessingStatus().equals(3)) {
                                // 出现如下三种情况，总状态变失败。
                                List<FeedSubmissionInfoDto> tempList = new ArrayList<>();
                                tempList.add(productFeedSubmissionInfoDto);
                                updateFeedUpload(uploadId, tempList, 3);
                                //判断用户是否有等待上传线程
                                UploadEntity currentUpload = uploadService.selectOne(new EntityWrapper<UploadEntity>().eq("upload_state", 0).eq("user_id",uploadEntity.getUserId()));
                                if(currentUpload == null){
                                    System.out.println("-------------------当前上传项:" + uploadEntity.getUploadId() + "结束。" + "用户：" + userService.selectById(uploadEntity.getUserId()).getUsername() + "当前没有等待上传项，线程结束-------------------");
                                    return;
                                }else{
                                    System.out.println("-------------------当前上传项:" + uploadEntity.getUploadId() + "结束。" + "用户：" + userService.selectById(uploadEntity.getUserId()).getUsername() + "当前有等待上传项，线程继续-------------------");
                                    submitFeed(currentUpload);
                                    return;
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    List<FeedSubmissionInfoDto> oneList = new ArrayList<FeedSubmissionInfoDto>();
                    oneList.add(productFeedSubmissionInfoDto);
                    List<FeedSubmissionResultDto> oneResultList = new ArrayList<FeedSubmissionResultDto>();
                    while (true) {
                        oneResultList = getFeedSubmissionResultAsync(uploadId, fileStoragePath, serviceURL, merchantId, sellerDevAuthToken, oneList);
                        if(oneResultList.size() > 0 && oneResultList.get(0) != null){
                            String submissionId = productFeedSubmissionInfoDto.getFeedSubmissionId();
                            Calendar calendar = Calendar.getInstance();
                            String year = calendar.get(Calendar.YEAR) + "/";
                            String month = (calendar.get(Calendar.MONTH)) + 1 + "/";
                            // 返回结果路径
                            String tempPath = fileStoragePath + year + month + "FeedSubmissionResult/" + submissionId + "_SubmissionResult.xml";
                            AnalysisFeedSubmissionResultDto analysisFeedSubmissionResultDto = XMLUtil.analysisFeedSubmissionResult(tempPath);
                            int tempStatus = 1;
                            tempStatus = judgementState(analysisFeedSubmissionResultDto);
                            uploadEntity.setProductsResultStatus(tempStatus);
                            uploadService.updateById(uploadEntity);
                            List<ResultXMLDto> resultXMLDtoList = analysisFeedSubmissionResultDto.getResultXMLDtoList();
                            if (resultXMLDtoList.size() != 0) {
                                for (int k = 0; k < resultXMLDtoList.size(); k++) {
                                    ResultXmlEntity resultXmlEntity = new ResultXmlEntity();
                                    resultXmlEntity.setSku(resultXMLDtoList.get(k).getSku());
                                    resultXmlEntity.setProductId(productsService.queryIdBySku(resultXMLDtoList.get(k).getSku()));
                                    resultXmlEntity.setUploadId(uploadId);
                                    resultXmlEntity.setType("基本信息");
                                    resultXmlEntity.setState(tempStatus);
                                    resultXmlEntity.setResult(resultXMLDtoList.get(k).getResultDescription());
                                    resultXmlEntity.setResultType(resultXMLDtoList.get(k).getResultCode());
                                    resultXmlEntity.setResultCode(resultXMLDtoList.get(k).getResultMessageCode());
                                    resultXmlEntity.setCreationTime(new Date());
                                    resultXmlService.insert(resultXmlEntity);
                                }
                            }
                            if(tempStatus == 3){
                                List<FeedSubmissionInfoDto> tempList = new ArrayList<>();
                                tempList.add(productFeedSubmissionInfoDto);
                                updateFeedUpload(uploadId, tempList, 3);
                                //判断用户是否有等待上传线程
                                UploadEntity currentUpload = uploadService.selectOne(new EntityWrapper<UploadEntity>().eq("upload_state", 0).eq("user_id",uploadEntity.getUserId()));
                                if(currentUpload == null){
                                    System.out.println("-------------------当前上传项:" + uploadEntity.getUploadId() + "结束。" + "用户：" + userService.selectById(uploadEntity.getUserId()).getUsername() + "当前没有等待上传项，线程结束-------------------");
                                    return;
                                }else{
                                    System.out.println("-------------------当前上传项:" + uploadEntity.getUploadId() + "结束。" + "用户：" + userService.selectById(uploadEntity.getUserId()).getUsername() + "当前有等待上传项，线程继续-------------------");
                                    submitFeed(currentUpload);
                                    return;
                                }
                            }
                            break;
                        }

                        // 设置睡眠的时间 60 秒
                        try {
                            Thread.sleep(2 * 60 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    // 处理总状态
                    uploadEntity.setUploadState(3);
                    uploadEntity.setUpdateTime(new Date());
                    //保存xml结果，保存状态
                    uploadService.updateById(uploadEntity);
                    ResultXmlEntity resultInfo = new ResultXmlEntity();
                    resultInfo.setUploadId(uploadId);
                    resultInfo.setType("授权");
                    resultInfo.setState(3);
                    resultInfo.setResult("授权有误，请核实后再次尝试");
                    resultInfo.setResultType("错误");
                    resultInfo.setResultCode("001");
                    resultInfo.setCreationTime(new Date());
                    resultXmlService.insert(resultInfo);
                    //判断用户是否有等待上传线程
                    UploadEntity currentUpload = uploadService.selectOne(new EntityWrapper<UploadEntity>().eq("upload_state", 0).eq("user_id",uploadEntity.getUserId()));
                    if(currentUpload == null){
                        System.out.println("-------------------当前上传项:" + uploadEntity.getUploadId() + "结束。" + "用户：" + userService.selectById(uploadEntity.getUserId()).getUsername() + "当前没有等待上传项，线程结束-------------------");
                        return;
                    }else{
                        System.out.println("-------------------当前上传项:" + uploadEntity.getUploadId() + "结束。" + "用户：" + userService.selectById(uploadEntity.getUserId()).getUsername() + "当前有等待上传项，线程继续-------------------");
                        submitFeed(currentUpload);
                        return;
                    }
                }

            }

            List<FeedSubmissionInfoDto> feedSubmissionInfoDtoList;
            // 剩余xml的上传
            while (true) {

                feedSubmissionInfoDtoList = submitFeedAsync(uploadId, serviceURL, merchantId, sellerDevAuthToken, uploadTypeMap, filePathMap, marketplaceIdList);

//                if (productFeedSubmissionInfoDto != null) {
//                    feedSubmissionInfoDtoList.add(productFeedSubmissionInfoDto);
//                }
                operateItemList.remove("0");
                if (feedSubmissionInfoDtoList.size() == operateItemList.size() ) {
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
                    Thread.sleep(5 * 60 * 1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count = 0;
                feedSubmissionInfoDtoList = getFeedSubmissionListAsync(uploadId, serviceURL, merchantId, sellerDevAuthToken, feedSubmissionIdList);
                for (int i = 0; i < feedSubmissionInfoDtoList.size(); i++) {
                    if (feedSubmissionInfoDtoList.get(0).getFeedProcessingStatus() == 1) {
                        count++;
                        if (count == feedSubmissionInfoDtoList.size()) {
                            b = true;
                            break;
                        }
                    }
                }
            }

            // 总状态改为正在上传,并改子状态
            updateFeedUpload(uploadId, feedSubmissionInfoDtoList, 1);

            //留个纪念
           /* try {
                // 设置睡眠的时间 5 分钟
                Thread.sleep(5 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            // 获取报告
            List<FeedSubmissionResultDto> feedSubmissionResultDtos;
            while (true) {

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


            Map<String, String> pathMap = new HashMap<>();
            for (int i = 0; i < feedSubmissionInfoDtoList.size(); i++) {
                String submissionId = feedSubmissionInfoDtoList.get(i).getFeedSubmissionId();
                Calendar calendar = Calendar.getInstance();
                String year = calendar.get(Calendar.YEAR) + "/";
                String month = (calendar.get(Calendar.MONTH)) + 1 + "/";
                // 返回结果路径
                String tempPath = fileStoragePath + year + month + "FeedSubmissionResult/" + submissionId + "_SubmissionResult.xml";

                for (int j = 0; j < feedSubmissionResultDtos.size(); j++) {
                    if (submissionId.equals(feedSubmissionResultDtos.get(j).getFeedSubmissionId())) {
                        switch (feedSubmissionInfoDtoList.get(i).getFeedType()) {
//                            case "_POST_PRODUCT_DATA_":
//                                pathMap.put("基本信息", tempPath);
//                                break;
                            case "_POST_PRODUCT_RELATIONSHIP_DATA_":
                                pathMap.put("关系", tempPath);
                                break;
                            case "_POST_PRODUCT_IMAGE_DATA_":
                                pathMap.put("图片", tempPath);
                                break;
                            case "_POST_INVENTORY_AVAILABILITY_DATA_":
                                pathMap.put("库存", tempPath);
                                break;
                            case "_POST_PRODUCT_PRICING_DATA_":
                                pathMap.put("价格", tempPath);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            // 解析xml
            List<Integer> typeStatus = new ArrayList();
            Iterator<Map.Entry<String, String>> it = pathMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                AnalysisFeedSubmissionResultDto analysisFeedSubmissionResultDto = null;
                String type = null;
                int tempStatus = 1;
                switch (entry.getKey()) {
//                    case "基本信息":
//                        type = "基本信息";
//                        analysisFeedSubmissionResultDto = XMLUtil.analysisFeedSubmissionResult(pathMap.get("基本信息"));
//                        // 子状态判断
//                        tempStatus = judgementState(analysisFeedSubmissionResultDto);
//                        uploadEntity.setProductsResultStatus(tempStatus);
//                        break;
                    case "关系":
                        type = "关系";
                        analysisFeedSubmissionResultDto = XMLUtil.analysisFeedSubmissionResult(pathMap.get("关系"));
                        // 子状态判断
                        tempStatus = judgementState(analysisFeedSubmissionResultDto);
                        uploadEntity.setRelationshipsResultStatus(tempStatus);
                        break;
                    case "图片":
                        type = "图片";
                        analysisFeedSubmissionResultDto = XMLUtil.analysisFeedSubmissionResult(pathMap.get("图片"));
                        // 子状态判断
                        tempStatus = judgementState(analysisFeedSubmissionResultDto);
                        uploadEntity.setImagesResultStatus(tempStatus);
                        break;
                    case "库存":
                        type = "库存";
                        analysisFeedSubmissionResultDto = XMLUtil.analysisFeedSubmissionResult(pathMap.get("库存"));
                        // 子状态判断
                        tempStatus = judgementState(analysisFeedSubmissionResultDto);
                        uploadEntity.setInventoryResultStatus(tempStatus);
                        break;
                    case "价格":
                        type = "价格";
                        analysisFeedSubmissionResultDto = XMLUtil.analysisFeedSubmissionResult(pathMap.get("价格"));
                        // 子状态判断
                        tempStatus = judgementState(analysisFeedSubmissionResultDto);
                        uploadEntity.setPricesResultStatus(tempStatus);
                        break;
                    default:
                        break;
                }
                typeStatus.add(tempStatus);
                List<ResultXMLDto> resultXMLDtoList = analysisFeedSubmissionResultDto.getResultXMLDtoList();
                if (resultXMLDtoList.size() != 0) {
                    for (int k = 0; k < resultXMLDtoList.size(); k++) {
                        ResultXmlEntity resultXmlEntity = new ResultXmlEntity();
                        resultXmlEntity.setSku(resultXMLDtoList.get(k).getSku());
                        resultXmlEntity.setProductId(productsService.queryIdBySku(resultXMLDtoList.get(k).getSku()));
                        resultXmlEntity.setUploadId(uploadId);
                        resultXmlEntity.setType(type);
                        resultXmlEntity.setState(tempStatus);
                        resultXmlEntity.setResult(resultXMLDtoList.get(k).getResultDescription());
                        resultXmlEntity.setResultType(resultXMLDtoList.get(k).getResultCode());
                        resultXmlEntity.setResultCode(resultXMLDtoList.get(k).getResultMessageCode());
                        resultXmlEntity.setCreationTime(new Date());

                        resultXmlService.insert(resultXmlEntity);
                    }
                } else {
                    continue;
                }
            }
            // 处理总状态
            uploadEntity.setUploadState(judgingTheTotalState(typeStatus));
            uploadEntity.setUpdateTime(new Date());
            //保存xml结果，保存状态
            uploadService.updateById(uploadEntity);
            //判断用户是否有等待上传线程
            UploadEntity currentUpload = uploadService.selectOne(new EntityWrapper<UploadEntity>().eq("upload_state", 0).eq("user_id",uploadEntity.getUserId()));
            if(currentUpload == null){
                System.out.println("-------------------当前上传项:" + uploadEntity.getUploadId() + "结束。" + "用户：" + userService.selectById(uploadEntity.getUserId()).getUsername() + "当前没有等待上传项，线程结束-------------------");
                return;
            }else{
                System.out.println("-------------------当前上传项:" + uploadEntity.getUploadId() + "结束。" + "用户：" + userService.selectById(uploadEntity.getUserId()).getUsername() + "当前有等待上传项，线程继续-------------------");
                submitFeed(currentUpload);
                return;
            }
        }catch (Exception ex){
            System.out.println("错误为：" +ex);
            uploadEntity.setUploadState(3);
            uploadEntity.setUpdateTime(new Date());
            uploadService.updateById(uploadEntity);
            ResultXmlEntity resultInfo = new ResultXmlEntity();
            resultInfo.setUploadId(uploadId);
            resultInfo.setType("产品");
            resultInfo.setState(3);
            resultInfo.setResult("产品信息有误，请修正后再次上传");
            resultInfo.setResultType("错误");
            resultInfo.setResultCode("002");
            resultInfo.setCreationTime(new Date());
            resultXmlService.insert(resultInfo);
            //判断用户是否有等待上传线程
            UploadEntity currentUpload = uploadService.selectOne(new EntityWrapper<UploadEntity>().eq("upload_state", 0).eq("user_id",uploadEntity.getUserId()));
            if(currentUpload == null){
                System.out.println("-------------------当前上传项:" + uploadEntity.getUploadId() + "结束。" + "用户：" + userService.selectById(uploadEntity.getUserId()).getUsername() + "当前没有等待上传项，线程结束-------------------");
                return;
            }else{
                System.out.println("-------------------当前上传项:" + uploadEntity.getUploadId() + "结束。" + "用户：" + userService.selectById(uploadEntity.getUserId()).getUsername() + "当前有等待上传项，线程继续-------------------");
                submitFeed(currentUpload);
                return;
            }
        }

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
        if(feedSubmissionInfoDtoList != null){
            if (feedSubmissionInfoDtoList.size() == 0) {
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                feedSubmissionInfoDtoList = invokeSubmitFeedAsync(uploadId, service, submitFeedRequestList);
                return submitProductFeed(uploadId, serviceURL, merchantId, sellerDevAuthToken, feedType, filePath, marketplaceIdList);
            }
            return feedSubmissionInfoDtoList.get(0);
        }else{
            return null;
        }

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
                    if("InvalidAccessKeyId".equals(exception.getErrorCode()) || "AccessDenied".equals(exception.getErrorCode()) || "InvalidParameterValue".equals(exception.getErrorCode())){
                        return null;
                    }
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
                                    temp = 0;
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
        MarketplaceWebService service = null;
        switch (serviceURL) {
            // 加拿大
            case "https://mws.amazonservices.ca":
                // 墨西哥
            case "https://mws.amazonservices.com.mx":
                // 美国
            case "https://mws.amazonservices.com":
                service = new MarketplaceWebServiceClient(naAccessKey, naSecretKey, appName, appVersion, config);
                break;
            // 欧洲
            case "https://mws-eu.amazonservices.com":
                service = new MarketplaceWebServiceClient(euAccessKey, euSecretKey, appName, appVersion, config);
                break;
            // 澳大利亚
            case "https://mws.amazonservices.com.au":
                service = new MarketplaceWebServiceClient(auAccessKey, auSecretKey, appName, appVersion, config);
                break;
            // 日本
            case "https://mws.amazonservices.jp":
                service = new MarketplaceWebServiceClient(jpAccessKey, jpSecretKey, appName, appVersion, config);
                break;
            default:
        }
        return service;
    }

    @Override
    public MarketplaceWebService getAsyncService(String serviceURL) {
        MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();
        config.setServiceURL(serviceURL);
        config.setMaxAsyncThreads(35);
        config.setConnectionTimeout(120000);
        config.setSoTimeout(120000);
        MarketplaceWebService service = null;
        switch (serviceURL) {
            // 加拿大
            case "https://mws.amazonservices.ca":
                // 墨西哥
            case "https://mws.amazonservices.com.mx":
                // 美国
            case "https://mws.amazonservices.com":
                service = new MarketplaceWebServiceClient(naAccessKey, naSecretKey, appName, appVersion, config);
                break;
            // 欧洲
            case "https://mws-eu.amazonservices.com":
                service = new MarketplaceWebServiceClient(euAccessKey, euSecretKey, appName, appVersion, config);
                break;
            // 澳大利亚
            case "https://mws.amazonservices.com.au":
                service = new MarketplaceWebServiceClient(auAccessKey, auSecretKey, appName, appVersion, config);
                break;
            // 日本
            case "https://mws.amazonservices.jp":
                service = new MarketplaceWebServiceClient(jpAccessKey, jpSecretKey, appName, appVersion, config);
                break;
            default:
                break;
        }
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
        uploadEntity.setUpdateTime(new Date());
        uploadService.updateById(uploadEntity);
    }

    @Override
    public int judgementState(AnalysisFeedSubmissionResultDto analysisFeedSubmissionResultDto) {
        int temp;
        if (analysisFeedSubmissionResultDto.getMessagesWithError() != 0) {
            temp = 3;
        } else {
            if (analysisFeedSubmissionResultDto.getMessagesWithWarning() != 0) {
                temp = 4;
            } else {
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
        String filePath = generateProductXML.generateProductXMLByHomeImprovement(uploadId, merchantIdentifierText, productsList, countryCode);
        /*switch (templateName) {
            // 家居装修/HomeImprovement
            case "HomeImprovement":
                filePath = generateProductXML.generateProductXMLByHomeImprovement(uploadId, merchantIdentifierText, productsList, countryCode);
                break;
            // 服装/ProductClothing
            case "ProductClothing":
//                filePath = generateProductXML.generateProductXMLByClothing(uploadId, merchantIdentifierText, productsList, countryCode);
                filePath = generateProductXML.generateProductXMLByHomeImprovement(uploadId, merchantIdentifierText, productsList, countryCode);
                break;
            // 汽车配件/AutoAccessory
            case "AutoAccessory":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 宝宝/Baby
            case "Baby":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 美妆/Beauty
            case "Beauty":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 相机照片/CameraPhoto
            case "CameraPhoto":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 服装饰品/ClothingAccessories
            case "ClothingAccessories":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 电脑/Computers
            case "Computers":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 消费类电子产品/CE
            case "CE":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 美食/Gourmet
            case "Gourmet":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 食品和饮料/FoodAndBeverages
            case "FoodAndBeverages":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 健康/Health
            case "Health":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 食品服务和门卫，卫生与安全/FoodServiceAndJanSan
            case "FoodServiceAndJanSan":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 实验室和科学用品/LabSupplies
            case "LabSupplies":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 电力传输/PowerTransmission
            case "PowerTransmission":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 原材料/RawMaterials
            case "RawMaterials":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 首饰/Jewelry
            case "Jewelry":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 大家电/LargeAppliances
            case "LargeAppliances":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 灯/Lighting
            case "Lighting":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 乐器/MusicalInstruments
            case "MusicalInstruments":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 宠物用品/PetSupplies
            case "PetSupplies":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 体育用品/Sports
            case "Sports":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 体育纪念品/SportsMemorabilia
            case "SportsMemorabilia":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 轮胎和车轮/TiresAndWheels
            case "TiresAndWheels":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 工具/Tools
            case "Tools":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 玩具和游戏/Toys
            case "Toys":
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;

                // 无线/Wireless
            case "Wireless":
                // 除了衣服，剩下的分类都用默认的模板
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;
            default:
                filePath = generateProductXML.generateDefaultProductXML(uploadId, merchantIdentifierText, productsList, countryCode);
                break;
        }*/
        return filePath;
    }

    @Override
    @Async("taskExecutor")
    public void test1() {
        Future<String> future;

        for(int i=0; i < 5; i++){
            try {
                future = new AsyncResult<String>("success:" + i);
                System.out.println("future:" + future);
                Thread.sleep(1 * 3 * 1000);
                System.out.println("线程1正在执行");
            } catch (InterruptedException e) {
                future = new AsyncResult<String>("error");
                System.out.println("线程终止");
                e.printStackTrace();
            }
        }
        return;
    }

    @Override
    @Async("taskExecutor")
    public void test2() {
        for(int i=0; i < 10; i++){
            try {
                Thread.sleep(1 * 3 * 1000);
                System.out.println("线程2正在执行");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ThreadGroup currentGroup =
                Thread.currentThread().getThreadGroup();
        int noThreads = currentGroup.activeCount();
        Thread[] lstThreads = new Thread[noThreads];
        currentGroup.enumerate(lstThreads);
        int count = 0;
        for (int i = 0; i < noThreads; i++){
            if(lstThreads[i].getName().indexOf("taskExecutor") != -1){
                count += 1;
                System.out.println("线程名字：" + lstThreads[i].getName());
            }
        }
        System.out.println("线程数量：" + count);
    }
    @Override
    @Async("taskExecutor")
    public void test3() {
        Future<String> future;

        for(int i=0; i < 10; i++){
            try {
                future = new AsyncResult<String>("success:" + i);
                System.out.println("future:" + future);
                Thread.sleep(1 * 3 * 1000);
                System.out.println("线程3正在执行");
            } catch (InterruptedException e) {
                future = new AsyncResult<String>("error");
                System.out.println("线程终止");
                e.printStackTrace();
            }
        }
    }
    @Override
    @Async("taskExecutor")
    public void test4() {
        Future<String> future;

        for(int i=0; i < 10; i++){
            try {
                future = new AsyncResult<String>("success:" + i);
                System.out.println("future:" + future);
                Thread.sleep(1 * 3 * 1000);
                System.out.println("线程4正在执行");
            } catch (InterruptedException e) {
                future = new AsyncResult<String>("error");
                System.out.println("线程终止");
                e.printStackTrace();
            }
        }
    }
    @Override
    @Async("taskExecutor")
    public void test5() {
        Future<String> future;

        for(int i=0; i < 10; i++){
            try {
                future = new AsyncResult<String>("success:" + i);
                System.out.println("future:" + future);
                Thread.sleep(1 * 3 * 1000);
                System.out.println("线程5正在执行");
            } catch (InterruptedException e) {
                future = new AsyncResult<String>("error");
                System.out.println("线程终止");
                e.printStackTrace();
            }
        }
    }
    @Override
    @Async("taskExecutor")
    public void test6() {
        Future<String> future;

        for(int i=0; i < 10; i++){
            try {
                future = new AsyncResult<String>("success:" + i);
                System.out.println("future:" + future);
                Thread.sleep(1 * 3 * 1000);
                System.out.println("线程6正在执行");
            } catch (InterruptedException e) {
                future = new AsyncResult<String>("error");
                System.out.println("线程终止");
                e.printStackTrace();
            }
        }
    }
}