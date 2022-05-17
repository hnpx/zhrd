package cn.px.sys.modular.peopleAppRecord.wrapper;

import cn.hutool.core.bean.BeanUtil;
import cn.px.base.core.wrapper.BaseWrapper;
import cn.px.sys.modular.peopleAppRecord.vo.PeopleAppRecordVo;
import cn.px.sys.modular.peopleCrowd.entity.PeopleCrowdEntity;
import cn.px.sys.modular.peopleCrowd.service.PeopleCrowdService;
import cn.px.sys.modular.peopleOrganization.entity.PeopleOrganizationEntity;
import cn.px.sys.modular.peopleOrganization.service.PeopleOrganizationService;
import cn.px.sys.modular.peopleWebmaster.vo.PeopleWebmasterVo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class PeopleAppRecordWrapper extends BaseWrapper<Map<String, Object>, PeopleAppRecordVo> {

    @Resource
    private PeopleCrowdService peopleCrowdService;

    @Override
    public PeopleAppRecordVo wrap(Map<String, Object> item) {
        PeopleAppRecordVo vo = new PeopleAppRecordVo();
        BeanUtil.copyProperties(item, vo);
        PeopleCrowdEntity peopleCrowdEntity = peopleCrowdService.queryById(vo.getCrowdId());
        if(peopleCrowdEntity != null){
            vo.setCrowdName(peopleCrowdEntity.getName());
            vo.setPhone(peopleCrowdEntity.getPhone());
        }
        return vo;
    }
}
