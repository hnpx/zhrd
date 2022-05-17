package cn.px.sys.modular.volunteersClass.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.px.base.auth.context.LoginContextHolder;
import cn.px.base.core.BaseController;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.base.support.Assert;
import cn.px.base.support.Pagination;
import cn.px.base.util.ExportExcelUtil;
import cn.px.sys.modular.volunteersClass.entity.VolunteersClassEntity;
import cn.px.sys.modular.volunteersClass.service.VolunteersClassService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (VolunteersClass)表控制层
 *
 * @author
 * @since 2020-10-12 09:37:51
 */
@RestController
@RequestMapping("volunteersClass")
@Api(value = "(VolunteersClass)管理")
public class VolunteersClassController extends BaseController<VolunteersClassEntity, VolunteersClassService> {

    private static final String PREFIX = "/modular/volunteersClass";
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
    public Object query(ModelMap modelMap, VolunteersClassEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        Map<String, Object> paramsMap = params == null ? new HashMap<String, Object>() : BeanUtil.beanToMap(params);
        return super.query(modelMap, params, page);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @ResponseBody
    public Object update(ModelMap modelMap, VolunteersClassEntity params) {
        //TODO 数据验证
        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, VolunteersClassEntity params) {
        Assert.notNull(params.getId(), "ID");
        return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("导出excel")
    @PutMapping("/excel")
    public Object exportExcel(ModelMap modelMap, VolunteersClassEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        Pagination<VolunteersClassEntity> p = super.service.query(params, page);
        List<VolunteersClassEntity> data = p.getRecords();
        //TODO
        String fileName = ExportExcelUtil.doExportFile(new ExportParams(".xls", "VolunteersClass"), VolunteersClassEntity.class, data);
        return super.setSuccessModelMap(modelMap, fileName);
    }


    @ApiOperation("查询分类")
    @PutMapping("/read/list/fl")
    @ResponseBody
    public Object query1(ModelMap modelMap, VolunteersClassEntity params) {
        Page<Long> page = new Page(1,Integer.MAX_VALUE);
        Map<String, Object> paramsMap = params == null ? new HashMap<String, Object>() : BeanUtil.beanToMap(params);
        return super.query(modelMap, params, page);
    }

}