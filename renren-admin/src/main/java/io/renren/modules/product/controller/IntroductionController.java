package io.renren.modules.product.controller;

import com.swjtu.lang.LANG;
import com.swjtu.querier.Querier;
import com.swjtu.trans.AbstractTranslator;
import com.swjtu.trans.impl.BaiduTranslator;
import com.swjtu.trans.impl.GoogleTranslator;
import com.swjtu.trans.impl.YoudaoTranslator;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.product.entity.IntroductionEntity;
import io.renren.modules.product.service.IntroductionService;
import io.renren.modules.product.vm.TranslateVM;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * 国家介绍
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
@RestController
@RequestMapping("product/introduction")
public class IntroductionController {
    @Autowired
    private IntroductionService introductionService;

    /**
     * @methodname: list 列表
     * @param: [params] 接受参数
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:introduction:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = introductionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * @methodname: info 信息
     * @param: [introductionId] 国家介绍id
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/info/{introductionId}")
    @RequiresPermissions("product:introduction:info")
    public R info(@PathVariable("introductionId") Long introductionId){
        IntroductionEntity introduction = introductionService.selectById(introductionId);

        return R.ok().put("introduction", introduction);
    }

    /**
     * @methodname: save 保存
     * @param: [introduction] 国家介绍实体
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:introduction:save")
    public R save(@RequestBody IntroductionEntity introduction){
        introductionService.insert(introduction);

        return R.ok();
    }

    /**
     * @methodname: update 删除
     * @param: [introduction] 国家介绍实体
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:introduction:update")
    public R update(@RequestBody IntroductionEntity introduction){
        ValidatorUtils.validateEntity(introduction);
        introductionService.updateAllColumnById(introduction);//全部更新

        return R.ok();
    }

    /**
     * @methodname: delete 删除
     * @param: [introductionIds] 国家介绍id数组
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:introduction:delete")
    public R delete(@RequestBody Long[] introductionIds){
        introductionService.deleteBatchIds(Arrays.asList(introductionIds));

        return R.ok();
    }
    /**
     * @methodname: introductionOne 根据国家介绍id查出介绍信息
     * @param: [introductionId]国家介绍id
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 17:53
     */
    @RequestMapping("/introductionone")
    public R introductionOne(Long introductionId) {
        IntroductionEntity introductionEntity = introductionService.selectById(introductionId);
        return R.ok().put("introductionEntity", introductionEntity);
    }

    /**
     * ZhtoEn 中译英
     * @param: [productTitle, keyWord, keyPoints, productDescription]
     * @return: java.lang.String
     * @auther: wdh
     * @date: 2018/11/5 16:16
     */
    @RequestMapping("/zhtoEn")
    public R zhtoEn(@RequestBody TranslateVM translateVM){
//    public List<IntroductionEntity> ZhtoEn(String productTitle,String keyWord,String keyPoints,String productDescription){
        String productTitle = translateVM.getProductTitle();
        String keyWord = translateVM.getKeyWord();
        String keyPoints = translateVM.getKeyPoints();
        String productDescription = translateVM.getProductDescription();
        IntroductionEntity introductionEn = new IntroductionEntity();
        // 获取查询器
        Querier<AbstractTranslator> querierTrans = new Querier<>();
        // 设置参数：标题
        querierTrans.setParams(LANG.ZH, LANG.EN, productTitle);
        // 向查询器中添加 Google 翻译器
        querierTrans.attach(new GoogleTranslator());
        // 向查询器中添加 Baidu 翻译器
        querierTrans.attach(new BaiduTranslator());
        // 执行查询并接收查询结果
        //翻译标题
        List<String> titleList = querierTrans.execute();
        if (titleList.get(0) != "" && titleList.get(0) != null){
            //谷歌
            introductionEn.setProductTitle(toUpperCase(titleList.get(0)));
        }else{
            //百度
            introductionEn.setProductTitle(toUpperCase(titleList.get(1)));
        }

        //翻译关键字
        querierTrans.setText(keyWord);
        List<String> keyWordList = querierTrans.execute();
        if (keyWordList.get(0) != "" && keyWordList.get(0) != null){
            introductionEn.setKeyWord(toUpperCase(keyWordList.get(0)));
        }else{
            introductionEn.setKeyWord(toUpperCase(keyWordList.get(1)));
        }

        //翻译要点说明
        querierTrans.setText(keyPoints);
        List<String> keyPointsList = querierTrans.execute();
        if (keyPointsList.get(0) != "" && keyPointsList.get(0) != null){
            introductionEn.setKeyPoints(keyPointsList.get(0));
        }else{
            introductionEn.setKeyPoints(keyPointsList.get(1));
        }

        //翻译产品描述
        querierTrans.setText(productDescription);
        List<String> productDescriptionList = querierTrans.execute();
        if (productDescriptionList.get(0) != "" && productDescriptionList.get(0) != null){
            introductionEn.setProductDescription(productDescriptionList.get(0));
        }else{
            introductionEn.setProductDescription(productDescriptionList.get(1));
        }
        introductionEn.setCountry("EN");
        IntroductionEntity introductionFra = EntoOther(introductionEn,"FRA");
        IntroductionEntity introductionDe = EntoOther(introductionEn,"DE");
        IntroductionEntity introductionIt = EntoOther(introductionEn,"IT");
        IntroductionEntity introductionSpa = EntoOther(introductionEn,"SPA");
        IntroductionEntity introductionJp = EntoOther(introductionEn,"JP");

        return R.ok().put("introductionEn",introductionEn)
                .put("introductionFra",introductionFra)
                .put("introductionDe",introductionDe)
                .put("introductionIt",introductionIt)
                .put("introductionSpa",introductionSpa)
                .put("introductionJp",introductionJp);
//        List<IntroductionEntity> list = new ArrayList<>();
//        list.add(introductionEn);
//        list.add(introductionFra);
//        list.add(introductionDe);
//        list.add(introductionIt);
//        list.add(introductionSpa);
//        list.add(introductionJp);
//        return list;
    }

    /**
     * EntoOther 英译其他语言
     * @param: [introduction]
     * @return: io.renren.modules.product.entity.IntroductionEntity
     * @auther: wdh
     * @date: 2018/11/6 15:33
     */
    public IntroductionEntity EntoOther(IntroductionEntity introductionEn, String country){
        IntroductionEntity introduction = new IntroductionEntity();
        Querier<AbstractTranslator> querierTrans = new Querier<>();
        switch (country){
            //法国
            case "FRA":
                querierTrans.setParams(LANG.EN, LANG.FRA, introductionEn.getProductTitle());
                break;
            //德国
            case "DE":
                querierTrans.setParams(LANG.EN, LANG.DE, introductionEn.getProductTitle());
                break;
            //意大利
            case "IT":
                querierTrans.setParams(LANG.EN, LANG.IT, introductionEn.getProductTitle());
                break;
            //西班牙
            case "SPA":
                querierTrans.setParams(LANG.EN, LANG.SPA, introductionEn.getProductTitle());
                break;
            //日本
            case "JP":
                querierTrans.setParams(LANG.EN, LANG.JP, introductionEn.getProductTitle());
                break;
            default:
                break;
        }

        querierTrans.attach(new GoogleTranslator());
        querierTrans.attach(new BaiduTranslator());
        querierTrans.attach(new YoudaoTranslator());

        //翻译标题
        List<String> titleList = querierTrans.execute();
        if (titleList.get(0) != "" && titleList.get(0) != null){
            introduction.setProductTitle(toUpperCase(titleList.get(0)));
        }else if(titleList.get(1) != "" && titleList.get(1) != null){
            introduction.setProductTitle(toUpperCase(titleList.get(1)));
        }else{
            introduction.setProductTitle(toUpperCase(titleList.get(2)));
        }

        //翻译关键字
        querierTrans.setText(introductionEn.getKeyWord());
        List<String> keyWordList = querierTrans.execute();
        if (keyWordList.get(0) != "" && keyWordList.get(0) != null){
            introduction.setKeyWord(toUpperCase(keyWordList.get(0)));
        }else if(keyWordList.get(1) != "" && keyWordList.get(1) != null){
            introduction.setKeyWord(toUpperCase(keyWordList.get(1)));
        }else{
            introduction.setKeyWord(toUpperCase(keyWordList.get(2)));
        }

        //翻译要点说明
        querierTrans.setText(introductionEn.getKeyPoints());
        List<String> keyPointsList = querierTrans.execute();
        if (keyPointsList.get(0) != "" && keyPointsList.get(0) != null){
            introduction.setKeyPoints(keyPointsList.get(0));
        }else if(keyPointsList.get(1) != "" && keyPointsList.get(1) != null){
            introduction.setKeyPoints(keyPointsList.get(1));
        }else{
            introduction.setKeyPoints(keyPointsList.get(2));
        }

        //翻译产品描述
        querierTrans.setText(introductionEn.getProductDescription());
        List<String> productDescriptionList = querierTrans.execute();
        if (productDescriptionList.get(0) != "" && productDescriptionList.get(0) != null){
            introduction.setProductDescription(productDescriptionList.get(0));
        }else if(productDescriptionList.get(1) != "" && productDescriptionList.get(1) != null){
            introduction.setKeyPoints(productDescriptionList.get(1));
        }else{
            introduction.setKeyPoints(productDescriptionList.get(2));
        }
        introduction.setCountry(country);
        System.out.println("country:" + introduction.getCountry());
        System.out.println("title:" + introduction.getProductTitle());
        System.out.println("keyWord:" + introduction.getKeyWord());
        System.out.println("keyPoints:" + introduction.getKeyPoints());
        System.out.println("productDescription:" + introduction.getProductDescription());
        return introduction;
    }
    /**
     * ZhtoEn 中译英
     * @param: [productTitle, keyWord, keyPoints, productDescription]
     * @return: java.lang.String
     * @auther: wdh
     * @date: 2018/11/5 16:16
     */
    @RequestMapping("/entoOthers")
    public R entoOthers(@RequestBody IntroductionEntity introductionEn){
//    public List<IntroductionEntity> ZhtoEn(String productTitle,String keyWord,String keyPoints,String productDescription){

        IntroductionEntity introductionFra = EntoOther(introductionEn,"FRA");
        IntroductionEntity introductionDe = EntoOther(introductionEn,"DE");
        IntroductionEntity introductionIt = EntoOther(introductionEn,"IT");
        IntroductionEntity introductionSpa = EntoOther(introductionEn,"SPA");
        IntroductionEntity introductionJp = EntoOther(introductionEn,"JP");

        return R.ok().put("introductionEn",introductionEn)
                .put("introductionFra",introductionFra)
                .put("introductionDe",introductionDe)
                .put("introductionIt",introductionIt)
                .put("introductionSpa",introductionSpa)
                .put("introductionJp",introductionJp);
//        List<IntroductionEntity> list = new ArrayList<>();
//        list.add(introductionEn);
//        list.add(introductionFra);
//        list.add(introductionDe);
//        list.add(introductionIt);
//        list.add(introductionSpa);
//        list.add(introductionJp);
//        return list;
    }
    /**
     * toUpperCase 所有首字母转为大写
     * @param: [text]
     * @return: java.lang.String
     * @auther: wdh
     * @date: 2018/11/5 17:16
     */
    public String toUpperCase(String text) {
        if(text != null && !"".equals(text)){
            String[] strs = text.split(" ");
            StringBuilder sb = new StringBuilder();
            for (String strTmp : strs) {
                char[] ch = strTmp.toCharArray();
                if(ch.length>0){
                    if (ch[0] >= 'a' && ch[0] <= 'z') {
                        ch[0] = (char) (ch[0] - 32);
                    }
                    String strT = new String(ch);
                    sb.append(strT).append(" ");
                }
            }
            return sb.toString().trim();
        }
        return null;
    }
}