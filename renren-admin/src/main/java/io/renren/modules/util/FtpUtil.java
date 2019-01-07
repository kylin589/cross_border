package io.renren.modules.util;


import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

//连接ftp服务器的工具类
public class FtpUtil {
    //ftp服务器ip地址
//    private static final String FTP_ADDRESS = "132.232.22.202";
    private static final String FTP_ADDRESS = "39.105.120.226";
    //端口号
    private static final int FTP_PORT = 21;
    //用户名
    private static final String FTP_USERNAME = "ftpuser";
    //密码
    private static final String FTP_PASSWORD = "ftpuser123";

    //把图片上传到ftp服务器上
    public static boolean uploadFile(String originFileName, InputStream input, String ftpBasepath) {
        boolean success = false;
        //创建FTPClient客户端
        FTPClient ftp = new FTPClient();
        ftp.setControlEncoding("GBK");
        ftp.enterLocalActiveMode();

        try {
            int reply;
            // 设置ftp上传模式，已二进制方式上传。
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            // 连接FTP服务器,ip地址，端口号
            ftp.connect(FTP_ADDRESS, FTP_PORT);
            // 登录账号和密码
            ftp.login(FTP_USERNAME, FTP_PASSWORD);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return success;
            }
            //主动模式
//            ftp.enterLocalActiveMode();
            //被动模式
            ftp.enterLocalPassiveMode();
            boolean a = ftp.changeWorkingDirectory("/");
            //如果输入的路径为空或者为根路径，则不转换操作目录
            if (StringUtils.isBlank(ftpBasepath) || ftpBasepath.equals("/")) {
                //否则创建想要上传文件的目录，并且将操作目录转为新创建的目录
            } else {
                StringTokenizer s = new StringTokenizer(ftpBasepath, "/");
                s.countTokens();
                String pathName = "";
                while (s.hasMoreElements()) {
                    //创建多级目录
                    pathName = pathName + "/" + (String) s.nextElement();
                    try {
                        ftp.mkd(pathName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ftp.changeWorkingDirectory(ftpBasepath);
            }
            ftp.storeFile(originFileName, input);
            input.close();
            ftp.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }
}
