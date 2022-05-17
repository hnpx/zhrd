package cn.px.sys.modular.reports.service;


import cn.px.sys.modular.activity.entity.ActivityEntity;
import cn.px.sys.modular.activity.service.ActivityService;
import cn.px.sys.modular.activity.vo.ActivityVo;
import cn.px.sys.modular.activity.wrapper.ActivityWrapper;
import cn.px.sys.modular.demand.service.DemandService;
import cn.px.sys.modular.integral.entity.IntegralRecordEntity;
import cn.px.sys.modular.integral.service.IntegralRecordService;
import cn.px.sys.modular.project.service.ProjectService;
import cn.px.sys.modular.project.vo.ProjectVo;
import cn.px.sys.modular.project.wrapper.ProjectWrapper;
import cn.px.sys.modular.reports.mapper.ApiMapper;
import cn.px.sys.modular.unitSecondClass.entity.UnitSecondClassEntity;
import cn.px.sys.modular.unitSecondClass.service.UnitSecondClassService;
import cn.px.sys.modular.wx.entity.AllUserEntity;
import cn.px.sys.modular.wx.service.AllUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiService {

    @Autowired
    private ApiMapper apiMapper;


    @Autowired
    private ActivityWrapper activityWrapper;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectWrapper projectWrapper;


    @Autowired
    private AllUserService allUserService;

    @Autowired
    private UnitSecondClassService unitSecondClassService;

    @Autowired
    private IntegralRecordService integralRecordService;
    @Autowired
    private DemandService demandService;



    public Map homeList(){
        Page<Map<String,Object>> activityInfo = activityService.getActivityWx1(1,2,1);
        Page<ActivityVo> actvityList =activityWrapper.getVoPage(activityInfo);

        Page<Map<String,Object>> project = projectService.getListPage2(1,4,null,1);
        Page<ProjectVo> projectList =  projectWrapper.getVoPage(project);
        Page<Map<String,Object>> demandList =  demandService.getListByUser(1,4);

        List newList = apiMapper.getNews(new Page(1,4)).getRecords();


        Map map = new HashMap();
        map.put("actvityList",actvityList.getRecords());
        map.put("projectList",projectList.getRecords());
        map.put("newList",newList);
        map.put("demandList",demandList);

        return   map;
    }


    public void updateStatus(Long userid,Integer type,String phone){

        AllUserEntity entity = allUserService.queryById(userid);
        AllUserEntity updateEntity = new AllUserEntity();
        if (entity.getIntegral() == null){
            entity.setIntegral(0);
        }
        if (entity.getRemainingPoints() == null){
            entity.setRemainingPoints(0);
        }
        //用户操作
        updateEntity.setId(userid);
        updateEntity.setPhone(phone);
        updateEntity.setIntegral(entity.getIntegral() + 1);
        updateEntity.setRemainingPoints(entity.getRemainingPoints() + 1);
        updateEntity.setStatus(2);
        updateEntity.setType(type);
        allUserService.update(updateEntity);

        //往记录表中插入数据
        IntegralRecordEntity recordEntity = new IntegralRecordEntity();
        recordEntity.setUserId(userid);
        recordEntity.setType(1);
        recordEntity.setIntegral(1);
        recordEntity.setSource("报到积分");
        integralRecordService.update(recordEntity);

    }


    public  List<Map<String,Object>> selectUserSign(Long id){
        return apiMapper.selectUserSign(id);
    }


}
