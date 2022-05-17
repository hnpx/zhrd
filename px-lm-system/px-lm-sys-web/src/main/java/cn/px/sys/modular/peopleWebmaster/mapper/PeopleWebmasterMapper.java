package cn.px.sys.modular.peopleWebmaster.mapper;

import cn.px.sys.modular.peopleWebmaster.entity.PeopleWebmasterEntity;
import cn.px.base.core.BaseMapperImpl;
import cn.px.sys.modular.peopleWebmaster.vo.WebmasterVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 联络站人员(PeopleWebmaster)表数据库访问层
 *
 * @author
 * @since 2021-05-13 16:10:51
 */
public interface PeopleWebmasterMapper extends BaseMapperImpl<PeopleWebmasterEntity> {

    Page<Map<String,Object>>selectPeopleWebmasterVo(@Param("page") Page page, @Param("name") String name, @Param("organization") Long organization);

    public List<WebmasterVo> getWebmasterVoList(@Param("organization") Long organization);
}
