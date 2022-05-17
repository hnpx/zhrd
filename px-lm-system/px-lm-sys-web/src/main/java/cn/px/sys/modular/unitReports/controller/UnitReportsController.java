package cn.px.sys.modular.unitReports.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.px.base.auth.context.LoginContextHolder;
import cn.px.base.core.BaseController;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.base.support.Assert;
import cn.px.base.support.Pagination;
import cn.px.base.util.ExportExcelUtil;
import cn.px.sys.modular.communityClass.entity.CommunityClassEntity;
import cn.px.sys.modular.communityClass.service.CommunityClassService;
import cn.px.sys.modular.gridClass.entity.GridClassEntity;
import cn.px.sys.modular.gridClass.service.GridClassService;
import cn.px.sys.modular.unitReports.entity.UnitReportsEntity;
import cn.px.sys.modular.unitReports.service.UnitReportsService;
import cn.px.sys.modular.wx.service.AllUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jodd.util.URLDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 单位报到(UnitReports)表控制层
 *
 * @author
 * @since 2020-10-12 15:46:52
 */
@RestController
@RequestMapping("unitReports")
@Api(value = "单位报到(UnitReports)管理")
public class UnitReportsController extends BaseController<UnitReportsEntity, UnitReportsService> {

    private static final String PREFIX = "/modular/unitReports";
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AllUserService allUserService;
    @Autowired
    private CommunityClassService communityClassService;
    @Autowired
    private GridClassService gridClassService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/read/detail/{id}")
    @ResponseBody
    public Object selectOne(ModelMap modelMap, @PathVariable("id") Long id) {

       UnitReportsEntity unitReportsEntity = super.service.queryById(id);
        unitReportsEntity.setResources(URLDecoder.decode(unitReportsEntity.getResources(),"UTF-8"));
        return super.setSuccessModelMap(modelMap, unitReportsEntity);
    }

    @ApiOperation("查询")
    @PutMapping("/read/list")
    @ResponseBody
    public Object query(ModelMap modelMap, UnitReportsEntity params) {

        Long userId = LoginContextHolder.getContext().getUserId();
        CommunityClassEntity communityClassEntity = new CommunityClassEntity();
        communityClassEntity.setUserId(userId);
        CommunityClassEntity entity = communityClassService.selectOne(communityClassEntity);
        if (entity != null){
            Long comid = entity.getId();
            if (comid.longValue() != 0 || comid !=null){
                params.setCommunityClass(String.valueOf(comid));
            }
        }

        Page<Long> page = LayuiPageFactory.defaultPage();
        Page<Map<String,Object>> page1 = service.selectListPage(page,params);
        List<Map<String,Object>> map = page1.getRecords();
        map.forEach(a->{
            String resources = String.valueOf(a.get("resources"));
            a.put("resources", URLDecoder.decode(resources,"UTF-8"));
           if( a.containsKey("gridClass")){
               String  gridClass = a.get("gridClass").toString();
               if(gridClass == null || gridClass.length()==0){
                   a.put("gridClassName","");
               }else {
                GridClassEntity gridClassEntity = gridClassService.queryById(Long.parseLong(gridClass));
                   a.put("gridClassName",gridClassEntity.getName());
               }
           }else {
               a.put("gridClassName","");
           }
        });
        page1.setRecords(map);
        return setSuccessModelMap(page1);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @ResponseBody
    public Object update(ModelMap modelMap, UnitReportsEntity params) {
        //TODO 数据验证

        try {
            params.setResources(URLEncoder.encode(params.getResources(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, UnitReportsEntity params) {
        Assert.notNull(params.getId(), "ID");
        allUserService.delete(params.getUserId());
        return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("导出excel")
    @PutMapping("/excel")
    public Object exportExcel(ModelMap modelMap, UnitReportsEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        Pagination<UnitReportsEntity> p = super.service.query(params, page);
        List<UnitReportsEntity> data = p.getRecords();
        //TODO
        String fileName = ExportExcelUtil.doExportFile(new ExportParams("单位报到.xls", "UnitReports"), UnitReportsEntity.class, data);
        return super.setSuccessModelMap(modelMap, fileName);
    }



}