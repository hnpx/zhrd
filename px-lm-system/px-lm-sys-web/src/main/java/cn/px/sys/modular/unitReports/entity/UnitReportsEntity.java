package cn.px.sys.modular.unitReports.entity;

import cn.px.base.core.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 单位报到(UnitReports)
 *
 * @author
 * @since 2020-10-12 15:47:04
 */
@TableName("unit_reports")
public class UnitReportsEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = -84508849877313817L;
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
     * 联系人
     */
    @TableField("user_name")
    private String userName;
    /**
     * 联系人电话
     */
    @TableField("user_phone")
    private String userPhone;
    /**
     * 联系人地址
     */
    @TableField("user_address")
    private String userAddress;
    /**
     * 资源库
     */
    @TableField("resources")
    private String resources;


    @TableField("community_class")
    private String communityClass;

    @TableField("user_id")
    private Long userId;
    @TableField(exist = false)
    private String secondName;

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

}