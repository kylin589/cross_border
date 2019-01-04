package io.renren.modules.logistics.entity;

import java.util.List;

/**
 * @Auther: wdh
 * @Date: 2018/12/31 18:28
 * @Description:
 */
public class SendDataMoedl {
    private List<Shipping> list;
    private List<String> serviceURL;
    private List<String> marketplaceIds;
    private String sellerId;
    private String mwsAuthToken;

    public List<Shipping> getList() {
        return list;
    }

    public void setList(List<Shipping> list) {
        this.list = list;
    }

    public List<String> getServiceURL() {
        return serviceURL;
    }

    public void setServiceURL(List<String> serviceURL) {
        this.serviceURL = serviceURL;
    }

    public List<String> getMarketplaceIds() {
        return marketplaceIds;
    }

    public void setMarketplaceIds(List<String> marketplaceIds) {
        this.marketplaceIds = marketplaceIds;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getMwsAuthToken() {
        return mwsAuthToken;
    }

    public void setMwsAuthToken(String mwsAuthToken) {
        this.mwsAuthToken = mwsAuthToken;
    }

    public SendDataMoedl(List<Shipping> list, List<String> serviceURL, List<String> marketplaceIds, String sellerId, String mwsAuthToken) {
        this.list = list;
        this.serviceURL = serviceURL;
        this.marketplaceIds = marketplaceIds;
        this.sellerId = sellerId;
        this.mwsAuthToken = mwsAuthToken;
    }
}
