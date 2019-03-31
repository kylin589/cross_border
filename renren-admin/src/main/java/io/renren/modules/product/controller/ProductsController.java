package io.renren.modules.product.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.swjtu.lang.LANG;
import com.swjtu.querier.Querier;
import com.swjtu.trans.AbstractTranslator;
import com.swjtu.trans.impl.GoogleTranslator;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;

import io.renren.modules.product.dto.BatchModifyDto;
import io.renren.modules.product.dto.BatchTranslateDto;
import io.renren.modules.product.entity.*;
import io.renren.modules.product.service.*;
import io.renren.modules.product.vm.ChangeAuditStatusVM;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysDeptService;
import io.renren.modules.util.TranslateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
    @Autowired
    private AmazonRateService amazonRateService;
    @Autowired
    private ImageAddressService imageAddressService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private EanUpcService eanUpcService;

    private static final String US = "US";//美国
    private static final String CA = "CA";//加拿大
    private static final String MX = "MX";// 墨西哥
    private static final String GB = "GB";// 英国
    private static final String FR = "FR";// 法国
    private static final String DE = "DE";// 德国
    private static final String IT = "IT";// 意大利
    private static final String ES = "ES";// 西班牙
    private static final String JP = "JP"; // 日本
    private static final String AU = "AU"; // 澳大利亚
    private static final String USD = "USD";//美元
    private static final String CAD = "CAD";//加拿大元
    private static final String MXN = "MXN";// 墨西哥比索
    private static final String GBP = "GBP";// 英镑
    private static final String EUR = "EUR";// 欧元
    private static final String JPY = "JPY";// 日元
    private static final String AUD = "AUD";//澳大利亚元


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
        return R.ok().put("page", map.get("page")).put("proNum", map.get("proCount")).put("via", map.get("approvedCount")).put("variant", map.get("numberOfVariants")).put("allVariant", map.get("variantsCount")).put("userId",getUserId());
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
     * @methodname: getClaimList 所有认领产品列表
     * @param: [params]
     * @return: io.renren.common.utils.R
     * page 产品page
     * 根据用户id查询没有被删除的产品，按时间降序排列
     * @auther: jhy
     * @date: 2018/11/29 19:32
     */
    @RequestMapping("/getClaimList")
    public R getClaimList(@RequestParam Map<String, Object> params) {
        //公司所有产品列表(认领产品不在其中)
        PageUtils page = productsService.queryClaimPage(params, getDeptId());
        return R.ok().put("page", page);
    }

    /**
     * @return R
     * @methodname: 更改产品的审核、上架、产品状态
     * @auther zjr
     * @date 2018-11-7 9:54
     */
    @RequestMapping("/changeauditstatus")
    public R changeAuditStatus(@RequestBody ChangeAuditStatusVM changeAuditStatusVM) {
        Long[] productIds = changeAuditStatusVM.getProductIds();
        String number = changeAuditStatusVM.getNumber();
        String type = changeAuditStatusVM.getType();
        System.out.println("productIds:" + productIds[0]);
        System.out.println("number:" + number);
        System.out.println("type:" + type);
        for (int i = 0; i < productIds.length; i++) {
            ProductsEntity entity = productsService.selectById(productIds[i]);
            if ("AUDIT_STATE".equals(type)) {
                entity.setAuditStatus(number);
                //如果是审核通过
                if(number.equals("001")){
                    //设置主产品ean码
                    EanUpcEntity mainEanUpcEntity = eanUpcService.selectOne(new EntityWrapper<EanUpcEntity>().eq("state",0));
                    mainEanUpcEntity.setState(1);
                    eanUpcService.updateById(mainEanUpcEntity);
                    entity.setEanCode(mainEanUpcEntity.getCode());
                    //如果有变体，设置变体ean码
                    List<VariantsInfoEntity> variantsInfosList = variantsInfoService.selectList(new EntityWrapper<VariantsInfoEntity>().eq("product_id",entity.getProductId()));
                    if (variantsInfosList != null && variantsInfosList.size() != 0) {
                        int size = variantsInfosList.size();
                        EanUpcEntity eanUpcEntity = new EanUpcEntity();
                        eanUpcEntity.setState(0);
                        eanUpcEntity.setType("EAN");
                        eanUpcEntity.setSize(size);
                        //查出未使用的EAN码，修改为使用
                        List<EanUpcEntity> eanUpcEntities = eanUpcService.selectByLimit(eanUpcEntity);
                        if (eanUpcEntities != null && eanUpcEntities.size() > 0 && eanUpcEntities.size() == variantsInfosList.size()) {
                            for (int j = 0; j < eanUpcEntities.size(); j++) {
                                eanUpcEntities.get(j).setState(1);
                                eanUpcEntities.get(j).setProductId(entity.getProductId());
                                eanUpcService.updateById(eanUpcEntities.get(j));
                            }
                        } else {
                            return R.error("EAN码数量不足，尽快添加");
                        }
                        for (int j = 0; j < variantsInfosList.size(); j++) {
                            String code = eanUpcEntities.get(j).getCode();
                            VariantsInfoEntity variantsInfoEntity = variantsInfosList.get(j);
                            variantsInfoEntity.setEanCode(code);
                        }
                        variantsInfoService.updateBatchById(variantsInfosList);
                    }
                }
            }
            if ("SHELVE_STATE".equals(type)) {
                entity.setShelveStatus(number);
            }
            if ("PRODUCT_TYPE".equals(type)) {
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
            ProductsEntity entity = productsService.selectById(productIds[i]);
            entity.setIsDeleted(1);
            entity.setLastOperationTime(new Date());
            entity.setLastOperationUserId(getUserId());
            productsService.updateById(entity);
        }
        return R.ok();
    }

    /**
     * @return R里包括SKU结果。
     * @methodname 修正SKU
     * @auther zjr
     * @date 2018-11-7 11:23
     */
    @RequestMapping("/modifySKU")
    public R modifySKU(Long productId) {
        ProductsEntity productsEntity = productsService.selectById(productId);
        String productSku = productsEntity.getProductSku();
        Long deptId = getDeptId();
        SysDeptEntity sysDeptEntity = sysDeptService.selectById(deptId);
        String companySku = sysDeptEntity.getCompanySku();
        String SKU = companySku + "-" + productSku;
        productsEntity.setProductSku(SKU);
        productsService.updateById(productsEntity);
        return R.ok().put("SKU", SKU);
    }

    /**
     * @return R里包括productId。
     * @methodname 获取新建产品的id，原创第一步
     * @auther zjr
     * @date 2018-11-10 10:23
     */
    @RequestMapping("/getproductid")
    public R getProductId() {
        ProductsEntity productsEntity = productsService.getNewProductId(getUserId());
        productsEntity.setCreateUserId(getUserId());
        productsEntity.setDeptId(getDeptId());
        productsEntity.setLastOperationUserId(getUserId());
        productsEntity.setLastOperationTime(new Date());
        //生成SKU
        SysUserEntity user = getUser();
        String SKU = user.getEnName() + "-" + user.getEnBrand() + "-" + productsEntity.getProductId();
        productsEntity.setProductSku(SKU);
        //生成库存
        int IStock = (int) (200 + Math.random() * (300 - 200 + 1));
        productsEntity.setStock(new BigDecimal(IStock));
        // 生成国内运费，15
        productsEntity.setDomesticFreight(new BigDecimal(15));
        // 生成产品标题
        productsEntity.setProductTitle("产品标题");
        productsEntity.setProducerName(user.getEnName());
        productsEntity.setBrandName(user.getEnBrand());
        //设置预处理时间
        productsEntity.setPretreatmentDate(1);
        productsEntity.setAuditStatus("002");
        productsEntity.setShelveStatus("001");
        productsEntity.setProductType("002");
        //获取Ean码
        /*EanUpcEntity eanUpcEntity = eanUpcService.selectOne(new EntityWrapper<EanUpcEntity>().eq("type", "EAN").eq("state", 0).orderBy(true, "state", true));
        if (eanUpcEntity != null) {
            String code = eanUpcEntity.getCode();
            //设置Ean码
            productsEntity.setEanCode(code);
            //修改状态
            eanUpcEntity.setState(1);
            //关联产品id
            eanUpcEntity.setProductId(productsEntity.getProductId());
            eanUpcService.updateById(eanUpcEntity);
        }*/
        productsService.updateById(productsEntity);
        return R.ok().put("productsEntity", productsEntity);
    }

    /**
     * @return R.ok()
     * @methodname 更新
     * @auther zjr
     * @date 2018-11-10 10:23
     */
    @RequestMapping("/update")
    public R update(@RequestBody ProductsEntity products) {
        //ValidatorUtils.validateEntity((products);
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
        } else {
            FreightCostEntity americanFC = new FreightCostEntity();
            productsEntity.setAmericanFC(americanFC);
        }
        Long canadaid = productsEntity.getCanadaFreight();
        if (canadaid != null) {
            FreightCostEntity canadaFC = freightCostService.selectById(canadaid);
            productsEntity.setCanadaFC(canadaFC);
        } else {
            FreightCostEntity canadaFC = new FreightCostEntity();
            productsEntity.setCanadaFC(canadaFC);
        }
        Long mexicoid = productsEntity.getMexicoFreight();
        if (mexicoid != null) {
            FreightCostEntity mexicoFC = freightCostService.selectById(mexicoid);
            productsEntity.setMexicoFC(mexicoFC);
        } else {
            FreightCostEntity mexicoFC = new FreightCostEntity();
            productsEntity.setMexicoFC(mexicoFC);
        }
        Long britainid = productsEntity.getBritainFreight();
        if (britainid != null) {
            FreightCostEntity britainFC = freightCostService.selectById(britainid);
            productsEntity.setBritainFC(britainFC);
        } else {
            FreightCostEntity britainFC = new FreightCostEntity();
            productsEntity.setBritainFC(britainFC);
        }
        Long franceid = productsEntity.getFranceFreight();
        if (franceid != null) {
            FreightCostEntity franceFC = freightCostService.selectById(franceid);
            productsEntity.setFranceFC(franceFC);
        } else {
            FreightCostEntity franceFC = new FreightCostEntity();
            productsEntity.setFranceFC(franceFC);
        }
        Long germanyid = productsEntity.getGermanyFreight();
        if (germanyid != null) {
            FreightCostEntity germanyFC = freightCostService.selectById(germanyid);
            productsEntity.setGermanyFC(germanyFC);
        } else {
            FreightCostEntity germanyFC = new FreightCostEntity();
            productsEntity.setGermanyFC(germanyFC);
        }
        Long italyid = productsEntity.getItalyFreight();
        if (italyid != null) {
            FreightCostEntity italyFC = freightCostService.selectById(italyid);
            productsEntity.setItalyFC(italyFC);
        } else {
            FreightCostEntity italyFC = new FreightCostEntity();
            productsEntity.setItalyFC(italyFC);
        }
        Long spainid = productsEntity.getSpainFreight();
        if (spainid != null) {
            FreightCostEntity spainFC = freightCostService.selectById(spainid);
            productsEntity.setSpainFC(spainFC);
        } else {
            FreightCostEntity spainFC = new FreightCostEntity();
            productsEntity.setSpainFC(spainFC);
        }
        Long japanid = productsEntity.getJapanFreight();
        if (japanid != null) {
            FreightCostEntity japanFC = freightCostService.selectById(japanid);
            productsEntity.setJapanFC(japanFC);
        } else {
            FreightCostEntity japanFC = new FreightCostEntity();
            productsEntity.setJapanFC(japanFC);
        }
        Long australiaid = productsEntity.getAustraliaFreight();
        if (australiaid != null) {
            FreightCostEntity australiaFC = freightCostService.selectById(australiaid);
            productsEntity.setAustraliaFC(australiaFC);
        } else {
            FreightCostEntity australiaFC = new FreightCostEntity();
            productsEntity.setAustraliaFC(australiaFC);
        }
        //各个国家的介绍
        Long chineseinid = productsEntity.getChineseIntroduction();
        if (chineseinid != null) {
            IntroductionEntity chinesePRE = introductionService.selectById(chineseinid);
            productsEntity.setChinesePRE(chinesePRE);
        } else {
            IntroductionEntity chinesePRE = new IntroductionEntity();
            productsEntity.setChinesePRE(chinesePRE);
        }
        Long britaininid = productsEntity.getBritainIntroduction();
        if (britaininid != null) {
            IntroductionEntity britainPRE = introductionService.selectById(britaininid);
            productsEntity.setBritainPRE(britainPRE);
        } else {
            IntroductionEntity britainPRE = new IntroductionEntity();
            productsEntity.setBritainPRE(britainPRE);
        }
        Long franceinid = productsEntity.getFranceIntroduction();
        if (franceinid != null) {
            IntroductionEntity francePRE = introductionService.selectById(franceinid);
            productsEntity.setFrancePRE(francePRE);
        } else {
            IntroductionEntity francePRE = new IntroductionEntity();
            productsEntity.setFrancePRE(francePRE);
        }
        Long germanyInid = productsEntity.getGermanyIntroduction();
        if (germanyInid != null) {
            IntroductionEntity germanyPRE = introductionService.selectById(germanyInid);
            productsEntity.setGermanyPRE(germanyPRE);
        } else {
            IntroductionEntity germanyPRE = new IntroductionEntity();
            productsEntity.setGermanyPRE(germanyPRE);
        }
        Long italyInid = productsEntity.getItalyIntroduction();
        if (italyInid != null) {
            IntroductionEntity italyPRE = introductionService.selectById(italyInid);
            productsEntity.setItalyPRE(italyPRE);
        } else {
            IntroductionEntity italyPRE = new IntroductionEntity();
            productsEntity.setItalyPRE(italyPRE);
        }
        Long spainInid = productsEntity.getSpainIntroduction();
        if (spainInid != null) {
            IntroductionEntity spainPRE = introductionService.selectById(spainInid);
            productsEntity.setSpainPRE(spainPRE);
        } else {
            IntroductionEntity spainPRE = new IntroductionEntity();
            productsEntity.setSpainPRE(spainPRE);
        }
        Long japanInid = productsEntity.getJapanIntroduction();
        if (japanInid != null) {
            IntroductionEntity japanPRE = introductionService.selectById(japanInid);
            productsEntity.setJapanPRE(japanPRE);
        } else {
            IntroductionEntity japanPRE = new IntroductionEntity();
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
        variantsInfos = variantsInfoService.selectList(new EntityWrapper<VariantsInfoEntity>().eq("product_id", productId).orderBy("variant_id"));
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
    public R batchModify(@RequestBody BatchModifyDto batchModifyDto) {
        //翻译标识
        int[] translates = batchModifyDto.getTranslates();
        //英文追加模型
        BatchTranslateDto enTranslateDto = new BatchTranslateDto();
        enTranslateDto.setCountryCode("EN");
        //法语追加模型
        BatchTranslateDto fraTranslateDto = new BatchTranslateDto();
        fraTranslateDto.setCountryCode("FRA");
        //德语追加模型
        BatchTranslateDto deTranslateDto = new BatchTranslateDto();
        deTranslateDto.setCountryCode("DE");
        //意大利语追加模型
        BatchTranslateDto itTranslateDto = new BatchTranslateDto();
        itTranslateDto.setCountryCode("IT");
        //西班牙语追加模型
        BatchTranslateDto spaTranslateDto = new BatchTranslateDto();
        spaTranslateDto.setCountryCode("SPA");
        //日语追加模型
        BatchTranslateDto jpTranslateDto = new BatchTranslateDto();
        jpTranslateDto.setCountryCode("JP");
        if (translates != null && translates.length > 0) {
            Querier<AbstractTranslator> querierTrans = new Querier<>();
            querierTrans.attach(new GoogleTranslator());
            //标题前加
            String productTitleQ = batchModifyDto.getProductTitleQ().trim();
            //标题后加
            String productTitleH = batchModifyDto.getProductTitleH().trim();
            //关键字
            String keyWord = batchModifyDto.getKeyWord().trim();
            //要点说明
            String keyPoints = batchModifyDto.getKeyPoints().trim();
            //描述前加
            String productDescriptionQ = batchModifyDto.getProductDescriptionQ().trim();
            //描述后加
            String productDescriptionH = batchModifyDto.getProductDescriptionH().trim();
            //英文追加模型
            enTranslateDto.setProductTitleQ(TranslateUtils.toUpperCase(productTitleQ));
            enTranslateDto.setProductTitleH(TranslateUtils.toUpperCase(productTitleH));
            enTranslateDto.setKeyWord(TranslateUtils.toUpperCase(keyWord));
            enTranslateDto.setKeyPoints(keyPoints);
            enTranslateDto.setProductDescriptionQ(productDescriptionQ);
            enTranslateDto.setProductDescriptionH(productDescriptionH);
            if(StringUtils.isNotBlank(productTitleQ)){
                for (int q = 0; q < translates.length; q++) {
                    switch (q) {
                        case 1:
                            querierTrans.setParams(LANG.EN, LANG.FRA, productTitleQ);
                            //翻译
                            List<String> resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0 && StringUtils.isNotBlank(resultList.get(0))) {
                                String title = TranslateUtils.toUpperCase(resultList.get(0));
                                fraTranslateDto.setProductTitleQ(title);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 2:
                            querierTrans.setParams(LANG.EN, LANG.IT, productTitleQ);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String title = TranslateUtils.toUpperCase(resultList.get(0));
                                itTranslateDto.setProductTitleQ(title);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 3:
                            querierTrans.setParams(LANG.EN, LANG.SPA, productTitleQ);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String title = TranslateUtils.toUpperCase(resultList.get(0));
                                spaTranslateDto.setProductTitleQ(title);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 4:
                            querierTrans.setParams(LANG.EN, LANG.DE, productTitleQ);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String title = TranslateUtils.toUpperCase(resultList.get(0));
                                deTranslateDto.setProductTitleQ(title);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 5:
                            querierTrans.setParams(LANG.EN, LANG.JP, productTitleQ);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String title = TranslateUtils.toUpperCase(resultList.get(0));
                                jpTranslateDto.setProductTitleQ(title);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            if(StringUtils.isNotBlank(productTitleH)){
                for (int q = 0; q < translates.length; q++) {
                    switch (q) {
                        case 1:
                            querierTrans.setParams(LANG.EN, LANG.FRA, productTitleH);
                            //翻译
                            List<String> resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String title = TranslateUtils.toUpperCase(resultList.get(0));
                                fraTranslateDto.setProductTitleH(title);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 2:
                            querierTrans.setParams(LANG.EN, LANG.IT, productTitleH);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String title = TranslateUtils.toUpperCase(resultList.get(0));
                                itTranslateDto.setProductTitleH(title);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 3:
                            querierTrans.setParams(LANG.EN, LANG.SPA, productTitleH);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String title = TranslateUtils.toUpperCase(resultList.get(0));
                                spaTranslateDto.setProductTitleH(title);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 4:
                            querierTrans.setParams(LANG.EN, LANG.DE, productTitleH);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String title = TranslateUtils.toUpperCase(resultList.get(0));
                                deTranslateDto.setProductTitleH(title);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 5:
                            querierTrans.setParams(LANG.EN, LANG.JP, productTitleH);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String title = TranslateUtils.toUpperCase(resultList.get(0));
                                jpTranslateDto.setProductTitleH(title);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            if(StringUtils.isNotBlank(keyWord)){
                for (int q = 0; q < translates.length; q++) {
                    switch (q) {
                        case 1:
                            querierTrans.setParams(LANG.EN, LANG.FRA, keyWord);
                            //翻译
                            List<String> resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String keyword = TranslateUtils.toUpperCase(resultList.get(0));
                                fraTranslateDto.setKeyWord(keyword);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 2:
                            querierTrans.setParams(LANG.EN, LANG.IT, keyWord);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String keyword = TranslateUtils.toUpperCase(resultList.get(0));
                                itTranslateDto.setKeyWord(keyword);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 3:
                            querierTrans.setParams(LANG.EN, LANG.SPA, keyWord);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String keyword = TranslateUtils.toUpperCase(resultList.get(0));
                                spaTranslateDto.setKeyWord(keyword);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 4:
                            querierTrans.setParams(LANG.EN, LANG.DE, keyWord);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String keyword = TranslateUtils.toUpperCase(resultList.get(0));
                                deTranslateDto.setKeyWord(keyword);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 5:
                            querierTrans.setParams(LANG.EN, LANG.JP, keyWord);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String keyword = TranslateUtils.toUpperCase(resultList.get(0));
                                jpTranslateDto.setKeyWord(keyword);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            if(StringUtils.isNotBlank(keyPoints)){
                String translateStr = keyPoints.replace("\r\n"," !! ").replace("\r"," !! ").replace("\n"," !! ");
                for (int q = 0; q < translates.length; q++) {
                    switch (q) {
                        case 1:
                            querierTrans.setParams(LANG.EN, LANG.FRA, translateStr);
                            //翻译
                            List<String> resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String point = resultList.get(0).replace(" !! ","\n").replace("!! ","\n").replace("!!","\n").replace(" ! ","\n").replace("! ","\n").replace("!","\n");
                                fraTranslateDto.setKeyPoints(point);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 2:
                            querierTrans.setParams(LANG.EN, LANG.IT, translateStr);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String point = resultList.get(0).replace(" !! ","\n").replace("!! ","\n").replace("!!","\n").replace(" ! ","\n").replace("! ","\n").replace("!","\n");
                                itTranslateDto.setKeyPoints(point);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 3:
                            querierTrans.setParams(LANG.EN, LANG.SPA, translateStr);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String point = resultList.get(0).replace(" !! ","\n").replace("!! ","\n").replace("!!","\n").replace(" ! ","\n").replace("! ","\n").replace("!","\n");
                                spaTranslateDto.setKeyPoints(point);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 4:
                            querierTrans.setParams(LANG.EN, LANG.DE, translateStr);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String point = resultList.get(0).replace(" !! ","\n").replace("!! ","\n").replace("!!","\n").replace(" ! ","\n").replace("! ","\n").replace("!","\n");
                                deTranslateDto.setKeyPoints(point);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 5:
                            querierTrans.setParams(LANG.EN, LANG.JP, translateStr);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String point = resultList.get(0).replace(" !! ","\n").replace("!! ","\n").replace("!!","\n").replace(" ! ","\n").replace("! ","\n").replace("!","\n");
                                jpTranslateDto.setKeyPoints(point);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            if(StringUtils.isNotBlank(productDescriptionQ)){
                String translateStr = productDescriptionQ.replace("\r\n"," !! ").replace("\r"," !! ").replace("\n"," !! ");
                for (int q = 0; q < translates.length; q++) {
                    switch (q) {
                        case 1:
                            querierTrans.setParams(LANG.EN, LANG.FRA, translateStr);
                            //翻译
                            List<String> resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String description = resultList.get(0).replace(" !! ","\n").replace("!! ","\n").replace("!!","\n").replace(" ! ","\n").replace("! ","\n").replace("!","\n");
                                fraTranslateDto.setProductDescriptionQ(description);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 2:
                            querierTrans.setParams(LANG.EN, LANG.IT, translateStr);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String description = resultList.get(0).replace(" !! ","\n").replace("!! ","\n").replace("!!","\n").replace(" ! ","\n").replace("! ","\n").replace("!","\n");
                                itTranslateDto.setProductDescriptionQ(description);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 3:
                            querierTrans.setParams(LANG.EN, LANG.SPA, translateStr);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String description = resultList.get(0).replace(" !! ","\n").replace("!! ","\n").replace("!!","\n").replace(" ! ","\n").replace("! ","\n").replace("!","\n");
                                spaTranslateDto.setProductDescriptionQ(description);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 4:
                            querierTrans.setParams(LANG.EN, LANG.DE, translateStr);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String description = resultList.get(0).replace(" !! ","\n").replace("!! ","\n").replace("!!","\n").replace(" ! ","\n").replace("! ","\n").replace("!","\n");
                                deTranslateDto.setProductDescriptionQ(description);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 5:
                            querierTrans.setParams(LANG.EN, LANG.JP, translateStr);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String description = resultList.get(0).replace(" !! ","\n").replace("!! ","\n").replace("!!","\n").replace(" ! ","\n").replace("! ","\n").replace("!","\n");
                                jpTranslateDto.setProductDescriptionQ(description);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            if(StringUtils.isNotBlank(productDescriptionH)){
                String translateStr = productDescriptionH.replace("\r\n"," !! ").replace("\r"," !! ").replace("\n"," !! ");
                for (int q = 0; q < translates.length; q++) {
                    switch (q) {
                        case 1:
                            querierTrans.setParams(LANG.EN, LANG.FRA, translateStr);
                            //翻译
                            List<String> resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String description = resultList.get(0).replace(" !! ","\n").replace("!! ","\n").replace("!!","\n").replace(" ! ","\n").replace("! ","\n").replace("!","\n");
                                fraTranslateDto.setProductDescriptionH(description);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 2:
                            querierTrans.setParams(LANG.EN, LANG.IT, translateStr);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String description = resultList.get(0).replace(" !! ","\n").replace("!! ","\n").replace("!!","\n").replace(" ! ","\n").replace("! ","\n").replace("!","\n");
                                itTranslateDto.setProductDescriptionH(description);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 3:
                            querierTrans.setParams(LANG.EN, LANG.SPA, translateStr);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String description = resultList.get(0).replace(" !! ","\n").replace("!! ","\n").replace("!!","\n").replace(" ! ","\n").replace("! ","\n").replace("!","\n");
                                spaTranslateDto.setProductDescriptionH(description);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 4:
                            querierTrans.setParams(LANG.EN, LANG.DE, translateStr);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String description = resultList.get(0).replace(" !! ","\n").replace("!! ","\n").replace("!!","\n").replace(" ! ","\n").replace("! ","\n").replace("!","\n");
                                deTranslateDto.setProductDescriptionH(description);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        case 5:
                            querierTrans.setParams(LANG.EN, LANG.JP, translateStr);
                            //翻译
                            resultList = querierTrans.execute();
                            if (resultList != null && resultList.size() > 0) {
                                String description = resultList.get(0).replace(" !! ","\n").replace("!! ","\n").replace("!!","\n").replace(" ! ","\n").replace("! ","\n").replace("!","\n");
                                jpTranslateDto.setProductDescriptionH(description);
                            } else {
                                return R.error("翻译失败，请重新尝试");
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        Long[] productIds = batchModifyDto.getProductIds();
        //循环遍历出前台传入的产品id
        System.out.println(batchModifyDto);
        for (int i = 0; i < productIds.length; i++) {
            ProductsEntity productsEntity = productsService.selectById(productIds[i]);
            productsEntity.setAuditStatus(batchModifyDto.getAuditStatus());
            productsEntity.setShelveStatus(batchModifyDto.getShelveStatus());
            productsEntity.setProductType(batchModifyDto.getProductType());
            //通过前台传入的三级分类id获取一个一二三级的字符串id，以逗号拼接的
            Long categoryThreeId = batchModifyDto.getCategoryThreeId();
            if (categoryThreeId != null) {
                String ids = categoryService.queryParentByChildIdAndCategory(categoryThreeId, productsEntity);
                String[] id = ids.split(",");
                productsEntity.setCategoryOneId(Long.parseLong(id[0]));
                productsEntity.setCategoryTwoId(Long.parseLong(id[1]));
                productsEntity.setCategoryThreeId(Long.parseLong(id[2]));
            }
            productsEntity.setProducerName(batchModifyDto.getProducerName());
            productsEntity.setManufacturerNumber(batchModifyDto.getManufacturerNumber());
            productsEntity.setBrandName(batchModifyDto.getBrandName());
            productsEntity.setProductLength(batchModifyDto.getProductLength());
            productsEntity.setProductWide(batchModifyDto.getProductWide());
            productsEntity.setProductHeight(batchModifyDto.getProductHeight());

            //翻译标识
            if (translates != null && translates.length > 0) {
                for (int q = 0; q < translates.length; q++) {
                    switch (q) {
                        case 0:
                            IntroductionEntity enIntroduction = new IntroductionEntity();
                            if(productsEntity.getBritainIntroduction() != null){
                                enIntroduction = introductionService.selectById(productsEntity.getBritainIntroduction());
                            }else{
                                enIntroduction = new IntroductionEntity();
                            }
                            enIntroduction.setCountry("EN");
                            enIntroduction = updateProductTranslate(enTranslateDto, batchModifyDto.getKeywordsadd(), batchModifyDto.getKeyPointsadd(), enIntroduction);
                            introductionService.insertOrUpdate(enIntroduction);
                            productsEntity.setBritainIntroduction(enIntroduction.getIntroductionId());
                            break;
                        case 1:
                            IntroductionEntity fraIntroduction = new IntroductionEntity();
                            if(productsEntity.getFranceIntroduction() != null){
                                fraIntroduction = introductionService.selectById(productsEntity.getFranceIntroduction());
                            }else{
                                fraIntroduction = new IntroductionEntity();
                            }
                            fraIntroduction.setCountry("FRA");
                            fraIntroduction = updateProductTranslate(fraTranslateDto, batchModifyDto.getKeywordsadd(), batchModifyDto.getKeyPointsadd(), fraIntroduction);
                            introductionService.insertOrUpdate(fraIntroduction);
                            productsEntity.setFranceIntroduction(fraIntroduction.getIntroductionId());
                            break;
                        case 2:
                            IntroductionEntity itIntroduction = new IntroductionEntity();
                            if(productsEntity.getItalyIntroduction() != null){
                                itIntroduction = introductionService.selectById(productsEntity.getItalyIntroduction());
                            }else{
                                itIntroduction = new IntroductionEntity();
                            }
                            itIntroduction.setCountry("IT");
                            itIntroduction = updateProductTranslate(itTranslateDto, batchModifyDto.getKeywordsadd(), batchModifyDto.getKeyPointsadd(), itIntroduction);
                            introductionService.insertOrUpdate(itIntroduction);
                            productsEntity.setItalyIntroduction(itIntroduction.getIntroductionId());
                            break;
                        case 3:
                            IntroductionEntity spaIntroduction = new IntroductionEntity();
                            if(productsEntity.getSpainIntroduction() != null){
                                spaIntroduction = introductionService.selectById(productsEntity.getSpainIntroduction());
                            }else{
                                spaIntroduction = new IntroductionEntity();
                            }
                            spaIntroduction.setCountry("SPA");
                            spaIntroduction = updateProductTranslate(spaTranslateDto, batchModifyDto.getKeywordsadd(), batchModifyDto.getKeyPointsadd(), spaIntroduction);
                            introductionService.insertOrUpdate(spaIntroduction);
                            productsEntity.setSpainIntroduction(spaIntroduction.getIntroductionId());
                            break;
                        case 4:
                            IntroductionEntity deIntroduction = new IntroductionEntity();
                            if(productsEntity.getGermanyIntroduction() != null){
                                deIntroduction = introductionService.selectById(productsEntity.getGermanyIntroduction());
                            }else{
                                deIntroduction = new IntroductionEntity();
                            }
                            deIntroduction.setCountry("DE");
                            deIntroduction = updateProductTranslate(deTranslateDto, batchModifyDto.getKeywordsadd(), batchModifyDto.getKeyPointsadd(), deIntroduction);
                            introductionService.insertOrUpdate(deIntroduction);
                            productsEntity.setGermanyIntroduction(deIntroduction.getIntroductionId());
                            break;
                        case 5:
                            IntroductionEntity jpIntroduction = new IntroductionEntity();
                            if(productsEntity.getJapanIntroduction() != null){
                                jpIntroduction = introductionService.selectById(productsEntity.getJapanIntroduction());
                            }else{
                                jpIntroduction = new IntroductionEntity();
                            }
                            jpIntroduction.setCountry("JP");
                            jpIntroduction = updateProductTranslate(jpTranslateDto, batchModifyDto.getKeywordsadd(), batchModifyDto.getKeyPointsadd(), jpIntroduction);
                            introductionService.insertOrUpdate(jpIntroduction);
                            productsEntity.setJapanIntroduction(jpIntroduction.getIntroductionId());
                            break;

                        default:
                            break;
                    }
                }
                String productTitle = introductionService.selectById(productsEntity.getBritainIntroduction()).getProductTitle();
                productsEntity.setProductTitle(productTitle);
            }
            productsEntity.setLastOperationTime(new Date());
            productsEntity.setLastOperationUserId(getUserId());
            //设置重量，并计算运费等信息
            if(batchModifyDto.getProductWeight() != null && batchModifyDto.getProductWeight() != 0){
                productsEntity.setProductWeight(batchModifyDto.getProductWeight());
                productsEntity = productsService.costFreightSave(productsEntity);
                productsEntity = productsService.refreshSave(productsEntity);
            }
            productsService.updateById(productsEntity);

        }
        return R.ok();
    }
    /**
     * 批量修改——语言
     */
    private IntroductionEntity updateProductTranslate(BatchTranslateDto dto, boolean keywordsadd, boolean keyPointsadd, IntroductionEntity introductionEntity) {
        String productTitleQ = dto.getProductTitleQ();
        String productTitleH = dto.getProductTitleH();
        String keyWord = dto.getKeyWord();
        String keyPoints = dto.getKeyPoints();
        String productDescriptionQ = dto.getProductDescriptionQ();
        String productDescriptionH = dto.getProductDescriptionH();
        String productTitle = introductionEntity.getProductTitle();
        if (StringUtils.isNotEmpty(productTitleQ) && StringUtils.isNotEmpty(productTitleH)) {
            //判断标题前加和后加不为空，进行标题拼接
            introductionEntity.setProductTitle(productTitleQ + " " + productTitle + " " + productTitleH);
        } else if (StringUtils.isNotEmpty(productTitleQ) && StringUtils.isEmpty(productTitleH)) {
            //判断标题前加不为空，标题后加为空。进行拼接
            introductionEntity.setProductTitle(productTitleQ.trim() + " " + productTitle);
        } else if (StringUtils.isEmpty(productTitleQ) && StringUtils.isNotEmpty(productTitleH)) {
            //判断标题前加为空，标题后加不为空。进行拼接
            introductionEntity.setProductTitle(productTitle + " " + productTitleH);
        } else {
            //标题前加和标题后加都为空，标题不变
            introductionEntity.setProductTitle(productTitle);
        }

        //获取原来的买点说明
        String keyPointsJ = introductionEntity.getKeyPoints();
        if (keyPointsadd == true) {
            //追加不为空，进行追加
            introductionEntity.setKeyPoints(keyPointsJ + keyPoints);
        } else {
            //追加为空，进行买点说明更新
            introductionEntity.setKeyPoints(keyPoints);
        }
        //获取原来的关键字
        String keyWordJ = introductionEntity.getKeyWord();
        if (keywordsadd == true) {
            //追加不为空，进行追加
            String keyWordX = keyWordJ + keyWord;
            introductionEntity.setKeyWord(keyWordX);
        } else {
            //追加为空，进行关键字更新更新
            introductionEntity.setKeyWord(keyWord);
        }
        String productDescription = introductionEntity.getProductDescription();

        if (StringUtils.isNotEmpty(productDescriptionQ) && StringUtils.isNotEmpty(productDescriptionH)) {
            //判断描述前加和后加不为空，进行描述拼接
            introductionEntity.setProductDescription(productDescriptionQ + " " + productDescription + " " + productDescriptionH);
        } else if (StringUtils.isNotEmpty(productDescriptionQ) && StringUtils.isEmpty(productDescriptionH)) {
            //判断描述前加不为空，描述后加为空。进行拼接
            introductionEntity.setProductDescription(productDescriptionQ + " " + productDescription);
        } else if (StringUtils.isEmpty(productDescriptionQ) && StringUtils.isNotEmpty(productDescriptionH)) {
            //判断描述前加不为空，描述后加为空。进行拼接
            introductionEntity.setProductDescription(productDescription + " " + productDescriptionH);
        } else {
            //无需更新
            introductionEntity.setProductDescription(productDescription);
        }
        return introductionEntity;
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
        //产品重量
        productsEntity.setProductWeight(0.00);
        //产品长度
        productsEntity.setProductLength(0.00);
        //产品宽度
        productsEntity.setProductWide(0.00);
        //产品高度
        productsEntity.setProductHeight(0.00);
        //国内运费
        productsEntity.setDomesticFreight(new BigDecimal("0.00"));
        //折扣系数
        productsEntity.setDiscount(new BigDecimal("1.00"));
        //生成SKU
        SysUserEntity user = getUser();
        String SKU = user.getEnName() + "-" + user.getEnBrand() + "-" + productsEntity.getProductId();
        productsEntity.setProductSku(SKU);
        //生成库存
        int IStock = (int) (200 + Math.random() * (300 - 200 + 1));
        productsEntity.setStock(new BigDecimal(IStock));
        // 生成国内运费，15
        productsEntity.setDomesticFreight(new BigDecimal(15));
        productsEntity.setProducerName(user.getEnName());
        productsEntity.setBrandName(user.getEnBrand());
        //设置预处理时间
        productsEntity.setPretreatmentDate(1);
        //获取码
        /*EanUpcEntity eanUpcEntity = eanUpcService.selectOne(new EntityWrapper<EanUpcEntity>().eq("type", "EAN").eq("state", 0).orderBy(true, "state", true));
        if (eanUpcEntity != null) {
            String code = eanUpcEntity.getCode();
            //设置Ean码
            productsEntity.setEanCode(code);
            //修改状态
            eanUpcEntity.setState(1);
            //关联产品id
            eanUpcEntity.setProductId(productId);
            eanUpcService.updateById(eanUpcEntity);
        }*/
        //设置分类
        /*Long categoryThreeId = productsEntity.getCategoryThreeId();
        String s = categoryService.queryParentByChildIdAndCategory(categoryThreeId, productsEntity);
        System.out.println(s);
        String[] ids = s.split(",");
        productsEntity.setCategoryOneId(Long.parseLong(ids[0]));
        productsEntity.setCategoryTwoId(Long.parseLong(ids[1]));*/
        //根据主图片的id查出主图片的url
        Long mainImageId = productsEntity.getMainImageId();
        if (mainImageId != null) {
            ImageAddressEntity imageAddressEntity = imageAddressService.selectById(mainImageId);
            String imageUrl = imageAddressEntity.getImageUrl();
            productsEntity.setMainImageUrl(imageUrl);
        }
        if (productsEntity.getBritainIntroduction() != null) {
            IntroductionEntity introductionEntity = introductionService.selectById(productsEntity.getBritainIntroduction());
            productsEntity.setProductTitle(introductionEntity.getProductTitle());
        }
        if (productsEntity.getChineseIntroduction() != null) {
            IntroductionEntity introductionEntity = introductionService.selectById(productsEntity.getChineseIntroduction());
            productsEntity.setProductTitle(introductionEntity.getProductTitle());
        }
        productsEntity.setCreateTime(new Date());
        productsEntity.setLastOperationTime(new Date());
        productsEntity.setCreateUserId(getUserId());
        productsEntity.setDeptId(getDeptId());
        productsService.updateById(productsEntity);
        return R.ok();
    }

    /**
     * @methodname: cancelOriginal 取消原创产品
     * @param: []
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/12/15 16:26
     */
    @RequestMapping("/cancelOriginal")
    public R cancelOriginal(Long productId) {
        List<ImageAddressEntity> ImageAddressEntitys = imageAddressService.selectList(new EntityWrapper<ImageAddressEntity>().eq("product_id", productId));
        if (ImageAddressEntitys != null && ImageAddressEntitys.size() != 0) {
            imageAddressService.delete(new EntityWrapper<ImageAddressEntity>().eq("product_id", productId));
        }
        productsService.deleteById(productId);
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
        if (threeId != null) {
            String idString = categoryService.queryParentByChildId(threeId);
            String[] id = idString.split(",");
            products.setCategoryOneId(Long.parseLong(id[0]));
            products.setCategoryTwoId(Long.parseLong(id[1]));
            products.setCategoryThreeId(Long.parseLong(id[2]));
        }
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
        if (StringUtils.isNotBlank(chinesePRE.getProductTitle())) {
            products.setProductTitle(chinesePRE.getProductTitle().trim());
        } else if (StringUtils.isNotBlank(britainPRE.getProductTitle())) {
            products.setProductTitle(britainPRE.getProductTitle().trim());
        } else {
            products.setProductTitle("产品标题");
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
        //获取尺寸和颜色的变体
        VariantParameterEntity sizeVP = products.getSizeVP();
        if (sizeVP != null) {
            Long paramsId = sizeVP.getParamsId();
            products.setSizeId(paramsId);
        }
        VariantParameterEntity colorVP = products.getColorVP();
        if (colorVP != null) {
            Long paramsId = colorVP.getParamsId();
            products.setColorId(paramsId);
        }
        //设置主图片，在上传图片步骤实现
        /*List<ImageAddressEntity> imageList =imageAddressService.selectList(new EntityWrapper<ImageAddressEntity>().eq("product_id",products.getProductId()).eq("is_deleted", "0").orderBy("sort",true));
        if(imageList != null && imageList.size() >0 ){
            ImageAddressEntity mainImage = imageList.get(0);
            if(mainImage != null){
                System.out.println("主图片id====" + mainImage.getImageId());
                products.setMainImageId(mainImage.getImageId());
            }
        }*/
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
        if(StringUtils.isNotBlank(products.getAuditStatus()) && "001".equals(products.getAuditStatus())){
            EanUpcEntity mainEanUpcEntity = eanUpcService.selectOne(new EntityWrapper<EanUpcEntity>().eq("state",0));
            mainEanUpcEntity.setState(1);
            eanUpcService.updateById(mainEanUpcEntity);
            products.setEanCode(mainEanUpcEntity.getCode());
        }
        //获得修正
        String correction = products.getCorrection();
        StringBuffer correctionLater = new StringBuffer();
        String productSku = products.getProductSku();
        correctionLater.append(productSku);
        //判断修正是否为空
        if (correction != "" && StringUtils.isNotBlank(correction)) {
            products.setCorrection(correction);
            correctionLater.append("-");
            correctionLater.append(correction);
        }
        String correctionLaterString = correctionLater.toString();
        //插入变体信息
        List<VariantsInfoEntity> variantsInfosList = products.getVariantsInfos();
        if (variantsInfosList != null && variantsInfosList.size() != 0) {
            int size = variantsInfosList.size();
            if(StringUtils.isNotBlank(products.getAuditStatus()) && "001".equals(products.getAuditStatus())){
                EanUpcEntity eanUpcEntity = new EanUpcEntity();
                eanUpcEntity.setState(0);
                eanUpcEntity.setType("EAN");
                eanUpcEntity.setSize(size);
                //查出未使用的EAN码，修改为使用
                List<EanUpcEntity> eanUpcEntities = eanUpcService.selectByLimit(eanUpcEntity);
                if (eanUpcEntities != null && eanUpcEntities.size() != 0 && eanUpcEntities.size() == variantsInfosList.size()) {
                    for (int i = 0; i < eanUpcEntities.size(); i++) {
                        eanUpcEntities.get(i).setState(1);
                        eanUpcEntities.get(i).setProductId(products.getProductId());
                    }
                    eanUpcService.updateBatchById(eanUpcEntities);
                } else {
                    return R.error("EAN码数量不足，尽快添加");
                }

                for (int i = 0; i < variantsInfosList.size(); i++) {
                    String code = eanUpcEntities.get(i).getCode();
                    VariantsInfoEntity variantsInfoEntity = variantsInfosList.get(i);
                    variantsInfoEntity.setEanCode(code);
                    variantsInfoEntity.setVariantSku(correctionLaterString + "-" + code);
                    variantsInfoEntity.setProductId(products.getProductId());
                    variantsInfoEntity.setUserId(getUserId());
                    variantsInfoEntity.setDeptId(getDeptId());
                }
                variantsInfoService.insertBatch(variantsInfosList);
            }else{
                for (int i = 0; i < variantsInfosList.size(); i++) {
                    VariantsInfoEntity variantsInfoEntity = variantsInfosList.get(i);
                    variantsInfoEntity.setVariantSku(correctionLaterString + "-" + i);
                    variantsInfoEntity.setProductId(products.getProductId());
                    variantsInfoEntity.setUserId(getUserId());
                    variantsInfoEntity.setDeptId(getDeptId());
                }
                variantsInfoService.insertBatch(variantsInfosList);
            }


        }

        //根据产品id进行更新
        products.setProductSku(correctionLaterString);
        products.setCorrection(null);
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
        if (threeId != null) {
            String idString = categoryService.queryParentByChildId(threeId);
            String[] id = idString.split(",");
            products.setCategoryOneId(Long.parseLong(id[0]));
            products.setCategoryTwoId(Long.parseLong(id[1]));
            products.setCategoryThreeId(Long.parseLong(id[2]));
        }

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
            System.out.println("英文描述：" + britainPRE.getProductDescription());
            if (britainPRE.getIntroductionId() != null) {
                introductionService.updateById(britainPRE);
            } else {
                introductionService.insert(britainPRE);
                products.setBritainIntroduction(britainPRE.getIntroductionId());
            }
        }
        //产品标题
        if (StringUtils.isNotBlank(britainPRE.getProductTitle())) {
            products.setProductTitle(britainPRE.getProductTitle().trim());
        } else if (StringUtils.isNotBlank(chinesePRE.getProductTitle())) {
            products.setProductTitle(chinesePRE.getProductTitle().trim());
        } else {
            products.setProductTitle("产品标题");
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
        //设置主图片
        ImageAddressEntity mainImage = imageAddressService.selectOne(new EntityWrapper<ImageAddressEntity>().eq("sort",0).eq("is_deleted",0).eq("product_id",products.getProductId()));
        if(mainImage != null){
            products.setMainImageId(mainImage.getImageId());
            products.setMainImageUrl(mainImage.getImageUrl());
        }
        //最后的操作时间
        products.setLastOperationTime(new Date());
        //获取最后操作用户id
        products.setLastOperationUserId(this.getUserId());
        Long productId = products.getProductId();
        //获取尺寸和颜色的变体
        VariantParameterEntity sizeVP = products.getSizeVP();
        if (sizeVP != null) {
            Long paramsId = sizeVP.getParamsId();
            products.setSizeId(paramsId);
        }
        VariantParameterEntity colorVP = products.getColorVP();
        if (colorVP != null) {
            Long paramsId = colorVP.getParamsId();
            products.setColorId(paramsId);
        }
        //批量删除变体信息
//        variantsInfoService.delete(new EntityWrapper<VariantsInfoEntity>().eq("product_id", productId));
        //获得修正
        String correction = products.getCorrection();
        StringBuffer correctionLater = new StringBuffer();
        String productSku = products.getProductSku();
        correctionLater.append(productSku);
        List<VariantsInfoEntity> variantsInfosList = products.getVariantsInfos();
        //判断修正是否为空，不为空修改所有ean码
        if (StringUtils.isNotBlank(correction)) {
            products.setCorrection(correction);
            correctionLater.append("-");
            correctionLater.append(correction);
            String correctionLaterString = correctionLater.toString();
            EanUpcEntity mainEanUpcEntity = eanUpcService.selectOne(new EntityWrapper<EanUpcEntity>().eq("state",0));
            if(mainEanUpcEntity != null){
                mainEanUpcEntity.setState(1);
                products.setEanCode(mainEanUpcEntity.getCode());
                eanUpcService.updateById(mainEanUpcEntity);
            }else{
                return R.error("EAN码数量不足，尽快添加");
            }
            //插入变体信息
            variantsInfoService.delete(new EntityWrapper<VariantsInfoEntity>().eq("product_id", productId));
            if (variantsInfosList != null && variantsInfosList.size() != 0) {
                int size = variantsInfosList.size();
                EanUpcEntity eanUpcEntity = new EanUpcEntity();
                eanUpcEntity.setState(0);
                eanUpcEntity.setType("EAN");
                eanUpcEntity.setSize(size);
                //查出未使用的EAN码，修改为使用
                List<EanUpcEntity> eanUpcEntities = eanUpcService.selectByLimit(eanUpcEntity);
                if (eanUpcEntities != null && eanUpcEntities.size() > 0 && eanUpcEntities.size() == variantsInfosList.size()) {
                    for (int i = 0; i < eanUpcEntities.size(); i++) {
                        eanUpcEntities.get(i).setState(1);
                        eanUpcEntities.get(i).setProductId(products.getProductId());
                        eanUpcService.updateById(eanUpcEntities.get(i));
                    }
                } else {
                    return R.error("EAN码数量不足，尽快添加");
                }
                for (int i = 0; i < variantsInfosList.size(); i++) {
                    String code = eanUpcEntities.get(i).getCode();
                    VariantsInfoEntity variantsInfoEntity = variantsInfosList.get(i);
                    variantsInfoEntity.setEanCode(code);
                    variantsInfoEntity.setVariantSku(correctionLaterString + "-" + i);
                    variantsInfoEntity.setProductId(products.getProductId());
                    variantsInfoEntity.setUserId(getUserId());
                    variantsInfoEntity.setDeptId(getDeptId());
                }
                eanUpcEntities.clear();
                variantsInfoService.insertBatch(variantsInfosList);
            }
            //根据产品id进行更新
            products.setProductSku(correctionLaterString);
        }else{
            //修正为空时，判断是否有ean，没有则生成
            if(StringUtils.isBlank(products.getEanCode())){
                EanUpcEntity mainEanUpcEntity = eanUpcService.selectOne(new EntityWrapper<EanUpcEntity>().eq("state",0));
                if(mainEanUpcEntity != null){
                    mainEanUpcEntity.setState(1);
                    products.setEanCode(mainEanUpcEntity.getCode());
                    eanUpcService.updateById(mainEanUpcEntity);
                }else{
                    return R.error("EAN码数量不足，尽快添加");
                }
            }
            if (variantsInfosList != null && variantsInfosList.size() != 0) {
                if(variantsInfosList.get(0).getVariantId() != null){
                    EanUpcEntity vEanUpcEntity = null;
                    for (int i = 0; i < variantsInfosList.size(); i++) {
                        if(StringUtils.isBlank(variantsInfosList.get(i).getEanCode())){
                            vEanUpcEntity = eanUpcService.selectOne(new EntityWrapper<EanUpcEntity>().eq("state",0));
                            if(vEanUpcEntity != null){
                                vEanUpcEntity.setState(1);
                                variantsInfosList.get(i).setEanCode(vEanUpcEntity.getCode());
                                eanUpcService.updateById(vEanUpcEntity);
                            }else{
                                return R.error("EAN码数量不足，尽快添加");
                            }
                        }
                    }
                    variantsInfoService.updateBatchById(variantsInfosList);
                }else{
                    variantsInfoService.delete(new EntityWrapper<VariantsInfoEntity>().eq("product_id", productId));
                    int size = variantsInfosList.size();
                    EanUpcEntity eanUpcEntity = new EanUpcEntity();
                    eanUpcEntity.setState(0);
                    eanUpcEntity.setType("EAN");
                    eanUpcEntity.setSize(size);
                    //查出未使用的EAN码，修改为使用
                    List<EanUpcEntity> eanUpcEntities = eanUpcService.selectByLimit(eanUpcEntity);
                    if (eanUpcEntities != null && eanUpcEntities.size() > 0 && eanUpcEntities.size() == variantsInfosList.size()) {
                        for (int i = 0; i < eanUpcEntities.size(); i++) {
                            eanUpcEntities.get(i).setState(1);
                            eanUpcEntities.get(i).setProductId(products.getProductId());
                            eanUpcService.updateById(eanUpcEntities.get(i));
                        }
                    } else {
                        return R.error("EAN码数量不足，尽快添加");
                    }
                    for (int i = 0; i < variantsInfosList.size(); i++) {
                        String code = eanUpcEntities.get(i).getCode();
                        VariantsInfoEntity variantsInfoEntity = variantsInfosList.get(i);
                        variantsInfoEntity.setEanCode(code);
                        variantsInfoEntity.setVariantSku(productSku + "-" + i);
                        variantsInfoEntity.setProductId(products.getProductId());
                        variantsInfoEntity.setUserId(getUserId());
                        variantsInfoEntity.setDeptId(getDeptId());
                    }
                    eanUpcEntities.clear();
                    variantsInfoService.insertBatch(variantsInfosList);
                }
            }
            //根据产品id进行更新
            products.setProductSku(productSku);
        }
        variantsInfosList.clear();
        products.setCorrection(null);
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
    public R costFreight(@RequestBody ProductsEntity productsEntity) {
        productsEntity = productsService.costFreight(productsEntity);
        return R.ok().put("productsEntity", productsEntity);

        //根据长宽高计算出来重量
        /*Double weight = calculatedWeight(productLength, productWide, productHeight);
        if (productWeight != 0 || weight != 0) {
            //体积重量大于实际重量的必须按体积重量计收资费
            //长宽高计算出来重量和输入的产品重量做比较
            if (productWeight > weight) {
                //美国运费
                FreightCostEntity americanFC = productsEntity.getAmericanFC();
                if (americanFC == null) {
                    americanFC = new FreightCostEntity();
                    //计算出美国运费
                    americanFC = calculateFreight(productWeight, americanFC, US);
                    //美国运费
                    BigDecimal americanFreight = americanFC.getFreight();
                    //美国成本价格=采购价格+国内物流+国际物流价格
                    BigDecimal americanCost = americanFreight.add(sum);
                    //计算出美国售价，外币，优化信息
                    americanFC = priceInfo(americanCost, americanFC, USD, discount);
                    productsEntity.setAmericanFC(americanFC);
                } else {
                    //计算出美国运费
                    americanFC = calculateFreight(productWeight, americanFC, US);
                    //美国运费
                    BigDecimal americanFreight = americanFC.getFreight();
                    //美国成本价格=采购价格+国内物流+国际物流价格
                    BigDecimal americanCost = americanFreight.add(sum);
                    //计算出美国售价，外币，优化信息
                    americanFC = priceInfo(americanCost, americanFC, USD, discount);
                }


                // 加拿大运费
                FreightCostEntity canadaFC = productsEntity.getCanadaFC();
                if (canadaFC == null) {
                    canadaFC = new FreightCostEntity();
                    canadaFC = calculateFreight(productWeight, canadaFC, CA);
                    BigDecimal canadaFreight = canadaFC.getFreight();
                    BigDecimal canadaCost = canadaFreight.add(sum);
                    canadaFC = priceInfo(canadaCost, canadaFC, CAD, discount);
                    productsEntity.setCanadaFC(canadaFC);
                } else {
                    canadaFC = calculateFreight(productWeight, canadaFC, CA);
                    BigDecimal canadaFreight = canadaFC.getFreight();
                    BigDecimal canadaCost = canadaFreight.add(sum);
                    canadaFC = priceInfo(canadaCost, canadaFC, CAD, discount);
                }


                // 墨西哥运费
                FreightCostEntity mexicoFC = productsEntity.getMexicoFC();
                if (mexicoFC == null) {
                    mexicoFC = new FreightCostEntity();
                    mexicoFC = calculateFreight(productWeight, mexicoFC, MX);
                    BigDecimal mexicoFreight = mexicoFC.getFreight();
                    BigDecimal mexicoCost = mexicoFreight.add(sum);
                    mexicoFC = priceInfo(mexicoCost, mexicoFC, MXN, discount);
                    productsEntity.setMexicoFC(mexicoFC);
                } else {
                    mexicoFC = calculateFreight(productWeight, mexicoFC, MX);
                    BigDecimal mexicoFreight = mexicoFC.getFreight();
                    BigDecimal mexicoCost = mexicoFreight.add(sum);
                    mexicoFC = priceInfo(mexicoCost, mexicoFC, MXN, discount);
                }


                //英国运费
                FreightCostEntity britainFC = productsEntity.getBritainFC();
                if (britainFC == null) {
                    britainFC = new FreightCostEntity();
                    britainFC = calculateFreight(productWeight, britainFC, GB);
                    BigDecimal britainFreight = britainFC.getFreight();
                    BigDecimal britainCost = britainFreight.add(sum);
                    britainFC = priceInfo(britainCost, britainFC, GBP, discount);
                    productsEntity.setBritainFC(britainFC);

                } else {
                    britainFC = calculateFreight(productWeight, britainFC, GB);
                    BigDecimal britainFreight = britainFC.getFreight();
                    BigDecimal britainCost = britainFreight.add(sum);
                    britainFC = priceInfo(britainCost, britainFC, GBP, discount);
                }
                // 法国运费
                FreightCostEntity franceFC = productsEntity.getFranceFC();
                if (franceFC == null) {
                    franceFC = new FreightCostEntity();
                    franceFC = calculateFreight(productWeight, franceFC, FR);
                    BigDecimal franceFreight = franceFC.getFreight();
                    BigDecimal franceCost = franceFreight.add(sum);
                    franceFC = priceInfo(franceCost, franceFC, EUR, discount);
                    productsEntity.setFranceFC(franceFC);

                } else {
                    franceFC = calculateFreight(productWeight, franceFC, FR);
                    BigDecimal franceFreight = franceFC.getFreight();
                    BigDecimal franceCost = franceFreight.add(sum);
                    franceFC = priceInfo(franceCost, franceFC, EUR, discount);
                }

                // 德国运费
                FreightCostEntity germanyFC = productsEntity.getGermanyFC();
                if (germanyFC == null) {
                    germanyFC = new FreightCostEntity();
                    germanyFC = calculateFreight(productWeight, germanyFC, DE);
                    BigDecimal germanyFreight = germanyFC.getFreight();
                    BigDecimal germanyCost = germanyFreight.add(sum);
                    germanyFC = priceInfo(germanyCost, germanyFC, EUR, discount);
                    productsEntity.setGermanyFC(germanyFC);
                } else {
                    germanyFC = calculateFreight(productWeight, germanyFC, DE);
                    BigDecimal germanyFreight = germanyFC.getFreight();
                    BigDecimal germanyCost = germanyFreight.add(sum);
                    germanyFC = priceInfo(germanyCost, germanyFC, EUR, discount);
                }


                //意大利运费
                FreightCostEntity italyFC = productsEntity.getItalyFC();
                if (italyFC == null) {
                    italyFC = new FreightCostEntity();
                    italyFC = calculateFreight(productWeight, italyFC, IT);
                    BigDecimal italyFreight = italyFC.getFreight();
                    BigDecimal italyCost = italyFreight.add(sum);
                    italyFC = priceInfo(italyCost, italyFC, EUR, discount);
                    productsEntity.setItalyFC(italyFC);

                } else {
                    italyFC = calculateFreight(productWeight, italyFC, IT);
                    BigDecimal italyFreight = italyFC.getFreight();
                    BigDecimal italyCost = italyFreight.add(sum);
                    italyFC = priceInfo(italyCost, italyFC, EUR, discount);
                }


                //西班牙运费
                FreightCostEntity spainFC = productsEntity.getSpainFC();
                if (spainFC == null) {
                    spainFC = new FreightCostEntity();
                    spainFC = calculateFreight(productWeight, spainFC, ES);
                    BigDecimal spainFreight = spainFC.getFreight();
                    BigDecimal spainCost = spainFreight.add(sum);
                    spainFC = priceInfo(spainCost, spainFC, EUR, discount);
                    productsEntity.setSpainFC(spainFC);
                } else {
                    spainFC = calculateFreight(productWeight, spainFC, ES);
                    BigDecimal spainFreight = spainFC.getFreight();
                    BigDecimal spainCost = spainFreight.add(sum);
                    spainFC = priceInfo(spainCost, spainFC, EUR, discount);

                }


                // 日本运费
                FreightCostEntity japanFC = productsEntity.getJapanFC();
                if (japanFC == null) {
                    japanFC = new FreightCostEntity();
                    japanFC = calculateFreight(productWeight, japanFC, JP);
                    BigDecimal japanFreight = japanFC.getFreight();
                    BigDecimal japanCost = japanFreight.add(sum);
                    japanFC = priceInfo(japanCost, japanFC, JPY, discount);
                    productsEntity.setJapanFC(japanFC);

                } else {
                    japanFC = calculateFreight(productWeight, japanFC, JP);
                    BigDecimal japanFreight = japanFC.getFreight();
                    BigDecimal japanCost = japanFreight.add(sum);
                    japanFC = priceInfo(japanCost, japanFC, JPY, discount);
                }


                //澳大利亚运费
                FreightCostEntity australiaFC = productsEntity.getAustraliaFC();
                if (australiaFC == null) {
                    australiaFC = new FreightCostEntity();
                    australiaFC = calculateFreight(productWeight, australiaFC, AU);
                    BigDecimal australiaFreight = australiaFC.getFreight();
                    BigDecimal australiaCost = australiaFreight.add(sum);
                    australiaFC = priceInfo(australiaCost, australiaFC, AUD, discount);
                    productsEntity.setAustraliaFC(australiaFC);

                } else {
                    australiaFC = calculateFreight(productWeight, australiaFC, AU);
                    BigDecimal australiaFreight = australiaFC.getFreight();
                    BigDecimal australiaCost = australiaFreight.add(sum);
                    australiaFC = priceInfo(australiaCost, australiaFC, AUD, discount);
                }

            } else {
                //美国运费
                FreightCostEntity americanFC = productsEntity.getAmericanFC();
                if (americanFC == null) {
                    americanFC = new FreightCostEntity();
                    //计算出美国运费
                    americanFC = calculateFreight(weight, americanFC, US);
                    //美国运费
                    BigDecimal americanFreight = americanFC.getFreight();
                    //美国成本价格=采购价格+国内物流+国际物流价格
                    BigDecimal americanCost = americanFreight.add(sum);
                    //计算出美国售价，外币，优化信息
                    americanFC = priceInfo(americanCost, americanFC, USD, discount);
                    productsEntity.setAmericanFC(americanFC);
                } else {
                    //计算出美国运费
                    americanFC = calculateFreight(weight, americanFC, US);
                    //美国运费
                    BigDecimal americanFreight = americanFC.getFreight();
                    //美国成本价格=采购价格+国内物流+国际物流价格
                    BigDecimal americanCost = americanFreight.add(sum);
                    //计算出美国售价，外币，优化信息
                    americanFC = priceInfo(americanCost, americanFC, USD, discount);
                }


                // 加拿大运费
                FreightCostEntity canadaFC = productsEntity.getCanadaFC();
                if (canadaFC == null) {
                    canadaFC = new FreightCostEntity();
                    canadaFC = calculateFreight(weight, canadaFC, CA);
                    BigDecimal canadaFreight = canadaFC.getFreight();
                    BigDecimal canadaCost = canadaFreight.add(sum);
                    canadaFC = priceInfo(canadaCost, canadaFC, CAD, discount);
                    productsEntity.setCanadaFC(canadaFC);
                } else {
                    canadaFC = calculateFreight(weight, canadaFC, CA);
                    BigDecimal canadaFreight = canadaFC.getFreight();
                    BigDecimal canadaCost = canadaFreight.add(sum);
                    canadaFC = priceInfo(canadaCost, canadaFC, CAD, discount);
                }


                // 墨西哥运费
                FreightCostEntity mexicoFC = productsEntity.getMexicoFC();
                if (mexicoFC == null) {
                    mexicoFC = new FreightCostEntity();
                    mexicoFC = calculateFreight(weight, mexicoFC, MX);
                    BigDecimal mexicoFreight = mexicoFC.getFreight();
                    BigDecimal mexicoCost = mexicoFreight.add(sum);
                    mexicoFC = priceInfo(mexicoCost, mexicoFC, MXN, discount);
                    productsEntity.setMexicoFC(mexicoFC);
                } else {
                    mexicoFC = calculateFreight(weight, mexicoFC, MX);
                    BigDecimal mexicoFreight = mexicoFC.getFreight();
                    BigDecimal mexicoCost = mexicoFreight.add(sum);
                    mexicoFC = priceInfo(mexicoCost, mexicoFC, MXN, discount);
                }


                //英国运费
                FreightCostEntity britainFC = productsEntity.getBritainFC();
                if (britainFC == null) {
                    britainFC = new FreightCostEntity();
                    britainFC = calculateFreight(weight, britainFC, GB);
                    BigDecimal britainFreight = britainFC.getFreight();
                    BigDecimal britainCost = britainFreight.add(sum);
                    britainFC = priceInfo(britainCost, britainFC, GBP, discount);
                    productsEntity.setBritainFC(britainFC);

                } else {
                    britainFC = calculateFreight(weight, britainFC, GB);
                    BigDecimal britainFreight = britainFC.getFreight();
                    BigDecimal britainCost = britainFreight.add(sum);
                    britainFC = priceInfo(britainCost, britainFC, GBP, discount);
                }
                // 法国运费
                FreightCostEntity franceFC = productsEntity.getFranceFC();
                if (franceFC == null) {
                    franceFC = new FreightCostEntity();
                    franceFC = calculateFreight(weight, franceFC, FR);
                    BigDecimal franceFreight = franceFC.getFreight();
                    BigDecimal franceCost = franceFreight.add(sum);
                    franceFC = priceInfo(franceCost, franceFC, EUR, discount);
                    productsEntity.setFranceFC(franceFC);

                } else {
                    franceFC = calculateFreight(weight, franceFC, FR);
                    BigDecimal franceFreight = franceFC.getFreight();
                    BigDecimal franceCost = franceFreight.add(sum);
                    franceFC = priceInfo(franceCost, franceFC, EUR, discount);
                }

                // 德国运费
                FreightCostEntity germanyFC = productsEntity.getGermanyFC();
                if (germanyFC == null) {
                    germanyFC = new FreightCostEntity();
                    germanyFC = calculateFreight(weight, germanyFC, DE);
                    BigDecimal germanyFreight = germanyFC.getFreight();
                    BigDecimal germanyCost = germanyFreight.add(sum);
                    germanyFC = priceInfo(germanyCost, germanyFC, EUR, discount);
                    productsEntity.setGermanyFC(germanyFC);
                } else {
                    germanyFC = calculateFreight(weight, germanyFC, DE);
                    BigDecimal germanyFreight = germanyFC.getFreight();
                    BigDecimal germanyCost = germanyFreight.add(sum);
                    germanyFC = priceInfo(germanyCost, germanyFC, EUR, discount);
                }


                //意大利运费
                FreightCostEntity italyFC = productsEntity.getItalyFC();
                if (italyFC == null) {
                    italyFC = new FreightCostEntity();
                    italyFC = calculateFreight(weight, italyFC, IT);
                    BigDecimal italyFreight = italyFC.getFreight();
                    BigDecimal italyCost = italyFreight.add(sum);
                    italyFC = priceInfo(italyCost, italyFC, EUR, discount);
                    productsEntity.setItalyFC(italyFC);

                } else {
                    italyFC = calculateFreight(weight, italyFC, IT);
                    BigDecimal italyFreight = italyFC.getFreight();
                    BigDecimal italyCost = italyFreight.add(sum);
                    italyFC = priceInfo(italyCost, italyFC, EUR, discount);
                }


                //西班牙运费
                FreightCostEntity spainFC = productsEntity.getSpainFC();
                if (spainFC == null) {
                    spainFC = new FreightCostEntity();
                    spainFC = calculateFreight(weight, spainFC, ES);
                    BigDecimal spainFreight = spainFC.getFreight();
                    BigDecimal spainCost = spainFreight.add(sum);
                    spainFC = priceInfo(spainCost, spainFC, EUR, discount);
                    productsEntity.setSpainFC(spainFC);
                } else {
                    spainFC = calculateFreight(weight, spainFC, ES);
                    BigDecimal spainFreight = spainFC.getFreight();
                    BigDecimal spainCost = spainFreight.add(sum);
                    spainFC = priceInfo(spainCost, spainFC, EUR, discount);
                }


                // 日本运费
                FreightCostEntity japanFC = productsEntity.getJapanFC();
                if (japanFC == null) {
                    japanFC = new FreightCostEntity();
                    japanFC = calculateFreight(weight, japanFC, JP);
                    BigDecimal japanFreight = japanFC.getFreight();
                    BigDecimal japanCost = japanFreight.add(sum);
                    japanFC = priceInfo(japanCost, japanFC, JPY, discount);
                    productsEntity.setJapanFC(japanFC);

                } else {
                    japanFC = calculateFreight(weight, japanFC, JP);
                    BigDecimal japanFreight = japanFC.getFreight();
                    BigDecimal japanCost = japanFreight.add(sum);
                    japanFC = priceInfo(japanCost, japanFC, JPY, discount);
                }


                //澳大利亚运费
                FreightCostEntity australiaFC = productsEntity.getAustraliaFC();
                if (australiaFC == null) {
                    australiaFC = new FreightCostEntity();
                    australiaFC = calculateFreight(weight, australiaFC, AU);
                    BigDecimal australiaFreight = australiaFC.getFreight();
                    BigDecimal australiaCost = australiaFreight.add(sum);
                    australiaFC = priceInfo(australiaCost, australiaFC, AUD, discount);
                    productsEntity.setAustraliaFC(australiaFC);

                } else {
                    australiaFC = calculateFreight(weight, australiaFC, AU);
                    BigDecimal australiaFreight = australiaFC.getFreight();
                    BigDecimal australiaCost = australiaFreight.add(sum);
                    australiaFC = priceInfo(australiaCost, australiaFC, AUD, discount);
                }
            }
            return R.ok().put("productsEntity", productsEntity);
        } else {
            //美国运费
            FreightCostEntity americanFC = productsEntity.getAmericanFC();
            if (americanFC == null) {
                americanFC = new FreightCostEntity();
                americanFC = priceInfo(sum, americanFC, USD, discount);
                productsEntity.setAmericanFC(americanFC);
            } else {
                //计算出美国售价，外币，优化信息
                americanFC = priceInfo(sum, americanFC, USD, discount);
            }
            // 加拿大运费
            FreightCostEntity canadaFC = productsEntity.getCanadaFC();
            if (canadaFC == null) {
                canadaFC = new FreightCostEntity();
                canadaFC = priceInfo(sum, canadaFC, CAD, discount);
                productsEntity.setCanadaFC(canadaFC);
            } else {
                canadaFC = priceInfo(sum, canadaFC, CAD, discount);
            }

            // 墨西哥运费
            FreightCostEntity mexicoFC = productsEntity.getMexicoFC();
            if (mexicoFC == null) {
                mexicoFC = new FreightCostEntity();
                mexicoFC = priceInfo(sum, mexicoFC, MXN, discount);
                productsEntity.setMexicoFC(mexicoFC);
            } else {
                mexicoFC = priceInfo(sum, mexicoFC, MXN, discount);
            }
            //英国运费
            FreightCostEntity britainFC = productsEntity.getBritainFC();
            if (britainFC == null) {
                britainFC = new FreightCostEntity();
                britainFC = priceInfo(sum, britainFC, GBP, discount);
                productsEntity.setBritainFC(britainFC);
            } else {
                britainFC = priceInfo(sum, britainFC, GBP, discount);
            }

            // 法国运费
            FreightCostEntity franceFC = productsEntity.getFranceFC();
            if (franceFC == null) {
                franceFC = new FreightCostEntity();
                franceFC = priceInfo(sum, franceFC, EUR, discount);
                productsEntity.setFranceFC(franceFC);
            } else {
                franceFC = priceInfo(sum, franceFC, EUR, discount);
            }
            // 德国运费
            FreightCostEntity germanyFC = productsEntity.getGermanyFC();
            if (germanyFC == null) {
                germanyFC = new FreightCostEntity();
                germanyFC = priceInfo(sum, germanyFC, EUR, discount);
                productsEntity.setGermanyFC(germanyFC);
            } else {
                germanyFC = priceInfo(sum, germanyFC, EUR, discount);
            }

            //意大利运费
            FreightCostEntity italyFC = productsEntity.getItalyFC();
            if (italyFC == null) {
                italyFC = new FreightCostEntity();
                italyFC = priceInfo(sum, italyFC, EUR, discount);
                productsEntity.setItalyFC(italyFC);
            } else {
                italyFC = priceInfo(sum, italyFC, EUR, discount);
            }

            //西班牙运费
            FreightCostEntity spainFC = productsEntity.getSpainFC();
            if (spainFC == null) {
                spainFC = new FreightCostEntity();
                spainFC = priceInfo(sum, spainFC, EUR, discount);
                productsEntity.setSpainFC(spainFC);
            } else {
                spainFC = priceInfo(sum, spainFC, EUR, discount);
            }


            // 日本运费
            FreightCostEntity japanFC = productsEntity.getJapanFC();
            if (japanFC == null) {
                japanFC = new FreightCostEntity();
                japanFC = priceInfo(sum, japanFC, JPY, discount);
                productsEntity.setJapanFC(japanFC);
            } else {
                japanFC = priceInfo(sum, japanFC, JPY, discount);
            }


            //澳大利亚运费
            FreightCostEntity australiaFC = productsEntity.getAustraliaFC();
            if (australiaFC == null) {
                australiaFC = new FreightCostEntity();
                australiaFC = priceInfo(sum, australiaFC, AUD, discount);
                productsEntity.setAustraliaFC(australiaFC);
            } else {
                australiaFC = priceInfo(sum, australiaFC, AUD, discount);
            }

            return R.ok().put("productsEntity", productsEntity);
        }*/
    }

    /**
     * @methodname: refresh 刷新（最终，利润，利润率）
     * @param: [productsEntity]
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/12/8 11:11
     */
    @RequestMapping("/refresh")
    public R refresh(@RequestBody ProductsEntity productsEntity) {
        productsEntity = productsService.refresh(productsEntity);
        return R.ok().put("productsEntity", productsEntity);
    }
    /**
     * @methodname: refresh 刷新（最终，利润，利润率）
     * @param: [productsEntity]
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/12/8 11:11
     */
    @RequestMapping("/refreshProfitRate")
    public R refreshProfitRate(@RequestBody ProductsEntity productsEntity) {
        //减去亚马逊15%
        BigDecimal temp = new BigDecimal("0.85");
        //美国汇率
        BigDecimal americanRate = getRate(USD);
        //产品采购价格
        BigDecimal purchasePrice = productsEntity.getPurchasePrice();
        //国内运费
        BigDecimal domesticFreight = productsEntity.getDomesticFreight();
        //产品采购价格和国内运费的和
        BigDecimal sum = purchasePrice.add(domesticFreight);
        //美国运费信息
        FreightCostEntity americanFC = productsEntity.getAmericanFC();
        //把利润率格式转化为0.00%格式的
        DecimalFormat format = new DecimalFormat("0.00%");
        if (americanFC != null) {
            BigDecimal americanOptimization = americanFC.getOptimization();
            BigDecimal americanFinalPrice = americanFC.getFinalPrice();
            //最终
            americanFC.setFinalPrice(americanFinalPrice);
            //美国运费
            BigDecimal americanFreight = americanFC.getFreight();
            //美国成本
            BigDecimal americanCost = sum.add(americanFreight);
            //美国利润
            BigDecimal americanProfit = ((temp.multiply(americanRate).multiply(americanFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(americanCost);
            americanFC.setProfit(americanProfit);
            //美国利润率 divide(BigDecimal)相除  multiply(BigDecimal)相乘
            BigDecimal americanProfitRate = americanProfit.divide(americanRate.multiply(americanFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            //把利润率转换成%格式
            String americanProfitRateString = format.format(americanProfitRate);
            americanFC.setProfitRate(americanProfitRateString);
            productsEntity.setAmericanFC(americanFC);
        }

        FreightCostEntity canadaFC = productsEntity.getCanadaFC();
        if (canadaFC != null) {
            BigDecimal canadaFinalPrice = canadaFC.getFinalPrice();
            canadaFC.setFinalPrice(canadaFinalPrice);
            BigDecimal canadaRate = getRate(CAD);
            BigDecimal canadaFreight = canadaFC.getFreight();
            BigDecimal canadaCost = sum.add(canadaFreight);
            BigDecimal canadaProfit = ((temp.multiply(canadaRate).multiply(canadaFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(canadaCost);
            canadaFC.setProfit(canadaProfit);
            BigDecimal canadaProfitRate = canadaProfit.divide(canadaRate.multiply(canadaFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String canadaProfitRateString = format.format(canadaProfitRate);
            canadaFC.setProfitRate(canadaProfitRateString);
            productsEntity.setCanadaFC(canadaFC);
        }

        FreightCostEntity mexicoFC = productsEntity.getMexicoFC();
        if (mexicoFC != null) {
            BigDecimal mexicoFinalPrice = mexicoFC.getFinalPrice();
            mexicoFC.setFinalPrice(mexicoFinalPrice);
            BigDecimal mexicoRate = getRate(MXN);
            BigDecimal mexicoFreight = mexicoFC.getFreight();
            BigDecimal mexicoCost = sum.add(mexicoFreight);
            BigDecimal mexicoProfit = ((temp.multiply(mexicoRate).multiply(mexicoFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(mexicoCost);
            mexicoFC.setProfit(mexicoProfit);
            BigDecimal mexicoProfitRate = mexicoProfit.divide(mexicoRate.multiply(mexicoFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String mexicoProfitRateString = format.format(mexicoProfitRate);
            mexicoFC.setProfitRate(mexicoProfitRateString);
            productsEntity.setMexicoFC(mexicoFC);
        }

        FreightCostEntity britainFC = productsEntity.getBritainFC();
        if (britainFC != null) {
            BigDecimal britainFinalPrice = britainFC.getFinalPrice();
            britainFC.setFinalPrice(britainFinalPrice);
            BigDecimal britainRate = getRate(GBP);
            BigDecimal britainFreight = britainFC.getFreight();
            BigDecimal britainCost = sum.add(britainFreight);
            BigDecimal britainProfit = ((temp.multiply(britainRate).multiply(britainFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(britainCost);
            britainFC.setProfit(britainProfit);
            BigDecimal britainProfitRate = britainProfit.divide(britainRate.multiply(britainFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String britainProfitRateString = format.format(britainProfitRate);
            britainFC.setProfitRate(britainProfitRateString);
            productsEntity.setBritainFC(britainFC);
        }

        FreightCostEntity franceFC = productsEntity.getFranceFC();
        if (franceFC != null) {
            BigDecimal franceFinalPrice = franceFC.getFinalPrice();
            franceFC.setFinalPrice(franceFinalPrice);
            BigDecimal franceRate = getRate(EUR);
            BigDecimal franceFreight = franceFC.getFreight();
            BigDecimal franceCost = sum.add(franceFreight);
            BigDecimal franceProfit = ((temp.multiply(franceRate).multiply(franceFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(franceCost);
            franceFC.setProfit(franceProfit);
            BigDecimal franceProfitRate = franceProfit.divide(franceRate.multiply(franceFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String franceProfitRateString = format.format(franceProfitRate);
            franceFC.setProfitRate(franceProfitRateString);
            productsEntity.setFranceFC(franceFC);

        }

        FreightCostEntity germanyFC = productsEntity.getGermanyFC();
        if (germanyFC != null) {
            BigDecimal germanyFinalPrice = germanyFC.getFinalPrice();
            germanyFC.setFinalPrice(germanyFinalPrice);
            BigDecimal germanyRate = getRate(EUR);
            BigDecimal germanyFreight = germanyFC.getFreight();
            BigDecimal germanyCost = sum.add(germanyFreight);
            BigDecimal germanyProfit = ((temp.multiply(germanyRate).multiply(germanyFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(germanyCost);
            germanyFC.setProfit(germanyProfit);
            BigDecimal germanyProfitRate = germanyProfit.divide(germanyRate.multiply(germanyFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String germanyProfitRateString = format.format(germanyProfitRate);
            germanyFC.setProfitRate(germanyProfitRateString);
            productsEntity.setGermanyFC(germanyFC);
        }

        FreightCostEntity italyFC = productsEntity.getItalyFC();
        if (italyFC != null) {
            BigDecimal italyFinalPrice = italyFC.getFinalPrice();
            italyFC.setFinalPrice(italyFinalPrice);
            BigDecimal italyRate = getRate(EUR);
            BigDecimal italyFreight = italyFC.getFreight();
            BigDecimal italyCost = sum.add(italyFreight);
            BigDecimal italyProfit = ((temp.multiply(italyRate).multiply(italyFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(italyCost);
            italyFC.setProfit(italyProfit);
            BigDecimal italyProfitRate = italyProfit.divide(italyRate.multiply(italyFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String italyProfitRateString = format.format(italyProfitRate);
            italyFC.setProfitRate(italyProfitRateString);
            productsEntity.setItalyFC(italyFC);
        }

        FreightCostEntity spainFC = productsEntity.getSpainFC();
        if (spainFC != null) {
            BigDecimal spainFinalPrice = spainFC.getFinalPrice();
            spainFC.setFinalPrice(spainFinalPrice);
            BigDecimal spainRate = getRate(EUR);
            BigDecimal spainFreight = spainFC.getFreight();
            BigDecimal spainCost = sum.add(spainFreight);
            BigDecimal spainProfit = ((temp.multiply(spainRate).multiply(spainFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(spainCost);
            spainFC.setProfit(spainProfit);
            BigDecimal spainProfitRate = spainProfit.divide(spainRate.multiply(spainFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String spainProfitRateString = format.format(spainProfitRate);
            spainFC.setProfitRate(spainProfitRateString);
            productsEntity.setSpainFC(spainFC);
        }

        FreightCostEntity japanFC = productsEntity.getJapanFC();
        if (japanFC != null) {
            BigDecimal japanFinalPrice = japanFC.getFinalPrice();
            japanFC.setFinalPrice(japanFinalPrice);
            BigDecimal japanRate = getRate(JPY);
            BigDecimal japanFreight = japanFC.getFreight();
            BigDecimal japanCost = sum.add(japanFreight);
            BigDecimal japanProfit = ((temp.multiply(japanRate).multiply(japanFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(japanCost);
            japanFC.setProfit(japanProfit);
            BigDecimal japanProfitRate = japanProfit.divide(japanRate.multiply(japanFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String japanProfitRateString = format.format(japanProfitRate);
            japanFC.setProfitRate(japanProfitRateString);
            productsEntity.setJapanFC(japanFC);
        }


        FreightCostEntity australiaFC = productsEntity.getAustraliaFC();
        if (australiaFC != null) {
            BigDecimal australiaFinalPrice = australiaFC.getFinalPrice();
            australiaFC.setFinalPrice(australiaFinalPrice);
            BigDecimal australiaRate = getRate(AUD);
            BigDecimal australiaFreight = australiaFC.getFreight();
            BigDecimal australiaCost = sum.add(australiaFreight);
            BigDecimal australiaProfit = ((temp.multiply(australiaRate).multiply(australiaFinalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(australiaCost);
            australiaFC.setProfit(australiaProfit);
            BigDecimal australiaProfitRate = australiaProfit.divide(australiaRate.multiply(australiaFinalPrice), 4, BigDecimal.ROUND_HALF_UP);
            String australiaProfitRateString = format.format(australiaProfitRate);
            australiaFC.setProfitRate(australiaProfitRateString);
            productsEntity.setAustraliaFC(australiaFC);
        }
        return R.ok().put("productsEntity", productsEntity);
    }

    //根据产品长宽高计算产品重量
    public Double calculatedWeight(Double productLength, Double productWide, Double productHeight) {
        //体积重量按每6000立方厘米折合1公斤计算,体积重量的计算公式为:体积重量=(长X宽X高)/6000
        Double weight = (productHeight * productLength * productWide) / 6000;
        BigDecimal b = new BigDecimal(weight);
        double weightLater = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return weightLater;
    }

    //计算国际运费
    public FreightCostEntity calculateFreight(Double weight, FreightCostEntity freightCostEntity, String countryCode) {
        if (weight < 2) {
            //根据国家编号查找运费价格进行运费计算
            FreightPriceEntity freightPriceEntity = freightPriceService.selectOne(new EntityWrapper<FreightPriceEntity>().eq("type", "小包").eq("country_code", countryCode));
            BigDecimal bigDecimal = new BigDecimal(weight);
            //计算出运费价格
            BigDecimal freightPrice = bigDecimal.multiply(freightPriceEntity.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
            freightCostEntity.setFreight(freightPrice);
            freightCostEntity.setType("小包");
            return freightCostEntity;
        } else {
            //根据国家编号查找运费价格进行运费计算
            FreightPriceEntity freightPriceEntity = freightPriceService.selectOne(new EntityWrapper<FreightPriceEntity>().eq("type", "大包").eq("country_code", countryCode));
            BigDecimal bigDecimal = new BigDecimal(weight);
            //计算出运费价格
            BigDecimal freightPrice = bigDecimal.multiply(freightPriceEntity.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
            freightCostEntity.setFreight(freightPrice);
            freightCostEntity.setType("大包");
            return freightCostEntity;
        }
    }
    //获取国家的汇率
    private BigDecimal getRate(String rateCode) {
        //获取国家的汇率
        AmazonRateEntity amazonRateEntity = amazonRateService.selectOne(new EntityWrapper<AmazonRateEntity>().eq("rate_code", rateCode));
        BigDecimal rate = amazonRateEntity.getRate();
        return rate;
    }

}

