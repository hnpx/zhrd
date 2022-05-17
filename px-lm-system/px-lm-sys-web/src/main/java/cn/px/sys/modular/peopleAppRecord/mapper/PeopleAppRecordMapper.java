package cn.px.sys.modular.peopleAppRecord.mapper;

import cn.px.sys.modular.peopleAppRecord.entity.PeopleAppRecordEntity;
import cn.px.base.core.BaseMapperImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 群众预约记录(PeopleAppRecord)表数据库访问层
 *
 * @author
 * @since 2021-05-13 15:55:28
 */
public interface PeopleAppRecordMapper extends BaseMapperImpl<PeopleAppRecordEntity> {

    /**
     *
     * @param page
     * @param type (1.未开始2.已结束3.已拒绝)
     * @return
     */
    Page<PeopleAppRecordEntity> getAppRecordList(@Param("page") Page page,@Param("type") Integer type,
                                                 @Param("crowdId") Long crowdId,@Param("congressId") Long congressId,@Param("date") Date date );

    List<PeopleAppRecordEntity> getAppNotice(@Param("crowdId") Long crowdId,@Param("congressId") Long congressId);
    int getAppNoticeNum(@Param("crowdId") Long crowdId, @Param("congressId") Long congressId);

    /**
     *
     * @param page
     * @return
     */
    Page<PeopleAppRecordEntity> getAppRecordListBystatus(@Param("page") Page page);

    int getCount( @Param("congressId") Long congressId, @Param("status") Integer status);

    Page<Map<String, Object>> getStatusList(@Param("page") Page page,@Param("congressId") Long congressId,@Param("status") Integer status);

    PeopleAppRecordEntity getAppByRoomNumber(@Param("roomNumber") String roomNumber);

}
