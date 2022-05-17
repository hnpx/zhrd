package cn.px.sys.modular.unitSecondClass.service;

import cn.px.base.Constants;
import cn.px.base.core.BaseServiceImpl;
import cn.px.sys.modular.unitSecondClass.entity.UnitSecondClassEntity;
import cn.px.sys.modular.unitSecondClass.mapper.UnitSecondClassMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

/**
 * (UnitSecondClass)表服务实现类
 *
 * @author
 * @since 2020-10-12 11:56:32
 */
@Service("unitSecondClassService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "unitSecondClass")
public class UnitSecondClassService extends BaseServiceImpl<UnitSecondClassEntity, UnitSecondClassMapper> {


    public Page<Map<String,Object>> selectList(Page page,String name){
        return mapper.selectList(page,name);
    }

    public List<Map<String,Object>> selectListByid(Long id){
        return mapper.selectListByid(id);
    }



  public   List<UnitSecondClassEntity> getList(){
        return mapper.getList();
    }

   public List<UnitSecondClassEntity> getListByCid( Long cid){
        return mapper.getListByCid(cid);
    }

    public List<UnitSecondClassEntity> getListByCid1( Long cid){
        return mapper.getListByCid1(cid);
    }


    public   UnitSecondClassEntity getUnitById(Long id){
        return super.mapper.getUnitById(id);
    }
}