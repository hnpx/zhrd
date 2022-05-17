package cn.px.sys.modular.peopleDynamic.service;

import cn.px.sys.modular.peopleDynamic.entity.PeopleDynamicEntity;
import cn.px.sys.modular.peopleDynamic.mapper.PeopleDynamicMapper;
import cn.px.sys.modular.peopleDynamic.vo.PeopleDynamicVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import cn.px.base.core.BaseServiceImpl;
import cn.px.base.Constants;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;
import java.util.Map;

/**
 * 文章内容(PeopleDynamic)表服务实现类
 *
 * @author
 * @since 2021-05-13 16:07:16
 */
@Service("peopleDynamicService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "peopleDynamic")
public class PeopleDynamicService extends BaseServiceImpl<PeopleDynamicEntity, PeopleDynamicMapper> {

   public Page<Map<String,Object>> selectPeopleDynamicList( Page page,  Long dynamicType,  String name,Long uid){
        return super.mapper.selectPeopleDynamicList(page,dynamicType,name,uid);

    }

    /**
     * 查询关联代表下的文章
     */
    public List<PeopleDynamicEntity> getList(Integer type, Integer pageSize,Long uid){

        return super.mapper.getList(type,pageSize,uid);

    }

    /**
     * 查询关联代表下的文章数
     */
    public int getCount( Integer type,Long uid){
        return super.mapper.getCount(type,uid);
    }
}
