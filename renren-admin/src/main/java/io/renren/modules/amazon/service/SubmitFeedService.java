package io.renren.modules.amazon.service;

import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.entity.UploadEntity;

import java.util.List;
import java.util.Map;

public interface SubmitFeedService {

   String generateProductXML(String merchantIdentifierText, List<ProductsEntity> productsList, String countryCode, String categoryNodeId);

   String generateImagesXML(List<ProductsEntity> productsList, String merchantIdentifierText);

   String generateInventoryXML(List<ProductsEntity> productsList, String merchantIdentifierText);

   String generatePricesXML(String countryCode,List<ProductsEntity> productsList, String merchantIdentifierText);

   String generateRelationshipsXML(List<ProductsEntity> productsList, String merchantIdentifierText);

   // 上传产品
   List<String>  singleSubmitFeed(UploadEntity uploadEntity);

   // 同步上传数据
   String submitFeed(String serviceURL,String merchantId,String sellerDevAuthToken,String feedType,String filePath,List<String> marketplaceIdList);

   List<String> submitAsyncFeed(String serviceURL, String merchantId, String sellerDevAuthToken, Map<String,String> typeMap, Map<String,String> filePathMap, List<String> marketplaceIdList) ;
}
