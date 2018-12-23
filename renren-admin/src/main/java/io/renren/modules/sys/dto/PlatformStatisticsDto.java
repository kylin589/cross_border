package io.renren.modules.sys.dto;

import java.math.BigDecimal;

/**
 * @Auther: wdh
 * @Date: 2018/12/23 03:18
 * @Description:
 */
public class PlatformStatisticsDto {
    private BigDecimal choucheng = new BigDecimal(0.00);
    private BigDecimal userProfit = new BigDecimal(0.00);
    private BigDecimal allProfit = new BigDecimal(0.00);

    public BigDecimal getChoucheng() {
        return choucheng;
    }

    public void setChoucheng(BigDecimal choucheng) {
        this.choucheng = choucheng;
    }

    public BigDecimal getUserProfit() {
        return userProfit;
    }

    public void setUserProfit(BigDecimal userProfit) {
        this.userProfit = userProfit;
    }

    public BigDecimal getAllProfit() {
        return allProfit;
    }

    public void setAllProfit(BigDecimal allProfit) {
        this.allProfit = allProfit;
    }
}
