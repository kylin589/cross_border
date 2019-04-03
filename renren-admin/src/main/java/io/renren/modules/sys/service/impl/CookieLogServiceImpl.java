package io.renren.modules.sys.service.impl;

import io.renren.modules.amazon.util.ConstantDictionary;
import io.renren.modules.product.entity.EanUpcEntity;
import io.renren.modules.product.entity.OrderEntity;
import io.renren.modules.product.service.EanUpcService;
import io.renren.modules.product.service.OrderService;
import io.renren.modules.sys.entity.*;
import io.renren.modules.sys.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.sys.dao.CookieLogDao;


@Service("cookieLogService")
public class CookieLogServiceImpl extends ServiceImpl<CookieLogDao, CookieLogEntity> implements CookieLogService {

    @Autowired
    private EanUpcService eanUpcService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private SysDeptService deptService;
    @Autowired
    @Lazy
    private OrderService orderService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysUserService sysUserService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<CookieLogEntity> page = this.selectPage(
                new Query<CookieLogEntity>(params).getPage(),
                new EntityWrapper<CookieLogEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Async("taskExecutor")
    public void Calculation(Long userId, Long deptId) {
        //总部查询MEN码
        if (userId == 1L) {
            int count = eanUpcService.selectCount(new EntityWrapper<EanUpcEntity>().eq("state", 0));
            if (count < 1000) {
                NoticeEntity notice = new NoticeEntity();
                notice.setDeptId(deptId);
                notice.setUserId(userId);
                notice.setNoticeContent("UPC/EAN码已不足，请及时添加。");
                notice.setCreateTime(new Date());
                notice.setNoticeType("UPC/EAN");
                noticeService.insert(notice);
            }
        }
        SysDeptEntity dept = deptService.selectById(deptId);
        if (dept.getBalance().compareTo(new BigDecimal(500)) == -1) {
            SysRoleEntity roleEntity = roleService.selectOne(new EntityWrapper<SysRoleEntity>().eq("role_name", "加盟商管理员"));
            if (sysUserRoleService.selectCount(new EntityWrapper<SysUserRoleEntity>().eq("user_id", userId).eq("role_id", roleEntity.getRoleId())) > 0) {
                NoticeEntity notice = new NoticeEntity();
                notice.setDeptId(deptId);
                notice.setUserId(userId);
                notice.setNoticeContent("公司可用余额不足，为避免订单出现异常，请及时充值。");
                notice.setCreateTime(new Date());
                notice.setNoticeType("余额");
                noticeService.insert(notice);
            }
        }
    }

}
