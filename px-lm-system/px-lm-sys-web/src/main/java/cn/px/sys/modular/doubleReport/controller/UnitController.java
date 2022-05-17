package cn.px.sys.modular.doubleReport.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.px.base.auth.context.LoginContextHolder;
import cn.px.base.core.BaseController;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.base.support.Assert;
import cn.px.base.support.Pagination;
import cn.px.base.util.ExportExcelUtil;
import cn.px.sys.modular.communityClass.entity.CommunityClassEntity;
import cn.px.sys.modular.communityClass.service.CommunityClassService;
import cn.px.sys.modular.doubleReport.entity.UnitEntity;
import cn.px.sys.modular.doubleReport.service.UnitService;
import cn.px.sys.modular.doubleReport.vo.UnitListVo;
import cn.px.sys.modular.doubleReport.vo.UnitVo;
import cn.px.sys.modular.doubleReport.wrapper.UnitWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 单位管理(Unit)表控制层
 *
 * @author
 * @since 2020-08-28 16:16:30
 */
@RestController
@RequestMapping("unit")
@Api(value = "单位管理(Unit)管理")
public class UnitController extends BaseController<UnitEntity, UnitService> {

    private static final String PREFIX = "/modular/unit";
    @Autowired
    private HttpServletRequest request;
    @Resource
    private CommunityClassService communityClassService;

    @Resource
    private UnitWrapper unitWrapper;




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
    public Object query(ModelMap modelMap, UnitEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        Map<String, Object> paramsMap = params == null ? new HashMap<String, Object>() : BeanUtil.beanToMap(params);
        return super.query(modelMap, params, page);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @ResponseBody
    public Object update(ModelMap modelMap, UnitEntity params) {
        //TODO 数据验证
        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, UnitEntity params) {
        Assert.notNull(params.getId(), "ID");
        return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("导出excel")
    @PutMapping("/excel")
    public Object exportExcel(ModelMap modelMap, UnitEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        Pagination<UnitEntity> p = super.service.query(params, page);
        List<UnitEntity> data = p.getRecords();
        //TODO
        String fileName = ExportExcelUtil.doExportFile(new ExportParams("单位管理.xls", "Unit"), UnitEntity.class, data);
        return super.setSuccessModelMap(modelMap, fileName);
    }

    @ApiOperation("通过社区id查询单位信息")
    @PutMapping("/read/listByBelongingCommunity")
    @ResponseBody
    public Object query(@RequestParam(required = false) Long belongingCommunity) {
     return super.setSuccessModelMap(super.service.getUnitEntityByCommunity(belongingCommunity));
    }

    @ApiOperation("查询列表")
    @PutMapping("/read/homeList")
    @ResponseBody
    public Object query(@RequestParam(required = false) String name,@RequestParam(required = false) String contactPerson,@RequestParam(required = false) Long belongingCommunity) {
      Long cid = null;
      CommunityClassEntity communityClassEntity = communityClassService.getCommunityClassEntityByUserId(LoginContextHolder.getContext().getUserId());
      if(communityClassEntity != null){
          cid = communityClassEntity.getId();
      }
      Page<Map<String,Object>> unit =  super.service.getList(name,cid,contactPerson,belongingCommunity);
      Page<UnitListVo>  pv = unitWrapper.getVoPage(unit);
      return super.setSuccessModelMap(pv);
    }

    @ApiOperation("通过手机号获取单位所属社区")
    @PutMapping("/read/unit")
    @ResponseBody
    public Object query1(@RequestParam("phone") String phone) {
      UnitEntity unitEntity = super.service.getUnitEntityByPhone(phone);
      UnitVo unitVo = new UnitVo();
      BeanUtil.copyProperties(unitEntity,unitVo);
      CommunityClassEntity communityClassEntity =  communityClassService.queryById(unitEntity.getBelongingCommunity());
      unitVo.setBelongingCommunityName(communityClassEntity.getName());
      return super.setSuccessModelMap(unitVo);
    }

}