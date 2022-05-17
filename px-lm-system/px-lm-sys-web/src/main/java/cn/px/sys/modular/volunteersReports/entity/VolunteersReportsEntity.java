package cn.px.sys.modular.volunteersReports.entity;

import cn.px.base.core.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 志愿者报到(VolunteersReports)
 *
 * @author
 * @since 2020-10-12 15:18:50
 */
@TableName("volunteers_reports")
public class VolunteersReportsEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = -17901334994137356L;
    /**
     * 姓名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 性别
     */
    @TableField("user_sex")
    private String userSex;
    /**
     * 用户身份证
     */
    @TableField("user_idcard")
    private String userIdcard;
    /**
     * 联系电话
     */
    @TableField("user_phone")
    private String userPhone;
    /**
     * 所属社区
     */
    @TableField("community_class")
    private String communityClass;
    /**
     * 服务大队id
     */
    @TableField("brigade_class")
    private String brigadeClass;

    @TableField("user_id")
    private Long userId;

    @TableField("volunteers_class")
    private Long volunteersClass;
    /**
     * 是否为党员（1.是2.否）
     */
    @TableField("role")
    private Integer role;

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Long getVolunteersClass() {
        return volunteersClass;
    }

    public void setVolunteersClass(Long volunteersClass) {
        this.volunteersClass = volunteersClass;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserIdcard() {
        return userIdcard;
    }

    public void setUserIdcard(String userIdcard) {
        this.userIdcard = userIdcard;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getCommunityClass() {
        return communityClass;
    }

    public void setCommunityClass(String communityClass) {
        this.communityClass = communityClass;
    }

    public String getBrigadeClass() {
        return brigadeClass;
    }

    public void setBrigadeClass(String brigadeClass) {
        this.brigadeClass = brigadeClass;
    }

}