package cn.px.sys.modular.peopleOrganization.controller;

import cn.px.sys.modular.peopleOrganization.entity.PeopleOrganizationEntity;
import cn.px.sys.modular.peopleOrganization.service.PeopleOrganizationService;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.sys.modular.system.warpper.MenuWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * 组织架构(PeopleOrganization)表控制层
 *
 * @author
 * @since 2021-05-13 16:09:56
 */
@RestController
@RequestMapping("peopleOrganization")
@Api(value = "组织架构(PeopleOrganization)管理")
public class PeopleOrganizationController extends BaseController<PeopleOrganizationEntity, PeopleOrganizationService> {

    private static final String PREFIX = "/modular/peopleOrganization";
    @Autowired
    private HttpServletRequest request;


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
    public Object query(ModelMap modelMap, PeopleOrganizationEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        Map<String, Object> paramsMap = params == null ? new HashMap<String, Object>() : BeanUtil.beanToMap(params);
        return super.query(modelMap, params, page);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @ResponseBody
    public Object update(ModelMap modelMap, PeopleOrganizationEntity params) {
        //TODO 数据验证
        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, PeopleOrganizationEntity params) {
        Assert.notNull(params.getId(), "ID");
        return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("导出excel")
    @PutMapping("/excel")
    public Object exportExcel(ModelMap modelMap, PeopleOrganizationEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        Pagination<PeopleOrganizationEntity> p = super.service.query(params, page);
        List<PeopleOrganizationEntity> data = p.getRecords();
        //TODO
        String fileName = ExportExcelUtil.doExportFile(new ExportParams("组织架构.xls", "PeopleOrganization"), PeopleOrganizationEntity.class, data);
        return super.setSuccessModelMap(modelMap, fileName);
    }

    @ApiOperation("组织架构列表")
    @PutMapping("/read/listPage")
    @ResponseBody
    public Object queryPage() {
        Map<String,Object> map = new HashMap<>();
        map.put("pid",1);
        map.put("is_modify",1);
        return super.setSuccessModelMap(super.queryList(map));
    }

    @ApiOperation("联络站列表")
    @PutMapping("/read/listPage/junction")
    @ResponseBody
    public Object queryPageJunction() {
        Map<String,Object> map = new HashMap<>();
        map.put("pid",2);
        map.put("is_modify",2);
        return super.setSuccessModelMap(super.queryList(map));
    }

    @ApiOperation("组织架构")
    @PutMapping("/read/organization/list")
    @ResponseBody
    public Object getOrganization(){
        Page<Map<String, Object>> info= super.service.getLiatPage();
        Page<Map<String, Object>> wrap = new MenuWrapper(info).wrap();
        return super.setSuccessModelMap(wrap);
    }
    @ApiOperation("服务团队")
    @PutMapping("/read/organization/listpage")
    @ResponseBody
    public Object getListPage(){
        Map<String,Object> map = new HashMap<>();
        map.put("pid",0);
        map.put("is_modify",1);
        List<PeopleOrganizationEntity> peopleOrganizationEntityList =    super.service.queryList(map);
        peopleOrganizationEntityList.forEach(peopleOrganizationEntity -> {
            peopleOrganizationEntity.setTitle(peopleOrganizationEntity.getName());
            Map<String,Object> map1 = new HashMap<>();
            map1.put("pid",peopleOrganizationEntity.getId());
            map1.put("is_modify",1);
            List<PeopleOrganizationEntity> peopleOrganizationEntityList1 = super.service.queryList(map1);
            peopleOrganizationEntityList1.forEach(peopleOrganizationEntity1 -> {
                peopleOrganizationEntity1.setTitle(peopleOrganizationEntity1.getName());
            });
            peopleOrganizationEntity.setChildren(peopleOrganizationEntityList1);
        });

        return super.setSuccessModelMap(peopleOrganizationEntityList);
    }

}

