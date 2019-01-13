package io.renren.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * cookie表
 * 
 * @author wdh
 * @email 594340717@qq.com
 * @date 2019-01-11 01:41:22
 */
@TableName("sys_cookie_log")
public class CookieLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long cookieId;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * cookie
	 */
	private String cookie;
	/**
	 * cookie类型
	 */
	private String type;
	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 设置：
	 */
	public void setCookieId(Long cookieId) {
		this.cookieId = cookieId;
	}
	/**
	 * 获取：
	 */
	public Long getCookieId() {
		return cookieId;
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
	 * 设置：cookie
	 */
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	/**
	 * 获取：cookie
	 */
	public String getCookie() {
		return cookie;
	}
	/**
	 * 设置：cookie类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：cookie类型
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
}
