package cn.px.sys.modular.peopleOpinion.controller;

import cn.px.sys.modular.peopleOpinion.entity.PeopleOpinionEntity;
import cn.px.sys.modular.peopleOpinion.service.PeopleOpinionService;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.sys.modular.peopleWebmaster.entity.PeopleWebmasterEntity;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * 征集意见，优化营商(PeopleOpinion)表控制层
 *
 * @author
 * @since 2021-05-13 16:09:23
 */
@RestController
@RequestMapping("peopleOpinion")
@Api(value = "征集意见，优化营商(PeopleOpinion)管理")
public class PeopleOpinionController extends BaseController<PeopleOpinionEntity, PeopleOpinionService> {

    private static final String PREFIX = "/modular/peopleOpinion";
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
        PeopleOpinionEntity peopleOpinionEntity = super.service.queryById(id);
       // peopleOpinionEntity.setName("群众"+peopleOpinionEntity.getName());
        if(peopleOpinionEntity!= null){

            /**
             * 类型（1.征集意见2.优化营商）
             */
            if(peopleOpinionEntity.getType()==1){
                peopleOpinionEntity.setTypeName("征集意见");
            }else if(peopleOpinionEntity.getType()==2){
                peopleOpinionEntity.setTypeName("优化营商");
            }
            /**
             * 解决状态（1.未解决2.已解决）
             */

            if(peopleOpinionEntity.getStatus()== 1){
                peopleOpinionEntity.setStatusName("未解决");
            }else if(peopleOpinionEntity.getStatus()== 2){
                peopleOpinionEntity.setStatusName("已解决");
            }
        }
        return super.setSuccessModelMap(modelMap, peopleOpinionEntity);
    }

    @ApiOperation("查询")
    @PutMapping("/read/list")
    @ResponseBody
    public Object query(@RequestParam(required = false) Integer type,@RequestParam(required = false) Integer status,
                        @RequestParam(required = false) Date startTime,@RequestParam(required = false) Date endTime) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        Page<Map<String, Object>> pageInfo = super.service.selectPeopleOpinionVo(page,type,status,startTime,endTime);
        return super.setSuccessModelMap(pageInfo);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @ResponseBody
    public Object update(ModelMap modelMap, PeopleOpinionEntity params) {
        //TODO 数据验证
        if(params.getStatus() == 1){
            params.setStatus(2);
        }else {
            return super.setModelMap("400","已经标记为已解决");
        }
        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }


    @ApiOperation("更新解决意见")
    @PostMapping("/update1")
    @ResponseBody
    public Object update1(ModelMap modelMap, PeopleOpinionEntity params) {
        //TODO 数据验证
            return super.service.update(params);

    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, PeopleOpinionEntity params) {
        Assert.notNull(params.getId(), "ID");
        return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("导出excel")
    @PutMapping("/excel")
    public Object exportExcel(ModelMap modelMap, PeopleOpinionEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        Pagination<PeopleOpinionEntity> p = super.service.query(params, page);
        List<PeopleOpinionEntity> data = p.getRecords();
        //TODO
        String fileName = ExportExcelUtil.doExportFile(new ExportParams("征集意见，优化营商.xls", "PeopleOpinion"), PeopleOpinionEntity.class, data);
        return super.setSuccessModelMap(modelMap, fileName);
    }

}

