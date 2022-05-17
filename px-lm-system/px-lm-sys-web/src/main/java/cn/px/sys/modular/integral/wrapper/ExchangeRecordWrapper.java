package cn.px.sys.modular.integral.wrapper;

import cn.hutool.core.bean.BeanUtil;
import cn.px.base.core.wrapper.BaseWrapper;
import cn.px.sys.modular.integral.vo.ExchangeRecordVo;
import cn.px.sys.modular.spike.entity.ProductEntity;
import cn.px.sys.modular.spike.service.ProductService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class ExchangeRecordWrapper extends BaseWrapper<Map<String,Object>, ExchangeRecordVo> {

    @Resource
    private ProductService productService;
    @Override
    public ExchangeRecordVo wrap(Map<String, Object> item) {
        ExchangeRecordVo vo = new ExchangeRecordVo();
        BeanUtil.copyProperties(item, vo);
        try {
            Long pid =  Long.parseLong(item.get("pid").toString());
            ProductEntity productEntity = productService.queryById(pid);
            vo.setPName(productEntity.getName());
        }catch (Exception e){
            vo.setPName("");
        }
        return vo;
    }
}