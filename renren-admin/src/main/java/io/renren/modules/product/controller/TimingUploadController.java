package io.renren.modules.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.common.utils.R;
import io.renren.modules.amazon.entity.AmazonCategoryHistoryEntity;
import io.renren.modules.amazon.entity.AmazonGrantShopEntity;
import io.renren.modules.amazon.service.AmazonCategoryHistoryService;
import io.renren.modules.amazon.service.AmazonGrantShopService;
import io.renren.modules.amazon.util.COUNTY;
import io.renren.modules.job.service.ScheduleJobService;
import io.renren.modules.product.entity.AmazonCategoryEntity;
import io.renren.modules.product.entity.ProductsEntity;
import io.renren.modules.product.entity.UploadEntity;
import io.renren.modules.product.service.AmazonCategoryService;
import io.renren.modules.product.service.ProductsService;
import io.renren.modules.product.service.UploadService;
import io.renren.modules.product.vm.AddUploadVM;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysUserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jdom.output.SAXOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * 定时上传产品,调用立即上传的方法
 * timingUpload为spring bean的名称
 */
@Component("timingUpload")
public class TimingUploadController {
    @Autowired
    private UploadController uploadController;

    public void timingUpload(String params) {
        //使用ObjectMapper将传过来的字符串参数变成实体
        ObjectMapper om = new ObjectMapper();
        AddUploadVM addUploadVM = null;
        try {
            addUploadVM = om.readValue(params, AddUploadVM.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //调用已经写好的立即上传产品的方法
        uploadController.timingUpload(addUploadVM);
    }
}
