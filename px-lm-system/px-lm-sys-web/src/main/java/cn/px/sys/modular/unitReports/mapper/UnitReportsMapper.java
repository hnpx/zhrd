package cn.px.sys.modular.unitReports.mapper;

import cn.px.base.core.BaseMapperImpl;
import cn.px.sys.modular.unitReports.entity.UnitReportsEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

/**
 * 单位报到(UnitReports)表数据库访问层
 *
 * @author
 * @since 2020-10-12 15:47:04
 */
public interface UnitReportsMapper extends BaseMapperImpl<UnitReportsEntity> {


    Page<Map<String,Object>> selectListPage(@Param("page")Page page,@Param("entity")UnitReportsEntity entity);

    Integer getCount();

    Page<Map<String,Object>> getAllById (@Param("page")Page page);

    UnitReportsEntity getEntityByUser(@Param("userId") Long userId);

    int  getCountByall();






}