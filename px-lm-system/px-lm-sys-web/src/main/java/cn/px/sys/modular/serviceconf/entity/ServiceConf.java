package cn.px.sys.modular.serviceconf.entity;

import cn.px.base.core.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName("service_conf")
@Data
public class ServiceConf extends BaseModel implements Serializable {

    private static final long serialVersionUID = 956163609713482199L;
    /**
     * 服务配置
     */
    @TableField("service_conf")
    private String serviceConf;

    public String getServiceConf() {
        return serviceConf;
    }

    public void setServiceConf(String serviceConf) {
        this.serviceConf = serviceConf;
    }
}

