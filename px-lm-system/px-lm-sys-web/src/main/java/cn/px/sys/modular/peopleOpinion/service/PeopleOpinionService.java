package cn.px.sys.modular.peopleOpinion.service;

import cn.px.sys.modular.peopleOpinion.entity.PeopleOpinionEntity;
import cn.px.sys.modular.peopleOpinion.mapper.PeopleOpinionMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import cn.px.base.core.BaseServiceImpl;
import cn.px.base.Constants;
import org.springframework.cache.annotation.CacheConfig;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * 征集意见，优化营商(PeopleOpinion)表服务实现类
 *
 * @author
 * @since 2021-05-13 16:09:23
 */
@Service("peopleOpinionService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "peopleOpinion")
public class PeopleOpinionService extends BaseServiceImpl<PeopleOpinionEntity, PeopleOpinionMapper> {

    @Resource
    private PeopleOpinionMapper peopleOpinionMapper;
    public Page<Map<String,Object>> selectPeopleOpinionVo( Page page,  Integer type,
                                                          Integer status, Date startTime,  Date endTime){
        return super.mapper.selectPeopleOpinionVo(page,type,status,startTime,endTime);
    }

    public int getCountByall(){
        return super.mapper.getCountByall();
    }


    public Page<Map<String, Object>> getByStatus(Page page){

        return super.mapper.getByStatus(page);
    }

}
