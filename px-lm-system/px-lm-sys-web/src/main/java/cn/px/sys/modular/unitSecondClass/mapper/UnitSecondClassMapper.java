package cn.px.sys.modular.unitSecondClass.mapper;

import cn.px.base.core.BaseMapperImpl;
import cn.px.sys.modular.unitReports.entity.UnitReportsEntity;
import cn.px.sys.modular.unitSecondClass.entity.UnitSecondClassEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

/**
 * (UnitSecondClass)表数据库访问层
 *
 * @author
 * @since 2020-10-12 11:56:44
 */
public interface UnitSecondClassMapper extends BaseMapperImpl<UnitSecondClassEntity> {

    Page<Map<String,Object>> selectList(@Param("page")Page page,@Param("name") String name);

    List<Map<String,Object>>selectListByid(@Param("id") Long id);

    List<UnitSecondClassEntity> getList();

    List<UnitSecondClassEntity> getListByCid(@Param("cid") Long cid);

    List<UnitSecondClassEntity> getListByCid1(@Param("cid") Long cid);


    UnitSecondClassEntity getUnitById(@Param("id") Long id);

}