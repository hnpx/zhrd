package cn.px.sys.modular.serviceconf.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.px.base.auth.context.LoginContextHolder;
import cn.px.base.core.BaseController;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.base.support.Assert;
import cn.px.base.support.Pagination;
import cn.px.base.util.ExportExcelUtil;
import cn.px.sys.modular.serviceconf.entity.ServiceConf;
import cn.px.sys.modular.serviceconf.service.ServiceConfService;
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
 * 平台配置信息(PlatformConf)表控制层
 *
 * @author makejava
 * @since 2020-08-06 14:49:03
 */
@RestController
@Api(value = "服务配置信息(ServiceConf)管理", tags = "服务配置信息(ServiceConf)管理")
public class ServiceConfController extends BaseController<ServiceConf, ServiceConfService> {

    private static final String PREFIX = "/modular/platformConf";
    @Autowired
    private HttpServletRequest request;


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("ServiceConf/read/detail/{id}")
    @ResponseBody
    public Object selectOne(ModelMap modelMap, @PathVariable("id") Long id) {
        return super.setSuccessModelMap(modelMap, super.service.queryById(id));
    }

    @ApiOperation("查询平台配置")
    @PutMapping("/api/ServiceConf/read/list")
    @ResponseBody
    public Object query(ModelMap modelMap, ServiceConf params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        Map<String, Object> paramsMap = params == null ? new HashMap<String, Object>() : BeanUtil.beanToMap(params);
        return super.query(modelMap, params, page);
    }

    @ApiOperation("更新")
    @PostMapping("ServiceConf/update")
    @ResponseBody
    public Object update(ModelMap modelMap, ServiceConf params) {
        //TODO 数据验证
        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("删除")
    @PostMapping("ServiceConf/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, ServiceConf params) {
        Assert.notNull(params.getId(), "ID");
        return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("导出excel")
    @PutMapping("ServiceConf/excel")
    public Object exportExcel(ModelMap modelMap, ServiceConf params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        Pagination<ServiceConf> p = super.service.query(params, page);
        List<ServiceConf> data = p.getRecords();

        String fileName = ExportExcelUtil.doExportFile(new ExportParams("服务配置信息.xls", "ServiceConf"), ServiceConf.class, data);
        return super.setSuccessModelMap(modelMap, fileName);
    }

}