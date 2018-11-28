package io.renren.modules.product.dao;

import io.renren.modules.product.entity.ProductsEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 产品
 * 
 * @author jhy
 * @email 617493711@qq.com
 * @date 2018-11-08 09:59:27
 */
public interface ProductsDao extends BaseMapper<ProductsEntity> {
    int relationVariantColor(@Param("productId")Long productId, @Param("variantParameterId")Long variantParameterId);

    int relationVariantSize(@Param("productId")Long productId, @Param("variantParameterId")Long variantParameterId);
}
