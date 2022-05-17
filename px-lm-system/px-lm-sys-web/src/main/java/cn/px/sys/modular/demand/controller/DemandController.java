package cn.px.sys.modular.demand.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.px.base.auth.context.LoginContextHolder;
import cn.px.base.core.BaseController;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.base.support.Assert;
import cn.px.base.support.Pagination;
import cn.px.base.util.ExportExcelUtil;
import cn.px.sys.modular.activity.service.ActivityService;
import cn.px.sys.modular.communityClass.entity.CommunityClassEntity;
import cn.px.sys.modular.communityClass.service.CommunityClassService;
import cn.px.sys.modular.demand.entity.DemandEntity;
import cn.px.sys.modular.demand.service.DemandService;
import cn.px.sys.modular.demand.vo.DemandVo;
import cn.px.sys.modular.demand.vo.DemandVo1;
import cn.px.sys.modular.demand.wrapper.DemandWrapper;
import cn.px.sys.modular.project.service.ProjectService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jodd.util.URLDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 需求管理(Demand)表控制层
 *
 * @author
 * @since 2020-09-02 19:38:12
 */
@RestController
@RequestMapping("demand")
@Api(value = "需求管理(Demand)管理")
public class DemandController extends BaseController<DemandEntity, DemandService> {

    private static final String PREFIX = "/modular/demand";
    @Autowired
    private HttpServletRequest request;
    @Resource
    private ProjectService projectService;
    @Resource
    private DemandWrapper demandWrapper;
    @Resource
    private ActivityService activityService;
    @Resource
    private CommunityClassService communityClassService;


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/read/detail/{id}")
    @ResponseBody
    public Object selectOne(ModelMap modelMap, @PathVariable("id") Long id) {



        DemandEntity entity = super.service.queryById(id);

        entity.setContent(URLDecoder.decode(entity.getContent().toString(),"UTF-8"));

        return super.setSuccessModelMap(modelMap,entity);
    }

    @ApiOperation("查询")
    @PutMapping("/read/list")
    @ResponseBody
    public Object query(ModelMap modelMap, DemandEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        Map<String, Object> paramsMap = params == null ? new HashMap<String, Object>() : BeanUtil.beanToMap(params);
        return super.query(modelMap, params, page);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @ResponseBody
    public Object update(ModelMap modelMap, DemandEntity params) {
        //TODO 数据验证
        Long userId =  LoginContextHolder.getContext().getUserId();
        Map<String,Object> map  = super.service.get(userId);
        params.setCid(Long.parseLong(map.get("cid").toString()));
        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("审核")
    @PostMapping("/detail")
    @ResponseBody
    @Transactional
    public Object detail(ModelMap modelMap, DemandEntity params) throws UnsupportedEncodingException {

        params.setContent(URLEncoder.encode(params.getContent(),"UTF-8"));

        //TODO 数据验证
        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, DemandEntity params) {
        Assert.notNull(params.getId(), "ID");
        return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("导出excel")
    @PutMapping("/excel")
    public Object exportExcel(ModelMap modelMap, DemandEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        Pagination<DemandEntity> p = super.service.query(params, page);
        List<DemandEntity> data = p.getRecords();
        //TODO
        String fileName = ExportExcelUtil.doExportFile(new ExportParams("需求管理.xls", "Demand"), DemandEntity.class, data);
        return super.setSuccessModelMap(modelMap, fileName);
    }

    @ApiOperation("查询列表")
    @PutMapping("/read/listHome")
    @ResponseBody
    public Object query(@RequestParam(required = false) String name,@RequestParam(required = false) Integer status,@RequestParam(required = false) String phone,@RequestParam(required = false) Long demandClass) {
        Long userId = LoginContextHolder.getContext().getUserId();
        Page<Map<String,Object>> demand = super.service.getListPage(name,status,userId,phone,demandClass);
        Page<DemandVo> pv = demandWrapper.getVoPage(demand);
        return super.setSuccessModelMap(pv);
       /* if(userId == 1){
            Page<Map<String,Object>> demand = super.service.getListPage(name,status,userId,phone,demandClass);
            Page<DemandVo> pv = demandWrapper.getVoPage(demand);
            return super.setSuccessModelMap(pv);
        }else {
         CommunityClassEntity communityClassEntity = communityClassService.getCommunityClassEntityByUserId(userId);
            Page<Map<String,Object>> demand = super.service.getListPage(name,status,userId,phone,demandClass);
            Page<DemandVo> pv = demandWrapper.getVoPage(demand);
            Page<DemandVo> pv1 = new Page<DemandVo>();
            List<DemandVo> demandVoList = new ArrayList<>();
            pv.getRecords().forEach(a->{
            Map<String,Object> map = super.service.get(a.getCreateBy());
            if(map.get("cid").equals(communityClassEntity.getId())){
                DemandVo demandVo = new DemandVo();
                BeanUtil.copyProperties(a,demandVo);
                demandVoList.add(demandVo);
            }

            });

            pv1.setRecords(demandVoList);
            return super.setSuccessModelMap(pv1);

        }*/


    }

    @GetMapping("/read/detail1/{id}")
    @ResponseBody
    public Object selectOne1(ModelMap modelMap, @PathVariable("id") Long id) {
        DemandEntity demandEntity = super.service.queryById(id);
        DemandVo1 demandVo1 = new DemandVo1();
        demandVo1.setContent1(URLDecoder.decode(demandEntity.getContent(),"UTF-8"));
        demandVo1.setDemandClass1(demandEntity.getDemandClass());
        demandVo1.setName1(demandEntity.getName());
        demandVo1.setPhone1(demandEntity.getPhone());
        return super.setSuccessModelMap(demandVo1);
    }




}