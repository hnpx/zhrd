package cn.px.sys.modular.doubleReport.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.px.base.Constants;
import cn.px.base.auth.context.LoginContextHolder;
import cn.px.base.core.BaseController;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.base.support.Assert;
import cn.px.base.support.Pagination;
import cn.px.base.util.ExportExcelUtil;
import cn.px.sys.modular.doubleReport.constant.IsBindEnum;
import cn.px.sys.modular.doubleReport.entity.ReportCadreEntity;
import cn.px.sys.modular.doubleReport.service.ReportCadreService;
import cn.px.sys.modular.doubleReport.vo.ReportCadreVo;
import cn.px.sys.modular.doubleReport.vo.UserVo;
import cn.px.sys.modular.doubleReport.wrapper.ReportCadreWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 党员干部信息管理(ReportCadre)表控制层
 *
 * @author
 * @since 2020-08-28 14:10:46
 */
@RestController
@RequestMapping("reportCadre")
@Api(value = "党员干部信息管理(ReportCadre)管理")
public class ReportCadreController extends BaseController<ReportCadreEntity, ReportCadreService> {

    private static final String PREFIX = "/modular/reportCadre";
    @Autowired
    private HttpServletRequest request;
    @Resource
    private ReportCadreWrapper reportCadreWrapper;


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
    public Object query(ModelMap modelMap, ReportCadreEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        Map<String, Object> paramsMap = params == null ? new HashMap<String, Object>() : BeanUtil.beanToMap(params);
        return super.query(modelMap, params, page);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @ResponseBody
    public Object update(ModelMap modelMap, ReportCadreEntity params) {
        //TODO 数据验证
        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, ReportCadreEntity params) {
        Assert.notNull(params.getId(), "ID");
        return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("导出excel")
    @RequestMapping("/excel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String cid = request.getParameter("cid");
        Workbook wb = super.service.createWorkbook(name, phone, cid);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        String fileName = "党员信息表";
        response.setCharacterEncoding("utf-8");
        try {
            response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1") + ".xls" );
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("导入excel")
    @PostMapping("/upload")
    public  @ResponseBody Object  uploadExcel(MultipartFile file) throws Exception {
        if(file.isEmpty()){
            return "文件不能为空";
        }
        List<ReportCadreEntity> entityList = super.service.getBankListByExcel(file);
        //插入数据
        for (ReportCadreEntity entity : entityList) {
            super.service.insert(entity);
        }
        return super.setSuccessModelMap();
    }


    @ApiOperation("修改党员干部是否绑定")
    @PostMapping("/update/changeIsBind")
    @ResponseBody
    public Object changeStatus(ModelMap modelMap,Long id) {
        ReportCadreEntity params = this.service.queryById(id);

        if(params.getIsBind().equals(IsBindEnum.IS_BIND_TRUE.getValue())){
            params.setIsBind(IsBindEnum.IS_BIND_FALSE.getValue());
        }else {
            params.setIsBind(IsBindEnum.IS_BIND_TRUE.getValue());
        }
        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("绑定个人信息")
    @PostMapping("/update/isBind")
    @ResponseBody
    public Object update(@RequestBody UserVo userVo) {
        //TODO 数据验证
      int i =  super.service.getIsBind(userVo,LoginContextHolder.getContext().getUserId());
        if(i == 1){
            return super.setModelMap("400","党员干部信息不存在");
        }else if(i == 2){
            return super.setModelMap("400","单位信息不存在");
        }
        return super.setSuccessModelMap();
    }



    @ApiOperation("查询列表")
    @PutMapping("/read/listHome")
    @ResponseBody
    public Object query(@RequestParam(required = false) String name,@RequestParam(required = false) String phone,@RequestParam(required = false) Long belongingCommunity,@RequestParam(required = false) Long belongingUnit) {
        Long userId = LoginContextHolder.getContext().getUserId();
        Page<Map<String,Object>> cadre =  super.service.getListHome(name,phone,userId,belongingCommunity,belongingUnit);
        Page<ReportCadreVo>  pv = reportCadreWrapper.getVoPage(cadre);
        return super.setSuccessModelMap(pv);

    }




}