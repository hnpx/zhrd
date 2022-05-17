package cn.px.sys.modular.peopleInformationSet.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.px.base.auth.context.LoginContextHolder;
import cn.px.base.auth.service.AuthService;
import cn.px.base.core.BaseController;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.base.support.Assert;
import cn.px.base.support.Pagination;
import cn.px.base.util.ExportExcelUtil;
import cn.px.sys.core.wx.WxMaConfiguration;
import cn.px.sys.modular.peopleAppRecord.entity.PeopleAppRecordEntity;
import cn.px.sys.modular.peopleAppRecord.service.PeopleAppRecordService;
import cn.px.sys.modular.peopleAppRecord.vo.PeopleAppRecordVo;
import cn.px.sys.modular.peopleAppTime.entity.PeopleAppTimeEntity;
import cn.px.sys.modular.peopleAppTime.service.PeopleAppTimeService;
import cn.px.sys.modular.peopleAppTime.vo.AppTimeVo;
import cn.px.sys.modular.peopleCongress.controller.PeopleCongressController;
import cn.px.sys.modular.peopleCongress.entity.PeopleCongressEntity;
import cn.px.sys.modular.peopleCongress.service.PeopleCongressService;
import cn.px.sys.modular.peopleCongress.vo.PeopleCongressVo;
import cn.px.sys.modular.peopleCrowd.entity.PeopleCrowdEntity;
import cn.px.sys.modular.peopleCrowd.service.PeopleCrowdService;
import cn.px.sys.modular.peopleDynamic.entity.PeopleDynamicEntity;
import cn.px.sys.modular.peopleDynamic.service.PeopleDynamicService;
import cn.px.sys.modular.peopleDynamic.vo.PeopleDynamicVo;
import cn.px.sys.modular.peopleInformationSet.entity.PeopleInformationSetEntity;
import cn.px.sys.modular.peopleInformationSet.service.PeopleInformationSetService;
import cn.px.sys.modular.peopleInformationSet.vo.LoginUserVo;
import cn.px.sys.modular.peopleOpinion.entity.PeopleOpinionEntity;
import cn.px.sys.modular.peopleOpinion.service.PeopleOpinionService;
import cn.px.sys.modular.peopleOrganization.entity.PeopleOrganizationEntity;
import cn.px.sys.modular.peopleOrganization.service.PeopleOrganizationService;
import cn.px.sys.modular.system.warpper.MenuWrapper;
import cn.px.sys.modular.wx.entity.AllUserEntity;
import cn.px.sys.modular.wx.service.AllUserService;
import cn.px.sys.modular.wx.service.WxConfService;
import cn.px.sys.modular.wx.vo.WxConfVo;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import cn.stylefeng.roses.kernel.model.response.SuccessResponseData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.method.P;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 人大信息设置(PeopleInformationSet)表控制层
 *
 * @author
 * @since 2021-05-13 16:08:45
 */
@RestController
@RequestMapping("api/peopleInformationSet")
@Api(value = "人大信息设置(PeopleInformationSet)管理")
public class PeopleInformationSetApiController extends BaseController<PeopleInformationSetEntity, PeopleInformationSetService> {

    private static final String PREFIX = "/modular/peopleInformationSet";
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PeopleCongressService peopleCongressService;
    @Autowired
    private PeopleDynamicService peopleDynamicService;
    @Autowired
    private PeopleAppRecordService peopleAppRecordService;
    @Autowired
    private AllUserService allUserService;
    @Autowired
    private PeopleCrowdService peopleCrowdService;
    @Autowired
    private PeopleOrganizationService peopleOrganizationService;
    @Autowired
    private WxConfService wxConfService;
    @Autowired
    private AuthService authService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PeopleAppTimeService peopleAppTimeService;
    @Autowired
    private PeopleOpinionService peopleOpinionService;


    /**
     * 小程序首页
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/read/detail/{id}")
    @ResponseBody
    public Object selectOne(ModelMap modelMap, @PathVariable("id") Long id) {
        PeopleInformationSetEntity peopleInformationSetEntity = super.service.queryById(id);
        return super.setSuccessModelMap(peopleInformationSetEntity);
    }

    /**
     * 代表风采列表
     */
    @GetMapping("/peopleCongress/list")
    @ResponseBody
    public Object getPeopleCongressList(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        Page page = new Page(pageNo, pageSize);
        Page<PeopleCongressEntity> peopleCongressEntityPage = peopleCongressService.getPeopleCongressList(page);
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        peopleCongressEntityPage.getRecords().forEach(peopleCongressEntity -> {
            if (peopleCongressEntity.getYears() != null) {
                peopleCongressEntity.setAge(Integer.parseInt(year) - peopleCongressEntity.getYears());
            }
            PeopleOrganizationEntity peopleOrganizationEntity = peopleOrganizationService.queryById(peopleCongressEntity.getOrganization());
            if (peopleOrganizationEntity != null) {
                peopleCongressEntity.setOrganizationName(peopleOrganizationEntity.getName());
            }
        });
        return super.setSuccessModelMap(peopleCongressEntityPage);
    }

    /**
     * 代表风采列表详情
     */
    @GetMapping("/peopleCongress/byId")
    @ResponseBody
    public Object getPeopleCongressById(@RequestParam("id") Long id) {
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        PeopleCongressEntity peopleCongressEntityPage = peopleCongressService.queryById(id);
        PeopleCongressVo peopleCongressVo = new PeopleCongressVo();
        BeanUtil.copyProperties(peopleCongressEntityPage, peopleCongressVo, true);
        if (peopleCongressEntityPage.getYears() != null) {
            peopleCongressVo.setAge(Integer.parseInt(year) - peopleCongressEntityPage.getYears());
        }
        if (peopleCongressVo.getOrganization() != null) {
            PeopleOrganizationEntity peopleOrganizationEntity = peopleOrganizationService.queryById(peopleCongressVo.getOrganization());
            peopleCongressVo.setOrganizationName(peopleOrganizationEntity.getName());
        }
        //工作动态
        List<PeopleDynamicEntity> peopleDynamicVoList = peopleDynamicService.getList(1, 1, id);
        peopleCongressVo.setWorkNewList(peopleDynamicVoList);
        //工作动态 数
        peopleCongressVo.setWorkNewNum(peopleDynamicService.getCount(1, id));
        // 接待记录
        List<PeopleDynamicEntity> peopleDynamicVoList1 = peopleDynamicService.getList(9, 3, id);
        peopleCongressVo.setReceptionList(peopleDynamicVoList1);
        //接待记录 数
        peopleCongressVo.setReceptionNum(peopleDynamicService.getCount(9, id));
        //爱心暖家
        List<PeopleDynamicEntity> peopleDynamicVoList2 = peopleDynamicService.getList(4, 3, id);
        peopleCongressVo.setLoveWarmHomeList(peopleDynamicVoList2);
        //爱心暖家 数
        peopleCongressVo.setLoveWarmHomeNum(peopleDynamicService.getCount(4, id));

        return super.setSuccessModelMap(peopleCongressVo);
    }


    /**
     * 组织结构（服务团队）
     */
    @GetMapping("/organizational/structure")
    @ResponseBody
    public Object getOrganizational() {
        return peopleOrganizationService.getTree();
    }

    /**
     * 文章列表
     */
    @GetMapping("/dynamic/list")
    @ResponseBody
    public Object getDynamiclist(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize,
                                 @RequestParam(required = false) String name, @RequestParam(required = false) Long dynamicType,
                                 @RequestParam(required = false) Long uid) {
        Page page = new Page(pageNo, pageSize);
        Page<Map<String, Object>> info = peopleDynamicService.selectPeopleDynamicList(page, dynamicType, name,uid);
        return super.setSuccessModelMap(info);
    }

    /**
     * 文章列表详情
     */
    @GetMapping("/dynamic/details")
    @ResponseBody
    public Object getdetails(@RequestParam("id") Long id) {
        PeopleDynamicEntity peopleDynamicEntity = peopleDynamicService.queryById(id);
        if (peopleDynamicEntity != null) {
            peopleDynamicEntity.setViews(peopleDynamicEntity.getViews() + 1);
        }
        return super.setSuccessModelMap(peopleDynamicService.update(peopleDynamicEntity));
    }

    /**
     * 群众登录或代表
     * <p>
     * type(1.群众2.代表)
     */
    @PostMapping("/crowd/login")
    @ResponseBody
    public Object getCrowdLogin(LoginUserVo loginUserVo) {
        if (StringUtils.isNotEmpty(loginUserVo.getPhoneCode())) {
            Object code = redisTemplate.opsForValue().get(loginUserVo.getPhone());
            if (code != null) {
                if (!code.equals(loginUserVo.getPhoneCode())) {
                    return super.setModelMap("400", "验证码不正确");
                }
            } else {
                return super.setModelMap("400", "验证码失效");
            }
        } else {
            return super.setModelMap("400", "验证码不能为空");
        }
        WxConfVo wxConfig = wxConfService.getWxConfEntity();
        AllUserEntity allUser = null;
        Map<String, Object> result = new HashMap<>();
        try {
            final WxMaService wxService = WxMaConfiguration.getMaService(wxConfig.getAppid(), wxConfig.getSecret());
            try {
                WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(loginUserVo.getCode());
                String wxOpenid = session.getOpenid();
                // WxMaUserInfo wxMaUserInfo = wxService.getUserService().getUserInfo(session.getSessionKey(), loginUserVo.getEncryptedData(), loginUserVo.getIv());
                allUser = this.allUserService.readByOpenid(wxOpenid);
                //如果用户不存在，则注册用户
                if (allUser == null) {
                    allUser = new AllUserEntity();
                    allUser.setNickname(loginUserVo.getNickname());
                    allUser.setAvatar(loginUserVo.getAvatar());
                    allUser.setPhone(loginUserVo.getPhone());
                    // allUser.setAddress(wxMaUserInfo.getProvince() + "-" + wxMaUserInfo.getCity() + "-" + wxMaUserInfo.getCountry());
                    allUser.setOpenid(wxOpenid);
                    if (loginUserVo.getType() == 1) {
                        allUser.setIdentity(1); //群众
                    } else {
                        PeopleCongressEntity peopleCongressEntity = new PeopleCongressEntity();
                        peopleCongressEntity.setPhone(loginUserVo.getPhone());
                        PeopleCongressEntity peopleCongressEntity1 = peopleCongressService.selectOne(peopleCongressEntity);
                        if (peopleCongressEntity1 == null) {
                            return super.setModelMap("400", "您的身份信息还没被录入，请联系管理员");
                        }
                        allUser.setIdentity(2);  //代表
                    }
                    allUser = allUserService.update(allUser);

                }
                allUser.setLoginTime(new Date());
                allUser.setNickname(loginUserVo.getNickname());
                allUser.setAvatar(loginUserVo.getAvatar());
                //allUser.setAddress(wxMaUserInfo.getProvince() + "-" + wxMaUserInfo.getCity() + "-" + wxMaUserInfo.getCountry());
                allUser.setOpenid(wxOpenid);
                if (loginUserVo.getType() == 1) {
                    allUser.setIdentity(1); //群众
                    allUserService.update(allUser);
                    PeopleCrowdEntity peopleCrowdEntity = new PeopleCrowdEntity();
                    peopleCrowdEntity.setUserId(allUser.getId());
                    PeopleCrowdEntity peopleCrowdEntity1 = peopleCrowdService.selectOne(peopleCrowdEntity);
                    if (peopleCrowdEntity1 == null) {
                        PeopleCrowdEntity peopleCrowdEntity2 = new PeopleCrowdEntity();
                        peopleCrowdEntity2.setUserId(allUser.getId());
                        peopleCrowdEntity2.setPhone(loginUserVo.getPhone());
                        peopleCrowdEntity2.setName(loginUserVo.getName());
                        peopleCrowdService.update(peopleCrowdEntity2);
                        allUser.setCrowdEntity(peopleCrowdEntity2);
                    } else {
                        allUser.setCrowdEntity(peopleCrowdEntity1);
                    }
                } else {
                    PeopleCongressEntity peopleCongressEntity = new PeopleCongressEntity();
                    peopleCongressEntity.setPhone(loginUserVo.getPhone());
                    PeopleCongressEntity peopleCongressEntity1 = peopleCongressService.selectOne(peopleCongressEntity);
                    if (peopleCongressEntity1 == null) {
                        return super.setModelMap("400", "您的身份信息还没被录入，请联系管理员");
                    }
                    allUser.setIdentity(2);  //代表
                    allUserService.update(allUser);
                    peopleCongressEntity1.setUserId(allUser.getId());
                    peopleCongressService.update(peopleCongressEntity1);
                }
            } catch (WxErrorException e) {
                this.logger.error(e.getMessage(), e);
                throw new RequestEmptyException("登录失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //登录并创建token
        String token = authService.login1(allUser.getOpenid(), null);
        /**
         * 用户登录成功
         */
        if (token != null) {
            result.put("allUser", allUser);
            result.put("token", token);
        } else {
            throw new RequestEmptyException("登录失败！");
        }
        return new SuccessResponseData(result);
    }

    /**
     * 获取验证码
     *
     * @param phone
     * @param type
     * @return
     */
    @GetMapping("/phoneCode")
    @ResponseBody
    public Object getCode(@RequestParam("phone") String phone, @RequestParam("type") Integer type) {
        if (type == 2) {
            PeopleCongressEntity peopleCongressEntity = new PeopleCongressEntity();
            peopleCongressEntity.setPhone(phone);
            PeopleCongressEntity peopleCongressEntity1 = peopleCongressService.selectOne(peopleCongressEntity);
            if (peopleCongressEntity1 == null) {
                return super.setModelMap("400", "您的身份信息还没被录入，请联系管理员");
            }
        }
        try {
            int n = super.service.sendVerifySms(phone);
            if (n == 1) {
                return super.setSuccessModelMap();
            } else {
                return super.setModelMap("400", "短信发送失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.setSuccessModelMap();
    }

    /**
     * 今日接待/下一个接待
     */
    @GetMapping("/myApplyInfo/notice")
    @ResponseBody
    public Object getApplyRecord() {
        Map<String, Object> map = new HashMap<>();
        PeopleAppTimeEntity peopleAppTimeEntity = peopleAppTimeService.getToday();
        AppTimeVo appTimeVo = new AppTimeVo();
        BeanUtil.copyProperties(peopleAppTimeEntity, appTimeVo, true);
        if (peopleAppTimeEntity != null) {
            PeopleCongressEntity peopleCongressEntity = peopleCongressService.queryById(peopleAppTimeEntity.getPeopleCongress());
            if (peopleCongressEntity != null) {
                appTimeVo.setPeopleCongressName(peopleCongressEntity.getName());
                appTimeVo.setPhoto(peopleCongressEntity.getPhoto());
                PeopleOrganizationEntity peopleOrganizationEntity = peopleOrganizationService.queryById(peopleCongressEntity.getOrganization());
                if (peopleOrganizationEntity != null) {
                    appTimeVo.setOrganizationName(peopleOrganizationEntity.getName());
                }
            }
            map.put("todayRecord", appTimeVo);
        } else {
            map.put("todayRecord", appTimeVo);
        }

        PeopleAppTimeEntity peopleAppTimeEntity1 = peopleAppTimeService.getNext();
        AppTimeVo appTimeVo1 = new AppTimeVo();
        BeanUtil.copyProperties(peopleAppTimeEntity1, appTimeVo1, true);
        if (peopleAppTimeEntity1 != null) {
            PeopleCongressEntity peopleCongressEntity1 = peopleCongressService.queryById(peopleAppTimeEntity1.getPeopleCongress());
            if (peopleCongressEntity1 != null) {
                appTimeVo1.setPeopleCongressName(peopleCongressEntity1.getName());
                appTimeVo1.setPhoto(peopleCongressEntity1.getPhoto());
                PeopleOrganizationEntity peopleOrganizationEntity = peopleOrganizationService.queryById(peopleCongressEntity1.getOrganization());
                if (peopleOrganizationEntity != null) {
                    appTimeVo1.setOrganizationName(peopleOrganizationEntity.getName());
                }
            }
            map.put("nextRecord", appTimeVo1);
        } else {
            map.put("nextRecord", appTimeVo1);
        }

        return super.setSuccessModelMap(map);

    }

    /**
     * 代表接待记录
     */
    @GetMapping("/reception/records")
    @ResponseBody
    public Object getReceptionRecords(@RequestParam(required = false, defaultValue = "1") Integer pageNo
            , @RequestParam(required = false, defaultValue = "4") Integer pageSize) {
        Page page = new Page(pageNo, pageSize);
        Page<Map<String, Object>> info = peopleAppTimeService.getRecord(page);
        info.getRecords().forEach(peopleApp -> {
            if (peopleApp.get("peopleCongress") != null) {
                PeopleCongressEntity peopleCongressEntity = peopleCongressService.queryById(Long.parseLong(peopleApp.get("peopleCongress").toString()));
                if (peopleCongressEntity != null) {
                    peopleApp.put("userName", peopleCongressEntity.getName());
                    PeopleOrganizationEntity peopleOrganizationEntity = peopleOrganizationService.queryById(peopleCongressEntity.getOrganization());
                    if (peopleOrganizationEntity != null) {
                        peopleApp.put("organizatioonName", peopleOrganizationEntity.getName());
                    }
                }
            }
        });
        return super.setSuccessModelMap(info);
    }

    /**
     * @return
     */
    @GetMapping("/get")
    @ResponseBody
    public Object get() {
        int count = peopleAppRecordService.getAppNoticeNum(1000001549921305L, null);
        return super.setSuccessModelMap(count);
    }


    /**
     * 已解决的优化运行商和征集意见
     */
    @GetMapping("/getByStatus")
    @ResponseBody
    public Object getByStatus(@RequestParam(required = false,defaultValue = "1") Integer pageNo,
                              @RequestParam(required = false,defaultValue = "3") Integer pageSize){
        Page page = new Page(pageNo,pageSize);
       Page<Map<String, Object>> info = peopleOpinionService.getByStatus(page);
       return super.setSuccessModelMap(info);
    }


}

