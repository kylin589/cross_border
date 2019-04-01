package io.renren.modules.product.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.product.entity.ClaimEntity;
import io.renren.modules.product.entity.ImageAddressEntity;
import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.entity.VariantsInfoEntity;
import io.renren.modules.product.service.ClaimService;
import io.renren.modules.product.service.ImageAddressService;
import io.renren.modules.product.service.ProductsService;
import io.renren.modules.product.service.VariantsInfoService;
import io.renren.modules.product.vm.ClaimVM;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysDeptService;
import io.renren.modules.sys.service.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * 产品认领记录表
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-19 00:26:46
 */
@RestController
@RequestMapping("product/claim")
public class ClaimController extends AbstractController{
    @Autowired
    private ClaimService claimService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private SysDeptService deptService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private VariantsInfoService variantsInfoService;
    @Autowired
    private ImageAddressService imageAddressService;
    /**
     * 认领
     */
    @RequestMapping("/claim")
//    @RequiresPermissions("product:claim:claim")
    public R claim(@RequestBody ClaimVM claimVM){
        Long productId = claimVM.getProductId();

        Long userId = claimVM.getUserId();
        SysUserEntity toUser = userService.selectById(userId);
        Long deptId = toUser.getDeptId();
        ProductsEntity product = productsService.selectById(productId);

        SysDeptEntity dept = deptService.selectById(product.getDeptId());
        SysUserEntity user = userService.selectById(product.getCreateUserId());
        ClaimEntity claimEntity = new ClaimEntity();
        //设置来源产品id
        claimEntity.setProductId(productId);
        //设置来源
        claimEntity.setFromDeptId(dept.getDeptId());
        claimEntity.setFromDeptName(dept.getName());
        claimEntity.setFromUserId(user.getUserId());
        claimEntity.setFromUserName(user.getDisplayName());
        //设置认领到谁
        claimEntity.setToDeptId(deptId);
        claimEntity.setToDeptName(deptService.selectById(deptId).getName());
        claimEntity.setToUserId(userId);
        claimEntity.setToUserName(toUser.getDisplayName());
        //设置操作人
        claimEntity.setOperatorDeptId(getDeptId());
        claimEntity.setOperatorId(getUserId());
        claimEntity.setOperatorName(getUser().getDisplayName());
        claimEntity.setOperatorTime(new Date());
        claimService.insert(claimEntity);
        //获取图片列表
        List<ImageAddressEntity> imageList = imageAddressService.selectList(new EntityWrapper<ImageAddressEntity>().eq("product_id",productId));
        //获取变体列表
        List<VariantsInfoEntity> variantsInfoList = variantsInfoService.selectList(new EntityWrapper<VariantsInfoEntity>().eq("product_id",productId));
        /**
         * 复制产品
         */
        product.setProductId(null);
        product.setCreateUserId(userId);
        product.setDeptId(deptId);
        product.setAuditStatus("002");
        product.setProductType("007");
        product.setEanCode(null);
        product.setIsUpload(0);
        productsService.insert(product);
        Long newProductId = product.getProductId();
        product.setProductSku(toUser.getEnName() + "-" + toUser.getEnBrand() + "-" + newProductId);
        productsService.updateById(product);
        //生成新的变体信息
        if(variantsInfoList != null && variantsInfoList.size() > 0){
            for(int i = 0; i<variantsInfoList.size(); i++){
                variantsInfoList.get(i).setVariantId(null);
                variantsInfoList.get(i).setProductId(newProductId);
                variantsInfoList.get(i).setEanCode(null);
                variantsInfoList.get(i).setVariantSku(null);
                variantsInfoList.get(i).setUserId(userId);
                variantsInfoList.get(i).setDeptId(deptId);
            }
            variantsInfoService.insertBatch(variantsInfoList);
        }
        //生成新的图片信息
        if(imageList != null && imageList.size() > 0){
            for(int i = 0; i < imageList.size(); i++){
                imageList.get(i).setImageId(null);
                imageList.get(i).setProductId(newProductId);
            }
            imageAddressService.insertBatch(imageList);
        }
        return R.ok();
    }
    /**
     * 认领
     */
    @RequestMapping("/batchClaim")
//    @RequiresPermissions("product:claim:claim")
    public R batchClaim(@RequestBody ClaimVM claimVM){
        Long[] productIds = claimVM.getProductIds();
        Long userId = claimVM.getUserId();
        SysUserEntity toUser = userService.selectById(userId);
        Long deptId = toUser.getDeptId();
        for(int i=0; i < productIds.length; i++){
            ProductsEntity product = productsService.selectById(productIds[i]);

            SysDeptEntity dept = deptService.selectById(product.getDeptId());
            SysUserEntity user = userService.selectById(product.getCreateUserId());
            ClaimEntity claimEntity = new ClaimEntity();
            //设置来源产品id
            claimEntity.setProductId(productIds[i]);
            //设置来源
            claimEntity.setFromDeptId(dept.getDeptId());
            claimEntity.setFromDeptName(dept.getName());
            claimEntity.setFromUserId(user.getUserId());
            claimEntity.setFromUserName(user.getDisplayName());
            //设置认领到谁
            claimEntity.setToDeptId(deptId);
            claimEntity.setToDeptName(deptService.selectById(deptId).getName());
            claimEntity.setToUserId(userId);
            claimEntity.setToUserName(toUser.getDisplayName());
            //设置操作人
            claimEntity.setOperatorDeptId(getDeptId());
            claimEntity.setOperatorId(getUserId());
            claimEntity.setOperatorName(getUser().getDisplayName());
            claimEntity.setOperatorTime(new Date());
            claimService.insert(claimEntity);
            //获取图片列表
            List<ImageAddressEntity> imageList = imageAddressService.selectList(new EntityWrapper<ImageAddressEntity>().eq("product_id",productIds[i]));
            //获取变体列表
            List<VariantsInfoEntity> variantsInfoList = variantsInfoService.selectList(new EntityWrapper<VariantsInfoEntity>().eq("product_id",productIds[i]));
            /**
             * 复制产品
             */
            product.setProductId(null);
            product.setCreateUserId(userId);
            product.setDeptId(deptId);
            product.setAuditStatus("002");
            product.setProductType("007");
            product.setEanCode(null);
            productsService.insert(product);
            Long newProductId = product.getProductId();
            product.setProductSku(toUser.getEnName() + "-" + toUser.getEnBrand() + "-" + newProductId);
            productsService.updateById(product);
            //生成新的变体信息
            if(variantsInfoList != null && variantsInfoList.size() > 0){
                for(int x = 0; x < variantsInfoList.size(); x++){
                    variantsInfoList.get(x).setVariantId(null);
                    variantsInfoList.get(x).setProductId(newProductId);
                    variantsInfoList.get(x).setEanCode(null);
                    variantsInfoList.get(x).setVariantSku(null);
                    variantsInfoList.get(x).setUserId(userId);
                    variantsInfoList.get(x).setDeptId(deptId);
                }
                variantsInfoService.insertBatch(variantsInfoList);
            }
            //生成新的图片信息
            if(imageList != null && imageList.size() > 0){
                for(int x = 0; x < imageList.size(); x++){
                    imageList.get(x).setImageId(null);
                    imageList.get(x).setProductId(newProductId);
                }
                imageAddressService.insertBatch(imageList);
            }
        }
        return R.ok();
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("product:claim:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = claimService.queryPage(params,getDeptId());

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{productClaimId}")
    @RequiresPermissions("product:claim:info")
    public R info(@PathVariable("productClaimId") Long productClaimId){
        ClaimEntity claim = claimService.selectById(productClaimId);

        return R.ok().put("claim", claim);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:claim:save")
    public R save(@RequestBody ClaimEntity claim){
        claimService.insert(claim);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:claim:update")
    public R update(@RequestBody ClaimEntity claim){
        //ValidatorUtils.validateEntity((claim);
        claimService.updateAllColumnById(claim);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:claim:delete")
    public R delete(@RequestBody Long[] productClaimIds){
        claimService.deleteBatchIds(Arrays.asList(productClaimIds));

        return R.ok();
    }

}
