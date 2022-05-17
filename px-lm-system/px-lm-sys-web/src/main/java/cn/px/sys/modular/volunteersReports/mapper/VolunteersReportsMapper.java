package cn.px.sys.modular.volunteersReports.mapper;

import cn.px.base.core.BaseMapperImpl;
import cn.px.sys.modular.unitReports.entity.UnitReportsEntity;
import cn.px.sys.modular.volunteersReports.entity.VolunteersReportsEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;


import java.util.List;
import java.util.Map;

/**
 * 志愿者报到(VolunteersReports)表数据库访问层
 *
 * @author
 * @since 2020-10-12 15:18:50
 */
public interface VolunteersReportsMapper extends BaseMapperImpl<VolunteersReportsEntity> {
    Page<Map<String,Object>> selectListPage(@Param("page")Page page,@Param("entity") VolunteersReportsEntity entity);

    Integer getCount(@Param("id")Long id);

    Page<Map<String,Object>> getAllById (@Param("page")Page page,@Param("id")Long id);


    Integer isBrigadeClass(@Param("uid")Long uid,@Param("bid")Long bid);

    VolunteersReportsEntity getEntityByUser(@Param("userId") Long userId);

}