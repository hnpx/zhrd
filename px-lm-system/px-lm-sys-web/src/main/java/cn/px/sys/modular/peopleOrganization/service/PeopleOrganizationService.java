package cn.px.sys.modular.peopleOrganization.service;

import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.sys.modular.peopleCongress.mapper.PeopleCongressMapper;
import cn.px.sys.modular.peopleCongress.vo.PeopleMermerVo;
import cn.px.sys.modular.peopleCongress.wapper.PeopleCongressWrapper;
import cn.px.sys.modular.peopleOrganization.entity.PeopleOrganizationEntity;
import cn.px.sys.modular.peopleOrganization.mapper.PeopleOrganizationMapper;
import cn.px.sys.modular.peopleOrganization.vo.PeopleOrganizationTree;
import cn.px.sys.modular.peopleOrganization.vo.PeopleOrganizationVo;
import cn.px.sys.modular.peopleWebmaster.mapper.PeopleWebmasterMapper;
import cn.px.sys.modular.peopleWebmaster.vo.WebmasterVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.px.base.core.BaseServiceImpl;
import cn.px.base.Constants;
import org.springframework.cache.annotation.CacheConfig;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 组织架构(PeopleOrganization)表服务实现类
 *
 * @author
 * @since 2021-05-13 16:09:56
 */
@Service("peopleOrganizationService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "peopleOrganization")
public class PeopleOrganizationService extends BaseServiceImpl<PeopleOrganizationEntity, PeopleOrganizationMapper> {

    @Resource
    private PeopleCongressMapper peopleCongressMapper;
    @Resource
    private PeopleWebmasterMapper peopleWebmasterMapper;
    public List<PeopleOrganizationTree> getTree() {
        List<PeopleOrganizationTree> list = super.mapper.getList(0L);
        list.forEach(peopleOrganizationTree -> {
            List<PeopleOrganizationVo> peopleOrganizationVoList = super.mapper.getList1(peopleOrganizationTree.getId());
            peopleOrganizationVoList.forEach(peopleOrganizationVo -> {
                if(peopleOrganizationTree.getId()==1){
                    List<PeopleMermerVo> peopleMermerVoList =  peopleCongressMapper.getPeopleMermerVo(peopleOrganizationVo.getId());
                    peopleOrganizationVo.setPeopleMermerVoList(peopleMermerVoList);

                }else {
                  List<WebmasterVo> webmasterVoList = peopleWebmasterMapper.getWebmasterVoList(peopleOrganizationVo.getId());
                    peopleOrganizationVo.setWebmasterVoList(webmasterVoList);
                }
            });

            peopleOrganizationTree.setList(peopleOrganizationVoList);
        });
        return list;
    }

    public Page<Map<String,Object>> getLiatPage(){
        Page page = LayuiPageFactory.defaultPage();
        return super.mapper.getLiatPage(page);

    }
}
