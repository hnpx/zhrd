package cn.px.sys.modular.project.entity;

import cn.px.base.core.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * (ProjectEvaluate)
 *
 * @author
 * @since 2020-09-28 11:03:34
 */
@TableName("project_evaluate")
public class ProjectEvaluateEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = 254748430051715755L;
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
     * 党员评价内容
     */
    @TableField("content")
    private String content;


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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}