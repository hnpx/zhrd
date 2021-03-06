package cn.px.sys.modular.peopleInformationSet.controller;

import cn.hutool.core.thread.ThreadUtil;
import cn.px.base.support.cache.RedisHelper;
import cn.px.sys.modular.peopleAppRecord.entity.PeopleAppRecordEntity;
import cn.px.sys.modular.peopleAppRecord.service.PeopleAppRecordService;
import cn.px.sys.modular.peopleAppRecord.vo.ConfirmVo;
import cn.px.sys.modular.peopleAppRecord.vo.PeopleAppRecordVo;
import cn.px.sys.modular.peopleCongress.entity.PeopleCongressEntity;
import cn.px.sys.modular.peopleCongress.service.PeopleCongressService;
import cn.px.sys.modular.peopleCrowd.entity.PeopleCrowdEntity;
import cn.px.sys.modular.peopleCrowd.service.PeopleCrowdService;
import cn.px.sys.modular.peopleInformationSet.entity.PeopleInformationSetEntity;
import cn.px.sys.modular.peopleInformationSet.service.PeopleInformationSetService;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.sys.modular.peopleOpinion.entity.PeopleOpinionEntity;
import cn.px.sys.modular.peopleOpinion.service.PeopleOpinionService;
import cn.px.sys.modular.peopleOpinion.vo.PeopleOpinionVo;
import cn.px.sys.modular.peopleOrganization.entity.PeopleOrganizationEntity;
import cn.px.sys.modular.peopleOrganization.service.PeopleOrganizationService;
import cn.px.sys.modular.wx.entity.AllUserEntity;
import cn.px.sys.modular.wx.service.AllUserService;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.parameters.P;
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
 * ??????????????????(PeopleInformationSet)????????????
 *
 * @author
 * @since 2021-05-13 16:08:45
 */
@RestController
@RequestMapping("peopleInformationSet")
@Api(value = "??????????????????(PeopleInformationSet)??????")
public class PeopleInformationSetController extends BaseController<PeopleInformationSetEntity, PeopleInformationSetService> {

    private static final String PREFIX = "/modular/peopleInformationSet";
    private final String db = "daibiao";
    private final String qz = "qunzhong";

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PeopleCrowdService peopleCrowdService;
    @Autowired
    private PeopleAppRecordService peopleAppRecordService;
    @Autowired
    private PeopleCongressService peopleCongressService;
    @Autowired
    private PeopleOpinionService peopleOpinionService;
    @Autowired
    private AllUserService allUserService;
    @Autowired
    private PeopleOrganizationService peopleOrganizationService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired(required = false)
    private RedisHelper redisHelper;
    @Autowired
    private PeopleInformationSetService peopleInformationSetService;


    /**
     * ??????????????????????????????
     *
     * @param id ??????
     * @return ????????????
     */
    @GetMapping("/read/detail/{id}")
    @ResponseBody
    public Object selectOne(ModelMap modelMap, @PathVariable("id") Long id) {
        return super.setSuccessModelMap(modelMap, super.service.queryById(id));
    }

    @ApiOperation("??????")
    @PutMapping("/read/list")
    @ResponseBody
    public Object query(ModelMap modelMap, PeopleInformationSetEntity params) {
        Page<Long> page = LayuiPageFactory.defaultPage();
        Map<String, Object> paramsMap = params == null ? new HashMap<String, Object>() : BeanUtil.beanToMap(params);
        return super.query(modelMap, params, page);
    }

    @ApiOperation("??????")
    @PostMapping("/update")
    @ResponseBody
    public Object update(ModelMap modelMap, PeopleInformationSetEntity params) {
        //TODO ????????????
        return super.update(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    @ApiOperation("??????")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object del(ModelMap modelMap, PeopleInformationSetEntity params) {
        Assert.notNull(params.getId(), "ID");
        return super.del(modelMap, params, LoginContextHolder.getContext().getUserId());
    }

    /**
     * ??????????????????
     */
    @PostMapping("/appointment/online")
    @ResponseBody
    public Object getAppointment(PeopleAppRecordVo peopleAppRecordVo) {
        Long user = LoginContextHolder.getContext().getUserId();
        PeopleCrowdEntity peopleCrowdEntity = new PeopleCrowdEntity();
        peopleCrowdEntity.setUserId(user);
        PeopleCrowdEntity peopleCrowdEntity1 = peopleCrowdService.selectOne(peopleCrowdEntity);
        if (peopleCrowdEntity1 != null) {
            peopleAppRecordVo.setCrowdId(peopleCrowdEntity1.getId());
            peopleAppRecordVo.setCreateBy(peopleCrowdEntity1.getId());
        } else {
            return super.setModelMap("400", "?????????????????????????????????????????????????????????");
        }
        peopleAppRecordVo.setCreateTime(new Date());
        PeopleAppRecordEntity peopleAppRecordEntity = new PeopleAppRecordEntity();
        BeanUtil.copyProperties(peopleAppRecordVo, peopleAppRecordEntity, true);
        peopleAppRecordEntity.setStatus(1); //?????????
        peopleAppRecordEntity.setIsToview(1); //??????
        peopleAppRecordEntity.setIsDelete(2);
        //?????????????????????

        // peopleAppRecordEntity.setRoomNumber();
        return super.setSuccessModelMap(peopleAppRecordService.update(peopleAppRecordEntity));

    }

    /**
     * ???????????????????????????
     */
    @PostMapping("/opinion")
    @ResponseBody
    public Object getOpinion(PeopleOpinionVo peopleOpinionVo) {
        Long user = LoginContextHolder.getContext().getUserId();
        PeopleCrowdEntity peopleCrowdEntity = new PeopleCrowdEntity();
        peopleCrowdEntity.setUserId(user);
        PeopleCrowdEntity peopleCrowdEntity1 = peopleCrowdService.selectOne(peopleCrowdEntity);
        if (peopleCrowdEntity1 == null) {
            return super.setModelMap("400", "?????????????????????????????????????????????????????????");
        } else {
            peopleOpinionVo.setName(peopleCrowdEntity1.getName());
            peopleOpinionVo.setPhone(peopleCrowdEntity1.getPhone());
        }
        PeopleOpinionEntity peopleOpinionEntity = new PeopleOpinionEntity();
        BeanUtil.copyProperties(peopleOpinionVo, peopleOpinionEntity, true);
        peopleOpinionEntity.setStatus(1);
        return super.setSuccessModelMap(peopleOpinionService.update(peopleOpinionEntity));
    }


    /**
     * ????????????/??????????????????
     */
    @GetMapping("/getName")
    @ResponseBody
    public Object getName() {
        Long user = LoginContextHolder.getContext().getUserId();
        AllUserEntity allUserEntity = allUserService.queryById(user);
        if (allUserEntity != null) {
            if (allUserEntity.getIdentity() != null) {
                if (allUserEntity.getIdentity() == 1) {
                    PeopleCrowdEntity peopleCrowdEntity = new PeopleCrowdEntity();
                    peopleCrowdEntity.setUserId(user);
                    PeopleCrowdEntity peopleCrowdEntity1 = peopleCrowdService.selectOne(peopleCrowdEntity);
                    if (peopleCrowdEntity1 == null) {
                        return super.setModelMap("400", "???????????????????????????????????????");
                    } else {
                        Integer count = peopleAppRecordService.getAppNoticeNum(peopleCrowdEntity1.getId(), null);
                        peopleCrowdEntity1.setNoticeNum(count);
                        if (peopleCrowdEntity1.getUserId() != null) {
                            AllUserEntity allUserEntity1 = allUserService.queryById(peopleCrowdEntity1.getUserId());
                            peopleCrowdEntity1.setAvatar(allUserEntity1.getAvatar());
                        }
                        return super.setSuccessModelMap(peopleCrowdEntity1);
                    }
                } else {
                    PeopleCongressEntity peopleCongressEntity = new PeopleCongressEntity();
                    peopleCongressEntity.setUserId(user);
                    PeopleCongressEntity peopleCongressEntity1 = peopleCongressService.selectOne(peopleCongressEntity);
                    if (peopleCongressEntity1 == null) {
                        return super.setModelMap("400", "???????????????????????????????????????");
                    } else {
                        int count = peopleAppRecordService.getAppNoticeNum(null, peopleCongressEntity1.getId());
                        peopleCongressEntity1.setNoticeNum(count);
                        if (peopleCongressEntity1.getOrganization() != null) {
                            PeopleOrganizationEntity peopleOrganizationEntity = peopleOrganizationService.queryById(peopleCongressEntity1.getOrganization());
                            peopleCongressEntity1.setOrganizationName(peopleOrganizationEntity.getName());
                        }
                        return super.setSuccessModelMap(peopleCongressEntity1);
                    }
                }
            } else {
                return super.setModelMap("400", "??????????????????????????????");
            }
        } else {
            return super.setModelMap("400", "?????????????????????");
        }


    }

    /**
     * ??????/????????????????????????
     */
    @GetMapping("/myApplyInfo")
    @ResponseBody
    public Object getmyApplyInfo(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize, @RequestParam("type") Integer type) {
        Page page = new Page(pageNo, pageSize);
        Long user = LoginContextHolder.getContext().getUserId();
        AllUserEntity allUserEntity = allUserService.queryById(user);
        if (allUserEntity != null) {
            if (allUserEntity.getIdentity() != null) {
                if (allUserEntity.getIdentity() == 1) {
                    PeopleCrowdEntity peopleCrowdEntity = new PeopleCrowdEntity();
                    peopleCrowdEntity.setUserId(user);
                    PeopleCrowdEntity peopleCrowdEntity1 = peopleCrowdService.selectOne(peopleCrowdEntity);
                    if (peopleCrowdEntity1 == null) {
                        return super.setModelMap("400", "?????????????????????????????????????????????");
                    }
                    Page<PeopleAppRecordEntity> peopleAppRecordEntityPage = peopleAppRecordService.getAppRecordList(page, type, peopleCrowdEntity1.getId(), null);
                    peopleAppRecordEntityPage.getRecords().forEach(peopleAppRecordEntity -> {
                        PeopleCongressEntity peopleCongressEntity = peopleCongressService.queryById(peopleAppRecordEntity.getCongressId());
                        if (peopleCongressEntity != null) {
                            peopleAppRecordEntity.setCongressName(peopleCongressEntity.getName());
                        }
                    });
                    return super.setSuccessModelMap(peopleAppRecordEntityPage);
                } else {
                    PeopleCongressEntity peopleCongressEntity = new PeopleCongressEntity();
                    peopleCongressEntity.setUserId(user);
                    PeopleCongressEntity peopleCongressEntity1 = peopleCongressService.selectOne(peopleCongressEntity);
                    if (peopleCongressEntity1 == null) {
                        return super.setModelMap("400", "???????????????????????????????????????");
                    }
                    Page<PeopleAppRecordEntity> peopleAppRecordEntityPage = peopleAppRecordService.getAppRecordList(page, type, null, peopleCongressEntity1.getId());
                    peopleAppRecordEntityPage.getRecords().forEach(peopleAppRecordEntity -> {
                        PeopleCrowdEntity peopleCrowdEntity = peopleCrowdService.queryById(peopleAppRecordEntity.getCrowdId());
                        if (peopleCrowdEntity != null) {
                            peopleAppRecordEntity.setCrowdName(peopleCrowdEntity.getName());
                        }
                    });
                    return super.setSuccessModelMap(peopleAppRecordEntityPage);

                }
            } else {
                return super.setModelMap("400", "??????????????????????????????");
            }
        } else {
            return super.setModelMap("400", "?????????????????????");
        }

    }

    /**
     * ??????/??????????????????????????????
     */
    @GetMapping("/myApplyInfo/notice")
    @ResponseBody
    public Object getmyApplyInfoNotice() {
        Long user = LoginContextHolder.getContext().getUserId();
        AllUserEntity allUserEntity = allUserService.queryById(user);
        if (allUserEntity != null) {
            if (allUserEntity.getIdentity() != null) {
                if (allUserEntity.getIdentity() == 1) {
                    PeopleCrowdEntity peopleCrowdEntity = new PeopleCrowdEntity();
                    peopleCrowdEntity.setUserId(user);
                    PeopleCrowdEntity peopleCrowdEntity1 = peopleCrowdService.selectOne(peopleCrowdEntity);
                    if (peopleCrowdEntity1 == null) {
                        return super.setModelMap("400", "?????????????????????????????????????????????");
                    }
                    List<PeopleAppRecordEntity> peopleAppRecordEntityList = peopleAppRecordService.getAppNotice(peopleCrowdEntity1.getId(), null);
                    peopleAppRecordEntityList.forEach(peopleAppRecordEntity -> {
                        peopleAppRecordEntity.setIsToview(2);
                        peopleAppRecordService.update(peopleAppRecordEntity);
                    });
                    return super.setSuccessModelMap(peopleAppRecordEntityList);
                } else {
                    PeopleCongressEntity peopleCongressEntity = new PeopleCongressEntity();
                    peopleCongressEntity.setUserId(user);
                    PeopleCongressEntity peopleCongressEntity1 = peopleCongressService.selectOne(peopleCongressEntity);
                    if (peopleCongressEntity1 == null) {
                        return super.setModelMap("400", "???????????????????????????????????????");
                    }
                    List<PeopleAppRecordEntity> peopleAppRecordEntityList = peopleAppRecordService.getAppNotice(null, peopleCongressEntity1.getId());
                    peopleAppRecordEntityList.forEach(peopleAppRecordEntity -> {
                        peopleAppRecordEntity.setIsToview(2);
                        peopleAppRecordService.update(peopleAppRecordEntity);
                    });
                    return super.setSuccessModelMap(peopleAppRecordEntityList);
                }
            } else {
                return super.setModelMap("400", "??????????????????????????????");
            }
        } else {
            return super.setModelMap("400", "?????????????????????");
        }


    }

    /**
     * ????????????????????????
     */
    @GetMapping("/applyCongress/details")
    @ResponseBody
    public Object getApplyCongressDetails(@RequestParam("id") Long id) {
        PeopleAppRecordEntity peopleAppRecordEntity = peopleAppRecordService.queryById(id);
        if (peopleAppRecordEntity != null) {
            PeopleCrowdEntity peopleCrowdEntity = peopleCrowdService.queryById(peopleAppRecordEntity.getCrowdId());
            if (peopleCrowdEntity != null) {
                peopleAppRecordEntity.setCrowdName(peopleCrowdEntity.getName());
                peopleAppRecordEntity.setCrowdPhone(peopleCrowdEntity.getPhone());
            }
        }
        return super.setSuccessModelMap(peopleAppRecordEntity);
    }

    /**
     * ???????????? (?????????????????????)
     */
    @GetMapping("/applyCongress/confirm")
    @ResponseBody
    public Object getconfirm(ConfirmVo confirmVo) {
        PeopleAppRecordEntity peopleAppRecordEntity = peopleAppRecordService.queryById(confirmVo.getId());
        peopleAppRecordEntity.setAppTime(confirmVo.getAppTime());
        if (confirmVo.getStatus() == 1) { // ??????
            peopleAppRecordEntity.setStatus(2);
        } else if ((confirmVo.getStatus() == 2)) { //??????
            peopleAppRecordEntity.setStatus(3);
            peopleAppRecordEntity.setInstructions(confirmVo.getInstructions());
        }
        return super.setSuccessModelMap(peopleAppRecordService.update(peopleAppRecordEntity));
    }


    /**
     * ????????????
     */
    @GetMapping("/open/room")
    @ResponseBody
    public Object openRoom(@RequestParam("id") Long id) {

        PeopleAppRecordEntity peopleAppRecordEntity = peopleAppRecordService.queryById(id);
        Long user = LoginContextHolder.getContext().getUserId();
        AllUserEntity allUserEntity = allUserService.queryById(user);
        if (allUserEntity != null) {  //identity ?????????1.??????2.???????????????
            if (allUserEntity.getIdentity() == 2) {
                String roomNumber = peopleInformationSetService.getRandom(9).toString();
                peopleAppRecordEntity.setRoomNumber(roomNumber);
                peopleAppRecordService.update(peopleAppRecordEntity);
                //????????????????????????????????? redis ??????????????????
                redisHelper.set(roomNumber, 1);
                System.out.println(roomNumber + "??????roomNumber=========" + redisHelper.get(roomNumber));
                return super.setSuccessModelMap(peopleAppRecordEntity);
            } else if (allUserEntity.getIdentity() == 1) {
                PeopleAppRecordEntity peopleAppRecordEntity1 = peopleAppRecordService.queryById(id);
                if (StringUtils.isNotEmpty(peopleAppRecordEntity.getRoomNumber())) {
                    if (redisHelper.exists(peopleAppRecordEntity1.getRoomNumber())) {
                        //????????????????????????????????? redis ??????????????????
                        redisHelper.set(peopleAppRecordEntity1.getRoomNumber(), 2);
                        //??????????????????
                        ThreadUtil.execute(() -> {
                            try {
                                Thread.sleep(1000);
                                Boolean b = peopleAppRecordService.startVideo(peopleAppRecordEntity1.getRoomNumber());
                                if (b) {
                                    System.out.println("??????????????????******************************");
                                } else {
                                    System.out.println("??????????????????******************************");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                        peopleAppRecordService.startVideo(peopleAppRecordEntity1.getRoomNumber());
                        System.out.println(peopleAppRecordEntity1.getRoomNumber() + "??????roomNumber=========" + redisHelper.get(peopleAppRecordEntity1.getRoomNumber()));
                        return super.setSuccessModelMap(peopleAppRecordEntity1);
                    } else {
                        return super.setModelMap("400", "????????????????????????????????????????????????");
                    }
                } else {
                    return super.setModelMap("400", "????????????????????????????????????????????????");
                }

            } else {
                return super.setModelMap("400", "???????????????????????????????????????");
            }
        }
        return super.setSuccessModelMap(peopleAppRecordEntity);

    }

    /**
     * ????????????
     */
    @GetMapping("/leave/room")
    @ResponseBody
    public Object getLeave(@RequestParam("id") Long id) {
        PeopleAppRecordEntity peopleAppRecordEntity = peopleAppRecordService.queryById(id);
        if (redisHelper.exists(peopleAppRecordEntity.getRoomNumber() + qz)) {
            if (redisHelper.get(peopleAppRecordEntity.getRoomNumber() + qz).equals(1)) {
                peopleAppRecordEntity.setStatus(4);
                peopleAppRecordService.update(peopleAppRecordEntity);
            } else if (redisHelper.get(peopleAppRecordEntity.getRoomNumber() + qz).equals(1)) {
                redisHelper.del(peopleAppRecordEntity.getRoomNumber());
                peopleAppRecordEntity.setRoomNumber("");
                peopleAppRecordService.update(peopleAppRecordEntity);
            }
        }
        return super.setSuccessModelMap();
    }


    /**
     * ??????????????????
     */
    @GetMapping("/congress/open/room")
    @ResponseBody
    public Object getCongressRoom(@RequestParam("id") Long id) {
        Map<String, Object> map = new HashMap<>();
        PeopleAppRecordEntity peopleAppRecordEntity = peopleAppRecordService.queryById(id);
        if (StringUtils.isNotEmpty(peopleAppRecordEntity.getRoomNumber())) {
            if (redisHelper.exists(peopleAppRecordEntity.getRoomNumber() + qz)) {
                if (redisHelper.get(peopleAppRecordEntity.getRoomNumber() + qz).equals(1)) {
                    map.put("roomNumber", peopleAppRecordEntity.getRoomNumber());
                    //????????????????????????????????? redis ?????????????????????1.??????2.???????????????
                    //status(1.?????????2.????????????)
                    map.put("status", 1);

                    //??????????????????
                    ThreadUtil.execute(() -> {
                        try {
                            Thread.sleep(1000);
                            Boolean b = peopleAppRecordService.startVideo(peopleAppRecordEntity.getRoomNumber());
                            if (b) {
                                System.out.println("??????????????????******************************");
                            } else {
                                System.out.println("??????????????????******************************");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    return super.setSuccessModelMap(map);
                } else {
                    redisHelper.set(peopleAppRecordEntity.getRoomNumber() + db, 1);
                    map.put("roomNumber", peopleAppRecordEntity.getRoomNumber());
                    //????????????????????????????????? redis ?????????????????????1.??????2.???????????????
                    //status(1.?????????2.????????????)
                    map.put("status", 2);
                    return super.setSuccessModelMap(map);
                }
            } else {
                map.put("roomNumber", peopleAppRecordEntity.getRoomNumber());
                //????????????????????????????????? redis ?????????????????????1.??????2.???????????????
                //status(1.?????????2.????????????)
                map.put("status", 2);
                redisHelper.set(peopleAppRecordEntity.getRoomNumber() + db, 1);
                return super.setSuccessModelMap(map);
            }
        } else {
            String roomNumber = peopleInformationSetService.getRandom(9).toString();
            peopleAppRecordEntity.setRoomNumber(roomNumber);
            peopleAppRecordService.update(peopleAppRecordEntity);
            //????????????????????????????????? redis ??????????????????
            redisHelper.set(roomNumber + db, 1);
            map.put("roomNumber", roomNumber);
            //????????????????????????????????? redis ?????????????????????1.??????2.???????????????
            //status(1.?????????2.????????????)
            map.put("status", 2);
            return super.setSuccessModelMap(map);
        }
    }


    /**
     * ??????????????????
     */
    @GetMapping("/crowd/open/room")
    @ResponseBody
    public Object getCrowdRoom(@RequestParam("id") Long id) {
        Map<String, Object> map = new HashMap<>();
        PeopleAppRecordEntity peopleAppRecordEntity = peopleAppRecordService.queryById(id);
        if (StringUtils.isNotEmpty(peopleAppRecordEntity.getRoomNumber())) {
            if (redisHelper.exists(peopleAppRecordEntity.getRoomNumber() + db)) {
                if (redisHelper.get(peopleAppRecordEntity.getRoomNumber() + db).equals(1)) {
                    map.put("roomNumber", peopleAppRecordEntity.getRoomNumber());
                    //????????????????????????????????? redis ?????????????????????1.??????2.???????????????
                    redisHelper.set(peopleAppRecordEntity.getRoomNumber() + qz, 1);
                    //status(1.?????????2.????????????)
                    map.put("status", 1);

                    //??????????????????
                    ThreadUtil.execute(() -> {
                        try {
                            Thread.sleep(1000);
                            Boolean b = peopleAppRecordService.startVideo(peopleAppRecordEntity.getRoomNumber());
                            if (b) {
                                System.out.println("??????????????????******************************");
                            } else {
                                System.out.println("??????????????????******************************");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    return super.setSuccessModelMap(map);
                } else {
                    map.put("roomNumber", peopleAppRecordEntity.getRoomNumber());
                    map.put("status", 2);
                    return super.setSuccessModelMap(map);
                }
            } else {
                map.put("roomNumber", peopleAppRecordEntity.getRoomNumber());
                map.put("status", 2);
                return super.setSuccessModelMap(map);
            }
        } else {
            map.put("roomNumber", peopleAppRecordEntity.getRoomNumber());
            map.put("status", 2);
            return super.setSuccessModelMap(map);
        }
    }

    /**
     * ????????????????????????
     */
    @GetMapping("/congress/close/room")
    @ResponseBody
    public Object getClose(@RequestParam("id") Long id){
        PeopleAppRecordEntity peopleAppRecordEntity = peopleAppRecordService.queryById(id);
        if (StringUtils.isNotEmpty(peopleAppRecordEntity.getRoomNumber())) {
            if (redisHelper.exists(peopleAppRecordEntity.getRoomNumber() + db)) {
                redisHelper.del(peopleAppRecordEntity.getRoomNumber() + db);
                peopleAppRecordEntity.setRoomNumber("");
                peopleAppRecordService.update(peopleAppRecordEntity);
            }
        }
        return super.setSuccessModelMap();
    }

}




