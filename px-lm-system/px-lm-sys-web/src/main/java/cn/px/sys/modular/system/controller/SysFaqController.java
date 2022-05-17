package cn.px.sys.modular.system.controller;

import cn.px.sys.modular.system.entity.SysFaqEntity;
import cn.px.sys.modular.system.service.SysFaqService;
import cn.px.base.pojo.page.LayuiPageFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import cn.px.base.auth.context.LoginContextHolder;
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
 * (SysFaq)表控制层
 *
 * @author makejava
 * @since 2020-06-09 14:43:58
 */
@Controller
@RequestMapping("sysFaq")
@ApiOperation(value = "(SysFaq)管理")
public class SysFaqController extends BaseController<SysFaqEntity,SysFaqService>{

	private static final String PREFIX = "/modular/sysFaq";
	@Autowired
	private HttpServletRequest request;
	/**
	 * 跳转到主页面
	 */
	@RequestMapping("/show")
	public String index() {
		return PREFIX + "/index.html";
	}

	/**
	 * 新增页面
	 */
	@RequestMapping("/show/add")
	public String add() {
		return PREFIX + "/add.html";
	}

	/**
	 * 编辑页面
	 */
	@RequestMapping("/show/edit")
	public String edit() {
		return PREFIX + "/edit.html";
	}


	/**
	 * 通过主键查询单条数据
	 *
	 * @param id 主键
	 * @return 单条数据
	 */
	@GetMapping("/read/detail/{id}")
	@ResponseBody
	public Object selectOne(ModelMap modelMap,  @PathVariable("id") Long id) {
		return super.setSuccessModelMap(modelMap, super.service.queryById(id));
	}

	@ApiOperation("查询")
	@PutMapping("/read/list")
	@ResponseBody
	public Object query(ModelMap modelMap, SysFaqEntity params) {
		Page<Long> page= LayuiPageFactory.defaultPage();
        Map<String, Object> paramsMap = params == null ? new HashMap<String, Object>() : BeanUtil.beanToMap(params);
		return super.query(modelMap, params, page);
	}

	@ApiOperation("更新")
	@PostMapping("/update")
	@ResponseBody
	public Object update(ModelMap modelMap, SysFaqEntity params) {
		//TODO 数据验证
		return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
	}

	@ApiOperation("删除")
	@DeleteMapping("/delete")
	@ResponseBody
	public Object del(ModelMap modelMap, SysFaqEntity params) {
		Assert.notNull(params.getId(), "ID");
		return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
	}

    @ApiOperation("导出excel")
    @PutMapping("/excel")
    public Object exportExcel(ModelMap modelMap, SysFaqEntity params){
        Page<Long> page= LayuiPageFactory.defaultPage();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        Pagination<SysFaqEntity> p= super.service.query(params,page);
        List<SysFaqEntity> data = p.getRecords();
        //TODO
        String fileName = ExportExcelUtil.doExportFile(new ExportParams(".xls","SysFaq"), SysFaqEntity.class, data);
        return super.setSuccessModelMap(modelMap,fileName);
     }

}