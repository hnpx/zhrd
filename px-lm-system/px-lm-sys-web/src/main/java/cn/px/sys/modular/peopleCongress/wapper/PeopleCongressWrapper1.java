package cn.px.sys.modular.peopleCongress.wapper;

import cn.hutool.core.bean.BeanUtil;
import cn.px.base.core.wrapper.BaseWrapper;
import cn.px.sys.modular.peopleAppRecord.service.PeopleAppRecordService;
import cn.px.sys.modular.peopleCongress.vo.PeopleCongressVo;
import cn.px.sys.modular.peopleOrganization.entity.PeopleOrganizationEntity;
import cn.px.sys.modular.peopleOrganization.service.PeopleOrganizationService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class PeopleCongressWrapper1 extends BaseWrapper<Map<String,Object>, PeopleCongressVo> {

    @Resource
    private PeopleOrganizationService peopleOrganizationService;
    @Resource
    private PeopleAppRecordService peopleAppRecordService;
    @Override
    public PeopleCongressVo wrap(Map<String, Object> item) {
        PeopleCongressVo vo = new PeopleCongressVo();
        BeanUtil.copyProperties(item, vo);
            //查询预约接待次数和申请连线次数
            vo.setApplyNum( peopleAppRecordService.getCount(vo.getId(),null));
            vo.setOnlineNum(peopleAppRecordService.getCount(vo.getId(),4));

        return vo;
    }
}
