package io.renren.modules.product.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.product.dto.BatchModifyDto;
import io.renren.modules.product.entity.*;
import io.renren.modules.product.service.*;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
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
    @Autowired
    private FreightCostService freightCostService;
    @Autowired
    private IntroductionService introductionService;
    @Autowired
    private VariantParameterService variantParameterService;
    @Autowired
    private VariantsInfoService variantsInfoService;
    @Autowired
    private CategoryService categoryService;

    /**
     * @param params 产品id
     * @return R
     * page 产品page
     * proCount 产品数量
     * approvedCount 审核通过
     * numberOfVariants 包含变体的商品
     * variantsCount 变体总数
     * @methodname 员工：我的产品列表
     * 管理员：所有产品
     * 根据用户id查询没有被删除的产品，按时间降序排列
     * @auther zjr
     * @date 2018-11-7 9:54
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        Map<String, Object> map = productsService.queryPage(params, getUserId());
        return R.ok().put("page", map.get("page")).put("proNum", map.get("proCount")).put("via", map.get("approvedCount")).put("variant", map.get("numberOfVariants")).put("allVariant", map.get("variantsCount"));
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
     * @return R.ok()
     * @methodname 产品详情
     * @auther zjr
     * @date 2018-11-10 10:23
     */
    @RequestMapping("/info/{productId}")
    public R info(@PathVariable("productId") Long productId) {
        ProductsEntity products = productsService.selectById(productId);

        return R.ok().put("products", products);
    }

    /**
     * @return R.ok()
     * @methodname 保存产品
     * @auther zjr
     * @date 2018-11-10 10:23
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:products:save")
    public R save(@RequestBody ProductsEntity products) {
        // TODO: 2018/11/10
        return R.ok();
    }

    /**
     * @return R里包括productId。
     * @methodname 获取新建产品的id
     * @auther zjr
     * @date 2018-11-10 10:23
     */
    @RequestMapping("/getproductid")
    public R getProductId() {
        Long productId = productsService.getNewProductId(getUserId());
        return R.ok().put("productId", productId);
    }

    /**
     * @return R.ok()
     * @methodname 更新
     * @auther zjr
     * @date 2018-11-10 10:23
     */
    @RequestMapping("/update")
    public R update(@RequestBody ProductsEntity products) {
        ValidatorUtils.validateEntity(products);
        //全部更新
        productsService.updateAllColumnById(products);

        return R.ok();
    }

    /**
     * @methodname: productDetails:产品详情
     * @param: [productId] 产品id
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/9 14:53
     */
    @RequestMapping("productdetails")
    public R queryByIdProductDetails(Long productId) {
        ProductsEntity productsEntity = productsService.selectById(productId);
        //各个国家的运费信息
        Long americanid = productsEntity.getAmericanFreight();
        FreightCostEntity americanFC = freightCostService.selectById(americanid);
        productsEntity.setAmericanFC(americanFC);
        Long canadaid = productsEntity.getCanadaFreight();
        FreightCostEntity canadaFC = freightCostService.selectById(canadaid);
        productsEntity.setCanadaFC(canadaFC);
        Long mexicoid = productsEntity.getMexicoFreight();
        FreightCostEntity mexicoFC = freightCostService.selectById(mexicoid);
        productsEntity.setMexicoFC(mexicoFC);
        Long britainid = productsEntity.getBritainFreight();
        FreightCostEntity britainFC = freightCostService.selectById(britainid);
        productsEntity.setBritainFC(britainFC);
        Long franceid = productsEntity.getFranceFreight();
        FreightCostEntity franceFC = freightCostService.selectById(franceid);
        productsEntity.setFranceFC(franceFC);
        Long germanyid = productsEntity.getGermanyFreight();
        FreightCostEntity germanyFC = freightCostService.selectById(germanyid);
        productsEntity.setGermanyFC(germanyFC);
        Long italyid = productsEntity.getItalyFreight();
        FreightCostEntity italyFC = freightCostService.selectById(italyid);
        productsEntity.setItalyFC(italyFC);
        Long spainid = productsEntity.getSpainFreight();
        FreightCostEntity spainFC = freightCostService.selectById(spainid);
        productsEntity.setSpainFC(spainFC);
        Long japanid = productsEntity.getJapanFreight();
        FreightCostEntity japanFC = freightCostService.selectById(japanid);
        productsEntity.setJapanFC(japanFC);
        Long australiaid = productsEntity.getAustraliaFreight();
        FreightCostEntity australiaFC = freightCostService.selectById(australiaid);
        productsEntity.setAustraliaFC(australiaFC);
        //各个国家的介绍
        int chineseinid = productsEntity.getChineseIntroduction();
        IntroductionEntity chinesePRE = introductionService.selectById(chineseinid);
        productsEntity.setChinesePRE(chinesePRE);
        int britaininid = productsEntity.getBritainIntroduction();
        IntroductionEntity britainPRE = introductionService.selectById(britaininid);
        productsEntity.setBritainPRE(britainPRE);
        int franceinid = productsEntity.getFranceIntroduction();
        IntroductionEntity francePRE = introductionService.selectById(franceinid);
        productsEntity.setFrancePRE(francePRE);
        int germanyInid = productsEntity.getGermanyIntroduction();
        IntroductionEntity germanyPRE = introductionService.selectById(germanyInid);
        productsEntity.setGermanyPRE(germanyPRE);
        int italyInid = productsEntity.getItalyIntroduction();
        IntroductionEntity italyPRE = introductionService.selectById(italyInid);
        productsEntity.setItalyPRE(italyPRE);
        int spainInid = productsEntity.getSpainIntroduction();
        IntroductionEntity spainPRE = introductionService.selectById(spainInid);
        productsEntity.setSpainPRE(spainPRE);
        int japanInid = productsEntity.getJapanIntroduction();
        IntroductionEntity japanPRE = introductionService.selectById(japanInid);
        productsEntity.setJapanPRE(japanPRE);
        //颜色尺寸大小查变体
        int colorid = productsEntity.getColorId();
        VariantParameterEntity colorVP = variantParameterService.selectById(colorid);
        productsEntity.setColorVP(colorVP);
        int sizeid = productsEntity.getSizeId();
        VariantParameterEntity sizeVP = variantParameterService.selectById(sizeid);
        productsEntity.setSizeVP(sizeVP);

        //通过产品id查出变体信息
        List<VariantsInfoEntity> variantsInfos = variantsInfoService.selectList(new EntityWrapper<VariantsInfoEntity>().eq("product_id", productId).orderBy("variant_sort"));
        productsEntity.setVariantsInfos(variantsInfos);
        return R.ok().put("productsEntity", productsEntity);
    }

    /**
     * @methodname: batchModify 产品的批量修改
     * @param: [productIds, batchModifyDto] 产品id数组，包装类
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/11 11:14
     */
    @RequestMapping("batchmodify")
    public R batchModify(Long[] productIds, BatchModifyDto batchModifyDto) {
        //循环遍历出前台传入的产品id
        for (int i = 0; i < productIds.length; i++) {
            ProductsEntity productsEntity = productsService.selectById(productIds[i]);
            productsEntity.setAuditStatus(batchModifyDto.getAuditStatus());
            productsEntity.setShelveStatus(batchModifyDto.getShelveStatus());
            productsEntity.setProductType(batchModifyDto.getProductType());
            //通过前台传入的三级分类id获取一个一二三级的字符串id，以逗号拼接的
            String ids = categoryService.queryParentByChildId(batchModifyDto.getCategoryThreeId());
            String[] id = ids.split(",");
            productsEntity.setCategoryOneId(Long.parseLong(id[0]));
            productsEntity.setCategoryTwoId(Long.parseLong(id[1]));
            productsEntity.setCategoryThreeId(Long.parseLong(id[2]));
            productsEntity.setProducerName(batchModifyDto.getProducerName());
            productsEntity.setManufacturerNumber(batchModifyDto.getManufacturerNumber());
            productsEntity.setBrandName(batchModifyDto.getBrandName());
            productsEntity.setProductWeight(batchModifyDto.getProductWeight());
            productsEntity.setProductLength(batchModifyDto.getProductLength());
            productsEntity.setProductWide(batchModifyDto.getProductWide());
            int chineseId = productsEntity.getChineseIntroduction();
            IntroductionEntity chinesePRE = introductionService.selectById(chineseId);
            String productTitle = productsEntity.getProductTitle();
            //标题前加
            String productTitleQ = batchModifyDto.getProductTitleQ();
            //标题后加
            String productTitleH = batchModifyDto.getProductTitleH();

            if (StringUtils.isNotEmpty(productTitleQ) && StringUtils.isNotEmpty(productTitleH)) {
                //判断标题前加和后加不为空，进行标题拼接
                String productTitlesQH = productTitleQ + productTitle + productTitleH;
                productsEntity.setProductTitle(productTitlesQH);
                chinesePRE.setProductTitle(productTitlesQH);
            } else if (StringUtils.isNotEmpty(productTitleQ) && StringUtils.isEmpty(productTitleH)) {
                //判断标题前加不为空，标题后加为空。进行拼接
                String productTitles = productTitleQ + productTitle;
                productsEntity.setProductTitle(productTitles);
                chinesePRE.setProductTitle(productTitles);
            } else if (StringUtils.isEmpty(productTitleQ) && StringUtils.isNotEmpty(productTitleH)) {
                //判断标题前加为空，标题后加不为空。进行拼接
                String productTitless = productTitle + productTitleH;
                productsEntity.setProductTitle(productTitless);
                chinesePRE.setProductTitle(productTitless);
            } else {
                //标题前加和标题后加都为空，标题不变
                productsEntity.setProductTitle(productTitle);
                chinesePRE.setProductTitle(productTitle);
            }
            //获取原来的买点说明
            String keyPointsJ = chinesePRE.getKeyPoints();
            //前台获取是否追加
            String keyPointsadd = batchModifyDto.getKeyPointsadd();
            if (StringUtils.isNotEmpty(keyPointsadd)) {
                //追加不为空，进行追加
                String keyPointsX = keyPointsJ + batchModifyDto.getKeyPoints();
                chinesePRE.setKeyPoints(keyPointsX);
            } else {
                //追加为空，进行买点说明更新
                chinesePRE.setKeyPoints(batchModifyDto.getKeyPoints());
            }
            //获取原来的关键字
            String keyWordJ = chinesePRE.getKeyWord();
            //前台获取是否追加
            String keywordsadd = batchModifyDto.getKeywordsadd();
            if (StringUtils.isNotEmpty(keywordsadd)) {
                //追加不为空，进行追加
                String keyWordX = keyWordJ + batchModifyDto.getKeyWord();
                chinesePRE.setKeyWord(keyWordX);
            } else {
                //追加为空，进行关键字更新更新
                chinesePRE.setKeyWord(batchModifyDto.getKeyWord());
            }
            String productDescription = chinesePRE.getProductDescription();
            //描述前加
            String productDescriptionQ = batchModifyDto.getProductDescriptionQ();
            //描述后加
            String productDescriptionH = batchModifyDto.getProductDescriptionH();
            if (StringUtils.isNotEmpty(productDescriptionQ) && StringUtils.isNotEmpty(productDescriptionH)) {
                //判断描述前加和后加不为空，进行描述拼接
                String productDescriptionQH = productDescriptionQ + productDescription + productDescriptionH;
                chinesePRE.setProductDescription(productDescriptionQH);
            } else if (StringUtils.isNotEmpty(productDescriptionQ) && StringUtils.isEmpty(productDescriptionH)) {
                //判断描述前加不为空，描述后加为空。进行拼接
                String productDescriptions = productDescriptionQ + productDescription;
                chinesePRE.setProductDescription(productDescriptions);
            } else if (StringUtils.isEmpty(productDescriptionQ) && StringUtils.isNotEmpty(productDescriptionH)) {
                //判断描述前加不为空，描述后加为空。进行拼接
                String productDescriptionss = productDescription + productDescriptionH;
                chinesePRE.setProductDescription(productDescriptionss);
            } else {
                //无需更新
                chinesePRE.setProductDescription(productDescription);
            }
            introductionService.updateById(chinesePRE);
            productsService.updateById(productsEntity);
            //TODO 国家翻译

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
        int totalCount = productsService.getTotalCount(params, getUserId(), "0");
        return R.ok().put("totalCount", totalCount);
    }
}