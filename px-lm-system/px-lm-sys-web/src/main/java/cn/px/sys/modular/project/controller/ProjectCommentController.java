package cn.px.sys.modular.project.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.px.base.auth.context.LoginContextHolder;
import cn.px.base.core.BaseController;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.base.support.Assert;
import cn.px.base.support.Pagination;
import cn.px.base.util.ExportExcelUtil;
import cn.px.sys.modular.activity.vo.CommentVo;
import cn.px.sys.modular.activity.wrapper.CommentWrapper;
import cn.px.sys.modular.activity.wrapper.UserWrapper;
import cn.px.sys.modular.cadresReports.entity.CadresReportsEntity;
import cn.px.sys.modular.cadresReports.service.CadresReportsService;
import cn.px.sys.modular.project.entity.ProjectCommentEntity;
import cn.px.sys.modular.project.service.ProjectCommentService;
import cn.px.sys.modular.unitReports.entity.UnitReportsEntity;
import cn.px.sys.modular.unitReports.service.UnitReportsService;
import cn.px.sys.modular.unitSecondClass.entity.UnitSecondClassEntity;
import cn.px.sys.modular.unitSecondClass.service.UnitSecondClassService;
import cn.px.sys.modular.volunteersReports.entity.VolunteersReportsEntity;
import cn.px.sys.modular.volunteersReports.service.VolunteersReportsService;
import cn.px.sys.modular.wx.entity.AllUserEntity;
import cn.px.sys.modular.wx.service.AllUserService;
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
 * 项目评价(ProjectComment)表控制层
 *
 * @author
 * @since 2020-09-04 09:05:53
 */
@RestController
@RequestMapping("projectComment")
@Api(value = "项目评价(ProjectComment)管理")
public class ProjectCommentController extends BaseController<ProjectCommentEntity, ProjectCommentService> {

    private static final String PREFIX = "/modular/projectComment";
    @Autowired
    private HttpServletRequest request;
    @Resource
    private CommentWrapper commentWrapper;
    @Resource
    private UserWrapper userWrapper;


    @Autowired
    private VolunteersReportsService volunteersReportsService;


    @Autowired
    private CadresReportsService cadresReportsService;

    @Autowired
    private UnitReportsService unitReportsService;



    @Autowired
    private UnitSecondClassService unitSecondClassService;

    @Autowired
    private AllUserService allUserService;


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
    public Object query(ModelMap modelMap, ProjectCommentEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        Map<String, Object> paramsMap = params == null ? new HashMap<String, Object>() : BeanUtil.beanToMap(params);
        return super.query(modelMap, params, page);
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    @ResponseBody
    public Object update(ModelMap modelMap, ProjectCommentEntity params) {
        //TODO 数据验证
        params.setUserId(LoginContextHolder.getContext().getUserId());
        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, ProjectCommentEntity params) {
        Assert.notNull(params.getId(), "ID");
        return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("导出excel")
    @PutMapping("/excel")
    public Object exportExcel(ModelMap modelMap, ProjectCommentEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        page.setCurrent(1);
        page.setSize(Integer.MAX_VALUE);
        Pagination<ProjectCommentEntity> p = super.service.query(params, page);
        List<ProjectCommentEntity> data = p.getRecords();
        //TODO
        String fileName = ExportExcelUtil.doExportFile(new ExportParams("项目评价.xls", "ProjectComment"), ProjectCommentEntity.class, data);
        return super.setSuccessModelMap(modelMap, fileName);
    }

    @ApiOperation("查询评价列表")
    @PutMapping("/read/listHome")
    @ResponseBody
    public Object query(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer pageSize,
                        @RequestParam(required = false) Long projectId,
                        @RequestParam(required = false) String nickname) {
    Page<Map<String,Object>> projectComment =  super.service.getPageList(page,pageSize,projectId,nickname);
        projectComment.getRecords().forEach(a->{
            AllUserEntity allUserEntity = allUserService.queryById((Long) a.get("userId"));

            if (allUserEntity == null){
                return;
            }
            Integer userType = allUserEntity.getType();;


            if (userType ==null){
                return;
            }

            if (userType == 1){
                //查询志愿者表
                VolunteersReportsEntity vEntity = new VolunteersReportsEntity();
                vEntity.setUserId((Long) a.get("userId"));
                VolunteersReportsEntity volunteersReportsEntity  = volunteersReportsService.selectOne(vEntity);
                if (volunteersReportsEntity==null){
                    return;
                }
                a.put("realName",volunteersReportsEntity.getUserName());
                a.put("phone",volunteersReportsEntity.getUserPhone());
            }

            if (userType == 2){
                CadresReportsEntity cEntity = new CadresReportsEntity();
                cEntity.setUserId((Long) a.get("userId"));
                CadresReportsEntity cadresReportsEntity = cadresReportsService.selectOne(cEntity);
                if (cadresReportsEntity==null){
                    return;
                }
                a.put("realName",cadresReportsEntity.getUserName());
                a.put("phone",cadresReportsEntity.getUserPhone());
            }
            if (userType==3){
                UnitReportsEntity uEntity = new UnitReportsEntity();
                uEntity.setUserId((Long) a.get("userId"));
                UnitReportsEntity unitReportsEntity = unitReportsService.selectOne(uEntity);
                if (unitReportsEntity==null){
                    return;
                }
                UnitSecondClassEntity unitSecondClassEntity = unitSecondClassService.queryById(unitReportsEntity.getSecondId());
                if (unitSecondClassEntity == null){
                    return;
                }
                a.put("realName",unitSecondClassEntity.getName());
                a.put("phone",unitReportsEntity.getUserPhone());
            }
        });

       Page<CommentVo> pv = commentWrapper.getVoPage(projectComment);
       return super.setSuccessModelMap(pv);
    }

}