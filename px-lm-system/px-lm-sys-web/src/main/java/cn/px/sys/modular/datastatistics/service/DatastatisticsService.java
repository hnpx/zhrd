package cn.px.sys.modular.datastatistics.service;


import cn.px.sys.core.util.GetUtil;
import cn.px.sys.core.util.Jdbcresult;
import cn.px.sys.modular.datastatistics.mapper.DatastatisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatastatisticsService {

    @Autowired
    private DatastatisticsMapper datastatisticsMapper;


    public List<Map<String, Object>> typeCount() {
        return datastatisticsMapper.typeCount();
    }

    public List<Map<String, Object>> gridClassCount() {
        return datastatisticsMapper.gridClassCount();
    }

    public List<Map<String, Object>> claim() {
        return datastatisticsMapper.claim();
    }

    public List<Map<String, Object>> selectFiveCount() {
        return datastatisticsMapper.selectFiveCount();
    }


    public List<Map<String, Object>> reportData() {
        return datastatisticsMapper.reportData();
    }

    public List<Map<String, Object>> demand() {
        return datastatisticsMapper.demand();
    }

    public List<Map<String, Object>> brigadeData() {
        return datastatisticsMapper.brigadeData();
    }

    public List<Map<String, Object>> browse() {
        return datastatisticsMapper.browse();
    }

    public int getCountByall(String sql) {
        //  GetUtil util = new GetUtil();
        //  String sql = "SELECT count(1) as count from a_jmxxgl ";
        //Object[] param = {idcard};
        Connection con = GetUtil.getconnection();
        Jdbcresult rm = GetUtil.executeQuery(con, sql, null);
        ResultSet rs = rm.rs;
        PreparedStatement pt = rm.pt;
        int count = 0;
        try {
            while (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            GetUtil.closeAll(rs, pt, con);

        }
        return count;

    }

    //首页数据总览
    public Map<String, Object> getOverviewMap() throws Exception {
        Map<String, Object> map = new HashMap<>();
        // 3.拼写修改的SQL语句,参数采用?占位
        String replaceBuyRecord = "SELECT COUNT(1) as count FROM replace_buy_record WHERE `enable`=1 ";
        map.put("replaceBuyRecord",getSqlutil(replaceBuyRecord));
        String mealRecord = "SELECT COUNT(1) as count FROM meal_record WHERE `enable`=1 ";
        map.put("mealRecord",getSqlutil(mealRecord));
        String oldManInfo = "SELECT COUNT(1) as count FROM old_man_info WHERE `enable`=1 ";
        map.put("oldManInfo",getSqlutil(oldManInfo));
        return map;
    } ;

    public  static  String  getSqlutil(String sql) throws Exception{
        int  count=0;
        // 1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 2.获取连接对象
        String url = "jdbc:mysql://rm-2zelg6p00b03b25520o.mysql.rds.aliyuncs.com:3306/px-zhyl?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=CTT";
        Connection conn = DriverManager.getConnection(url, "root", "pinxun@2020");
        // 4.调用数据库连接对象con的方法prepareStatement获取SQL语句的预编译对象
        PreparedStatement pst = conn.prepareStatement(sql);
        // 5.调用pst方法执行SQL语句
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            count=rs.getInt("count");
        }
        // 7.关闭资源
        rs.close();
        pst.close();
        conn.close();
        return  count+"";
    }
}


