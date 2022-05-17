package cn.px.sys.modular.peopleDynamic.controller;

import cn.px.sys.modular.peopleDynamic.entity.PeopleDynamicTypeEntity;
import cn.px.sys.modular.peopleDynamic.service.PeopleDynamicTypeService;
import cn.px.base.pojo.page.LayuiPageFactory;
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
 * 文章类型(PeopleDynamicType)表控制层
 *
 * @author
 * @since 2021-05-13 16:08:06
 */
@RestController
@RequestMapping("peopleDynamicType")
@Api(value = "文章类型(PeopleDynamicType)管理")
public class PeopleDynamicTypeController extends BaseController<PeopleDynamicTypeEntity, PeopleDynamicTypeService> {

    private static final String PREFIX = "/modular/peopleDynamicType";
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
    public Object query(ModelMap modelMap, PeopleDynamicTypeEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        Map<String, Object> paramsMap = params == null ? new HashMap<String, Object>() : BeanUtil.beanToMap(params);
        return super.query(modelMap, params, page);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @ResponseBody
    public Object update(ModelMap modelMap, PeopleDynamicTypeEntity params) {
        //TODO 数据验证
        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, PeopleDynamicTypeEntity params) {
        Assert.notNull(params.getId(), "ID");
        return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("导出excel")
    @PutMapping("/excel")
    public Object exportExcel(ModelMap modelMap, PeopleDynamicTypeEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        Pagination<PeopleDynamicTypeEntity> p = super.service.query(params, page);
        List<PeopleDynamicTypeEntity> data = p.getRecords();
        //TODO
        String fileName = ExportExcelUtil.doExportFile(new ExportParams("文章类型.xls", "PeopleDynamicType"), PeopleDynamicTypeEntity.class, data);
        return super.setSuccessModelMap(modelMap, fileName);
    }

    @ApiOperation("查询")
    @PutMapping("/select/list")
    @ResponseBody
    public Object query() {
       Map<String,Object> map = new HashMap<>();
       map.put("enable",1);
        return super.setSuccessModelMap( super.service.queryList(map));
    }

}

