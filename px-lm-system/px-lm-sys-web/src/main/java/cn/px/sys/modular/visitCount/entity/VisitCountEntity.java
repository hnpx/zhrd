package cn.px.sys.modular.visitCount.entity;

import cn.px.base.core.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * (VisitCount)
 *
 * @author
 * @since 2020-10-19 14:17:06
 */
@TableName("visit_count")
public class VisitCountEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = -71519372648644780L;
    @TableField("user_id")
    private Long userId;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}