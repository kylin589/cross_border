package io.renren.modules.util;

import java.util.UUID;

public class UUIDUtils {

    /**
     * 生成一个32位的字符串(唯一)
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
