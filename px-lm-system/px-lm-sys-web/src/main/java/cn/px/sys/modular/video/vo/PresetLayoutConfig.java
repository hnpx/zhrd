package cn.px.sys.modular.video.vo;

import lombok.Data;

@Data
public class PresetLayoutConfig {
    private String UserId;
    private Integer StreamType;
    private Integer ImageWidth;
    private Integer ImageHeight;
    private Integer LocationX;
    private Integer LocationY;
    private Integer ZOrder;
    private Integer RenderMode;
    private Integer MixInputType;
    private Integer PlaceImageId;
}
