package cn.px.sys.modular.news.entity;

import cn.px.base.core.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("news")
public class News extends BaseModel implements Serializable {
    private static final long serialVersionUID = 956163609713482199L;


    @TableField("title")
    private String title;

    @TableField("img")
    private String img;

    @TableField("paper")
    private String paper;

    @TableField("content")
    private String content;



}
