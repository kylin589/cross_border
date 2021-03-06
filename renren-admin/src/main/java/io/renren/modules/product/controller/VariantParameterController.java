package io.renren.modules.product.controller;

import java.util.Arrays;
import java.util.Map;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.entity.VariantsInfoEntity;
import io.renren.modules.product.service.ProductsService;
import io.renren.modules.product.service.VariantsInfoService;
import io.renren.modules.product.vm.VariantParameterDelVM;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.product.entity.VariantParameterEntity;
import io.renren.modules.product.service.VariantParameterService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 变体参数
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
@RestController
@RequestMapping("product/variantparameter")
public class VariantParameterController {
    @Autowired
    private VariantParameterService variantParameterService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private VariantsInfoService variantsInfoService;

    /**
     * @methodname: list 列表
     * @param: [params] 参数接受
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:variantparameter:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = variantParameterService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * @methodname: info 变体信息
     * @param: [paramsId] 变体id
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/info/{paramsId}")
    @RequiresPermissions("product:variantparameter:info")
    public R info(@PathVariable("paramsId") Long paramsId){
        VariantParameterEntity variantParameter = variantParameterService.selectById(paramsId);

        return R.ok().put("variantParameter", variantParameter);
    }

    /**
     * @methodname: save 保存
     * @param: [variantParameter] 变体实体
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/save")
//    @RequiresPermissions("product:variantparameter:save")
    public R save(String paramsType,String paramsValue,long productId){
        VariantParameterEntity variantParameter = new VariantParameterEntity();
        variantParameter.setParamsType(paramsType);
        variantParameter.setParamsValue(paramsValue);
        variantParameterService.insert(variantParameter);
        Long id =variantParameter.getParamsId();
        boolean flag;
        if("color".equals(paramsType)){
            flag = productsService.relationVariantColor(productId,id);
        }else{
            flag = productsService.relationVariantSize(productId,id);
        }
        if(flag){
            return R.ok().put("variantParameterId",variantParameter.getParamsId());
        }else{
            return R.error("保存失败，请联系管理员");
        }
    }

    /**
     * @methodname: delete 修改
     * @param: [variantParameter] 变体实体
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/update")
//    @RequiresPermissions("product:variantparameter:update")
    public R update(@RequestBody VariantParameterEntity variantParameter){
        //ValidatorUtils.validateEntity((variantParameter);
        variantParameterService.updateAllColumnById(variantParameter);//全部更新

        return R.ok();
    }

    /**
     * @methodname: delete 删除
     * @param: [paramsIds] 变体id数组
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    /*@RequestMapping("/delete")
    @RequiresPermissions("product:variantparameter:delete")
    public R delete(@RequestBody Long[] paramsIds){
        variantParameterService.deleteBatchIds(Arrays.asList(paramsIds));

        return R.ok();
    }*/
    /**
     * @methodname: delete 删除
     * @param: [productId, variantParameterEntity]
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/27 21:45
     */
    @RequestMapping("/delete")
   // @RequiresPermissions("product:variantparameter:delete")
    public R delete( @RequestBody VariantParameterDelVM variantParameterDelVM){
        VariantParameterEntity variantParameterEntity = variantParameterDelVM.getVariantParameter();
        String paramsType = variantParameterEntity.getParamsType();
        if (paramsType.equals("color")){
            ProductsEntity productsEntity = productsService.selectById(variantParameterDelVM.getProductId());
            productsEntity.setColorId(0L);
            productsService.updateById(productsEntity);
        }else {
            ProductsEntity productsEntity = productsService.selectById(variantParameterDelVM.getProductId());
            productsEntity.setSizeId(0L);
            productsService.updateById(productsEntity);
        }
        variantParameterService.deleteById(variantParameterEntity);
        return R.ok();
    }
}
