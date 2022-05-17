package cn.px.sys.modular.peopleCrowd.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import cn.px.base.core.BaseModel;

/**
 * 群众表(PeopleCrowd)
 *
 * @author
 * @since 2021-05-13 16:06:43
 */
@TableName("people_crowd")
public class PeopleCrowdEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = -87147169253633936L;
    /**
     * 群众姓名
     */
    @TableField("name")
    private String name;
    /**
     * 联系方式
     */
    @TableField("phone")
    private String phone;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 预约通知数
     * @return
     */
    @TableField(exist = false)
    private Integer noticeNum;

    @TableField(exist = false)
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getNoticeNum() {
        return noticeNum;
    }

    public void setNoticeNum(Integer noticeNum) {
        this.noticeNum = noticeNum;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
