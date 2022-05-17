package cn.px.sys.modular.peopleOrganization.entity;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import cn.px.base.core.BaseModel;

/**
 * 组织架构(PeopleOrganization)
 *
 * @author
 * @since 2021-05-13 16:10:10
 */
@TableName("people_organization")
public class PeopleOrganizationEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = -35184272492179834L;
    /**
     * 名称
     */
    @TableField("name")
    private String name;
    /**
     * 父级id
     */
    @TableField("pid")
    private Long pid;
    /**
     * 是否可以修改（1.可以2.不可以）
     */
    @TableField("is_modify")
    private Integer isModify;

    @TableField(exist = false)
    private List<PeopleOrganizationEntity> children;
    @TableField(exist = false)
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<PeopleOrganizationEntity> getChildren() {
        return children;
    }

    public void setChildren(List<PeopleOrganizationEntity> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Integer getIsModify() {
        return isModify;
    }

    public void setIsModify(Integer isModify) {
        this.isModify = isModify;
    }

}
