package cn.px.sys.modular.peopleDynamic.mapper;

import cn.px.sys.modular.peopleDynamic.entity.PeopleDynamicEntity;
import cn.px.base.core.BaseMapperImpl;
import cn.px.sys.modular.peopleDynamic.vo.PeopleDynamicVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 文章内容(PeopleDynamic)表数据库访问层
 *
 * @author
 * @since 2021-05-13 16:07:30
 */
public interface PeopleDynamicMapper extends BaseMapperImpl<PeopleDynamicEntity> {
    Page<Map<String,Object>> selectPeopleDynamicList(@Param("page") Page page, @Param("dynamicType") Long dynamicType,
                                                     @Param("name") String name,@Param("uid") Long uid);
    /**
     * 查询关联代表下的文章
     */
    public List<PeopleDynamicEntity> getList(@Param("type") Integer type, @Param("pageSize") Integer pageSize,@Param("uid") Long uid);


    /**
     * 查询关联代表下的文章数
     */
    public int getCount(@Param("type") Integer type,@Param("uid") Long uid);




}
