package cn.px.sys.modular.peopleDynamic.wrapper;

import cn.hutool.core.bean.BeanUtil;
import cn.px.base.core.wrapper.BaseWrapper;
import cn.px.sys.modular.peopleCongress.vo.PeopleCongressVo;
import cn.px.sys.modular.peopleDynamic.entity.PeopleDynamicTypeEntity;
import cn.px.sys.modular.peopleDynamic.service.PeopleDynamicTypeService;
import cn.px.sys.modular.peopleDynamic.vo.PeopleDynamicVo;
import cn.px.sys.modular.peopleOrganization.entity.PeopleOrganizationEntity;
import cn.px.sys.modular.peopleOrganization.service.PeopleOrganizationService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class PeopleDynamicWrapper extends BaseWrapper<Map<String,Object>, PeopleDynamicVo> {

    @Resource
    private PeopleDynamicTypeService peopleDynamicTypeService;
    @Override
    public PeopleDynamicVo wrap(Map<String, Object> item) {
        PeopleDynamicVo vo = new PeopleDynamicVo();
        BeanUtil.copyProperties(item, vo);
        if(vo.getDynamicType() != null){
          PeopleDynamicTypeEntity peopleDynamicTypeEntity = peopleDynamicTypeService.queryById(vo.getDynamicType());
          if(peopleDynamicTypeEntity != null){
              vo.setDynamicTypeName(peopleDynamicTypeEntity.getName());
          }
        }
        return vo;
    }
}
