package io.renren.modules.amazon.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-11-27 11:00:50
 */
@TableName("amazon_grant_shop")
public class AmazonGrantShopEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 授权店铺id
	 */
	@TableId
	private Long grantShopId;
	/**
	 * 授权id
	 */
	private Long grantId;
	/**
	 * 店铺名称
	 */
	private String shopName;
	/**
	 * 店铺账户
	 */
	private String shopAccount;
	/**
	 * 授权国家
	 */
	private String grantCounty;
	/**
	 * 国家标识
	 */
	private String countryCode;
	//站点
	private String amazonSite;
	/**
	 * 亚马逊MWS端点
	 */
	private String mwsPoint;
	/**
	 * MarketplaceId
	 */
	private String marketplaceId;
	/**
	 * 所属区域(欧洲0、北美1、远东2）
	 */
	private Integer region;

	private Long userId;
	private Long deptId;
	/**
	 * 设置：授权店铺id
	 */
	public void setGrantShopId(Long grantShopId) {
		this.grantShopId = grantShopId;
	}
	/**
	 * 获取：授权店铺id
	 */
	public Long getGrantShopId() {
		return grantShopId;
	}
	/**
	 * 设置：店铺名称
	 */
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	/**
	 * 获取：店铺名称
	 */
	public String getShopName() {
		return shopName;
	}
	/**
	 * 设置：店铺账户
	 */
	public void setShopAccount(String shopAccount) {
		this.shopAccount = shopAccount;
	}
	/**
	 * 获取：店铺账户
	 */
	public String getShopAccount() {
		return shopAccount;
	}
	/**
	 * 设置：授权国家
	 */
	public void setGrantCounty(String grantCounty) {
		this.grantCounty = grantCounty;
	}
	/**
	 * 获取：授权国家
	 */
	public String getGrantCounty() {
		return grantCounty;
	}
	/**
	 * 设置：国家标识
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	/**
	 * 获取：国家标识
	 */
	public String getCountryCode() {
		return countryCode;
	}

	public String getAmazonSite() {
		return amazonSite;
	}

	public void setAmazonSite(String amazonSite) {
		this.amazonSite = amazonSite;
	}

	/**
	 * 设置：亚马逊MWS端点
	 */
	public void setMwsPoint(String mwsPoint) {
		this.mwsPoint = mwsPoint;
	}
	/**
	 * 获取：亚马逊MWS端点
	 */
	public String getMwsPoint() {
		return mwsPoint;
	}
	/**
	 * 设置：MarketplaceId
	 */
	public void setMarketplaceId(String marketplaceId) {
		this.marketplaceId = marketplaceId;
	}
	/**
	 * 获取：MarketplaceId
	 */
	public String getMarketplaceId() {
		return marketplaceId;
	}
	/**
	 * 设置：所属区域(欧洲0、北美1、远东2）
	 */
	public void setRegion(Integer region) {
		this.region = region;
	}
	/**
	 * 获取：所属区域(欧洲0、北美1、远东2）
	 */
	public Integer getRegion() {
		return region;
	}

	public Long getGrantId() {
		return grantId;
	}

	public void setGrantId(Long grantId) {
		this.grantId = grantId;
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
}
