package io.renren.modules.product.controller;

import io.renren.common.utils.R;
import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.service.ProductsService;
import io.renren.modules.product.vm.ProductIdsVM;
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
     * @param params 产品回收站列表
     * @return R
     * page 产品page
     * proCount 产品数量
     * approvedCount 审核通过
     * numberOfVariants 包含变体的商品
     * variantsCount 变体总数
     * @methodname 产品回收站列表
     * @auther zjr
     * @date 2018-11-7 9:54
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        Map<String, Object> map = productsService.queryRecyclingPage(params, getUserId());
        return R.ok().put("page", map.get("page")).put("proNum", map.get("proCount")).put("via", map.get("approvedCount")).put("variant", map.get("numberOfVariants")).put("allVariant", map.get("variantsCount"));
    }

    /**
     * @param
     * @return R productIdsVM
     * @methodname 产品一键恢复
     * 把is_deleted从1该为0
     * @auther zjr
     * @date 2018-11-7 9:59
     */
    @RequestMapping("/restore")
    public R restore(@RequestBody ProductIdsVM productIdsVM) {
        Long[] productIds = productIdsVM.getProductIds();
        for (int i = 0; i < productIds.length; i++) {
            ProductsEntity entity = productsService.selectById(productIds[i]);
            if(entity != null){
                entity.setIsDeleted(0);
                entity.setLastOperationTime(new Date());
                entity.setLastOperationUserId(getUserId());
                productsService.updateById(entity);
            }
        }
        return R.ok();
    }

    /**
     * @methodname: getTotalCount 获取总记录数
     * @param: [params] 接受参数
     * @return: io.renren.common.utils.R
     * @auther: zjr
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/gettotalcount")
    public R getTotalCount(@RequestParam Map<String, Object> params) {
        int totalCount = productsService.getMyTotalCount(params, getUserId(), "1");
        return R.ok().put("totalCount", totalCount);
    }

    /**
     * @methodname: delete 彻底删除
     * @param: [productIds] 产品id数组
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/batchdelete")
    //@RequiresPermissions("product:productrecycling:delete")
    public R delete(@RequestBody ProductIdsVM productIdsVM) {
        Long[] productIds = productIdsVM.getProductIds();
        productsService.deleteBatchIds(Arrays.asList(productIds));
        return R.ok();
    }
}
