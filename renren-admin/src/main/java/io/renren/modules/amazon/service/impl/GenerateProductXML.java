package io.renren.modules.amazon.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
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
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 生成xml
 */
@Component("generateProductXML")
public class GenerateProductXML {

    @Autowired
    private FieldMiddleService fieldMiddleService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private VariantsInfoService variantsInfoService;

    @Autowired
    private AmazonCategoryService amazonCategoryService;

    @Autowired
    private IntroductionService introductionService;

    @Autowired
    private VariantParameterService variantParameterService;

    @Autowired
    private ImageAddressService imageAddressService;

    @Autowired
    private FreightCostService freightCostService;

    @Autowired
    private AmazonRateService amazonRateService;

    @Value(("${file.path}"))
    private String fileStoragePath;

    /**
     * 生成产品基础信息xml
     *
     * @param merchantIdentifierText 卖家记号
     * @param productsList           产品列表
     * @param countryCode            国家代码
     * @return XML路径
     * @author zjr
     */
    public String generateDefaultProductXML(Long uploadId, String merchantIdentifierText, List<ProductsEntity> productsList, String countryCode) {
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
            if (productsEntity.getProductSku() != null) {
                Element sku = product.addElement("SKU");
                sku.addText(productsEntity.getProductSku());
            }

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
            String titleStr = "\\t";
            Element title = descriptionData.addElement("Title");
            if (introductionEntity.getProductTitle() != null) {
                if (introductionEntity.getProductTitle().length() > 200) {
                    titleStr = introductionEntity.getProductTitle().trim().substring(0, 200);
                } else {
                    titleStr = introductionEntity.getProductTitle();
                }
            }
            title.addText(titleStr);

            // Brand-品牌
            String brandStr = "\\t";
            Element brand = descriptionData.addElement("Brand");
            if (productsEntity.getBrandName() != null) {
                brandStr = productsEntity.getBrandName();
            }
            brand.addText(brandStr);

            // Description-描述
            String descriptionStr = "\\t";
            Element description = descriptionData.addElement("Description");
            if (introductionEntity.getProductDescription() != null) {
                if (introductionEntity.getProductDescription().length() > 2000) {
                    descriptionStr = introductionEntity.getProductDescription().trim().substring(0, 2000).replace("\n", "<br />");
                } else {
                    descriptionStr = introductionEntity.getProductDescription().replace("\n", "<br />");
                }
            }
            description.addText(descriptionStr);

            // BulletPoint-重点，有可能需要多个，但最多5个
            if (introductionEntity.getKeyPoints() != null) {
                String[] kepPoints = introductionEntity.getKeyPoints().split("\n");
                if (kepPoints.length <= 5) {
                    for (int j = 0; j < kepPoints.length; j++) {
                        String tempKepPoints;
                        Element bulletPoint = descriptionData.addElement("BulletPoint");
                        if (kepPoints[j].length() > 500) {
                            tempKepPoints = kepPoints[j].trim().substring(0, 500);
                        } else {
                            tempKepPoints = kepPoints[j].trim();
                        }
                        bulletPoint.addText(tempKepPoints);
                    }
                } else {
                    for (int j = 0; j < 4; j++) {
                        String tempKepPoints;
                        Element bulletPoint = descriptionData.addElement("BulletPoint");
                        if (kepPoints[j].length() > 500) {
                            tempKepPoints = kepPoints[j].trim().substring(0, 500);
                        } else {
                            tempKepPoints = kepPoints[j].trim();
                        }
                        bulletPoint.addText(tempKepPoints);
                    }

                }
            } else {
                Element bulletPoint = descriptionData.addElement("BulletPoint");
                bulletPoint.addText("\\t");
               /* for (int j = 0; j < 4; j++) {
                    Element bulletPoint = descriptionData.addElement("BulletPoint");
                    bulletPoint.addText("\\t");
                }*/
            }

            // Manufacturer - 生产厂家
            String manufacturerStr = "\\t";
            Element manufacturer = descriptionData.addElement("Manufacturer");
            if (productsEntity.getProducerName() != null) {
                manufacturerStr = productsEntity.getProducerName();
            }
            manufacturer.addText(manufacturerStr);

            // SearchTerms - 关键字
            Element searchTerms = descriptionData.addElement("SearchTerms");
            String searchTermsStr = "\\t";
            if (introductionEntity.getKeyWord() != null) {
                if (introductionEntity.getKeyWord().trim().length() > 250) {
                    searchTermsStr = introductionEntity.getKeyWord().trim().substring(0, 250);
                } else {
                    searchTermsStr = introductionEntity.getKeyWord().trim();
                }
            }
            searchTerms.addText(searchTermsStr);

            // ItemType - 推荐节点
            AmazonCategoryEntity amazonCategoryEntity = amazonCategoryService.selectOne(new EntityWrapper<AmazonCategoryEntity>().eq("id", uploadEntity.getAmazonCategoryId()));
            String itemTypeStr = "\\t";
            Element itemType = descriptionData.addElement("ItemType");
            if (amazonCategoryEntity.getCategoryName() != null) {
                itemTypeStr = amazonCategoryEntity.getCategoryName();
            }
            itemType.addText(itemTypeStr);

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
            Element beauty = productData.addElement("Beauty");
            Element productType = beauty.addElement("ProductType");
            Element meautyMisc = productType.addElement("BeautyMisc");

            String variationThemeStr = "";
            if (isVariant) {
                // 有变体
                Element variationData = meautyMisc.addElement("VariationData");
                Element parentage = variationData.addElement("Parentage");
                parentage.addText("parent");

                Element variationTheme = variationData.addElement("VariationTheme");
                System.out.println(productsEntity.getProductId());
                if (productsEntity.getColorId() != null && productsEntity.getSizeId() != null) {
                    variationThemeStr = "Size-Color";
                    variationTheme.addText(variationThemeStr);
                    /*Element colorMap = variationData.addElement("ColorMap");
                    colorMap.addText("white");*/
                    /*Element sizeMap = variationData.addElement("SizeMap");
                    sizeMap.addText("free");*/
                } else if (productsEntity.getColorId() != null) {
                    variationThemeStr = "Color";
                    variationTheme.addText(variationThemeStr);
                    /*Element colorMap = variationData.addElement("ColorMap");
                    colorMap.addText("white");*/
                } else if (productsEntity.getSizeId() != null) {
                    variationThemeStr = "Size";
                    variationTheme.addText(variationThemeStr);
                    /*Element sizeMap = variationData.addElement("SizeMap");
                    sizeMap.addText("free");*/
                }

                /*Element unitCount = meautyMisc.addElement("UnitCount");
                unitCount.addAttribute("unitOfMeasure", "oz");
                unitCount.addText("0.1");
                Element directions = meautyMisc.addElement("Directions");
                directions.addText("\\t");*/

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
                    System.out.println("变体id：" + variantsInfoEntity.getVariantId());
                    value.addText(variantsInfoEntity.getEanCode());

                    Element vieProductTaxCode = vieProduct.addElement("ProductTaxCode");
                    vieProductTaxCode.addText("A_GEN_TAX");

                    Element vieDescriptionData = vieProduct.addElement("DescriptionData");

                    // Title-商品标题
                    Element vieTitle = vieDescriptionData.addElement("Title");
                    vieTitle.addText(titleStr);

                    // Brand-品牌
                    Element vieBrand = vieDescriptionData.addElement("Brand");
                    vieBrand.addText(brandStr);

                    // Description-描述
                    Element vieDescription = vieDescriptionData.addElement("Description");
                    vieDescription.addText(descriptionStr);

                    // BulletPoint-重点，有可能需要多个，但最多5个
                    if (introductionEntity.getKeyPoints() != null) {
                        String[] kepPoints = introductionEntity.getKeyPoints().split("\n");
                        if (kepPoints.length <= 5) {
                            for (int z = 0; z < kepPoints.length; z++) {
                                String tmep;
                                Element vieDulletPoint = vieDescriptionData.addElement("BulletPoint");
                                if (kepPoints[z].length() > 500) {
                                    tmep = kepPoints[z].trim().substring(0, 500);
                                } else {
                                    tmep = kepPoints[z];
                                }
                                vieDulletPoint.addText(tmep);
                            }
                        } else {
                            for (int x = 0; x < 5; x++) {
                                String tmep;
                                Element vieDulletPoint = vieDescriptionData.addElement("BulletPoint");
                                if (kepPoints[x].length() > 500) {
                                    tmep = kepPoints[x].trim().substring(0, 500);
                                } else {
                                    tmep = kepPoints[x];
                                }
                                vieDulletPoint.addText(tmep);
                            }

                        }
                    } else {
                        /*for (int x = 0; x < 4; x++) {
                            Element vieDulletPoint = vieDescriptionData.addElement("BulletPoint");
                            vieDulletPoint.addText("\\t");
                        }*/
                        Element vieDulletPoint = vieDescriptionData.addElement("BulletPoint");
                        vieDulletPoint.addText("\\t");
                    }

                    // Manufacturer - 生产厂家
                    Element vieManufacturer = vieDescriptionData.addElement("Manufacturer");
                    vieManufacturer.addText(manufacturerStr);

                    // SearchTerms - 关键字
                    Element vieSearchTerms = vieDescriptionData.addElement("SearchTerms");
                    vieSearchTerms.addText(searchTermsStr);

                    // ItemType - 推荐节点
                    Element vieItemType = vieDescriptionData.addElement("ItemType");
                    vieItemType.addText(itemTypeStr);

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
                    Element vieBeauty = vieProductData.addElement("Beauty");
                    Element vieProductType = vieBeauty.addElement("ProductType");
                    Element vieMeautyMisc = vieProductType.addElement("BeautyMisc");
                    Element vieVariationData = vieMeautyMisc.addElement("VariationData");

                    Element vieParentage = vieVariationData.addElement("Parentage");
                    vieParentage.addText("child");
                    Element vieVariationTheme = vieVariationData.addElement("VariationTheme");
                    vieVariationTheme.addText(variationThemeStr);
                    switch (variationThemeStr) {
                        case "Size-Color":
                            Element vieSize = vieVariationData.addElement("Size");
                            String[] str = variantsInfoEntity.getVariantCombination().split(" - ");
                            vieSize.addText(str[1]);
                            Element vieColor = vieVariationData.addElement("Color");
                            vieColor.addText(str[0]);
                            /*Element colorMap1 = vieVariationData.addElement("ColorMap");
                            colorMap1.addText("white");*/
                            /*Element sizeMap1 = vieVariationData.addElement("SizeMap");
                            sizeMap1.addText("free");*/
                            break;
                        case "Color":
                            Element vieColor1 = vieVariationData.addElement("Color");
                            vieColor1.addText(variantsInfoEntity.getVariantCombination());
                            /*Element colorMap2 = vieVariationData.addElement("ColorMap");
                            colorMap2.addText("white");*/
                            break;
                        case "Size":
                            Element vieSize2 = vieVariationData.addElement("Size");
                            vieSize2.addText(variantsInfoEntity.getVariantCombination());
                            /*Element sizeMap2 = vieVariationData.addElement("SizeMap");
                            sizeMap2.addText("free");*/
                            break;
                        default:
                            break;
                    }

                    /*Element vieUnitCount = vieMeautyMisc.addElement("UnitCount");
                    vieUnitCount.addAttribute("unitOfMeasure", "oz");
                    vieUnitCount.addText("0.1");
                    Element vieDirections = vieMeautyMisc.addElement("Directions");
                    vieDirections.addText("\\t");*/
                }
            } else {
                /*Element unitCount = meautyMisc.addElement("UnitCount");
                unitCount.addAttribute("unitOfMeasure", "oz");
                unitCount.addText("0.1");
                Element directions = meautyMisc.addElement("Directions");
                directions.addText("\\t");*/
            }

        }
        // 生成文件路径
        String path = fileStoragePath;
        String filePath = FileUtil.generateFilePath(path, "Product", uploadId);

        try {
            XMLUtil.writeXMLToFile(document, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!FileUtil.judeFileExists(filePath)) {
            return null;
        }
        return filePath;
    }

    /**
     * 生成产品图片信息xml
     *
     * @param productsList           产品列表
     * @param merchantIdentifierText 卖家记号
     * @return XML路径
     * @author zjr
     */
    public String generateImagesXML(List<ProductsEntity> productsList, String merchantIdentifierText, Long uploadId) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("AmazonEnvelope");
        root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance").addAttribute("xsi:noNamespaceSchemaLocation", "https://images-na.ssl-images-amazon.com/images/G/01/rainier/help/xsd/release_1_9/amzn-envelope.xsd");
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
            List<ImageAddressEntity> imageAddressEntityList = imageAddressService.selectList(new EntityWrapper<ImageAddressEntity>().eq("product_id", productsEntity.getProductId()).eq("is_deleted", "0").orderBy("sort",true));
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
            if (variantsInfoEntityList.size() != 0) {
                for (int j = 0; j < variantsInfoEntityList.size(); j++) {
                    VariantsInfoEntity variantsInfoEntity = variantsInfoEntityList.get(j);
                    List<String> viImageUrls = new ArrayList<>();
                    if (variantsInfoEntity.getImageUrl() != null) {
                        viImageUrls = Arrays.asList(variantsInfoEntity.getImageUrl().split(","));
                    } else {
                        for (int k = 0; k < imageAddressEntityList.size(); k++) {
                            viImageUrls.add(imageAddressEntityList.get(k).getImageUrl());
                        }
                    }

                    if (viImageUrls.size() != 0) {
                        int temp = viImageUrls.size();
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
                            imageLocation.addText(viImageUrls.get(k));
                            messageId++;
                        }
                    }
                }
            }
        }

        // 生成文件路径
        String path = fileStoragePath;
        String filePath = FileUtil.generateFilePath(path, "Images", uploadId);

        try {
            XMLUtil.writeXMLToFile(document, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!FileUtil.judeFileExists(filePath)) {
            return null;
        }
        return filePath;
    }

    /**
     * 生成库存信息xml
     *
     * @param productsList           产品列表
     * @param merchantIdentifierText 卖家记号
     * @return xml路径
     * @author zjr
     */
    public String generateInventoryXML(List<ProductsEntity> productsList, String merchantIdentifierText, Long uploadId) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("AmazonEnvelope");
        root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance").addAttribute("xsi:noNamespaceSchemaLocation", "https://images-na.ssl-images-amazon.com/images/G/01/rainier/help/xsd/release_1_9/amzn-envelope.xsd");
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
            if (variantsInfoEntityList.size() != 0) {
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
        String filePath = FileUtil.generateFilePath(path, "Inventory", uploadId);

        try {
            XMLUtil.writeXMLToFile(document, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!FileUtil.judeFileExists(filePath)) {
            return null;
        }
        return filePath;
    }

    /**
     * 生成价格信息xml
     *
     * @param countryCode            国家代码
     * @param productsList           产品列表
     * @param merchantIdentifierText 卖家记号
     * @return xml路径
     * @author zjr
     */
    public String generatePricesXML(String countryCode, List<ProductsEntity> productsList, String merchantIdentifierText, Long uploadId) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("AmazonEnvelope");
        root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance").addAttribute("xsi:noNamespaceSchemaLocation", "https://images-na.ssl-images-amazon.com/images/G/01/rainier/help/xsd/release_1_9/amzn-envelope.xsd");
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
            if (variantsInfoEntityList.size() != 0) {
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
                    BigDecimal addPrice = variantsInfoEntity.getVariantAddPrice();
                    if(addPrice != null && addPrice.compareTo(new BigDecimal(0)) != 0){
                        AmazonRateEntity rateEntity = amazonRateService.selectOne(new EntityWrapper<AmazonRateEntity>().eq("rate_code",money));
                        BigDecimal relAddPrice = addPrice.divide(rateEntity.getRate(),2,BigDecimal.ROUND_HALF_UP);
                        BigDecimal variantPrice = final_price.add(relAddPrice);
                        standardPrice1.addText(variantPrice.toString());
                    }else {
                        standardPrice1.addText(final_price.toString());
                    }
                    messageId++;
                }

            }
        }

        // 生成文件路径
        String path = fileStoragePath;
        String filePath = FileUtil.generateFilePath(path, "Prices", uploadId);

        try {
            XMLUtil.writeXMLToFile(document, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!FileUtil.judeFileExists(filePath)) {
            return null;
        }
        return filePath;
    }

    /**
     * 生成关系信息xml
     *
     * @param productsList           产品列表
     * @param merchantIdentifierText 卖家记号
     * @return xml路径
     * @author zjr
     */
    public String generateRelationshipsXML(List<ProductsEntity> productsList, String merchantIdentifierText, Long uploadId) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("AmazonEnvelope");
        root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance").addAttribute("xsi:noNamespaceSchemaLocation", "https://images-na.ssl-images-amazon.com/images/G/01/rainier/help/xsd/release_1_9/amzn-envelope.xsd");
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
            if (variantsInfoEntityList.size() != 0) {
                // 有变体
                Element message = root.addElement("Message");
                Element messageID = message.addElement("MessageID");
                messageID.addText(messageId + "");
                messageId++;
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
            } else {
                // 没有变体
                continue;
            }
        }

        // 生成文件路径
        String path = fileStoragePath;
        String filePath = FileUtil.generateFilePath(path, "Relationships", uploadId);

        try {
            XMLUtil.writeXMLToFile(document, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!FileUtil.judeFileExists(filePath)) {
            return null;
        }
        return filePath;
    }

    /**
     * 生成衣服模板
     *
     * @param uploadId
     * @param merchantIdentifierText
     * @param productsList
     * @param countryCode
     * @return
     */
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
            if (productsEntity.getProductSku() != null) {
                Element sku = product.addElement("SKU");
                sku.addText(productsEntity.getProductSku());
            }

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
            String titleStr = "\\t";
            Element title = descriptionData.addElement("Title");
            if (introductionEntity.getProductTitle() != null) {
                if (introductionEntity.getProductTitle().length() > 200) {
                    titleStr = introductionEntity.getProductTitle().trim().substring(0, 200);
                } else {
                    titleStr = introductionEntity.getProductTitle();
                }
            }
            title.addText(titleStr);

            // Brand-品牌
            String brandStr = "\\t";
            Element brand = descriptionData.addElement("Brand");
            if (productsEntity.getBrandName() != null) {
                brandStr = productsEntity.getBrandName();
            }
            brand.addText(brandStr);

            // Description-描述
            String descriptionStr = "\\t";
            Element description = descriptionData.addElement("Description");
            if (introductionEntity.getProductDescription() != null) {
                if (introductionEntity.getProductDescription().length() > 2000) {
                    descriptionStr = introductionEntity.getProductDescription().trim().substring(0, 2000).replace("\n", "<br />");
                } else {
                    descriptionStr = introductionEntity.getProductDescription().replace("\n", "<br />");
                }
            }
            description.addText(descriptionStr);

            // BulletPoint-重点，有可能需要多个，但最多5个
            if (introductionEntity.getKeyPoints() != null) {
                String[] kepPoints = introductionEntity.getKeyPoints().split("\n");
                if (kepPoints.length <= 5) {
                    for (int j = 0; j < kepPoints.length; j++) {
                        String tempKepPoints;
                        Element bulletPoint = descriptionData.addElement("BulletPoint");
                        if (kepPoints[j].length() > 500) {
                            tempKepPoints = kepPoints[j].trim().substring(0, 500);
                        } else {
                            tempKepPoints = kepPoints[j].trim();
                        }
                        bulletPoint.addText(tempKepPoints);
                    }
                } else {
                    for (int j = 0; j < 5; j++) {
                        String tempKepPoints;
                        Element bulletPoint = descriptionData.addElement("BulletPoint");
                        if (kepPoints[j].length() > 500) {
                            tempKepPoints = kepPoints[j].trim().substring(0, 500);
                        } else {
                            tempKepPoints = kepPoints[j].trim();
                        }
                        bulletPoint.addText(tempKepPoints);
                    }

                }
            } else {
                Element bulletPoint = descriptionData.addElement("BulletPoint");
                bulletPoint.addText("\\t");
               /* for (int j = 0; j < 4; j++) {
                    Element bulletPoint = descriptionData.addElement("BulletPoint");
                    bulletPoint.addText("\\t");
                }*/
            }

            // Manufacturer - 生产厂家
            String manufacturerStr = "\\t";
            Element manufacturer = descriptionData.addElement("Manufacturer");
            if (productsEntity.getProducerName() != null) {
                manufacturerStr = productsEntity.getProducerName();
            }
            manufacturer.addText(manufacturerStr);

            // SearchTerms - 关键字
            Element searchTerms = descriptionData.addElement("SearchTerms");
            String searchTermsStr = "\\t";
            if (introductionEntity.getKeyWord() != null) {
                if (introductionEntity.getKeyWord().trim().length() > 250) {
                    searchTermsStr = introductionEntity.getKeyWord().trim().substring(0, 250);
                } else {
                    searchTermsStr = introductionEntity.getKeyWord().trim();
                }
            }
            searchTerms.addText(searchTermsStr);

            // ItemType - 推荐节点
            AmazonCategoryEntity amazonCategoryEntity = amazonCategoryService.selectOne(new EntityWrapper<AmazonCategoryEntity>().eq("id", uploadEntity.getAmazonCategoryId()));
            String itemTypeStr = "\\t";
            Element itemType = descriptionData.addElement("ItemType");
            if (amazonCategoryEntity.getCategoryName() != null) {
                itemTypeStr = amazonCategoryEntity.getCategoryName();
            }
            itemType.addText(itemTypeStr);

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
                if (productsEntity.getColorId() != null && productsEntity.getSizeId() != null) {
                    variationThemeStr = "SizeColor";
                    variationTheme.addText(variationThemeStr);
                    /*Element colorMap = variationData.addElement("ColorMap");
                    colorMap.addText("white");
                    Element sizeMap = variationData.addElement("SizeMap");
                    sizeMap.addText("free");*/
                } else if (productsEntity.getColorId() != null) {
                    variationThemeStr = "Color";
                    variationTheme.addText(variationThemeStr);
                    /*Element colorMap = variationData.addElement("ColorMap");
                    colorMap.addText("white");*/
                } else if (productsEntity.getSizeId() != null) {
                    variationThemeStr = "Size";
                    variationTheme.addText(variationThemeStr);
                    /*Element sizeMap = variationData.addElement("SizeMap");
                    sizeMap.addText("free");*/
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
                // Element warnings1 = vieClassificationData1.addElement("Warnings");
                // warnings1.addText("123123132132");
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
                /*Element sizeMap1 = vieClassificationData1.addElement("SizeMap");
                sizeMap1.addText("null");*/
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
                // Element safetyWarning1 = vieClassificationData1.addElement("SafetyWarning");
                // safetyWarning1.addText("1231231");
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
                    vieTitle.addText(titleStr);

                    // Brand-品牌
                    Element vieBrand = vieDescriptionData.addElement("Brand");
                    vieBrand.addText(brandStr);

                    // Description-描述
                    Element vieDescription = vieDescriptionData.addElement("Description");
                    vieDescription.addText(descriptionStr);

                    // BulletPoint-重点，有可能需要多个，但最多5个
                    if (introductionEntity.getKeyPoints() != null) {
                        String[] kepPoints = introductionEntity.getKeyPoints().split("\n");
                        if (kepPoints.length <= 5) {
                            for (int z = 0; z < kepPoints.length; z++) {
                                String tmep;
                                Element vieDulletPoint = vieDescriptionData.addElement("BulletPoint");
                                if (kepPoints[z].length() > 500) {
                                    tmep = kepPoints[z].trim().substring(0, 500);
                                } else {
                                    tmep = kepPoints[z];
                                }
                                vieDulletPoint.addText(tmep);
                            }
                        } else {
                            for (int x = 0; x < 5; x++) {
                                String tmep;
                                Element vieDulletPoint = vieDescriptionData.addElement("BulletPoint");
                                if (kepPoints[x].length() > 500) {
                                    tmep = kepPoints[x].trim().substring(0, 500);
                                } else {
                                    tmep = kepPoints[x];
                                }
                                vieDulletPoint.addText(tmep);
                            }

                        }
                    } else {
                        /*for (int x = 0; x < 4; x++) {
                            Element vieDulletPoint = vieDescriptionData.addElement("BulletPoint");
                            vieDulletPoint.addText("\\t");
                        }*/
                        Element vieDulletPoint = vieDescriptionData.addElement("BulletPoint");
                        vieDulletPoint.addText("\\t");
                    }

                    // Manufacturer - 生产厂家
                    Element vieManufacturer = vieDescriptionData.addElement("Manufacturer");
                    vieManufacturer.addText(manufacturerStr);

                    // SearchTerms - 关键字
                    Element vieSearchTerms = vieDescriptionData.addElement("SearchTerms");
                    vieSearchTerms.addText(searchTermsStr);

                    // ItemType - 推荐节点
                    Element vieItemType = vieDescriptionData.addElement("ItemType");
                    vieItemType.addText(itemTypeStr);

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
                            String[] str = variantsInfoEntity.getVariantCombination().split(" - ");
                            vieSize.addText(str[1]);
                            Element vieColor = vieVariationData.addElement("Color");
                            vieColor.addText(str[0]);
                            /*Element colorMap1 = vieVariationData.addElement("ColorMap");
                            colorMap1.addText("white");
                            Element sizeMap1 = vieVariationData.addElement("SIzeMap");
                            sizeMap1.addText("free");*/
                            break;
                        case "Color":
                            Element vieColor1 = vieVariationData.addElement("Color");
                            vieColor1.addText(variantsInfoEntity.getVariantCombination());
                            /*Element colorMap2 = vieVariationData.addElement("ColorMap");
                            colorMap2.addText("white");*/
                            break;
                        case "Size":
                            Element vieSize2 = vieVariationData.addElement("Size");
                            vieSize2.addText(variantsInfoEntity.getVariantCombination());
                            /*Element sizeMap2 = vieVariationData.addElement("SIzeMap");
                            sizeMap2.addText("free");*/
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
                    // Element warnings = vieClassificationData.addElement("Warnings");
                    //warnings.addText("123123132132");
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
                    /*Element sizeMap = vieClassificationData.addElement("SizeMap");
                    sizeMap.addText("null");*/
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
                    // Element safetyWarning = vieClassificationData.addElement("SafetyWarning");
                    // safetyWarning.addText("123123");
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
                // Element warnings = classificationData.addElement("Warnings");
                //  warnings.addText("123123132132");
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
                /*Element sizeMap = classificationData.addElement("SizeMap");
                sizeMap.addText("null");*/
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
                // Element safetyWarning = classificationData.addElement("SafetyWarning");
                // safetyWarning.addText("123123");
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
        String filePath = FileUtil.generateFilePath(path, "ProductByClothing",uploadId);

        try {
            XMLUtil.writeXMLToFile(document, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!FileUtil.judeFileExists(filePath)) {
            return null;
        }
        return filePath;
    }

    /**
     * 生成家居装修模板
     *
     * @param uploadId
     * @param merchantIdentifierText
     * @param productsList
     * @param countryCode
     * @return
     */
    public String generateProductXMLByHomeImprovement(Long uploadId, String merchantIdentifierText, List<ProductsEntity> productsList, String countryCode) {

        /*
            家居装修使用的是子分类建筑材料的模板，如果要增加家居装修的子分类，需要数据库表和判断模板的方法。
         */

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
            if (productsEntity.getProductSku() != null) {
                Element sku = product.addElement("SKU");
                sku.addText(productsEntity.getProductSku());
            }

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
            String titleStr = "\\t";
            Element title = descriptionData.addElement("Title");
            if (introductionEntity.getProductTitle() != null) {
                if (introductionEntity.getProductTitle().length() > 200) {
                    titleStr = introductionEntity.getProductTitle().trim().substring(0, 200);
                } else {
                    titleStr = introductionEntity.getProductTitle();
                }
            }
            title.addText(titleStr);

            // Brand-品牌
            String brandStr = "\\t";
            Element brand = descriptionData.addElement("Brand");
            if (productsEntity.getBrandName() != null) {
                brandStr = productsEntity.getBrandName();
            }
            brand.addText(brandStr);

            // Description-描述
            String descriptionStr = "\\t";
            Element description = descriptionData.addElement("Description");
            if (introductionEntity.getProductDescription() != null) {
                if (introductionEntity.getProductDescription().length() > 2000) {
                    descriptionStr = introductionEntity.getProductDescription().trim().substring(0, 2000).replace("\n", "<br />");
                } else {
                    descriptionStr = introductionEntity.getProductDescription().replace("\n", "<br />");
                }
            }
            description.addText(descriptionStr);

            // BulletPoint-重点，有可能需要多个，但最多5个
            if (introductionEntity.getKeyPoints() != null) {
                String[] kepPoints = introductionEntity.getKeyPoints().split("\n");
                if (kepPoints.length <= 5) {
                    for (int j = 0; j < kepPoints.length; j++) {
                        String tempKepPoints;
                        Element bulletPoint = descriptionData.addElement("BulletPoint");
                        if (kepPoints[j].length() > 500) {
                            tempKepPoints = kepPoints[j].trim().substring(0, 500);
                        } else {
                            tempKepPoints = kepPoints[j].trim();
                        }
                        bulletPoint.addText(tempKepPoints);
                    }
                } else {
                    for (int j = 0; j < 5; j++) {
                        String tempKepPoints;
                        Element bulletPoint = descriptionData.addElement("BulletPoint");
                        if (kepPoints[j].length() > 500) {
                            tempKepPoints = kepPoints[j].trim().substring(0, 500);
                        } else {
                            tempKepPoints = kepPoints[j].trim();
                        }
                        bulletPoint.addText(tempKepPoints);
                    }

                }
            } else {
                Element bulletPoint = descriptionData.addElement("BulletPoint");
                bulletPoint.addText("\\t");
               /* for (int j = 0; j < 4; j++) {
                    Element bulletPoint = descriptionData.addElement("BulletPoint");
                    bulletPoint.addText("\\t");
                }*/
            }

            // Manufacturer - 生产厂家
            String manufacturerStr = "\\t";
            Element manufacturer = descriptionData.addElement("Manufacturer");
            if (productsEntity.getProducerName() != null) {
                manufacturerStr = productsEntity.getProducerName();
            }
            manufacturer.addText(manufacturerStr);

            // SearchTerms - 关键字
            Element searchTerms = descriptionData.addElement("SearchTerms");
            String searchTermsStr = "\\t";
            if (introductionEntity.getKeyWord() != null) {
                if (introductionEntity.getKeyWord().trim().length() > 250) {
                    searchTermsStr = introductionEntity.getKeyWord().trim().substring(0, 250);
                } else {
                    searchTermsStr = introductionEntity.getKeyWord().trim();
                }
            }
            searchTerms.addText(searchTermsStr);

            // ItemType - 推荐节点
            AmazonCategoryEntity amazonCategoryEntity = amazonCategoryService.selectOne(new EntityWrapper<AmazonCategoryEntity>().eq("id", uploadEntity.getAmazonCategoryId()));
            String itemTypeStr = "\\t";
            Element itemType = descriptionData.addElement("ItemType");
            if (amazonCategoryEntity.getCategoryName() != null) {
                itemTypeStr = amazonCategoryEntity.getCategoryName();
            }
            itemType.addText(itemTypeStr);

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
            }

            Element productData = product.addElement("ProductData");
            Element homeImprovement = productData.addElement("HomeImprovement");


            String variationThemeStr = "";
            if (isVariant) {
                // 有变体
                Element productType = homeImprovement.addElement("ProductType");
                Element buildingMaterials = productType.addElement("BuildingMaterials");
                Element variationData = buildingMaterials.addElement("VariationData");
                Element parentage = variationData.addElement("Parentage");
                parentage.addText("parent");
                Element variationTheme = variationData.addElement("VariationTheme");
//                if (productsEntity.getColorId() != null && productsEntity.getSizeId() != null) {
                Long colorId = productsEntity.getColorId();
                Long sizeId = productsEntity.getSizeId();
                VariantParameterEntity colorVP = null;
                VariantParameterEntity sizeVP = null;
                if(colorId != null){
                    colorVP = variantParameterService.selectById(colorId);
                }
                if(sizeId != null){
                    sizeVP = variantParameterService.selectById(sizeId);
                }
                if (colorVP != null && sizeVP != null) {
                    variationThemeStr = "Size-Color";
                    variationTheme.addText(variationThemeStr);
                    /*Element colorMap = variationTheme.addElement("ColorMap");
                    colorMap.addText("white");
                    Element sizeMap = variationTheme.addElement("SizeMap");
                    sizeMap.addText("free");*/
                } else if (colorVP != null) {
                    variationThemeStr = "Color";
                    variationTheme.addText(variationThemeStr);
                    /*Element colorMap = variationTheme.addElement("ColorMap");
                    colorMap.addText("white");*/
                } else if (sizeVP != null) {
                    variationThemeStr = "Size";
                    variationTheme.addText(variationThemeStr);
                    /*EElement sizeMap = variationTheme.addElement("SizeMap");
                    sizeMap.addText("free");*/
                }

                // 后续部分
                Element accessoryConnectionType = buildingMaterials.addElement("AccessoryConnectionType");
                accessoryConnectionType.addText("null");
                /*Element bladeEdge = buildingMaterials.addElement("BladeEdge");
                bladeEdge.addText("\\t");
                Element bulbType = buildingMaterials.addElement("BulbType");
                bulbType.addText("\\t");
                Element compatibleDevices = buildingMaterials.addElement("CompatibleDevices");
                compatibleDevices.addText("\\t");
                Element compatibleFastenerRange = buildingMaterials.addElement("CompatibleFastenerRange");
                compatibleFastenerRange.addText("\\t");
                Element coolingMethod = buildingMaterials.addElement("CoolingMethod");
                coolingMethod.addText("\\t");
                Element environmentalDescription = buildingMaterials.addElement("EnvironmentalDescription");
                environmentalDescription.addText("\\t");
                Element externalTestingCertification = buildingMaterials.addElement("ExternalTestingCertification");
                externalTestingCertification.addText("\\t");
                Element coverage = buildingMaterials.addElement("Coverage");
                coverage.addText("\\t");
                Element cutType = buildingMaterials.addElement("CutType");
                cutType.addText("\\t");
                Element deviceType = buildingMaterials.addElement("DeviceType");
                deviceType.addText("\\t");
                Element displayStyle = buildingMaterials.addElement("DisplayStyle");
                displayStyle.addText("\\t");
                Element flushType = buildingMaterials.addElement("FlushType");
                flushType.addText("\\t");
                Element foldedKnifeSize = buildingMaterials.addElement("FoldedKnifeSize");
                foldedKnifeSize.addText("\\t");
                Element handleMaterial = buildingMaterials.addElement("HandleMaterial");
                handleMaterial.addText("\\t");
                Element headStyle = buildingMaterials.addElement("HeadStyle");
                headStyle.addText("\\t");
                Element laserBeamColor = buildingMaterials.addElement("LaserBeamColor");
                laserBeamColor.addText("\\t");
                Element measurementAccuracy = buildingMaterials.addElement("MeasurementAccuracy");
                measurementAccuracy.addElement("\\t");
                Element measurementSystem = buildingMaterials.addElement("MeasurementSystem");
                measurementSystem.addText("\\t");
                Element performanceDescription = buildingMaterials.addElement("PerformanceDescription");
                performanceDescription.addText("\\t");
            */
                // PlugProfile - 插头类型
                Element plugProfile = buildingMaterials.addElement("PlugProfile");
                plugProfile.addText(String.valueOf(valueMap.get("PlugProfile")));

               /* Element switchStyle = buildingMaterials.addElement("SwitchStyle");
                switchStyle.addText("\\t");*/

                // SwitchType - 开关类型
                Element switchType = buildingMaterials.addElement("SwitchType");
                switchType.addText(String.valueOf(valueMap.get("SwitchType")));

               /* Element toolTipDescription = buildingMaterials.addElement("ToolTipDescription");
                toolTipDescription.addText("\\t");
                Element uVProtection = buildingMaterials.addElement("UVProtection");
                uVProtection.addText("\\t");
                Element viewingArea = buildingMaterials.addElement("ViewingArea");
                viewingArea.addText("\\t");*/

                /*// 尺寸
                Element size = buildingMaterials.addElement("Size");
                size.addText("\\t");
                // 颜色
                Element color = buildingMaterials.addElement("Color");
                color.addText("\\t");
                // 色系和颜色一样
                Element colorMap = buildingMaterials.addElement("ColorMap");
                colorMap.addText("\\t");*/

                // Material - 材料
                Element material = buildingMaterials.addElement("Material");
                material.addText(String.valueOf(valueMap.get("Material")));

                /*Element manufacturerWarrantyDescription = buildingMaterials.addElement("ManufacturerWarrantyDescription");
                manufacturerWarrantyDescription.addText("\\t");*/

                // PowerSource - 能量源
                Element powerSource = buildingMaterials.addElement("PowerSource");
                powerSource.addText(String.valueOf(valueMap.get("PowerSource")));

             /*   Element sellerWarrantyDescription = buildingMaterials.addElement("SellerWarrantyDescription");
                sellerWarrantyDescription.addText("\\t");
                Element customerPackageType = buildingMaterials.addElement("CustomerPackageType");
                customerPackageType.addText("\\t");
                Element bladeColor = buildingMaterials.addElement("BladeColor");
                bladeColor.addText("\\t");
                Element circuitBreakerType = buildingMaterials.addElement("CircuitBreakerType");
                circuitBreakerType.addText("\\t");
                Element efficiency = buildingMaterials.addElement("Efficiency");
                efficiency.addText("\\t");
                Element internationalProtectionRating = buildingMaterials.addElement("InternationalProtectionRating");
                internationalProtectionRating.addText("\\t");
                Element lightingMethod = buildingMaterials.addElement("LightingMethod");
                lightingMethod.addText("\\t");
                Element shadeMaterialType = buildingMaterials.addElement("ShadeMaterialType");
                shadeMaterialType.addText("\\t");
                Element shortProductDescription = buildingMaterials.addElement("ShortProductDescription");
                shortProductDescription.addText("\\t");
                Element startUpTimeDescription = buildingMaterials.addElement("StartUpTimeDescription");
                startUpTimeDescription.addText("\\t");
                Element strands = buildingMaterials.addElement("Strands");
                strands.addText("\\t");
                Element customerRestrictionType = buildingMaterials.addElement("CustomerRestrictionType");
                customerRestrictionType.addText("\\t");
                Element legalComplianceCertificationMetadata = buildingMaterials.addElement("LegalComplianceCertificationMetadata");
                legalComplianceCertificationMetadata.addText("\\t");
                Element legalComplianceCertificationDateOfIssue = buildingMaterials.addElement("LegalComplianceCertificationDateOfIssue");
                legalComplianceCertificationDateOfIssue.addText("\\t");
                Element legalComplianceCertificationExpirationDate = buildingMaterials.addElement("LegalComplianceCertificationExpirationDate");
                legalComplianceCertificationExpirationDate.addText("\\t");

                Element mfrWarrantyDescriptionLabor = homeImprovement.addElement("MfrWarrantyDescriptionLabor");
                mfrWarrantyDescriptionLabor.addText("\\t");
                Element mfrWarrantyDescriptionParts = homeImprovement.addElement("MfrWarrantyDescriptionParts");
                mfrWarrantyDescriptionParts.addText("\\t");
                Element mfrWarrantyDescriptionType = homeImprovement.addElement("MfrWarrantyDescriptionType");
                mfrWarrantyDescriptionType.addText("\\t");
                Element fabricType = homeImprovement.addElement("FabricType");
                fabricType.addText("\\t");
*/
                for (int j = 0; j < variantsInfoEntityList.size(); j++) {
                    System.out.println("varid=" + variantsInfoEntityList.get(j).getVariantId());
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
                    vieTitle.addText(titleStr);

                    // Brand-品牌
                    Element vieBrand = vieDescriptionData.addElement("Brand");
                    vieBrand.addText(brandStr);

                    // Description-描述
                    Element vieDescription = vieDescriptionData.addElement("Description");
                    vieDescription.addText(descriptionStr);

                    // BulletPoint-重点，有可能需要多个，但最多5个
                    if (introductionEntity.getKeyPoints() != null) {
                        String[] kepPoints = introductionEntity.getKeyPoints().split("\n");
                        if (kepPoints.length <= 5) {
                            for (int z = 0; z < kepPoints.length; z++) {
                                String tmep;
                                Element vieDulletPoint = vieDescriptionData.addElement("BulletPoint");
                                if (kepPoints[z].length() > 500) {
                                    tmep = kepPoints[z].trim().substring(0, 500);
                                } else {
                                    tmep = kepPoints[z];
                                }
                                vieDulletPoint.addText(tmep);
                            }
                        } else {
                            for (int x = 0; x < 5; x++) {
                                String tmep;
                                Element vieDulletPoint = vieDescriptionData.addElement("BulletPoint");
                                if (kepPoints[x].length() > 500) {
                                    tmep = kepPoints[x].trim().substring(0, 500);
                                } else {
                                    tmep = kepPoints[x];
                                }
                                vieDulletPoint.addText(tmep);
                            }

                        }
                    } else {
                        /*for (int x = 0; x < 4; x++) {
                            Element vieDulletPoint = vieDescriptionData.addElement("BulletPoint");
                            vieDulletPoint.addText("\\t");
                        }*/
                        Element vieDulletPoint = vieDescriptionData.addElement("BulletPoint");
                        vieDulletPoint.addText("\\t");
                    }

                    // Manufacturer - 生产厂家
                    Element vieManufacturer = vieDescriptionData.addElement("Manufacturer");
                    vieManufacturer.addText(manufacturerStr);

                    // SearchTerms - 关键字
                    Element vieSearchTerms = vieDescriptionData.addElement("SearchTerms");
                    vieSearchTerms.addText(searchTermsStr);

                    // ItemType - 推荐节点
                    Element vieItemType = vieDescriptionData.addElement("ItemType");
                    vieItemType.addText(itemTypeStr);

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
                    Element vieHomeImprovement = vieProductData.addElement("HomeImprovement");

                    Element vieProductType = vieHomeImprovement.addElement("ProductType");
                    Element vieBuildingMaterials = vieProductType.addElement("BuildingMaterials");
                    Element vieVariationData = vieBuildingMaterials.addElement("VariationData");
                    Element vieParentage = vieVariationData.addElement("Parentage");
                    vieParentage.addText("child");
                    Element vieVariationTheme = vieVariationData.addElement("VariationTheme");
                    vieVariationTheme.addText(variationThemeStr);


                    /*Element vieAccessoryConnectionType = vieBuildingMaterials.addElement("AccessoryConnectionType");
                    vieAccessoryConnectionType.addText("\\t");
                    Element vieBladeEdge = vieBuildingMaterials.addElement("BladeEdge");
                    vieBladeEdge.addText("\\t");
                    Element vieBulbType = vieBuildingMaterials.addElement("BulbType");
                    vieBulbType.addText("\\t");
                    Element vieCompatibleDevices = vieBuildingMaterials.addElement("CompatibleDevices");
                    vieCompatibleDevices.addText("\\t");
                    Element vieCompatibleFastenerRange = vieBuildingMaterials.addElement("CompatibleFastenerRange");
                    vieCompatibleFastenerRange.addText("\\t");
                    Element vieCoolingMethod = vieBuildingMaterials.addElement("CoolingMethod");
                    vieCoolingMethod.addText("\\t");
                    Element vieEnvironmentalDescription = vieBuildingMaterials.addElement("EnvironmentalDescription");
                    vieEnvironmentalDescription.addText("\\t");
                    Element vieExternalTestingCertification = vieBuildingMaterials.addElement("ExternalTestingCertification");
                    vieExternalTestingCertification.addText("\\t");
                    Element vieCoverage = vieBuildingMaterials.addElement("Coverage");
                    vieCoverage.addText("\\t");
                    Element vieCutType = vieBuildingMaterials.addElement("CutType");
                    vieCutType.addText("\\t");
                    Element vieDeviceType = vieBuildingMaterials.addElement("DeviceType");
                    vieDeviceType.addText("\\t");
                    Element vieDisplayStyle = vieBuildingMaterials.addElement("DisplayStyle");
                    vieDisplayStyle.addText("\\t");
                    Element vieFlushType = vieBuildingMaterials.addElement("FlushType");
                    vieFlushType.addText("\\t");
                    Element vieFoldedKnifeSize = vieBuildingMaterials.addElement("FoldedKnifeSize");
                    vieFoldedKnifeSize.addText("\\t");
                    Element vieHandleMaterial = vieBuildingMaterials.addElement("HandleMaterial");
                    vieHandleMaterial.addText("\\t");
                    Element vieHeadStyle = vieBuildingMaterials.addElement("HeadStyle");
                    vieHeadStyle.addText("\\t");
                    Element vieLaserBeamColor = vieBuildingMaterials.addElement("LaserBeamColor");
                    vieLaserBeamColor.addText("\\t");
                    Element vieMeasurementAccuracy = vieBuildingMaterials.addElement("MeasurementAccuracy");
                    vieMeasurementAccuracy.addElement("\\t");
                    Element vieMeasurementSystem = vieBuildingMaterials.addElement("MeasurementSystem");
                    vieMeasurementSystem.addText("\\t");
                    Element viePerformanceDescription = vieBuildingMaterials.addElement("PerformanceDescription");
                    viePerformanceDescription.addText("\\t");*/

                    // PlugProfile - 插头类型
                    Element viePlugProfile = vieBuildingMaterials.addElement("PlugProfile");
                    viePlugProfile.addText(String.valueOf(valueMap.get("PlugProfile")));

                    /*Element vieSwitchStyle = vieBuildingMaterials.addElement("SwitchStyle");
                    vieSwitchStyle.addText("\\t");*/

                    // SwitchType - 开关类型
                    Element vieSwitchType = vieBuildingMaterials.addElement("SwitchType");
                    vieSwitchType.addText(String.valueOf(valueMap.get("SwitchType")));

                    /*Element vieToolTipDescription = vieBuildingMaterials.addElement("ToolTipDescription");
                    vieToolTipDescription.addText("\\t");
                    Element vieUVProtection = vieBuildingMaterials.addElement("UVProtection");
                    vieUVProtection.addText("\\t");
                    Element vieViewingArea = vieBuildingMaterials.addElement("ViewingArea");
                    vieViewingArea.addText("\\t");*/

                    switch (variationThemeStr) {
                        case "Size-Color":
                            String[] str = variantsInfoEntity.getVariantCombination().split(" - ");

                            // 尺寸
                            Element vieSize = vieBuildingMaterials.addElement("Size");
                            vieSize.addText(str[1]);

                            // 颜色
                            Element vieColor = vieBuildingMaterials.addElement("Color");
                            vieColor.addText(str[0]);

                            // 色系和颜色一样
                            /*Element vieColorMap = vieBuildingMaterials.addElement("ColorMap");
                            vieColorMap.addText("\\t");*/

                            Element vieColorMap = vieBuildingMaterials.addElement("ColorMap");
                            vieColorMap.addText("white");
                            /*Element vieSizeMap = vieBuildingMaterials.addElement("SizeMap");
                            vieSizeMap.addText("free");*/
                            break;
                        case "Color":
                            // 尺寸
                           /* Element vieSize1 = vieBuildingMaterials.addElement("Size");
                            vieSize1.addText("\\t");*/

                            // 颜色
                            Element vieColor1 = vieBuildingMaterials.addElement("Color");
                            vieColor1.addText(variantsInfoEntity.getVariantCombination());
                            Element vieColorMap1 = vieBuildingMaterials.addElement("ColorMap");
                            vieColorMap1.addText("white");
                            // 色系和颜色一样
                           /* Element vieColorMap1 = vieBuildingMaterials.addElement("ColorMap");
                            vieColorMap1.addText("\\t");*/
                            break;
                        case "Size":
                            // 尺寸
                            Element vieSize2 = vieBuildingMaterials.addElement("Size");
                            vieSize2.addText(variantsInfoEntity.getVariantCombination());

                            /*Element vieSizeMap1 = vieBuildingMaterials.addElement("SizeMap");
                            vieSizeMap1.addText("free");*/
                          /*  // 颜色
                            Element vieColor2 = vieBuildingMaterials.addElement("Color");
                            vieColor2.addText("\\t");

                            // 色系和颜色一样
                            Element vieColorMap2 = vieBuildingMaterials.addElement("ColorMap");
                            vieColorMap2.addText("\\t");*/
                            break;
                        default:
                    }
                    // Material - 材料
                    Element vieMaterial = vieBuildingMaterials.addElement("Material");
                    vieMaterial.addText(String.valueOf(valueMap.get("Material")));

                   /* Element vieManufacturerWarrantyDescription = vieBuildingMaterials.addElement("ManufacturerWarrantyDescription");
                    vieManufacturerWarrantyDescription.addText("\\t");*/

                    // PowerSource - 能量源
                    Element viePowerSource = vieBuildingMaterials.addElement("PowerSource");
                    viePowerSource.addText(String.valueOf(valueMap.get("PowerSource")));

                   /* Element vieSellerWarrantyDescription = vieBuildingMaterials.addElement("SellerWarrantyDescription");
                    vieSellerWarrantyDescription.addText("\\t");
                    Element vieCustomerPackageType = vieBuildingMaterials.addElement("CustomerPackageType");
                    vieCustomerPackageType.addText("\\t");
                    Element vieBladeColor = vieBuildingMaterials.addElement("BladeColor");
                    vieBladeColor.addText("\\t");
                    Element vieCircuitBreakerType = vieBuildingMaterials.addElement("CircuitBreakerType");
                    vieCircuitBreakerType.addText("\\t");
                    Element vieEfficiency = vieBuildingMaterials.addElement("Efficiency");
                    vieEfficiency.addText("\\t");
                    Element vieInternationalProtectionRating = vieBuildingMaterials.addElement("InternationalProtectionRating");
                    vieInternationalProtectionRating.addText("\\t");
                    Element vieLightingMethod = vieBuildingMaterials.addElement("LightingMethod");
                    vieLightingMethod.addText("\\t");
                    Element vieShadeMaterialType = vieBuildingMaterials.addElement("ShadeMaterialType");
                    vieShadeMaterialType.addText("\\t");
                    Element vieShortProductDescription = vieBuildingMaterials.addElement("ShortProductDescription");
                    vieShortProductDescription.addText("\\t");
                    Element vieStartUpTimeDescription = vieBuildingMaterials.addElement("StartUpTimeDescription");
                    vieStartUpTimeDescription.addText("\\t");
                    Element vieStrands = vieBuildingMaterials.addElement("Strands");
                    vieStrands.addText("\\t");
                    Element vieCustomerRestrictionType = vieBuildingMaterials.addElement("CustomerRestrictionType");
                    vieCustomerRestrictionType.addText("\\t");
                    Element vieLegalComplianceCertificationMetadata = vieBuildingMaterials.addElement("LegalComplianceCertificationMetadata");
                    vieLegalComplianceCertificationMetadata.addText("\\t");
                    Element vieLegalComplianceCertificationDateOfIssue = vieBuildingMaterials.addElement("LegalComplianceCertificationDateOfIssue");
                    vieLegalComplianceCertificationDateOfIssue.addText("\\t");
                    Element vieLegalComplianceCertificationExpirationDate = vieBuildingMaterials.addElement("LegalComplianceCertificationExpirationDate");
                    legalComplianceCertificationExpirationDate.addText("\\t");

                    Element vieMfrWarrantyDescriptionLabor = vieHomeImprovement.addElement("MfrWarrantyDescriptionLabor");
                    vieMfrWarrantyDescriptionLabor.addText("\\t");
                    Element vieMfrWarrantyDescriptionParts = vieHomeImprovement.addElement("MfrWarrantyDescriptionParts");
                    vieMfrWarrantyDescriptionParts.addText("\\t");
                    Element vieMfrWarrantyDescriptionType = vieHomeImprovement.addElement("MfrWarrantyDescriptionType");
                    vieMfrWarrantyDescriptionType.addText("\\t");
                    Element vieFabricType = vieHomeImprovement.addElement("FabricType");
                    vieFabricType.addText("\\t");*/
                }
            } else {
                Element productType = homeImprovement.addElement("ProductType");
                Element buildingMaterials = productType.addElement("BuildingMaterials");
                Element accessoryConnectionType = buildingMaterials.addElement("AccessoryConnectionType");
                accessoryConnectionType.addText("null");
                /*Element bladeEdge = buildingMaterials.addElement("BladeEdge");
                bladeEdge.addText("\\t");
                Element bulbType = buildingMaterials.addElement("BulbType");
                bulbType.addText("\\t");
                Element compatibleDevices = buildingMaterials.addElement("CompatibleDevices");
                compatibleDevices.addText("\\t");
                Element compatibleFastenerRange = buildingMaterials.addElement("CompatibleFastenerRange");
                compatibleFastenerRange.addText("\\t");
                Element coolingMethod = buildingMaterials.addElement("CoolingMethod");
                coolingMethod.addText("\\t");
                Element environmentalDescription = buildingMaterials.addElement("EnvironmentalDescription");
                environmentalDescription.addText("\\t");
                Element externalTestingCertification = buildingMaterials.addElement("ExternalTestingCertification");
                externalTestingCertification.addText("\\t");
                Element coverage = buildingMaterials.addElement("Coverage");
                coverage.addText("\\t");
                Element cutType = buildingMaterials.addElement("CutType");
                cutType.addText("\\t");
                Element deviceType = buildingMaterials.addElement("DeviceType");
                deviceType.addText("\\t");
                Element displayStyle = buildingMaterials.addElement("DisplayStyle");
                displayStyle.addText("\\t");
                Element flushType = buildingMaterials.addElement("FlushType");
                flushType.addText("\\t");
                Element foldedKnifeSize = buildingMaterials.addElement("FoldedKnifeSize");
                foldedKnifeSize.addText("\\t");
                Element handleMaterial = buildingMaterials.addElement("HandleMaterial");
                handleMaterial.addText("\\t");
                Element headStyle = buildingMaterials.addElement("HeadStyle");
                headStyle.addText("\\t");
                Element laserBeamColor = buildingMaterials.addElement("LaserBeamColor");
                laserBeamColor.addText("\\t");
                Element measurementAccuracy = buildingMaterials.addElement("MeasurementAccuracy");
                measurementAccuracy.addText("\\t");
                Element measurementSystem = buildingMaterials.addElement("MeasurementSystem");
                measurementSystem.addText("\\t");
                Element performanceDescription = buildingMaterials.addElement("PerformanceDescription");
                performanceDescription.addText("\\t");

                */
                // PlugProfile - 插头类型
                Element plugProfile = buildingMaterials.addElement("PlugProfile");
                plugProfile.addText(String.valueOf(valueMap.get("PlugProfile")));

                /*Element switchStyle = buildingMaterials.addElement("SwitchStyle");
                switchStyle.addText("\\t");*/

                // SwitchType - 开关类型
                Element switchType = buildingMaterials.addElement("SwitchType");
                switchType.addText(String.valueOf(valueMap.get("SwitchType")));

               /* Element toolTipDescription = buildingMaterials.addElement("ToolTipDescription");
                toolTipDescription.addText("\\t");
                Element uVProtection = buildingMaterials.addElement("UVProtection");
                uVProtection.addText("\\t");
                Element viewingArea = buildingMaterials.addElement("ViewingArea");
                viewingArea.addText("\\t");*/

              /*  // 尺寸
                Element size = buildingMaterials.addElement("Size");
                size.addText("\\t");
                // 颜色
                Element color = buildingMaterials.addElement("Color");
                color.addText("\\t");
                // 色系和颜色一样
                Element colorMap = buildingMaterials.addElement("ColorMap");
                colorMap.addText("\\t");*/
                /*Element colorMap = buildingMaterials.addElement("ColorMap");
                colorMap.addText("white");*/
                /*Element sizeMap = buildingMaterials.addElement("SizeMap");
                sizeMap.addText("free");*/
                // Material - 材料
                Element material = buildingMaterials.addElement("Material");
                material.addText(String.valueOf(valueMap.get("Material")));

                /*Element manufacturerWarrantyDescription = buildingMaterials.addElement("ManufacturerWarrantyDescription");
                manufacturerWarrantyDescription.addText("\\t");*/

                // PowerSource - 能量源
                Element powerSource = buildingMaterials.addElement("PowerSource");
                powerSource.addText(String.valueOf(valueMap.get("PowerSource")));

               /* Element sellerWarrantyDescription = buildingMaterials.addElement("SellerWarrantyDescription");
                sellerWarrantyDescription.addText("\\t");
                Element customerPackageType = buildingMaterials.addElement("CustomerPackageType");
                customerPackageType.addText("\\t");
                Element bladeColor = buildingMaterials.addElement("BladeColor");
                bladeColor.addText("\\t");
                Element circuitBreakerType = buildingMaterials.addElement("CircuitBreakerType");
                circuitBreakerType.addText("\\t");
                Element efficiency = buildingMaterials.addElement("Efficiency");
                efficiency.addText("\\t");
                Element internationalProtectionRating = buildingMaterials.addElement("InternationalProtectionRating");
                internationalProtectionRating.addText("\\t");
                Element lightingMethod = buildingMaterials.addElement("LightingMethod");
                lightingMethod.addText("\\t");
                Element shadeMaterialType = buildingMaterials.addElement("ShadeMaterialType");
                shadeMaterialType.addText("\\t");
                Element shortProductDescription = buildingMaterials.addElement("ShortProductDescription");
                shortProductDescription.addText("\\t");
                Element startUpTimeDescription = buildingMaterials.addElement("StartUpTimeDescription");
                startUpTimeDescription.addText("\\t");
                Element strands = buildingMaterials.addElement("Strands");
                strands.addText("\\t");
                Element customerRestrictionType = buildingMaterials.addElement("CustomerRestrictionType");
                customerRestrictionType.addText("\\t");
                Element legalComplianceCertificationMetadata = buildingMaterials.addElement("LegalComplianceCertificationMetadata");
                legalComplianceCertificationMetadata.addText("\\t");
                Element legalComplianceCertificationDateOfIssue = buildingMaterials.addElement("LegalComplianceCertificationDateOfIssue");
                legalComplianceCertificationDateOfIssue.addText("\\t");
                Element legalComplianceCertificationExpirationDate = buildingMaterials.addElement("LegalComplianceCertificationExpirationDate");
                legalComplianceCertificationExpirationDate.addText("\\t");*/

                /*Element mfrWarrantyDescriptionLabor = homeImprovement.addElement("MfrWarrantyDescriptionLabor");
                mfrWarrantyDescriptionLabor.addText("\\t");
                Element mfrWarrantyDescriptionParts = homeImprovement.addElement("MfrWarrantyDescriptionParts");
                mfrWarrantyDescriptionParts.addText("\\t");
                Element mfrWarrantyDescriptionType = homeImprovement.addElement("MfrWarrantyDescriptionType");
                mfrWarrantyDescriptionType.addText("\\t");
                Element fabricType = homeImprovement.addElement("FabricType");
                fabricType.addText("\\t");*/
            }

        }
        // 生成文件路径
        String path = fileStoragePath;
        String filePath = FileUtil.generateFilePath(path, "ProductByHomeImprovement", uploadId);

        try {
            XMLUtil.writeXMLToFile(document, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!FileUtil.judeFileExists(filePath)) {
            return null;
        }
        return filePath;
    }

    /**
     * 根据国家代码判断货币
     *
     * @param productsEntity
     * @param countryCode
     * @return
     */
    public Map<String, Object> switchCountry(ProductsEntity productsEntity, String countryCode) {
        Map<String, Object> map = new HashMap<>();
        String money = "";
        Long freightId = 0L;
        switch (countryCode) {
            // 加拿大
            case "CA":
                freightId = productsEntity.getBritainIntroduction();
                money = "CAD";
                break;
            // 墨西哥
            case "MX":
                freightId = productsEntity.getSpainIntroduction();
                money = "MXN";
                break;
            // 美国
            case "US":
                freightId = productsEntity.getBritainIntroduction();
                money = "USD";
                break;
            // 德国
            case "DE":
                freightId = productsEntity.getGermanyIntroduction();
                money = "EUR";
                break;
            // 西班牙
            case "ES":
                freightId = productsEntity.getSpainIntroduction();
                money = "EUR";
                break;
            // 法国
            case "FR":
                freightId = productsEntity.getFranceIntroduction();
                money = "EUR";
                break;
            // 英国
            case "GB":
                freightId = productsEntity.getBritainIntroduction();
                money = "GBP";
                break;
            // 意大利
            case "IT":
                freightId = productsEntity.getItalyIntroduction();
                money = "EUR";
                break;
            // 澳大利亚
            case "AU":
                freightId = productsEntity.getBritainIntroduction();
                money = "AUD";
                break;
            // 日本
            case "JP":
                freightId = productsEntity.getJapanIntroduction();
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
