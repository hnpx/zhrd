package cn.px.sys.modular.peopleWebmaster.vo;

import cn.px.base.core.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

@Data
public class PeopleWebmasterVo extends BaseModel implements Serializable {
    private String name;

    private String photo;

    private Long organization;

    private Integer isWebmaster;
    private String organizationName;
    private Integer isVicechief;

}
