package cn.px.sys.core.util;

import java.sql.*;

public class GetUtil {


    private static String dirver;
    private static String url;
    private static String user;
    private static String pwd;
    static{

            dirver = "com.mysql.cj.jdbc.Driver";
            url = "jdbc:mysql://rm-2zelg6p00b03b25520o.mysql.rds.aliyuncs.com:3306/chongwen_tp_hnp?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=CTT";
            user = "root";
            pwd = "pinxun@2020";


    }
    public static Connection getconnection(){
        Connection con = null;
        try {
            Class.forName(dirver);
            con= DriverManager.getConnection(url,user,pwd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  con;

    }


    public static int addDate(Connection conn,String sql,Object[] param){
        int result = 0 ;
        if (conn==null){
            return  0;
        }
        if (sql==null){
            return 0;
        }
        try{
            System.out.println(param);
            PreparedStatement ps = conn.prepareStatement(sql);
            if (param==null||param.length==0){
                return  0;
            }
            for (int i = 0;i<param.length;i++){
                // System.out.println(new String(param[i].toString().getBytes("iso-8859-1"),"gb2312"));

                ps.setObject(i+1,param[i]);//new String(param[i].toString().getBytes("iso-8859-1"),"gb2312"));
            }
            result=ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static Jdbcresult executeQuery(Connection conn,String sql,Object[] param){
        if (conn==null){
            return  null;
        }
        if (sql==null){
            return null;
        }
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            pt = conn.prepareStatement(sql);
            if (param!=null&& param.length !=0){
                for (int i = 0 ; i< param.length;i++){
                    pt.setObject(i+1,param[i]);
                }
            }
            rs = pt.executeQuery();
            Jdbcresult map = new Jdbcresult();
            map.pt =  pt;
            map.rs =  rs;
            return  map;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  null;
    }
    public static void closeAll(ResultSet rs, PreparedStatement pt, Connection con){

        try {
            if (rs!=null) {
                rs.close();
            }
            if (pt!=null) {
                pt.close();
            }
            if (con!=null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
