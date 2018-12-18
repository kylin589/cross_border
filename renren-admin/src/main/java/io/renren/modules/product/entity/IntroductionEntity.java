package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 国家介绍
 * 
 * @author zjr
 * @email zhang-jiarui@baizesoft.com
 * @date 2018-11-07 14:54:47
 */
@TableName("product_introduction")
public class IntroductionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 产品介绍id
	 */
	@TableId
	private Long introductionId;
	/**
	 * 国家代码
	 */
	private String country=" ";
	/**
	 * 产品标题
	 */
	private String productTitle=" ";
	/**
	 * 关键词
	 */
	private String keyWord=" ";
	/**
	 * 要点说明
	 */
	private String keyPoints=" ";
	/**
	 * 产品描述
	 */
	private String productDescription=" ";

	/**
	 * 设置：产品介绍id
	 */
	public void setIntroductionId(Long introductionId) {
		this.introductionId = introductionId;
	}
	/**
	 * 获取：产品介绍id
	 */
	public Long getIntroductionId() {
		return introductionId;
	}
	/**
	 * 设置：国家代码
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * 获取：国家代码
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * 设置：产品标题
	 */
	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}
	/**
	 * 获取：产品标题
	 */
	public String getProductTitle() {
		return productTitle;
	}
	/**
	 * 设置：关键词
	 */
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	/**
	 * 获取：关键词
	 */
	public String getKeyWord() {
		return keyWord;
	}
	/**
	 * 设置：要点说明
	 */
	public void setKeyPoints(String keyPoints) {
		this.keyPoints = keyPoints;
	}
	/**
	 * 获取：要点说明
	 */
	public String getKeyPoints() {
		return keyPoints;
	}
	/**
	 * 设置：产品描述
	 */
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	/**
	 * 获取：产品描述
	 */
	public String getProductDescription() {
		return productDescription;
	}
}
