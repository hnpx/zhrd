package cn.px.sys.modular.peopleInformationSet.vo;

import lombok.Data;

import java.sql.PreparedStatement;


@Data
public class LoginUserVo {
    private String code;
    private String phone;
    private String name;
    private Integer type;
    private String phoneCode;
    private String avatar;
    private String nickname;





}
