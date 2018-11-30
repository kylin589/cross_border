package io.renren.modules.amazon.service;

import io.renren.modules.product.entity.ProductsEntity;

import java.util.List;

public interface SubmitFeedService {

   // TODO: 2018/11/29 改写产品
   String generateProductXML(String countryCode,Long[] productIds);

   String generateImagesXML(List<ProductsEntity> productsList, String merchantIdentifierText);

   String generateInventoryXML(List<ProductsEntity> productsList, String merchantIdentifierText);

   String generatePricesXML(String countryCode,List<ProductsEntity> productsList, String merchantIdentifierText);

   String generateRelationshipsXML(List<ProductsEntity> productsList, String merchantIdentifierText);
}
