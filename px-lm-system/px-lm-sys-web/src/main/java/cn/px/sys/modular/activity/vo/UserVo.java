package cn.px.sys.modular.activity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserVo {
    private Long userId;
    private String nickname;
    private String avatar;
    private String realName;
    private String createTime;
    private String phone;
}
