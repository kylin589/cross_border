package io.renren.modules.sys.dto;

import java.math.BigDecimal;

/**
 * @Auther: wdh
 * @Date: 2018/12/23 01:25
 * @Description:
 */
public class StatisticsDto {
    private UserStatisticsDto userStatisticsDto;
    private FranchiseeStatisticsDto franchiseeStatisticsDto;
    private PlatformStatisticsDto platformStatisticsDto;

    public UserStatisticsDto getUserStatisticsDto() {
        return userStatisticsDto;
    }

    public void setUserStatisticsDto(UserStatisticsDto userStatisticsDto) {
        this.userStatisticsDto = userStatisticsDto;
    }

    public FranchiseeStatisticsDto getFranchiseeStatisticsDto() {
        return franchiseeStatisticsDto;
    }

    public void setFranchiseeStatisticsDto(FranchiseeStatisticsDto franchiseeStatisticsDto) {
        this.franchiseeStatisticsDto = franchiseeStatisticsDto;
    }

    public PlatformStatisticsDto getPlatformStatisticsDto() {
        return platformStatisticsDto;
    }

    public void setPlatformStatisticsDto(PlatformStatisticsDto platformStatisticsDto) {
        this.platformStatisticsDto = platformStatisticsDto;
    }
}
