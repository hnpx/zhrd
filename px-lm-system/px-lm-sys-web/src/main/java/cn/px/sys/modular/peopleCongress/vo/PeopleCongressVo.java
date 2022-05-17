package cn.px.sys.modular.peopleCongress.vo;

import cn.px.base.core.BaseModel;
import cn.px.sys.modular.peopleDynamic.entity.PeopleDynamicEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 人大代表(PeopleCongress)
 *
 * @author
 * @since 2021-05-13 16:05:50
 */
@Data
public class PeopleCongressVo extends BaseModel implements Serializable {
    private static final long serialVersionUID = -68533107305422235L;
    /**
     * 人大代表姓名
     */
    private String name;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 性别（1.男2.女）
     */
    private Integer sex;
    /**
     * 代表照片
     */
    private String photo;
    /**
     * 所属职位
     */
    private Long position;
    /**
     * 政治一言
     */
    private Object politicalWords;
    /**
     * 代表简介
     */
    private Object introduction;
    /**
     * 叙述信息
     */
    private Object narrative;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 所属组织
     */
    private Long organization;

    /**
     * 所属组织名字
     *
     * @return
     */
    private String organizationName;

    /**
     * 工作动态
     *
     * @return
     */
    private List<PeopleDynamicEntity> workNewList;

    /**
     * 工作动态
     *
     * @return
     */
    private Integer workNewNum;

    /**
     * 爱心暖家
     *
     * @return
     */
    private List<PeopleDynamicEntity> loveWarmHomeList;
    /**
     * 爱心暖家数
     *
     * @return
     */
    private Integer loveWarmHomeNum;


    /**
     * 接待记录
     *
     * @return
     */
    private List<PeopleDynamicEntity> receptionList;
    /**
     * 接待记录 数
     *
     * @return
     */
    private Integer receptionNum;
    /**
     * 年龄
     */
    private Integer age;

    /**
     *是否开启连线（1.开启2.不开启）
     */
    private Integer isOpenOnline;

    /**
     * 连线次数
     */

    private Integer onlineNum;
    /**
     * 申请连线次数
     */
    private Integer applyNum;

}
