package io.renren.modules.sys.vm;

import java.util.Date;

/**
 * @Auther: wdh
 * @Date: 2018/12/23 01:07
 * @Description:
 */
public class StatisticsVM {
    /**
     * 查询类型：日：day；月：month年：year；时间段：time
     */
    public String type = "day";
    /**
     * 开始日期
     */
    public String startDate;
    /**
     * 结束日期
     */
    public String endDate;
    /**
     * 公司id
     */
    public String deptId;
    /**
     * 用户id
     */
    public String userId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
