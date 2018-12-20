package io.renren.modules.amazon.util;

import java.io.File;
import java.util.Calendar;
import java.util.Random;

public class FileUtil {

    // 判断文件是否存在
    public static boolean judeFileExists(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static String generateFilePath(String path, String fileType) {
        Calendar calendar = Calendar.getInstance();
        String year = calendar.get(Calendar.YEAR) + "/";
        String month = (calendar.get(Calendar.MONTH)) + 1 + "/";
        path = path + year + month + fileType + "/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        Random random = new Random();
        int length = 10;
        String numstr = "123456789";
        String chastr_b = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String chastr_s = "abcdefghijklmnopqrstuvwxyz";
        String specil = "_";
        String base = numstr + chastr_b + chastr_s + specil;
        StringBuffer sb = new StringBuffer();

        sb.append(chastr_b.charAt(random.nextInt(chastr_b.length())));
        for (int i = 0; i < length - 2; i++) {
            int num = random.nextInt(base.length());
            sb.append(base.charAt(num));
        }
        //追加最后一个数字
        sb.append(numstr.charAt(random.nextInt(numstr.length())));
        return path + sb.toString() + specil + fileType + ".xml";
    }
}