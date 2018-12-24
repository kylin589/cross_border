package io.renren.modules.product.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-23 16:58:48
 */
@TableName("product_ean_upc")
public class EanUpcEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long eanUpcId;
	/**
	 * 码
	 */
	private String code;
	/**
	 * 状态（0：未使用，1：已使用）
	 */
	private Integer state;
	/**
	 * 产品id
	 */
	private Long productId;
	/**
	 * 添加时间
	 */
	private Date createTime;
	/**
	 * 类型
	 */
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 设置：id
	 */
	public void setEanUpcId(Long eanUpcId) {
		this.eanUpcId = eanUpcId;
	}
	/**
	 * 获取：id
	 */
	public Long getEanUpcId() {
		return eanUpcId;
	}
	/**
	 * 设置：码
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 获取：码
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 设置：状态（0：未使用，1：已使用）
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 获取：状态（0：未使用，1：已使用）
	 */
	public Integer getState() {
		return state;
	}
	/**
	 * 设置：产品id
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	/**
	 * 获取：产品id
	 */
	public Long getProductId() {
		return productId;
	}
	/**
	 * 设置：添加时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：添加时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
}
