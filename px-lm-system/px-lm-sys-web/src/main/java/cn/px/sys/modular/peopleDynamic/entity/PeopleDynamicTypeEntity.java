package cn.px.sys.modular.peopleDynamic.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import cn.px.base.core.BaseModel;

/**
 * 文章类型(PeopleDynamicType)
 *
 * @author
 * @since 2021-05-13 16:08:20
 */
@TableName("people_dynamic_type")
public class PeopleDynamicTypeEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = 148906516616909043L;
    /**
     * 文章名称
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
