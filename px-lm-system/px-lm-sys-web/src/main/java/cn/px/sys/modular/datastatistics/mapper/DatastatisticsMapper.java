package cn.px.sys.modular.datastatistics.mapper;


import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DatastatisticsMapper {


    List<Map<String,Object>> typeCount();

    List<Map<String,Object>> gridClassCount();


    List<Map<String,Object>>claim();

    List<Map<String,Object>> selectFiveCount();



    List<Map<String,Object>> reportData();


    List<Map<String,Object>> demand();

    List<Map<String,Object>>brigadeData();


    List<Map<String,Object>> browse();

    //首页数据总览
    Map<String, Object> getOverviewMap();

}
