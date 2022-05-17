package cn.px.sys.modular.peopleInformationSet.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import cn.px.base.core.BaseModel;

/**
 * 人大信息设置(PeopleInformationSet)
 *
 * @author
 * @since 2021-05-13 16:09:00
 */
@TableName("people_information_set")
public class PeopleInformationSetEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = 950718934419650822L;
    /**
     * 人大名称
     */
    @TableField("name")
    private String name;
    /**
     * 人大介绍
     */
    @TableField("introduce")
    private String introduce;
    /**
     * 人大简介照片
     */
    @TableField("picture")
    private String picture;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}
