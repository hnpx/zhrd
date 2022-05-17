package cn.px.sys.modular.project.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.px.base.auth.context.LoginContextHolder;
import cn.px.base.core.BaseController;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.base.support.Assert;
import cn.px.base.support.Pagination;
import cn.px.base.util.ExportExcelUtil;
import cn.px.sys.modular.ExcelUtil.ExcelBaseUtil;
import cn.px.sys.modular.activity.vo.ExcelVo;
import cn.px.sys.modular.activity.vo.UserVo;
import cn.px.sys.modular.activity.wrapper.UserWrapper;
import cn.px.sys.modular.cadresReports.entity.CadresReportsEntity;
import cn.px.sys.modular.cadresReports.service.CadresReportsService;
import cn.px.sys.modular.doubleReport.entity.UnitEntity;
import cn.px.sys.modular.doubleReport.service.UnitService;
import cn.px.sys.modular.integral.constant.TypeEnum;
import cn.px.sys.modular.project.constant.PersonnelTypeEnum;
import cn.px.sys.modular.project.entity.ProjectEntity;
import cn.px.sys.modular.project.entity.ProjectUserSignEntity;
import cn.px.sys.modular.project.service.ProjectEvaluateService;
import cn.px.sys.modular.project.service.ProjectService;
import cn.px.sys.modular.project.service.ProjectUserSignService;
import cn.px.sys.modular.system.vo.UserVO;
import cn.px.sys.modular.unitReports.entity.UnitReportsEntity;
import cn.px.sys.modular.unitReports.service.UnitReportsService;
import cn.px.sys.modular.unitSecondClass.entity.UnitSecondClassEntity;
import cn.px.sys.modular.unitSecondClass.service.UnitSecondClassService;
import cn.px.sys.modular.volunteersReports.entity.VolunteersReportsEntity;
import cn.px.sys.modular.volunteersReports.service.VolunteersReportsService;
import cn.px.sys.modular.wx.entity.AllUserEntity;
import cn.px.sys.modular.wx.service.AllUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.method.P;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (ProjectUserSign)表控制层
 *
 * @author
 * @since 2020-09-04 09:06:28
 */
@RestController
@RequestMapping("projectUserSign")
@Api(value = "(ProjectUserSign)管理")
public class ProjectUserSignController extends BaseController<ProjectUserSignEntity, ProjectUserSignService> {

    private static final String PREFIX = "/modular/projectUserSign";
    @Autowired
    private HttpServletRequest request;
    @Resource
    private UserWrapper userWrapper;
    @Resource
    private ProjectService projectService;
    @Resource
    private AllUserService allUserService;
    @Resource
    private UnitService unitService;
    @Resource
    private ProjectEvaluateService projectEvaluateService;

    @Autowired
    private VolunteersReportsService volunteersReportsService;


    @Autowired
    private UnitReportsService unitReportsService;


    @Autowired
    private CadresReportsService cadresReportsService;

    @Autowired
    private UnitSecondClassService unitSecondClassService;




    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/read/detail/{id}")
    @ResponseBody
    public Object selectOne(ModelMap modelMap, @PathVariable("id") Long id) {
        return super.setSuccessModelMap(modelMap, super.service.queryById(id));
    }

    @ApiOperation("查询")
    @PutMapping("/read/list")
    @ResponseBody
    public Object query(ModelMap modelMap, ProjectUserSignEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        Map<String, Object> paramsMap = params == null ? new HashMap<String, Object>() : BeanUtil.beanToMap(params);
        return super.query(modelMap, params, page);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @ResponseBody
    public Object update(ModelMap modelMap, ProjectUserSignEntity params) {

        //TODO 数据验证
        ProjectEntity projectEntity = projectService.queryById(params.getProjectId());
        if(new Date().compareTo(projectEntity.getEndTime())==1 ){
            return super.setModelMap("400", "此项目已经开始您不能报名");
        }
        if (projectEntity.getPersonnelType().equals(PersonnelTypeEnum.SIGN_ENUM_THREE.getValue())) {
            Long userId = LoginContextHolder.getContext().getUserId();
            AllUserEntity allUserEntity = allUserService.queryById(userId);

            if (allUserEntity.getType().equals(PersonnelTypeEnum.SIGN_ENUM_THREE.getValue())) {

                //如果身份是单位 去查询报到的单位是否与当前项目一致
                UnitReportsEntity uEntity = new UnitReportsEntity();
                uEntity.setUserId(userId);
                UnitReportsEntity unitReportsEntity = unitReportsService.selectOne(uEntity);
                Long unid = unitReportsEntity.getSecondId();
                if (unid.equals(projectEntity.getBelongingUnit())){
                    params.setUserId(LoginContextHolder.getContext().getUserId());
                    return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
                }else {
                    return super.setModelMap("400", "您不属于指定单位不能认领此项目");
                }

            }else if (allUserEntity.getType().equals(PersonnelTypeEnum.SIGN_ENUM_TWO.getValue())){
                CadresReportsEntity cEntity = new CadresReportsEntity();
                cEntity.setUserId(userId);
                CadresReportsEntity cadresReportsEntity = cadresReportsService.selectOne(cEntity);
                Long unid = cadresReportsEntity.getSecondId();
                if (unid.equals(projectEntity.getBelongingUnit())){
                    params.setUserId(LoginContextHolder.getContext().getUserId());
                    return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
                } else {
                    return super.setModelMap("400", "您不属于指定单位不能认领此项目");
                }

            } else {
                return super.setModelMap("400", "您不属于单位不能认领此项目");
            }
        } else {
            Integer i =projectEntity.getCurrentPerson();
               if(i == null){
                  i=0;
               }
            if (i < projectEntity.getMaxPerson()) {
                params.setUserId(LoginContextHolder.getContext().getUserId());
                ProjectUserSignEntity projectUserSignEntity = new ProjectUserSignEntity();
                projectUserSignEntity.setProjectId(params.getProjectId());
                projectUserSignEntity.setUserId(LoginContextHolder.getContext().getUserId());
                ProjectUserSignEntity p = super.service.selectOne(projectUserSignEntity);
                if (p == null) {
                    Integer currentPerson = i + 1;
                    projectEntity.setCurrentPerson(currentPerson);
                    projectService.update(projectEntity);
                    return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
                } else {
                    return super.setModelMap("400", "已认领不能再次被认领");
                }
            } else {
                return super.setModelMap("400", "认领人数已满，不能认领");
            }
        }

    }


//    @ApiOperation("手动报名")
//    @PostMapping("/update1")
//    @ResponseBody
//    public Object update1(ModelMap modelMap, ProjectUserSignEntity params) {
//
//        //TODO 数据验证
//        ProjectEntity projectEntity = projectService.queryById(params.getProjectId());
//        if(new Date().compareTo(projectEntity.getEndTime())==1 ){
//            return super.setModelMap("400", "此项目已经开始您不能报名");
//        }
//        if (projectEntity.getPersonnelType().equals(PersonnelTypeEnum.SIGN_ENUM_THREE.getValue())) {
//            Long userId = params.getUserId();
//            AllUserEntity allUserEntity = allUserService.queryById(userId);
//            if (allUserEntity.getType().equals(PersonnelTypeEnum.SIGN_ENUM_THREE.getValue())) {
//                UnitEntity unitEntity = unitService.getUnitEntityByPhone(allUserEntity.getPhone());
//                if (unitEntity.getId().equals(projectEntity.getBelongingUnit())) {
//                    params.setUserId(params.getUserId());
//                    return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
//                } else {
//                    return super.setModelMap("400", "您不属于指定单位不能认领此项目");
//                }
//            } else {
//                return super.setModelMap("400", "您不属于单位不能认领此项目");
//            }
//        } else {
//            Integer i =projectEntity.getCurrentPerson();
//            if(i == null){
//                i=0;
//            }
//            if (i < projectEntity.getMaxPerson()) {
//                params.setUserId(params.getUserId());
//                ProjectUserSignEntity projectUserSignEntity = new ProjectUserSignEntity();
//                projectUserSignEntity.setProjectId(params.getProjectId());
//                projectUserSignEntity.setUserId(params.getUserId());
//                ProjectUserSignEntity p = super.service.selectOne(projectUserSignEntity);
//                if (p == null) {
//                    Integer currentPerson = i + 1;
//                    projectEntity.setCurrentPerson(currentPerson);
//                    projectService.update(projectEntity);
//                    return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
//                } else {
//                    return super.setModelMap("400", "已认领不能再次被认领");
//                }
//            } else {
//                return super.setModelMap("400", "认领人数已满，不能认领");
//            }
//        }
//
//    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, ProjectUserSignEntity params) {
        Assert.notNull(params.getId(), "ID");
        return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("导出excel")
    @PutMapping("/excel")
    public Object exportExcel(ModelMap modelMap, ProjectUserSignEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        Pagination<ProjectUserSignEntity> p = super.service.query(params, page);
        List<ProjectUserSignEntity> data = p.getRecords();
        //TODO
        String fileName = ExportExcelUtil.doExportFile(new ExportParams(".xls", "ProjectUserSign"), ProjectUserSignEntity.class, data);
        return super.setSuccessModelMap(modelMap, fileName);
    }

    @ApiOperation("查询报名列表")
    @PutMapping("/read/listHome")
    @ResponseBody
    public Object query(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer pageSize,@RequestParam(required = false) Long projectId) {
     Page<Map<String,Object>> user = super.service.getPageList(page,pageSize,projectId);

        user.getRecords().forEach(a->{
            AllUserEntity allUserEntity = allUserService.queryById((Long) a.get("userId"));

            if (allUserEntity==null){
                return;
            }
            Integer userType = allUserEntity.getType();


            if (userType ==null){
                return;
            }

            if (userType == 1){
                //查询志愿者表
                VolunteersReportsEntity vEntity = new VolunteersReportsEntity();
                vEntity.setUserId((Long) a.get("userId"));
                VolunteersReportsEntity volunteersReportsEntity  = volunteersReportsService.selectOne(vEntity);
                if (volunteersReportsEntity == null){
                    return;
                }
                a.put("realName",volunteersReportsEntity.getUserName());
                a.put("phone",volunteersReportsEntity.getUserPhone());
            }

            if (userType == 2){
                CadresReportsEntity cEntity = new CadresReportsEntity();
                cEntity.setUserId((Long) a.get("userId"));
                CadresReportsEntity cadresReportsEntity = cadresReportsService.selectOne(cEntity);
                if (cadresReportsEntity == null){
                    return;
                }
                a.put("realName",cadresReportsEntity.getUserName());
                a.put("phone",cadresReportsEntity.getUserPhone());
            }
            if (userType==3){
                UnitReportsEntity uEntity = new UnitReportsEntity();
                uEntity.setUserId((Long) a.get("userId"));
                UnitReportsEntity unitReportsEntity = unitReportsService.selectOne(uEntity);
                if (unitReportsEntity == null){
                    return;
                }

                UnitSecondClassEntity unitSecondClassEntity = unitSecondClassService.queryById(unitReportsEntity.getSecondId());
                a.put("realName",unitSecondClassEntity.getName());
                a.put("phone",unitReportsEntity.getUserPhone());
            }
        });


      Page<UserVo> pv = userWrapper.getVoPage(user);
      return super.setSuccessModelMap(pv);
    }


    @ApiOperation("我认领的")
    @PutMapping("/read/listMySelf")
    @ResponseBody
    public Object query(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer pageSize,@RequestParam(required = false) Integer status){
        Long userId =  LoginContextHolder.getContext().getUserId();
        Page<Map<String,Object>> project = super.service.getListByUserId(page,pageSize,userId,status);
        return super.setSuccessModelMap(project);

    }

   /* @Scheduled(cron = "0 0 0 * * ?")*/
    @ApiOperation("未开启签到每天凌晨给人加积分")
    @PostMapping("/read/integeral")
    @ResponseBody
    public Object query1() {
        super.service.getIntegeral();
        return super.setSuccessModelMap();
    }



    @ApiOperation("表现档案")
    @PutMapping("/read/erformance")
    public Object query(@RequestParam(required = false) Long userId,@RequestParam(required = false) String projectName){
     Page<Map<String,Object>> ob = super.service.getPerformance(userId,projectName);
     ob.getRecords().forEach(a->{
         Long pid = Long.parseLong(a.get("projectId").toString());
        int n = projectEvaluateService.getCount(pid,userId);
        if(n>0){
            a.put("type",0);  //可以查看评价
        }else {
            a.put("type",1);   //可以评价
        }

        });

     return super.setSuccessModelMap(ob);

    }

    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
    public void heihei(HttpServletResponse response, HttpServletRequest request,
                       ExcelVo vo
    )  {

            List<ExcelVo> list = service.importExl(vo.getId());
            String name = service.exlName(vo.getId());
            if (list.size()>0){
                list.forEach(a->{
                    AllUserEntity entity = allUserService.queryById(a.getUserId());
                    if (entity==null){
                        return;
                    }

                    Integer userType = entity.getType();
                    if (userType ==null){
                        return;
                    }

                    if (userType == 1){
                        //查询志愿者表
                        VolunteersReportsEntity vEntity = new VolunteersReportsEntity();
                        vEntity.setUserId(a.getUserId());
                        VolunteersReportsEntity volunteersReportsEntity  = volunteersReportsService.selectOne(vEntity);
                        if (volunteersReportsEntity ==null){
                            return;
                        }
                        a.setRealName(volunteersReportsEntity.getUserName());
                        a.setPhone(volunteersReportsEntity.getUserPhone());
                    }

                    if (userType == 2){
                        CadresReportsEntity cEntity = new CadresReportsEntity();
                        cEntity.setUserId(a.getUserId());
                        CadresReportsEntity cadresReportsEntity = cadresReportsService.selectOne(cEntity);
                        if (cadresReportsEntity ==null){
                            return;
                        }
                        a.setRealName(cadresReportsEntity.getUserName());
                        a.setPhone(cadresReportsEntity.getUserPhone());
                    }
                    if (userType==3){
                        UnitReportsEntity uEntity = new UnitReportsEntity();
                        uEntity.setUserId(a.getUserId());
                        UnitReportsEntity unitReportsEntity = unitReportsService.selectOne(uEntity);
                        if (unitReportsEntity == null){
                            return;
                        }

                        UnitSecondClassEntity unitSecondClassEntity = unitSecondClassService.queryById(unitReportsEntity.getSecondId());
                        if (unitSecondClassEntity ==null){
                            return;
                        }
                        a.setRealName(unitSecondClassEntity.getName());
                        a.setPhone(unitReportsEntity.getUserPhone());
                    }
                });

            }






            //模拟从数据库获取需要导出的数据

            ExcelBaseUtil.exportExcel(list,name,"项目签到表",ExcelVo.class,"项目签到表.xls",response);


    }
}