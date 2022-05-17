package cn.px.sys.modular.peopleWebmaster.vo;

import lombok.Data;

@Data
public class WebmasterVo {
    private Long id;
    private String name;
    private String photo;
    private int isWebmaster;
    private int isVicechief;

}
