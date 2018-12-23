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
    public Date startDate;
    /**
     * 结束日期
     */
    public Date endDate;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
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
