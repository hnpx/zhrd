package cn.px.sys.modular.volunteersReports.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.px.base.auth.context.LoginContextHolder;
import cn.px.base.core.BaseController;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.base.support.Assert;
import cn.px.base.support.Pagination;
import cn.px.base.util.ExportExcelUtil;
import cn.px.sys.modular.brigadeClass.service.BrigadeClassService;
import cn.px.sys.modular.communityClass.entity.CommunityClassEntity;
import cn.px.sys.modular.communityClass.service.CommunityClassService;
import cn.px.sys.modular.volunteersReports.entity.VolunteersReportsEntity;
import cn.px.sys.modular.volunteersReports.service.VolunteersReportsService;
import cn.px.sys.modular.wx.service.AllUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jodd.util.URLDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 志愿者报到(VolunteersReports)表控制层
 *
 * @author
 * @since 2020-10-12 15:18:35
 */
@RestController
@RequestMapping("volunteersReports")
@Api(value = "志愿者报到(VolunteersReports)管理")
public class VolunteersReportsController extends BaseController<VolunteersReportsEntity, VolunteersReportsService> {

    private static final String PREFIX = "/modular/volunteersReports";
    @Autowired
    private HttpServletRequest request;


    @Autowired
    private BrigadeClassService  brigadeClassService;

    @Autowired
    private AllUserService allUserService;

    @Autowired
    private CommunityClassService communityClassService;


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @ApiOperation("详情")
    @GetMapping("/read/detail/{id}")
    @ResponseBody
    public Object selectOne(ModelMap modelMap, @PathVariable("id") Long id) {
        return super.setSuccessModelMap(modelMap, super.service.queryById(id));
    }

    @ApiOperation("查询")
    @PutMapping("/read/list")
    @ResponseBody
    public Object query(ModelMap modelMap, VolunteersReportsEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();


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

        Page<Map<String,Object>> page1 = service.selectListPage(page,params);
        List<Map<String,Object>> map = page1.getRecords();
        map.forEach(a->{
            String name =   URLDecoder.decode(a.get("user_name").toString(),"UTF-8");
            a.put("user_name",name);
            String bid = String.valueOf(a.get("brigade_class"));
            List id = Arrays.asList(bid.split(","));
            a.put("brigade_class",brigadeClassService.getNameByList(id));

        });


        page1.setRecords(map);
        return setSuccessModelMap(page1);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @ResponseBody
    public Object update(ModelMap modelMap, VolunteersReportsEntity params) {
        //TODO 数据验证
        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, VolunteersReportsEntity params) {
        Assert.notNull(params.getId(), "ID");
        allUserService.delete(params.getUserId());
        return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("导出excel")
    @PutMapping("/excel")
    public Object exportExcel(ModelMap modelMap, VolunteersReportsEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        Pagination<VolunteersReportsEntity> p = super.service.query(params, page);
        List<VolunteersReportsEntity> data = p.getRecords();
        //TODO
        String fileName = ExportExcelUtil.doExportFile(new ExportParams("志愿者报到.xls", "VolunteersReports"), VolunteersReportsEntity.class, data);
        return super.setSuccessModelMap(modelMap, fileName);
    }

}