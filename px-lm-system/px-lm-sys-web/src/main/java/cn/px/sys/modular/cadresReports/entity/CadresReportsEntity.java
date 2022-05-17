package cn.px.sys.modular.cadresReports.entity;

import cn.px.base.core.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 党员干部报到(CadresReports)
 *
 * @author
 * @since 2020-10-12 15:46:05
 */
@TableName("cadres_reports")
public class CadresReportsEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = 484419479806109178L;
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
     * 一级分类id
     */
    @TableField("first_id")
    private Long firstId;
    /**
     * 二级分类id
     */
    @TableField("second_id")
    private Long secondId;
    /**
     * 服务大队id
     */
    @TableField("brigade_class")
    private String brigadeClass;
    /**
     * 1:成功 2:代审核
     */
    @TableField("type")
    private Integer type;

    @TableField("user_id")
    private Long userId;


    @TableField("is_member")
    private Long isMember;


    public Long getIsMember() {
        return isMember;
    }

    public void setIsMember(Long isMember) {
        this.isMember = isMember;
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

    public Long getFirstId() {
        return firstId;
    }

    public void setFirstId(Long firstId) {
        this.firstId = firstId;
    }

    public Long getSecondId() {
        return secondId;
    }

    public void setSecondId(Long secondId) {
        this.secondId = secondId;
    }

    public String getBrigadeClass() {
        return brigadeClass;
    }

    public void setBrigadeClass(String brigadeClass) {
        this.brigadeClass = brigadeClass;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}