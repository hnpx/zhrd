package cn.px.sys.modular.doubleReport.wrapper;

import cn.hutool.core.bean.BeanUtil;
import cn.px.base.core.wrapper.BaseWrapper;
import cn.px.sys.modular.doubleReport.vo.ReportCadreVo;
import cn.px.sys.modular.doubleReport.vo.UnitListVo;
import cn.px.sys.modular.integralWater.service.IntegralWaterService;
import cn.px.sys.modular.wx.service.AllUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class ReportCadreWrapper extends BaseWrapper<Map<String,Object>, ReportCadreVo> {
@Resource
private AllUserService allUserService;
@Resource
private IntegralWaterService integralWaterService;
    @Override
    public ReportCadreVo wrap(Map<String, Object> item) {
        ReportCadreVo vo = new ReportCadreVo();
        BeanUtil.copyProperties(item, vo);
        try{
         Long userId = Long.parseLong(item.get("userId").toString());

         if(userId != null){
             int showIntegral = integralWaterService.showIntegral(userId);
             vo.setShowIntegral(showIntegral);
             if(allUserService.queryById(userId)!= null){
                 if(allUserService.queryById(userId).getStatus().equals(1)){   //1. 未报到2.已报道0.未绑定信息
                     vo.setReportStatus(1);
                 }else {
                     vo.setReportStatus(2);
                 }
             }else {
                 vo.setReportStatus(0);
             }

         }else {
             vo.setReportStatus(0);
         }
        }catch (Exception e){

            vo.setReportStatus(0);
        }


        return vo;
    }
}
