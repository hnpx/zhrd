package cn.px.sys.modular.project.service;

import cn.hutool.core.bean.BeanUtil;
import cn.px.base.Constants;
import cn.px.base.core.BaseServiceImpl;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.sys.modular.activity.constant.IsSignEnum;
import cn.px.sys.modular.activity.constant.SignEnum;
import cn.px.sys.modular.activity.constant.TimeStatusEnum;
import cn.px.sys.modular.activity.entity.ActivityEntity;
import cn.px.sys.modular.activity.entity.ActivityUserSignEntity;
import cn.px.sys.modular.activity.vo.ActivityDeVo;
import cn.px.sys.modular.activity.vo.CommentVo;
import cn.px.sys.modular.activity.vo.UserVo;
import cn.px.sys.modular.cadresReports.entity.CadresReportsEntity;
import cn.px.sys.modular.cadresReports.service.CadresReportsService;
import cn.px.sys.modular.integral.service.IntegralRecordService;
import cn.px.sys.modular.project.entity.ProjectEntity;
import cn.px.sys.modular.project.entity.ProjectUserSignEntity;
import cn.px.sys.modular.project.mapper.ProjectMapper;
import cn.px.sys.modular.project.vo.ProjectVo;
import cn.px.sys.modular.unitReports.entity.UnitReportsEntity;
import cn.px.sys.modular.unitReports.service.UnitReportsService;
import cn.px.sys.modular.unitSecondClass.entity.UnitSecondClassEntity;
import cn.px.sys.modular.unitSecondClass.service.UnitSecondClassService;
import cn.px.sys.modular.volunteersReports.entity.VolunteersReportsEntity;
import cn.px.sys.modular.volunteersReports.service.VolunteersReportsService;
import cn.px.sys.modular.wx.entity.AllUserEntity;
import cn.px.sys.modular.wx.service.AllUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 项目管理(Project)表服务实现类
 *
 * @author
 * @since 2020-09-02 20:20:08
 */
@Service("projectService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "project")
public class ProjectService extends BaseServiceImpl<ProjectEntity, ProjectMapper> {

    @Resource
    private ProjectUserSignService projectUserSignService;
    @Resource
    private ProjectCommentService projectCommentService;
    @Resource
    private IntegralRecordService integralRecordService;
    @Resource
    private ProjectService projectService;
    @Resource
    private UnitReportsService unitReportsService;

    @Autowired
    private AllUserService allUserService;

    @Autowired
    private UnitSecondClassService unitSecondClassService;

    @Autowired
    private VolunteersReportsService volunteersReportsService;


    @Autowired
    private CadresReportsService cadresReportsService;

    /**
     * 查询项目列表
     * @param
     * @param name
     * @return
     */
   public Page<Map<String,Object>> getListPage( String name,Long cid,String contactPerson,String phone,Integer timeStatus,String month){
        Page page = LayuiPageFactory.defaultPage();

        return super.mapper.getListPage(page,name,cid,contactPerson,phone,timeStatus,month);
    }

    /**
     * 查询项目列表通过项目分类id
     * @param
     * @param
     * @return
     */
    public Page<Map<String,Object>> getListPage1(Integer page,Integer pageSize, Long cid,Integer timeStatus){
       Page page1 = new Page(page,pageSize);
       Date date = new Date();
       return super.mapper.getListPage1(page1,cid,date,timeStatus);
    }


    /**
     * 查询项目列表通过项目分类id
     * @param
     * @param
     * @return
     */
    public Page<Map<String,Object>> getListPage2(Integer page,Integer pageSize, Long cid,Integer timeStatus){
        Page page1 = new Page(page,pageSize);
        Date date = new Date();
        return super.mapper.getListPage2(page1,cid,date,timeStatus);
    }


    public ProjectVo getProjectVo(Long id, Long userId){

        ProjectEntity projectEntity =  super.mapper.selectById(id);
        ProjectVo projectVo = new ProjectVo();

        BeanUtil.copyProperties(projectEntity,projectVo);
        //判断项目开始和未开始状态
        if(projectVo.getEndTime().compareTo(new Date())==-1){
            projectVo.setTimeStatus(TimeStatusEnum.TIME_STATUS_END.getValue());
        }else{
            if(projectVo.getStartTime().compareTo(new Date())==1){
                projectVo.setTimeStatus(TimeStatusEnum.TIME_STATUS_TRUE.getValue());
            }else {
                projectVo.setTimeStatus(TimeStatusEnum.TIME_STATUS_FALSE.getValue());
            }
        }

        //判断此活动用户是否已经报名
        ProjectUserSignEntity projectUserSignEntity = new ProjectUserSignEntity();
        projectUserSignEntity.setProjectId(id);
        //projectUserSignEntity.setUserId(userId);
        ProjectUserSignEntity projectUserSignEntity1 = projectUserSignService.selectOne(projectUserSignEntity);

        if(projectUserSignEntity1 == null){
            projectVo.setIsSign(IsSignEnum.IS_SIGN_ENUM_FALSE.getValue());
        }else {
            projectVo.setIsSign(IsSignEnum.IS_SIGN_ENUM_TRUE.getValue());
            projectVo.setSignInOrOut(projectUserSignEntity1.getStatus());
        }
        List<UserVo> userVoList = projectUserSignService.getUserVo(id);

        userVoList.forEach(a->{
          AllUserEntity entity =  allUserService.queryById(a.getUserId());
          if (entity==null){
              return;
          }
          Integer type = entity.getType();
          if (type==null){
              return;
          }

            if (type == 1){
                //查询志愿者表
                VolunteersReportsEntity vEntity = new VolunteersReportsEntity();
                vEntity.setUserId(a.getUserId());
                VolunteersReportsEntity volunteersReportsEntity  = volunteersReportsService.selectOne(vEntity);
                if (volunteersReportsEntity==null){
                    return;
                }
                a.setRealName(volunteersReportsEntity.getUserName());
                a.setPhone(volunteersReportsEntity.getUserPhone());
            }

            if (type == 2){
                CadresReportsEntity cEntity = new CadresReportsEntity();
                cEntity.setUserId(a.getUserId());
                CadresReportsEntity cadresReportsEntity = cadresReportsService.selectOne(cEntity);
                if (cadresReportsEntity==null){
                    return;
                }
                a.setRealName(cadresReportsEntity.getUserName());
                a.setPhone(cadresReportsEntity.getUserPhone());
            }

          if (type == 3){
              //获取单位报到信息
              UnitReportsEntity uEntity = new UnitReportsEntity();
              uEntity.setUserId(a.getUserId());
              UnitReportsEntity unitReportsEntity = unitReportsService.selectOne(uEntity);
              if (unitReportsEntity==null){
                  return;
              }
              UnitSecondClassEntity unitSecondClassEntity = unitSecondClassService.queryById(unitReportsEntity.getSecondId());
              if (unitSecondClassEntity==null){
                  return;
              }
              //最后添加姓名
              a.setRealName(unitSecondClassEntity.getName());
              a.setPhone(unitReportsEntity.getUserPhone());

          }

        });

        projectVo.setUserVoList(userVoList);
        List<CommentVo> commentVoList = projectCommentService.getCommentVoList(id);
        projectVo.setCommentVoList(commentVoList);
        // 判断用户是否是认领单位的负责人
        UnitReportsEntity unitReportsEntity = new UnitReportsEntity();
        unitReportsEntity.setSecondId(id);
        unitReportsEntity.setUserId(userId);
        int count = unitReportsService.count(unitReportsEntity);
        //isJudge 1.是单位负责人 2.不是单位负责人
        if(count >0){
            projectVo.setIsJudge(1);
        }else {
            projectVo.setIsJudge(2);
        }
        return projectVo;
    }

    /**
     * 项目签到接口
     * @param pid 项目id
     * @return
     */
    public int getSignIn(Long pid,Long userId){

        ProjectUserSignEntity projectUserSignEntity = new ProjectUserSignEntity();
        projectUserSignEntity.setUserId(userId);
        projectUserSignEntity.setProjectId(pid);
        ProjectUserSignEntity projectUserSignEntity1 =  projectUserSignService.selectOne(projectUserSignEntity);
        if(projectUserSignEntity1 != null && projectUserSignEntity1.getStatus().equals(SignEnum.SIGN_ENUM_ONE.getValue())){
            projectUserSignEntity1.setStatus(SignEnum.SIGN_ENUM_TWO.getValue());
            ProjectUserSignEntity projectUserSignEntity2 = projectUserSignService.update(projectUserSignEntity1);
            return 1;
        }else {
            return 0;
        }
    }

    /**
     * 项目签出接口
     * @param pid 项目id
     * @return
     */
    @Transactional
    public int getSignOut(Long pid,Long userId){
       ProjectEntity projectEntity = projectService.queryById(pid);
        ProjectUserSignEntity projectUserSignEntity = new ProjectUserSignEntity();
        projectUserSignEntity.setUserId(userId);
        projectUserSignEntity.setProjectId(pid);
        ProjectUserSignEntity projectUserSignEntity1 =  projectUserSignService.selectOne(projectUserSignEntity);
        if(getTime(projectEntity.getEndTime())){
            if(projectUserSignEntity1 != null && projectUserSignEntity1.getStatus().equals(SignEnum.SIGN_ENUM_TWO.getValue()) ){
                integralRecordService.getIntegralRecordEntity1(pid,userId);
                projectUserSignEntity1.setStatus(SignEnum.SIGN_ENUM_THREE.getValue());
                ProjectUserSignEntity projectUserSignEntity2 = projectUserSignService.update(projectUserSignEntity1);
                return 1;
            }else {
                return 0;
            }
        }else {
            return 3;
        }


    }

    /**
     * 判断签出是否再结束一个小时只内
     * @param date
     * @return
     */
    public Boolean getTime(Date date){
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.setTime(date);
        long lastly = c.getTimeInMillis();
        return (lastly - now) <= 3600000 & (lastly - now)>= 0;
    }

    /**
     * 项目没有开启签到  每天凌晨自动加积分
     * @return
     */
    public List<ProjectEntity> getList(){
        return super.mapper.getList();
    }

    public int getCountByall(){
        return super.mapper.getCountByall();
    }

}