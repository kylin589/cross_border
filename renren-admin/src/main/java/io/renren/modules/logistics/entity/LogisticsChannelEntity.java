package io.renren.modules.logistics.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

    import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-03-31 09:38:56
 */
@TableName("amazon_logistics_channel")
public class LogisticsChannelEntity implements Serializable {
    private static final long serialVersionUID = 1L;

            /**
         * 渠道中文名
         */
                @TableId
            private Long channelId;

            /**
         * 包裹类型 0-小包(云途) ；1-大包(三泰)
         */
            private Integer packageType;
            /**
         * 渠道code
         */
            private String channelName;




    /**
         * 
         */
            private String channelCode;
            /**
         * 备注
         */
            private String remark;


            /**
         * 设置：渠道中文名
         */
        public void setChannelId(Long channelId) {
            this.channelId = channelId;
        }

        /**
         * 获取：渠道中文名
         */
        public Long getChannelId() {
            return channelId;
        }
            /**
         * 设置：包裹类型 0-小包(云途) ；1-大包(三泰)
         */
        public void setPackageType(Integer packageType) {
            this.packageType = packageType;
        }

        /**
         * 获取：包裹类型 0-小包(云途) ；1-大包(三泰)
         */
        public Integer getPackageType() {
            return packageType;
        }
            /**
         * 设置：渠道code
         */
        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        /**
         * 获取：渠道code
         */
        public String getChannelName() {
            return channelName;
        }
            /**
         * 设置：
         */
        public void setChannelCode(String channelCode) {
            this.channelCode = channelCode;
        }

        /**
         * 获取：
         */
        public String getChannelCode() {
            return channelCode;
        }
            /**
         * 设置：备注
         */
        public void setRemark(String remark) {
            this.remark = remark;
        }

        /**
         * 获取：备注
         */
        public String getRemark() {
            return remark;
        }

    }
