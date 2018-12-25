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
    public String generateProductXML(String merchantIdentifierText, List<ProductsEntity> productsList, String countryCode, String categoryNodeId) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("AmazonEnvelope");
        root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance").addAttribute("xsi:noNamespaceSchemaLocation", "amzn-envelope.xsd");
        Element header = root.addElement("Header");
        Element documentVersion = header.addElement("DocumentVersion");
        documentVersion.addText("1.01");
        Element merchantIdentifier = header.addElement("MerchantIdentifier");
        merchantIdentifier.addText(merchantIdentifierText);
        Element messageType = root.addElement("MessageType");
        messageType.addText("Product");
        Element purgeAndReplace = root.addElement("PurgeAndReplace");
        purgeAndReplace.addText("false");
        int messageId = 1;
        for (int i = 0; i < productsList.size(); i++) {
            ProductsEntity productsEntity = productsList.get(i);

            Element message = root.addElement("Message");
            Element messageID = message.addElement("MessageID");
            messageID.addText(messageId + "");
            messageId++;
            Element operationType = message.addElement("OperationType");
            operationType.addText("Update");
            Element product = message.addElement("Product");
            Element sku = product.addElement("SKU");
            sku.addText(productsEntity.getProductSku());

            // TODO: 2018/12/22 判断是否有变体

            // 父商品没有ean
           /* if (productsEntity.getEanCode() != null || !"".equals(productsEntity.getEanCode()) || !StringUtils.isBlank(productsEntity.getEanCode())) {
                Element standardProductID = product.addElement("StandardProductID");
                Element type = standardProductID.addElement("Type");
                type.addText("EAN");
                Element value = standardProductID.addElement("Value");
                value.addText(productsEntity.getEanCode());
            } else if (productsEntity.getUpcCode() != null || !"".equals(productsEntity.getUpcCode()) || !StringUtils.isBlank(productsEntity.getUpcCode())) {
                Element standardProductID = product.addElement("StandardProductID");
                Element type = standardProductID.addElement("Type");
                type.addText("UPC");
                Element value = standardProductID.addElement("Value");
                value.addText(productsEntity.getUpcCode());
            }*/

            Element productTaxCode = product.addElement("ProductTaxCode");
            productTaxCode.addText("A_GEN_TAX");

            Element descriptionData = product.addElement("DescriptionData");
            // 不同国家语言
            String title = "";
            long freightId = 0L;
            String money = "USD";
            switch (countryCode) {
                // 加拿大
                case "CA":
                    freightId = productsEntity.getCanadaFreight();
                    money = "CAD";
                    break;
                // 墨西哥
                case "MX":
                    freightId = productsEntity.getMexicoFreight();
                    money = "MXN";
                    break;
                // 美国
                case "US":
                    freightId = productsEntity.getAmericanFreight();
                    money = "USD";
                    break;
                // 德国
                case "DE":
                    freightId = productsEntity.getGermanyFreight();
                    money = "EUR";
                    break;
                // 西班牙
                case "ES":
                    freightId = productsEntity.getSpainFreight();
                    money = "EUR";
                    break;
                // 法国
                case "FR":
                    freightId = productsEntity.getFranceFreight();
                    money = "EUR";
                    break;
                // 英国
                case "GB":
                    freightId = productsEntity.getBritainFreight();
                    money = "GBP";
                    break;
                // 意大利
                case "IT":
                    freightId = productsEntity.getItalyFreight();
                    money = "EUR";
                    break;
                // 土耳其
                case "TR":
                    money = "TRY";
                    break;
                // 澳大利亚
                case "AU":
                    freightId = productsEntity.getAustraliaFreight();
                    money = "AUD";
                    break;
                // 日本
                case "JP":
                    freightId = productsEntity.getJapanFreight();
                    money = "JPY";
                    break;
                default:
                    break;
            }

            IntroductionEntity introductionEntity = introductionService.selectById(freightId);
            Element titleEl = descriptionData.addElement("Title");
            titleEl.addText(introductionEntity.getProductTitle());
            Element brand = descriptionData.addElement("Brand");
            brand.addText(productsEntity.getBrandName());
            Element description = descriptionData.addElement("Description");
            description.addText(introductionEntity.getProductDescription());
            Element bulletPoint = descriptionData.addElement("BulletPoint");
            bulletPoint.addText(introductionEntity.getKeyPoints());

            // 生产厂家
            Element manufacturer = descriptionData.addElement("Manufacturer");
            manufacturer.addText(productsEntity.getProducerName());

            // RecommendedBrowseNode 分类
            // TODO: 2018/12/21 ItemType
            Element recommendedBrowseNode = descriptionData.addElement("RecommendedBrowseNode");
            recommendedBrowseNode.addText(categoryNodeId);

            Element productData = product.addElement("ProductData");
            Element computers = productData.addElement("Beauty");
            Element productType = computers.addElement("ProductType");
            Element keyboards = productType.addElement("BeautyMisc");

            String sizeStr = "";
            String colorStr = "";
            boolean isSize = false;
            boolean isColor = false;
            String typeStr = "";
            if (productsEntity.getSizeId() != null || productsEntity.getSizeId() != 0) {
                VariantParameterEntity sizeVariantParameter = variantParameterService.selectById(productsEntity.getSizeId());
                sizeStr = sizeVariantParameter.getParamsType();
                isSize = true;
            }
            if (productsEntity.getColorId() != null || productsEntity.getColorId() != 0) {
                VariantParameterEntity colorVariantParameter = variantParameterService.selectById(productsEntity.getColorId());
                colorStr = colorVariantParameter.getParamsType();
                isColor = true;
            }
            if ((!"".equals(sizeStr)) || (!"".equals(colorStr))) {
                Element variationData = keyboards.addElement("VariationData");
                Element parentage = variationData.addElement("Parentage");
                parentage.addText("parent");
                Element variationTheme = variationData.addElement("VariationTheme");
                if (isSize && (!isColor)) {
                    variationTheme.addText("Size");
                    typeStr = "Size";
                }
                if (isColor && (!isSize)) {
                    variationTheme.addText("Color");
                    typeStr = "Color";
                }
                if (isSize && isColor) {
                    if (("size".equals(sizeStr) || "Size".equals(sizeStr)) && ("color".equals(colorStr) || "Color".equals(colorStr))) {
                        variationTheme.addText("Size-Color");
                        typeStr = "Size-Color";
                    }
                }
            }

            // 只有变体有MSRP
            /*
            Element msrp = descriptionData.addElement("MSRP");
            msrp.addText(productsEntity.get)
            */

            List<VariantsInfoEntity> variantsInfoEntityList = variantsInfoService.selectList(new EntityWrapper<VariantsInfoEntity>().eq("product_id", productsEntity.getProductId()).orderBy(true, "variant_sort", true));
            if (variantsInfoEntityList != null) {
                for (VariantsInfoEntity variantsInfoEntity : variantsInfoEntityList) {
                    Element message1 = root.addElement("Message");
                    Element messageID1 = message1.addElement("MessageID");
                    messageID1.addText(messageId + "");
                    messageId++;
                    Element operationType1 = message1.addElement("OperationType");
                    operationType1.addText("Update");
                    Element product1 = message1.addElement("Product");
                    Element sku1 = product1.addElement("SKU");
                    // ean
                    // TODO: 2018/12/22 zjr ?????
                    sku1.addText(variantsInfoEntity.getVariantSku());
                    if (variantsInfoEntity.getEanCode() != null || !"".equals(variantsInfoEntity.getEanCode()) || !StringUtils.isBlank(variantsInfoEntity.getEanCode())) {
                        Element standardProductID1 = product1.addElement("StandardProductID");
                        Element type1 = standardProductID1.addElement("Type");
                        type1.addText("EAN");
                        Element value1 = standardProductID1.addElement("Value");
                        value1.addText(productsEntity.getEanCode());
                    }
                    Element productTaxCode1 = product1.addElement("ProductTaxCode");
                    productTaxCode1.addText("A_GEN_TAX");

                    Element descriptionData1 = product1.addElement("DescriptionData");
                    Element titleEl1 = descriptionData1.addElement("Title");
                    titleEl1.addText(introductionEntity.getProductTitle());
                    Element brand1 = descriptionData1.addElement("Brand");
                    brand1.addText(productsEntity.getBrandName());
                    Element description1 = descriptionData1.addElement("Description");
                    description1.addText(introductionEntity.getProductDescription());
                    Element bulletPoint1 = descriptionData1.addElement("BulletPoint");
                    bulletPoint1.addText(introductionEntity.getKeyPoints());

                    // 生产厂家
                    Element manufacturer1 = descriptionData1.addElement("Manufacturer");
                    manufacturer1.addText(productsEntity.getProducerName());

                    // RecommendedBrowseNode 分类
                    Element recommendedBrowseNode1 = descriptionData1.addElement("RecommendedBrowseNode");
                    recommendedBrowseNode1.addText(categoryNodeId);

                    Element productData1 = product1.addElement("ProductData");
                    Element computers1 = productData1.addElement("Beauty");
                    Element productType1 = computers1.addElement("ProductType");
                    Element keyboards1 = productType1.addElement("BeautyMisc");
                    Element variationData1 = keyboards1.addElement("VariationData");
                    Element parentage1 = variationData1.addElement("Parentage");
                    parentage1.addText("child");
                    Element variationTheme1 = variationData1.addElement("VariationTheme");
                    switch (typeStr) {
                        case "Size":
                            // TODO: 2018/12/20 zjr size 写死
                            variationTheme1.addText(typeStr);
                            Element size = variationData1.addElement("Size");
                            size.addText("45");
                            break;
                        case "Color":
                            // TODO: 2018/12/20 zjr color 写死
                            variationTheme1.addText(typeStr);
                            Element color = variationData1.addElement("Color");
                            color.addText("Darkroom");
                            break;
                        case "Size-Color":
                            // TODO: 2018/12/20 zjr Size_color 写死
                            variationTheme1.addText(typeStr);
                            Element size1 = variationData1.addElement("Size");
                            size1.addText("45");
                            Element color1 = variationData1.addElement("Color");
                            color1.addText("Darkroom");
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        // 生成文件路径
        String path = fileStoragePath;
        String filePath = FileUtil.generateFilePath(path, "Product");

        try {
            XMLUtil.writeXMLToFile(document, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (FileUtil.judeFileExists(filePath) == false) {
            return null;
        }
        return filePath;
    }

    @Override
    public String generateImagesXML(List<ProductsEntity> productsList, String merchantIdentifierText) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("AmazonEnvelope");
        root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance").addAttribute("xsi:noNamespaceSchemaLocation", "amzn-envelope.xsd");
        Element header = root.addElement("Header");
        Element documentVersion = header.addElement("DocumentVersion");
        documentVersion.addText("1.01");
        Element merchantIdentifier = header.addElement("MerchantIdentifier");
        merchantIdentifier.addText(merchantIdentifierText);
        Element messageType = root.addElement("MessageType");
        messageType.addText("ProductImage");
        int messageId = 1;
        for (int i = 0; i < productsList.size(); i++) {
            ProductsEntity productsEntity = productsList.get(i);

            Long mainImageId = productsEntity.getMainImageId();

            if (mainImageId != null) {
                // 有主图片时执行
                ImageAddressEntity imageAddressEntity = imageAddressService.selectById(mainImageId);
                if (imageAddressEntity != null) {
                    Element message = root.addElement("Message");
                    Element messageID = message.addElement("MessageID");
                    messageID.addText(messageId + "");
                    Element operationType = message.addElement("OperationType");
                    operationType.addText("Update");
                    Element productImage = message.addElement("ProductImage");
                    Element sku = productImage.addElement("SKU");
                    sku.addText(productsEntity.getProductSku());
                    Element imageType = productImage.addElement("ImageType");
                    imageType.addText("Main");
                    Element imageLocation = productImage.addElement("ImageLocation");
                    imageLocation.addText(imageAddressEntity.getImageUrl());
                    messageId++;
                }
            }

            // 主产品图片
            List<ImageAddressEntity> imageAddressEntityList = imageAddressService.selectList(new EntityWrapper<ImageAddressEntity>().eq("product_id", productsEntity.getProductId()).eq("is_deleted", "0"));
            if (imageAddressEntityList != null || imageAddressEntityList.size() != 0) {
                int temp = imageAddressEntityList.size();
                if (temp > 8) {
                    temp = 8;
                }
                for (int j = 0; j < temp; j++) {
                    ImageAddressEntity imageAddress = imageAddressEntityList.get(j);
                    if (imageAddress.getImageId().equals(mainImageId) == true) {
                        continue;
                    } else {
                        Element message = root.addElement("Message");
                        Element messageID = message.addElement("MessageID");
                        messageID.addText(messageId + "");
                        Element operationType = message.addElement("OperationType");
                        operationType.addText("Update");
                        Element productImage = message.addElement("ProductImage");
                        Element sku = productImage.addElement("SKU");
                        sku.addText(productsEntity.getProductSku());
                        Element imageType = productImage.addElement("ImageType");
                        imageType.addText("PT" + (j + 1));
                        Element imageLocation = productImage.addElement("ImageLocation");
                        imageLocation.addText(imageAddress.getImageUrl());
                        messageId++;
                    }
                }
            }

            // 变体图片
            List<VariantsInfoEntity> variantsInfoEntityList = variantsInfoService.selectList(new EntityWrapper<VariantsInfoEntity>().eq("product_id", productsEntity.getProductId()).orderBy(true, "variant_sort", true));
            if (variantsInfoEntityList != null || variantsInfoEntityList.size() != 0) {
                for (int j = 0; j < variantsInfoEntityList.size(); j++) {
                    VariantsInfoEntity variantsInfoEntity = variantsInfoEntityList.get(j);
                    String[] viImageUrls = variantsInfoEntity.getImageUrl().split(",");
                    if (viImageUrls.length != 0) {
                        int temp = viImageUrls.length;
                        if (temp > 9) {
                            temp = 9;
                        }
                        for (int k = 0; k < temp; k++) {
                            Element message = root.addElement("Message");
                            Element messageID = message.addElement("MessageID");
                            messageID.addText(messageId + "");
                            Element operationType = message.addElement("OperationType");
                            operationType.addText("Update");
                            Element productImage = message.addElement("ProductImage");
                            Element sku = productImage.addElement("SKU");
                            sku.addText(variantsInfoEntity.getVariantSku());
                            Element imageType = productImage.addElement("ImageType");
                            if (k == 0) {
                                imageType.addText("Main");
                            } else {
                                imageType.addText("PT" + k);
                            }
                            Element imageLocation = productImage.addElement("ImageLocation");
                            imageLocation.addText(viImageUrls[k]);
                            messageId++;
                        }
                    }
                }
            }
        }

        // 生成文件路径
        String path = fileStoragePath;
        String filePath = FileUtil.generateFilePath(path, "Images");

        try {
            XMLUtil.writeXMLToFile(document, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (FileUtil.judeFileExists(filePath) == false) {
            return null;
        }
        return filePath;
    }

    @Override
    public String generateInventoryXML(List<ProductsEntity> productsList, String merchantIdentifierText) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("AmazonEnvelope");
        root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance").addAttribute("xsi:noNamespaceSchemaLocation", "amzn-envelope.xsd");
        Element header = root.addElement("Header");
        Element documentVersion = header.addElement("DocumentVersion");
        documentVersion.addText("1.01");
        Element merchantIdentifier = header.addElement("MerchantIdentifier");
        merchantIdentifier.addText(merchantIdentifierText);
        Element messageType = root.addElement("MessageType");
        messageType.addText("Inventory");
        int messageId = 1;
        for (int i = 0; i < productsList.size(); i++) {
            ProductsEntity productsEntity = productsList.get(i);

            Element message = root.addElement("Message");
            Element messageID = message.addElement("MessageID");
            messageID.addText(messageId + "");
            Element operationType = message.addElement("OperationType");
            operationType.addText("Update");
            Element inventory = message.addElement("Inventory");
            Element sku = inventory.addElement("SKU");
            sku.addText(productsEntity.getProductSku());
            Element quantity = inventory.addElement("Quantity");
            if (productsEntity.getStock() != null) {
                quantity.addText(productsEntity.getStock().toString());
            } else {
                quantity.addText("0");
                return null;
            }
            messageId++;

            List<VariantsInfoEntity> variantsInfoEntityList = variantsInfoService.selectList(new EntityWrapper<VariantsInfoEntity>().eq("product_id", productsEntity.getProductId()).orderBy(true, "variant_sort", true));
            if (variantsInfoEntityList != null || variantsInfoEntityList.size() != 0) {
                for (int j = 0; j < variantsInfoEntityList.size(); j++) {
                    VariantsInfoEntity variantsInfoEntity = variantsInfoEntityList.get(j);
                    Element message1 = root.addElement("Message");
                    Element messageID1 = message1.addElement("MessageID");
                    messageID1.addText(messageId + "");
                    Element operationType1 = message1.addElement("OperationType");
                    operationType1.addText("Update");
                    Element inventory1 = message1.addElement("Inventory");
                    Element sku1 = inventory1.addElement("SKU");
                    sku1.addText(variantsInfoEntity.getVariantSku());
                    Element quantity1 = inventory1.addElement("Quantity");
                    if (productsEntity.getStock() != null) {
                        quantity1.addText(variantsInfoEntity.getVariantStock().toString());
                    } else {
                        quantity1.addText("0");
                    }
                    messageId++;
                }
            }
        }

        // 生成文件路径
        String path = fileStoragePath;
        String filePath = FileUtil.generateFilePath(path, "Inventory");

        try {
            XMLUtil.writeXMLToFile(document, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (FileUtil.judeFileExists(filePath) == false) {
            return null;
        }
        return filePath;
    }

    @Override
    public String generatePricesXML(String countryCode, List<ProductsEntity> productsList, String merchantIdentifierText) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("AmazonEnvelope");
        root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance").addAttribute("xsi:noNamespaceSchemaLocation", "amzn-envelope.xsd");
        Element header = root.addElement("Header");
        Element documentVersion = header.addElement("DocumentVersion");
        documentVersion.addText("1.01");
        Element merchantIdentifier = header.addElement("MerchantIdentifier");
        merchantIdentifier.addText(merchantIdentifierText);
        Element messageType = root.addElement("MessageType");
        messageType.addText("Inventory");
        int messageId = 1;
        for (int i = 0; i < productsList.size(); i++) {

            ProductsEntity productsEntity = productsList.get(i);

            Long freightCostId = null;
            String money = "USD";
            switch (countryCode) {
                // 加拿大
                case "CA":
                    money = "CAD";
                    freightCostId = productsEntity.getCanadaFreight();
                    break;
                // 墨西哥
                case "MX":
                    money = "MXN";
                    freightCostId = productsEntity.getMexicoFreight();
                    break;
                // 美国
                case "US":
                    freightCostId = productsEntity.getAmericanFreight();
                    money = "USD";
                    break;
                // 德国
                case "DE":
                    freightCostId = productsEntity.getGermanyFreight();
                    money = "EUR";
                    break;
                // 西班牙
                case "ES":
                    money = "EUR";
                    freightCostId = productsEntity.getSpainFreight();
                    break;
                // 法国
                case "FR":
                    money = "EUR";
                    freightCostId = productsEntity.getFranceFreight();
                    break;
                // 英国
                case "GB":
                    money = "GBP";
                    freightCostId = productsEntity.getBritainFreight();
                    break;
                // 意大利
                case "IT":
                    money = "EUR";
                    freightCostId = productsEntity.getItalyFreight();
                    break;
                // 澳大利亚
                case "AU":
                    money = "AUD";
                    freightCostId = productsEntity.getAustraliaFreight();
                    break;
                // 日本
                case "JP":
                    money = "JPY";
                    freightCostId = productsEntity.getJapanFreight();
                    break;
                default:
                    break;
            }

            // 价格
            EntityWrapper<FreightCostEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("freight_cost_id", freightCostId);
            FreightCostEntity freightCostEntity = freightCostService.selectOne(wrapper);
            // 最终售价，当前国家的货币价格。
            BigDecimal final_price = freightCostEntity.getFinalPrice();

            Element message = root.addElement("Message");
            Element messageID = message.addElement("MessageID");
            messageID.addText(messageId + "");
            Element price = message.addElement("Price");
            Element sku = price.addElement("SKU");
            sku.addText(productsEntity.getProductSku());
            Element standardPrice = price.addElement("StandardPrice");
            standardPrice.addAttribute("currency", money);
            standardPrice.addText(final_price.toString());
            messageId++;

            List<VariantsInfoEntity> variantsInfoEntityList = variantsInfoService.selectList(new EntityWrapper<VariantsInfoEntity>().eq("product_id", productsEntity.getProductId()).orderBy(true, "variant_sort", true));
            if (variantsInfoEntityList != null || variantsInfoEntityList.size() != 0) {
                for (int j = 0; j < variantsInfoEntityList.size(); j++) {
                    VariantsInfoEntity variantsInfoEntity = variantsInfoEntityList.get(j);
                    Element message1 = root.addElement("Message");
                    Element messageID1 = message1.addElement("MessageID");
                    messageID1.addText(messageId + "");
                    Element price1 = message1.addElement("Price");
                    Element sku1 = price1.addElement("SKU");
                    sku1.addText(variantsInfoEntity.getVariantSku());
                    Element standardPrice1 = price1.addElement("StandardPrice");
                    standardPrice1.addAttribute("currency", money);
                    standardPrice1.addText(final_price.toString());
                    messageId++;
                }

            }
        }

        // 生成文件路径
        String path = fileStoragePath;
        String filePath = FileUtil.generateFilePath(path, "Prices");

        try {
            XMLUtil.writeXMLToFile(document, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (FileUtil.judeFileExists(filePath) == false) {
            return null;
        }
        return filePath;
    }

    @Override
    public String generateRelationshipsXML(List<ProductsEntity> productsList, String merchantIdentifierText) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("AmazonEnvelope");
        root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance").addAttribute("xsi:noNamespaceSchemaLocation", "amzn-envelope.xsd");
        Element header = root.addElement("Header");
        Element documentVersion = header.addElement("DocumentVersion");
        documentVersion.addText("1.01");
        Element merchantIdentifier = header.addElement("MerchantIdentifier");
        merchantIdentifier.addText(merchantIdentifierText);
        Element messageType = root.addElement("MessageType");
        messageType.addText("Relationship");
        int messageId = 1;
        for (int i = 0; i < productsList.size(); i++) {
            ProductsEntity productsEntity = productsList.get(i);

            List<VariantsInfoEntity> variantsInfoEntityList = variantsInfoService.selectList(new EntityWrapper<VariantsInfoEntity>().eq("product_id", productsEntity.getProductId()).orderBy(true, "variant_sort", true));
            if (variantsInfoEntityList != null || variantsInfoEntityList.size() != 0) {
                Element message = root.addElement("Message");
                Element messageID = message.addElement("MessageID");
                messageID.addText(messageId + "");
                Element operationType = message.addElement("OperationType");
                operationType.addText("Update");
                Element relationship = message.addElement("Relationship");
                Element parentSKU = relationship.addElement("ParentSKU");
                parentSKU.addText(productsEntity.getProductSku());
                for (VariantsInfoEntity vi : variantsInfoEntityList) {
                    Element relation = relationship.addElement("Relation");
                    Element sku = relation.addElement("SKU");
                    sku.addText(vi.getVariantSku());
                    Element type = relation.addElement("Type");
                    type.addText("Variation");
                }
            }
        }

        // 生成文件路径
        String path = fileStoragePath;
        String filePath = FileUtil.generateFilePath(path, "Relationships");

        try {
            XMLUtil.writeXMLToFile(document, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (FileUtil.judeFileExists(filePath) == false) {
            return null;
        }
        return filePath;
    }

    @Override
    public List<FeedSubmissionInfoDto> submitProductFeed(Long uploadId, String serviceURL, String merchantId, String sellerDevAuthToken, String feedType, String filePath, List<String> marketplaceIdList) {

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
        return invokeSubmitFeedAsync(uploadId, service, submitFeedRequestList);
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
    @Async
    public void submitFeed(UploadEntity uploadEntity) {

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

        // 生成xml文件路径
        Map<String, String> filePathMap = new HashMap<>();
        for (int i = 0; i < operateItemStr.length; i++) {
            switch (operateItemStr[i]) {
                // 0 基本信息
                case "0":
                    String productPath = generateProductXMLByClothing(uploadId,merchantId, productsEntityList, countryCode);
                    filePathMap.put("0", productPath);
                    break;
                // 1 关系
                case "1":
                    String relationshipsPath = generateRelationshipsXML(productsEntityList, merchantId);
                    filePathMap.put("1", relationshipsPath);
                    break;
                // 2 图片
                case "2":
                    String imagesPath = generateImagesXML(productsEntityList, merchantId);
                    filePathMap.put("2", imagesPath);
                    break;
                // 3 库存
                case "3":
                    String inventoryPath = generateInventoryXML(productsEntityList, merchantId);
                    filePathMap.put("3", inventoryPath);
                    break;
                // 4 价格
                case "4":
                    String pricesPath = generatePricesXML(countryCode, productsEntityList, merchantId);
                    filePathMap.put("4", pricesPath);
                    break;
                default:
                    break;
            }
        }


        // 上传xml
        FeedSubmissionInfoDto productFeedSubmissionInfoDto = null;
        // 0 是产品基本信息xml
        if (filePathMap.containsKey("0")) {
            // 产品信息上传
            productFeedSubmissionInfoDto = submitProductFeed(uploadId, serviceURL, merchantId, sellerDevAuthToken, uploadTypeMap.get("0"), filePathMap.get("0"), marketplaceIdList).get(0);
            //使用FeedSubmissionId获取的亚马逊对于xml的处理状态
            while (true) {
                try {
                    List<String> feedSubmissionList = new ArrayList<>();
                    feedSubmissionList.add(productFeedSubmissionInfoDto.getFeedSubmissionId());
                    productFeedSubmissionInfoDto = getFeedSubmissionListAsync(uploadId, serviceURL, merchantId, sellerDevAuthToken, feedSubmissionList).get(0);

                    if (productFeedSubmissionInfoDto.getFeedProcessingStatus().equals(1)) {
                        break;
                    }
                    // 出现如下三种情况，总状态变失败。
                    if (productFeedSubmissionInfoDto.getFeedProcessingStatus().equals(3)) {
                        List<FeedSubmissionInfoDto> tempList = new ArrayList<>();
                        tempList.add(productFeedSubmissionInfoDto);
                        updateFeedUpload(tempList, 3);
                        break;
                    }
                    // 设置睡眠的时间 60 秒
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


        // FeedSubmissionInfoDto 数据存放，等待上传
        updateFeedUpload(feedSubmissionInfoDtoList, 0);

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
        updateFeedUpload(feedSubmissionInfoDtoList, 1);

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

        UploadEntity updateUploadEntity = new UploadEntity();
        updateUploadEntity.setUpdateTime(new Date());
        updateUploadEntity.setUploadId(uploadId);
        String tempPath;
        List<Integer> typeStatus = new ArrayList();
        for (int i = 0; i < feedSubmissionInfoDtoList.size(); i++) {
            tempPath = "";
            String submissionId = feedSubmissionInfoDtoList.get(i).getFeedSubmissionId();
            tempPath = fileStoragePath + "FeedSubmissionResult/" + submissionId + "_SubmissionResult.xml";
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
                            resultXmlEntity1 = isExist(uploadId, "products");
                            if (resultXmlEntity1 != null) {
                                resultXmlEntity.setId(resultXmlEntity1.getId());
                            }
                            resultXmlEntity.setType("products");
                            updateUploadEntity.setProductsResultStatus(tempStatus);
                            break;
                        case "_POST_PRODUCT_RELATIONSHIP_DATA_":
                            resultXmlEntity1 = isExist(uploadId, "relationships");
                            if (resultXmlEntity1 != null) {
                                resultXmlEntity.setId(resultXmlEntity1.getId());
                            }
                            resultXmlEntity.setType("relationships");
                            updateUploadEntity.setProductsResultStatus(tempStatus);
                            break;
                        case "_POST_PRODUCT_IMAGE_DATA_":
                            resultXmlEntity1 = isExist(uploadId, "images");
                            if (resultXmlEntity1 != null) {
                                resultXmlEntity.setId(resultXmlEntity1.getId());
                            }
                            resultXmlEntity.setType("images");
                            updateUploadEntity.setProductsResultStatus(tempStatus);
                            break;
                        case "_POST_INVENTORY_AVAILABILITY_DATA_":
                            resultXmlEntity1 = isExist(uploadId, "inventory");
                            if (resultXmlEntity1 != null) {
                                resultXmlEntity.setId(resultXmlEntity1.getId());
                            }
                            resultXmlEntity.setType("inventory");
                            updateUploadEntity.setProductsResultStatus(tempStatus);
                            break;
                        case "_POST_PRODUCT_PRICING_DATA_":
                            resultXmlEntity1 = isExist(uploadId, "prices");
                            if (resultXmlEntity1 != null) {
                                resultXmlEntity.setId(resultXmlEntity1.getId());
                            }
                            resultXmlEntity.setType("prices");
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
    @Async
    public void reUploadFeed(UploadEntity uploadEntity) {
        //上传id
        Long uploadId = uploadEntity.getUploadId();

        // 商品列表
        List<ProductsEntity> productsEntityList = uploadEntity.getUploadProductsList();

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

        // 生成xml文件路径
        Map<String, String> filePathMap = new HashMap<>();

        // 操作项
        Integer productsResultStatus = uploadEntity.getProductsResultStatus();
        Integer relationshipsResultStatus = uploadEntity.getRelationshipsResultStatus();
        Integer imagesResultStatus = uploadEntity.getImagesResultStatus();
        Integer inventoryResultStatus = uploadEntity.getInventoryResultStatus();
        Integer pricesResultStatus = uploadEntity.getPricesResultStatus();

        // 更新的操作，原来没有上传的操作项，现在也不上传
        if (productsResultStatus != null) {
            if (!productsResultStatus.equals(2)) {
                String productPath = generateProductXML(merchantId, productsEntityList, countryCode, uploadEntity.getAmazonCategoryNodeId());
                filePathMap.put("0", productPath);
            }
        }
        if (relationshipsResultStatus != null) {
            if (!relationshipsResultStatus.equals(2)) {
                String relationshipsPath = generateRelationshipsXML(productsEntityList, merchantId);
                filePathMap.put("1", relationshipsPath);
            }
        }
        if (imagesResultStatus != null) {
            if (!imagesResultStatus.equals(2)) {
                String imagesPath = generateImagesXML(productsEntityList, merchantId);
                filePathMap.put("2", imagesPath);
            }
        }
        if (inventoryResultStatus != null) {
            if (!inventoryResultStatus.equals(2)) {
                String inventoryPath = generateInventoryXML(productsEntityList, merchantId);
                filePathMap.put("3", inventoryPath);
            }
        }
        if (pricesResultStatus != null) {
            if (!pricesResultStatus.equals(2)) {
                String pricesPath = generatePricesXML(countryCode, productsEntityList, merchantId);
                filePathMap.put("4", pricesPath);
            }
        }

        // 上传xml
        FeedSubmissionInfoDto productFeedSubmissionInfoDto = null;
        // 0 是产品基本信息xml
        if (filePathMap.containsKey("0")) {
            // 产品信息上传
            productFeedSubmissionInfoDto = submitProductFeed(uploadId, serviceURL, merchantId, sellerDevAuthToken, uploadTypeMap.get("0"), filePathMap.get("0"), marketplaceIdList).get(0);
            //使用FeedSubmissionId获取的亚马逊对于xml的处理状态
            while (true) {
                try {
                    List<String> feedSubmissionList = new ArrayList<>();
                    feedSubmissionList.add(productFeedSubmissionInfoDto.getFeedSubmissionId());
                    productFeedSubmissionInfoDto = getFeedSubmissionListAsync(uploadId, serviceURL, merchantId, sellerDevAuthToken, feedSubmissionList).get(0);

                    if (productFeedSubmissionInfoDto.getFeedProcessingStatus().equals(1)) {
                        break;
                    }
                    // 出现如下三种情况，总状态变失败。
                    if (productFeedSubmissionInfoDto.getFeedProcessingStatus().equals(3)) {
                        List<FeedSubmissionInfoDto> tempList = new ArrayList<>();
                        tempList.add(productFeedSubmissionInfoDto);
                        updateFeedUpload(tempList, 3);
                        break;
                    }
                    // 设置睡眠的时间 60 秒
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


        // FeedSubmissionInfoDto 数据存放，等待上传
        updateFeedUpload(feedSubmissionInfoDtoList, 0);

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
        updateFeedUpload(feedSubmissionInfoDtoList, 1);

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

        UploadEntity updateUploadEntity = new UploadEntity();
        updateUploadEntity.setUpdateTime(new Date());
        updateUploadEntity.setUploadId(uploadId);
        String tempPath;
        List<Integer> typeStatus = new ArrayList();
        for (int i = 0; i < feedSubmissionInfoDtoList.size(); i++) {

            tempPath = "";
            String submissionId = feedSubmissionInfoDtoList.get(i).getFeedSubmissionId();
            tempPath = fileStoragePath + "FeedSubmissionResult/" + submissionId + "_SubmissionResult.xml";
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
                            resultXmlEntity1 = isExist(uploadId, "products");
                            if (resultXmlEntity1 != null) {
                                resultXmlEntity.setId(resultXmlEntity1.getId());
                            }
                            resultXmlEntity.setType("products");
                            updateUploadEntity.setProductsResultStatus(tempStatus);
                            break;
                        case "_POST_PRODUCT_RELATIONSHIP_DATA_":
                            resultXmlEntity1 = isExist(uploadId, "relationships");
                            if (resultXmlEntity1 != null) {
                                resultXmlEntity.setId(resultXmlEntity1.getId());
                            }
                            resultXmlEntity.setType("relationships");
                            updateUploadEntity.setProductsResultStatus(tempStatus);
                            break;
                        case "_POST_PRODUCT_IMAGE_DATA_":
                            resultXmlEntity1 = isExist(uploadId, "images");
                            if (resultXmlEntity1 != null) {
                                resultXmlEntity.setId(resultXmlEntity1.getId());
                            }
                            resultXmlEntity.setType("images");
                            updateUploadEntity.setProductsResultStatus(tempStatus);
                            break;
                        case "_POST_INVENTORY_AVAILABILITY_DATA_":
                            resultXmlEntity1 = isExist(uploadId, "inventory");
                            if (resultXmlEntity1 != null) {
                                resultXmlEntity.setId(resultXmlEntity1.getId());
                            }
                            resultXmlEntity.setType("inventory");
                            updateUploadEntity.setProductsResultStatus(tempStatus);
                            break;
                        case "_POST_PRODUCT_PRICING_DATA_":
                            resultXmlEntity1 = isExist(uploadId, "prices");
                            if (resultXmlEntity1 != null) {
                                resultXmlEntity.setId(resultXmlEntity1.getId());
                            }
                            resultXmlEntity.setType("prices");
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
        String tempPath = path + "FeedSubmissionResult/";
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
            tempPath = tempPath + feedSubmissionInfoDtoList.get(i).getFeedSubmissionId() + "_SubmissionResult.xml";
            try {
                processingResult = new FileOutputStream(tempPath);
                request.setFeedSubmissionResultOutputStream(processingResult);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            tempPath = path + "FeedSubmissionResult/";
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
    public void updateFeedUpload(List<FeedSubmissionInfoDto> feedSubmissionInfoDtoList, int uploadState) {
        UploadEntity uploadEntity = new UploadEntity();
        uploadEntity.setUploadId(feedSubmissionInfoDtoList.get(0).getUploadId());
        // 总状态改变
        uploadEntity.setUploadState(uploadState);
        uploadEntity.setUpdateTime(new Date());
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
                temp = 2;
                if (!analysisFeedSubmissionResultDto.getMessagesWithWarning().equals(0)) {
                    temp = 4;
                }
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
    public String generateProductXMLByClothing(Long uploadId, String merchantIdentifierText, List<ProductsEntity> productsList, String countryCode) {
        // 获取模板数据
        EntityWrapper<FieldMiddleEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("upload_id", uploadId);
        List<FieldMiddleEntity> fieldMiddleEntitieList = fieldMiddleService.selectList(wrapper);
        Map<String, Object> valueMap = new HashMap<>();
        for (int i = 0; i < fieldMiddleEntitieList.size(); i++) {
            valueMap.put(fieldMiddleEntitieList.get(i).getFieldName(), fieldMiddleEntitieList.get(i).getValue());
        }

        // 获取上传数据
        UploadEntity uploadEntity = uploadService.selectById(uploadId);

        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("AmazonEnvelope");
        root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance").addAttribute("xsi:noNamespaceSchemaLocation", "https://images-na.ssl-images-amazon.com/images/G/01/rainier/help/xsd/release_1_9/amzn-envelope.xsd");
        Element header = root.addElement("Header");
        Element documentVersion = header.addElement("DocumentVersion");
        documentVersion.addText("1.01");
        Element merchantIdentifier = header.addElement("MerchantIdentifier");
        merchantIdentifier.addText(merchantIdentifierText);
        Element messageType = root.addElement("MessageType");
        messageType.addText("Product");
        Element purgeAndReplace = root.addElement("PurgeAndReplace");
        purgeAndReplace.addText("false");
        int messageId = 1;
        for (int i = 0; i < productsList.size(); i++) {
            ProductsEntity productsEntity = productsList.get(i);

            Element message = root.addElement("Message");
            Element messageID = message.addElement("MessageID");
            messageID.addText(messageId + "");
            messageId++;
            Element operationType = message.addElement("OperationType");
            operationType.addText("Update");
            Element product = message.addElement("Product");
            Element sku = product.addElement("SKU");
            sku.addText(productsEntity.getProductSku());

            List<VariantsInfoEntity> variantsInfoEntityList = variantsInfoService.selectList(new EntityWrapper<VariantsInfoEntity>().eq("product_id", productsEntity.getProductId()).orderBy(true, "variant_sort", true));

            // 没有变体
            boolean isVariant = false;
            if (variantsInfoEntityList.size() == 0) {
                if (StringUtils.isNotBlank(productsEntity.getEanCode())) {
                    Element standardProductID = product.addElement("StandardProductID");
                    Element type = standardProductID.addElement("Type");
                    type.addText("EAN");
                    Element value = standardProductID.addElement("Value");
                    value.addText(productsEntity.getEanCode());
                } else if (StringUtils.isNotBlank(productsEntity.getUpcCode())) {
                    Element standardProductID = product.addElement("StandardProductID");
                    Element type = standardProductID.addElement("Type");
                    type.addText("UPC");
                    Element value = standardProductID.addElement("Value");
                    value.addText(productsEntity.getUpcCode());
                }
            } else {
                // 有变体
                isVariant = true;
            }

            Element productTaxCode = product.addElement("ProductTaxCode");
            productTaxCode.addText("A_GEN_TAX");

            Map<String, Object> countryMap = switchCountry(productsEntity, countryCode);
            Long freightId = Long.valueOf(String.valueOf(countryMap.get("freightId")));
            String money = String.valueOf(countryMap.get("money"));
            IntroductionEntity introductionEntity = introductionService.selectById(Long.valueOf(String.valueOf(countryMap.get("freightId"))));

            Element descriptionData = product.addElement("DescriptionData");

            // Title-商品标题
            Element title = descriptionData.addElement("Title");
            title.addText(introductionEntity.getProductTitle());

            // Brand-品牌
            Element brand = descriptionData.addElement("Brand");
            brand.addText(productsEntity.getBrandName());

            // Description-描述
            Element description = descriptionData.addElement("Description");
            description.addText(introductionEntity.getProductDescription());

            // BulletPoint-重点，有可能需要多个，但最多5个
            String[] kepPoints = introductionEntity.getKeyPoints().split("\r\n");
            if (kepPoints.length <= 5) {
                for (int j = 0; j < kepPoints.length; j++) {
                    Element bulletPoint = descriptionData.addElement("BulletPoint");
                    bulletPoint.addText(kepPoints[j]);
                }
            } else {
                for (int j = 0; j < 4; j++) {
                    Element bulletPoint = descriptionData.addElement("BulletPoint");
                    bulletPoint.addText(kepPoints[j]);
                }

            }

            // Manufacturer - 生产厂家
            Element manufacturer = descriptionData.addElement("Manufacturer");
            manufacturer.addText(productsEntity.getProducerName());

            // ItemType - 推荐节点
            AmazonCategoryEntity amazonCategoryEntity = amazonCategoryService.selectById(uploadEntity.getAmazonCategoryId());

            Element itemType = descriptionData.addElement("ItemType");
            itemType.addText(amazonCategoryEntity.getCategoryName());


            switch (countryCode) {
                // 加拿大
                case "CA":
                    // 德国
                case "DE":
                    // 西班牙
                case "ES":
                    // 法国
                case "FR":
                    // 英国
                case "GB":
                    // 意大利
                case "IT":
                    // 日本
                case "JP":
                    // RecommendedBrowseNode - 浏览节点
                    Element recommendedBrowseNode = descriptionData.addElement("RecommendedBrowseNode");
                    recommendedBrowseNode.addText(uploadEntity.getAmazonCategoryNodeId());
                    break;
                default:
                    break;
            }

            Element productData = product.addElement("ProductData");
            Element clothing = productData.addElement("Clothing");


            String variationThemeStr = "";
            if (isVariant) {
                // 有变体
                Element variationData = clothing.addElement("VariationData");
                Element parentage = variationData.addElement("Parentage");
                parentage.addText("parent");

                Element variationTheme = variationData.addElement("VariationTheme");
                if (variantsInfoEntityList.get(0).getVariantCombination().contains("*")) {
                    variationThemeStr = "SizeColor";
                    variationTheme.addText(variationThemeStr);
                } else if (productsEntity.getColorId() != null) {
                    variationThemeStr = "Color";
                    variationTheme.addText(variationThemeStr);
                } else if (productsEntity.getSizeId() != null) {
                    variationThemeStr = "Size";
                    variationTheme.addText(variationThemeStr);
                }

                Element vieClassificationData1 = clothing.addElement("ClassificationData");
                Element clothingType1 = vieClassificationData1.addElement("ClothingType");
                clothingType1.addText("Jinbei");
                Element department1 = vieClassificationData1.addElement("Department");
                department1.addText(String.valueOf(valueMap.get("Department")));
                Element styleKeywords1 = vieClassificationData1.addElement("StyleKeywords");
                styleKeywords1.addText("null");
                Element materialComposition1 = vieClassificationData1.addElement("MaterialComposition");
                materialComposition1.addText(String.valueOf(valueMap.get("MaterialComposition")));
                Element innerMaterial1 = vieClassificationData1.addElement("InnerMaterial");
                innerMaterial1.addText(String.valueOf(valueMap.get("InnerMaterial")));
                Element outerMaterial1 = vieClassificationData1.addElement("OuterMaterial");
                outerMaterial1.addText(String.valueOf(valueMap.get("OuterMaterial")));
                Element closureType1 = vieClassificationData1.addElement("ClosureType");
                closureType1.addText("null");
                Element careInstructions1 = vieClassificationData1.addElement("CareInstructions");
                careInstructions1.addText("null");
                Element warnings1 = vieClassificationData1.addElement("Warnings");
                warnings1.addText("Do not Expose to sun");
                Element customizableTemplateName1 = vieClassificationData1.addElement("CustomizableTemplateName");
                customizableTemplateName1.addText("null");
                Element styleName1 = vieClassificationData1.addElement("StyleName");
                styleName1.addText("null");
                Element wistStyle1 = vieClassificationData1.addElement("WaistStyle");
                wistStyle1.addText("null");
                Element countryName1 = vieClassificationData1.addElement("CountryName");
                countryName1.addText("null");
                Element modelName1 = vieClassificationData1.addElement("ModelName");
                modelName1.addText("null");
                Element modelNumber1 = vieClassificationData1.addElement("ModelNumber");
                modelNumber1.addText("null");
                Element sizeMap1 = vieClassificationData1.addElement("SizeMap");
                sizeMap1.addText("null");
                Element beltStyle1 = vieClassificationData1.addElement("BeltStyle");
                beltStyle1.addText("null");
                Element bottomStyle1 = vieClassificationData1.addElement("BottomStyle");
                bottomStyle1.addText("null");
                Element character1 = vieClassificationData1.addElement("Character");
                character1.addText("null");
                Element controlType1 = vieClassificationData1.addElement("ControlType");
                controlType1.addText("null");
                Element cuffType1 = vieClassificationData1.addElement("CuffType");
                cuffType1.addText("null");
                Element fabricType1 = vieClassificationData1.addElement("FabricType");
                fabricType1.addText("null");
                Element fabricWash1 = vieClassificationData1.addElement("FabricWash");
                fabricWash1.addText("null");
                Element fitType1 = vieClassificationData1.addElement("FitType");
                fitType1.addText("null");
                Element fitToSizeDescription1 = vieClassificationData1.addElement("FitToSizeDescription");
                fitToSizeDescription1.addText("null");
                Element frontPleatType1 = vieClassificationData1.addElement("FrontPleatType");
                frontPleatType1.addText("null");
                Element includedComponents1 = vieClassificationData1.addElement("IncludedComponents");
                includedComponents1.addText("null");
                Element laptopCapacity1 = vieClassificationData1.addElement("LaptopCapacity");
                laptopCapacity1.addText("null");
                Element legDiameter1 = vieClassificationData1.addElement("LegDiameter");
                legDiameter1.addText("null");
                Element legStyle1 = vieClassificationData1.addElement("LegStyle");
                legStyle1.addText("null");
                Element materialType1 = vieClassificationData1.addElement("MaterialType");
                materialType1.addText("null");
                Element mfrWarrantyDescriptionLabor1 = vieClassificationData1.addElement("MfrWarrantyDescriptionLabor");
                mfrWarrantyDescriptionLabor1.addText("null");
                Element mfrWarrantyDescriptionParts1 = vieClassificationData1.addElement("MfrWarrantyDescriptionParts");
                mfrWarrantyDescriptionParts1.addText("null");
                Element mfrWarrantyDescriptionType1 = vieClassificationData1.addElement("MfrWarrantyDescriptionType");
                mfrWarrantyDescriptionType1.addText("null");
                Element neckStyle1 = vieClassificationData1.addElement("NeckStyle");
                neckStyle1.addText("null");
                Element patternStyle1 = vieClassificationData1.addElement("PatternStyle");
                patternStyle1.addText("null");
                Element collectionName1 = vieClassificationData1.addElement("CollectionName");
                collectionName1.addText("null");
                Element frameMaterialType1 = vieClassificationData1.addElement("FrameMaterialType");
                frameMaterialType1.addText("null");
                Element lensMaterialType1 = vieClassificationData1.addElement("LensMaterialType");
                lensMaterialType1.addText("null");
                Element polarizationType1 = vieClassificationData1.addElement("PolarizationType");
                polarizationType1.addText("null");
                Element bridgeWidth1 = vieClassificationData1.addElement("BridgeWidth");
                bridgeWidth1.addText("null");
                Element pocketDescription1 = vieClassificationData1.addElement("PocketDescription");
                pocketDescription1.addText("null");
                Element regionOfOrigin1 = vieClassificationData1.addElement("RegionOfOrigin");
                regionOfOrigin1.addText("null");
                Element riseStyle1 = vieClassificationData1.addElement("RiseStyle");
                riseStyle1.addText("null");
                Element safetyWarning1 = vieClassificationData1.addElement("SafetyWarning");
                safetyWarning1.addText("Do not Expose to sun");
                Element sellerWarrantyDescription1 = vieClassificationData1.addElement("SellerWarrantyDescription");
                sellerWarrantyDescription1.addText("null");
                Element specialFeature1 = vieClassificationData1.addElement("SpecialFeature");
                specialFeature1.addText("null");
                Element targetGender1 = vieClassificationData1.addElement("TargetGender");
                targetGender1.addText("unisex");
                Element theme1 = vieClassificationData1.addElement("Theme");
                theme1.addText("null");
                Element topStyle1 = vieClassificationData1.addElement("TopStyle");
                topStyle1.addText("null");
                Element underwireType1 = vieClassificationData1.addElement("UnderwireType");
                underwireType1.addText("null");
                Element wheelType1 = vieClassificationData1.addElement("WheelType");
                wheelType1.addText("null");
                Element strapType1 = vieClassificationData1.addElement("StrapType");
                strapType1.addText("null");
                Element toeShape1 = vieClassificationData1.addElement("ToeShape");
                toeShape1.addText("null");
                Element warrantyType1 = vieClassificationData1.addElement("WarrantyType");
                warrantyType1.addText("null");
                Element warrantyDescription1 = vieClassificationData1.addElement("WarrantyDescription");
                warrantyDescription1.addText("null");
                Element occasionType1 = vieClassificationData1.addElement("OccasionType");
                occasionType1.addText("null");
                Element leatherType1 = vieClassificationData1.addElement("LeatherType");
                leatherType1.addText("null");
                Element harmonizedCode1 = vieClassificationData1.addElement("HarmonizedCode");
                harmonizedCode1.addText("null");
                Element contributor1 = vieClassificationData1.addElement("Contributor");
                contributor1.addText("null");
                Element supportType1 = vieClassificationData1.addElement("SupportType");
                supportType1.addText("null");
                Element weaveType1 = vieClassificationData1.addElement("WeaveType");
                weaveType1.addText("null");
                Element embroideryType1 = vieClassificationData1.addElement("EmbroideryType");
                embroideryType1.addText("null");
                Element designName1 = vieClassificationData1.addElement("DesignName");
                designName1.addText("null");
                Element collectionDescription1 = vieClassificationData1.addElement("CollectionDescription");
                collectionDescription1.addText("null");
                Element specificUsesForProduct1 = vieClassificationData1.addElement("SpecificUsesForProduct");
                specificUsesForProduct1.addText("null");
                Element patternName1 = vieClassificationData1.addElement("PatternName");
                patternName1.addText("null");
                Element shellType1 = vieClassificationData1.addElement("ShellType");
                shellType1.addText("null");

                Element powerSource = clothing.addElement("PowerSource");
                powerSource.addText("null");
                Element itemLengthDescription = clothing.addElement("ItemLengthDescription");
                itemLengthDescription.addText("null");
                Element codabar = clothing.addElement("Codabar");
                codabar.addText("null");

                for (int j = 0; j < variantsInfoEntityList.size(); j++) {
                    VariantsInfoEntity variantsInfoEntity = variantsInfoEntityList.get(j);
                    Element vieMessage = root.addElement("Message");
                    Element vieMessageID = vieMessage.addElement("MessageID");
                    vieMessageID.addText(messageId + "");
                    messageId++;
                    Element vieOperationType = vieMessage.addElement("OperationType");
                    vieOperationType.addText("Update");
                    Element vieProduct = vieMessage.addElement("Product");
                    Element vieSku = vieProduct.addElement("SKU");
                    vieSku.addText(variantsInfoEntity.getVariantSku());

                    Element standardProductID = vieProduct.addElement("StandardProductID");
                    Element type = standardProductID.addElement("Type");
                    type.addText("EAN");
                    Element value = standardProductID.addElement("Value");
                    value.addText(variantsInfoEntity.getEanCode());

                    Element vieProductTaxCode = vieProduct.addElement("ProductTaxCode");
                    vieProductTaxCode.addText("A_GEN_TAX");

                    Element vieDescriptionData = vieProduct.addElement("DescriptionData");

                    // Title-商品标题
                    Element vieTitle = vieDescriptionData.addElement("Title");
                    vieTitle.addText(introductionEntity.getProductTitle());

                    // Brand-品牌
                    Element vieBrand = vieDescriptionData.addElement("Brand");
                    vieBrand.addText(productsEntity.getBrandName());

                    // Description-描述
                    Element vieDescription = vieDescriptionData.addElement("Description");
                    vieDescription.addText(introductionEntity.getProductDescription());

                    // BulletPoint-重点，有可能需要多个，但最多5个
                    if (kepPoints.length <= 5) {
                        for (int k = 0; k < kepPoints.length; k++) {
                            Element bulletPoint = vieDescriptionData.addElement("BulletPoint");
                            bulletPoint.addText(kepPoints[k]);
                        }
                    } else {
                        for (int f = 0; f < 4; f++) {
                            Element bulletPoint = vieDescriptionData.addElement("BulletPoint");
                            bulletPoint.addText(kepPoints[f]);
                        }
                    }

                    // Manufacturer - 生产厂家
                    Element vieManufacturer = vieDescriptionData.addElement("Manufacturer");
                    vieManufacturer.addText(productsEntity.getProducerName());

                    // ItemType - 推荐节点
                    Element vieItemType = vieDescriptionData.addElement("ItemType");
                    vieItemType.addText(amazonCategoryEntity.getCategoryName());

                    switch (countryCode) {
                        // 加拿大
                        case "CA":
                            // 德国
                        case "DE":
                            // 西班牙
                        case "ES":
                            // 法国
                        case "FR":
                            // 英国
                        case "GB":
                            // 意大利
                        case "IT":
                            // 日本
                        case "JP":
                            // RecommendedBrowseNode - 浏览节点
                            Element vieRecommendedBrowseNode = vieDescriptionData.addElement("RecommendedBrowseNode");
                            vieRecommendedBrowseNode.addText(uploadEntity.getAmazonCategoryNodeId());
                            break;
                        default:
                            break;
                    }

                    Element vieProductData = vieProduct.addElement("ProductData");
                    Element vieClothing = vieProductData.addElement("Clothing");

                    Element vieVariationData = vieClothing.addElement("VariationData");
                    Element vieParentage = vieVariationData.addElement("Parentage");
                    vieParentage.addText("child");

                    switch (variationThemeStr) {
                        case "SizeColor":
                            Element vieSize = vieVariationData.addElement("Size");
                            String[] str = variantsInfoEntity.getVariantCombination().split("\\*");
                            vieSize.addText(str[1]);
                            Element vieCode = vieVariationData.addElement("Code");
                            vieCode.addText(str[0]);
                            break;
                        case "Color":
                            Element vieCode1 = vieVariationData.addElement("Code");
                            vieCode1.addText(variantsInfoEntity.getVariantCombination());
                            break;
                        case "Size":
                            Element vieSize2 = vieVariationData.addElement("Size");
                            vieSize2.addText(variantsInfoEntity.getVariantCombination());
                            break;
                        default:
                            break;
                    }
                    Element vieVariationTheme = vieVariationData.addElement("VariationTheme");
                    vieVariationTheme.addText(variationThemeStr);

                    Element vieClassificationData = vieClothing.addElement("ClassificationData");
                    Element clothingType = vieClassificationData.addElement("ClothingType");
                    clothingType.addText("Jinbei");
                    Element department = vieClassificationData.addElement("Department");
                    department.addText(String.valueOf(valueMap.get("Department")));
                    Element styleKeywords = vieClassificationData.addElement("StyleKeywords");
                    styleKeywords.addText("null");
                    Element materialComposition = vieClassificationData.addElement("MaterialComposition");
                    materialComposition.addText(String.valueOf(valueMap.get("MaterialComposition")));
                    Element innerMaterial = vieClassificationData.addElement("InnerMaterial");
                    innerMaterial.addText(String.valueOf(valueMap.get("InnerMaterial")));
                    Element outerMaterial = vieClassificationData.addElement("OuterMaterial");
                    outerMaterial.addText(String.valueOf(valueMap.get("OuterMaterial")));
                    Element closureType = vieClassificationData.addElement("ClosureType");
                    closureType.addText("null");
                    Element careInstructions = vieClassificationData.addElement("CareInstructions");
                    careInstructions.addText("null");
                    Element warnings = vieClassificationData.addElement("Warnings");
                    warnings.addText("not");
                    Element customizableTemplateName = vieClassificationData.addElement("CustomizableTemplateName");
                    customizableTemplateName.addText("null");
                    Element styleName = vieClassificationData.addElement("StyleName");
                    styleName.addText("null");
                    Element wistStyle = vieClassificationData.addElement("WaistStyle");
                    wistStyle.addText("null");
                    Element countryName = vieClassificationData.addElement("CountryName");
                    countryName.addText("null");
                    Element modelName = vieClassificationData.addElement("ModelName");
                    modelName.addText("null");
                    Element modelNumber = vieClassificationData.addElement("ModelNumber");
                    modelNumber.addText("null");
                    Element sizeMap = vieClassificationData.addElement("SizeMap");
                    sizeMap.addText("null");
                    Element beltStyle = vieClassificationData.addElement("BeltStyle");
                    beltStyle.addText("null");
                    Element bottomStyle = vieClassificationData.addElement("BottomStyle");
                    bottomStyle.addText("null");
                    Element character = vieClassificationData.addElement("Character");
                    character.addText("null");
                    Element controlType = vieClassificationData.addElement("ControlType");
                    controlType.addText("null");
                    Element cuffType = vieClassificationData.addElement("CuffType");
                    cuffType.addText("null");
                    Element fabricType = vieClassificationData.addElement("FabricType");
                    fabricType.addText("null");
                    Element fabricWash = vieClassificationData.addElement("FabricWash");
                    fabricWash.addText("null");
                    Element fitType = vieClassificationData.addElement("FitType");
                    fitType.addText("null");
                    Element fitToSizeDescription = vieClassificationData.addElement("FitToSizeDescription");
                    fitToSizeDescription.addText("null");
                    Element frontPleatType = vieClassificationData.addElement("FrontPleatType");
                    frontPleatType.addText("null");
                    Element includedComponents = vieClassificationData.addElement("IncludedComponents");
                    includedComponents.addText("null");
                    Element laptopCapacity = vieClassificationData.addElement("LaptopCapacity");
                    laptopCapacity.addText("null");
                    Element legDiameter = vieClassificationData.addElement("LegDiameter");
                    legDiameter.addText("null");
                    Element legStyle = vieClassificationData.addElement("LegStyle");
                    legStyle.addText("null");
                    Element materialType = vieClassificationData.addElement("MaterialType");
                    materialType.addText("null");
                    Element mfrWarrantyDescriptionLabor = vieClassificationData.addElement("MfrWarrantyDescriptionLabor");
                    mfrWarrantyDescriptionLabor.addText("null");
                    Element mfrWarrantyDescriptionParts = vieClassificationData.addElement("MfrWarrantyDescriptionParts");
                    mfrWarrantyDescriptionParts.addText("null");
                    Element mfrWarrantyDescriptionType = vieClassificationData.addElement("MfrWarrantyDescriptionType");
                    mfrWarrantyDescriptionType.addText("null");
                    Element neckStyle = vieClassificationData.addElement("NeckStyle");
                    neckStyle.addText("null");
                    Element patternStyle = vieClassificationData.addElement("PatternStyle");
                    patternStyle.addText("null");
                    Element collectionName = vieClassificationData.addElement("CollectionName");
                    collectionName.addText("null");
                    Element frameMaterialType = vieClassificationData.addElement("FrameMaterialType");
                    frameMaterialType.addText("null");
                    Element lensMaterialType = vieClassificationData.addElement("LensMaterialType");
                    lensMaterialType.addText("null");
                    Element polarizationType = vieClassificationData.addElement("PolarizationType");
                    polarizationType.addText("null");
                    Element bridgeWidth = vieClassificationData.addElement("BridgeWidth");
                    bridgeWidth.addText("null");
                    Element pocketDescription = vieClassificationData.addElement("PocketDescription");
                    pocketDescription.addText("null");
                    Element regionOfOrigin = vieClassificationData.addElement("RegionOfOrigin");
                    regionOfOrigin.addText("null");
                    Element riseStyle = vieClassificationData.addElement("RiseStyle");
                    riseStyle.addText("null");
                    Element safetyWarning = vieClassificationData.addElement("SafetyWarning");
                    safetyWarning.addText("Do not Expose to sun");
                    Element sellerWarrantyDescription = vieClassificationData.addElement("SellerWarrantyDescription");
                    sellerWarrantyDescription.addText("null");
                    Element specialFeature = vieClassificationData.addElement("SpecialFeature");
                    specialFeature.addText("null");
                    Element targetGender = vieClassificationData.addElement("TargetGender");
                    targetGender.addText("unisex");
                    Element theme = vieClassificationData.addElement("Theme");
                    theme.addText("null");
                    Element topStyle = vieClassificationData.addElement("TopStyle");
                    topStyle.addText("null");
                    Element underwireType = vieClassificationData.addElement("UnderwireType");
                    underwireType.addText("null");
                    Element wheelType = vieClassificationData.addElement("WheelType");
                    wheelType.addText("null");
                    Element strapType = vieClassificationData.addElement("StrapType");
                    strapType.addText("null");
                    Element toeShape = vieClassificationData.addElement("ToeShape");
                    toeShape.addText("null");
                    Element warrantyType = vieClassificationData.addElement("WarrantyType");
                    warrantyType.addText("null");
                    Element warrantyDescription = vieClassificationData.addElement("WarrantyDescription");
                    warrantyDescription.addText("null");
                    Element occasionType = vieClassificationData.addElement("OccasionType");
                    occasionType.addText("null");
                    Element leatherType = vieClassificationData.addElement("LeatherType");
                    leatherType.addText("null");
                    Element harmonizedCode = vieClassificationData.addElement("HarmonizedCode");
                    harmonizedCode.addText("null");
                    Element contributor = vieClassificationData.addElement("Contributor");
                    contributor.addText("null");
                    Element supportType = vieClassificationData.addElement("SupportType");
                    supportType.addText("null");
                    Element weaveType = vieClassificationData.addElement("WeaveType");
                    weaveType.addText("null");
                    Element embroideryType = vieClassificationData.addElement("EmbroideryType");
                    embroideryType.addText("null");
                    Element designName = vieClassificationData.addElement("DesignName");
                    designName.addText("null");
                    Element collectionDescription = vieClassificationData.addElement("CollectionDescription");
                    collectionDescription.addText("null");
                    Element specificUsesForProduct = vieClassificationData.addElement("SpecificUsesForProduct");
                    specificUsesForProduct.addText("null");
                    Element patternName = vieClassificationData.addElement("PatternName");
                    patternName.addText("null");
                    Element shellType = vieClassificationData.addElement("ShellType");
                    shellType.addText("null");

                    Element powerSource1 = vieClothing.addElement("PowerSource");
                    powerSource1.addText("null");
                    Element itemLengthDescription1 = vieClothing.addElement("ItemLengthDescription");
                    itemLengthDescription1.addText("null");
                    Element codabar1 = vieClothing.addElement("Codabar");
                    codabar1.addText("null");
                }
            } else {
                // 没有变体
                Element classificationData = clothing.addElement("ClassificationData");
                Element clothingType = classificationData.addElement("ClothingType");
                clothingType.addText("Jinbei");
                Element department = classificationData.addElement("Department");
                department.addText(String.valueOf(valueMap.get("Department")));
                Element styleKeywords = classificationData.addElement("StyleKeywords");
                styleKeywords.addText("null");
                Element materialComposition = classificationData.addElement("MaterialComposition");
                materialComposition.addText(String.valueOf(valueMap.get("MaterialComposition")));
                Element innerMaterial = classificationData.addElement("InnerMaterial");
                innerMaterial.addText(String.valueOf(valueMap.get("InnerMaterial")));
                Element outerMaterial = classificationData.addElement("OuterMaterial");
                outerMaterial.addText(String.valueOf(valueMap.get("OuterMaterial")));
                Element closureType = classificationData.addElement("ClosureType");
                closureType.addText("null");
                Element careInstructions = classificationData.addElement("CareInstructions");
                careInstructions.addText("null");
                Element warnings = classificationData.addElement("Warnings");
                warnings.addText("not");
                Element customizableTemplateName = classificationData.addElement("CustomizableTemplateName");
                customizableTemplateName.addText("null");
                Element styleName = classificationData.addElement("StyleName");
                styleName.addText("null");
                Element wistStyle = classificationData.addElement("WaistStyle");
                wistStyle.addText("null");
                Element countryName = classificationData.addElement("CountryName");
                countryName.addText("null");
                Element modelName = classificationData.addElement("ModelName");
                modelName.addText("null");
                Element modelNumber = classificationData.addElement("ModelNumber");
                modelNumber.addText("null");
                Element sizeMap = classificationData.addElement("SizeMap");
                sizeMap.addText("null");
                Element beltStyle = classificationData.addElement("BeltStyle");
                beltStyle.addText("null");
                Element bottomStyle = classificationData.addElement("BottomStyle");
                bottomStyle.addText("null");
                Element character = classificationData.addElement("Character");
                character.addText("null");
                Element controlType = classificationData.addElement("ControlType");
                controlType.addText("null");
                Element cuffType = classificationData.addElement("CuffType");
                cuffType.addText("null");
                Element fabricType = classificationData.addElement("FabricType");
                fabricType.addText("null");
                Element fabricWash = classificationData.addElement("FabricWash");
                fabricWash.addText("null");
                Element fitType = classificationData.addElement("FitType");
                fitType.addText("null");
                Element fitToSizeDescription = classificationData.addElement("FitToSizeDescription");
                fitToSizeDescription.addText("null");
                Element frontPleatType = classificationData.addElement("FrontPleatType");
                frontPleatType.addText("null");
                Element includedComponents = classificationData.addElement("IncludedComponents");
                includedComponents.addText("null");
                Element laptopCapacity = classificationData.addElement("LaptopCapacity");
                laptopCapacity.addText("null");
                Element legDiameter = classificationData.addElement("LegDiameter");
                legDiameter.addText("null");
                Element legStyle = classificationData.addElement("LegStyle");
                legStyle.addText("null");
                Element materialType = classificationData.addElement("MaterialType");
                materialType.addText("null");
                Element mfrWarrantyDescriptionLabor = classificationData.addElement("MfrWarrantyDescriptionLabor");
                mfrWarrantyDescriptionLabor.addText("null");
                Element mfrWarrantyDescriptionParts = classificationData.addElement("MfrWarrantyDescriptionParts");
                mfrWarrantyDescriptionParts.addText("null");
                Element mfrWarrantyDescriptionType = classificationData.addElement("MfrWarrantyDescriptionType");
                mfrWarrantyDescriptionType.addText("null");
                Element neckStyle = classificationData.addElement("NeckStyle");
                neckStyle.addText("null");
                Element patternStyle = classificationData.addElement("PatternStyle");
                patternStyle.addText("null");
                Element collectionName = classificationData.addElement("CollectionName");
                collectionName.addText("null");
                Element frameMaterialType = classificationData.addElement("FrameMaterialType");
                frameMaterialType.addText("null");
                Element lensMaterialType = classificationData.addElement("LensMaterialType");
                lensMaterialType.addText("null");
                Element polarizationType = classificationData.addElement("PolarizationType");
                polarizationType.addText("null");
                Element bridgeWidth = classificationData.addElement("BridgeWidth");
                bridgeWidth.addText("null");
                Element pocketDescription = classificationData.addElement("PocketDescription");
                pocketDescription.addText("null");
                Element regionOfOrigin = classificationData.addElement("RegionOfOrigin");
                regionOfOrigin.addText("null");
                Element riseStyle = classificationData.addElement("RiseStyle");
                riseStyle.addText("null");
                Element safetyWarning = classificationData.addElement("SafetyWarning");
                safetyWarning.addText("Do not Expose to sun");
                Element sellerWarrantyDescription = classificationData.addElement("SellerWarrantyDescription");
                sellerWarrantyDescription.addText("null");
                Element specialFeature = classificationData.addElement("SpecialFeature");
                specialFeature.addText("null");
                Element targetGender = classificationData.addElement("TargetGender");
                targetGender.addText("unisex");
                Element theme = classificationData.addElement("Theme");
                theme.addText("null");
                Element topStyle = classificationData.addElement("TopStyle");
                topStyle.addText("null");
                Element underwireType = classificationData.addElement("UnderwireType");
                underwireType.addText("null");
                Element wheelType = classificationData.addElement("WheelType");
                wheelType.addText("null");
                Element strapType = classificationData.addElement("StrapType");
                strapType.addText("null");
                Element toeShape = classificationData.addElement("ToeShape");
                toeShape.addText("null");
                Element warrantyType = classificationData.addElement("WarrantyType");
                warrantyType.addText("null");
                Element warrantyDescription = classificationData.addElement("WarrantyDescription");
                warrantyDescription.addText("null");
                Element occasionType = classificationData.addElement("OccasionType");
                occasionType.addText("null");
                Element leatherType = classificationData.addElement("LeatherType");
                leatherType.addText("null");
                Element harmonizedCode = classificationData.addElement("HarmonizedCode");
                harmonizedCode.addText("null");
                Element contributor = classificationData.addElement("Contributor");
                contributor.addText("null");
                Element supportType = classificationData.addElement("SupportType");
                supportType.addText("null");
                Element weaveType = classificationData.addElement("WeaveType");
                weaveType.addText("null");
                Element embroideryType = classificationData.addElement("EmbroideryType");
                embroideryType.addText("null");
                Element designName = classificationData.addElement("DesignName");
                designName.addText("null");
                Element collectionDescription = classificationData.addElement("CollectionDescription");
                collectionDescription.addText("null");
                Element specificUsesForProduct = classificationData.addElement("SpecificUsesForProduct");
                specificUsesForProduct.addText("null");
                Element patternName = classificationData.addElement("PatternName");
                patternName.addText("null");
                Element shellType = classificationData.addElement("ShellType");
                shellType.addText("null");

                Element powerSource = clothing.addElement("PowerSource");
                powerSource.addText("null");
                Element itemLengthDescription = clothing.addElement("ItemLengthDescription");
                itemLengthDescription.addText("null");
                Element codabar = clothing.addElement("Codabar");
                codabar.addText("null");
            }

        }
        // 生成文件路径
        String path = fileStoragePath;
        String filePath = FileUtil.generateFilePath(path, "ProductByClothing");

        try {
            XMLUtil.writeXMLToFile(document, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (FileUtil.judeFileExists(filePath) == false) {
            return null;
        }
        return filePath;
    }

    @Override
    public Map<String, Object> switchCountry(ProductsEntity productsEntity, String countryCode) {
        Map<String, Object> map = new HashMap<>();
        String money = "";
        Long freightId = 0L;
        switch (countryCode) {
            // 加拿大
            case "CA":
                freightId = productsEntity.getCanadaFreight();
                money = "CAD";
                break;
            // 墨西哥
            case "MX":
                freightId = productsEntity.getMexicoFreight();
                money = "MXN";
                break;
            // 美国
            case "US":
                freightId = productsEntity.getAmericanFreight();
                money = "USD";
                break;
            // 德国
            case "DE":
                freightId = productsEntity.getGermanyFreight();
                money = "EUR";
                break;
            // 西班牙
            case "ES":
                freightId = productsEntity.getSpainFreight();
                money = "EUR";
                break;
            // 法国
            case "FR":
                freightId = productsEntity.getFranceFreight();
                money = "EUR";
                break;
            // 英国
            case "GB":
                freightId = productsEntity.getBritainFreight();
                money = "GBP";
                break;
            // 意大利
            case "IT":
                freightId = productsEntity.getItalyFreight();
                money = "EUR";
                break;
            // 澳大利亚
            case "AU":
                freightId = productsEntity.getAustraliaFreight();
                money = "AUD";
                break;
            // 日本
            case "JP":
                freightId = productsEntity.getJapanFreight();
                money = "JPY";
                break;
            default:
                break;
        }
        map.put("freightId", freightId);
        map.put("money", money);
        return map;
    }
}