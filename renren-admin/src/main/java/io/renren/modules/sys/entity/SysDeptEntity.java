/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.renren.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.UpdateGroup;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 公司管理
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-20 15:23:47
 */
@TableName("sys_dept")
public class SysDeptEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//公司ID
	@TableId
	private Long deptId;
	//上级公司ID，一级公司为0
	private Long parentId;
	//公司名称
	@NotBlank(message="公司名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String name;
	//上级公司名称
	@TableField(exist=false)
	private String parentName;

	@TableLogic
	private Integer delFlag;
	/**
	 * ztree属性
	 */
	@TableField(exist=false)
	private Boolean open;
	@TableField(exist=false)
	private List<?> list;

	//公司账户数
	@NotBlank(message="公司账户数不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private Integer accountCount;

	//公司SKU信息
	@NotBlank(message="公司SKU信息不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String companySku;

	//公司地址
	@NotBlank(message="公司地址不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String companyAddress;

	//公司负责人
	@NotBlank(message="公司负责人不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String companyPerson;

	//电话
	@NotBlank(message="电话不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String companyTel;

	//QQ
	@NotBlank(message="QQ不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String companyQq;

	//平台佣金点数
	@NotBlank(message="平台佣金点数不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private BigDecimal companyPoints;

	//余额
	private BigDecimal balance = new BigDecimal(0.00);

	//预计费用
	private BigDecimal estimatedCost = new BigDecimal(0.00);

	//未结算订单数
	private int unliquidatedNumber;

	//未发货订单数
	private int unshippedNumber;

	//可用余额
	private BigDecimal availableBalance = new BigDecimal(0.00);

	//预计还可生成单数
	private int estimatedOrder;

	//更新时间
	private Date updateTime;

	//创建时间
	private Date createTime;

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Long getDeptId() {
		return deptId;
	}
	/**
	 * 设置：上级公司ID，一级公司为0
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：上级公司ID，一级公司为0
	 */
	public Long getParentId() {
		return parentId;
	}
	/**
	 * 设置：公司名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：公司名称
	 */
	public String getName() {
		return name;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public Integer getAccountCount() {
		return accountCount;
	}

	public void setAccountCount(Integer accountCount) {
		this.accountCount = accountCount;
	}

	public String getCompanySku() {
		return companySku;
	}

	public void setCompanySku(String companySku) {
		this.companySku = companySku;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanyPerson() {
		return companyPerson;
	}

	public void setCompanyPerson(String companyPerson) {
		this.companyPerson = companyPerson;
	}

	public String getCompanyTel() {
		return companyTel;
	}

	public void setCompanyTel(String companyTel) {
		this.companyTel = companyTel;
	}

	public String getCompanyQq() {
		return companyQq;
	}

	public void setCompanyQq(String companyQq) {
		this.companyQq = companyQq;
	}

	public BigDecimal getCompanyPoints() {
		return companyPoints;
	}

	public void setCompanyPoints(BigDecimal companyPoints) {
		this.companyPoints = companyPoints;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}

	public int getUnliquidatedNumber() {
		return unliquidatedNumber;
	}

	public void setUnliquidatedNumber(int unliquidatedNumber) {
		this.unliquidatedNumber = unliquidatedNumber;
	}

	public int getUnshippedNumber() {
		return unshippedNumber;
	}

	public void setUnshippedNumber(int unshippedNumber) {
		this.unshippedNumber = unshippedNumber;
	}

	public BigDecimal getEstimatedCost() {
		return estimatedCost;
	}

	public void setEstimatedCost(BigDecimal estimatedCost) {
		this.estimatedCost = estimatedCost;
	}

	public int getEstimatedOrder() {
		return estimatedOrder;
	}

	public void setEstimatedOrder(int estimatedOrder) {
		this.estimatedOrder = estimatedOrder;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
