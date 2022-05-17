package cn.px.sys.modular.wx.entity;

import cn.px.base.core.BaseModel;
import cn.px.sys.modular.peopleCongress.entity.PeopleCongressEntity;
import cn.px.sys.modular.peopleCrowd.entity.PeopleCrowdEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * (AllUser)
 *
 * @author
 * @since 2020-08-29 11:03:46
 */
@TableName("all_user")
public class AllUserEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = 345281169513235645L;
    /**
     * 详细地址
     */
    @TableField("address")
    private String address;
    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;
    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;
    /**
     * openid
     */
    @TableField("openid")
    private String openid;
    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;
    /**
     * 用户类型（1.党员干部2.单位3.普通用户）
     */
    @TableField("type")
    private Integer type;
    @TableField("login_time")
    private Date loginTime;
    @TableField("integral")
    private Integer integral;
    @TableField("status")
    private Integer status;

    /**
     * 消耗积分
     */
    @TableField("points_consumption")
    private Integer pointsConsumption;
    /**
     * 剩余积分
     */
    @TableField("remaining_points")
    private Integer remainingPoints;

    @TableField("grid_class")
    private Long gridClass;
    /**
     * 身份（1.群众2.人大代表）
     */
    @TableField("identity")
    private Integer identity;

    /**
     * 智慧养老身份
     */
    @TableField("user_type")
    private Integer userType;

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    @TableField(exist = false)
    private PeopleCrowdEntity crowdEntity;

    @TableField(exist = false)
    private PeopleCongressEntity peopleCongress;

    public PeopleCongressEntity getPeopleCongress() {
        return peopleCongress;
    }

    public void setPeopleCongress(PeopleCongressEntity peopleCongress) {
        this.peopleCongress = peopleCongress;
    }

    public Integer getIdentity() {
        return identity;
    }

    public void setIdentity(Integer identity) {
        this.identity = identity;
    }

    public PeopleCrowdEntity getCrowdEntity() {
        return crowdEntity;
    }

    public void setCrowdEntity(PeopleCrowdEntity crowdEntity) {
        this.crowdEntity = crowdEntity;
    }

    public Long getGridClass() {
        return gridClass;
    }

    public void setGridClass(Long gridClass) {
        this.gridClass = gridClass;
    }



    public Integer getPointsConsumption() {
        return pointsConsumption;
    }

    public void setPointsConsumption(Integer pointsConsumption) {
        this.pointsConsumption = pointsConsumption;
    }

    public Integer getRemainingPoints() {
        return remainingPoints;
    }

    public void setRemainingPoints(Integer remainingPoints) {
        this.remainingPoints = remainingPoints;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}