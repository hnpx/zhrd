package cn.px.sys.modular.peopleWebmaster.controller;

import cn.px.sys.modular.peopleWebmaster.entity.PeopleWebmasterEntity;
import cn.px.sys.modular.peopleWebmaster.service.PeopleWebmasterService;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.sys.modular.peopleWebmaster.vo.PeopleWebmasterVo;
import cn.px.sys.modular.peopleWebmaster.wrapper.PeopleWebmasterWrapper;
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
 * 联络站人员(PeopleWebmaster)表控制层
 *
 * @author
 * @since 2021-05-13 16:10:39
 */
@RestController
@RequestMapping("peopleWebmaster")
@Api(value = "联络站人员(PeopleWebmaster)管理")
public class PeopleWebmasterController extends BaseController<PeopleWebmasterEntity, PeopleWebmasterService> {

    private static final String PREFIX = "/modular/peopleWebmaster";
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PeopleWebmasterWrapper peopleWebmasterWrapper;


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
    public Object query(@RequestParam(required = false) String name,@RequestParam(required = false) Long organization) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        Page<Map<String,Object>> info =  super.service.selectPeopleWebmasterVo(page,name,organization);
        Page<PeopleWebmasterVo> peopleWebmasterVoPage = peopleWebmasterWrapper.getVoPage(info);
        return super.setSuccessModelMap(peopleWebmasterVoPage);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @ResponseBody
    public Object update(ModelMap modelMap, PeopleWebmasterEntity params) {
        //TODO 数据验证
        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, PeopleWebmasterEntity params) {
        Assert.notNull(params.getId(), "ID");
        return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("导出excel")
    @PutMapping("/excel")
    public Object exportExcel(ModelMap modelMap, PeopleWebmasterEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        Pagination<PeopleWebmasterEntity> p = super.service.query(params, page);
        List<PeopleWebmasterEntity> data = p.getRecords();
        //TODO
        String fileName = ExportExcelUtil.doExportFile(new ExportParams("联络站人员.xls", "PeopleWebmaster"), PeopleWebmasterEntity.class, data);
        return super.setSuccessModelMap(modelMap, fileName);
    }

}

