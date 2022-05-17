package cn.px.sys.modular.peopleOrganization.mapper;

import cn.px.sys.modular.peopleOrganization.entity.PeopleOrganizationEntity;
import cn.px.base.core.BaseMapperImpl;
import cn.px.sys.modular.peopleOrganization.vo.PeopleOrganizationTree;
import cn.px.sys.modular.peopleOrganization.vo.PeopleOrganizationVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 组织架构(PeopleOrganization)表数据库访问层
 *
 * @author
 * @since 2021-05-13 16:10:10
 */
public interface PeopleOrganizationMapper extends BaseMapperImpl<PeopleOrganizationEntity> {

    public List<PeopleOrganizationTree> getList(@Param("pid") Long pid);
    public List<PeopleOrganizationVo> getList1(@Param("pid") Long pid);

    public Page<Map<String,Object>> getLiatPage(@Param("page") Page page);
}
