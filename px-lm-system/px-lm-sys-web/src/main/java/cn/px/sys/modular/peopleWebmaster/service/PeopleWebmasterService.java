package cn.px.sys.modular.peopleWebmaster.service;

import cn.px.sys.modular.peopleWebmaster.entity.PeopleWebmasterEntity;
import cn.px.sys.modular.peopleWebmaster.mapper.PeopleWebmasterMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import cn.px.base.core.BaseServiceImpl;
import cn.px.base.Constants;
import org.springframework.cache.annotation.CacheConfig;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 联络站人员(PeopleWebmaster)表服务实现类
 *
 * @author
 * @since 2021-05-13 16:10:40
 */
@Service("peopleWebmasterService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "peopleWebmaster")
public class PeopleWebmasterService extends BaseServiceImpl<PeopleWebmasterEntity, PeopleWebmasterMapper> {
    @Resource
    private PeopleWebmasterMapper peopleWebmasterMapper;
   public Page<Map<String,Object>> selectPeopleWebmasterVo( Page page,  String name,  Long organization){
        return super.mapper.selectPeopleWebmasterVo(page,name,organization);
    }
}
