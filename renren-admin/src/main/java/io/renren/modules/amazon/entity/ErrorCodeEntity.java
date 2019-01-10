package io.renren.modules.amazon.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author zjr
 * @email 1981763981@qq.com
 * @date 2019-01-09 14:18:08
 */
@TableName("amazon_error_code")
public class ErrorCodeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 错误代码
	 */
	private String errorCode;
	/**
	 * 
	 */
	private String errorExplanation;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：错误代码
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * 获取：错误代码
	 */
	public String getErrorCode() {
		return errorCode;
	}
	/**
	 * 设置：
	 */
	public void setErrorExplanation(String errorExplanation) {
		this.errorExplanation = errorExplanation;
	}
	/**
	 * 获取：
	 */
	public String getErrorExplanation() {
		return errorExplanation;
	}
}
