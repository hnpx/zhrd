package cn.px.sys.modular.activity.mapper;

import cn.px.base.core.BaseMapperImpl;
import cn.px.sys.modular.activity.entity.ActivityUserSignEntity;
import cn.px.sys.modular.activity.vo.ExcelVo;
import cn.px.sys.modular.activity.vo.UserVo;
import cn.px.sys.modular.wx.entity.AllUserEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * (ActivityUserSign)表数据库访问层
 *
 * @author
 * @since 2020-08-31 11:08:19
 */
public interface ActivityUserSignMapper extends BaseMapperImpl<ActivityUserSignEntity> {
    /**
     * 我参与的活动列表
     * @param page
     * @param userId
     * @return
     */
    public Page<Map<String,Object>> getListByUserId(@Param("page") Page page, @Param("userId") Long userId,@Param("date") Date date,@Param("status") Integer status);

    /**
     * 通过用户id查新报名列表
     * @param page
     * @param activeId
     * @return
     */
    public Page<Map<String,Object>> getListSignByUserId(@Param("page") Page page,@Param("activeId") Long activeId);

    public List<UserVo> getListSignBy(@Param("activeId") Long activeId);

    public List<ActivityUserSignEntity> getListSignByAid(@Param("activeId") Long activeId,@Param("status") Integer status);

    /**
     * 通过活动id和用户id查询记录
     * @param id
     * @param userId
     * @return
     */
    public int getCount(Long id,Long userId);


    List<ExcelVo> importExl(@Param("id")Long id);

    String exlName(@Param("id") Long id);
}