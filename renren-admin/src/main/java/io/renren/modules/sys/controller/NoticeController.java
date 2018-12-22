package io.renren.modules.sys.controller;

import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.sys.entity.NoticeEntity;
import io.renren.modules.sys.service.NoticeService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 消息表
 *
 * @author wdh
 * @email 594340717@qq.com
 * @date 2018-12-22 03:22:35
 */
@RestController
@RequestMapping("sys/notice")
public class NoticeController extends AbstractController{
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private SysUserService userService;
    /**
     * 查询未读消息数量
     */
    @RequestMapping("/count")
    public R count(){
        int count = noticeService.selectCount(new EntityWrapper<NoticeEntity>().eq("user_id",getUserId()).eq("notice_state",0));
        return R.ok().put("count",count);
    }
    /**
     * 标记为已读
     */
    @RequestMapping("/sign")
    public R sign(@RequestParam Long noticeId){
        NoticeEntity notice = noticeService.selectById(noticeId);
        notice.setNoticeState(1);
        noticeService.updateById(notice);
        return R.ok();
    }
    /**
     * 全部标记为已读
     */
    @RequestMapping("/signAll")
    public R signAll(){
        List<NoticeEntity> noticeList = noticeService.selectList(new EntityWrapper<NoticeEntity>().eq("user_id",getUserId()).eq("notice_state",0));
        for(NoticeEntity notice : noticeList){
            notice.setNoticeState(1);
        }
        if(noticeList.size() > 0){
            noticeService.updateBatchById(noticeList);
        }
        return R.ok();
    }
    /**
     * 全部列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = noticeService.queryPage(params,getUserId());
        return R.ok().put("page", page);
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] noticeIds){
        noticeService.deleteBatchIds(Arrays.asList(noticeIds));
        return R.ok();
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{noticeId}")
    @RequiresPermissions("sys:notice:info")
    public R info(@PathVariable("noticeId") Long noticeId){
        NoticeEntity notice = noticeService.selectById(noticeId);

        return R.ok().put("notice", notice);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:notice:save")
    public R save(@RequestBody NoticeEntity notice){
        noticeService.insert(notice);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:notice:update")
    public R update(@RequestBody NoticeEntity notice){
        ValidatorUtils.validateEntity(notice);
        noticeService.updateAllColumnById(notice);//全部更新
        return R.ok();
    }


}
