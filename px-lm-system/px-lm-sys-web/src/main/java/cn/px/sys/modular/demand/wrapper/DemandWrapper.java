package cn.px.sys.modular.demand.wrapper;

import cn.hutool.core.bean.BeanUtil;
import cn.px.base.core.wrapper.BaseWrapper;
import cn.px.sys.modular.demand.vo.DemandVo;
import cn.px.sys.modular.demandClass.service.DemandClassService;
import cn.px.sys.modular.merchant.vo.MerchantVo;
import jodd.util.URLDecoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
@Component
public class DemandWrapper extends BaseWrapper<Map<String,Object>, DemandVo> {

    @Resource
    private DemandClassService demandClassService;
    @Override
    public DemandVo wrap(Map<String, Object> item) {
        DemandVo vo = new DemandVo();
        BeanUtil.copyProperties(item, vo);
        try{

            vo.setContent(URLDecoder.decode(item.get("content").toString(),"UTF-8"));
            Long  demandClass = Long.parseLong(item.get("demandClass").toString());
            if (demandClass!=null){
                vo.setDemandClassName(demandClassService.queryById(demandClass).getName());
            }
        }catch (Exception e){
            vo.setDemandClassName("");
        }
        return vo;
    }
}
