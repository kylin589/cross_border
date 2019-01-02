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

package io.renren.modules.sys.controller;

import io.renren.common.utils.Constant;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.product.service.AmazonCategoryService;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysDeptService;
import io.renren.modules.sys.service.SysUserService;
import io.renren.modules.sys.vm.DeptMergeSeparateVM;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * 公司管理
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-20 15:23:47
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends AbstractController {
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysUserService userService;
	@Autowired
	private AmazonCategoryService amazonCategoryService;
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:dept:list")
	public List<SysDeptEntity> list(){
		List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>(), getDeptId());
		return deptList;
	}

	/**
	 * 公司合并
	 */
	@RequestMapping("/merge")
//	@RequiresPermissions("sys:dept:merge")
	public R merge(@RequestBody DeptMergeSeparateVM vm){
		Long fromDeptId = vm.getFromDeptId();
		Long toDeptId = vm.getToDeptId();
		if(fromDeptId == 1L){
			return R.error("总部不能合并");
		}
		//将员工全部归到目标公司
		List<SysUserEntity> fromUserList = userService.selectUserList(fromDeptId);
		for(SysUserEntity user : fromUserList){
			user.setDeptId(toDeptId);
		}
		if(fromUserList.size() >0){
			userService.updateBatchById(fromUserList);
		}
		//将对应表中对应公司id替换
		//历史记录表、授权店铺表、公司消费表、充值记录表、Amazon授权表、订单表、产品表、上传表
		sysDeptService.merge(fromDeptId, toDeptId);
		//删除原来的公司
		List<Long> deptList = sysDeptService.queryDetpIdList(fromDeptId);
		if(deptList.size() > 1){
			for(Long deptId : deptList){
				sysDeptService.deleteById(fromDeptId);
			}
		}else{
			sysDeptService.deleteById(fromDeptId);
		}

		return R.ok();
	}
	/**
	 * 公司分离
	 */
	@RequestMapping("/separate")
//	@RequiresPermissions("sys:dept:separate")
	public R separate(@RequestBody DeptMergeSeparateVM vm){
		Long fromDeptId = vm.getFromDeptId();
		Long toDeptId = vm.getToDeptId();
		Long[] userIds = vm.getUserIds();
		if(fromDeptId == 1L){
			return R.error("总部不能分离");
		}
		//将员工全部归到目标公司
		if(userIds.length >0){
			List<SysUserEntity> userList = userService.selectBatchIds(Arrays.asList(userIds));
			for(SysUserEntity user : userList){
				user.setDeptId(toDeptId);
			}
			userService.updateBatchById(userList);
		}
		//将对应表中对应员工的公司id替换
		//历史记录表、授权店铺表、公司消费表、充值记录表、Amazon授权表、订单表、产品表、上传表
		sysDeptService.separate(userIds, toDeptId);
		return R.ok();
	}

	/**
	 * 选择公司(添加、修改菜单)
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:dept:select")
	public R select(){
		List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>(), getDeptId());
/*
		//添加一级公司
		if(getUserId() == Constant.SUPER_ADMIN){
			SysDeptEntity root = new SysDeptEntity();
			root.setDeptId(0L);
			root.setName("一级公司");
			root.setParentId(-1L);
			root.setOpen(true);
			deptList.add(root);
		}*/
		return R.ok().put("deptList", deptList);
	}

	/**
	 * 上级公司Id(管理员则为0)
	 */
	@RequestMapping("/info")
	@RequiresPermissions("sys:dept:list")
	public R info(){
		long deptId = 0;
		if(getUserId() != Constant.SUPER_ADMIN){
			List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>(), getDeptId());
			Long parentId = null;
			for(SysDeptEntity sysDeptEntity : deptList){
				if(parentId == null){
					parentId = sysDeptEntity.getParentId();
					continue;
				}

				if(parentId > sysDeptEntity.getParentId().longValue()){
					parentId = sysDeptEntity.getParentId();
				}
			}
			deptId = parentId;
		}

		return R.ok().put("deptId", deptId);
	}
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{deptId}")
	@RequiresPermissions("sys:dept:info")
	public R info(@PathVariable("deptId") Long deptId){
		SysDeptEntity dept = sysDeptService.selectById(deptId);
		String parentName = null;
		if(dept.getParentId() != null && dept.getParentId() != 0L){
			parentName = sysDeptService.selectById(dept.getParentId()).getName();
		}
		dept.setParentName(parentName);
		return R.ok().put("dept", dept);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("sys:dept:save")
	public R save(@RequestBody SysDeptEntity dept){
		ValidatorUtils.validateEntity(dept);
		dept.setUpdateTime(new Date());
		dept.setCreateTime(new Date());
		sysDeptService.insert(dept);
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("sys:dept:update")
	public R update(@RequestBody SysDeptEntity dept){
		ValidatorUtils.validateEntity(dept);
		dept.setUpdateTime(new Date());
		sysDeptService.updateById(dept);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:dept:delete")
	public R delete(long deptId){
		//判断是否有子公司
		List<Long> deptList = sysDeptService.queryDetpIdList(deptId);
		if(deptList.size() > 0){
			return R.error("请先删除子公司");
		}

		sysDeptService.deleteById(deptId);
		
		return R.ok();
	}
	
}
