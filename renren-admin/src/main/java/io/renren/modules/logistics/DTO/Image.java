package io.renren.modules.logistics.DTO;

/**
 * @Auther: wdh
 * @Date: 2019/1/14 16:08
 * @Description:
 */
public class Image {
    private String asin;
    private String sellersku;
    private String orderitemid;
    private String title;
    private String pic;

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getSellersku() {
        return sellersku;
    }

    public void setSellersku(String sellersku) {
        this.sellersku = sellersku;
    }

    public String getOrderitemid() {
        return orderitemid;
    }

    public void setOrderitemid(String orderitemid) {
        this.orderitemid = orderitemid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
