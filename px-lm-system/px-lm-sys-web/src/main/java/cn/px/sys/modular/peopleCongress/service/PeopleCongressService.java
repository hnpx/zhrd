package cn.px.sys.modular.peopleCongress.service;

import cn.px.sys.modular.peopleCongress.entity.PeopleCongressEntity;
import cn.px.sys.modular.peopleCongress.mapper.PeopleCongressMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import cn.px.base.core.BaseServiceImpl;
import cn.px.base.Constants;
import org.springframework.cache.annotation.CacheConfig;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 人大代表(PeopleCongress)表服务实现类
 *
 * @author
 * @since 2021-05-13 16:05:35
 */
@Service("peopleCongressService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "peopleCongress")
public class PeopleCongressService extends BaseServiceImpl<PeopleCongressEntity, PeopleCongressMapper> {

    @Resource
    private PeopleCongressMapper peopleCongressMapper;
    public Page<PeopleCongressEntity> getPeopleCongressList(Page page){

       return peopleCongressMapper.getPeopleCongressList(page);
    }


   public Page<Map<String,Object>> selectPeopleCongressPageList(Page page,String name,Long organization){
        return peopleCongressMapper.selectPeopleCongressPageList(page,name,organization);
    }

   public int getCountByall(){
        return super.mapper.getCountByall();
    }


}
