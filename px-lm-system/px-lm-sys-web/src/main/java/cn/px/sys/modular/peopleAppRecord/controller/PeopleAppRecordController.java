package cn.px.sys.modular.peopleAppRecord.controller;

import cn.px.sys.modular.peopleAppRecord.entity.PeopleAppRecordEntity;
import cn.px.sys.modular.peopleAppRecord.service.PeopleAppRecordService;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.sys.modular.peopleAppRecord.vo.ConfirmVo;
import cn.px.sys.modular.peopleAppRecord.vo.PeopleAppRecordVo;
import cn.px.sys.modular.peopleAppRecord.wrapper.PeopleAppRecordWrapper;
import cn.px.sys.modular.peopleInformationSet.vo.VideoUrlVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * 群众预约记录(PeopleAppRecord)表控制层
 *
 * @author
 * @since 2021-05-13 15:56:11
 */
@RestController
@RequestMapping("peopleAppRecord")
@Api(value = "群众预约记录(PeopleAppRecord)管理")
public class PeopleAppRecordController extends BaseController<PeopleAppRecordEntity, PeopleAppRecordService> {

    private static final String PREFIX = "/modular/peopleAppRecord";
    @Autowired
    private HttpServletRequest request;
   @Autowired
   private PeopleAppRecordWrapper peopleAppRecordWrapper;
   @Autowired
   private PeopleAppRecordService peopleAppRecordService;

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
    public Object query(ModelMap modelMap, PeopleAppRecordEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        Map<String, Object> paramsMap = params == null ? new HashMap<String, Object>() : BeanUtil.beanToMap(params);
        return super.query(modelMap, params, page);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @ResponseBody
    public Object update(ModelMap modelMap, PeopleAppRecordEntity params) {
        //TODO 数据验证

        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, PeopleAppRecordEntity params) {
         PeopleAppRecordEntity peopleAppRecordEntity = super.service.queryById(params.getId());
         peopleAppRecordEntity.setIsDelete(1);
        return super.update(modelMap, peopleAppRecordEntity, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("导出excel")
    @PutMapping("/excel")
    public Object exportExcel(ModelMap modelMap, PeopleAppRecordEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        Pagination<PeopleAppRecordEntity> p = super.service.query(params, page);
        List<PeopleAppRecordEntity> data = p.getRecords();
        //TODO
        String fileName = ExportExcelUtil.doExportFile(new ExportParams("群众预约记录.xls", "PeopleAppRecord"), PeopleAppRecordEntity.class, data);
        return super.setSuccessModelMap(modelMap, fileName);
    }

    @ApiOperation("查询某个代表接待记录和预约的申请")
    @PutMapping("/read/congress/list")
    @ResponseBody
    public Object query1(@RequestParam("congressId") Long congressId,@RequestParam("status") Integer status) {
        Page page = LayuiPageFactory.defaultPage();
        Page<Map<String, Object>> pageInfo = super.service.getStatusList(page,congressId,status);
        Page<PeopleAppRecordVo> peopleAppRecordVoPage = peopleAppRecordWrapper.getVoPage(pageInfo);
        return super.setSuccessModelMap(peopleAppRecordVoPage);
    }

    /**
     * 代表预约 (拒接，确认连线)
     */
    @PostMapping("/applyCongress/confirm")
    @ResponseBody
    public Object getconfirm( ConfirmVo confirmVo){
        PeopleAppRecordEntity peopleAppRecordEntity =  peopleAppRecordService.queryById(confirmVo.getId());
        peopleAppRecordEntity.setAppTime(confirmVo.getAppTime());
        if(confirmVo.getStatus()==1){ // 同意
            peopleAppRecordEntity.setStatus(2);
        }else if((confirmVo.getStatus()==2)) { //拒绝
            peopleAppRecordEntity.setStatus(3);
            peopleAppRecordEntity.setInstructions(confirmVo.getInstructions());
        }
        return super.setSuccessModelMap(peopleAppRecordService.update(peopleAppRecordEntity));
    }

    /**
     * 查看视频记录
     */
    @GetMapping("/video/list")
    @ResponseBody
    public Object getVideoList(@RequestParam("id") Long id){
      PeopleAppRecordEntity peopleAppRecordEntity =  peopleAppRecordService.queryById(id);
        List<VideoUrlVo> videoUrlVoList = new ArrayList<>();
       if(peopleAppRecordEntity.getIsDelete() != 1){
           if(peopleAppRecordEntity != null){
               if(StringUtils.isNotEmpty(peopleAppRecordEntity.getUrl())){
                   JSONArray jsonArray = new JSONArray(peopleAppRecordEntity.getUrl());
                   for (int i=0; i < jsonArray.length(); i++)    {
                       try{
                           VideoUrlVo videoUrlVo = new VideoUrlVo();
                           videoUrlVo.setId(id);
                           JSONObject jsonObject = jsonArray.getJSONObject(i);
                           String url = jsonObject.getString("url");
                           videoUrlVo.setUrl(url);
                           Integer duration = jsonObject.getInt("duration");
                           videoUrlVo.setDuration(duration);
                           String startTime = jsonObject.getString("startTime");
                           videoUrlVo.setStartTime(startTime);
                           String endTime = jsonObject.getString("endTime");
                           videoUrlVo.setEndTime(endTime);
                           String fileId = jsonObject.getString("fileId");
                           videoUrlVo.setFileId(fileId);
                           videoUrlVoList.add(videoUrlVo);
                       }catch (Exception e){
                           e.printStackTrace();
                       }
                   }
               }
           }
       }

        return super.setSuccessModelMap(videoUrlVoList);
    }

}

