package cn.px.sys.modular.unitFirstClass.mapper;

import cn.px.base.core.BaseMapperImpl;
import cn.px.sys.modular.unitFirstClass.entity.UnitFirstClassEntity;

import java.util.List;
import java.util.Map;

/**
 * (UnitFirstClass)表数据库访问层
 *
 * @author
 * @since 2020-10-12 11:55:39
 */
public interface UnitFirstClassMapper extends BaseMapperImpl<UnitFirstClassEntity> {

    List<Map<String,Object>> selectList();

}