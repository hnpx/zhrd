package cn.px.sys.modular.unitReports.service;

import cn.px.base.Constants;
import cn.px.base.core.BaseServiceImpl;
import cn.px.sys.modular.unitReports.entity.UnitReportsEntity;
import cn.px.sys.modular.unitReports.mapper.UnitReportsMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 单位报到(UnitReports)表服务实现类
 *
 * @author
 * @since 2020-10-12 15:46:53
 */
@Service("unitReportsService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "unitReports")
public class UnitReportsService extends BaseServiceImpl<UnitReportsEntity, UnitReportsMapper> {

    public Page<Map<String,Object>> selectListPage(Page page ,UnitReportsEntity entity){
        return mapper.selectListPage(page,entity);
    }

    public Integer getCount(){
        return mapper.getCount();
    }

    public Page<Map<String,Object>> getAllById (Page page){
     return mapper.getAllById(page);
    }

   public UnitReportsEntity getEntityByUser(Long userId){
        return super.mapper.getEntityByUser(userId);
    }
   public int  getCountByall(){
     return  super.mapper.getCountByall();
   }

}