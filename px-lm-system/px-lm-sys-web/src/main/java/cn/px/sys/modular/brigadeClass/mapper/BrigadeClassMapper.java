package cn.px.sys.modular.brigadeClass.mapper;

import cn.px.base.core.BaseMapperImpl;
import cn.px.sys.modular.brigadeClass.entity.BrigadeClassEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * (BrigadeClass)表数据库访问层
 *
 * @author
 * @since 2020-10-12 09:39:04
 */
public interface BrigadeClassMapper extends BaseMapperImpl<BrigadeClassEntity> {


    List<Map<String,Object>> selectList();

    String getNameByList(@Param("list") List id);

    Page<Map<String,Object>> selectPage(@Param("page")Page page,@Param("entity") BrigadeClassEntity entity);

    Integer selectCount(@Param("entity") BrigadeClassEntity entity);


}