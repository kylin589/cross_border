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

package io.renren.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.sys.entity.SysDeptEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公司管理
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-20 15:23:47
 */
public interface SysDeptDao extends BaseMapper<SysDeptEntity> {

    /**
     * 查询子公司ID列表
     * @param parentId  上级公司ID
     */
    List<Long> queryDetpIdList(Long parentId);


    /**
     * 合并历史记录表的公司
     */
    int updateCategoryHistory(@Param("fromDeptId")Long fromDeptId, @Param("toDeptId")Long toDeptId);
    /**
     * 合并授权店铺表的公司
     */
    int updateGrantShop(@Param("fromDeptId")Long fromDeptId, @Param("toDeptId")Long toDeptId);
    /**
     * 合并公司消费表的公司
     */
    int updateCompanyConsume(@Param("fromDeptId")Long fromDeptId, @Param("toDeptId")Long toDeptId);
    /**
     * 合并充值记录表的公司
     */
    int updateCompanyRecharge(@Param("fromDeptId")Long fromDeptId, @Param("toDeptId")Long toDeptId);
    /**
     * 合并Amazon授权表的公司
     */
    int updateGrant(@Param("fromDeptId")Long fromDeptId, @Param("toDeptId")Long toDeptId);
    /**
     * 合并订单表的公司
     */
    int updateOrder(@Param("fromDeptId")Long fromDeptId, @Param("toDeptId")Long toDeptId);
    /**
     * 合并产品表的公司
     */
    int updateProduct(@Param("fromDeptId")Long fromDeptId, @Param("toDeptId")Long toDeptId);
    /**
     * 合并上传表的公司
     */
    int updateProductUpload(@Param("fromDeptId")Long fromDeptId, @Param("toDeptId")Long toDeptId);



    /**
     * 分离历史记录表的公司
     */
    int separateCategoryHistory(@Param("userIds")Long[] userIds, @Param("toDeptId")Long toDeptId);
    /**
     * 分离授权店铺表的公司
     */
    int separateGrantShop(@Param("userIds")Long[] userIds, @Param("toDeptId")Long toDeptId);
    /**
     * 分离公司消费表的公司
     */
    int separateCompanyConsume(@Param("userIds")Long[] userIds, @Param("toDeptId")Long toDeptId);
    /**
     * 分离充值记录表的公司
     */
    int separateCompanyRecharge(@Param("userIds")Long[] userIds, @Param("toDeptId")Long toDeptId);
    /**
     * 分离Amazon授权表的公司
     */
    int separateGrant(@Param("userIds")Long[] userIds, @Param("toDeptId")Long toDeptId);
    /**
     * 分离订单表的公司
     */
    int separateOrder(@Param("userIds")Long[] userIds, @Param("toDeptId")Long toDeptId);
    /**
     * 分离产品表的公司
     */
    int separateProduct(@Param("userIds")Long[] userIds, @Param("toDeptId")Long toDeptId);
    /**
     * 分离上传表的公司
     */
    int separateProductUpload(@Param("userIds")Long[] userIds, @Param("toDeptId")Long toDeptId);
}
