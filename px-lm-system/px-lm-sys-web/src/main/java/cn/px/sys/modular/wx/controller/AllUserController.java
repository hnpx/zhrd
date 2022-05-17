package cn.px.sys.modular.wx.controller;

import cn.px.sys.modular.activity.entity.ActiveCommentEntity;
import cn.px.sys.modular.activity.entity.ActivityEntity;
import cn.px.sys.modular.activity.entity.ActivityUserSignEntity;
import cn.px.sys.modular.activity.service.ActiveCommentService;
import cn.px.sys.modular.activity.service.ActivityService;
import cn.px.sys.modular.activity.service.ActivityUserSignService;
import cn.px.sys.modular.cadresReports.entity.CadresReportsEntity;
import cn.px.sys.modular.cadresReports.service.CadresReportsService;
import cn.px.sys.modular.integral.entity.IntegralRecordEntity;
import cn.px.sys.modular.integral.service.IntegralRecordService;
import cn.px.sys.modular.project.entity.ProjectCommentEntity;
import cn.px.sys.modular.project.entity.ProjectUserSignEntity;
import cn.px.sys.modular.project.service.ProjectCommentService;
import cn.px.sys.modular.project.service.ProjectUserSignService;
import cn.px.sys.modular.spike.entity.OrdersEntity;
import cn.px.sys.modular.spike.service.OrdersService;
import cn.px.sys.modular.unitReports.entity.UnitReportsEntity;
import cn.px.sys.modular.unitReports.service.UnitReportsService;
import cn.px.sys.modular.volunteersReports.entity.VolunteersReportsEntity;
import cn.px.sys.modular.volunteersReports.service.VolunteersReportsService;
import cn.px.sys.modular.wx.entity.AllUserEntity;
import cn.px.sys.modular.wx.service.AllUserService;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.sys.modular.wx.vo.AllUserVo;
import cn.px.sys.modular.wx.vo.CadreVo;
import cn.px.sys.modular.wx.vo.UserReVo;
import cn.px.sys.modular.wx.vo.WxUserVo;
import cn.px.sys.modular.wx.wrapper.AllUserWrapper;
import cn.px.sys.modular.wx.wrapper.UserReWrapper;
import cn.stylefeng.roses.kernel.model.response.SuccessResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.internal.$Gson$Preconditions;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import cn.px.base.auth.context.LoginContextHolder;
import io.swagger.annotations.Api;
import cn.px.base.core.BaseController;
import cn.px.base.support.Assert;
import cn.hutool.core.bean.BeanUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.px.base.support.Pagination;
import cn.px.base.util.ExportExcelUtil;


import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * (AllUser)表控制层
 *
 * @author
 * @since 2020-08-29 11:03:35
 */
/*@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务*/
@RestController
@RequestMapping("allUser")
@Api(value = "(AllUser)管理")
public class AllUserController extends BaseController<AllUserEntity, AllUserService> {

    private static final String PREFIX = "/modular/allUser";
    @Autowired
    private HttpServletRequest request;

    @Resource
    private AllUserWrapper allUserWrapper;

    @Resource
    private UserReWrapper userReWrapper;

    @Resource
    private ActivityService activityService;

    @Autowired
    private UnitReportsService unitReportsService;

    @Autowired
    private VolunteersReportsService volunteersReportsService;

    @Autowired
    private CadresReportsService cadresReportsService;

    @Autowired
    private IntegralRecordService integralRecordService;

    @Autowired
    private ActivityUserSignService activityUserSignService;

    @Autowired
    private ActiveCommentService activeCommentService;

    @Autowired
    private ProjectUserSignService projectUserSignService;

    @Autowired
    private ProjectCommentService projectCommentService;

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private AllUserService allUserService;



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
    public Object query(ModelMap modelMap, AllUserEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        Map<String, Object> paramsMap = params == null ? new HashMap<String, Object>() : BeanUtil.beanToMap(params);
        return super.query(modelMap, params, page);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @ResponseBody
    public Object update(ModelMap modelMap, AllUserEntity params) {
        //TODO 数据验证
        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, AllUserEntity params) {
        Assert.notNull(params.getId(), "ID");
        return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("导出excel")
    @PutMapping("/excel")
    public Object exportExcel(ModelMap modelMap, AllUserEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        Pagination<AllUserEntity> p = super.service.query(params, page);
        List<AllUserEntity> data = p.getRecords();
        //TODO
        String fileName = ExportExcelUtil.doExportFile(new ExportParams(".xls", "AllUser"), AllUserEntity.class, data);
        return super.setSuccessModelMap(modelMap, fileName);
    }


    @ApiOperation("查询用户信息")
    @PutMapping("/read/AllUserList")
    @ResponseBody
    public Object query(@RequestParam("openid") String openid) {
      AllUserEntity  allUser = super.service.readByOpenid(openid);
      AllUserVo allUserVo = super.service.getAllUserVo(allUser);


        Integer userType = null;
      if (allUserVo !=null){
          userType = allUser.getType();

          if (userType==null){
              userType=0;
          }

          if (userType == 3){
              UnitReportsEntity uEntity = new UnitReportsEntity();
              uEntity.setUserId(allUserVo.getId());
              UnitReportsEntity unitReportsEntity = unitReportsService.selectOne(uEntity);
              if (unitReportsEntity !=null){
                  allUserVo.setBelongingUnit(unitReportsEntity.getSecondId());
              }
          }
      }

      return super.setSuccessModelMap(allUserVo);
    }


    @ApiOperation("双报到")
    @PostMapping("/read/report")
    @ResponseBody
    public Object query() {
       Long userId = LoginContextHolder.getContext().getUserId();
      int  i =  super.service.report(userId);
      if(i==1){
          return super.setSuccessModelMap();
      }else if(i==0){
          return super.setModelMap("400","您的身份不符合双报到");
      } else if(i==2){
          return super.setModelMap("400","您今年已经进行双报到");
      }else {
          return super.setSuccessModelMap();
      }

    }


  //  @Scheduled(cron = "0 0 0 1 1 ?")
    @ApiOperation("双报到初始化信息")
    @PostMapping("/read/reportInit")
    @ResponseBody
    public Object query1() {
   //  super.service.initReport();
     return super.setSuccessModelMap();
    }


    @ApiOperation("双报到用户信息")
    @PostMapping("/read/getCardreIntegral")
    @ResponseBody
    public Object getCardreIntegral(@RequestParam("page") Integer page,@RequestParam("pageSize") Integer pageSize){
      Page<Map<String,Object>> user =  super.service.getAllUserEntityByType(page,pageSize);
      Page<CadreVo> pv = allUserWrapper.getVoPage(user);
      return super.setSuccessModelMap(pv);
    }

    @ApiOperation("根据用户id查询内容")
    @PostMapping("/update/id")
    @ResponseBody
    public Object update(@RequestParam("userId") Long userId) {
     return super.setSuccessModelMap(super.service.getName(userId));

    }


    @ApiOperation("查询用户信息报名")
    @PutMapping("/read/AllUserList/report")
    @ResponseBody
    public Object query(@RequestParam(required = false) Long activeId, @RequestParam(required = false) String nickname) {

          Page<Map<String,Object>> user =  super.service.getListUser(activeId,nickname);
          Page<UserReVo> pv = userReWrapper.getVoPage(user,activeId);
          return super.setSuccessModelMap(pv);
    }


    @ApiOperation("查询用户信息报名")
    @PutMapping("/read/AllUserList/report1")
    @ResponseBody
    public Object query1(@RequestParam(required = false) Long pid,@RequestParam(required = false) String nickname) {

        Page<Map<String,Object>> user =  super.service.getListUser1(pid,nickname);
        Page<UserReVo> pv = userReWrapper.getVoPage(user,pid);
        return super.setSuccessModelMap(pv);
    }




    @ApiOperation("删除")
    @DeleteMapping("/deleteUser")
    @ResponseBody
    public Object del(Long userId) {


        //删除单位报到记录
        UnitReportsEntity unitReportsEntity = new UnitReportsEntity();
        unitReportsEntity.setUserId(userId);
        unitReportsService.deleteByEntity(unitReportsEntity);



        //删除志愿者报到记录
        VolunteersReportsEntity volunteersReportsEntity = new VolunteersReportsEntity();
        volunteersReportsEntity.setUserId(userId);
        volunteersReportsService.deleteByEntity(volunteersReportsEntity);



        //删除党员干部报到记录
        CadresReportsEntity cadresReportsEntity = new CadresReportsEntity();
        cadresReportsEntity.setUserId(userId);
        cadresReportsService.deleteByEntity(cadresReportsEntity);

        //删除积分记录
        IntegralRecordEntity integralRecordEntity = new IntegralRecordEntity();
        integralRecordEntity.setUserId(userId);
        integralRecordService.deleteByEntity(integralRecordEntity);


        //删除活动报名记录
        ActivityUserSignEntity activityUserSignEntity = new ActivityUserSignEntity();
        activityUserSignEntity.setUserId(userId);
        activityUserSignService.deleteByEntity(activityUserSignEntity);


        //删除活动评价记录
        ActiveCommentEntity activeCommentEntity = new ActiveCommentEntity();
        activeCommentEntity.setUserId(userId);
        activeCommentService.deleteByEntity(activeCommentEntity);

        //删除项目报名记录
        ProjectUserSignEntity projectUserSignEntity = new ProjectUserSignEntity();
        projectUserSignEntity.setUserId(userId);
        projectUserSignService.deleteByEntity(projectUserSignEntity);

        //删除项目评价记录
        ProjectCommentEntity projectCommentEntity = new ProjectCommentEntity();
        projectCommentEntity.setUserId(userId);
        projectCommentService.deleteByEntity(projectCommentEntity);

        //删除订单记录
        OrdersEntity ordersEntity = new OrdersEntity();
        ordersEntity.setUserId(userId);
        ordersService.deleteByEntity(ordersEntity);

        //删除授权记录
        AllUserEntity entity = new AllUserEntity();
        entity.setId(userId);
        service.deleteByEntity(entity);

        return setSuccessModelMap();
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/user/getOne")
    public Object getOne(@RequestParam("uid") Long uid){
        AllUserEntity allUserEntity =  allUserService.queryById(uid);
        WxUserVo wxUserVo = new WxUserVo();
        BeanUtil.copyProperties(allUserEntity,wxUserVo,true);
        return super.setSuccessModelMap(wxUserVo);
    }
    /**
     * 修改用户信息
     */
    @GetMapping("/user/updata")
    public Object updataUser(@RequestParam("uid") Long uid,@RequestParam("userType") Integer usertype){
        AllUserEntity allUserEntity =  allUserService.queryById(uid);
        allUserEntity.setUserType(usertype);
        allUserService.update(allUserEntity);
        WxUserVo wxUserVo = new WxUserVo();
        BeanUtil.copyProperties(allUserEntity,wxUserVo,true);
        return super.setSuccessModelMap(wxUserVo);
    }






}