package cn.px.sys.modular.integral.mapper;

import cn.px.base.core.BaseMapperImpl;
import cn.px.sys.modular.integral.entity.IntegralRecordEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 积分管理(IntegralRecord)表数据库访问层
 *
 * @author
 * @since 2020-09-07 09:36:11
 */
public interface IntegralRecordMapper extends BaseMapperImpl<IntegralRecordEntity> {

    /**
     * 查询用户列表
     * @param page
     * @param userId
     * @return
     */
    Page<Map<String,Object>> getHomeList(@Param("page") Page page,@Param("userId") Long userId);

    /**
     * 今日获得积分
     * @param userId
     * @return
     */
    public int getIntegralToday(@Param("userId") Long userId);


    /**
     * 积分流水
     *
     * @return
     */
    Page<Map<String,Object>> FindIntergralList(@Param("page") Page page);

    /*
        个人积分排行列表
     */
    Page<Map<String,Object>> FindIntergralListByUser(@Param("page")Page page);

    /*
        个人积分排行
     */
    Map<String,Object> FindIntergralListByUserId(@Param("userId")Long userId);




     /*
        组织排行列表
     */
     Page<Map<String,Object>> FindIntergralListByOrganization(@Param("page")Page page);

     /*
        个人组织排行
     */
     Map<String,Object> FindOrganizationIntergralListByUserId(@Param("id")Long id);



    /*
    社区排行列表
    */
    Page<Map<String,Object>> FindIntergralListByCommunity(@Param("page")Page page);


     /*
    个人社区排行
    */
     Map<String,Object> FindCommunityByCommunityId(@Param("id")Long id);



    /*
    社区平均分排行列表
  */
    Page<Map<String,Object>> FindIntergralAVGListByCommunity(@Param("page")Page page);


    /*
   个人社区平均分排行
   */
    Map<String,Object> FindCommunityAVGByCommunityId(@Param("id")Long id);


    /**
     * 查询商家获取积分和
     */

    public int getIntegral (@Param("mid") Long mid);

    Page<Map<String,Object>> getExchangeRecord(@Param("page") Page page,@Param("userId") Long userId);



    Page<Map<String,Object>> VolunteerRanking(@Param("page")Page page);
    Page<Map<String,Object>> memberRanking(@Param("page")Page page);
    Page<Map<String,Object>> unitRanking(@Param("page")Page page);


}