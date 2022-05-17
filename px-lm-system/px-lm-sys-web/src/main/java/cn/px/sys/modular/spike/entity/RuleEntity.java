package cn.px.sys.modular.spike.entity;

import cn.px.base.core.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * (Rule)
 *
 * @author
 * @since 2020-11-19 09:43:55
 */
@TableName("rule")
public class RuleEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = -46322685384617017L;
    /**
     * 积分规则
     */
    @TableField("rule")
    private String rule;


    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

}