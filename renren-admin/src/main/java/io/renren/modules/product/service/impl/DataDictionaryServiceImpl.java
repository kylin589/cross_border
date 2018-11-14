package io.renren.modules.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.product.dao.DataDictionaryDao;
import io.renren.modules.product.entity.DataDictionaryEntity;
import io.renren.modules.product.service.DataDictionaryService;


@Service("dataDictionaryService")
public class DataDictionaryServiceImpl extends ServiceImpl<DataDictionaryDao, DataDictionaryEntity> implements DataDictionaryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DataDictionaryEntity> page = this.selectPage(
                new Query<DataDictionaryEntity>(params).getPage(),
                new EntityWrapper<DataDictionaryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * @methodname: auditList 审核状态分类
     * @param: []
     * @return: java.util.List<io.renren.modules.product.entity.DataDictionaryEntity>
     * @auther: jhy
     * @date: 2018/11/13 23:36
     */
    @Override
    public List<DataDictionaryEntity> auditList() {
        List<DataDictionaryEntity> dataList = this.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "AUDIT_STATE").orderBy(true, "data_sort", true));
        return dataList;
    }

    /**
     * @methodname: putawayList 上架状态分类
     * @param: []
     * @return: java.util.List<io.renren.modules.product.entity.DataDictionaryEntity>
     * @auther: jhy
     * @date: 2018/11/13 23:37
     */
    @Override
    public List<DataDictionaryEntity> putawayList() {
        List<DataDictionaryEntity> dataList = this.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "SHELVE_STATE").orderBy(true, "data_sort", true));
        return dataList;
    }

    /**
     * @methodname: productTypeList 产品类型分类
     * @param: []
     * @return: java.util.List<io.renren.modules.product.entity.DataDictionaryEntity>
     * @auther: jhy
     * @date: 2018/11/13 23:37
     */
    @Override
    public List<DataDictionaryEntity> productTypeList() {
        List<DataDictionaryEntity> productList = this.selectList(new EntityWrapper<DataDictionaryEntity>().eq("data_type", "PRODUCT_TYPE").orderBy(true, "data_sort", true));
        return productList;
    }

}
