package cn.px.sys.modular.unitFirstClass.entity;

import cn.px.base.core.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * (UnitFirstClass)
 *
 * @author
 * @since 2020-10-12 11:55:39
 */
@TableName("unit_first_class")
public class UnitFirstClassEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = 337048476224861089L;
    /**
     * 分类名称
     */
    @TableField("name")
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}