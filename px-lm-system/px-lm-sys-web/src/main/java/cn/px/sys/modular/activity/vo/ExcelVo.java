package cn.px.sys.modular.activity.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class ExcelVo {

    @Excel(name = "真实姓名",width = 50)
    private String realName;

    @Excel(name = "联系电话",width = 50)
    private String phone;

    @Excel(name = "签到人昵称",width = 50)
    private String name ;

    
    @Excel(name = "报名时间",width = 50)
    private String createTime;



    private Long id;

    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
