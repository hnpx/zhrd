package cn.px.sys.modular.peopleOpinion.vo;

import cn.px.base.core.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 征集意见，优化营商(PeopleOpinion)
 *
 * @author
 * @since 2021-05-13 16:09:34
 */
@Data
public class PeopleOpinionVo extends BaseModel implements Serializable {
    /**
     * 姓名
     */
    private String name;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 企业名称
     */
    private String enterpriseName;
    /**
     * 标题
     */
    private String title;
    /**
     * 留言内容
     */
    private String messageContent;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 类型（1.征集意见2.优化营商）
     */
    private Integer type;
    /**
     * 解决状态（1.未解决2.已解决）
     */
    private Integer status;

    /**
     * 类型（1.征集意见2.优化营商）
     */
    private String typeName;
    /**
     * 解决状态（1.未解决2.已解决）
     */
    private String statusName;

    public String solution;



}
