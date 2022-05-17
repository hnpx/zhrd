package cn.px.sys.modular.volunteersClass.entity;

import cn.px.base.core.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * (VolunteersClass)
 *
 * @author
 * @since 2020-10-12 09:37:35
 */
@TableName("volunteers_class")
public class VolunteersClassEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = 895882832591679899L;
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