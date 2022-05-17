package cn.px.sys.modular.peopleAppTime.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class AppTimeVo {
    private Long id;
    private Long peopleCongress;
    private Date appointmentTime;
    private String peopleCongressName;
    private String organizationName;
    private String photo;


}
