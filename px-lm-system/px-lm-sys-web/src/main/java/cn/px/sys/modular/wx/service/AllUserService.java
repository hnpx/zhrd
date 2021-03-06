package cn.px.sys.modular.wx.service;

import cn.hutool.core.bean.BeanUtil;
import cn.px.base.Constants;
import cn.px.base.core.BaseServiceImpl;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.sys.modular.activity.entity.ActivityUserSignEntity;
import cn.px.sys.modular.activity.mapper.ActivityUserSignMapper;
import cn.px.sys.modular.activity.service.ActivityUserSignService;
import cn.px.sys.modular.communityClass.entity.CommunityClassEntity;
import cn.px.sys.modular.communityClass.service.CommunityClassService;
import cn.px.sys.modular.doubleReport.entity.ReportCadreEntity;
import cn.px.sys.modular.doubleReport.entity.ReportIntegralEntity;
import cn.px.sys.modular.doubleReport.entity.UnitEntity;
import cn.px.sys.modular.doubleReport.mapper.ReportCadreMapper;
import cn.px.sys.modular.doubleReport.mapper.UnitMapper;
import cn.px.sys.modular.doubleReport.service.ReportCadreService;
import cn.px.sys.modular.doubleReport.service.ReportIntegralService;
import cn.px.sys.modular.doubleReport.service.UnitService;
import cn.px.sys.modular.homeClass.service.HomeClassService;
import cn.px.sys.modular.integral.constant.TypeEnum;
import cn.px.sys.modular.integral.entity.IntegralRecordEntity;
import cn.px.sys.modular.integral.mapper.IntegralRecordMapper;
import cn.px.sys.modular.integral.service.IntegralRecordService;
import cn.px.sys.modular.integralWater.service.IntegralWaterService;
import cn.px.sys.modular.member.entity.MemberEntity;
import cn.px.sys.modular.member.mapper.MemberMapper;
import cn.px.sys.modular.member.service.MemberService;
import cn.px.sys.modular.merchant.entity.MerchantEntity;
import cn.px.sys.modular.merchant.service.MerchantService;
import cn.px.sys.modular.project.constant.PersonnelTypeEnum;
import cn.px.sys.modular.project.entity.ProjectEntity;
import cn.px.sys.modular.project.service.ProjectService;
import cn.px.sys.modular.unitReports.entity.UnitReportsEntity;
import cn.px.sys.modular.unitReports.service.UnitReportsService;
import cn.px.sys.modular.wx.constant.ReportStatusEnum;
import cn.px.sys.modular.wx.entity.AllUserEntity;
import cn.px.sys.modular.wx.mapper.AllUserMapper;
import cn.px.sys.modular.wx.vo.AllUserVo;
import cn.px.sys.modular.wx.vo.CadreVo;
import cn.px.sys.modular.wx.vo.UserVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * (AllUser)??????????????????
 *
 * @author
 * @since 2020-08-29 11:03:35
 */
@Service("allUserService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "allUser")
public class AllUserService extends BaseServiceImpl<AllUserEntity, AllUserMapper> {

    @Resource
    private MemberMapper memberMapper;
    @Resource
    private ReportCadreMapper reportCadreMapper;
    @Resource
    private UnitMapper unitMapper;
    @Resource
     private CommunityClassService communityClassService;
    @Resource
    private HomeClassService homeClassService;
    @Resource
    private UnitService unitService;

    @Resource
    private MerchantService merchantService;
    @Resource
    private IntegralRecordMapper integralRecordMapper;
    @Resource
    private ReportIntegralService reportIntegralService;
    @Resource
    private IntegralRecordService integralRecordService;
    @Resource
    private MemberService memberService;
    @Resource
    private ReportCadreService reportCadreService;
    @Resource
    private ActivityUserSignMapper activityUserSignMapper;
    @Resource
    private ProjectService projectService;
    @Resource
    private IntegralWaterService integralWaterService;

    @Autowired
    private UnitReportsService unitReportsService;

    /**
     * ??????openid????????????
     * @param openid
     * @return
     */
    public AllUserEntity readByOpenid(@Param("openid") String openid){
        return super.mapper.readByOpenid(openid);
    }

    /**
     * ????????????
     * @param member
     * @return
     */
    @Transactional
    public AllUserEntity register(AllUserEntity member){
        member.setEnable(Constants.ENABLE_TRUE);
        super.update(member);
        return member;
    }

    public AllUserVo getAllUserVo(AllUserEntity allUser){
        AllUserVo allUserVo = new AllUserVo();
        BeanUtil.copyProperties(allUser,allUserVo);
        MerchantEntity merchantEntity = merchantService.getMerchantEntityByUserId(allUser.getId());
        if(merchantEntity != null){
            allUserVo.setMerchantStatus(merchantEntity.getStatus());
        }

        //????????????
        try{
          Integer showIntegral =  integralWaterService.showIntegral(allUser.getId());
            allUserVo.setShowIntegral(showIntegral);
        }catch (Exception e){
            allUserVo.setShowIntegral(0);
        }
        //??????????????????
        try{
            Integer integral =  integralRecordMapper.getIntegralToday(allUser.getId());
            allUserVo.setIntegralToday(integral);
        }catch (Exception e){
            allUserVo.setIntegralToday(0);
        }

        if(allUserVo.getType()!= null){
            switch (allUserVo.getType())
            {//????????????
                case 1:
                    MemberEntity memberEntity = memberMapper.getMemberEntityByPhone(allUserVo.getId());
                    if(memberEntity != null){
                        allUserVo.setName(memberEntity.getName());
                        allUserVo.setAddress(memberEntity.getAddress());
                        allUserVo.setIdNumber(memberEntity.getIdNumber());
                        allUserVo.setBelongingCommunity(memberEntity.getBelongingCommunity());
                        allUserVo.setBelongingHome(memberEntity.getBelongingHome());
                        allUserVo.setBelongingUnit(memberEntity.getBelongingUnit());
                        allUserVo.setIsBind(memberEntity.getIsBind());
                    }
                    break;
                //????????????
                case 2:
                    ReportCadreEntity reportCadreEntity = reportCadreMapper.getReportCadreEntityByUserId(allUserVo.getId());
                    if(reportCadreEntity != null){
                        allUserVo.setName(reportCadreEntity.getName());
                        allUserVo.setAddress(reportCadreEntity.getAddress());
                        allUserVo.setIdNumber(reportCadreEntity.getIdNumber());
                        allUserVo.setBelongingCommunity(reportCadreEntity.getBelongingCommunity());
                        allUserVo.setBelongingHome(reportCadreEntity.getBelongingHome());
                        allUserVo.setBelongingUnit(reportCadreEntity.getBelongingUnit());
                        allUserVo.setIsBind(reportCadreEntity.getIsBind());
                    }

                    break;
                //??????
                case 3:
                    UnitEntity unitEntity = unitMapper.getUnitEntityByUserId(allUserVo.getId());
                    if(unitEntity != null){
                        allUserVo.setName(unitEntity.getContactPerson());
                        allUserVo.setAddress(unitEntity.getAddress());
                        allUserVo.setBelongingCommunity(unitEntity.getBelongingCommunity());
                        allUserVo.setBelongingUnit(unitEntity.getId());
                        CommunityClassEntity communityClassEntity = communityClassService.queryById(unitEntity.getBelongingCommunity());
                        allUserVo.setBelongingCommunityName(communityClassEntity.getName());
                        UnitEntity unitEntity1 =unitService.queryById(unitEntity.getId());
                        allUserVo.setBelongingUnitName(unitEntity1.getName());
                    }
                    break;

            }
        }
        return allUserVo;
    }

    /**
     * ?????????
     * @param userId
     */
    @Transactional
    public int report(Long userId){
     AllUserEntity allUserEntity =   this.queryById(userId);
     String msg;
        if(allUserEntity.getType().equals(PersonnelTypeEnum.SIGN_ENUM_TWO.getValue()) ){
            if(allUserEntity.getStatus().equals(ReportStatusEnum.REPORT_STATUS_ENUM_ONE.getValue())){
                List<ReportIntegralEntity> reportIntegralEntityList = reportIntegralService.getReportIntegralEntity();
                int integral =  reportIntegralEntityList.get(0).getIntegral();
                allUserEntity.setIntegral(allUserEntity.getIntegral()+integral);
                allUserEntity.setRemainingPoints(allUserEntity.getRemainingPoints()+integral);
                allUserEntity.setStatus(ReportStatusEnum.REPORT_STATUS_ENUM_TWO.getValue());
                super.update(allUserEntity);
                IntegralRecordEntity integralRecordEntity = new IntegralRecordEntity();
                integralRecordEntity.setIntegral(integral);
                integralRecordEntity.setType(TypeEnum.TYPE_ENUM_ONE.getValue());
                integralRecordEntity.setSource("?????????");
                integralRecordEntity.setUserId(userId);
                integralRecordService.update(integralRecordEntity);
                return 1;
            }else {
                return 2;
            }

        }else {
            return 0;
        }

    }

    /**
     * ????????????????????????
     */
    public void initReport(){
     List<AllUserEntity> allUserEntityList  = super.mapper.getAllUserEntityByType1();
        for (AllUserEntity allUserEntity:allUserEntityList) {
            allUserEntity.setStatus(ReportStatusEnum.REPORT_STATUS_ENUM_ONE.getValue());
            this.update(allUserEntity);
        }
    }

    /**
     * ?????????????????????????????????
     * @return
     */
    public Page<Map<String,Object>> getAllUserEntityByType(Integer page,Integer pageSize){
        Page page1 = new Page(page,pageSize);
        return super.mapper.getAllUserEntityByType(page1);

    }

    /**
     * ????????????id?????????????????????
     * @param userId
     * @return
     */
    public UserVo getName(Long userId){
       AllUserEntity allUserEntity = this.queryById(userId);
       Integer type = allUserEntity.getType();
        UserVo userVo = new UserVo();
       if(type != null){
           switch (type){
               //????????????
               case 1:
                   MemberEntity memberEntity =  memberMapper.getMemberEntityByUserId(userId);
                   BeanUtil.copyProperties(memberEntity,userVo);
                   break;
               //????????????
               case 2:
                   ReportCadreEntity reportCadreEntity =   reportCadreMapper.getReportCadreEntityByUserId(userId);

                   BeanUtil.copyProperties(reportCadreEntity,userVo);
                   break;
               //??????
               case 3:
                   UnitEntity unitEntity = unitMapper.getUnitEntityByUserId(userId);
                   BeanUtil.copyProperties(unitEntity,userVo);
                   if(unitEntity != null){
                       userVo.setBelongingUnit(unitEntity.getId());
                   }else {
                       userVo.setBelongingUnit(null);
                   }


                   break;
           }
       }

       return userVo;

    }


    /**
     * ????????????????????????
     * @param activeId
     * @param
     * @return
     */
    public Page<Map<String,Object>> getListUser(Long activeId,String nickname) {
       /* ProjectEntity projectEntity = projectService.queryById(pid);
        Integer userType = projectEntity.getPersonnelType();*/
       Integer userType = null;
       Long uid = null;
        String userIds = "";
        Page page = LayuiPageFactory.defaultPage();
        List<AllUserEntity> allUserEntities = super.mapper.getUserIds(activeId);
        if(allUserEntities.size() !=0){
            for (AllUserEntity allUserEntity:allUserEntities) {
                userIds = userIds + allUserEntity.getId()+",";
            }
            userIds = userIds.substring(0,userIds.length()-1);
        }

       return super.mapper.getListUser(page,userIds,nickname,userType,uid);
    }

    /**
     * ????????????????????????
     * @param
     * @param
     * @return
     */
    public Page<Map<String,Object>> getListUser1(Long pid,String nickname) {
      ProjectEntity projectEntity = projectService.queryById(pid);
      Integer userType = projectEntity.getPersonnelType();
      Long uid = projectEntity.getBelongingUnit();
        String userIds = "";
        Page page = LayuiPageFactory.defaultPage();
        List<AllUserEntity> allUserEntities = super.mapper.getUserIds1(pid);
        if(allUserEntities.size() !=0){
            for (AllUserEntity allUserEntity:allUserEntities) {
                userIds = userIds + allUserEntity.getId()+",";
            }
            userIds = userIds.substring(0,userIds.length()-1);
        }
        return super.mapper.getListUser(page,userIds,nickname,userType,uid);
    }
}