package cn.px.sys.modular.cadresReports.service;

import cn.px.base.Constants;
import cn.px.base.core.BaseServiceImpl;
import cn.px.sys.modular.cadresReports.entity.CadresReportsEntity;
import cn.px.sys.modular.cadresReports.mapper.CadresReportsMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 党员干部报到(CadresReports)表服务实现类
 *
 * @author
 * @since 2020-10-12 15:45:53
 */
@Service("cadresReportsService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "cadresReports")
public class CadresReportsService extends BaseServiceImpl<CadresReportsEntity, CadresReportsMapper> {

   public Page<Map<String,Object>> selectListPage(Page page,CadresReportsEntity entity){
        return mapper.selectListPage(page,entity);
    }


   public Integer getCount(){
       return mapper.getCount();
    }


   public Page<Map<String,Object>> getAllById(Page page){
       return mapper.getAllById(page);
    }


    public Integer isBrigadeClass(Long uid,Long bid){
       return mapper.isBrigadeClass(uid,bid);
    }

  public   CadresReportsEntity getEntityByUser( Long userId){
       return super.mapper.getEntityByUser(userId);
    }
    public  int getCountByall(){
       return super.mapper.getCountByall();
    }

}