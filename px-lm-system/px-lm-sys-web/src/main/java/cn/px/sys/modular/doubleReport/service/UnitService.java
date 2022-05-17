package cn.px.sys.modular.doubleReport.service;

import cn.hutool.core.bean.BeanUtil;
import cn.px.base.Constants;
import cn.px.base.core.BaseServiceImpl;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.sys.modular.doubleReport.entity.UnitEntity;
import cn.px.sys.modular.doubleReport.mapper.UnitMapper;
import cn.px.sys.modular.doubleReport.vo.UnitVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 单位管理(Unit)表服务实现类
 *
 * @author
 * @since 2020-08-28 16:16:30
 */
@Service("unitService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "unit")
public class UnitService extends BaseServiceImpl<UnitEntity, UnitMapper> {

    /**
     * 通过社区查询单位列表
     * @param belongingCommunity
     * @return
     */
   public List<UnitVo> getUnitEntityByCommunity(@Param("belongingCommunity") Long belongingCommunity){

        List<UnitEntity> unitEntityList = super.mapper.getUnitEntityByCommunity(belongingCommunity);
        List<UnitVo> unitVoList = new ArrayList<>();
        for (UnitEntity unitEntity:unitEntityList) {
            UnitVo unitVo = new UnitVo();
            BeanUtil.copyProperties(unitEntity,unitVo);
            unitVoList.add(unitVo);
        }
        return unitVoList;

    }

    /**
     * 查询单位列表
     * @param name
     * @param cid
     * @return
     */
   public Page<Map<String,Object>> getList(String name,Long cid,String contactPerson,Long belongingCommunity){
        Page page = LayuiPageFactory.defaultPage();
        return super.mapper.getList(page,name,cid,contactPerson,belongingCommunity);
    }

    /**
     * 通过手机号获取单位信息
     * @param phone
     * @return
     */
    public UnitEntity getUnitEntityByPhone(@Param("phone") String phone){
        return super.mapper.getUnitEntityByPhone(phone);
    }

}