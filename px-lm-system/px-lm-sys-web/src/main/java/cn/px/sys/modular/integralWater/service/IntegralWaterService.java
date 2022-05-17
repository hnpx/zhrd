package cn.px.sys.modular.integralWater.service;

import cn.px.base.Constants;
import cn.px.base.core.BaseServiceImpl;
import cn.px.sys.modular.integralWater.entity.IntegralWaterEntity;
import cn.px.sys.modular.integralWater.mapper.IntegralWaterMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * (IntegralWater)表服务实现类
 *
 * @author
 * @since 2020-09-28 11:14:04
 */
@Service("integralWaterService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "integralWater")
public class IntegralWaterService extends BaseServiceImpl<IntegralWaterEntity, IntegralWaterMapper> {
   public Page<Map<String,Object>> getlist(Page page,Long id){
        return mapper.getlist(page,id);
    }



    /**
     * 表现积分
     * @param userId
     * @return
     */
    public int showIntegral(Long userId)
    {
        return super.mapper.showIntegral(userId);
    }
}