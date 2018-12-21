package io.renren.modules.product.vm;

import io.renren.modules.sys.entity.SysUserEntity;

import java.util.Arrays;

/**
 * @Auther: wdh
 * @Date: 2018/11/27 10:36
 * @Description:
 */
public class AddUploadVM {
    //开始id
    private Long startId;
    //结束id
    private Long endId;
    //上传id列表
    private Long[] uploadIds;
    //授权店铺id
    private Long grantShopId;
    //授权店铺
    private String grantShop;
    //操作项（0:产品基本信息1:关系 2:图片3:库存4:价格）
    private String[] operateItem;
    //亚马逊分类id
    private Long amazonCategoryId;
    //亚马逊分类
    private String amazonCategory;
    //亚马逊模板id
    private Long amazonTemplateId;
    //亚马逊模板
    private String amazonTemplate;
    //是否有分类属性（默认0：没有  1：有）
    private Integer isAttribute;
    // TODO: 2018/11/27 分类属性
    //接受前台传过来的时间；
    private String time;

    //用户
    private SysUserEntity user;
    //用户id
    private Long userId;
    //公司id
    private Long deptId;

    public SysUserEntity getUser() {
        return user;
    }

    public void setUser(SysUserEntity user) {
        this.user = user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getStartId() {
        return startId;
    }

    public void setStartId(Long startId) {
        this.startId = startId;
    }

    public Long getEndId() {
        return endId;
    }

    public void setEndId(Long endId) {
        this.endId = endId;
    }

    public Long[] getUploadIds() {
        return uploadIds;
    }

    public void setUploadIds(Long[] uploadIds) {
        this.uploadIds = uploadIds;
    }

    public Long getGrantShopId() {
        return grantShopId;
    }

    public void setGrantShopId(Long grantShopId) {
        this.grantShopId = grantShopId;
    }

    public String getGrantShop() {
        return grantShop;
    }

    public void setGrantShop(String grantShop) {
        this.grantShop = grantShop;
    }

    public String[] getOperateItem() {
        return operateItem;
    }

    public void setOperateItem(String[] operateItem) {
        this.operateItem = operateItem;
    }

    public Long getAmazonCategoryId() {
        return amazonCategoryId;
    }

    public void setAmazonCategoryId(Long amazonCategoryId) {
        this.amazonCategoryId = amazonCategoryId;
    }

    public String getAmazonCategory() {
        return amazonCategory;
    }

    public void setAmazonCategory(String amazonCategory) {
        this.amazonCategory = amazonCategory;
    }

    public Long getAmazonTemplateId() {
        return amazonTemplateId;
    }

    public void setAmazonTemplateId(Long amazonTemplateId) {
        this.amazonTemplateId = amazonTemplateId;
    }

    public String getAmazonTemplate() {
        return amazonTemplate;
    }

    public void setAmazonTemplate(String amazonTemplate) {
        this.amazonTemplate = amazonTemplate;
    }

    public Integer getIsAttribute() {
        return isAttribute;
    }

    public void setIsAttribute(Integer isAttribute) {
        this.isAttribute = isAttribute;
    }

    @Override
    public String toString() {
        return "AddUploadVM{" +
                "startId=" + startId +
                ", endId=" + endId +
                ", uploadIds=" + Arrays.toString(uploadIds) +
                ", grantShopId=" + grantShopId +
                ", grantShop='" + grantShop + '\'' +
                ", operateItem=" + Arrays.toString(operateItem) +
                ", amazonCategoryId=" + amazonCategoryId +
                ", amazonCategory='" + amazonCategory + '\'' +
                ", amazonTemplateId=" + amazonTemplateId +
                ", amazonTemplate='" + amazonTemplate + '\'' +
                ", isAttribute=" + isAttribute +
                ", time='" + time + '\'' +
                '}';
    }
}
