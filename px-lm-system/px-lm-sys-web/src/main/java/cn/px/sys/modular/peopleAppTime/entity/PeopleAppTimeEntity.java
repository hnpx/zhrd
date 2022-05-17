package cn.px.sys.modular.peopleAppTime.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import cn.px.base.core.BaseModel;

/**
 * 人大预约时间(PeopleAppTime)
 *
 * @author
 * @since 2021-05-13 16:04:54
 */
@TableName("people_app_time")
public class PeopleAppTimeEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = 475466516623907852L;
    /**
     * 人大代表id
     */
    @TableField("people_congress")
    private Long peopleCongress;
    /**
     * 预约时间
     */
    @TableField("appointment_time")
    private Date appointmentTime;

    /**
     * 预约时间类型（1.线上2.线下）
     * @return
     */
    @TableField("type")
    private Integer type;

    /**
     * 代表名字
     * @return
     */
    @TableField(exist = false)
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getPeopleCongress() {
        return peopleCongress;
    }

    public void setPeopleCongress(Long peopleCongress) {
        this.peopleCongress = peopleCongress;
    }

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

}
