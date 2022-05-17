package cn.px.sys.modular.peopleDynamic.service;

import cn.px.sys.modular.peopleDynamic.entity.PeopleDynamicTypeEntity;
import cn.px.sys.modular.peopleDynamic.mapper.PeopleDynamicTypeMapper;
import org.springframework.stereotype.Service;
import cn.px.base.core.BaseServiceImpl;
import cn.px.base.Constants;
import org.springframework.cache.annotation.CacheConfig;

/**
 * 文章类型(PeopleDynamicType)表服务实现类
 *
 * @author
 * @since 2021-05-13 16:08:06
 */
@Service("peopleDynamicTypeService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "peopleDynamicType")
public class PeopleDynamicTypeService extends BaseServiceImpl<PeopleDynamicTypeEntity, PeopleDynamicTypeMapper> {

}
