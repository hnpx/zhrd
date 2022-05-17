package cn.px.sys.modular.peopleOpinion.mapper;

import cn.px.sys.modular.peopleOpinion.entity.PeopleOpinionEntity;
import cn.px.base.core.BaseMapperImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;

/**
 * 征集意见，优化营商(PeopleOpinion)表数据库访问层
 *
 * @author
 * @since 2021-05-13 16:09:34
 */
public interface PeopleOpinionMapper extends BaseMapperImpl<PeopleOpinionEntity> {

    public Page<Map<String,Object>> selectPeopleOpinionVo(@Param("page") Page page, @Param("type") Integer type,
                                                          @Param("status") Integer status, @Param("startTime")Date startTime,@Param("endTime") Date endTime);

    public int getCountByall();

    public Page<Map<String, Object>> getByStatus(@Param("page") Page page);

}
