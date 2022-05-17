package cn.px.sys.modular.peopleOpinion.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import cn.px.base.core.BaseModel;

/**
 * 征集意见，优化营商(PeopleOpinion)
 *
 * @author
 * @since 2021-05-13 16:09:34
 */
@TableName("people_opinion")
public class PeopleOpinionEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = -80647472940210178L;
    /**
     * 姓名
     */
    @TableField("name")
    private String name;
    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;
    /**
     * 企业名称
     */
    @TableField("enterprise_name")
    private String enterpriseName;
    /**
     * 标题
     */
    @TableField("title")
    private String title;
    /**
     * 留言内容
     */
    @TableField("message_content")
    private String messageContent;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 类型（1.征集意见2.优化营商）
     */
    @TableField("type")
    private Integer type;
    /**
     * 解决状态（1.未解决2.已解决）
     */
    @TableField("status")
    private Integer status;

    /**
     * 解决方案
     */
    @TableField("solution")
    private String solution;
    /**
     * 类型（1.征集意见2.优化营商）
     */
    @TableField(exist = false)
    private String typeName;
    /**
     * 解决状态（1.未解决2.已解决）
     */
    @TableField(exist = false)
    private String statusName;

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
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

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
