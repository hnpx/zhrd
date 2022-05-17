package cn.px.sys.modular.peopleAppRecord.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ConfirmVo {
    private Long id;
    private Integer status;
    private String instructions;
    private Date appTime;
}
