package io.renren.modules.amazon.service.impl;

import com.amazonaws.mws.MarketplaceWebService;
import com.amazonaws.mws.MarketplaceWebServiceClient;
import com.amazonaws.mws.MarketplaceWebServiceConfig;
import com.amazonaws.mws.MarketplaceWebServiceException;
import com.amazonaws.mws.model.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.modules.amazon.config.SubmitFeedConfig;
import io.renren.modules.amazon.entity.AmazonGrantEntity;
import io.renren.modules.amazon.entity.AmazonGrantShopEntity;
import io.renren.modules.amazon.service.AmazonGrantService;
import io.renren.modules.amazon.service.AmazonGrantShopService;
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

            if (productsEntity.getEanCode() != null || !"".equals(productsEntity.getEanCode()) || !StringUtils.isBlank(productsEntity.getEanCode())) {
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
            }

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
            Element recommendedBrowseNode = descriptionData.addElement("RecommendedBrowseNode");
            recommendedBrowseNode.addText(categoryNodeId);

            Element productData = product.addElement("ProductData");
            Element computers = productData.addElement("Computers");
            Element productType = computers.addElement("ProductType");
            Element keyboards = productType.addElement("Keyboards");

            String sizeStr = "";
            String colorStr = "";
            boolean isSize = false;
            boolean isColor = false;
            String typeStr = "";
            if (productsEntity.getSizeId() != null && productsEntity.getSizeId() != 0) {
                VariantParameterEntity sizeVariantParameter = variantParameterService.selectById(productsEntity.getSizeId());
                sizeStr = sizeVariantParameter.getParamsType();
                isSize = true;
            }
            if (productsEntity.getColorId() != null && productsEntity.getColorId() != 0) {
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
                    Element computers1 = productData1.addElement("Computers");
                    Element productType1 = computers1.addElement("ProductType");
                    Element keyboards1 = productType1.addElement("Keyboards");
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
    public String submitFeed(String serviceURL, String merchantId, String sellerDevAuthToken, String feedType, String filePath, List<String> marketplaceIdList) {

        String feedSubmissionId = null;

        MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();
        config.setServiceURL(serviceURL);
        MarketplaceWebService service = new MarketplaceWebServiceClient("AKIAJPTOJEGMM7G4FJQA", "1ZlBne3VgcLhoGUmXkD+TtOVztOzzGassbCDam6A",
                "mws", "1.1", config);

        IdList marketplaces = new IdList(marketplaceIdList);

        SubmitFeedRequest request = new SubmitFeedRequest();
        request.setMerchant(merchantId);
        request.setMWSAuthToken(sellerDevAuthToken);
        request.setMarketplaceIdList(marketplaces);
        request.setFeedType(feedType);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String md5 = "";
        try {
            md5 = ContentMD5Util.computeContentMD5HeaderValue(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        request.setContentMD5(md5);
        request.setFeedContent(fileInputStream);

        try {
            SubmitFeedResponse response = service.submitFeed(request);
            if (response.isSetSubmitFeedResult()) {
                SubmitFeedResult submitFeedResult = response
                        .getSubmitFeedResult();
                if (submitFeedResult.isSetFeedSubmissionInfo()) {
                    FeedSubmissionInfo feedSubmissionInfo = submitFeedResult
                            .getFeedSubmissionInfo();
                    if (feedSubmissionInfo.isSetFeedSubmissionId()) {
                        feedSubmissionId = feedSubmissionInfo.getFeedSubmissionId();
                    }
                }
            }
        } catch (MarketplaceWebServiceException ex) {
            System.out.println("Caught Exception: " + ex.getMessage());
            System.out.println("Response Status Code: " + ex.getStatusCode());
            System.out.println("Error Code: " + ex.getErrorCode());
            System.out.println("Error Type: " + ex.getErrorType());
            System.out.println("Request ID: " + ex.getRequestId());
            System.out.print("XML: " + ex.getXML());
            System.out.println("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata());
        }
        return feedSubmissionId;
    }

    @Override
    public List<String> submitAsyncFeed(String serviceURL, String merchantId, String sellerDevAuthToken, Map<String, String> typeMap, Map<String, String> filePathMap, List<String> marketplaceIdList) {
        IdList marketplaces = new IdList(marketplaceIdList);
        MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();
        config.setServiceURL(serviceURL);
        config.setMaxAsyncThreads(35);
        MarketplaceWebService service = new MarketplaceWebServiceClient("AKIAJPTOJEGMM7G4FJQA", "1ZlBne3VgcLhoGUmXkD+TtOVztOzzGassbCDam6A",
                "mws", "1.1", config);
        List<SubmitFeedRequest> submitFeedRequestList = new ArrayList<>();
        for (int i = 1; i <= filePathMap.size(); i++) {
            SubmitFeedRequest submitFeedRequest = new SubmitFeedRequest();
            submitFeedRequest.setMerchant(merchantId);
            submitFeedRequest.setMWSAuthToken(sellerDevAuthToken);
            submitFeedRequest.setMarketplaceIdList(marketplaces);
            submitFeedRequest.setFeedType(typeMap.get(i + ""));
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(filePathMap.get(i + ""));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String md5 = "";
            try {
                md5 = ContentMD5Util.computeContentMD5HeaderValue(fileInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            submitFeedRequest.setContentMD5(md5);
            submitFeedRequest.setFeedContent(fileInputStream);
            submitFeedRequestList.add(submitFeedRequest);
        }
        List<String> feedSubmissionId = invokeSubmitAsyncFeed(service, submitFeedRequestList);
        return feedSubmissionId;
    }

    @Override
    public List<String> singleSubmitFeed(UploadEntity uploadEntity) {
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
        String devToken = amazonGrantEntity.getGrantToken();
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

        String productKey = "0";
        if (filePathMap.containsKey(productKey)) {
            String productFeedSubmissionId = submitFeed(serviceURL, merchantId, devToken, uploadTypeMap.get(productKey), filePathMap.get(productKey), marketplaceIdList);
            System.out.println("产品基本信息FeedSubmissionId为了 = " + productFeedSubmissionId);
            List<String> list = new ArrayList<String>();
            list.add(productFeedSubmissionId);
            IdList idList = new IdList();
            idList.setId(list);
            GetFeedSubmissionListRequest feedSubmissionListRequest = new GetFeedSubmissionListRequest();
            feedSubmissionListRequest.setMerchant(merchantId);
            feedSubmissionListRequest.setMWSAuthToken(devToken);
            feedSubmissionListRequest.setFeedSubmissionIdList(idList);
            String feedProcessingStatus = null;
            while (true) {
                try {
                    // TODO: 2018/12/21 zjr service 封装
                    MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();
                    config.setServiceURL(serviceURL);
                    MarketplaceWebService service = new MarketplaceWebServiceClient("AKIAJPTOJEGMM7G4FJQA", "1ZlBne3VgcLhoGUmXkD+TtOVztOzzGassbCDam6A",
                            "mws", "1.1", config);

                    feedProcessingStatus = getFeedSubmissionList(service, feedSubmissionListRequest);
                    System.out.println("feedProcessingStatus = " + feedProcessingStatus);
                    if ("_DONE_".equals(feedProcessingStatus)) {
                        break;
                    }
                    // 设置暂停的时间 6 秒
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
          /*  List<String> feedSubmissionId = submitAsyncFeed(serviceURL, merchantId, devToken, typeMap, filePathMap, marketplaceIdList);
            feedSubmissionId.add(productFeedSubmissionId);*/
        }

        // TODO: 2018/12/21 todo
        return null;
}

    public List<String> invokeSubmitAsyncFeed(MarketplaceWebService service, List<SubmitFeedRequest> requests) {
        List<String> feedSubmissionIdList = new ArrayList<>();
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
                    FeedSubmissionInfo feedSubmissionInfo = response.getSubmitFeedResult().getFeedSubmissionInfo();
                    feedSubmissionIdList.add(feedSubmissionInfo.getFeedSubmissionId());
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
        return feedSubmissionIdList;
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

    public static String getFeedSubmissionList(MarketplaceWebService service, GetFeedSubmissionListRequest request) {
        try {
            GetFeedSubmissionListResponse response = service.getFeedSubmissionList(request);
            if (response.isSetGetFeedSubmissionListResult()) {
                GetFeedSubmissionListResult getFeedSubmissionListResult = response.getGetFeedSubmissionListResult();
                java.util.List<FeedSubmissionInfo> feedSubmissionInfoList = getFeedSubmissionListResult
                        .getFeedSubmissionInfoList();
                for (FeedSubmissionInfo feedSubmissionInfo : feedSubmissionInfoList) {
                    if (feedSubmissionInfo.isSetFeedProcessingStatus()) {
                        return feedSubmissionInfo.getFeedProcessingStatus();
                    }
                }
            }
        } catch (MarketplaceWebServiceException ex) {
        }
        return null;
    }

    public void submitAsyncFeed(String serviceURL, List<SubmitFeedRequest> requests) {

        MarketplaceWebService service = SubmitFeedConfig.getAsyncService(serviceURL);

        /*
        1.商家提交 SubmitFeed 操作
        2.亚马逊接收到数据，返回 FeedSubmissionId
        */
        List<String> feedSubmissionIdList = invokeSubmitAsyncFeed(service, requests);

        for (SubmitFeedRequest submitFeedRequest : requests) {

        }
        List<GetFeedSubmissionListRequest> getFeedSubmissionListRequestList = new ArrayList<>();
        if (feedSubmissionIdList.size() > 100) {

        } else if (feedSubmissionIdList.size() == 0) {

        } else {
            GetFeedSubmissionListRequest getFeedSubmissionListRequest = new GetFeedSubmissionListRequest();

        }

    }

    public List<String> invokeGetFeedSubmissionList(MarketplaceWebService service, List<GetFeedSubmissionListRequest> requests) {
        List<String> feedSubmissionIds = new ArrayList<>();
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
                    List<FeedSubmissionInfo> feedSubmissionInfoList = response.getGetFeedSubmissionListResult().getFeedSubmissionInfoList();
                    for (FeedSubmissionInfo feedSubmissionInfo : feedSubmissionInfoList) {
                        feedSubmissionIds.add(feedSubmissionInfo.getFeedSubmissionId());
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
        return feedSubmissionIds;
    }

    public void getFeedSubmissionResultAsync(String serviceURL) {
        MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();
        config.setServiceURL(serviceURL);
        config.setMaxAsyncThreads(35);
        MarketplaceWebService service = new MarketplaceWebServiceClient("AKIAJPTOJEGMM7G4FJQA", "1ZlBne3VgcLhoGUmXkD+TtOVztOzzGassbCDam6A",
                "mws", "1.1", config);

        List<GetFeedSubmissionResultRequest> requests = new ArrayList<>();

        GetFeedSubmissionResultRequest requestOne = new GetFeedSubmissionResultRequest();
        requests.add(requestOne);
        invokeGetFeedSubmissionResult(service, requests);
    }

}