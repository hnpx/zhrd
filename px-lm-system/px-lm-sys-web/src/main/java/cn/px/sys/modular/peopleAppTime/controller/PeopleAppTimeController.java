package cn.px.sys.modular.peopleAppTime.controller;

import cn.px.sys.modular.peopleAppTime.entity.PeopleAppTimeEntity;
import cn.px.sys.modular.peopleAppTime.service.PeopleAppTimeService;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.sys.modular.peopleAppTime.vo.AppTimeListVo;
import cn.px.sys.modular.peopleCongress.entity.PeopleCongressEntity;
import cn.px.sys.modular.peopleCongress.service.PeopleCongressService;
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
 * 人大预约时间(PeopleAppTime)表控制层
 *
 * @author
 * @since 2021-05-13 16:04:38
 */
@RestController
@RequestMapping("peopleAppTime")
@Api(value = "人大预约时间(PeopleAppTime)管理")
public class PeopleAppTimeController extends BaseController<PeopleAppTimeEntity, PeopleAppTimeService> {

    private static final String PREFIX = "/modular/peopleAppTime";
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PeopleCongressService peopleCongressService;


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
    @GetMapping("/read/list")
    @ResponseBody
    public Object query(@RequestParam(required = false) Date date) {

        List<PeopleAppTimeEntity> peopleAppTimeEntityList = super.service.getMonth(date);
        peopleAppTimeEntityList.forEach(peopleAppTimeEntity -> {
            if (peopleAppTimeEntity.getPeopleCongress() != null) {
                PeopleCongressEntity peopleCongressEntity = peopleCongressService.queryById(peopleAppTimeEntity.getPeopleCongress());
                if (peopleCongressEntity != null) {
                    peopleAppTimeEntity.setUserName(peopleCongressEntity.getName());
                }
            }
        });
        return super.setSuccessModelMap(peopleAppTimeEntityList);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @ResponseBody
    public Object update(ModelMap modelMap, PeopleAppTimeEntity params) {
        //TODO 数据验证
        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, PeopleAppTimeEntity params) {
        Assert.notNull(params.getId(), "ID");
        return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("导出excel")
    @PutMapping("/excel")
    public Object exportExcel(ModelMap modelMap, PeopleAppTimeEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        Pagination<PeopleAppTimeEntity> p = super.service.query(params, page);
        List<PeopleAppTimeEntity> data = p.getRecords();
        //TODO
        String fileName = ExportExcelUtil.doExportFile(new ExportParams("人大预约时间.xls", "PeopleAppTime"), PeopleAppTimeEntity.class, data);
        return super.setSuccessModelMap(modelMap, fileName);
    }

    /**
     * 代表设置预约时间    线上
     *
     * @param applyTime
     * @param congress
     * @param type      预约时间类型（1.线上2.线下）
     * @return
     */
    @ApiOperation("代表设置预约时间")
    @GetMapping("/offline/applyTime")
    @ResponseBody
    public Object getApplyTime(@RequestParam("applyTime") Date applyTime, @RequestParam("congress") Long congress,
                               @RequestParam("type") Integer type) {

        PeopleAppTimeEntity peopleAppTimeEntity1 = super.service.getObjectByappTime(applyTime);
        if (peopleAppTimeEntity1 != null) {
            if(congress == null){
                super.service.delete(peopleAppTimeEntity1.getId());
                return super.setSuccessModelMap();
            }else {

                peopleAppTimeEntity1.setPeopleCongress(congress);
                peopleAppTimeEntity1.setType(type);
                return super.setSuccessModelMap(super.service.update(peopleAppTimeEntity1));
            }

        } else {
            PeopleAppTimeEntity peopleAppTimeEntity = new PeopleAppTimeEntity();
            peopleAppTimeEntity.setAppointmentTime(applyTime);
            peopleAppTimeEntity.setPeopleCongress(congress);
            peopleAppTimeEntity.setCreateTime(new Date());
            peopleAppTimeEntity.setType(type);
            return super.service.update(peopleAppTimeEntity);
        }

    }


    /**
     * 代表设置预约时间    线下
     *
     * @param applyTime
     * @param congress
     * @param type      预约时间类型（.线下）
     * @return
     */
    @ApiOperation("代表设置预约时间")
    @GetMapping("/offline/applyTime1")
    @ResponseBody
    public Object getApplyTime1(@RequestParam("applyTime") Date applyTime, @RequestParam("congress") Long congress,
                               @RequestParam("type") Integer type) {
            PeopleAppTimeEntity peopleAppTimeEntity = new PeopleAppTimeEntity();
            peopleAppTimeEntity.setAppointmentTime(applyTime);
            peopleAppTimeEntity.setPeopleCongress(congress);
            peopleAppTimeEntity.setCreateTime(new Date());
            peopleAppTimeEntity.setType(type);
            return super.service.update(peopleAppTimeEntity);
    }


    /**
     * 代表设置预约时间    线上
     *
     * @param
     * @return
     */
    @ApiOperation("代表设置预约时间")
    @GetMapping("/offline/onlineapplyTime")
    @ResponseBody
    public Object getApplyTimeList(List<AppTimeListVo> appTimeListVoList) {
        appTimeListVoList.forEach(appTimeListVo -> {
            PeopleAppTimeEntity peopleAppTimeEntity = new PeopleAppTimeEntity();
            peopleAppTimeEntity.setAppointmentTime(appTimeListVo.getAppointmentTime());
            peopleAppTimeEntity.setPeopleCongress(appTimeListVo.getPeopleCongress());
            peopleAppTimeEntity.setCreateTime(new Date());
            peopleAppTimeEntity.setType(1);
            service.update(peopleAppTimeEntity);
        });
        return super.setSuccessModelMap();

    }

    /**
     * 代表预约时间列表
     * @param
     * @return
     */
    @ApiOperation("代表预约时间列表")
    @GetMapping("/congress/applyTimeList")
    @ResponseBody
    public Object getApplyTimeList(@RequestParam("congress") Long congress){

        return super.setSuccessModelMap(super.service.getListByCongress(congress));
    }

}



