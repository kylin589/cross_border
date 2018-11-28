package io.renren.modules.product.vm;

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
    private Object[] operateItem;
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

    public Object[] getOperateItem() {
        return operateItem;
    }

    public void setOperateItem(Object[] operateItem) {
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
}
