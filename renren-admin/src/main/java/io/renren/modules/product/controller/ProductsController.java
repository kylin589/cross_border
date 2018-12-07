package io.renren.modules.product.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.amazon.util.COUNTY;
import io.renren.modules.product.dto.BatchModifyDto;
import io.renren.modules.product.entity.*;
import io.renren.modules.product.service.*;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hpsf.Decimal;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;


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
    @Autowired
    private FreightPriceService freightPriceService;

    private String US;//美国
    private String CA;//加拿大
    private String MX;// 墨西哥
    private String GB;// 英国
    private String FR;// 法国
    private String DE;// 德国
    private String IT;// 意大利
    private String ES;// 西班牙
    private String JP; // 日本
    private String AU; // 澳大利亚

    /**
     * @param params 我的产品列表
     * @return R
     * page 产品page
     * proCount 产品数量
     * approvedCount 审核通过
     * numberOfVariants 包含变体的商品
     * variantsCount 变体总数
     * @methodname 员工：我的产品列表
     * 根据用户id查询没有被删除的产品，按时间降序排列
     * @auther zjr
     * @date 2018-11-7 9:54
     */
    @RequestMapping("/mylist")
    public R list(@RequestParam Map<String, Object> params) {
        //当前用户的产品列表
        Map<String, Object> map = productsService.queryMyPage(params, getUserId());
        return R.ok().put("page", map.get("page")).put("proNum", map.get("proCount")).put("via", map.get("approvedCount")).put("variant", map.get("numberOfVariants")).put("allVariant", map.get("variantsCount"));
    }

    /**
     * @methodname: allList 所有产品列表
     * @param: [params]
     * @return: io.renren.common.utils.R
     * page 产品page
     * proCount 产品数量
     * approvedCount 审核通过
     * numberOfVariants 包含变体的商品
     * variantsCount 变体总数
     * @methodname 管理员：所有产品列表
     * 根据用户id查询没有被删除的产品，按时间降序排列
     * @auther: jhy
     * @date: 2018/11/29 19:32
     */
    @RequestMapping("/alllist")
    public R allList(@RequestParam Map<String, Object> params) {
        //公司所有产品列表
        Map<String, Object> map = productsService.queryAllPage(params, getDeptId());
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
    @RequestMapping("/productdetails")
    public R queryByIdProductDetails(@RequestParam("productId") Long productId) {
        ProductsEntity productsEntity = productsService.selectById(productId);
        //各个国家的运费信息
        Long americanid = productsEntity.getAmericanFreight();
        if (americanid != null) {
            FreightCostEntity americanFC = freightCostService.selectById(americanid);
            productsEntity.setAmericanFC(americanFC);
        }
        Long canadaid = productsEntity.getCanadaFreight();
        if (canadaid != null) {
            FreightCostEntity canadaFC = freightCostService.selectById(canadaid);
            productsEntity.setCanadaFC(canadaFC);
        }
        Long mexicoid = productsEntity.getMexicoFreight();
        if (mexicoid != null) {
            FreightCostEntity mexicoFC = freightCostService.selectById(mexicoid);
            productsEntity.setMexicoFC(mexicoFC);
        }
        Long britainid = productsEntity.getBritainFreight();
        if (britainid != null) {
            FreightCostEntity britainFC = freightCostService.selectById(britainid);
            productsEntity.setBritainFC(britainFC);
        }
        Long franceid = productsEntity.getFranceFreight();
        if (franceid != null) {
            FreightCostEntity franceFC = freightCostService.selectById(franceid);
            productsEntity.setFranceFC(franceFC);
        }
        Long germanyid = productsEntity.getGermanyFreight();
        if (germanyid != null) {
            FreightCostEntity germanyFC = freightCostService.selectById(germanyid);
            productsEntity.setGermanyFC(germanyFC);
        }
        Long italyid = productsEntity.getItalyFreight();
        if (italyid != null) {
            FreightCostEntity italyFC = freightCostService.selectById(italyid);
            productsEntity.setItalyFC(italyFC);
        }
        Long spainid = productsEntity.getSpainFreight();
        if (spainid != null) {
            FreightCostEntity spainFC = freightCostService.selectById(spainid);
            productsEntity.setSpainFC(spainFC);
        }
        Long japanid = productsEntity.getJapanFreight();
        if (japanid != null) {
            FreightCostEntity japanFC = freightCostService.selectById(japanid);
            productsEntity.setJapanFC(japanFC);
        }
        Long australiaid = productsEntity.getAustraliaFreight();
        if (australiaid != null) {
            FreightCostEntity australiaFC = freightCostService.selectById(australiaid);
            productsEntity.setAustraliaFC(australiaFC);
        }
        //各个国家的介绍
        Long chineseinid = productsEntity.getChineseIntroduction();
        if (chineseinid != null) {
            IntroductionEntity chinesePRE = introductionService.selectById(chineseinid);
            productsEntity.setChinesePRE(chinesePRE);
        }
        Long britaininid = productsEntity.getBritainIntroduction();
        if (britaininid != null) {
            IntroductionEntity britainPRE = introductionService.selectById(britaininid);
            productsEntity.setBritainPRE(britainPRE);
        }
        Long franceinid = productsEntity.getFranceIntroduction();
        if (franceinid != null) {
            IntroductionEntity francePRE = introductionService.selectById(franceinid);
            productsEntity.setFrancePRE(francePRE);
        }
        Long germanyInid = productsEntity.getGermanyIntroduction();
        if (germanyInid != null) {
            IntroductionEntity germanyPRE = introductionService.selectById(germanyInid);
            productsEntity.setGermanyPRE(germanyPRE);
        }
        Long italyInid = productsEntity.getItalyIntroduction();
        if (italyInid != null) {
            IntroductionEntity italyPRE = introductionService.selectById(italyInid);
            productsEntity.setItalyPRE(italyPRE);
        }
        Long spainInid = productsEntity.getSpainIntroduction();
        if (spainInid != null) {
            IntroductionEntity spainPRE = introductionService.selectById(spainInid);
            productsEntity.setSpainPRE(spainPRE);
        }
        Long japanInid = productsEntity.getJapanIntroduction();
        if (japanInid != null) {
            IntroductionEntity japanPRE = introductionService.selectById(japanInid);
            productsEntity.setJapanPRE(japanPRE);
        }
        //颜色尺寸大小查变体
        Long colorid = productsEntity.getColorId();
        if (colorid != null) {
            VariantParameterEntity colorVP = variantParameterService.selectById(colorid);
            productsEntity.setColorVP(colorVP);
        }
        Long sizeid = productsEntity.getSizeId();
        if (sizeid != null) {
            VariantParameterEntity sizeVP = variantParameterService.selectById(sizeid);
            productsEntity.setSizeVP(sizeVP);
        }
        //通过产品id查出变体信息
        List<VariantsInfoEntity> variantsInfos = new ArrayList<VariantsInfoEntity>();
        variantsInfos = variantsInfoService.selectList(new EntityWrapper<VariantsInfoEntity>().eq("product_id", productId).orderBy("variant_sort"));
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
    @RequestMapping("/batchmodify")
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
            Long chineseId = productsEntity.getChineseIntroduction();
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
        int totalCount = productsService.getMyTotalCount(params, getUserId(), "0");
        return R.ok().put("totalCount", totalCount);
    }

    /**
     * @methodname: collectProduct 丰富采集产品
     * @param: [productId]
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/26 20:53
     */
    @RequestMapping("/collectproduct")
    public R collectProduct(@RequestParam("productId") Long productId) {
        ProductsEntity productsEntity = productsService.selectById(productId);
        Long categoryThreeId = productsEntity.getCategoryThreeId();
        String s = categoryService.queryParentByChildIdAndCategory(categoryThreeId, productsEntity);
        System.out.println(s);
        String[] ids = s.split(",");
        productsEntity.setCategoryOneId(Long.parseLong(ids[0]));
        productsEntity.setCategoryTwoId(Long.parseLong(ids[1]));
        productsEntity.setCreateTime(new Date());
        productsEntity.setLastOperationTime(new Date());
        productsEntity.setCreateUserId(getUserId());
        productsEntity.setDeptId(getDeptId());
        productsService.updateById(productsEntity);
        return R.ok();
    }

    /**
     * @methodname: originalProduct 原创产品
     * @param: [products]
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/27 10:01
     */
    @RequestMapping("/originalproduct")
    public R originalProduct(@RequestBody ProductsEntity products) {
        //通过三级id查出一级二级三级的id字符串，以逗号进行拆分。存入产品
        Long threeId = products.getCategoryThreeId();
        String idString = categoryService.queryParentByChildId(threeId);
        String[] id = idString.split(",");
        products.setCategoryOneId(Long.parseLong(id[0]));
        products.setCategoryTwoId(Long.parseLong(id[1]));
        products.setCategoryThreeId(Long.parseLong(id[2]));
        //美国运费
        FreightCostEntity americanFC = products.getAmericanFC();
        freightCostService.insert(americanFC);
        products.setAmericanFreight(americanFC.getFreightCostId());

        // 加拿大运费
        FreightCostEntity canadaFC = products.getCanadaFC();
        freightCostService.insert(canadaFC);
        products.setCanadaFreight(canadaFC.getFreightCostId());
        // 墨西哥运费
        FreightCostEntity mexicoFC = products.getMexicoFC();
        freightCostService.insert(mexicoFC);
        products.setMexicoFreight(mexicoFC.getFreightCostId());
        //英国运费
        FreightCostEntity britainFC = products.getBritainFC();
        freightCostService.insert(britainFC);
        products.setBritainFreight(britainFC.getFreightCostId());

        // 法国运费
        FreightCostEntity franceFC = products.getFranceFC();
        freightCostService.insert(franceFC);
        products.setFranceFreight(franceFC.getFreightCostId());

        // 德国运费
        FreightCostEntity germanyFC = products.getGermanyFC();
        freightCostService.insert(germanyFC);
        products.setGermanyFreight(germanyFC.getFreightCostId());
        //意大利运费
        FreightCostEntity italyFC = products.getItalyFC();
        freightCostService.insert(italyFC);
        products.setItalyFreight(italyFC.getFreightCostId());
        //西班牙运费
        FreightCostEntity spainFC = products.getSpainFC();
        freightCostService.insert(spainFC);
        products.setSpainFreight(spainFC.getFreightCostId());
        // 日本运费
        FreightCostEntity japanFC = products.getJapanFC();
        freightCostService.insert(japanFC);
        products.setJapanFreight(japanFC.getFreightCostId());
        //澳大利亚运费
        FreightCostEntity australiaFC = products.getAustraliaFC();
        freightCostService.insert(australiaFC);
        products.setAustraliaFreight(australiaFC.getFreightCostId());

        //中文介绍
        IntroductionEntity chinesePRE = products.getChinesePRE();
        introductionService.insert(chinesePRE);
        //products.setProductTitle(chinesePRE.getProductTitle());
        products.setChineseIntroduction(chinesePRE.getIntroductionId());
        //英文介绍
        IntroductionEntity britainPRE = products.getBritainPRE();
        introductionService.insert(britainPRE);
        products.setBritainIntroduction(britainPRE.getIntroductionId());

        //产品标题
        if (chinesePRE.getProductTitle() != null) {
            products.setProductTitle(chinesePRE.getProductTitle());
        } else if (britainPRE.getProductTitle() != null) {
            products.setProductTitle(britainPRE.getProductTitle());
        }

        //法语介绍
        IntroductionEntity francePRE = products.getFrancePRE();
        introductionService.insert(francePRE);
        products.setFranceIntroduction(francePRE.getIntroductionId());
        //德语介绍
        IntroductionEntity germanyPRE = products.getGermanyPRE();
        introductionService.insert(germanyPRE);
        products.setGermanyIntroduction(germanyPRE.getIntroductionId());
        //意大利语介绍
        IntroductionEntity italyPRE = products.getItalyPRE();
        introductionService.insert(italyPRE);
        products.setItalyIntroduction(italyPRE.getIntroductionId());
        //西班牙语介绍
        IntroductionEntity spainPRE = products.getSpainPRE();
        introductionService.insert(spainPRE);
        products.setSpainIntroduction(spainPRE.getIntroductionId());
        //日语介绍
        IntroductionEntity japanPRE = products.getJapanPRE();
        introductionService.insert(japanPRE);
        products.setJapanIntroduction(japanPRE.getIntroductionId());
        //创建时间
        products.setCreateTime(new Date());
        //创建用户id
        products.setCreateUserId(this.getUserId());
        //最后的操作时间
        products.setLastOperationTime(new Date());
        //获取最后操作用户id
        products.setLastOperationUserId(this.getUserId());
        //公司id
        products.setDeptId(this.getDeptId());
        //插入变体信息
        List<VariantsInfoEntity> variantsInfosList = products.getVariantsInfos();
        if (variantsInfosList != null && variantsInfosList.size() != 0) {
            for (VariantsInfoEntity variantsInfoEntity : variantsInfosList) {
                variantsInfoEntity.setProductId(products.getProductId());
            }
        }
        variantsInfoService.insertBatch(variantsInfosList);
        //根据产品id进行更新
        productsService.updateById(products);
        return R.ok();
    }

    /**
     * @methodname: modifyProduct 修改产品
     * @param: [products]
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/27 13:15
     */
    @RequestMapping("/modifyproduct")
    public R modifyProduct(@RequestBody ProductsEntity products) {
        //通过三级id查出一级二级三级的id字符串，以逗号进行拆分。存入产品
        Long threeId = products.getCategoryThreeId();
        String idString = categoryService.queryParentByChildId(threeId);
        String[] id = idString.split(",");
        products.setCategoryOneId(Long.parseLong(id[0]));
        products.setCategoryTwoId(Long.parseLong(id[1]));
        products.setCategoryThreeId(Long.parseLong(id[2]));
        //各个国家的运费信息
        //美国运费
        FreightCostEntity americanFC = products.getAmericanFC();
        if (americanFC != null) {
            if (americanFC.getFreightCostId() != null) {
                freightCostService.updateById(americanFC);
            } else {
                freightCostService.insert(americanFC);
                products.setAmericanFreight(americanFC.getFreightCostId());
            }
        }
        // 加拿大运费
        FreightCostEntity canadaFC = products.getCanadaFC();
        if (canadaFC != null) {
            if (canadaFC.getFreightCostId() != null) {
                freightCostService.updateById(canadaFC);
            } else {
                freightCostService.insert(canadaFC);
                products.setCanadaFreight(canadaFC.getFreightCostId());
            }
        }
        // 墨西哥运费
        FreightCostEntity mexicoFC = products.getMexicoFC();
        if (mexicoFC != null) {
            if (mexicoFC.getFreightCostId() != null) {
                freightCostService.updateById(mexicoFC);
            } else {
                freightCostService.insert(mexicoFC);
                products.setMexicoFreight(mexicoFC.getFreightCostId());
            }
        }
        //英国运费
        FreightCostEntity britainFC = products.getBritainFC();
        if (britainFC != null) {
            if (britainFC.getFreightCostId() != null) {
                freightCostService.updateById(britainFC);
            } else {
                freightCostService.insert(britainFC);
                products.setBritainFreight(britainFC.getFreightCostId());
            }
        }
        // 法国运费
        FreightCostEntity franceFC = products.getFranceFC();
        if (franceFC != null) {
            if (franceFC.getFreightCostId() != null) {
                freightCostService.updateById(franceFC);
            } else {
                freightCostService.insert(franceFC);
                products.setFranceFreight(franceFC.getFreightCostId());
            }
        }
        // 德国运费
        FreightCostEntity germanyFC = products.getGermanyFC();
        if (germanyFC != null) {
            if (germanyFC.getFreightCostId() != null) {
                freightCostService.updateById(germanyFC);
            } else {
                freightCostService.insert(germanyFC);
                products.setGermanyFreight(germanyFC.getFreightCostId());
            }
        }
        //意大利运费
        FreightCostEntity italyFC = products.getItalyFC();
        if (italyFC != null) {
            if (italyFC.getFreightCostId() != null) {
                freightCostService.updateById(italyFC);
            } else {
                freightCostService.insert(italyFC);
                products.setItalyFreight(italyFC.getFreightCostId());
            }
        }
        //西班牙运费
        FreightCostEntity spainFC = products.getSpainFC();
        if (spainFC != null) {
            if (spainFC.getFreightCostId() != null) {
                freightCostService.updateById(spainFC);
            } else {
                freightCostService.insert(spainFC);
                products.setSpainFreight(spainFC.getFreightCostId());
            }
        }
        // 日本运费
        FreightCostEntity japanFC = products.getJapanFC();
        if (japanFC != null) {
            if (japanFC.getFreightCostId() != null) {
                freightCostService.updateById(japanFC);
            } else {
                freightCostService.insert(japanFC);
                products.setJapanFreight(japanFC.getFreightCostId());
            }
        }
        //澳大利亚运费
        FreightCostEntity australiaFC = products.getAustraliaFC();
        if (australiaFC != null) {
            if (australiaFC.getFreightCostId() != null) {
                freightCostService.updateById(australiaFC);
            } else {
                freightCostService.insert(australiaFC);
                products.setAustraliaFreight(australiaFC.getFreightCostId());
            }
        }

        //中文介绍
        IntroductionEntity chinesePRE = products.getChinesePRE();
        if (chinesePRE != null) {
            if (chinesePRE.getIntroductionId() != null) {
                introductionService.updateById(chinesePRE);
            } else {
                introductionService.insert(chinesePRE);
                products.setChineseIntroduction(chinesePRE.getIntroductionId());
            }
        }
        //英文介绍
        IntroductionEntity britainPRE = products.getBritainPRE();
        if (britainPRE != null) {
            if (britainPRE.getIntroductionId() != null) {
                introductionService.updateById(britainPRE);
            } else {
                introductionService.insert(britainPRE);
                products.setBritainIntroduction(britainPRE.getIntroductionId());
            }
        }
        //产品标题
        if (chinesePRE.getProductTitle() != null) {
            products.setProductTitle(chinesePRE.getProductTitle());
        } else if (britainPRE.getProductTitle() != null) {
            products.setProductTitle(britainPRE.getProductTitle());
        }
        //法语介绍
        IntroductionEntity francePRE = products.getFrancePRE();
        if (francePRE != null) {
            if (francePRE.getIntroductionId() != null) {
                introductionService.updateById(francePRE);
            } else {
                introductionService.insert(francePRE);
                products.setFranceIntroduction(francePRE.getIntroductionId());
            }

        }
        //德语介绍
        IntroductionEntity germanyPRE = products.getGermanyPRE();
        if (germanyPRE != null) {
            if (germanyPRE.getIntroductionId() != null) {
                introductionService.updateById(germanyPRE);
            } else {
                introductionService.insert(germanyPRE);
                products.setGermanyIntroduction(germanyPRE.getIntroductionId());
            }
        }
        //意大利语介绍
        IntroductionEntity italyPRE = products.getItalyPRE();
        if (italyPRE != null) {
            if (italyPRE.getIntroductionId() != null) {
                introductionService.updateById(italyPRE);
            } else {
                introductionService.insert(italyPRE);
                products.setItalyIntroduction(italyPRE.getIntroductionId());
            }

        }
        //西班牙语介绍
        IntroductionEntity spainPRE = products.getSpainPRE();
        if (spainPRE != null) {
            if (spainPRE.getIntroductionId() != null) {
                introductionService.updateById(spainPRE);
            } else {
                introductionService.insert(spainPRE);
                products.setSpainIntroduction(spainPRE.getIntroductionId());
            }
        }
        //日语介绍
        IntroductionEntity japanPRE = products.getJapanPRE();
        if (japanPRE != null) {
            if (japanPRE.getIntroductionId() != null) {
                introductionService.updateById(japanPRE);
            } else {
                introductionService.insert(japanPRE);
                products.setJapanIntroduction(japanPRE.getIntroductionId());
            }
        }
        //最后的操作时间
        products.setLastOperationTime(new Date());
        //获取最后操作用户id
        products.setLastOperationUserId(this.getUserId());
        Long productId = products.getProductId();
        //批量删除变体信息
        variantsInfoService.delete(new EntityWrapper<VariantsInfoEntity>().eq("product_id", productId));
        //插入变体信息
        List<VariantsInfoEntity> variantsInfosList = products.getVariantsInfos();
        if (variantsInfosList != null && variantsInfosList.size() != 0) {
            for (VariantsInfoEntity variantsInfoEntity : variantsInfosList) {
                variantsInfoEntity.setProductId(products.getProductId());
            }
        }
        variantsInfoService.insertBatch(variantsInfosList);
        //根据产品id进行更新
        productsService.updateById(products);
        return R.ok();
    }


    /**
     * @methodname: costFreight 成本运费
     * @param: [productsEntity]
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/12/6 11:39
     */
    @RequestMapping("/costFreight")
    public R costFreight(ProductsEntity productsEntity) {

        //各个国家的运费信息
        //美国运费
        Long americanid = productsEntity.getAmericanFreight();
        FreightCostEntity americanFC = null;
        if (americanid != null) {
            americanFC = freightCostService.selectById(americanid);
            productsEntity.setAmericanFC(americanFC);
        }
        //加拿大运费
        Long canadaid = productsEntity.getCanadaFreight();
        FreightCostEntity canadaFC = null;
        if (canadaid != null) {
            canadaFC = freightCostService.selectById(canadaid);
            productsEntity.setCanadaFC(canadaFC);
        }
        //墨西哥运费
        Long mexicoid = productsEntity.getMexicoFreight();
        FreightCostEntity mexicoFC = null;
        if (mexicoid != null) {
            mexicoFC = freightCostService.selectById(mexicoid);
            productsEntity.setMexicoFC(mexicoFC);
        }
        //英国运费
        Long britainid = productsEntity.getBritainFreight();
        FreightCostEntity britainFC = null;
        if (britainid != null) {
            britainFC = freightCostService.selectById(britainid);
            productsEntity.setBritainFC(britainFC);
        }
        //法国运费
        Long franceid = productsEntity.getFranceFreight();
        FreightCostEntity franceFC = null;
        if (franceid != null) {
            franceFC = freightCostService.selectById(franceid);
            productsEntity.setFranceFC(franceFC);
        }
        //德国运费
        Long germanyid = productsEntity.getGermanyFreight();
        FreightCostEntity germanyFC = null;
        if (germanyid != null) {
            germanyFC = freightCostService.selectById(germanyid);
            productsEntity.setGermanyFC(germanyFC);
        }
        //意大利运费
        Long italyid = productsEntity.getItalyFreight();
        FreightCostEntity italyFC = null;
        if (italyid != null) {
            italyFC = freightCostService.selectById(italyid);
            productsEntity.setItalyFC(italyFC);
        }
        //西班牙运费
        Long spainid = productsEntity.getSpainFreight();
        FreightCostEntity spainFC = null;
        if (spainid != null) {
            spainFC = freightCostService.selectById(spainid);
            productsEntity.setSpainFC(spainFC);
        }
        //日本运费
        Long japanid = productsEntity.getJapanFreight();
        FreightCostEntity japanFC = null;
        if (japanid != null) {
            japanFC = freightCostService.selectById(japanid);
            productsEntity.setJapanFC(japanFC);
        }
        //澳大利亚运费
        Long australiaid = productsEntity.getAustraliaFreight();
        FreightCostEntity australiaFC = null;
        if (australiaid != null) {
            australiaFC = freightCostService.selectById(australiaid);
            productsEntity.setAustraliaFC(australiaFC);
        }
        //产品重量
        Double productWeight = productsEntity.getProductWeight();
        //产品长
        Double productLength = productsEntity.getProductLength();
        //产品宽
        Double productWide = productsEntity.getProductWide();
        //产品高
        Double productHeight = productsEntity.getProductHeight();
        //根据长宽高计算出来重量
        Double weight = calculatedWeight(productLength, productWide, productHeight);
        if (productWeight != 0 || weight != 0) {
            //体积重量大于实际重量的必须按体积重量计收资费
            //计算出各国运费
            if (productWeight > weight) {
                americanFC = calculateFreight(productWeight, americanFC, US);
                BigDecimal  americanPrice = americanFC.getPrice();
                canadaFC=calculateFreight(productWeight, canadaFC, CA);
                BigDecimal   canadaPrice = canadaFC.getPrice();
                mexicoFC=calculateFreight(productWeight, mexicoFC, MX);
                BigDecimal mexicoPrice = mexicoFC.getPrice();
                britainFC=calculateFreight(productWeight, britainFC, GB);
                BigDecimal britainPrice = britainFC.getPrice();
                franceFC=calculateFreight(productWeight, franceFC, FR);
                BigDecimal francePrice = franceFC.getPrice();
                germanyFC=calculateFreight(productWeight, germanyFC, DE);
                BigDecimal germanyPrice = germanyFC.getPrice();
                italyFC=calculateFreight(productWeight, italyFC, IT);
                BigDecimal italyPrice = italyFC.getPrice();
                spainFC=calculateFreight(productWeight, spainFC, ES);
                BigDecimal spainPrice = spainFC.getPrice();
                japanFC=calculateFreight(productWeight, japanFC, JP);
                BigDecimal japanPrice = japanFC.getPrice();
                australiaFC=calculateFreight(productWeight, australiaFC, AU);
                BigDecimal australiaPrice = australiaFC.getPrice();

            }else {
                //计算出各国运费
                americanFC= calculateFreight(weight, americanFC, US);
                BigDecimal  americanPrice = americanFC.getPrice();
                canadaFC=calculateFreight(weight, canadaFC, CA);
                BigDecimal   canadaPrice = canadaFC.getPrice();
                mexicoFC=calculateFreight(weight, mexicoFC, MX);
                BigDecimal mexicoPrice = mexicoFC.getPrice();
                britainFC=calculateFreight(weight, britainFC, GB);
                BigDecimal britainPrice = britainFC.getPrice();
                franceFC=calculateFreight(weight, franceFC, FR);
                BigDecimal francePrice = franceFC.getPrice();
                germanyFC=calculateFreight(weight, germanyFC, DE);
                BigDecimal germanyPrice = germanyFC.getPrice();
                italyFC=calculateFreight(weight, italyFC, IT);
                BigDecimal italyPrice = italyFC.getPrice();
                spainFC=calculateFreight(weight, spainFC, ES);
                BigDecimal spainPrice = spainFC.getPrice();
                japanFC=calculateFreight(weight, japanFC, JP);
                BigDecimal japanPrice = japanFC.getPrice();
                australiaFC=calculateFreight(weight, australiaFC, AU);
                BigDecimal australiaPrice = australiaFC.getPrice();
            }
        } else {
            return R.ok().put("code", 1);
        }
        return R.ok();
    }

    //根据产品长宽高计算产品重量
    public  Double calculatedWeight(Double productLength, Double productWide, Double productHeight) {
        //体积重量按每6000立方厘米折合1公斤计算,体积重量的计算公式为:体积重量=(长X宽X高)/6000
        Double weight = (productHeight * productLength * productWide) / 6000;
        return weight;
    }

    //计算国际运费
    public  FreightCostEntity calculateFreight(Double weight, FreightCostEntity freightCostEntity, String countryCode) {
        if (weight < 2) {
            //根据国家编号查找运费价格进行运费计算
            FreightPriceEntity freightPriceEntity = freightPriceService.selectOne(new EntityWrapper<FreightPriceEntity>().eq("type", "小包").eq("country_code", countryCode));
            BigDecimal bigDecimal =new BigDecimal(weight);
            //计算出运费价格
            BigDecimal freightPrice = bigDecimal.multiply(freightPriceEntity.getPrice());
            freightCostEntity.setFreight(freightPrice);
            freightCostEntity.setType("小包");
            return freightCostEntity;
        }else{
            //根据国家编号查找运费价格进行运费计算
            FreightPriceEntity freightPriceEntity = freightPriceService.selectOne(new EntityWrapper<FreightPriceEntity>().eq("type","大包").eq("country_code",countryCode));
            BigDecimal bigDecimal =new BigDecimal(weight);
            //计算出运费价格
            BigDecimal freightPrice = bigDecimal.multiply(freightPriceEntity.getPrice());
            freightCostEntity.setFreight(freightPrice);
            freightCostEntity.setType("大包");
            return freightCostEntity;
        }
    }
}

