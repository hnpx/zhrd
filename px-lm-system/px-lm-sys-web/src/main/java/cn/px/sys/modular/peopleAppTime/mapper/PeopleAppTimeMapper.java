package cn.px.sys.modular.peopleAppTime.mapper;

import cn.px.sys.modular.peopleAppTime.entity.PeopleAppTimeEntity;
import cn.px.base.core.BaseMapperImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 人大预约时间(PeopleAppTime)表数据库访问层
 *
 * @author
 * @since 2021-05-13 16:04:55
 */
public interface PeopleAppTimeMapper extends BaseMapperImpl<PeopleAppTimeEntity> {

    PeopleAppTimeEntity getToday(@Param("startTime") Date startTime,@Param("endTime") Date endTime);

    PeopleAppTimeEntity getNext(@Param("startTime") Date startTime);

    Page<Map<String,Object>> getRecord(@Param("page") Page page,@Param("startTime") Date startTime);

    List<PeopleAppTimeEntity> getMonth(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    int getCountByappTime(@Param("date") Date date);


    PeopleAppTimeEntity getObjectByappTime(@Param("date") Date date);

    List<PeopleAppTimeEntity> getListByCongress(@Param("congress") Long congress,@Param("date") Date date);

}
