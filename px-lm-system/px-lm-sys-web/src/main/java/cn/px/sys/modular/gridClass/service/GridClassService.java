package cn.px.sys.modular.gridClass.service;

import cn.px.base.Constants;
import cn.px.base.core.BaseServiceImpl;
import cn.px.sys.modular.gridClass.entity.GridClassEntity;
import cn.px.sys.modular.gridClass.mapper.GridClassMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * (GridClass)表服务实现类
 *
 * @author
 * @since 2020-10-12 09:40:00
 */
@Service("gridClassService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "gridClass")
public class GridClassService extends BaseServiceImpl<GridClassEntity, GridClassMapper> {

    public List<Map<String,Object>> selectList(){
        return mapper.selectList();
    }

}