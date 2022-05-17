package cn.px.sys.modular.volunteersClass.mapper;

import cn.px.base.core.BaseMapperImpl;
import cn.px.sys.modular.volunteersClass.entity.VolunteersClassEntity;

import java.util.List;
import java.util.Map;

/**
 * (VolunteersClass)表数据库访问层
 *
 * @author
 * @since 2020-10-12 09:37:36
 */
public interface VolunteersClassMapper extends BaseMapperImpl<VolunteersClassEntity> {

    List<Map<String,Object>> selectList();
}