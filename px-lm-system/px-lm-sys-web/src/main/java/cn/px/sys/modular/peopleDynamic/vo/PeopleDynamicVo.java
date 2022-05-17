package cn.px.sys.modular.peopleDynamic.vo;

import cn.px.base.core.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 文章内容(PeopleDynamic)
 *
 * @author
 * @since 2021-05-13 16:07:29
 */
public class PeopleDynamicVo extends BaseModel implements Serializable {
    private String name;
    private Long dynamicType;

    private String content;

    private Integer views;

    private Long peopleCongress;

    private String author;

    private String videos;

    private String photo;
    private String dynamicTypeName;
    private Integer isPass;

    public Integer getIsPass() {
        return isPass;
    }

    public void setIsPass(Integer isPass) {
        this.isPass = isPass;
    }

    public String getDynamicTypeName() {
        return dynamicTypeName;
    }

    public void setDynamicTypeName(String dynamicTypeName) {
        this.dynamicTypeName = dynamicTypeName;
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
