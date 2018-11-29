package io.renren.modules.amazon.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-19 11:02:06
 */
@TableName("product_amazon_marketplace")
public class AmazonMarketplaceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Integer pointId;
	/**
	 * 亚马逊商场
	 */
	private String country;
	/**
	 * 国家代码
	 */
	private String countryCode;
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
	/**
	 * 软删（1：删除）
	 */
	private Integer isDeleted;
	private String amazonSite;
	/**
	 * 设置：主键
	 */
	public void setPointId(Integer pointId) {
		this.pointId = pointId;
	}
	/**
	 * 获取：主键
	 */
	public Integer getPointId() {
		return pointId;
	}
	/**
	 * 设置：亚马逊商场
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * 获取：亚马逊商场
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * 设置：国家代码
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	/**
	 * 获取：国家代码
	 */
	public String getCountryCode() {
		return countryCode;
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

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getAmazonSite() {
		return amazonSite;
	}

	public void setAmazonSite(String amazonSite) {
		this.amazonSite = amazonSite;
	}
}
