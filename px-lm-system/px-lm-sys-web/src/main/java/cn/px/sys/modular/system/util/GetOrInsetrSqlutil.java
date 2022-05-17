package cn.px.sys.modular.system.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetOrInsetrSqlutil {


    private static String url = "http://112.126.61.241:9300/user/getPassWordEncoding";

    public static String zhyl_Url() {
        return url;
    }

    public static int getSqlutil(String sql) throws Exception {
        int count = 0;
        // 1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 2.获取连接对象
        String url = "jdbc:mysql://rm-2zelg6p00b03b25520o.mysql.rds.aliyuncs.com:3306/px-zhyl?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=CTT";
        Connection conn = DriverManager.getConnection(url, "root", "pinxun@2020");
        // 4.调用数据库连接对象con的方法prepareStatement获取SQL语句的预编译对象
        PreparedStatement pst = conn.prepareStatement(sql);
        // 5.调用pst方法执行SQL语句
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            count = rs.getInt("userId");
        }
        // 7.关闭资源
        rs.close();
        pst.close();
        conn.close();
        return count;
    }

    public static int getInsetrSqlutil(String sql) throws Exception {
        int count = 0;
        // 1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 2.获取连接对象
        String url = "jdbc:mysql://rm-2zelg6p00b03b25520o.mysql.rds.aliyuncs.com:3306/px-zhyl?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=CTT";
        Connection conn = DriverManager.getConnection(url, "root", "pinxun@2020");
        // 4.调用数据库连接对象con的方法prepareStatement获取SQL语句的预编译对象
        PreparedStatement pst = conn.prepareStatement(sql);
        // 5.调用pst方法执行SQL语句
        int result = pst.executeUpdate();
        // 7.关闭资源
        pst.close();
        conn.close();
        return result;
    }
}
