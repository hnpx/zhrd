package cn.px.sys.modular.brigadeClass.service;

import cn.px.base.Constants;
import cn.px.base.core.BaseServiceImpl;
import cn.px.sys.modular.brigadeClass.entity.BrigadeClassEntity;
import cn.px.sys.modular.brigadeClass.mapper.BrigadeClassMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * (BrigadeClass)表服务实现类
 *
 * @author
 * @since 2020-10-12 09:38:48
 */
@Service("brigadeClassService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "brigadeClass")
public class BrigadeClassService extends BaseServiceImpl<BrigadeClassEntity, BrigadeClassMapper> {


    public List<Map<String,Object>> selectList(){
        return mapper.selectList();
    }


    public String getNameByList(List idlist){
        return mapper.getNameByList(idlist);
    }


    public Page<Map<String,Object>> selectPage(Page page,BrigadeClassEntity entity){
        return mapper.selectPage(page,entity);
    }

    public Integer selectCount( BrigadeClassEntity entity){
        return mapper.selectCount(entity);
    }
}