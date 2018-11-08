package io.renren.modules.product.controller;

import io.renren.common.utils.R;
import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.service.ProductsService;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 产品回收站
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
@RestController
@RequestMapping("product/productrecycling")
public class ProductRecyclingController extends AbstractController {

    @Autowired
    private ProductsService productsService;

    /**
     * @methodname 产品回收站
     * @param params 产品id
     * @return R
     *          page 产品page
     *          proCount 产品数量
     *          approvedCount 审核通过
     *          numberOfVariants 包含变体的商品
     *          variantsCount 变体总数
     * @auther zjr
     * @date 2018-11-7 9:54
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        Map<String, Object> map = productsService.queryRecyclingPage(params, getUserId());
        return R.ok().put("page", map.get("page")).put("proCount", map.get("proCount")).put("approvedCount", map.get("approvedCount")).put("numberOfVariants", map.get("numberOfVariants")).put("variantsCount", map.get("variantsCount"));
    }

    /**
     * @param productIds 产品id
     * @return R
     * @methodname 恢复
     * @auther zjr
     * @date 2018-11-7 9:59
     */
    @RequestMapping("/restore")
    public R restore(@RequestBody Long[] productIds) {
        for (int i = 0; i < productIds.length; i++) {
            ProductsEntity entity = new ProductsEntity();
            entity.setProductId(productIds[i]);
            entity.setIsDeleted(0);
            entity.setLastOperationTime(new Date());
            entity.setLastOperationUserId(getUserId());
            productsService.updateById(entity);
        }
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:productrecycling:delete")
    public R delete(@RequestBody Long[] productIds) {
        productsService.deleteBatchIds(Arrays.asList(productIds));

        return R.ok();
    }
}
