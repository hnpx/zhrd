package cn.px.sys.modular.volunteersReports.service;

import cn.px.base.Constants;
import cn.px.base.core.BaseServiceImpl;
import cn.px.sys.modular.volunteersReports.entity.VolunteersReportsEntity;
import cn.px.sys.modular.volunteersReports.mapper.VolunteersReportsMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 志愿者报到(VolunteersReports)表服务实现类
 *
 * @author
 * @since 2020-10-12 15:18:36
 */
@Service("volunteersReportsService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "volunteersReports")
public class VolunteersReportsService extends BaseServiceImpl<VolunteersReportsEntity, VolunteersReportsMapper> {


   public Page<Map<String,Object>> selectListPage(Page page,VolunteersReportsEntity entity){
     return mapper.selectListPage(page,entity);
    }

   public Integer getCount(Long id){
       return mapper.getCount(id);
    }

   public Page<Map<String,Object>> getAllById (Page page,Long id){
       return mapper.getAllById(page,id);
    }

    public Integer isBrigadeClass(Long uid,Long bid){
       return mapper.isBrigadeClass(uid,bid);
    }


   public VolunteersReportsEntity getEntityByUser( Long userId){

       return super.mapper.getEntityByUser(userId);
    }
}
