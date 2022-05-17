package cn.px.sys.modular.peopleCongress.wapper;

import cn.hutool.core.bean.BeanUtil;
import cn.px.base.core.wrapper.BaseWrapper;
import cn.px.sys.modular.member.vo.MemberVo;
import cn.px.sys.modular.peopleAppRecord.service.PeopleAppRecordService;
import cn.px.sys.modular.peopleCongress.vo.PeopleCongressVo;
import cn.px.sys.modular.peopleOrganization.entity.PeopleOrganizationEntity;
import cn.px.sys.modular.peopleOrganization.service.PeopleOrganizationService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class PeopleCongressWrapper extends BaseWrapper<Map<String,Object>, PeopleCongressVo> {

    @Resource
    private PeopleOrganizationService peopleOrganizationService;
    @Resource
    private PeopleAppRecordService peopleAppRecordService;
    @Override
    public PeopleCongressVo wrap(Map<String, Object> item) {
        PeopleCongressVo vo = new PeopleCongressVo();
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
