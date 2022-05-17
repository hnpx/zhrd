package cn.px.sys.modular.project.entity;

import cn.px.base.core.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * (ProjectUserSign)
 *
 * @author
 * @since 2020-09-04 09:06:41
 */
@TableName("project_user_sign")
public class ProjectUserSignEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = -88953490880274821L;
    /**
     * 活动id
     */
    @TableField("project_id")
    private Long projectId;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 参与状态（0.未参加1.已签到2.已签退）
     */
    @TableField("status")
    private Integer status;


    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}