package cn.px.sys.modular.wx.vo;

import lombok.Data;

@Data
public class WxUserVo {

    private String avatar;
    private String nickname;
    private Integer userType;
    private Long id;
    private String phone;
    private String openid;
}
