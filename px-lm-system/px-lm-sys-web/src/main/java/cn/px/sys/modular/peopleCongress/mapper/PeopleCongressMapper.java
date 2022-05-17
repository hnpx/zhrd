package cn.px.sys.modular.peopleCongress.mapper;

import cn.px.sys.modular.peopleCongress.entity.PeopleCongressEntity;
import cn.px.base.core.BaseMapperImpl;
import cn.px.sys.modular.peopleCongress.vo.PeopleMermerVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 人大代表(PeopleCongress)表数据库访问层
 *
 * @author
 * @since 2021-05-13 16:05:52
 */
public interface PeopleCongressMapper extends BaseMapperImpl<PeopleCongressEntity> {
    public Page<PeopleCongressEntity> getPeopleCongressList(@Param("page") Page page);

    Page<Map<String,Object>> selectPeopleCongressPageList(@Param("page")Page page, @Param("name") String name, @Param("organization") Long organization);
     List<PeopleMermerVo> getPeopleMermerVo(@Param("organization") Long organization);

     int getCountByall();
}
