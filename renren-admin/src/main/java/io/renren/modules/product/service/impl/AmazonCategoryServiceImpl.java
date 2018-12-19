package io.renren.modules.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.product.dao.AmazonCategoryDao;
import io.renren.modules.product.entity.AmazonCategoryEntity;
import io.renren.modules.product.service.AmazonCategoryService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("amazonCategoryService")
public class AmazonCategoryServiceImpl extends ServiceImpl<AmazonCategoryDao, AmazonCategoryEntity> implements AmazonCategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<AmazonCategoryEntity> page = this.selectPage(
                new Query<AmazonCategoryEntity>(params).getPage(),
                new EntityWrapper<AmazonCategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * @methodname: queryByAreaOneClassify 根据区域查询一级分类
     * @param: [region]
     * @return: java.util.List<io.renren.modules.product.entity.AmazonCategoryEntity>
     * @auther: jhy
     * @date: 2018/11/16 14:46
     */
    @Override
    public List<AmazonCategoryEntity> queryByAreaOneClassify(String countryCode) {
       Map<String,Object> map =new HashMap<String,Object>();
       map.put("country_code",countryCode);
       map.put("parent_id",0);
        List<AmazonCategoryEntity>amazonCategoryEntityList= this.selectByMap(map);
        return amazonCategoryEntityList;
    }

    /**
     * @methodname: queryByChindIdParentId 根据子级id查出所有的父级id以逗号进行拼接成字符串
     * @param: [amazonCategoryId] 子级id
     * @return: String
     * @auther: jhy
     * @date: 2018/11/16 15:46
     */
    /*通过子类id获取所有的父类id传过来一个子类的id 。定义一个数组， 把传过来的id加入到数组类面遍历数组
      判断传过来的id！=0，通过这个id查出他的实体。获取父类id，就是上级的id 存入到数组里面
      把数组进行反转， 判断数组里面不为空的  进行字符串进行拼接*/
    @Override
    public String queryByChindIdParentId(Long amazonCategoryId) {
        //设置一个数组存放查询出的id
        Long[]arr= new Long[6];
        //把传过来的子级id存放到数组
        arr[0] = amazonCategoryId;
        int j = 1;
        for (int i = 0; i < arr.length - 1; i++) {
            if (amazonCategoryId != 0) {
                AmazonCategoryEntity amazonCategoryEntity = this.selectById(amazonCategoryId);
                //查询出来它的父级id
                amazonCategoryId = amazonCategoryEntity.getParentId();
                arr[j] = amazonCategoryId;
                j++;
            }
        }
        String idString = "";
        //多数组进行反转，以逗号进行字符串拼接
        for (int i = arr.length - 1; i >= 0; i--) {
            //遍历数组里不为null和0的
            if (!(arr[i] == null || arr[i] == 0)) {
                if (i == 0) {
                    idString += arr[i];
                } else {
                    idString += arr[i];
                    idString += ",";
                }
            }
        }
        return idString;
    }
}
