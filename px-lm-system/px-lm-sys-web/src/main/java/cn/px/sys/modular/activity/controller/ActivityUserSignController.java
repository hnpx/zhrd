package cn.px.sys.modular.activity.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.px.base.auth.context.LoginContextHolder;
import cn.px.base.core.BaseController;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.base.support.Assert;
import cn.px.base.support.Pagination;
import cn.px.base.util.ExportExcelUtil;
import cn.px.sys.modular.ExcelUtil.ExcelBaseUtil;
import cn.px.sys.modular.activity.entity.ActivityEntity;
import cn.px.sys.modular.activity.entity.ActivityUserSignEntity;
import cn.px.sys.modular.activity.service.ActivityService;
import cn.px.sys.modular.activity.service.ActivityUserSignService;
import cn.px.sys.modular.activity.vo.ActivityUserVo;
import cn.px.sys.modular.activity.vo.ExcelVo;
import cn.px.sys.modular.activity.vo.UserVo;
import cn.px.sys.modular.activity.wrapper.ActivityUserWrapper;
import cn.px.sys.modular.activity.wrapper.UserWrapper;
import cn.px.sys.modular.cadresReports.entity.CadresReportsEntity;
import cn.px.sys.modular.cadresReports.service.CadresReportsService;
import cn.px.sys.modular.system.entity.User;
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
 * (ActivityUserSign)????????????
 *
 * @author
 * @since 2020-08-31 11:08:05
 */
@RestController
@RequestMapping("activityUserSign")
@Api(value = "(ActivityUserSign)??????")
public class ActivityUserSignController extends BaseController<ActivityUserSignEntity, ActivityUserSignService> {

    private static final String PREFIX = "/modular/activityUserSign";
    @Autowired
    private HttpServletRequest request;
    @Resource
    private AllUserService allUserService;
    @Resource
    private ActivityUserWrapper activityUserWrapper;
    @Resource
    private UserWrapper userWrapper;

    @Resource
    private ActivityService activityService;


    @Autowired
    private VolunteersReportsService volunteersReportsService;


    @Autowired
    private CadresReportsService cadresReportsService;

    @Autowired
    private UnitReportsService unitReportsService;



    @Autowired
    private UnitSecondClassService unitSecondClassService;


    /**
     * ??????????????????????????????
     *
     * @param id ??????
     * @return ????????????
     */
    @GetMapping("/read/detail/{id}")
    @ResponseBody
    public Object selectOne(ModelMap modelMap, @PathVariable("id") Long id) {
        return super.setSuccessModelMap(modelMap, super.service.queryById(id));
    }

    @ApiOperation("??????")
    @PutMapping("/read/list")
    @ResponseBody
    public Object query(ModelMap modelMap, ActivityUserSignEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        Map<String, Object> paramsMap = params == null ? new HashMap<String, Object>() : BeanUtil.beanToMap(params);



        return super.query(modelMap, params, page);
    }

    @ApiOperation("??????")
    @PostMapping("/update")
    @ResponseBody
    public Object update(ModelMap modelMap, ActivityUserSignEntity params) {
        //TODO ????????????
       ActivityEntity activityEntity= activityService.queryById(params.getActivityId());

        if(new Date().compareTo(activityEntity.getEndTime())==1 ){
            return super.setModelMap("400", "????????????????????????????????????");
        }
//
//        Long userId = LoginContextHolder.getContext().getUserId();
//
//        AllUserEntity allUserEntity = allUserService.queryById(userId);
//
//        //???????????? 1:????????? 2????????? 3?????????
//        Integer userType = allUserEntity.getType();
//
//        //?????????????????? 1:????????? 2??????????????? 3?????????
//        Integer activityType = activityEntity.getUserType();
//
//        //???????????????????????????
//        if (activityType == 2){
//            //?????????????????????
//            if (userType == 3 || userType == null || "".equals(userType)){
//                return setModelMap("400","???????????????????????????????????????????????????");
//            }
//            //????????????id
//            Long bid = activityEntity.getBrigadeClass();
//            //????????????????????????????????????
//            if (userType == 1){
//                //?????????????????? ?????????????????????????????????
//               Integer isJoin =  volunteersReportsService.isBrigadeClass(userId,bid);
//               if (isJoin ==null || isJoin ==0){
//                   return setModelMap("400","???????????????????????????????????????????????????");
//               }
//            }
//            //??????????????????????????????????????????
//            if (userType == 2){
//                //?????????????????????????????????????????????
//                Integer isJoin = cadresReportsService.isBrigadeClass(userId,bid);
//                if (isJoin ==null || isJoin ==0){
//                    return setModelMap("400","???????????????????????????????????????????????????");
//                }
//
//            }
//
//        }
//
//        //?????????????????????
//        if (activityType == 3){
//            //?????????????????? ????????????
//            if (userType == 1 || userType == null || "".equals(userType)){
//                return setModelMap("400","??????????????????????????????????????????");
//            }
//            Long unitId = activityEntity.getSecondClass();
//            //????????????????????? ?????????????????????????????????
//            if (userType == 2){
//                CadresReportsEntity cEntity = new CadresReportsEntity();
//                cEntity.setUserId(userId);
//                cEntity.setSecondId(unitId);
//                CadresReportsEntity cadresReportsEntity = cadresReportsService.selectOne(cEntity);
//                if (cadresReportsEntity == null){
//                    return setModelMap("400","??????????????????????????????????????????");
//                }
//
//            }
//            //????????????
//            if (userType == 3){
//                UnitReportsEntity uEntity  = new UnitReportsEntity();
//                uEntity.setUserId(userId);
//                uEntity.setSecondId(unitId);
//                UnitReportsEntity unitReportsEntity = unitReportsService.selectOne(uEntity);
//                if (unitReportsEntity == null){
//                    return setModelMap("400","??????????????????????????????????????????");
//                }
//            }
//        }



      Integer i =  activityEntity.getCurrentPerson();
      if(i==null){
          i=0;
      }
       if(i < activityEntity.getMaxPerson()){
           params.setUserId(LoginContextHolder.getContext().getUserId());
           //????????????????????????
           activityEntity.setCurrentPerson(i + 1);
           activityService.update(activityEntity);
           return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
       }else {
         return super.setModelMap("400","??????????????????");

       }

    }

//    @ApiOperation("??????????????????")
//    @PostMapping("/update/report")
//    @ResponseBody
//    public Object update1(ModelMap modelMap, ActivityUserSignEntity params) {
//        //TODO ????????????
//        ActivityEntity activityEntity= activityService.queryById(params.getActivityId());
//        Integer i =  activityEntity.getCurrentPerson();
//        if(i==null){
//            i=0;
//        }
//        if(i < activityEntity.getMaxPerson()){
//            params.setUserId(params.getUserId());
//            //????????????????????????
//            activityEntity.setCurrentPerson(i + 1);
//            activityService.update(activityEntity);
//            return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
//        }else {
//            return super.setModelMap("400","??????????????????");
//
//        }
//
//    }

    @ApiOperation("??????")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, ActivityUserSignEntity params) {
        Assert.notNull(params.getId(), "ID");
        return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("??????excel")
    @PutMapping("/excel")
    public Object exportExcel(ModelMap modelMap, ActivityUserSignEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        Pagination<ActivityUserSignEntity> p = super.service.query(params, page);
        List<ActivityUserSignEntity> data = p.getRecords();
        //TODO
        String fileName = ExportExcelUtil.doExportFile(new ExportParams(".xls", "ActivityUserSign"), ActivityUserSignEntity.class, data);
        return super.setSuccessModelMap(modelMap, fileName);
    }

    @ApiOperation("????????????id????????????????????????")
    @PutMapping("/read/listByUserId")
    @ResponseBody
    public Object query(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer pageSize,@RequestParam("status") Integer status) {
      Long userId =  LoginContextHolder.getContext().getUserId();
      Page<Map<String,Object>> activityUser =  super.service.getListByUserId(page,pageSize,userId,status);
      Page<ActivityUserVo> pv = activityUserWrapper.getVoPage(activityUser);
      return super.setSuccessModelMap(pv);
    }


    @ApiOperation("????????????id??????????????????")
    @PutMapping("/read/listUserByUserId")
    @ResponseBody
    public Object query1(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer pageSize,@RequestParam("activeId") Long activeId) {
        Page<Map<String,Object>> activityUser =  super.service.getListSignByUserId(page,pageSize,activeId);

        activityUser.getRecords().forEach(a->{
            AllUserEntity allUserEntity = allUserService.queryById((Long) a.get("userId"));

            if (allUserEntity == null){
                return;
            }
            Integer userType = allUserEntity.getType();;


            if (userType ==null){
                return;
            }

            if (userType == 1){
                //??????????????????
                VolunteersReportsEntity vEntity = new VolunteersReportsEntity();
                vEntity.setUserId((Long) a.get("userId"));
                VolunteersReportsEntity volunteersReportsEntity  = volunteersReportsService.selectOne(vEntity);
                if (volunteersReportsEntity==null){
                    return;
                }
                a.put("realName",volunteersReportsEntity.getUserName());
                a.put("phone",volunteersReportsEntity.getUserPhone());
            }

            if (userType == 2){
                CadresReportsEntity cEntity = new CadresReportsEntity();
                cEntity.setUserId((Long) a.get("userId"));
                CadresReportsEntity cadresReportsEntity = cadresReportsService.selectOne(cEntity);
                if (cadresReportsEntity==null){
                    return;
                }
                a.put("realName",cadresReportsEntity.getUserName());
                a.put("phone",cadresReportsEntity.getUserPhone());
            }
            if (userType==3){
                UnitReportsEntity uEntity = new UnitReportsEntity();
                uEntity.setUserId((Long) a.get("userId"));
                UnitReportsEntity unitReportsEntity = unitReportsService.selectOne(uEntity);
                if (unitReportsEntity==null){
                    return;
                }
                UnitSecondClassEntity unitSecondClassEntity = unitSecondClassService.queryById(unitReportsEntity.getSecondId());
                if (unitSecondClassEntity == null){
                    return;
                }
                a.put("realName",unitSecondClassEntity.getName());
                a.put("phone",unitReportsEntity.getUserPhone());
            }
        });


        Page<UserVo> pv = userWrapper.getVoPage(activityUser);
        return super.setSuccessModelMap(pv);
    }

   // @Scheduled(cron = "0 0 0 * * ?")
    @ApiOperation("??????????????????????????????????????????")
    @PostMapping("/read/integeral")
    @ResponseBody
    public Object query1() {
       super.service.getIntegeral();
       return super.setSuccessModelMap();
    }


//    @RequestMapping(value = "/exportXls")
//    public Object exl(ExcelVo vo){
//        List<ExcelVo> list = service.importExl(vo.getId());
//        String name = service.exlName(vo.getId());
//
//        Map map = new HashMap();
//        map.put("list",list);
//        map.put("name",name);
//
//        return setSuccessModelMap(map);
//
//    }

    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
    public void heihei(HttpServletResponse response,HttpServletRequest request,
                       ExcelVo vo
                       ) {

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
                        //??????????????????
                        VolunteersReportsEntity vEntity = new VolunteersReportsEntity();
                        vEntity.setUserId(a.getUserId());
                        VolunteersReportsEntity volunteersReportsEntity  = volunteersReportsService.selectOne(vEntity);
                        if (volunteersReportsEntity==null){
                            return;
                        }
                        a.setRealName(volunteersReportsEntity.getUserName());
                        a.setPhone(volunteersReportsEntity.getUserPhone());
                    }
                    if (userType == 2){
                        CadresReportsEntity cEntity = new CadresReportsEntity();
                        cEntity.setUserId(a.getUserId());
                        CadresReportsEntity cadresReportsEntity = cadresReportsService.selectOne(cEntity);
                        if (cadresReportsEntity==null){
                            return;
                        }
                        a.setRealName(cadresReportsEntity.getUserName());
                        a.setPhone(cadresReportsEntity.getUserPhone());
                    }
                    if (userType==3){
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
                        a.setRealName(unitSecondClassEntity.getName());
                        a.setPhone(unitReportsEntity.getUserPhone());
                    }
                });

            }

            //?????????????????????????????????????????????

            ExcelBaseUtil.exportExcel(list,name,"???????????????",ExcelVo.class,"???????????????.xls",response);




    }
}