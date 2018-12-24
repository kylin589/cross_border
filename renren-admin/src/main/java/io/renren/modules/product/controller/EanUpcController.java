package io.renren.modules.product.controller;

import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.product.vm.EanUpcvm;
import org.apache.catalina.LifecycleState;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.product.entity.EanUpcEntity;
import io.renren.modules.product.service.EanUpcService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-23 16:58:48
 */
@RestController
@RequestMapping("product/eanupc")
public class EanUpcController {
    @Autowired
    private EanUpcService eanUpcService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:eanupc:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = eanUpcService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{eanUpcId}")
    @RequiresPermissions("product:eanupc:info")
    public R info(@PathVariable("eanUpcId") Long eanUpcId){
        EanUpcEntity eanUpc = eanUpcService.selectById(eanUpcId);

        return R.ok().put("eanUpc", eanUpc);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:eanupc:save")
    public R save(@RequestBody EanUpcEntity eanUpc){
        eanUpcService.insert(eanUpc);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:eanupc:update")
    public R update(@RequestBody EanUpcEntity eanUpc){
        ValidatorUtils.validateEntity(eanUpc);
        eanUpcService.updateAllColumnById(eanUpc);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:eanupc:delete")
    public R delete(@RequestBody Long[] eanUpcIds){
        eanUpcService.deleteBatchIds(Arrays.asList(eanUpcIds));

        return R.ok();
    }

    /**
     * @methodname: batchAdd 批量增加码
     * @param: [type, codes]
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/12/23 18:26
     */
    @RequestMapping("/batchadd")
    public R batchAdd(@RequestBody EanUpcvm eanUpcvm){
        String codes = eanUpcvm.getCodes();
        String type = eanUpcvm.getType();
        String[] codeString = codes.split(",");
        List<EanUpcEntity>eanUpcEntities= new ArrayList<EanUpcEntity>();
        for (int i = 0; i <codeString.length ; i++) {
            if (type.equals("EAN")){
                EanUpcEntity eanUpcEntity = new EanUpcEntity();
                eanUpcEntity.setType("EAN");
                eanUpcEntity.setCode(codeString[i]);
                eanUpcEntity.setState(0);
                eanUpcEntity.setCreateTime(new Date());
                eanUpcEntities.add(eanUpcEntity);
            }else {
                EanUpcEntity eanUpcEntity = new EanUpcEntity();
                eanUpcEntity.setType("UPC");
                eanUpcEntity.setCode(codeString[i]);
                eanUpcEntity.setState(0);
                eanUpcEntity.setCreateTime(new Date());
                eanUpcEntities.add(eanUpcEntity);
            }
        }
        eanUpcService.insertBatch(eanUpcEntities);
        return R.ok().put("eanUpcEntities",eanUpcEntities);
    }

    /**
     * @methodname: count 码的总数
     * @param: []
     * @return: io.renren.common.utils.R
     * @auther: jhy
     * @date: 2018/12/23 19:33
     */
    @RequestMapping("/count")
    public R count(){
        //码的总数
        int count = eanUpcService.selectCount(new EntityWrapper<EanUpcEntity>());
        //未使用的
        int noStateCount = eanUpcService.selectCount(new EntityWrapper<EanUpcEntity>().eq("state", 0));
        //已使用的
        int YesStateCount = eanUpcService.selectCount(new EntityWrapper<EanUpcEntity>().eq("state", 1));
        return R.ok().put("count",count).put("noStateCount",noStateCount).put("YesStateCount",YesStateCount);
    }
}
