package cn.px.sys.modular.brigadeClass.entity;

import cn.px.base.core.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * (BrigadeClass)
 *
 * @author
 * @since 2020-10-12 09:39:03
 */
@TableName("brigade_class")
public class BrigadeClassEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = 798282319539199331L;
    /**
     * 分类名称
     */
    @TableField("name")
    private String name;


    @TableField(exist = false)
    private Long brigadeClass;

    @TableField(exist = false)
    private String realName;

    @TableField(exist = false)
    private String phone;

    @TableField(exist = false)
    private String communityClass;

    public String getCommunityClass() {
        return communityClass;
    }

    public void setCommunityClass(String communityClass) {
        this.communityClass = communityClass;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getBrigadeClass() {
        return brigadeClass;
    }

    public void setBrigadeClass(Long brigadeClass) {
        this.brigadeClass = brigadeClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}