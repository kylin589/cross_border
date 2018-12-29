package io.renren.modules.util;

/**
 * @Auther: wdh
 * @Date: 2018/12/29 01:24
 * @Description:
 */
public class TranslateUtils {
    /**
     * toUpperCase 所有首字母转为大写
     * @param: [text]
     * @return: java.lang.String
     * @auther: wdh
     * @date: 2018/11/5 17:16
     */
    public static String toUpperCase(String text) {
        if(text != null && !"".equals(text)){
            String[] strs = text.split(" ");
            StringBuilder sb = new StringBuilder();
            for (String strTmp : strs) {
                char[] ch = strTmp.toCharArray();
                if(ch.length>0){
                    if (ch[0] >= 'a' && ch[0] <= 'z') {
                        ch[0] = (char) (ch[0] - 32);
                    }
                    String strT = new String(ch);
                    sb.append(strT).append(" ");
                }
            }
            return sb.toString().trim();
        }
        return null;
    }
}
