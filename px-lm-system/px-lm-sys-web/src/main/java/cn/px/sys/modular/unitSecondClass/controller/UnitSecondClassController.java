package cn.px.sys.modular.unitSecondClass.controller;

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
import cn.px.sys.modular.unitSecondClass.entity.UnitSecondClassEntity;
import cn.px.sys.modular.unitSecondClass.service.UnitSecondClassService;
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
 * (UnitSecondClass)表控制层
 *
 * @author
 * @since 2020-10-12 11:56:32
 */
@RestController
@RequestMapping("unitSecondClass")
@Api(value = "(UnitSecondClass)管理")
public class UnitSecondClassController extends BaseController<UnitSecondClassEntity, UnitSecondClassService> {

    private static final String PREFIX = "/modular/unitSecondClass";
    @Autowired
    private HttpServletRequest request;
    @Resource
    private CommunityClassService communityClassService;


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
    public Object query(ModelMap modelMap, UnitSecondClassEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        return setSuccessModelMap(super.service.selectList(page,params.getName()));
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @ResponseBody
    public Object update(ModelMap modelMap, UnitSecondClassEntity params) {
        //TODO 数据验证
        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, UnitSecondClassEntity params) {
        Assert.notNull(params.getId(), "ID");
        return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
    }


    @ApiOperation("查询")
    @PutMapping("/read/listById")
    @ResponseBody
    public Object get(){

        return super.setSuccessModelMap(super.service.getList());
    }


    @ApiOperation("通过社区id查询单位")
    @PutMapping("/read/listByCid")
    public Object getUnit(@RequestParam(required = false) Long cid){
        if(cid != null){

            return super.setSuccessModelMap( super.service.getListByCid(cid));
        }else {
            return super.setSuccessModelMap();
        }

    }


    @ApiOperation("通过单位id查询社区")
    @PutMapping("/read/listByUnit")
    private Object getCid(@RequestParam("unit") Long unit){
        Map<String,Object> map = new HashMap<>();
       UnitSecondClassEntity unitSecondClassEntity = this.service.getUnitById(unit);
       if(unitSecondClassEntity != null){
           CommunityClassEntity communityClassEntity = communityClassService.queryById(unitSecondClassEntity.getCid());
           map.put("cid",communityClassEntity.getId());
           map.put("name",communityClassEntity.getName());
       }else {
           map.put("cid","");
           map.put("name","");
       }
       return super.setSuccessModelMap(map);

    }


    @ApiOperation("通过社区id查询单位")
    @PutMapping("/read/listByCid/project")
    public Object getUnitProject(){

      Long userId =  LoginContextHolder.getContext().getUserId();

      if(userId == 1){
          return super.setSuccessModelMap( super.service.getListByCid1(null));
      }else {
       CommunityClassEntity communityClassEntity =   communityClassService.getCommunityClassEntityByUserId(userId);

          return super.setSuccessModelMap( super.service.getListByCid1(communityClassEntity.getId()));
      }

    }

}