package cn.px.sys.modular.peopleDynamic.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import cn.px.base.core.BaseModel;

/**
 * 文章内容(PeopleDynamic)
 *
 * @author
 * @since 2021-05-13 16:07:29
 */
@TableName("people_dynamic")
public class PeopleDynamicEntity extends BaseModel implements Serializable {
    private static final long serialVersionUID = -61912582036660558L;
    /**
     * 名称
     */
    @TableField("name")
    private String name;
    /**
     * 类型id
     */
    @TableField("dynamic_type")
    private Long dynamicType;
    /**
     * 内容
     */
    @TableField("content")
    private String content;
    /**
     * 访问量
     */
    @TableField("views")
    private Integer views;
    /**
     * 人大代表id
     */
    @TableField("people_congress")
    private Long peopleCongress;
    /**
     * 作者
     */
    @TableField("author")
    private String author;
    /**
     * 视频
     */
    @TableField("videos")
    private String videos;

    /**
     * 封面图
     * @return
     */
    @TableField("photo")
    private String photo;
    @TableField("is_pass")
    private Integer isPass;

    public Integer getIsPass() {
        return isPass;
    }

    public void setIsPass(Integer isPass) {
        this.isPass = isPass;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDynamicType() {
        return dynamicType;
    }

    public void setDynamicType(Long dynamicType) {
        this.dynamicType = dynamicType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Long getPeopleCongress() {
        return peopleCongress;
    }

    public void setPeopleCongress(Long peopleCongress) {
        this.peopleCongress = peopleCongress;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

}
