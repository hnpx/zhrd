package cn.px.sys.modular.peopleCongress.controller;

import cn.px.sys.modular.peopleCongress.entity.PeopleCongressEntity;
import cn.px.sys.modular.peopleCongress.service.PeopleCongressService;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.sys.modular.peopleCongress.vo.PeopleCongressVo;
import cn.px.sys.modular.peopleCongress.wapper.PeopleCongressWrapper;
import cn.px.sys.modular.peopleCongress.wapper.PeopleCongressWrapper1;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
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
 * 人大代表(PeopleCongress)表控制层
 *
 * @author
 * @since 2021-05-13 16:05:33
 */
@RestController
@RequestMapping("peopleCongress")
@Api(value = "人大代表(PeopleCongress)管理")
public class PeopleCongressController extends BaseController<PeopleCongressEntity, PeopleCongressService> {

    private static final String PREFIX = "/modular/peopleCongress";
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PeopleCongressWrapper peopleCongressWrapper;
    @Autowired
    private PeopleCongressWrapper1 peopleCongressWrapper1;


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
        Page<Map<String,Object>> pageInfo = super.service.selectPeopleCongressPageList(page,name,organization);
        Page<PeopleCongressVo> peopleCongressEntityPage = peopleCongressWrapper.getVoPage(pageInfo);
        return super.setSuccessModelMap(peopleCongressEntityPage);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @ResponseBody
    public Object update(ModelMap modelMap, PeopleCongressEntity params) {
        //TODO 数据验证
       PeopleCongressEntity peopleCongressEntity = super.service.queryById(params.getId());
       if(!peopleCongressEntity.getPhone().equals(params.getPhone())){
           PeopleCongressEntity peopleCongressEntity1 = new PeopleCongressEntity();
           peopleCongressEntity1.setPhone(params.getPhone());
           peopleCongressEntity1.setEnable(1);
           PeopleCongressEntity peopleCongressEntity2 =  super.service.selectOne(peopleCongressEntity1);
           if(peopleCongressEntity2 != null){
               return super.setModelMap("400","此手机号已经被添加不能重新被添加");
           }
       }
        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("添加")
    @PostMapping("/update1")
    @ResponseBody
    public Object update1(ModelMap modelMap, PeopleCongressEntity params) {
        //TODO 数据验证
        if(params.getIsOpenOnline()== null){
            params.setIsOpenOnline(1); //是否开启连线（1.开启2.不开启）
        }
        if(StringUtils.isNotEmpty(params.getPhone())){
            PeopleCongressEntity peopleCongressEntity = new PeopleCongressEntity();
            peopleCongressEntity.setPhone(params.getPhone());
            peopleCongressEntity.setEnable(1);
            PeopleCongressEntity peopleCongressEntity1 =  super.service.selectOne(peopleCongressEntity);
            if(peopleCongressEntity1 != null){
                return super.setModelMap("400","此手机号已经被添加不能重新被添加");
            }
        }else {
            return super.setModelMap("400","手机号不能为空");
        }

        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, PeopleCongressEntity params) {
        Assert.notNull(params.getId(), "ID");
        return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("导出excel")
    @PutMapping("/excel")
    public Object exportExcel(ModelMap modelMap, PeopleCongressEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        Pagination<PeopleCongressEntity> p = super.service.query(params, page);
        List<PeopleCongressEntity> data = p.getRecords();
        //TODO
        String fileName = ExportExcelUtil.doExportFile(new ExportParams("人大代表.xls", "PeopleCongress"), PeopleCongressEntity.class, data);
        return super.setSuccessModelMap(modelMap, fileName);
    }

    @ApiOperation("查询")
    @PutMapping("/select/list")
    @ResponseBody
    public Object query() {
       Map<String, Object> map = new HashMap<>();
       map.put("enanle",1);
        return super.setSuccessModelMap(super.service.queryList(map));
    }


    @ApiOperation("查询预约连线")
    @PutMapping("/apply/online/list")
    @ResponseBody
    public Object applyOnline(@RequestParam(required = false) String name) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        Page<Map<String,Object>> pageInfo = super.service.selectPeopleCongressPageList(page,name,null);
        Page<PeopleCongressVo> peopleCongressEntityPage = peopleCongressWrapper1.getVoPage(pageInfo);
        return super.setSuccessModelMap(peopleCongressEntityPage);
    }

    @ApiOperation("修改是否可以连线")
    @PostMapping("/edit/isOpenOnline")
    @ResponseBody
    public Object getisOpenOnline(@RequestParam("id") Long id){
         //  是否开启连线（1.开启2.不开启）
         PeopleCongressEntity peopleCongressEntity = super.service.queryById(id);
         if(peopleCongressEntity.getIsOpenOnline()==1){
             peopleCongressEntity.setIsOpenOnline(2);
         }else if(peopleCongressEntity.getIsOpenOnline()==2) {
             peopleCongressEntity.setIsOpenOnline(1);
         }
        return super.setSuccessModelMap(super.service.update(peopleCongressEntity));
    }


}

