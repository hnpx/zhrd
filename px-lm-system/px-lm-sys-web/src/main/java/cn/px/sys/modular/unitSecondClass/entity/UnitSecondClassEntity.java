package cn.px.sys.modular.unitSecondClass.entity;

import cn.px.base.core.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * (UnitSecondClass)
 *
 * @author
 * @since 2020-10-12 11:56:43
 */
@TableName("unit_second_class")
public class UnitSecondClassEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = 109809886740267250L;
    /**
     * 分类名称
     */
    @TableField("name")
    private String name;

    @TableField("first_id")
    private Long firstId;

    @TableField("cid")
    private Long cid;

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Long getFirstId() {
        return firstId;
    }

    public void setFirstId(Long firstId) {
        this.firstId = firstId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}