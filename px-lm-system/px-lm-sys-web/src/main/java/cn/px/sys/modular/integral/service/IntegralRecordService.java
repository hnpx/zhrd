package cn.px.sys.modular.integral.service;

import cn.px.base.Constants;
import cn.px.base.core.BaseServiceImpl;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.base.support.Pagination;
import cn.px.sys.modular.activity.entity.ActivityEntity;
import cn.px.sys.modular.activity.service.ActivityService;
import cn.px.sys.modular.communityClass.entity.CommunityClassEntity;
import cn.px.sys.modular.communityClass.mapper.CommunityClassMapper;
import cn.px.sys.modular.communityClass.service.CommunityClassService;
import cn.px.sys.modular.doubleReport.entity.UnitEntity;
import cn.px.sys.modular.doubleReport.mapper.ReportCadreMapper;
import cn.px.sys.modular.doubleReport.mapper.UnitMapper;
import cn.px.sys.modular.doubleReport.service.UnitService;
import cn.px.sys.modular.doubleReport.vo.ReportCadreVo;
import cn.px.sys.modular.doubleReport.vo.UnitListVo;
import cn.px.sys.modular.integral.constant.StatusOrderEnum;
import cn.px.sys.modular.integral.constant.TypeEnum;
import cn.px.sys.modular.integral.entity.IntegralRecordEntity;
import cn.px.sys.modular.integral.mapper.IntegralRecordMapper;
import cn.px.sys.modular.member.mapper.MemberMapper;
import cn.px.sys.modular.member.vo.MemberVo;
import cn.px.sys.modular.merchant.entity.MerchantEntity;
import cn.px.sys.modular.merchant.service.MerchantService;
import cn.px.sys.modular.project.entity.ProjectEntity;
import cn.px.sys.modular.project.service.ProjectService;
import cn.px.sys.modular.spike.constant.TypeEnum1;
import cn.px.sys.modular.spike.entity.OrdersEntity;
import cn.px.sys.modular.spike.entity.ProductEntity;
import cn.px.sys.modular.spike.entity.SpikeEntity;
import cn.px.sys.modular.spike.mapper.OrdersMapper;
import cn.px.sys.modular.spike.service.OrdersService;
import cn.px.sys.modular.spike.service.ProductService;
import cn.px.sys.modular.spike.service.SpikeService;
import cn.px.sys.modular.spike.vo.SpVo;
import cn.px.sys.modular.wx.entity.AllUserEntity;
import cn.px.sys.modular.wx.service.AllUserService;
import cn.stylefeng.roses.core.mutidatasource.annotion.DataSource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * ????????????(IntegralRecord)??????????????????
 *
 * @author
 * @since 2020-09-07 09:35:59
 */
@Service("integralRecordService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "integralRecord")
public class IntegralRecordService extends BaseServiceImpl<IntegralRecordEntity, IntegralRecordMapper> {
   @Resource
   private ActivityService activityService;
   @Resource
   private AllUserService allUserService;
   @Resource
   private ProductService productService;

   @Resource
   private SpikeService spikeService;
   @Resource
   private IntegralRecordService integralRecordService;
   @Resource
   private OrdersService ordersService;
    @Resource
    private MerchantService merchantService;
    @Resource
    private ProjectService projectService;
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private CommunityClassMapper communityClassMapper;
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private ReportCadreMapper reportCadreMapper;
    @Resource
    private UnitMapper unitMapper;
    @Resource
    private CommunityClassService communityClassService;
    @Resource
    private UnitService unitService;
    /**
     * ????????????????????????
     * @param aid  ??????id
     * @return
     */
    @Transactional
    public IntegralRecordEntity getIntegralRecordEntity(Long aid,Long userId){
        //????????????????????????
        ActivityEntity activityEntity = activityService.queryById(aid);
        AllUserEntity allUserEntity = allUserService.queryById(userId);
        allUserEntity.setIntegral(allUserEntity.getIntegral()+activityEntity.getIntegral());
        allUserEntity.setRemainingPoints(allUserEntity.getRemainingPoints()+activityEntity.getIntegral());
        allUserService.update(allUserEntity);
      IntegralRecordEntity integralRecordEntity = new IntegralRecordEntity();
      integralRecordEntity.setUserId(userId);
      integralRecordEntity.setIntegral(activityEntity.getIntegral());
      integralRecordEntity.setSource("????????????");
      integralRecordEntity.setType(TypeEnum.TYPE_ENUM_ONE.getValue());
        IntegralRecordEntity integralRecordEntity1 = super.update(integralRecordEntity);
        return integralRecordEntity1;
    }

    /**
     * ??????????????????
     * @param pid  ??????id
     * @return
     */
    @Transactional
    public IntegralRecordEntity getIntegralRecordEntity1(Long pid,Long userId){
        //????????????????????????
        ProjectEntity projectEntity = projectService.queryById(pid);
        AllUserEntity allUserEntity = allUserService.queryById(userId);
        allUserEntity.setIntegral(allUserEntity.getIntegral()+projectEntity.getIntegral());
        allUserEntity.setRemainingPoints(allUserEntity.getRemainingPoints()+projectEntity.getIntegral());
        allUserService.update(allUserEntity);
        IntegralRecordEntity integralRecordEntity = new IntegralRecordEntity();
        integralRecordEntity.setUserId(userId);
        integralRecordEntity.setIntegral(projectEntity.getIntegral());
        integralRecordEntity.setSource("????????????");
        integralRecordEntity.setType(TypeEnum.TYPE_ENUM_ONE.getValue());
        IntegralRecordEntity integralRecordEntity1 = super.update(integralRecordEntity);
        return integralRecordEntity1;
    }

    /**
     * ??????????????????
     * @param pid ??????id
     * @param userId  ??????id
     * @return
     */
    @Transactional
    public String getIntegralRecordEntityBySpike(Long pid,Long userId){


        String msg = null;
        ProductEntity productEntity = productService.queryById(pid);
        AllUserEntity allUserEntity =   allUserService.queryById(userId);
        if(allUserEntity.getType() == 1){ //?????????
            if(productEntity.getRemainingAmount()>productEntity.getRedeemedAmpunt()){
                if(allUserEntity.getRemainingPoints() >= productEntity.getIntegral()){
                    //??????????????????
                    allUserEntity.setRemainingPoints(allUserEntity.getRemainingPoints()-productEntity.getIntegral());
                    allUserEntity.setPointsConsumption(allUserEntity.getPointsConsumption()+Integer.parseInt(productEntity.getIntegral().toString()));
                    allUserService.update(allUserEntity);
                    //???????????????????????????
                    productEntity.setRedeemedAmpunt(productEntity.getRedeemedAmpunt()+1);
                    productService.update(productEntity);
                    //??????????????????
                    IntegralRecordEntity integralRecordEntity  = new IntegralRecordEntity();
                    integralRecordEntity.setType(TypeEnum.TYPE_ENUM_TWO.getValue());
                    integralRecordEntity.setUserId(userId);
                    integralRecordEntity.setSource("????????????");
                    integralRecordEntity.setSort(1); //1.????????????????????????
                    integralRecordEntity.setPid(pid);
                    integralRecordEntity.setIntegral(Integer.parseInt(productEntity.getIntegral().toString()));
                    integralRecordService.update(integralRecordEntity);
                    //??????????????????
                    OrdersEntity ordersEntity = new OrdersEntity();
                    ordersEntity.setUserId(userId);
                    ordersEntity.setProductId(pid);
                    ordersEntity.setNumber(1);
                    ordersEntity.setStatus(StatusOrderEnum.STATUS_ORDER_ENUM_ONE.getValue());
                    ordersService.update(ordersEntity);
                }else {
                    int integral = (productEntity.getIntegral()) ;
                    msg = "?????????????????????????????????????????????(??????"+integral+"?????????????????????";
                    return msg;
                }
            }else {
                msg = "??????????????????????????????????????????";
                return msg;
            }

        }else if(allUserEntity.getType() == 2){//????????????
            if(productEntity.getRemainingAmount()>productEntity.getRedeemedAmpunt()){
                if(allUserEntity.getRemainingPoints()-10 >= productEntity.getIntegral()){
                    //??????????????????
                    allUserEntity.setRemainingPoints(allUserEntity.getRemainingPoints()-productEntity.getIntegral());
                    allUserEntity.setPointsConsumption(allUserEntity.getPointsConsumption()+Integer.parseInt(productEntity.getIntegral().toString()));
                    allUserService.update(allUserEntity);
                    //???????????????????????????
                    productEntity.setRedeemedAmpunt(productEntity.getRedeemedAmpunt()+1);
                    productService.update(productEntity);
                    //??????????????????
                    IntegralRecordEntity integralRecordEntity  = new IntegralRecordEntity();
                    integralRecordEntity.setType(TypeEnum.TYPE_ENUM_TWO.getValue());
                    integralRecordEntity.setUserId(userId);
                    integralRecordEntity.setSource("????????????");
                    integralRecordEntity.setSort(1); //1.????????????????????????
                    integralRecordEntity.setPid(pid);
                    integralRecordEntity.setIntegral(Integer.parseInt(productEntity.getIntegral().toString()));
                    integralRecordService.update(integralRecordEntity);
                    //??????????????????
                    OrdersEntity ordersEntity = new OrdersEntity();
                    ordersEntity.setUserId(userId);
                    ordersEntity.setProductId(pid);
                    ordersEntity.setNumber(1);
                    ordersEntity.setStatus(StatusOrderEnum.STATUS_ORDER_ENUM_ONE.getValue());
                    ordersService.update(ordersEntity);
                }else {
                    int integral = (productEntity.getIntegral()+10) ;
                    msg = "?????????????????????????????????????????????(??????"+integral+"?????????????????????";
                    return msg;
                }
            }else {
                msg = "??????????????????????????????????????????";
                return msg;
            }
        }else if(allUserEntity.getType() == 3){//??????
            msg = "?????????????????????????????????????????????";
            return msg;
        }else {
            msg = "?????????????????????????????????";
            return msg;
        }
        return msg;


//        String msg;
//        //????????????????????????
//       ProductEntity productEntity = productService.queryById(pid);
//       AllUserEntity allUserEntity = allUserService.queryById(userId);
//       SpVo spvo =  spikeService.getSpikeEntityByPid(pid);
//       if(spvo.getType().equals(TypeEnum1.TYPE_ENUM_ONE.getValue())){
//           if(spvo.getNumber() > spvo.getRedeemedAmpunt()){
//               if(allUserEntity.getRemainingPoints()-10 >= spvo.getPrice()){
//                   //??????????????????
//                   allUserEntity.setRemainingPoints(allUserEntity.getRemainingPoints()-spvo.getPrice());
//                   allUserEntity.setPointsConsumption(allUserEntity.getPointsConsumption()+Integer.parseInt(spvo.getPrice().toString()));
//                   allUserService.update(allUserEntity);
//                   //???????????????????????????
//                   productEntity.setRedeemedAmpunt(productEntity.getRedeemedAmpunt()+1);
//                   productService.update(productEntity);
//                   //??????????????????
//                   IntegralRecordEntity integralRecordEntity  = new IntegralRecordEntity();
//                   integralRecordEntity.setType(TypeEnum.TYPE_ENUM_TWO.getValue());
//                   integralRecordEntity.setUserId(userId);
//                   integralRecordEntity.setSource("????????????");
//                   integralRecordEntity.setIntegral(Integer.parseInt(spvo.getPrice().toString()));
//                   integralRecordService.update(integralRecordEntity);
//                   //??????????????????
//                   OrdersEntity ordersEntity = new OrdersEntity();
//                   ordersEntity.setUserId(userId);
//                   ordersEntity.setProductId(pid);
//                   ordersEntity.setNumber(1);
//                   ordersEntity.setStatus(StatusOrderEnum.STATUS_ORDER_ENUM_ONE.getValue());
//                   ordersService.update(ordersEntity);
//               }else {
//                   msg = "????????????????????????";
//                   return msg;
//               }
//
//
//           }else {
//              msg = "????????????????????????";
//            return msg;
//           }
//
//       }else {
//           if(spvo.getNumber() > spvo.getRedeemedAmpunt()){
//            int count =  ordersMapper.count(pid,userId,spvo.getStartTime(),spvo.getEndTime());
//            if(spvo.getLimitNumber() > count){
//                if(allUserEntity.getRemainingPoints()-10 >= spvo.getPrice()){
//                    //??????????????????
//                    allUserEntity.setRemainingPoints(allUserEntity.getRemainingPoints()-spvo.getPrice());
//                    allUserEntity.setPointsConsumption(allUserEntity.getPointsConsumption()+Integer.parseInt(spvo.getPrice().toString()));
//                    allUserService.update(allUserEntity);
//                    //???????????????????????????
//                    SpikeEntity spikeEntity = new SpikeEntity();
//                    spikeEntity.setId(spvo.getId());
//                    spikeEntity.setRedeemedAmpunt(spvo.getRedeemedAmpunt()+1);
//                    spikeEntity.setNumberPeopleExchanged(spvo.getNumberPeopleExchanged()+1);
//                    spikeService.update(spikeEntity);
//                    //??????????????????
//                    IntegralRecordEntity integralRecordEntity  = new IntegralRecordEntity();
//                    integralRecordEntity.setType(TypeEnum.TYPE_ENUM_TWO.getValue());
//                    integralRecordEntity.setUserId(userId);
//                    integralRecordEntity.setSource("????????????");
//                    integralRecordEntity.setIntegral(Integer.parseInt(spvo.getPrice().toString()));
//                    integralRecordService.update(integralRecordEntity);
//                    //??????????????????
//                    OrdersEntity ordersEntity = new OrdersEntity();
//                    ordersEntity.setUserId(userId);
//                    ordersEntity.setProductId(pid);
//                    ordersEntity.setNumber(1);
//                    ordersEntity.setStatus(StatusOrderEnum.STATUS_ORDER_ENUM_ONE.getValue());
//                    ordersService.update(ordersEntity);
//
//                }else {
//                    msg = "????????????????????????";
//                    return msg;
//                }
//            }else {
//                msg = "?????????????????????????????????????????????";
//                return msg;
//            }
//
//
//
//           }else {
//               msg = "????????????????????????";
//               return msg;
//           }
//
//       }
//       return null;
    }


    /**
     * ??????????????????
     * @param
     * @param userId
     * @return
     */
   public Page<Map<String,Object>> getHomeList(Long userId){

       Page page = LayuiPageFactory.defaultPage();
      return super.mapper.getHomeList(page,userId);
    }

    /**
     *
     * @param mid ??????id
     * @return
     */
    @Transactional
    public String allUser(Long mid,Integer integral,Long userId){

        String msg = null;

     MerchantEntity merchantEntity = merchantService.queryById(mid);
    AllUserEntity allUserEntity1 =   allUserService.queryById(userId);
    if(merchantService.getById(mid) != null){
        if(allUserEntity1.getRemainingPoints()-10 >= integral){
            allUserEntity1.setRemainingPoints(allUserEntity1.getRemainingPoints()-integral);
            allUserEntity1.setPointsConsumption(allUserEntity1.getPointsConsumption()+integral);
            allUserService.update(allUserEntity1);
            IntegralRecordEntity integralRecordEntity1 = new IntegralRecordEntity();
            integralRecordEntity1.setIntegral(integral);
            integralRecordEntity1.setUserId(allUserEntity1.getId());
            integralRecordEntity1.setSource("????????????");
            integralRecordEntity1.setType(TypeEnum.TYPE_ENUM_TWO.getValue());
            this.update(integralRecordEntity1);
            AllUserEntity allUserEntity =  allUserService.queryById(merchantEntity.getUserId());
            Integer i = allUserEntity.getIntegral();
            if(i== null){
                i=0;
            }
            Integer j = allUserEntity.getRemainingPoints();
            if(j== null){
                j=0;
            }
            allUserEntity.setIntegral(i+integral+1);
            allUserEntity.setRemainingPoints(j+integral+1);
            allUserService.update(allUserEntity);
            IntegralRecordEntity integralRecordEntity = new IntegralRecordEntity();
            integralRecordEntity.setIntegral(integral);
            integralRecordEntity.setUserId(allUserEntity.getId());
            integralRecordEntity.setSource("????????????");
            integralRecordEntity.setMid(mid);
            integralRecordEntity.setType(TypeEnum.TYPE_ENUM_ONE.getValue());
            this.update(integralRecordEntity);
            IntegralRecordEntity integralRecordEntity2 = new IntegralRecordEntity();
            integralRecordEntity2.setIntegral(1);
            integralRecordEntity2.setUserId(allUserEntity.getId());
            integralRecordEntity2.setSource("????????????????????????");
            integralRecordEntity2.setMid(mid);
            integralRecordEntity2.setType(TypeEnum.TYPE_ENUM_ONE.getValue());
            this.update(integralRecordEntity2);
        }else {
            msg = "??????????????????";
            return msg;
        }
    }else {
        msg = "??????????????????????????????";
        return msg;
    }
     return msg;
    }



    /**

     * @param page
     * @return
     */
    public Page<Map<String,Object>> FindIntergralList(Page<Long> page){
        return super.mapper.FindIntergralList(page);
    }

    /**

     * @param page
     * @return
     */
    public Page<Map<String,Object>> FindIntergralListByUser(Page<Long> page){
        return super.mapper.FindIntergralListByUser(page);
    }

    public Map<String,Object> FindIntergralListByUserId(Long userId){
        return super.mapper.FindIntergralListByUserId(userId);
    }

    public Page<Map<String,Object>> FindIntergralListByOrganization(Page<Long> page){
        return super.mapper.FindIntergralListByOrganization(page);
    }


    public Map<String,Object> FindOrganizationIntergralListByUserId(Long id){
        return super.mapper.FindOrganizationIntergralListByUserId(id);
    }




    public Page<Map<String,Object>> FindIntergralListByCommunity(Page<Long> page){
        return super.mapper.FindIntergralListByCommunity(page);
    }


    public Map<String,Object> FindCommunityByCommunityId(Long id){
        return super.mapper.FindCommunityByCommunityId(id);
    }

    public Page<Map<String,Object>> FindIntergralAVGListByCommunity(Page<Long> page){
        return super.mapper.FindIntergralAVGListByCommunity(page);
    }


    public Map<String,Object> FindCommunityAVGByCommunityId(Long id){
        return super.mapper.FindCommunityAVGByCommunityId(id);
    }

    /**
     * ?????? ??????
     */
    public void updateIntegral(){
    List<CommunityClassEntity> communityClassEntityList =  communityClassMapper.getList();

        for (CommunityClassEntity communityClassEntity :communityClassEntityList) {
            int integral= 0;
          MemberVo memberVo = memberMapper.getIntegral(communityClassEntity.getId());
          integral = integral + memberVo.getRemainingPoints();
          ReportCadreVo reportCadreVo = reportCadreMapper.getIntegral(communityClassEntity.getId());
          integral = integral + reportCadreVo.getRemainingPoints();
          UnitListVo unitListVo = unitMapper.getIntegral(communityClassEntity.getId());
          integral = integral + unitListVo.getRemainingPoints();
         communityClassEntity.setIntegral(integral);
         communityClassService.update(communityClassEntity);
        }

    }

    /**
     * ????????????
     */
    public void updateIntegral1(){
        List<UnitEntity> unitEntityList = unitMapper.getList1();
        for (UnitEntity unitEntity : unitEntityList) {
            int integral= 0;
            MemberVo memberVo = memberMapper.getIntegral1(unitEntity.getId());
            integral = integral + memberVo.getRemainingPoints();
            ReportCadreVo reportCadreVo = reportCadreMapper.getIntegral1(unitEntity.getId());
            integral = integral + reportCadreVo.getRemainingPoints();
            UnitListVo unitListVo = unitMapper.getIntegral1(unitEntity.getUserId());
            integral = integral + unitListVo.getRemainingPoints();
            unitEntity.setIntegral(integral);
            unitService.update(unitEntity);
        }

    }

    /**
     * ???????????????????????????
     */

    public int getIntegral (Long mid){
        return super.mapper.getIntegral(mid);
    }


    /**
     * ??????????????????
     * @param userId
     * @return
     */
    public Page<Map<String,Object>> getExchangeRecord(Integer page,Integer pageSize,Long userId){
        Page page1 = new Page(page,pageSize);
        return super.mapper.getExchangeRecord(page1,userId);
    }



   public Page<Map<String,Object>> VolunteerRanking(@Param("page")Page page){
        return super.mapper.VolunteerRanking(page);
   }
   public Page<Map<String,Object>> memberRanking(@Param("page")Page page){
       return super.mapper.memberRanking(page);

   }
   public Page<Map<String,Object>> unitRanking(@Param("page")Page page){
       return super.mapper.unitRanking(page);

   }
}