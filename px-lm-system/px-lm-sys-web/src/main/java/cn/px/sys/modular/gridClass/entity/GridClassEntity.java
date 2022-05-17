package cn.px.sys.modular.gridClass.entity;

import cn.px.base.core.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * (GridClass)
 *
 * @author
 * @since 2020-10-12 09:40:13
 */
@TableName("grid_class")
public class GridClassEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = 366504161467277568L;
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