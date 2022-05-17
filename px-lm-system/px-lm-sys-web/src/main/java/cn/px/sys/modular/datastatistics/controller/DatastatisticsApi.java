package cn.px.sys.modular.datastatistics.controller;

import cn.px.base.core.AbstractController;
import cn.px.sys.modular.activity.service.ActivityService;
import cn.px.sys.modular.cadresReports.service.CadresReportsService;
import cn.px.sys.modular.datastatistics.service.DatastatisticsService;
import cn.px.sys.modular.homeClass.service.HomeClassService;
import cn.px.sys.modular.peopleCongress.service.PeopleCongressService;
import cn.px.sys.modular.peopleOpinion.service.PeopleOpinionService;
import cn.px.sys.modular.project.service.ProjectService;
import cn.px.sys.modular.unitReports.service.UnitReportsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class DatastatisticsApi extends AbstractController {

    @Autowired
    private DatastatisticsService datastatisticsService;
    @Autowired
    private UnitReportsService unitReportsService;
    @Autowired
    private CadresReportsService cadresReportsService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private PeopleOpinionService peopleOpinionService;
    @Autowired
    private PeopleCongressService peopleCongressService;


    @Autowired
    private HomeClassService homeClassService;
    @RequestMapping("/data")
    public Object data(){

        //各角色人数占比
        List<Map<String,Object>>  roleListData = datastatisticsService.typeCount();


        //各网格人数占比
        List<Map<String,Object>> gridClassData = datastatisticsService.gridClassCount();

        //认领统计
        List<Map<String,Object>> claimData = datastatisticsService.claim();
        if (claimData.size()>0){
            claimData.forEach(a->{
                String name = String.valueOf(a.get("name"));
                String newName = nameDeal(name);
                a.put("name",newName);
            });
        }

        //近5天活动项目数
        List<Map<String,Object>> fiveCount = datastatisticsService.selectFiveCount();


        //近4天报到数
        List<Map<String,Object>> reportData = datastatisticsService.reportData();


        //需求认领
        List<Map<String,Object>> demand = datastatisticsService.demand();
        if (demand.size()>0){
            demand.forEach(a->{
                String name = String.valueOf(a.get("name"));
                String newName = nameDeal(name);
                a.put("name",newName);
            });
        }

        //服务大队人数
        List<Map<String,Object>> brigadeData = datastatisticsService.brigadeData();


        //近几天浏览数量
        List<Map<String,Object>> browse = datastatisticsService.browse();



        Map map = new HashMap();
        //各角色占比
        map.put("roleListData",roleListData);
        //各网格人数占比
        map.put("gridClassData",gridClassData);
        //认领统计
        map.put("claimData",claimData);
        //近5天活动项目数
        map.put("fiveCount",fiveCount);
        //近几天报到数
        map.put("reportData",reportData);
        //需求认领统计
        map.put("demand",demand);
        //服务大队人数统计
        map.put("brigadeData",brigadeData);
        //浏览数量
        map.put("browse",browse);

        return setSuccessModelMap(map);
    }





    /*
        名字处理
     */
    public String nameDeal(String name){

        StringBuilder newName = null;
        if (StringUtils.isNotEmpty(name)){
            newName = new StringBuilder(name);;
            newName.replace(1,newName.length()+1,"***");
            System.out.println(newName);
        }
        else {
            newName = new StringBuilder("***");
        }
        System.out.println(newName);
        return newName.toString();
    }


    @RequestMapping("/toatalData")
    public Object toatalData() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("data",homeClassService.HomeTotal());
        map.put("data2",datastatisticsService.getOverviewMap());
        return setSuccessModelMap(map);
    }

    @RequestMapping("/all/data")
    public Object getData(){
        Map<String, Object> map = new HashMap<>();

        //单位报道数
        map.put("unitReportNum",unitReportsService.getCountByall());
        //党员干部数
        map.put("cadresReport",cadresReportsService.getCountByall());
        //平台项目数
        map.put("projectNum",projectService.getCountByall());
        //平台活动数
        map.put("activityNum",activityService.getCountByall());
        //人大反馈数
        map.put("opinionNum",peopleOpinionService.getCountByall());
        //人大代表数
        map.put("congressNum",peopleCongressService.getCountByall());
        //街道居民数
        map.put("peopleNum",datastatisticsService.getCountByall("SELECT count(1) as count from a_jmxxgl"));
        //街道党员数
        map.put("partyMemberNum",datastatisticsService.getCountByall("SELECT count(1) as count from a_dyxxgl"));
        //独生子女数
        map.put("oneChildNum",datastatisticsService.getCountByall("SELECT count(1) as count from a_dszngl"));
        //退役士兵数
        map.put("soldiersNum",datastatisticsService.getCountByall("SELECT count(1) as count from a_xtygl where fyzt='退役'"));
        //违法信息数
        map.put("illegalNum",datastatisticsService.getCountByall("SELECT count(1) as count from a_wfxxgl"));
        //低保人员数
        map.put("lowNum",datastatisticsService.getCountByall("SELECT count(1) as count from a_dbrygl"));

        return super.setSuccessModelMap(map);
    }



}
