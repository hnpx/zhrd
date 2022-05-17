package cn.px.sys.modular.peopleCongress.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import cn.px.base.core.BaseModel;

/**
 * 人大代表(PeopleCongress)
 *
 * @author
 * @since 2021-05-13 16:05:50
 */
@TableName("people_congress")
public class PeopleCongressEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = -68533107305422235L;
    /**
     * 人大代表姓名
     */
    @TableField("name")
    private String name;
    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;
    /**
     * 性别（1.男2.女）
     */
    @TableField("sex")
    private Integer sex;
    /**
     * 代表照片
     */
    @TableField("photo")
    private String photo;
    /**
     * 所属职位
     */
    @TableField("position")
    private Long position;
    /**
     * 政治一言
     */
    @TableField("political_words")
    private Object politicalWords;
    /**
     * 代表简介
     */
    @TableField("introduction")
    private Object introduction;
    /**
     * 叙述信息
     */
    @TableField("narrative")
    private Object narrative;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 所属组织
     */
    @TableField("organization")
    private Long organization;

    /**
     * 年份
     * @return
     */
    @TableField("years")
    private Integer years;

    /**
     * 是否开启连线（1.开启2.不开启）
     */
    @TableField("is_open_online")
    private Integer  isOpenOnline;
    /**
     * 通知数
     * @return
     */
    @TableField(exist = false)
    private Integer noticeNum;

    /**
     * age
     * @return
     */
     @TableField(exist = false)
     private Integer age;

    /**
     * 所属组织
     */
    @TableField(exist = false)
    private String organizationName;

    public Integer getIsOpenOnline() {
        return isOpenOnline;
    }

    public void setIsOpenOnline(Integer isOpenOnline) {
        this.isOpenOnline = isOpenOnline;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getNoticeNum() {
        return noticeNum;
    }

    public void setNoticeNum(Integer noticeNum) {
        this.noticeNum = noticeNum;
    }

    public Integer getYears() {
        return years;
    }

    public void setYears(Integer years) {
        this.years = years;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public Object getPoliticalWords() {
        return politicalWords;
    }

    public void setPoliticalWords(Object politicalWords) {
        this.politicalWords = politicalWords;
    }

    public Object getIntroduction() {
        return introduction;
    }

    public void setIntroduction(Object introduction) {
        this.introduction = introduction;
    }

    public Object getNarrative() {
        return narrative;
    }

    public void setNarrative(Object narrative) {
        this.narrative = narrative;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrganization() {
        return organization;
    }

    public void setOrganization(Long organization) {
        this.organization = organization;
    }

}
