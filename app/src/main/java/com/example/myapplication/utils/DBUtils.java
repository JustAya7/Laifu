package com.example.myapplication.utils;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;

public class DBUtils {

    /*private final static String driver = "com.mysql.jdbc.Driver";
    private final static String url = "jdbc:mysql://120.46.196.22:3306/dzl_app?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    private final static String username = "dzl_group3";
    private final static String password = "dzlpassword";*/
    private static Connection conn = null;

    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    private static final String TAG = "DBUtils";

    static {
        try {
            Properties properties = new Properties();
            properties.load(DBUtils.class.getResourceAsStream("/assets/db-config.properties"));
            driver = properties.getProperty("db_driver");
            url = properties.getProperty("db_url");
            username = properties.getProperty("db_username");
            password = properties.getProperty("db_password");
            Log.d(TAG, "static initializer: success!");
        } catch (IOException e) {
            Log.e(TAG, "static initializer: failed!",e );
            e.printStackTrace();
        }
    }



    //静态加载驱动程序
    /*static {



    }*/


    //获取连接
    public static Connection getConnection() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("加载驱动错误");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("获取连接错误");
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     * @param rs
     * @param stat
     * @param conn
     */
     public static void close(ResultSet rs,Statement stat,Connection conn){
         try {
             if(rs!=null)rs.close();
             if(stat!=null)stat.close();
             if(conn!=null)conn.close();
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }
}


