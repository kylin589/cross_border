package io.renren.modules.amazon.service;

import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.entity.UploadEntity;

import java.util.List;
import java.util.Map;

public interface SubmitFeedService {

   /**
    * 生成产品基础信息xml
    * @param merchantIdentifierText 卖家记号
    * @param productsList 产品列表
    * @param countryCode 国家代码
    * @param categoryNodeId 分类Id
    * @return XML路径
    */
   String generateProductXML(String merchantIdentifierText, List<ProductsEntity> productsList, String countryCode, String categoryNodeId);

   /**
    * 生成产品图片信息xml
    * @param productsList 产品列表
    * @param merchantIdentifierText 卖家记号
    * @return XML路径
    */
   String generateImagesXML(List<ProductsEntity> productsList, String merchantIdentifierText);

   /**
    * 生成库存信息xml
    * @param productsList 产品列表
    * @param merchantIdentifierText 卖家记号
    * @return xml路径
    */
   String generateInventoryXML(List<ProductsEntity> productsList, String merchantIdentifierText);

   /**
    * 生成价格信息xml
    * @param countryCode 国家代码
    * @param productsList 产品列表
    * @param merchantIdentifierText 卖家记号
    * @return xml路径
    */
   String generatePricesXML(String countryCode,List<ProductsEntity> productsList, String merchantIdentifierText);

   /**
    * 生成关系信息xml
    * @param productsList 产品列表
    * @param merchantIdentifierText 卖家记号
    * @return xml路径
    */
   String generateRelationshipsXML(List<ProductsEntity> productsList, String merchantIdentifierText);

   // 上传产品
   List<String>  singleSubmitFeed(UploadEntity uploadEntity);

   // 同步上传数据
   String submitFeed(String serviceURL,String merchantId,String sellerDevAuthToken,String feedType,String filePath,List<String> marketplaceIdList);

   /**
    * 产品异步上传
    * @param serviceURL 服务器接口网址
    * @param merchantId 店铺id
    * @param sellerDevAuthToken 授权令牌
    * @param typeMap 文件类型map
    * @param filePathMap 文件路径map 用来对应文件类型
    * @param marketplaceIdList 国家端点列表
    * @return 上传id
    */
   List<String> submitAsyncFeed(String serviceURL, String merchantId, String sellerDevAuthToken, Map<String,String> typeMap, Map<String,String> filePathMap, List<String> marketplaceIdList) ;
}
