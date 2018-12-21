package io.renren.modules.amazon.service.impl;

import com.amazonaws.mws.MarketplaceWebService;
import com.amazonaws.mws.MarketplaceWebServiceClient;
import com.amazonaws.mws.MarketplaceWebServiceConfig;
import com.amazonaws.mws.MarketplaceWebServiceException;
import com.amazonaws.mws.model.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.modules.amazon.dto.FeedSubmissionInfoDto;
import io.renren.modules.amazon.entity.AmazonGrantEntity;
import io.renren.modules.amazon.entity.AmazonGrantShopEntity;
import io.renren.modules.amazon.service.AmazonGrantService;
import io.renren.modules.amazon.service.AmazonGrantShopService;
import io.renren.modules.amazon.service.SubmitFeedService;
import io.renren.modules.amazon.util.ContentMD5Util;
import io.renren.modules.amazon.util.FileUtil;
import io.renren.modules.amazon.util.UploadFeedThread;
import io.renren.modules.amazon.util.XMLUtil;
import io.renren.modules.product.entity.*;
import io.renren.modules.product.service.*;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.Future;

@Service
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
                // 巴西
                case "BR":
                    money = "BRL";
                    break;
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
        String money = "USD";
        switch (countryCode) {
            // 巴西
            case "BR":
                money = "BRL";
                break;
            // 加拿大
            case "CA":
                money = "CAD";
                break;
            // 墨西哥
            case "MX":
                money = "MXN";
                break;
            // 美国
            case "US":
                money = "USD";
                break;
            // 德国
            case "DE":
                money = "EUR";
                break;
            // 西班牙
            case "ES":
                money = "EUR";
                break;
            // 法国
            case "FR":
                money = "EUR";
                break;
            // 英国
            case "GB":
                money = "GBP";
                break;

            // 意大利
            case "IT":
                money = "EUR";
                break;
            // 土耳其
            case "TR":
                money = "TRY";
                break;
            // 澳大利亚
            case "AU":
                money = "AUD";
                break;
            // 日本
            case "JP":
                money = "JPY";
                break;
        }
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
            Element price = message.addElement("Price");
            Element sku = price.addElement("SKU");
            sku.addText(productsEntity.getProductSku());
            Element standardPrice = price.addElement("StandardPrice");
            standardPrice.addAttribute("currency", money);
            standardPrice.addText(productsEntity.getPurchasePrice().toString());
            messageId++;

            // 变体价格
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
                    standardPrice1.addText(variantsInfoEntity.getVariantAddPrice().toString());
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
            if (filePathMap.containsKey(String.valueOf(i))){
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
    public void singleSubmitFeed(UploadEntity uploadEntity) {

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
        // 操作项
        String[] operateItemStr = uploadEntity.getOperateItem().split(",");

        // 生成xml文件路径
        Map<String, String> filePathMap = new HashMap<>();
        for (int i = 0; i < operateItemStr.length; i++) {
            switch (operateItemStr[i]) {
                // 0 基本信息
                case "0":
                    String productPath = generateProductXML(merchantId, productsEntityList, countryCode, uploadEntity.getAmazonCategoryNodeId());
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
            }
        }

        new UploadFeedThread(uploadId, serviceURL, merchantId, sellerDevAuthToken, uploadTypeMap, filePathMap, marketplaceIdList).start();
    }

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
                            feedSubmissionInfoDto.setFeedProcessingStatus(feedSubmissionInfo.getFeedProcessingStatus());
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

    public static void invokeGetFeedSubmissionResult(MarketplaceWebService service, List<GetFeedSubmissionResultRequest> requests) {
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
                // Original request corresponding to this response, if needed:
                GetFeedSubmissionResultRequest originalRequest = requests.get(responses.indexOf(future));
                System.out.println("Result md5checksum : " + response.getGetFeedSubmissionResultResult().getMD5Checksum());
                System.out.println("Response request id: " + response.getResponseMetadata().getRequestId());
                System.out.println("FeedSubmissionResult: ");
                System.out.println(requests.get(responses.indexOf(future)).getFeedSubmissionResultOutputStream().toString());
                System.out.println(response.getResponseHeaderMetadata());
                System.out.println();
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
    }

    @Override
    public List<FeedSubmissionInfoDto> getFeedSubmissionListAsync(Long uploadId,String serviceURL, String merchantId, String sellerDevAuthToken,List<String> feedSubmissionIdList) {
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

        return invokeGetFeedSubmissionList(uploadId,service, requests);
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
                    GetFeedSubmissionListResult  getFeedSubmissionListResult = response.getGetFeedSubmissionListResult();
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
                            feedSubmissionInfoDto.setFeedProcessingStatus(feedSubmissionInfo.getFeedProcessingStatus());
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

    public void getFeedSubmissionResultAsync(String serviceURL) {

        MarketplaceWebService service = getAsyncService(serviceURL);

        List<GetFeedSubmissionResultRequest> requests = new ArrayList<>();

        GetFeedSubmissionResultRequest requestOne = new GetFeedSubmissionResultRequest();
        requests.add(requestOne);
        invokeGetFeedSubmissionResult(service, requests);
    }

    @Override
    public MarketplaceWebService getService(String serviceURL) {
        MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();
        config.setServiceURL(serviceURL);
        MarketplaceWebService service = new MarketplaceWebServiceClient(accessKey, secretKey, appName, appVersion, config);
        return service;
    }

    @Override
    public MarketplaceWebService getAsyncService(String serviceURL) {
        MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();
        config.setServiceURL(serviceURL);
        config.setMaxAsyncThreads(35);
        MarketplaceWebService service = new MarketplaceWebServiceClient(accessKey, secretKey, appName, appVersion, config);
        return service;
    }

    @Override
    public void updateFeedUpload(List<FeedSubmissionInfoDto> feedSubmissionInfoDtoList, int uploadState){

        // TODO: 2018/12/22 是获取报告后进行保存
        UploadEntity uploadEntity = new UploadEntity();
        uploadEntity.setUploadId(feedSubmissionInfoDtoList.get(0).getUploadId());
        uploadEntity.setUploadState(uploadState);
        uploadEntity.setUpdateTime(new Date());
        for (int i = 0; i < feedSubmissionInfoDtoList.size(); i++) {

            /*
            String feedSubmissionId= feedSubmissionInfoDtoList.get(i).getFeedSubmissionId();
            String feedProcessingStatus= feedSubmissionInfoDtoList.get(i).getFeedProcessingStatus();
            switch (feedSubmissionInfoDtoList.get(i).getFeedType()){
                case "_POST_PRODUCT_DATA_":
                    uploadEntity.setProductsSubmitId(feedSubmissionId);
                    uploadEntity.setProductsResultStatus(feedProcessingStatus);
                    break;
                case "_POST_PRODUCT_RELATIONSHIP_DATA_":
                    uploadEntity.setRelationshipsSubmitId(feedSubmissionId);
                    uploadEntity.setRelationshipsResultStatus(feedProcessingStatus);
                    break;
                case "_POST_PRODUCT_IMAGE_DATA_":
                    uploadEntity.setImagesSubmitId(feedSubmissionId);
                    uploadEntity.setImagesResultStatus(feedProcessingStatus);
                    break;
                case "_POST_INVENTORY_AVAILABILITY_DATA_":
                    uploadEntity.setInventorySubmitId(feedSubmissionId);
                    uploadEntity.setInventoryResultStatus(feedProcessingStatus);
                    break;
                case "_POST_PRODUCT_PRICING_DATA_":
                    uploadEntity.setPricesSubmitId(feedSubmissionId);
                    uploadEntity.setPricesResultStatus(feedProcessingStatus);
                    break;
            }
            */

        }
        uploadService.updateById(uploadEntity);
    }
}