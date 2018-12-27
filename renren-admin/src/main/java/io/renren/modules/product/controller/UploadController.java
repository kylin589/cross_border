package io.renren.modules.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.amazon.entity.AmazonCategoryHistoryEntity;
import io.renren.modules.amazon.entity.AmazonGrantShopEntity;
import io.renren.modules.amazon.service.AmazonCategoryHistoryService;
import io.renren.modules.amazon.service.AmazonGrantShopService;
import io.renren.modules.amazon.service.ResultXmlService;
import io.renren.modules.amazon.service.SubmitFeedService;
import io.renren.modules.amazon.util.COUNTY;
import io.renren.modules.job.entity.ScheduleJobEntity;
import io.renren.modules.job.service.ScheduleJobService;
import io.renren.modules.product.dto.DetailsDto;
import io.renren.modules.product.dto.TemplateFieldValueDto;
import io.renren.modules.product.dto.UploadProductDTO;
import io.renren.modules.product.entity.*;
import io.renren.modules.product.service.*;
import io.renren.modules.product.vm.AddUploadVM;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


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

    @Autowired
    private FieldMiddleService fieldMiddleService;

    @Autowired
    private SubmitFeedService submitFeedService;

    @Autowired
    private ResultXmlService resultXmlService;

    @Autowired
    private TemplateService templateService;

    //英国
    private static final int GBUTC = 0;
    // 美国
    private static final int USUTC = +5;
    //德国
    private static final int DEUTC = -1;
    //法国
    private static final int FRUTC = -1;
    //意大利
    private static final int ITUTC = -1;
    //墨西哥
    private static final int MXUTC = +6;
    //西班牙
    private static final int ESUTC = -1;
    //加拿大
    private static final int CAUTC = +5;

    /**
     * @methodname: 我的上传列表
     * @param: [params] 接受参数
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/11/8 21:22
     */
    @RequestMapping("/getMyUploadList")
//    @RequiresPermissions("product:upload:mylist")
    public R getMyUploadList(@RequestParam Map<String, Object> params) {
        params.put("userId", getUserId());
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
    public R getAllUploadList(@RequestParam Map<String, Object> params) {
        params.put("deptId", getDeptId());
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
    public R info(@PathVariable("uploadId") Long uploadId) {
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
    public R save(@RequestBody UploadEntity upload) {
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
    public R update(@RequestBody UploadEntity upload) {
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
    // @RequiresPermissions("product:upload:delete")
    public R delete(Long uploadId) {

        Map<String, Object> map = new HashMap<>();
        map.put("upload_id", uploadId);
        fieldMiddleService.deleteByMap(map);
        resultXmlService.deleteByMap(map);
        uploadService.deleteById(uploadId);

        return R.ok();
    }

    /**
     * 上传详情
     *
     * @param uploadId
     * @return
     */
    @RequestMapping("/details")
    public R details(Long uploadId) {

        DetailsDto detailsDto = new DetailsDto();

        UploadEntity uploadEntity = new UploadEntity();
        uploadEntity.setUploadId(uploadId);
        uploadEntity = uploadService.selectById(uploadEntity);

        // 字段和值
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("upload_id", uploadId);
        List<FieldMiddleEntity> middleEntitys = fieldMiddleService.selectByMap(fieldMap);

        for (int i = 0; i < middleEntitys.size(); i++) {
            Long fieldId = middleEntitys.get(i).getFieldId();
            System.out.println(fieldId);
            String countryCode = uploadEntity.getCountryCode();
            System.out.println(countryCode);
            List<TemplateFieldValueDto> templateFieldValueDtos = templateService.getTemplateFieldValueDtos(fieldId, countryCode);
            middleEntitys.get(i).setTemplateFieldValueDtos(templateFieldValueDtos);
        }

        // 获取所有分类
        Long id = uploadEntity.getAmazonCategoryId();
        String[] allId = amazonCategoryService.queryByChindIdParentId(id).split(",");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < allId.length; i++) {
            AmazonCategoryEntity amazonCategoryEntity = new AmazonCategoryEntity();
            amazonCategoryEntity.setAmazonCategoryId(Long.valueOf(allId[i]));
            amazonCategoryEntity = amazonCategoryService.selectById(amazonCategoryEntity);
            list.add(amazonCategoryEntity.getDisplayName());
        }
        String allCategories = StringUtils.join(list, "/");

        detailsDto.setUploadEntity(uploadEntity);
        detailsDto.setMiddleEntitys(middleEntitys);
        detailsDto.setAllCategories(allCategories);
        return R.ok().put("data", detailsDto);
    }

    /**
     * addUpload:立即上传
     *
     * @param: [addUploadVM]
     * @return: io.renren.common.utils.R
     * @auther: wdh
     * @date: 2018/11/27 16:17
     */
    @RequestMapping("/addUpload")
//    @RequiresPermissions("product:upload:addupload")
    public R addUpload(@RequestBody AddUploadVM addUploadVM) {
        UploadEntity uploadEntity = new UploadEntity();
        uploadEntity.setStartId(addUploadVM.getStartId());
        uploadEntity.setEndId(addUploadVM.getEndId());
        uploadEntity.setUploadIds(StringUtils.join(addUploadVM.getUploadIds(), ","));
        //ret:要上传的产品列表
        List<ProductsEntity> ret = new ArrayList<ProductsEntity>();
        //idList:要上传的id列表
        List<Long> idList = new ArrayList<Long>();
        if (addUploadVM.getUploadIds() != null) {
            UploadProductDTO dto1 = productsService.selectCanUploadProducts(Arrays.asList(addUploadVM.getUploadIds()), getUserId());
            if ("ok".equals(dto1.getCode())) {
                List productsList1 = dto1.getProductsList();
                ret.addAll(productsList1);
                idList.addAll(dto1.getRet());
            } else {
                return R.error(dto1.getMsg());
            }
        }
        ;
        List<Long> seList = new ArrayList<>();
        if (addUploadVM.getStartId() != null && addUploadVM.getEndId() != null) {
            Long index = addUploadVM.getStartId();
            while (index <= addUploadVM.getEndId()) {
                seList.add(index);
                index++;
            }
            UploadProductDTO dto2 = productsService.selectCanUploadProducts(seList, getUserId());
            if ("ok".equals(dto2.getCode())) {
                List productsList2 = dto2.getProductsList();
                ret.addAll(productsList2);
                idList.addAll(dto2.getRet());
            } else {
                return R.error(dto2.getMsg());
            }
        }
        uploadEntity.setUploadProductsList(ret);
        uploadEntity.setUploadProductsIds(StringUtils.join(idList.toArray(), ","));
        //获取分类对象
        AmazonCategoryEntity amazonCategory = amazonCategoryService.selectById(addUploadVM.getAmazonCategoryId());
        //设置授权账户
        AmazonGrantShopEntity amazonGrantShop = amazonGrantShopService.selectById(addUploadVM.getGrantShopId());
        uploadEntity.setGrantShopId(addUploadVM.getGrantShopId());
        uploadEntity.setGrantShop(addUploadVM.getGrantShop());
        //设置分类
        uploadEntity.setAmazonCategoryId(addUploadVM.getAmazonCategoryId());
        uploadEntity.setAmazonCategory(amazonCategory.getDisplayName());
        //设置分类节点id
        String countryCode = amazonGrantShop.getCountryCode();
        uploadEntity.setCountryCode(countryCode);
        uploadEntity.setAmazonCategoryNodeId(addUploadVM.getAmazonNodeId());
       /*
        COUNTY countyEnum = COUNTY.valueOf(countryCode.toUpperCase());
        switch (countyEnum) {
            case GB:
                uploadEntity.setAmazonCategoryNodeId(amazonCategory.getNodeIdUk());
                break;
            case DE:
                uploadEntity.setAmazonCategoryNodeId(amazonCategory.getNodeIdDe());
                break;
            case FR:
                uploadEntity.setAmazonCategoryNodeId(amazonCategory.getNodeIdFr());
                break;
            case IT:
                uploadEntity.setAmazonCategoryNodeId(amazonCategory.getNodeIdIt());
                break;
            case ES:
                uploadEntity.setAmazonCategoryNodeId(amazonCategory.getNodeIdEs());
                break;
            case US:
                uploadEntity.setAmazonCategoryNodeId(amazonCategory.getNodeIdUs());
                break;
            case MX:
                uploadEntity.setAmazonCategoryNodeId(amazonCategory.getNodeIdMx());
                break;
            case CA:
                uploadEntity.setAmazonCategoryNodeId(amazonCategory.getNodeIdCa());
                break;
            case AU:
                uploadEntity.setAmazonCategoryNodeId(amazonCategory.getNodeIdAu());
                break;
            case JP:
                uploadEntity.setAmazonCategoryNodeId(amazonCategory.getNodeIdJp());
                break;
            default:
                break;
        }*/

        //设置模板
        uploadEntity.setAmazonTemplateId(addUploadVM.getAmazonTemplateId());
        uploadEntity.setAmazonTemplate(addUploadVM.getAmazonTemplate());
        //设置操作类型（0：上传;1：修改）
        uploadEntity.setOperateType(0);
        //数组转','号隔开的字符串
        String operateItem = StringUtils.join(addUploadVM.getOperateItem(), ",");
        //设置操作项
        uploadEntity.setOperateItem(operateItem);
        //设置常用属性
        uploadEntity.setUploadTime(new Date());
        uploadEntity.setUpdateTime(new Date());
        uploadEntity.setUserId(getUserId());
        uploadEntity.setDeptId(getDeptId());
        uploadEntity.setUploadState(0);
        //添加到上传表
        uploadService.insert(uploadEntity);
        //设置是否有分类属性
        // uploadEntity.setIsAttribute(addUploadVM.getIsAttribute());

        List<TemplateCategoryFieldsEntity> fieldsEntityList = addUploadVM.getFieldsEntityList();
        for (int i = 0; i < fieldsEntityList.size(); i++) {
            FieldMiddleEntity middleEntity = new FieldMiddleEntity();
            middleEntity.setUploadId(uploadEntity.getUploadId());
            middleEntity.setFieldId(fieldsEntityList.get(i).getFieldId());
            middleEntity.setFieldName(fieldsEntityList.get(i).getFieldName());
            middleEntity.setFieldDisplayName(fieldsEntityList.get(i).getFieldDisplayName());
            middleEntity.setValue(fieldsEntityList.get(i).getValue());
            middleEntity.setIsCustom(fieldsEntityList.get(i).getIsCustom());
            fieldMiddleService.insert(middleEntity);
        }

        //添加到分类历史记录表
        AmazonCategoryHistoryEntity categoryHistory = amazonCategoryHistoryService.selectByAmazonCategoryId(addUploadVM.getAmazonCategoryId(), countryCode);
        //如果有历史数据，则累加数量1
        if (categoryHistory != null) {
            int count = categoryHistory.getCount() + 1;
            categoryHistory.setCount(count);
            amazonCategoryHistoryService.updateAllColumnById(categoryHistory);
        } else {
            //如果没有历史数据，则新增历史数据
            AmazonCategoryHistoryEntity categoryHistoryNew = new AmazonCategoryHistoryEntity();
            categoryHistoryNew.setAmazonCategoryId(addUploadVM.getAmazonCategoryId());
            categoryHistoryNew.setAmazonCategory(amazonCategory.getDisplayName());
            categoryHistoryNew.setAmazonAllCategory(addUploadVM.getAmazonCategory());
            categoryHistoryNew.setCount(1);
            categoryHistoryNew.setCountryCode(countryCode);
            categoryHistoryNew.setNodeId(addUploadVM.getAmazonNodeId());
            categoryHistoryNew.setUserId(getUserId());
            categoryHistoryNew.setDeptId(getDeptId());
            amazonCategoryHistoryService.insert(categoryHistoryNew);
        }
        submitFeedService.submitFeed(uploadEntity);
        return R.ok();
    }

    /**
     * 表单重新提交
     */
    @RequestMapping("/againUploadByForm")
    public R againUploadByForm(@RequestBody AddUploadVM addUploadVM) {

        // 查找基本信息
        UploadEntity uploadEntity = new UploadEntity();
        uploadEntity.setUploadId(addUploadVM.getUploadId());
        System.out.println("addUploadVM.getUploadId()：" + addUploadVM.getUploadId());
        uploadEntity = uploadService.selectById(uploadEntity);
        uploadEntity.setUploadState(0);
        uploadService.updateById(uploadEntity);
        // 删除xml报告
        Map<String, Object> map = new HashMap<>();
        map.put("upload_id", addUploadVM.getUploadId());
        resultXmlService.deleteByMap(map);
        fieldMiddleService.deleteByMap(map);

        //获取分类对象
        AmazonCategoryEntity amazonCategory = amazonCategoryService.selectById(addUploadVM.getAmazonCategoryId());

        String countryCode = uploadEntity.getCountryCode();
        uploadEntity.setCountryCode(countryCode);
        uploadEntity.setAmazonCategoryNodeId(addUploadVM.getAmazonNodeId());

        //设置模板
        uploadEntity.setAmazonTemplateId(addUploadVM.getAmazonTemplateId());
        uploadEntity.setAmazonTemplate(addUploadVM.getAmazonTemplate());
        //设置操作类型（0：上传;1：修改）
        uploadEntity.setOperateType(0);
        //数组转','号隔开的字符串
        String operateItem = StringUtils.join(addUploadVM.getOperateItem(), ",");
        //设置操作项
        uploadEntity.setOperateItem(operateItem);
        //设置常用属性
        uploadEntity.setUploadTime(new Date());
        uploadEntity.setUpdateTime(new Date());
        uploadEntity.setUserId(getUserId());
        uploadEntity.setDeptId(getDeptId());
        uploadEntity.setUploadState(0);
        //添加到上传表
        uploadService.insert(uploadEntity);

        //获取需要上传的商品id
        List<String> ids = Arrays.asList(uploadEntity.getUploadProductsIds().split(","));
        uploadEntity.setUploadProductsList(productsService.selectBatchIds(ids));

        List<TemplateCategoryFieldsEntity> fieldsEntityList = addUploadVM.getFieldsEntityList();
        for (int i = 0; i < fieldsEntityList.size(); i++) {
            FieldMiddleEntity middleEntity = new FieldMiddleEntity();
            middleEntity.setUploadId(uploadEntity.getUploadId());
            middleEntity.setFieldId(fieldsEntityList.get(i).getFieldId());
            middleEntity.setFieldName(fieldsEntityList.get(i).getFieldName());
            middleEntity.setFieldDisplayName(fieldsEntityList.get(i).getFieldDisplayName());
            middleEntity.setValue(fieldsEntityList.get(i).getValue());
            middleEntity.setIsCustom(fieldsEntityList.get(i).getIsCustom());
            fieldMiddleService.insert(middleEntity);
        }

        //添加到分类历史记录表
        AmazonCategoryHistoryEntity categoryHistory = amazonCategoryHistoryService.selectByAmazonCategoryId(addUploadVM.getAmazonCategoryId(), countryCode);
        //如果有历史数据，则累加数量1
        if (categoryHistory != null) {
            int count = categoryHistory.getCount() + 1;
            categoryHistory.setCount(count);
            amazonCategoryHistoryService.updateAllColumnById(categoryHistory);
        } else {
            //如果没有历史数据，则新增历史数据
            AmazonCategoryHistoryEntity categoryHistoryNew = new AmazonCategoryHistoryEntity();
            categoryHistoryNew.setAmazonCategoryId(addUploadVM.getAmazonCategoryId());
            categoryHistoryNew.setAmazonCategory(amazonCategory.getDisplayName());
            categoryHistoryNew.setAmazonAllCategory(addUploadVM.getAmazonCategory());
            categoryHistoryNew.setCount(1);
            categoryHistoryNew.setCountryCode(countryCode);
            categoryHistoryNew.setNodeId(addUploadVM.getAmazonNodeId());
            categoryHistoryNew.setUserId(getUserId());
            categoryHistoryNew.setDeptId(getDeptId());
            amazonCategoryHistoryService.insert(categoryHistoryNew);
        }
        submitFeedService.submitFeed(uploadEntity);
        return R.ok();
    }


    /**
     * 按钮重新提交
     */
    @RequestMapping("/againUploadByButton")
    public R againUploadByButton(Long uploadId) {
        // 查找基本信息
        UploadEntity uploadEntity = new UploadEntity();
        uploadEntity.setUploadId(uploadId);
        uploadEntity = uploadService.selectById(uploadEntity);
        uploadEntity.setUploadState(0);
        uploadService.updateById(uploadEntity);
        // 删除xml基本信息
        Map<String, Object> map = new HashMap<>();
        map.put("upload_id", uploadId);
        resultXmlService.deleteByMap(map);
        //获取需要上传的商品id
        if (uploadEntity.getUploadProductsIds() != null) {
            List<String> ids = Arrays.asList(uploadEntity.getUploadProductsIds().split(","));
            uploadEntity.setUploadProductsList(productsService.selectBatchIds(ids));
            submitFeedService.submitFeed(uploadEntity);
        } else {

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
        /**
         * 判断是否有不符合上传条件的产品
         */
        if (addUploadVM.getUploadIds() != null) {
            UploadProductDTO dto1 = productsService.isNotCanUpload(Arrays.asList(addUploadVM.getUploadIds()), getUserId());
            if ("error".equals(dto1.getCode())) {
                return R.error(dto1.getMsg());
            }
        }

        List<Long> seList = new ArrayList<>();
        if (addUploadVM.getStartId() != null && addUploadVM.getEndId() != null) {
            Long index = addUploadVM.getStartId();
            while (index <= addUploadVM.getEndId()) {
                seList.add(index);
                index++;
            }
            UploadProductDTO dto2 = productsService.isNotCanUpload(seList, getUserId());
            if ("error".equals(dto2.getCode())) {
                return R.error(dto2.getMsg());
            }
        }


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
        UploadEntity uploadEntity = new UploadEntity();
        uploadEntity.setStartId(addUploadVM.getStartId());
        uploadEntity.setEndId(addUploadVM.getEndId());
        uploadEntity.setUploadIds(StringUtils.join(addUploadVM.getUploadIds(), ","));
        //ret:要上传的产品列表
        List<ProductsEntity> ret = new ArrayList<ProductsEntity>();
        //idList:要上传的id列表
        List<Long> idList = new ArrayList<Long>();
        if (addUploadVM.getUploadIds() != null) {
            UploadProductDTO dto1 = productsService.selectCanUploadProducts(Arrays.asList(addUploadVM.getUploadIds()), getUserId());
            if ("ok".equals(dto1.getCode())) {
                List productsList1 = dto1.getProductsList();
                ret.addAll(productsList1);
                idList.addAll(dto1.getRet());
            } else {
                return R.error(dto1.getMsg());
            }
        }
        ;
        List<Long> seList = new ArrayList<>();
        if (addUploadVM.getStartId() != null && addUploadVM.getEndId() != null) {
            Long index = addUploadVM.getStartId();
            while (index <= addUploadVM.getEndId()) {
                seList.add(index);
                index++;
            }
            UploadProductDTO dto2 = productsService.selectCanUploadProducts(seList, getUserId());
            if ("ok".equals(dto2.getCode())) {
                List productsList2 = dto2.getProductsList();
                ret.addAll(productsList2);
                idList.addAll(dto2.getRet());
            } else {
                return R.error(dto2.getMsg());
            }
        }
        uploadEntity.setUploadProductsList(ret);
        uploadEntity.setUploadProductsIds(StringUtils.join(idList.toArray(), ","));
        //获取分类对象
        AmazonCategoryEntity amazonCategory = amazonCategoryService.selectById(addUploadVM.getAmazonCategoryId());
        //设置授权账户
        AmazonGrantShopEntity amazonGrantShop = amazonGrantShopService.selectById(addUploadVM.getGrantShopId());
        uploadEntity.setGrantShopId(addUploadVM.getGrantShopId());
        uploadEntity.setGrantShop(addUploadVM.getGrantShop());
        //设置分类
        uploadEntity.setAmazonCategoryId(addUploadVM.getAmazonCategoryId());
        uploadEntity.setAmazonCategory(amazonCategory.getDisplayName());
        //设置分类节点id
        String countryCode = amazonGrantShop.getCountryCode();
        uploadEntity.setCountryCode(countryCode);
        uploadEntity.setAmazonCategoryNodeId(addUploadVM.getAmazonNodeId());
       /*
        COUNTY countyEnum = COUNTY.valueOf(countryCode.toUpperCase());
        switch (countyEnum) {
            case GB:
                uploadEntity.setAmazonCategoryNodeId(amazonCategory.getNodeIdUk());
                break;
            case DE:
                uploadEntity.setAmazonCategoryNodeId(amazonCategory.getNodeIdDe());
                break;
            case FR:
                uploadEntity.setAmazonCategoryNodeId(amazonCategory.getNodeIdFr());
                break;
            case IT:
                uploadEntity.setAmazonCategoryNodeId(amazonCategory.getNodeIdIt());
                break;
            case ES:
                uploadEntity.setAmazonCategoryNodeId(amazonCategory.getNodeIdEs());
                break;
            case US:
                uploadEntity.setAmazonCategoryNodeId(amazonCategory.getNodeIdUs());
                break;
            case MX:
                uploadEntity.setAmazonCategoryNodeId(amazonCategory.getNodeIdMx());
                break;
            case CA:
                uploadEntity.setAmazonCategoryNodeId(amazonCategory.getNodeIdCa());
                break;
            case AU:
                uploadEntity.setAmazonCategoryNodeId(amazonCategory.getNodeIdAu());
                break;
            case JP:
                uploadEntity.setAmazonCategoryNodeId(amazonCategory.getNodeIdJp());
                break;
            default:
                break;
        }*/

        //设置模板
        uploadEntity.setAmazonTemplateId(addUploadVM.getAmazonTemplateId());
        uploadEntity.setAmazonTemplate(addUploadVM.getAmazonTemplate());
        //设置操作类型（0：上传;1：修改）
        uploadEntity.setOperateType(0);
        //数组转','号隔开的字符串
        String operateItem = StringUtils.join(addUploadVM.getOperateItem(), ",");
        //设置操作项
        uploadEntity.setOperateItem(operateItem);
        //设置常用属性
        uploadEntity.setUploadTime(new Date());
        uploadEntity.setUpdateTime(new Date());
        uploadEntity.setUserId(getUserId());
        uploadEntity.setDeptId(getDeptId());
        uploadEntity.setUploadState(0);
        //添加到上传表
        uploadService.insert(uploadEntity);
        //设置是否有分类属性
        // uploadEntity.setIsAttribute(addUploadVM.getIsAttribute());

        List<TemplateCategoryFieldsEntity> fieldsEntityList = addUploadVM.getFieldsEntityList();
        for (int i = 0; i < fieldsEntityList.size(); i++) {
            FieldMiddleEntity middleEntity = new FieldMiddleEntity();
            middleEntity.setUploadId(uploadEntity.getUploadId());
            middleEntity.setFieldId(fieldsEntityList.get(i).getFieldId());
            middleEntity.setFieldName(fieldsEntityList.get(i).getFieldName());
            middleEntity.setFieldDisplayName(fieldsEntityList.get(i).getFieldDisplayName());
            middleEntity.setValue(fieldsEntityList.get(i).getValue());
            middleEntity.setIsCustom(fieldsEntityList.get(i).getIsCustom());
            fieldMiddleService.insert(middleEntity);
        }

        //添加到分类历史记录表
        AmazonCategoryHistoryEntity categoryHistory = amazonCategoryHistoryService.selectByAmazonCategoryId(addUploadVM.getAmazonCategoryId(), countryCode);
        //如果有历史数据，则累加数量1
        if (categoryHistory != null) {
            int count = categoryHistory.getCount() + 1;
            categoryHistory.setCount(count);
            amazonCategoryHistoryService.updateAllColumnById(categoryHistory);
        } else {
            //如果没有历史数据，则新增历史数据
            AmazonCategoryHistoryEntity categoryHistoryNew = new AmazonCategoryHistoryEntity();
            categoryHistoryNew.setAmazonCategoryId(addUploadVM.getAmazonCategoryId());
            categoryHistoryNew.setAmazonCategory(amazonCategory.getDisplayName());
            categoryHistoryNew.setAmazonAllCategory(addUploadVM.getAmazonCategory());
            categoryHistoryNew.setCount(1);
            categoryHistoryNew.setCountryCode(countryCode);
            categoryHistoryNew.setNodeId(addUploadVM.getAmazonNodeId());
            categoryHistoryNew.setUserId(addUploadVM.getUserId());
            categoryHistoryNew.setDeptId(addUploadVM.getDeptId());
            amazonCategoryHistoryService.insert(categoryHistoryNew);
        }
        submitFeedService.submitFeed(uploadEntity);
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
        String CNTimeString = null;
        int CNTime = 0;
        switch (countyEnum) {
            //英国
            case GB:
                CNTime = 8 + GBUTC;
                CNTimeString = addDateMinut(countryTime, CNTime);
                break;
            // 美国
            case US:
                CNTime = 8 + USUTC;
                CNTimeString = addDateMinut(countryTime, CNTime);
                break;
            //德国
            case DE:
                CNTime = 8 + DEUTC;
                CNTimeString = addDateMinut(countryTime, CNTime);
                break;
            //法国
            case FR:
                CNTime = 8 + FRUTC;
                CNTimeString = addDateMinut(countryTime, CNTime);
                break;
            //意大利
            case IT:
                CNTime = 8 + ITUTC;
                CNTimeString = addDateMinut(countryTime, CNTime);
                break;
            //墨西哥
            case MX:
                CNTime = 8 + MXUTC;
                CNTimeString = addDateMinut(countryTime, CNTime);
                break;

            //西班牙
            case ES:
                CNTime = 8 + ESUTC;
                CNTimeString = addDateMinut(countryTime, CNTime);
                break;
            //加拿大
            case CA:
                CNTime = 8 + CAUTC;
                CNTimeString = addDateMinut(countryTime, CNTime);
                break;
            default:
                break;
        }
        return R.ok().put("CNTimeString", CNTimeString);
    }


    //时间的计算
    public static String addDateMinut(String day, int hour) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null) {
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
