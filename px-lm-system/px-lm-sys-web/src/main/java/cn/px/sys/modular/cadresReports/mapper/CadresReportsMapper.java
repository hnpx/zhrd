package cn.px.sys.modular.cadresReports.mapper;

import cn.px.base.core.BaseMapperImpl;
import cn.px.sys.modular.cadresReports.entity.CadresReportsEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;


/**
 * 党员干部报到(CadresReports)表数据库访问层
 *
 * @author
 * @since 2020-10-12 15:46:06
 */
public interface CadresReportsMapper extends BaseMapperImpl<CadresReportsEntity> {

    Page<Map<String,Object>> selectListPage(@Param("page") Page page,@Param("entity")CadresReportsEntity entity);


    Integer getCount();


    Page<Map<String,Object>> getAllById(@Param("page")Page page);

    Integer isBrigadeClass(@Param("uid")Long uid,@Param("bid")Long bid);

    CadresReportsEntity getEntityByUser(@Param("userId") Long userId);
    int getCountByall();


}