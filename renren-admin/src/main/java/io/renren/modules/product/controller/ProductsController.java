package io.renren.modules.product.controller;

import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.service.ProductsService;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;


/**
 * 产品
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
@RestController
@RequestMapping("product/products")
public class ProductsController extends AbstractController {
    @Autowired
    private ProductsService productsService;

    /**
     * @methodname 员工：我的产品列表
     *               管理员：所有产品
     *               根据用户id查询没有被删除的产品，按时间降序排列
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
    @RequiresPermissions("product:products:list")
    public R list(@RequestParam Map<String, Object> params) {
        Map<String, Object> map = productsService.queryPage(params, getUserId());
        return R.ok().put("page", map.get("page")).put("proCount", map.get("proCount")).put("approvedCount", map.get("approvedCount")).put("numberOfVariants", map.get("numberOfVariants")).put("variantsCount", map.get("variantsCount"));
    }

    /**
     * @param productIds 产品id
     * @param number     状态number，如001
     * @param type       以什么状态进行修改，如AUDIT_STATE
     * @return R
     * @methodname: 更改产品的审核、上架、产品状态
     * @auther zjr
     * @date 2018-11-7 9:54
     */
    @RequestMapping("/changeauditstatus")
    public R changeAuditStatus(@RequestBody Long[] productIds, String number, String type) {
        for (int i = 0; i < productIds.length; i++) {
            ProductsEntity entity = new ProductsEntity();
            entity.setProductId(productIds[i]);
            if (type == "AUDIT_STATE") {
                entity.setAuditStatus(number);
            }
            if (type == "SHELVE_STATE") {
                entity.setShelveStatus(number);
            }
            if (type == "PRODUCT_TYPE") {
                entity.setProductType(number);
            }
            entity.setLastOperationTime(new Date());
            entity.setLastOperationUserId(getUserId());
            productsService.updateById(entity);
        }
        return R.ok();
    }

    /**
     * @param productIds 产品id
     * @return R
     * @methodname 产品假删除
     * @auther zjr
     * @date 2018-11-7 9:58
     */
    @RequestMapping("/falsedeletion")
    public R falseDeletion(@RequestBody Long[] productIds) {
        for (int i = 0; i < productIds.length; i++) {
            ProductsEntity entity = new ProductsEntity();
            entity.setProductId(productIds[i]);
            entity.setIsDeleted(1);
            entity.setLastOperationTime(new Date());
            entity.setLastOperationUserId(getUserId());
            productsService.updateById(entity);
        }
        return R.ok();
    }

    /**
     * @return R里包括SKU结果。
     * @methodname 生成SKU
     * @auther zjr
     * @date 2018-11-7 11:23
     */
    @RequestMapping("/generateSKU")
    public R generateSKU() {
        // TODO: 2018/11/7 根据用户的英文缩写生成SKU
        String SKU = null;
        return R.ok().put("SKU", SKU);
    }

    /**
     * @return R里包括SKU结果。
     * @methodname 修正SKU
     * @auther zjr
     * @date 2018-11-7 11:23
     */
    @RequestMapping("/modifySKU")
    public R modifySKU() {
        // TODO: 2018/11/7 根据产品的SKU 重新修正
        String SKU = null;
        return R.ok().put("SKU", SKU);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{productId}")
    @RequiresPermissions("product:products:info")
    public R info(@PathVariable("productId") Long productId) {
        ProductsEntity products = productsService.selectById(productId);

        return R.ok().put("products", products);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:products:save")
    public R save(@RequestBody ProductsEntity products) {
        productsService.insert(products);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:products:update")
    public R update(@RequestBody ProductsEntity products) {
        ValidatorUtils.validateEntity(products);
        productsService.updateAllColumnById(products);//全部更新

        return R.ok();
    }

}
