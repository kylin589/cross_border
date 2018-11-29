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
 * @date 2018-11-27 10:43:39
 */
@TableName("product_amazon_grant")
public class AmazonGrantEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 授权id
	 */
	@TableId
	private Long grantId;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 公司id
	 */
	private Long deptId;
	/**
	 * 店铺名称
	 */
	private String shopName;
	/**
	 * Amazon账号
	 */
	private String amazonAccount;
	/**
	 * 开户区域(0：北美、1：欧洲、2：日本、3：澳大利亚)
	 */
	private Integer area;
	/**
	 * 卖家编号
	 */
	private String merchantId;
	/**
	 * 授权令牌
	 */
	private String grantToken;
	private Date createTime;
	/**
	 * 设置：授权id
	 */
	public void setGrantId(Long grantId) {
		this.grantId = grantId;
	}
	/**
	 * 获取：授权id
	 */
	public Long getGrantId() {
		return grantId;
	}
	/**
	 * 设置：用户id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户id
	 */
	public Long getUserId() {
		return userId;
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
	 * 设置：Amazon账号
	 */
	public void setAmazonAccount(String amazonAccount) {
		this.amazonAccount = amazonAccount;
	}
	/**
	 * 获取：Amazon账号
	 */
	public String getAmazonAccount() {
		return amazonAccount;
	}
	/**
	 * 设置：开户区域(0：北美、1：欧洲、2：日本、3：澳大利亚)
	 */
	public void setArea(Integer area) {
		this.area = area;
	}
	/**
	 * 获取：开户区域(0：北美、1：欧洲、2：日本、3：澳大利亚)
	 */
	public Integer getArea() {
		return area;
	}
	/**
	 * 设置：卖家编号
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	/**
	 * 获取：卖家编号
	 */
	public String getMerchantId() {
		return merchantId;
	}
	/**
	 * 设置：授权令牌
	 */
	public void setGrantToken(String grantToken) {
		this.grantToken = grantToken;
	}
	/**
	 * 获取：授权令牌
	 */
	public String getGrantToken() {
		return grantToken;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
