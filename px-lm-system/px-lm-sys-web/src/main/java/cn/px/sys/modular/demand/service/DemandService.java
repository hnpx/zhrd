package cn.px.sys.modular.demand.service;

import cn.px.base.Constants;
import cn.px.base.core.BaseServiceImpl;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.sys.modular.cadresReports.entity.CadresReportsEntity;
import cn.px.sys.modular.cadresReports.service.CadresReportsService;
import cn.px.sys.modular.communityClass.entity.CommunityClassEntity;
import cn.px.sys.modular.communityClass.service.CommunityClassService;
import cn.px.sys.modular.demand.entity.DemandEntity;
import cn.px.sys.modular.demand.mapper.DemandMapper;
import cn.px.sys.modular.unitReports.entity.UnitReportsEntity;
import cn.px.sys.modular.unitReports.service.UnitReportsService;
import cn.px.sys.modular.volunteersReports.entity.VolunteersReportsEntity;
import cn.px.sys.modular.volunteersReports.service.VolunteersReportsService;
import cn.px.sys.modular.wx.entity.AllUserEntity;
import cn.px.sys.modular.wx.service.AllUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 需求管理(Demand)表服务实现类
 *
 * @author
 * @since 2020-09-02 19:38:12
 */
@Service("demandService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "demand")
public class DemandService extends BaseServiceImpl<DemandEntity, DemandMapper> {

    @Resource
    private CommunityClassService communityClassService;
    @Resource
    private AllUserService allUserService;
    @Resource
    private VolunteersReportsService volunteersReportsService;
    @Resource
    private CadresReportsService cadresReportsService;
    @Resource
    private UnitReportsService unitReportsService;
    /**
     * 需求查询列表
     * @param
     * @param name
     * @param status
     * @return
     */
   public Page<Map<String,Object>> getListPage( String name, Integer status,Long userId,String phone,Long demandClass){
        Page page = LayuiPageFactory.defaultPage();
       Long cid;
       if(userId == 1){
           cid = null;
       }else {
           CommunityClassEntity communityClassEntity = communityClassService.getCommunityClassEntityByUserId(userId);
           cid =  communityClassEntity.getId();

       }
        return super.mapper.getListPage(page,name,status,cid,phone,demandClass);

    }


   public Page<Map<String,Object>> getListByUser( Integer page,Integer pageSize){
       Page page1 = new Page (page,pageSize);
       return super.mapper.getListByUser(page1);

    }


    public Map<String,Object> get(Long userId){
       Map<String,Object> map = new HashMap<>();
      AllUserEntity allUserEntity =  allUserService.queryById(userId);
      if(allUserEntity.getType() == 1){
         VolunteersReportsEntity volunteersReportsEntity = volunteersReportsService.getEntityByUser(userId);
          map.put("cid",volunteersReportsEntity.getCommunityClass());
      }else if(allUserEntity.getType() == 2){
       CadresReportsEntity cadresReportsEntity = cadresReportsService.getEntityByUser(userId);
          map.put("cid",cadresReportsEntity.getCommunityClass());
      }else if(allUserEntity.getType() == 3){
       UnitReportsEntity unitReportsEntity = unitReportsService.getEntityByUser(userId);

          map.put("cid",unitReportsEntity.getCommunityClass());
      }
    return map;

    }



}