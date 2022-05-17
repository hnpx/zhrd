package cn.px.sys.modular.brigadeClass.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.px.base.auth.context.LoginContextHolder;
import cn.px.base.core.BaseController;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.base.support.Assert;
import cn.px.base.support.Pagination;
import cn.px.base.util.ExportExcelUtil;
import cn.px.sys.modular.brigadeClass.entity.BrigadeClassEntity;
import cn.px.sys.modular.brigadeClass.service.BrigadeClassService;
import cn.px.sys.modular.communityClass.entity.CommunityClassEntity;
import cn.px.sys.modular.communityClass.service.CommunityClassService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (BrigadeClass)表控制层
 *
 * @author
 * @since 2020-10-12 09:38:48
 */
@RestController
@RequestMapping("brigadeClass")
@Api(value = "(BrigadeClass)管理")
public class BrigadeClassController extends BaseController<BrigadeClassEntity, BrigadeClassService> {

    private static final String PREFIX = "/modular/brigadeClass";
    @Autowired
    private HttpServletRequest request;

    @Autowired
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
    public Object query(ModelMap modelMap, BrigadeClassEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        Map<String, Object> paramsMap = params == null ? new HashMap<String, Object>() : BeanUtil.beanToMap(params);
        return super.query(modelMap, params, page);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @ResponseBody
    public Object update(ModelMap modelMap, BrigadeClassEntity params) {
        //TODO 数据验证
        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, BrigadeClassEntity params) {
        Assert.notNull(params.getId(), "ID");
        return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("导出excel")
    @PutMapping("/excel")
    public Object exportExcel(ModelMap modelMap, BrigadeClassEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        Pagination<BrigadeClassEntity> p = super.service.query(params, page);
        List<BrigadeClassEntity> data = p.getRecords();
        //TODO
        String fileName = ExportExcelUtil.doExportFile(new ExportParams(".xls", "BrigadeClass"), BrigadeClassEntity.class, data);
        return super.setSuccessModelMap(modelMap, fileName);
    }



    @ApiOperation("查询")
    @PutMapping("/read/blist")
    @ResponseBody
    public Object blist(ModelMap modelMap, BrigadeClassEntity params) {
        Long userId = LoginContextHolder.getContext().getUserId();
        CommunityClassEntity communityClassEntity = new CommunityClassEntity();
        communityClassEntity.setUserId(userId);
        CommunityClassEntity entity = communityClassService.selectOne(communityClassEntity);
        if (entity != null){
            Long comid = entity.getId();
            if (comid.longValue() != 0 || comid !=null){
                params.setCommunityClass(String.valueOf(comid));
            }
        }
        Page<Long> page = LayuiPageFactory.defaultPage();
        Map<String, Object> paramsMap = params == null ? new HashMap<String, Object>() : BeanUtil.beanToMap(params);
        Page<Map<String,Object>> list = service.selectPage(page,params);
        list.getRecords().forEach(a->{
            String bid = String.valueOf(a.get("brigade_class"));
            List ids = Arrays.asList(bid.split(","));
            a.put("brigade_class",service.getNameByList(ids));
            String cid = String.valueOf(a.get("community_class"));
            CommunityClassEntity communityClassEntity1 = communityClassService.queryById(Long.valueOf(cid));
            a.put("communityClass",communityClassEntity1.getName());
        });

//        Integer count = service.selectCount(params);
//        Map map = new HashMap();
//        map.put("list",list);
//        map.put("count",count);
        return setSuccessModelMap(list);
    }

}