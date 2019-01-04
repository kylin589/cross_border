package io.renren.modules.logistics.entity;

/**
 * @Auther: wdh
 * @Date: 2018/12/29 14:25
 * @Description:
 */
//配送产品
public class Item {
    private String amazonOrderItemCode;//Amazon订单id
    private String quantity;//数量

    public String getAmazonOrderItemCode() {
        return amazonOrderItemCode;
    }

    public void setAmazonOrderItemCode(String amazonOrderItemCode) {
        this.amazonOrderItemCode = amazonOrderItemCode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    public Item() {
        super();
    }


}