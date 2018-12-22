package io.renren.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息表
 * 
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-22 03:22:35
 */
@TableName("sys_notice")
public class NoticeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long noticeId;
	/**
	 * 消息类型
	 */
	private String noticeType;
	/**
	 * 消息状态（0未读、1已读）
	 * 默认未读
	 */
	private int noticeState = 0;
	/**
	 * 消息内容
	 */
	private String noticeContent;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 公司id
	 */
	private Long deptId;
	/**
	 * 时间
	 */
	private Date createTime;
	/**
	 * 设置：
	 */
	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}
	/**
	 * 获取：
	 */
	public Long getNoticeId() {
		return noticeId;
	}
	/**
	 * 设置：消息类型
	 */
	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}
	/**
	 * 获取：消息类型
	 */
	public String getNoticeType() {
		return noticeType;
	}

	public int getNoticeState() {
		return noticeState;
	}

	public void setNoticeState(int noticeState) {
		this.noticeState = noticeState;
	}

	/**
	 * 设置：消息内容
	 */
	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}
	/**
	 * 获取：消息内容
	 */
	public String getNoticeContent() {
		return noticeContent;
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
