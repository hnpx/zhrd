package cn.px.sys.modular.peopleOrganization.vo;

import cn.px.sys.modular.peopleInformationSet.utils.TreeNode;
import lombok.Data;

import java.util.List;
@Data
public class PeopleOrganizationTree {
 private Long id;
 private String name;
 private Long pid;
 private List<PeopleOrganizationVo> list;
}
