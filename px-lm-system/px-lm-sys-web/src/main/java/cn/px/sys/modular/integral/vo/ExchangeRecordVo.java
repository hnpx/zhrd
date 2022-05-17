package cn.px.sys.modular.integral.vo;

import lombok.Data;

import java.util.Date;


@Data
public class ExchangeRecordVo {
    private Integer integral;
    private String source;
    private Long pid;
    private Long userId;
    private String pName;
    private Date createTime;
}
