package io.renren.modules.product.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.common.annotation.SysLog;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.amazon.entity.AmazonCategoryHistoryEntity;
import io.renren.modules.amazon.entity.AmazonGrantEntity;
import io.renren.modules.amazon.entity.AmazonGrantShopEntity;
import io.renren.modules.amazon.service.AmazonCategoryHistoryService;
import io.renren.modules.amazon.service.AmazonGrantShopService;
import io.renren.modules.amazon.util.COUNTY;
import io.renren.modules.job.entity.ScheduleJobEntity;
import io.renren.modules.job.service.ScheduleJobService;
import io.renren.modules.product.entity.AmazonCategoryEntity;
import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.service.AmazonCategoryService;
import io.renren.modules.product.service.ProductsService;
import io.renren.modules.product.vm.AddUploadVM;
import io.renren.modules.sys.controller.AbstractController;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.product.entity.UploadEntity;
import io.renren.modules.product.service.UploadService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;

import javax.xml.crypto.Data;


/**
 * 产品上传
 *
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
@RestController
@RequestMapping("product/upload")
public class UploadController extends AbstractController {
    @Autowired
    private UploadService uploadService;

    @Autowired
    private ProductsService productsService;

    @Autowired
    private AmazonCategoryHistoryService amazonCategoryHistoryService;

    @Autowired
    private AmazonGrantShopService amazonGrantShopService;

    @Autowired
    private AmazonCategoryService amazonCategoryService;

    @Autowired
    private ScheduleJobService scheduleJobService;

    //英国
    private static final int GBUTC =0;
    // 美国
    private static final int USUTC =+5;
    //德国
    private static final int DEUTC =-1;
    //法国
    private static final int FRUTC =-1;
    //意大利
    private static final int ITUTC =-1;
    //墨西哥
    private static final int MXUTC =+6;
    //西班牙
    private static final int ESUTC =-1;
    //加拿大
    private static final int CAUTC =+5;

    /**
     * @methodname: 我的上传列表
     * @param: [params] 接受参数
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/getMyUploadList")
//    @RequiresPermissions("product:upload:mylist")
    public R getMyUploadList(@RequestParam Map<String, Object> params){
        params.put("userId",getUserId());
        PageUtils page = uploadService.queryMyUploadPage(params);
        return R.ok().put("page", page);
    }
    /**
     * @methodname: 所有上传列表
     * @param: [params] 接受参数
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/getAllUploadList")
//    @RequiresPermissions("product:upload:alllist")
    public R getAllUploadList(@RequestParam Map<String, Object> params){
        params.put("deptId",getDeptId());
        PageUtils page = uploadService.queryAllUploadPage(params);
        return R.ok().put("page", page);
    }

    /**
     * @methodname: info 信息
     * @param: [uploadId] 下载id
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/info/{uploadId}")
    @RequiresPermissions("product:upload:info")
    public R info(@PathVariable("uploadId") Long uploadId){
        UploadEntity upload = uploadService.selectById(uploadId);

        return R.ok().put("upload", upload);
    }

    /**
     * @methodname: save 保存
     * @param: [upload] 下载实体
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:upload:save")
    public R save(@RequestBody UploadEntity upload){
        uploadService.insert(upload);

        return R.ok();
    }

    /**
     * @methodname: update 更新
     * @param: [upload] 下载实体
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:upload:update")
    public R update(@RequestBody UploadEntity upload){
        ValidatorUtils.validateEntity(upload);
        uploadService.updateAllColumnById(upload);//全部更新
        
        return R.ok();
    }

    /**
     * @methodname: delete 删除
     * @param: [uploadIds] 下载id数组
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:upload:delete")
    public R delete(@RequestBody Long[] uploadIds){
        uploadService.deleteBatchIds(Arrays.asList(uploadIds));

        return R.ok();
    }

    /**
     * addUpload:立即上传
     * @param: [addUploadVM]
     *
     * @return: io.renren.common.utils.R
     * @auther: wdh
     * @date: 2018/11/27 16:17
     */
    @RequestMapping("/addUpload")
//    @RequiresPermissions("product:upload:addupload")
    public R addUpload(@RequestBody AddUploadVM addUploadVM){
        List<UploadEntity> uploadList = new ArrayList<UploadEntity>();
        Set<Long> ret = new LinkedHashSet<>(0);
        if(addUploadVM.getStartId() != null && addUploadVM.getEndId() != null){
            Long index = addUploadVM.getStartId();
            while (index <= addUploadVM.getEndId()){
                ret.add(index);
                index++;
            }
        }
        if(addUploadVM.getUploadIds() != null){
            ret.addAll(Arrays.asList(addUploadVM.getUploadIds()));
        }
        System.out.println("ret:" + ret);
        //迭代
        Iterator i = ret.iterator();
        //遍历
        while(i.hasNext()){
            UploadEntity upload = new UploadEntity();
            //获取产品
            ProductsEntity product = productsService.selectById(Long.valueOf(i.next().toString()));
            //设置产品id
            upload.setProductId(product.getProductId());
            //设置主图片
            upload.setMainUrl(product.getMainImageUrl());
            //设置授权账户
            AmazonGrantShopEntity amazonGrantShop = amazonGrantShopService.selectById(addUploadVM.getGrantShopId());
            upload.setGrantShopId(addUploadVM.getGrantShopId());
            upload.setGrantShop(addUploadVM.getGrantShop());
            //设置分类
            AmazonCategoryEntity amazonCategory = amazonCategoryService.selectById(addUploadVM.getAmazonCategoryId());
            upload.setAmazonCategoryId(addUploadVM.getAmazonCategoryId());
            upload.setAmazonCategory(addUploadVM.getAmazonCategory());
            //设置分类节点id
            String county = amazonGrantShop.getCountryCode();
            COUNTY countyEnum = COUNTY.valueOf(county.toUpperCase());
            switch (countyEnum){
                case GB:
                    upload.setAmazonCategoryNodeId(amazonCategory.getNodeIdUk());
                    break;
                case DE:
                    upload.setAmazonCategoryNodeId(amazonCategory.getNodeIdDe());
                    break;
                case FR:
                    upload.setAmazonCategoryNodeId(amazonCategory.getNodeIdFr());
                    break;
                case IT:
                    upload.setAmazonCategoryNodeId(amazonCategory.getNodeIdIt());
                    break;
                case ES:
                    upload.setAmazonCategoryNodeId(amazonCategory.getNodeIdEs());
                    break;
                // TODO: 2018/11/28 北美
                default:
                    break;
            }
            //设置模板
            upload.setAmazonTemplateId(addUploadVM.getAmazonTemplateId());
            upload.setAmazonTemplate(addUploadVM.getAmazonTemplate());
            //设置操作类型（0：上传   1：修改）
            upload.setOperateType(0);
            //数组转','号隔开的字符串
            String operateItem = StringUtils.join(addUploadVM.getOperateItem(),",");
            //设置操作项
            upload.setOperateItem(operateItem);
            //设置是否有分类属性
            upload.setIsAttribute(addUploadVM.getIsAttribute());
            // TODO: 2018/11/27 分类属性
            //设置状态(0：正在上传1：上传成功2：上传失败)
            upload.setUploadState(0);
            //设置常用属性
            upload.setUploadTime(new Date());
            upload.setUpdateTime(new Date());
            upload.setUserId(getUserId());
            upload.setDeptId(getDeptId());
            //添加到list
            uploadList.add(upload);
        }
        //批量添加到上传表
        uploadService.insertBatch(uploadList);
        //添加到分类历史记录表
        AmazonCategoryHistoryEntity categoryHistory = amazonCategoryHistoryService.selectByAmazonCategoryId(addUploadVM.getAmazonCategoryId());
        //如果有历史数据，则累加数量1
        if(categoryHistory != null){
            int count = categoryHistory.getCount() + 1;
            categoryHistory.setCount(count);
            amazonCategoryHistoryService.updateAllColumnById(categoryHistory);
        }else{
            //如果没有历史数据，则新增历史数据
            AmazonCategoryHistoryEntity categoryHistoryNew = new AmazonCategoryHistoryEntity();
            categoryHistoryNew.setAmazonCategoryId(addUploadVM.getAmazonCategoryId());
            categoryHistoryNew.setAmazonCategory(addUploadVM.getAmazonCategory());
            categoryHistoryNew.setCount(1);
            categoryHistoryNew.setUserId(getUserId());
            categoryHistoryNew.setDeptId(getDeptId());
            amazonCategoryHistoryService.insert(categoryHistoryNew);
        }
        return R.ok();
    }


    /**
     * @methodname: saveTimingUpload 创建定时上传产品任务任务
     * @param: [addUploadVM] 前台接受参数的实体类
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/12/3 16:08
     */
    @RequestMapping("/saveTimingUpload")
    //@RequiresPermissions("sys:schedule:save")
    //@RequestBody AddUploadVM addUploadVM
    public R saveTimingUpload(@RequestBody AddUploadVM addUploadVM) {
        //创建定时任务的实体
        ScheduleJobEntity scheduleJob = new ScheduleJobEntity();
        //设置为spring bean的名称
        scheduleJob.setBeanName("timingUpload");
        //设置方法名
        scheduleJob.setMethodName("timingUpload");

        //获取当前的用户和用户id和公司id
        addUploadVM.setUser(getUser());
        addUploadVM.setUserId(getUserId());
        addUploadVM.setDeptId(getDeptId());

        //把addUploadVM实体转换成json字符串
        ObjectMapper mapper = new ObjectMapper();
        String addUploadVMString = null;
        try {
            addUploadVMString = mapper.writeValueAsString(addUploadVM);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //设置参数
        scheduleJob.setParams(addUploadVMString);
        //把前台获取到的字符串时间转成时间格式
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = addUploadVM.getTime();
        //String time="2018-12-03 15:35:00";
        Date date = null;
        try {
            date = formatDate.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获取到的日期转成需要的cron表达式字符串格式
        SimpleDateFormat formatString = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
        String stringDate = formatString.format(date);
        //设置cron时间格式
        scheduleJob.setCronExpression(stringDate);
        scheduleJobService.save(scheduleJob);
        return R.ok();
    }

    /**
     * @methodname: timingUpload 定时上传
     * @param: [addUploadVM]
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/12/4 14:54
     */
    @RequestMapping
    //@RequiresPermissions("product:upload:addupload")
    public R timingUpload(@RequestBody AddUploadVM addUploadVM) {
        List<UploadEntity> uploadList = new ArrayList<UploadEntity>();
        Set<Long> ret = new LinkedHashSet<>(0);
        if (addUploadVM.getStartId() != null && addUploadVM.getEndId() != null) {
            Long index = addUploadVM.getStartId();
            while (index <= addUploadVM.getEndId()) {
                ret.add(index);
                index++;
            }
        }
        if (addUploadVM.getUploadIds() != null) {
            ret.addAll(Arrays.asList(addUploadVM.getUploadIds()));
        }
        System.out.println("ret:" + ret);
        //迭代
        Iterator i = ret.iterator();
        //遍历
        while (i.hasNext()) {
            UploadEntity upload = new UploadEntity();
            //获取产品
            ProductsEntity product = productsService.selectById(Long.valueOf(i.next().toString()));
            //设置产品id
            upload.setProductId(product.getProductId());
            //设置主图片
            upload.setMainUrl(product.getMainImageUrl());
            //设置授权账户
            AmazonGrantShopEntity amazonGrantShop = amazonGrantShopService.selectById(addUploadVM.getGrantShopId());
            upload.setGrantShopId(addUploadVM.getGrantShopId());
            upload.setGrantShop(addUploadVM.getGrantShop());
            //设置分类
            AmazonCategoryEntity amazonCategory = amazonCategoryService.selectById(addUploadVM.getAmazonCategoryId());
            upload.setAmazonCategoryId(addUploadVM.getAmazonCategoryId());
            upload.setAmazonCategory(addUploadVM.getAmazonCategory());
            //设置分类节点id
            String county = amazonGrantShop.getCountryCode();
            COUNTY countyEnum = COUNTY.valueOf(county.toUpperCase());
            switch (countyEnum) {
                case GB:
                    upload.setAmazonCategoryNodeId(amazonCategory.getNodeIdUk());
                    break;
                case DE:
                    upload.setAmazonCategoryNodeId(amazonCategory.getNodeIdDe());
                    break;
                case FR:
                    upload.setAmazonCategoryNodeId(amazonCategory.getNodeIdFr());
                    break;
                case IT:
                    upload.setAmazonCategoryNodeId(amazonCategory.getNodeIdIt());
                    break;
                case ES:
                    upload.setAmazonCategoryNodeId(amazonCategory.getNodeIdEs());
                    break;
                // TODO: 2018/11/28 北美
                default:
                    break;
            }
            //设置模板
            upload.setAmazonTemplateId(addUploadVM.getAmazonTemplateId());
            upload.setAmazonTemplate(addUploadVM.getAmazonTemplate());
            //设置操作类型（0：上传   1：修改）
            upload.setOperateType(0);
            //数组转','号隔开的字符串
            String operateItem = StringUtils.join(addUploadVM.getOperateItem(), ",");
            //设置操作项
            upload.setOperateItem(operateItem);
            //设置是否有分类属性
            upload.setIsAttribute(addUploadVM.getIsAttribute());
            // TODO: 2018/11/27 分类属性
            //设置状态(0：正在上传1：上传成功2：上传失败)
            upload.setUploadState(0);
            //设置常用属性
            upload.setUploadTime(new Date());
            upload.setUpdateTime(new Date());
            upload.setUserId(addUploadVM.getUserId());
            upload.setDeptId(addUploadVM.getDeptId());
            //添加到list
            uploadList.add(upload);
        }
        //批量添加到上传表
        uploadService.insertBatch(uploadList);
        //添加到分类历史记录表
        AmazonCategoryHistoryEntity categoryHistory = amazonCategoryHistoryService.selectByAmazonCategoryId(addUploadVM.getAmazonCategoryId());
        //如果有历史数据，则累加数量1
        if (categoryHistory != null) {
            int count = categoryHistory.getCount() + 1;
            categoryHistory.setCount(count);
            amazonCategoryHistoryService.updateAllColumnById(categoryHistory);
        } else {
            //如果没有历史数据，则新增历史数据
            AmazonCategoryHistoryEntity categoryHistoryNew = new AmazonCategoryHistoryEntity();
            categoryHistoryNew.setAmazonCategoryId(addUploadVM.getAmazonCategoryId());
            categoryHistoryNew.setAmazonCategory(addUploadVM.getAmazonCategory());
            categoryHistoryNew.setCount(1);
            categoryHistoryNew.setUserId(addUploadVM.getUserId());
            categoryHistoryNew.setDeptId(addUploadVM.getDeptId());
            amazonCategoryHistoryService.insert(categoryHistoryNew);
        }
        return R.ok();
    }

    /**
     * @methodname: timeZoneConversion 国家时区转换
     * @param: [countryCode, countryTime]
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/12/5 16:59
     */
    @RequestMapping("/timeZoneConversion")
    public R timeZoneConversion(String countryCode, String countryTime) {
        //国家编码转成大写
        COUNTY countyEnum = COUNTY.valueOf(countryCode.toUpperCase());
        String CNTimeString=null;
        int CNTime=0;
        switch (countyEnum) {
            //英国
            case GB:
                CNTime=8+GBUTC;
                CNTimeString = addDateMinut(countryTime, CNTime);
                break;
            // 美国
            case US:
                CNTime=8+USUTC;
                CNTimeString = addDateMinut(countryTime, CNTime);
                break;
            //德国
            case DE:
                CNTime=8+DEUTC;
                CNTimeString = addDateMinut(countryTime, CNTime);
                break;
            //法国
            case FR:
                CNTime=8+FRUTC;
                CNTimeString = addDateMinut(countryTime, CNTime);
                break;
            //意大利
            case IT:
                CNTime=8+ITUTC;
                CNTimeString = addDateMinut(countryTime, CNTime);
                break;
            //墨西哥
            case MX:
                CNTime=8+MXUTC;
                CNTimeString = addDateMinut(countryTime, CNTime);
                break;

            //西班牙
            case ES:
                CNTime=8+ESUTC;
                CNTimeString = addDateMinut(countryTime, CNTime);
                break;
            //加拿大
            case CA:
                CNTime=8+CAUTC;
                CNTimeString = addDateMinut(countryTime, CNTime);
                break;
            default:
                break;
        }
        return R.ok().put("CNTimeString",CNTimeString);
    }


    //时间的计算
    public static String addDateMinut(String day, int hour){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null){
            return "";
        }
        //获取日历
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);// 24小时制
        date = cal.getTime();
        cal = null;
        return format.format(date);

    }
}
