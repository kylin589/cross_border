package com.swjtu.http;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: wdh
 * @Date: 2018/12/28 00:16
 * @Description:
 */
public class JDBC {

    private static Connection getConn() {
        String driver = "com.mysql.jdbc.Driver";
//        String url = "jdbc:mysql://121.42.27.61:3306/cross_border";
        String url = "jdbc:mysql://127.0.0.1:3306/cross_border";
        String username = "root";
        String password = "admin";
        Connection conn = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static Map<String,Object> getOne() {
        Connection conn = getConn();
        String sql = "select * from agent_ip order by rand() LIMIT 1";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                String ip = rs.getString("ip");
                int port = rs.getInt("port");
                System.out.println("ip:" + ip);
                System.out.println("port:" + port);
                map.put("ip",ip);
                map.put("port",port);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs !=null){
                    rs.close();
                }
            } catch (Exception e) {
            }

            try {
                if(pstmt !=null){
                    pstmt.close();
                }
            } catch (Exception e) {
            }
            try {
                if(conn !=null){
                    conn.close();
                }
            } catch (Exception e) {
            }
        }
        return map;

    }
}
