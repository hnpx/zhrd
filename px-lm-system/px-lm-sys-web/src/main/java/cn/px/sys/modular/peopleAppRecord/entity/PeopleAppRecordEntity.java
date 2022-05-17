package cn.px.sys.modular.peopleAppRecord.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import cn.px.base.core.BaseModel;

/**
 * 群众预约记录(PeopleAppRecord)
 *
 * @author
 * @since 2021-05-13 15:55:27
 */
@TableName("people_app_record")
public class PeopleAppRecordEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = 574699933311516874L;
    /**
     * 群众id
     */
    @TableField("crowd_id")
    private Long crowdId;
    /**
     * 人大代表id
     */
    @TableField("congress_id")
    private Long congressId;
    /**
     * 申请预约连线状态（1.待同意2.同意预约3.拒绝连接4.连线结束）
     */
    @TableField("status")
    private Integer status;
    /**
     * 预约时间(1.群众预约时只有年月日，2.人大代表同意预约时有年月日时分)
     */
    @TableField("app_time")
    private Date appTime;
    /**
     * 预约内容说明
     */
    @TableField("content")
    private String content;
    /**
     * 拒绝预约说明
     */
    @TableField("instructions")
    private String instructions;
    /**
     * 连线视频记录
     */
    @TableField("url")
    private String url;

    /**
     *是否被查看（1.未看2.已看）
     */
    @TableField("is_toview")
    private Integer isToview;

    /**
     * 房间号
     */
   @TableField("room_number")
   private String roomNumber;
    /**
     *视频是否已删除（1.已删除2.未删除）
     */
    @TableField("is_delete")
    private Integer isDelete;
    /**
     * 群众名称
     * @return
     */
    @TableField(exist = false)
    private String crowdName;

    /**
     * 群众名称
     * @return
     */
    @TableField(exist = false)
    private String crowdPhone;

    /**
     * 代表名称
     * @return
     */
    @TableField(exist = false)
    private String congressName;

    /**
     * 组织名
     * @return
     */
    @TableField(exist = false)
    private String organizationName;

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getCrowdPhone() {
        return crowdPhone;
    }

    public void setCrowdPhone(String crowdPhone) {
        this.crowdPhone = crowdPhone;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getCrowdName() {
        return crowdName;
    }
    public void setCrowdName(String crowdName) {
        this.crowdName = crowdName;
    }
    public Integer getIsToview() {
        return isToview;
    }

    public void setIsToview(Integer isToview) {
        this.isToview = isToview;
    }

    public String getCongressName() {
        return congressName;
    }

    public void setCongressName(String congressName) {
        this.congressName = congressName;
    }

    public Long getCrowdId() {
        return crowdId;
    }

    public void setCrowdId(Long crowdId) {
        this.crowdId = crowdId;
    }

    public Long getCongressId() {
        return congressId;
    }

    public void setCongressId(Long congressId) {
        this.congressId = congressId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getAppTime() {
        return appTime;
    }

    public void setAppTime(Date appTime) {
        this.appTime = appTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
