package cn.px.sys.modular.spike.service;

import cn.px.base.Constants;
import cn.px.base.core.BaseServiceImpl;
import cn.px.sys.modular.spike.entity.RuleEntity;
import cn.px.sys.modular.spike.mapper.RuleMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * (Rule)表服务实现类
 *
 * @author
 * @since 2020-11-19 09:43:41
 */
@Service("ruleService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "rule")
public class RuleService extends BaseServiceImpl<RuleEntity, RuleMapper> {

}