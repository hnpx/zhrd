package cn.px.sys.modular.reports.mapper;


import cn.px.sys.modular.activity.entity.ActivityEntity;
import cn.px.sys.modular.news.entity.News;
import cn.px.sys.modular.project.entity.ProjectEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ApiMapper {

    Page<ActivityEntity> getActivity(@Param("page")Page page);

    Page<ProjectEntity> getProject(@Param("page")Page page);

    Page<News> getNews(@Param("page")Page page);

    List<Map<String,Object>> selectUserSign(@Param("id")Long id);


}
