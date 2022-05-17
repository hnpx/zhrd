package cn.px.sys.modular.peopleAppTime.service;

import cn.hutool.core.date.DateUtil;
import cn.px.sys.modular.peopleAppTime.entity.PeopleAppTimeEntity;
import cn.px.sys.modular.peopleAppTime.mapper.PeopleAppTimeMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import cn.px.base.core.BaseServiceImpl;
import cn.px.base.Constants;
import org.springframework.cache.annotation.CacheConfig;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 人大预约时间(PeopleAppTime)表服务实现类
 *
 * @author
 * @since 2021-05-13 16:04:39
 */
@Service("peopleAppTimeService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "peopleAppTime")
public class PeopleAppTimeService extends BaseServiceImpl<PeopleAppTimeEntity, PeopleAppTimeMapper> {

   public PeopleAppTimeEntity getToday(){
        return super.mapper.getToday(DateUtil.beginOfDay(new Date()),DateUtil.endOfDay(new Date()));
    }

   public PeopleAppTimeEntity getNext(){
        return super.mapper.getNext(DateUtil.endOfDay(new Date()));
    }

   public Page<Map<String,Object>> getRecord(Page page){
       return super.mapper.getRecord(page,DateUtil.beginOfDay(new Date()));
    }

    public List<PeopleAppTimeEntity> getMonth(Date date){
        return super.mapper.getMonth(DateUtil.beginOfMonth(date),DateUtil.endOfMonth(date));
    }

    public int getCountByappTime( Date date){
       return super.mapper.getCountByappTime(date);
    }

   public PeopleAppTimeEntity getObjectByappTime(Date date){
       return super.mapper.getObjectByappTime(date);
    }

   public List<PeopleAppTimeEntity> getListByCongress( Long congress){
        Date date =  DateUtil.endOfDay(DateUtil.yesterday());
       return super.mapper.getListByCongress(congress,date);
   }
}
