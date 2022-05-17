package cn.px.sys.modular.peopleWebmaster.wrapper;

import cn.hutool.core.bean.BeanUtil;
import cn.px.base.core.wrapper.BaseWrapper;
import cn.px.sys.modular.peopleCongress.vo.PeopleCongressVo;
import cn.px.sys.modular.peopleOrganization.entity.PeopleOrganizationEntity;
import cn.px.sys.modular.peopleOrganization.service.PeopleOrganizationService;
import cn.px.sys.modular.peopleWebmaster.entity.PeopleWebmasterEntity;
import cn.px.sys.modular.peopleWebmaster.vo.PeopleWebmasterVo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class PeopleWebmasterWrapper extends BaseWrapper<Map<String,Object>, PeopleWebmasterVo> {

    @Resource
    private PeopleOrganizationService peopleOrganizationService;
    @Override
    public PeopleWebmasterVo wrap(Map<String, Object> item) {
        PeopleWebmasterVo vo = new PeopleWebmasterVo();
        BeanUtil.copyProperties(item, vo);
        if (vo.getOrganization() != null){
          PeopleOrganizationEntity peopleOrganizationEntity = peopleOrganizationService.queryById(vo.getOrganization());
          if(peopleOrganizationEntity != null){
              vo.setOrganizationName(peopleOrganizationEntity.getName());
          }
        }
        return vo;
    }
}
