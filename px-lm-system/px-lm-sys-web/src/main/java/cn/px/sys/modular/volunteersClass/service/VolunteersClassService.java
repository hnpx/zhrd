package cn.px.sys.modular.volunteersClass.service;

import cn.px.base.Constants;
import cn.px.base.core.BaseServiceImpl;
import cn.px.sys.modular.volunteersClass.entity.VolunteersClassEntity;
import cn.px.sys.modular.volunteersClass.mapper.VolunteersClassMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * (VolunteersClass)表服务实现类
 *
 * @author
 * @since 2020-10-12 09:37:51
 */
@Service("volunteersClassService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "volunteersClass")
public class VolunteersClassService extends BaseServiceImpl<VolunteersClassEntity, VolunteersClassMapper> {


   public List<Map<String,Object>> selectList(){
        return mapper.selectList();
    }
}