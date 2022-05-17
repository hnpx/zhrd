package cn.px.sys.modular.peopleCrowd.service;

import cn.px.sys.modular.peopleCrowd.entity.PeopleCrowdEntity;
import cn.px.sys.modular.peopleCrowd.mapper.PeopleCrowdMapper;
import org.springframework.stereotype.Service;
import cn.px.base.core.BaseServiceImpl;
import cn.px.base.Constants;
import org.springframework.cache.annotation.CacheConfig;

/**
 * 群众表(PeopleCrowd)表服务实现类
 *
 * @author
 * @since 2021-05-13 16:06:30
 */
@Service("peopleCrowdService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "peopleCrowd")
public class PeopleCrowdService extends BaseServiceImpl<PeopleCrowdEntity, PeopleCrowdMapper> {

}
