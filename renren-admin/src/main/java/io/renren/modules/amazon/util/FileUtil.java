package io.renren.modules.amazon.util;

import java.io.File;

public class FileUtil {

    // 判断文件是否存在
    public static boolean judeFileExists(File file) {
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    // 判断文件是否存在
    public static boolean judeFileExists(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }
}