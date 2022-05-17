package cn.px.sys.modular.unitFirstClass.service;

import cn.px.base.Constants;
import cn.px.base.core.BaseServiceImpl;
import cn.px.sys.modular.unitFirstClass.entity.UnitFirstClassEntity;
import cn.px.sys.modular.unitFirstClass.mapper.UnitFirstClassMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * (UnitFirstClass)表服务实现类
 *
 * @author
 * @since 2020-10-12 11:55:25
 */
@Service("unitFirstClassService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "unitFirstClass")
public class UnitFirstClassService extends BaseServiceImpl<UnitFirstClassEntity, UnitFirstClassMapper> {


    public List<Map<String,Object>> selectList(){
        return mapper.selectList();
    }
}