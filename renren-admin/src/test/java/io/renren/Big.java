package io.renren;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Big {
    public static void main(String[] args) {
        BigDecimal b = new BigDecimal("2");
        BigDecimal c = new BigDecimal("1");
        BigDecimal d = new BigDecimal("199");
        BigDecimal temp = new BigDecimal(0.85);
        BigDecimal z=new BigDecimal("63.99");
        BigDecimal h= new BigDecimal("6.872718691000629");
        BigDecimal cb=new BigDecimal("199");
        //BigDecimal divide = b.divide(c,2,BigDecimal.ROUND_DOWN);
       /* BigDecimal divide=b.multiply(c);
        BigDecimal divide1 = d.divide(divide,2,BigDecimal.ROUND_HALF_UP);
*/
        /*String s = divide1.toString();
        String[] split = s.split("\\.");
        String s1 = split[0] + "." + "99";
        System.out.println(s1);*/
        /*b = b.add(d);
        System.out.println(b);*/
        /*BigDecimal add = b.add(c).add(d);
        System.out.println(add);*/
        /*BigDecimal divide = ((temp.multiply(z).multiply(h)).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(cb);
        BigDecimal divide1 = divide.divide(z.multiply(h).setScale(2, BigDecimal.ROUND_HALF_UP),2);
        System.out.println(divide1);
        DecimalFormat df = new DecimalFormat("0.00%");
        System.out.println(df.format(divide1));*/
       /* System.out.println(divide1);
        System.out.println(divide);
        BigDecimal divide2 = divide.divide(divide1,2);
        System.out.println(divide2);
        DecimalFormat df = new DecimalFormat("0.00%");
        System.out.println(df.format(divide2));*/


        BigDecimal add = b.add(c);
        System.out.println(add);
    }
}
