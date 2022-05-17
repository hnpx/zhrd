package cn.px.sys.modular.visitCount.service;

import cn.px.base.Constants;
import cn.px.base.core.BaseServiceImpl;
import cn.px.sys.modular.visitCount.entity.VisitCountEntity;
import cn.px.sys.modular.visitCount.mapper.VisitCountMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * (VisitCount)表服务实现类
 *
 * @author
 * @since 2020-10-19 14:16:54
 */
@Service("visitCountService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "visitCount")
public class VisitCountService extends BaseServiceImpl<VisitCountEntity, VisitCountMapper> {

}