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


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import io.renren.common.utils.R;
import io.renren.modules.amazon.util.ConstantDictionary;
import io.renren.modules.product.entity.EanUpcEntity;
import io.renren.modules.product.entity.OrderEntity;
import io.renren.modules.product.service.EanUpcService;
import io.renren.modules.product.service.OrderService;
import io.renren.modules.sys.entity.*;
import io.renren.modules.sys.service.NoticeService;
import io.renren.modules.sys.service.SysDeptService;
import io.renren.modules.sys.service.SysRoleService;
import io.renren.modules.sys.service.SysUserRoleService;
import io.renren.modules.sys.shiro.ShiroUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 登录相关
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月10日 下午1:15:31
 */
@Controller
public class SysLoginController extends AbstractController{
	@Autowired
	private Producer producer;
	@Autowired
	private SysDeptService deptService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private EanUpcService eanUpcService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysRoleService roleService;

	@RequestMapping("captcha.jpg")
	public void captcha(HttpServletResponse response)throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);
        
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
	}
	
	/**
	 * 登录
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public R login(String username, String password, String captcha) {
		String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
		if(!captcha.equalsIgnoreCase(kaptcha)){
			return R.error("验证码不正确");
		}
		
		try{
			Subject subject = ShiroUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			subject.login(token);
		}catch (UnknownAccountException e) {
			return R.error(e.getMessage());
		}catch (IncorrectCredentialsException e) {
			return R.error("账号或密码不正确");
		}catch (LockedAccountException e) {
			return R.error("账号已被锁定,请联系管理员");
		}catch (AuthenticationException e) {
			return R.error("账户验证失败");
		}

		new CalculationThread().start();
		return R.ok();
	}

	class CalculationThread extends Thread   {
		@Override
		public void run() {

			//总部查询MEN码
			if(getUserId() == 1L){
				int count = eanUpcService.selectCount(new EntityWrapper<EanUpcEntity>().eq("state",0));
				if(count<1000){
					NoticeEntity notice = new NoticeEntity();
					notice.setDeptId(getDeptId());
					notice.setUserId(getUserId());
					notice.setNoticeContent("UPC/EAN码已不足，请及时添加。");
					notice.setCreateTime(new Date());
					notice.setNoticeType("UPC/EAN");
					noticeService.insert(notice);
				}
			}
			SysDeptEntity dept = deptService.selectById(getDeptId());
			//未发货订单
			int unshippedNumber = orderService.selectCount(
					new EntityWrapper<OrderEntity>().in("order_status", ConstantDictionary.OrderStateCode.UNLIQUIDATED_ORDER_STATE)
							.ne("abnormal_status",ConstantDictionary.OrderStateCode.ORDER_STATE_RETURN)
			);
			dept.setUnshippedNumber(unshippedNumber);
			//未结算订单数(国际已发货)
			int unliquidatedNumber = orderService.selectCount(
					new EntityWrapper<OrderEntity>().eq("order_status",ConstantDictionary.OrderStateCode.ORDER_STATE_INTLSHIPPED)
			);
			dept.setUnliquidatedNumber(unliquidatedNumber);
			//预计费用
			BigDecimal estimatedCost = new BigDecimal(unshippedNumber * 50);
			dept.setEstimatedCost(estimatedCost);
			//可用余额
			BigDecimal availableBalance = dept.getBalance().subtract(estimatedCost).setScale(2,BigDecimal.ROUND_HALF_UP);
			if(availableBalance.compareTo(new BigDecimal(50)) == -1){
				SysRoleEntity roleEntity = roleService.selectOne(new EntityWrapper<SysRoleEntity>().eq("role_name","加盟商管理员"));
				if(sysUserRoleService.selectCount(new EntityWrapper<SysUserRoleEntity>().eq("user_id",getUserId()).eq("role_id",roleEntity.getRoleId())) > 0){
					NoticeEntity notice = new NoticeEntity();
					notice.setDeptId(getDeptId());
					notice.setUserId(getUserId());
					notice.setNoticeContent("公司可用余额不足，为避免订单出现异常，请及时充值。");
					notice.setCreateTime(new Date());
					notice.setNoticeType("余额");
					noticeService.insert(notice);
				}
			}
			dept.setAvailableBalance(availableBalance);
			//预计还可生成单数
			int estimatedOrder = availableBalance.divide(new BigDecimal(50),0,BigDecimal.ROUND_HALF_DOWN).intValue();
			dept.setEstimatedOrder(estimatedOrder);
			deptService.updateById(dept);
		}
	}
	
	/**
	 * 退出
	 */
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout() {
		ShiroUtils.logout();
		return "redirect:login.html";
	}
	
}
