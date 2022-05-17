package cn.px.sys.modular.peopleAppRecord.vo;

import cn.px.base.core.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 群众预约记录(PeopleAppRecord)
 *
 * @author
 * @since 2021-05-13 15:55:27
 */
@Data
public class PeopleAppRecordVo extends BaseModel implements Serializable {
    /**
     * 群众id
     */
    private Long crowdId;
    /**
     * 人大代表id
     */
    private Long congressId;
    /**
     * 申请预约连线状态（1.待同意2.同意预约3.拒绝连接4.连线结束）
     */
    private Integer status;
    /**
     * 预约时间(1.群众预约时只有年月日，2.人大代表同意预约时有年月日时分)
     */
    private Date appTime;
    /**
     * 预约内容说明
     */
    private String content;
    /**
     * 拒绝预约说明
     */
    private String instructions;
    /**
     * 连线视频记录
     */
    private String url;

    /**
     * 群众名字
     */
    private String crowdName;
    /**
     * 手机号
     */

    private String phone;
}
