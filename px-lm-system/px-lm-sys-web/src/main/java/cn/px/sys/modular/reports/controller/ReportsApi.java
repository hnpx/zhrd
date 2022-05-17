package cn.px.sys.modular.reports.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.px.base.core.AbstractController;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.sys.modular.activity.entity.ActivityEntity;
import cn.px.sys.modular.activity.service.ActivityService;
import cn.px.sys.modular.brigadeClass.service.BrigadeClassService;
import cn.px.sys.modular.cadresReports.entity.CadresReportsEntity;
import cn.px.sys.modular.cadresReports.service.CadresReportsService;
import cn.px.sys.modular.communityClass.entity.CommunityClassEntity;
import cn.px.sys.modular.communityClass.service.CommunityClassService;
import cn.px.sys.modular.doubleReport.entity.UnitEntity;
import cn.px.sys.modular.gridClass.service.GridClassService;
import cn.px.sys.modular.news.entity.News;
import cn.px.sys.modular.news.service.NesService;
import cn.px.sys.modular.project.entity.ProjectEntity;
import cn.px.sys.modular.project.service.ProjectService;
import cn.px.sys.modular.reports.service.ApiService;
import cn.px.sys.modular.unitFirstClass.service.UnitFirstClassService;
import cn.px.sys.modular.unitReports.entity.UnitReportsEntity;
import cn.px.sys.modular.unitReports.service.UnitReportsService;
import cn.px.sys.modular.unitSecondClass.entity.UnitSecondClassEntity;
import cn.px.sys.modular.unitSecondClass.service.UnitSecondClassService;
import cn.px.sys.modular.visitCount.entity.VisitCountEntity;
import cn.px.sys.modular.visitCount.service.VisitCountService;
import cn.px.sys.modular.volunteersClass.service.VolunteersClassService;
import cn.px.sys.modular.volunteersReports.entity.VolunteersReportsEntity;
import cn.px.sys.modular.volunteersReports.service.VolunteersReportsService;
import cn.px.sys.modular.wx.entity.AllUserEntity;
import cn.px.sys.modular.wx.service.AllUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jodd.util.URLDecoder;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/reports")
public class ReportsApi extends AbstractController {


    @Autowired
    private UnitFirstClassService unitFirstClassService;

    @Autowired
    private UnitSecondClassService unitSecondClassService;

    @Autowired
    private BrigadeClassService brigadeClassService;

    @Autowired
    private UnitReportsService unitReportsService;


    @Autowired
    private CadresReportsService cadresReportsService;


    @Autowired
    private VolunteersReportsService volunteersReportsService;


    @Autowired
    private ApiService apiService;

    @Autowired
    private NesService nesService;

    @Autowired
    private VolunteersClassService volunteersClassService;


    @Autowired
    private GridClassService gridClassService;


    @Autowired
    private AllUserService allUserService;

    @Autowired
    private CommunityClassService communityClassService;


    @Autowired
    private VisitCountService visitCountService;


    /*
        查询一级分类
     */
    @RequestMapping("/getUnitFirstlist")
    public Object getUnitFirstlist(){
        return setSuccessModelMap(unitFirstClassService.selectList());
    }
    /*
        根据一级id查询二级分类
     */
    @RequestMapping("/getUnitSecondList")
    public Object getUnitSecondList(Long id){
        return setSuccessModelMap(unitSecondClassService.selectListByid(id));
    }

    /*
        获取服务大队分类
     */
    @RequestMapping("/getBrigadeList")
    public Object getBrigadeList(){
        return setSuccessModelMap(brigadeClassService.selectList());
    }

    /*
        查询志愿者分类
     */

    @RequestMapping("/getVolunteersList")
    public Object getVolunteersClass(){
        return setSuccessModelMap(volunteersClassService.selectList());
    }

    /*
        网格分类
     */
    @RequestMapping("/getGridClassList")
    public Object getGridClassList(){
        return setSuccessModelMap(gridClassService.selectList());
    }


    /*
        单位报到
     */
    @Transactional
    @RequestMapping("/unitReports")
    public Object unitUpdate(UnitReportsEntity entity){

        UnitReportsEntity uEntity = new UnitReportsEntity();
        uEntity.setSecondId(entity.getSecondId());
        List<UnitReportsEntity> list = unitReportsService.queryList(uEntity);
        if (list.size()>0){
            return setModelMap("400","该单位已完成报到，请勿重复报到,可选择其他身份报到。");
        }

        UnitReportsEntity entity1 = new UnitReportsEntity();
        entity1.setUserId(entity.getUserId());
        UnitReportsEntity unitReportsEntity = unitReportsService.selectOne(entity1);
        if (unitReportsEntity == null){
            apiService.updateStatus(entity.getUserId(),3,entity.getUserPhone());
            return setSuccessModelMap(unitReportsService.update(entity));
        }else {
            return setModelMap("400","请勿重复报到");
        }


    }


    /*
        党员干部报到
     */
    @Transactional
    @RequestMapping("/updateCadresReports")
    public Object updateCadresReports(CadresReportsEntity entity){
        Map map = new HashMap();
        map.put("userId",entity.getUserId());
        List<CadresReportsEntity> list = cadresReportsService.query(map).getRecords();
        if (list.size()>0){
            AllUserEntity entity1 = allUserService.queryById(entity.getUserId());
            if (entity1.getStatus() == 3){
                return setModelMap("400","正在审核中");
            }
            if (entity1.getStatus() == 2) {
                return setModelMap("400","请勿重复签到");
            }
            if (entity1.getStatus() == 1){
                AllUserEntity userEntity  = new AllUserEntity();
                userEntity.setStatus(3);
                userEntity.setId(entity.getUserId());
                allUserService.update(userEntity);
                entity.setType(2);
                return setSuccessModelMap(cadresReportsService.update(entity));
            }
        }

        //先修改用户审核状态为审核状态
        AllUserEntity userEntity  = new AllUserEntity();
        userEntity.setStatus(3);
        userEntity.setId(entity.getUserId());
        allUserService.update(userEntity);
        entity.setType(2);
        return setSuccessModelMap(cadresReportsService.update(entity));

    }

    /*
        志愿者报到
     */
    @Transactional
    @RequestMapping("/updateVolunteersReports")
    public Object updateVolunteersReports(VolunteersReportsEntity entity){

        Map map = new HashMap();
        map.put("userId",entity.getUserId());
        List list = volunteersReportsService.query(map).getRecords();
        if (list.size()>0){
            return setModelMap("400","请勿重复报到");
        }else {
            apiService.updateStatus(entity.getUserId(),1,entity.getUserPhone());
            return setSuccessModelMap(volunteersReportsService.update(entity));
        }
    }

    /*
        首页信息
     */

    @RequestMapping("/gethome")
    public Object home(){
        return setSuccessModelMap(apiService.homeList());
    }

    /*
        快报列表
     */
    @RequestMapping("/newlist")
    public Object newList(@RequestParam(value = "page",
            required = false,
            defaultValue = "1")Integer page,
                          @RequestParam(value = "pageSize",
                                  required = false,
                                  defaultValue = "20") Integer pageSize){
        Page page1 = new Page(page,pageSize);
        return setSuccessModelMap(nesService.query(new News(), page1));
    }

    /*
        快报详情
     */
    @GetMapping("/read/newdetail/{id}")
    public Object selectOne(ModelMap modelMap, @PathVariable("id") Long id) {
        return super.setSuccessModelMap(modelMap, nesService.queryById(id));
    }


    /*
        党员干部报到列表(仅展示已审核通过的)
     */
    @RequestMapping("/cadresList")
    public Object cadresList(@RequestParam(value = "page",defaultValue = "1",required = false)Integer page,
                             @RequestParam(value = "pageSize",defaultValue = "20",required = false)Integer pageSize
                             ){
        Map map = new HashMap();
        Integer count = cadresReportsService.getCount() ;
        Page page1 = new Page(page,pageSize);
        Page<Map<String,Object>> mapPage = cadresReportsService.getAllById(page1);
        mapPage.getRecords().forEach(a->{
          String name =   URLDecoder.decode(a.get("user_name").toString(),"UTF-8");
          a.put("user_name",name);
        });

        if (count == null){
            count = 0;
        }
        map.put("count",count);
        map.put("list",mapPage);
        return setSuccessModelMap(map);
    }

    /*
        志愿者报到列表
        params {
            id:分类id
        }
     */
    @RequestMapping("/volunteersList")
    public Object volunteersList(@RequestParam(value = "page",defaultValue = "1",required = false)Integer page,
                                 @RequestParam(value = "pageSize",defaultValue = "20",required = false)Integer pageSize,
                                 @RequestParam(value = "id",required = true)Long id){
        Map map = new HashMap();
        Integer count = volunteersReportsService.getCount(id);
        Page page1 = new Page(page,pageSize);
        Page<Map<String,Object>> mapPage = volunteersReportsService.getAllById(page1,id);
        mapPage.getRecords().forEach(a->{
            String name =   URLDecoder.decode(a.get("user_name").toString(),"UTF-8");
            a.put("user_name",name);
        });
        map.put("count",count);
        map.put("list",mapPage);
        return setSuccessModelMap(map);
    }

    /*
        单位报到列表
     */
    @RequestMapping("/unitList")
    public Object unitList(@RequestParam(value = "page",defaultValue = "1",required = false)Integer page,
                             @RequestParam(value = "pageSize",defaultValue = "20",required = false)Integer pageSize
    ){
        Map map = new HashMap();
        Integer count = unitReportsService.getCount() ;
        Page page1 = new Page(page,pageSize);
        Page<Map<String,Object>> mapPage = unitReportsService.getAllById(page1);
        if (count == null){
            count = 0;
        }
        map.put("count",count);
        map.put("list",mapPage);
        return setSuccessModelMap(map);
    }



    @RequestMapping("/userForm")
    public Object userForm(Long id){

        AllUserEntity userEntity = allUserService.queryById(id);
        Page<Long> page = LayuiPageFactory.defaultPage();
        Integer status = userEntity.getStatus();
        if (status==null){
            status = 0;
        }

        Integer identity = userEntity.getType();
        if (identity == null){
            identity = 0;
        }


        if (status == 2 && identity == 1){
            VolunteersReportsEntity volunteersReportsEntity = new VolunteersReportsEntity();
            volunteersReportsEntity.setUserId(id);
            Page<Map<String,Object>> page1 = volunteersReportsService.selectListPage(page,volunteersReportsEntity);
            List<Map<String,Object>> map = page1.getRecords();
            map.forEach(a->{
                String bid = String.valueOf(a.get("brigade_class"));
                List ids = Arrays.asList(bid.split(","));
                a.put("brigade_class",brigadeClassService.getNameByList(ids));
            });

            return setSuccessModelMap(map.get(0));

        }
        if (status==2 && identity == 2){

            CadresReportsEntity cadresReportsEntity = new CadresReportsEntity();
            cadresReportsEntity.setUserId(id);
            Page<Map<String,Object>> page1 = cadresReportsService.selectListPage(page,cadresReportsEntity);
            List<Map<String,Object>> map = page1.getRecords();
            map.forEach(a->{
                String bid = String.valueOf(a.get("brigade_class"));
                List ids = Arrays.asList(bid.split(","));
                a.put("brigade_class",brigadeClassService.getNameByList(ids));
            });
            return setSuccessModelMap(map.get(0));
//            return setSuccessModelMap(cadresReportsService.query(map));
        }
        if (status== 2 && identity == 3){
            UnitReportsEntity unitReportsEntity = new UnitReportsEntity();
            unitReportsEntity.setUserId(id);
            List<Map<String,Object>> list =  unitReportsService.selectListPage(page,unitReportsEntity).getRecords();
            list.forEach(a->{
                String cid = String.valueOf(a.get("community_class"));
                CommunityClassEntity entity =  communityClassService.queryById(Long.valueOf(cid));
                a.put("communityClassName",entity.getName());
            });
            return setSuccessModelMap(list.get(0));
        }else {
            return setModelMap("400","您还未报到或正在审核中");
        }

    }



    @RequestMapping("/getUserSign")
    public Object getUserSign(@RequestParam("id")Long id){
        return setSuccessModelMap(apiService.selectUserSign(id));
    }

    @RequestMapping("/browse")
    public Object seeCount(VisitCountEntity entity){
        return visitCountService.update(entity);

    }

}
