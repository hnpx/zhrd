package cn.px.sys.modular.peopleOrganization.vo;

import cn.px.sys.modular.peopleCongress.vo.PeopleMermerVo;
import cn.px.sys.modular.peopleWebmaster.vo.WebmasterVo;
import lombok.Data;

import java.util.List;
@Data
public class PeopleOrganizationVo {
 private Long id;
 private String name;
 private Long pid;
 private List<PeopleMermerVo> peopleMermerVoList;
 private List<WebmasterVo> webmasterVoList;
}
