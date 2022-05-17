package cn.px.sys.modular.gridClass.mapper;

import cn.px.base.core.BaseMapperImpl;
import cn.px.sys.modular.gridClass.entity.GridClassEntity;

import java.util.List;
import java.util.Map;

/**
 * (GridClass)表数据库访问层
 *
 * @author
 * @since 2020-10-12 09:40:13
 */
public interface GridClassMapper extends BaseMapperImpl<GridClassEntity> {

    List<Map<String,Object>> selectList();

}