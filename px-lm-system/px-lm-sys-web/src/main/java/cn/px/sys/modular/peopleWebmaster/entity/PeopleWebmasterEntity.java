package cn.px.sys.modular.peopleWebmaster.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import cn.px.base.core.BaseModel;

/**
 * 联络站人员(PeopleWebmaster)
 *
 * @author
 * @since 2021-05-13 16:10:51
 */
@TableName("people_webmaster")
public class PeopleWebmasterEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = 340286786818716181L;
    /**
     * 名字
     */
    @TableField("name")
    private String name;
    /**
     * 照片
     */
    @TableField("photo")
    private String photo;
    /**
     * 所属联络站
     */
    @TableField("organization")
    private Long organization;
    /**
     * 是否未站长（1.是2.否）
     */
    @TableField("is_webmaster")
    private Integer isWebmaster;

    /**
     *是否为副站长（1.是2.否）
     * @return
     */
     @TableField("is_vicechief")
     private Integer isVicechief;

    public Integer getIsVicechief() {
        return isVicechief;
    }

    public void setIsVicechief(Integer isVicechief) {
        this.isVicechief = isVicechief;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Long getOrganization() {
        return organization;
    }

    public void setOrganization(Long organization) {
        this.organization = organization;
    }

    public Integer getIsWebmaster() {
        return isWebmaster;
    }

    public void setIsWebmaster(Integer isWebmaster) {
        this.isWebmaster = isWebmaster;
    }

}
