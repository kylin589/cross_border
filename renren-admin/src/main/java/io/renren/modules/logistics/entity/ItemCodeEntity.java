package io.renren.modules.logistics.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

    import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author wangdh
 * @email 594340717@qq.com
 * @date 2019-04-03 15:39:33
 */
@TableName("item_code")
public class ItemCodeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

            /**
         * 
         */
                @TableId
            private Long itemCodeId;
            /**
         * 
         */
            private String itemCode;
            /**
         * 关联货品的中文材质
         */
            private String itemCnMaterial;
            /**
         * 关联货品的英文材质
         */
            private String itemEnMaterial;
    
            /**
         * 设置：
         */
        public void setItemCodeId(Long itemCodeId) {
            this.itemCodeId = itemCodeId;
        }

        /**
         * 获取：
         */
        public Long getItemCodeId() {
            return itemCodeId;
        }
            /**
         * 设置：
         */
        public void setItemCode(String itemCode) {
            this.itemCode = itemCode;
        }

        /**
         * 获取：
         */
        public String getItemCode() {
            return itemCode;
        }
            /**
         * 设置：关联货品的中文材质
         */
        public void setItemCnMaterial(String itemCnMaterial) {
            this.itemCnMaterial = itemCnMaterial;
        }

        /**
         * 获取：关联货品的中文材质
         */
        public String getItemCnMaterial() {
            return itemCnMaterial;
        }
            /**
         * 设置：关联货品的英文材质
         */
        public void setItemEnMaterial(String itemEnMaterial) {
            this.itemEnMaterial = itemEnMaterial;
        }

        /**
         * 获取：关联货品的英文材质
         */
        public String getItemEnMaterial() {
            return itemEnMaterial;
        }
    }
