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
import io.renren.modules.util.TranslateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


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
        //ValidatorUtils.validateEntity((introduction);
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
     * ZhtoOther 中译其他
     * @param: introductionZh
     * @return: java.lang.String
     * @auther: wdh
     * @date: 2018/11/5 16:16
     */
    @RequestMapping("/ZhtoOther")
    public R ZhtoOther(@RequestBody IntroductionEntity introductionZh){
//    public List<IntroductionEntity> ZhtoEn(String productTitle,String keyWord,String keyPoints,String productDescription){
        String title = introductionZh.getProductTitle();
        String keyWord = introductionZh.getKeyWord();
        String keyPoint = "";
        if (StringUtils.isNotBlank(introductionZh.getKeyPoints())){
            keyPoint = introductionZh.getKeyPoints().replace("\n"," !!!!! ");;
        }
        String productDescription = "";
        if (StringUtils.isNotBlank(introductionZh.getProductDescription())){
            productDescription = introductionZh.getProductDescription().replace("\n"," !!!!! ");
        }
        StringBuffer strBuf = new StringBuffer();
        if(StringUtils.isNotBlank(title)){
            strBuf.append(title);
            strBuf.append(" ===== ");
        }else{
            strBuf.append(" ===== ");
        }
        if(StringUtils.isNotBlank(keyWord)){
            strBuf.append(keyWord);
            strBuf.append(" ===== ");
        }else{
            strBuf.append("  ===== ");
        }
        if(StringUtils.isNotBlank(keyPoint)){
            strBuf.append(keyPoint);
            strBuf.append(" ===== ");
        }else{
            strBuf.append("  ===== ");
        }
        if(StringUtils.isNotBlank(productDescription)){
            strBuf.append(productDescription);
        }else{
            strBuf.append(" ");
        }
        String tranStr = strBuf.toString();
        if(StringUtils.isNotBlank(tranStr)){
            Querier<AbstractTranslator> querierTrans = new Querier<>();
            querierTrans.attach(new GoogleTranslator());
            Map<String, Object> map = ZhtoEn(querierTrans, tranStr);
            String msg = (String) map.get("msg");
            if (StringUtils.isBlank(msg)){
                IntroductionEntity introductionEn = (IntroductionEntity)map.get("introduction");
                String tranEnStr = (String)map.get("tranEnStr");
                IntroductionEntity introductionFra = EntoOther(querierTrans, tranEnStr,"FRA");
                IntroductionEntity introductionDe = EntoOther(querierTrans, tranEnStr,"DE");
                IntroductionEntity introductionIt = EntoOther(querierTrans, tranEnStr,"IT");
                IntroductionEntity introductionSpa = EntoOther(querierTrans, tranEnStr,"SPA");
                IntroductionEntity introductionJp = EntoOther(querierTrans, tranEnStr,"JP");
                StringBuffer errorBuf = new StringBuffer();
                if(StringUtils.isNotBlank(introductionEn.getMsg())){
                    errorBuf.append(introductionEn.getMsg());
                    errorBuf.append("\n");
                }
                if(StringUtils.isNotBlank(introductionFra.getMsg())){
                    errorBuf.append(introductionFra.getMsg());
                    errorBuf.append("\n");
                }
                if(StringUtils.isNotBlank(introductionDe.getMsg())){
                    errorBuf.append(introductionDe.getMsg());
                    errorBuf.append("\n");
                }
                if(StringUtils.isNotBlank(introductionIt.getMsg())){
                    errorBuf.append(introductionIt.getMsg());
                    errorBuf.append("\n");
                }
                if(StringUtils.isNotBlank(introductionSpa.getMsg())){
                    errorBuf.append(introductionSpa.getMsg());
                    errorBuf.append("\n");
                }
                if(StringUtils.isNotBlank(introductionJp.getMsg())){
                    errorBuf.append(introductionJp.getMsg());
                    errorBuf.append("\n");
                }
                String error = "";
                if(StringUtils.isNotBlank(errorBuf.toString())){
                    error = errorBuf.toString().substring(0,strBuf.toString().length()-1);
                }
                return R.ok().put("introductionEn",introductionEn)
                        .put("introductionFra",introductionFra)
                        .put("introductionDe",introductionDe)
                        .put("introductionIt",introductionIt)
                        .put("introductionSpa",introductionSpa)
                        .put("introductionJp",introductionJp)
                        .put("error",error);
            }else{
                return R.error(msg);
            }
        }

        return R.error("请输入内容后再进行翻译");
    }

    /**
     * entoOthers 英译其他
     * @param: [productTitle, keyWord, keyPoints, productDescription]
     * @return: java.lang.String
     * @auther: wdh
     * @date: 2018/11/5 16:16
     */
    @RequestMapping("/entoOthers")
    public R entoOthers(@RequestBody IntroductionEntity introductionEn){
//    public List<IntroductionEntity> ZhtoEn(String productTitle,String keyWord,String keyPoints,String productDescription){
        Querier<AbstractTranslator> querierTrans = new Querier<>();
        querierTrans.attach(new GoogleTranslator());
        String title = introductionEn.getProductTitle();
        String keyWord = introductionEn.getKeyWord();
        String keyPoint = introductionEn.getKeyPoints().replace("\n"," !!!!! ");;
        String productDescription = introductionEn.getProductDescription().replace("\n"," !!!!! ");
        StringBuffer strBuf = new StringBuffer();
        if(StringUtils.isNotBlank(title)){
            strBuf.append(title);
            strBuf.append(" ===== ");
        }else{
            strBuf.append(" ===== ");
        }
        if(StringUtils.isNotBlank(keyWord)){
            strBuf.append(keyWord);
            strBuf.append(" ===== ");
        }else{
            strBuf.append("  ===== ");
        }
        if(StringUtils.isNotBlank(keyPoint)){
            strBuf.append(keyPoint);
            strBuf.append(" ===== ");
        }else{
            strBuf.append("  ===== ");
        }
        if(StringUtils.isNotBlank(productDescription)){
            strBuf.append(productDescription);
        }else{
            strBuf.append(" ");
        }
        String tranStr = strBuf.toString();
        IntroductionEntity introductionFra = EntoOther(querierTrans, tranStr, "FRA");
        IntroductionEntity introductionDe = EntoOther(querierTrans, tranStr,"DE");
        IntroductionEntity introductionIt = EntoOther(querierTrans, tranStr,"IT");
        IntroductionEntity introductionSpa = EntoOther(querierTrans, tranStr,"SPA");
        IntroductionEntity introductionJp = EntoOther(querierTrans, tranStr,"JP");
        StringBuffer errorBuf = new StringBuffer();
        if(StringUtils.isNotBlank(introductionFra.getMsg())){
            errorBuf.append(introductionFra.getMsg());
            errorBuf.append("\n");
        }
        if(StringUtils.isNotBlank(introductionDe.getMsg())){
            errorBuf.append(introductionDe.getMsg());
            errorBuf.append("\n");
        }
        if(StringUtils.isNotBlank(introductionIt.getMsg())){
            errorBuf.append(introductionIt.getMsg());
            errorBuf.append("\n");
        }
        if(StringUtils.isNotBlank(introductionSpa.getMsg())){
            errorBuf.append(introductionSpa.getMsg());
            errorBuf.append("\n");
        }
        if(StringUtils.isNotBlank(introductionJp.getMsg())){
            errorBuf.append(introductionJp.getMsg());
            errorBuf.append("\n");
        }
        String error = "";
        if(StringUtils.isNotBlank(errorBuf.toString())){
            error = errorBuf.toString().substring(0,strBuf.toString().length()-1);
        }
        return R.ok().put("introductionFra",introductionFra)
                .put("introductionDe",introductionDe)
                .put("introductionIt",introductionIt)
                .put("introductionSpa",introductionSpa)
                .put("introductionJp",introductionJp)
                .put("error",error);
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
     * ZhtoOther 中译英
     * @param: tranStr
     * @return: Map<String, Object>
     * @auther: wdh
     * @date: 2018/11/5 16:16
     */
    private Map<String, Object> ZhtoEn(Querier<AbstractTranslator> querierTrans, String tranStr){
        Map<String, Object> map = new HashMap<String, Object>();
        IntroductionEntity introductionEn = new IntroductionEntity();
        //翻译
        querierTrans.setParams(LANG.ZH, LANG.EN, tranStr);
        List<String> resultList = querierTrans.execute();
        if (resultList.get(0) != null &&resultList.get(0) != ""){

            String result = resultList.get(0);
            String[] results = result.split("=====");
            try{
                String title = TranslateUtils.toUpperCase(results[0].replace(","," "));
                introductionEn.setProductTitle(title);
                String keyWord = TranslateUtils.toUpperCase(results[1].replace(","," "));
                introductionEn.setKeyWord(keyWord);
                introductionEn.setKeyPoints(results[2].replace(" !!!!! ","\n"));
                String productDescription = results[3].replace(" !!!!! ","\n");
                introductionEn.setProductDescription(productDescription);
                introductionEn.setCountry("EN");
                map.put("querierTrans",querierTrans);
                map.put("introduction",introductionEn);
                map.put("tranEnStr",result);
                StringBuffer errorStr = new StringBuffer();;
                //判断标题
                if(title.length() >200){
                    errorStr.append("翻译失败，英文标题超过200字符");
                    errorStr.append("\n");
                }
                if(keyWord.length() >250){
                    errorStr.append("翻译失败，英文关键字超过250字符");
                    errorStr.append("\n");
                }
                String[] keyPoints = results[2].split(" !!!!! ");
                for(int i = 0; i < keyPoints.length; i++){
                    if(keyPoints[i].length() >1000){
                        errorStr.append("翻译失败，英文要点说明第" + i + "行超过1000字符");
                        errorStr.append("\n");
                    }
                }
                if(productDescription.length() >2000){
                    errorStr.append("翻译失败，英文产品描述超过2000字符");
                    errorStr.append("\n");
                }
                if(StringUtils.isNotBlank(errorStr.toString())){
                    map.put("error",errorStr.toString().substring(0,errorStr.toString().length()-1));
                }
                return map;
            }catch (Exception e){
                map.put("msg","翻译失败，请重新尝试");
                return map;
            }
        }else{
            map.put("msg","翻译失败，请重新尝试");
            return map;
        }
    }

    /**
     * EntoOther 英译其他语言
     * @param: [introduction]
     * @return: io.renren.modules.product.entity.IntroductionEntity
     * @auther: wdh
     * @date: 2018/11/6 15:33
     */
    private IntroductionEntity EntoOther(Querier<AbstractTranslator> querierTrans, String tranEnStr, String country){
//        Querier<AbstractTranslator> querierTrans = new Querier<>();
        switch (country){
            //法国
            case "FRA":
                querierTrans.setParams(LANG.EN, LANG.FRA, tranEnStr);
                break;
            //德国
            case "DE":
                querierTrans.setParams(LANG.EN, LANG.DE, tranEnStr);
                break;
            //意大利
            case "IT":
                querierTrans.setParams(LANG.EN, LANG.IT, tranEnStr);
                break;
            //西班牙
            case "SPA":
                querierTrans.setParams(LANG.EN, LANG.SPA, tranEnStr);
                break;
            //日本
            case "JP":
                querierTrans.setParams(LANG.EN, LANG.JP, tranEnStr);
                break;
            default:
                break;
        }
//        querierTrans.attach(new GoogleTranslator());
//        querierTrans.attach(new BaiduTranslator());
//        querierTrans.attach(new YoudaoTranslator());
        IntroductionEntity introduction = new IntroductionEntity();
        //翻译
        List<String> resultList = querierTrans.execute();
        if (resultList.get(0) != null &&resultList.get(0) != ""){
            String result = resultList.get(0);
            String[] results = result.split("=====");
            try{
                String title = TranslateUtils.toUpperCase(results[0].replace(","," "));
                introduction.setProductTitle(title);
                String keyWord = TranslateUtils.toUpperCase(results[1].replace(","," "));
                introduction.setKeyWord(keyWord);
                introduction.setKeyPoints(results[2].replace(" !!!!! ","\n"));
                String productDescription = results[3].replace(" !!!!! ","\n");
                introduction.setProductDescription(productDescription);
                introduction.setCountry("EN");
                StringBuffer errorStr = new StringBuffer();
                //判断标题
                if(title.length() >200){
                    errorStr.append(country + "标题超过200字符");
                    errorStr.append("\n");
                }
                if(keyWord.length() >250){
                    errorStr.append(country + "关键字超过250字符");
                    errorStr.append("\n");
                }
                String[] keyPoints = results[2].split(" !!!!! ");
                for(int i = 0; i < keyPoints.length; i++){
                    if(keyPoints[i].length() >1000){
                        errorStr.append(country + "要点说明第" + i + "行超过1000字符");
                        errorStr.append("\n");
                    }
                }
                if(productDescription.length() >2000){
                    errorStr.append(country + "产品描述超过2000字符");
                    errorStr.append("\n");
                }
                if(StringUtils.isNotBlank(errorStr.toString())){
                    introduction.setMsg(errorStr.toString().substring(0,errorStr.toString().length()-1));
                }
            }catch (Exception e){
                introduction.setMsg(country + "语言翻译失败,请调试");
                return introduction;
            }
        }else{
            introduction.setMsg(country + "语言翻译失败,请调试");
            return introduction;
        }
//        else if(titleList.get(1) != "" && titleList.get(1) != null){
//            introduction.setProductTitle(TranslateUtils.toUpperCase(titleList.get(1)));
//        }else{
//            introduction.setProductTitle(TranslateUtils.toUpperCase(titleList.get(2)));
//        }

        /*//翻译关键字
        querierTrans.setText(introductionEn.getKeyWord());
        List<String> keyWordList = querierTrans.execute();
        if (keyWordList.get(0) != "" && keyWordList.get(0) != null){
            introduction.setKeyWord(TranslateUtils.toUpperCase(keyWordList.get(0)));
        }*/
//        else if(keyWordList.get(1) != "" && keyWordList.get(1) != null){
//            introduction.setKeyWord(TranslateUtils.toUpperCase(keyWordList.get(1)));
//        }else{
//            introduction.setKeyWord(TranslateUtils.toUpperCase(keyWordList.get(2)));
//        }

        //翻译要点说明
        /*querierTrans.setText(introductionEn.getKeyPoints());
        List<String> keyPointsList = querierTrans.execute();
        if (keyPointsList.get(0) != "" && keyPointsList.get(0) != null){
            introduction.setKeyPoints(keyPointsList.get(0));
        }*/
//        else if(keyPointsList.get(1) != "" && keyPointsList.get(1) != null){
//            introduction.setKeyPoints(keyPointsList.get(1));
//        }else{
//            introduction.setKeyPoints(keyPointsList.get(2));
//        }

        //翻译产品描述
        /*querierTrans.setText(introductionEn.getProductDescription());
        List<String> productDescriptionList = querierTrans.execute();
        if (productDescriptionList.get(0) != "" && productDescriptionList.get(0) != null){
            introduction.setProductDescription(productDescriptionList.get(0));
        }*/
//        else if(productDescriptionList.get(1) != "" && productDescriptionList.get(1) != null){
//            introduction.setKeyPoints(productDescriptionList.get(1));
//        }else{
//            introduction.setKeyPoints(productDescriptionList.get(2));
//        }
        introduction.setCountry(country);
        System.out.println("country:" + introduction.getCountry());
        System.out.println("title:" + introduction.getProductTitle());
        System.out.println("keyWord:" + introduction.getKeyWord());
        System.out.println("keyPoints:" + introduction.getKeyPoints());
        System.out.println("productDescription:" + introduction.getProductDescription());
        return introduction;
    }

}