package cn.px.sys.modular.doubleReport.service;

import cn.px.base.Constants;
import cn.px.base.core.BaseServiceImpl;
import cn.px.base.util.ExcelUploadUtil;
import cn.px.base.util.ExportExcelUtil;
import cn.px.base.util.UUIDUtil;
import cn.px.sys.modular.communityClass.entity.CommunityClassEntity;
import cn.px.sys.modular.communityClass.mapper.CommunityClassMapper;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.sys.modular.communityClass.entity.CommunityClassEntity;
import cn.px.sys.modular.communityClass.service.CommunityClassService;
import cn.px.sys.modular.doubleReport.constant.IsBindEnum;
import cn.px.sys.modular.doubleReport.entity.ReportCadreEntity;
import cn.px.sys.modular.doubleReport.entity.UnitEntity;
import cn.px.sys.modular.doubleReport.mapper.ReportCadreMapper;
import cn.px.sys.modular.doubleReport.mapper.UnitMapper;
import cn.px.sys.modular.doubleReport.vo.UserVo;
import cn.px.sys.modular.homeClass.entity.HomeClassEntity;
import cn.px.sys.modular.homeClass.mapper.HomeClassMapper;
import cn.px.sys.modular.member.entity.MemberEntity;
import cn.px.sys.modular.member.mapper.MemberMapper;
import cn.px.sys.modular.member.service.MemberService;
import cn.px.sys.modular.wx.entity.AllUserEntity;
import cn.px.sys.modular.wx.service.AllUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ????????????????????????(ReportCadre)??????????????????
 *
 * @author
 * @since 2020-08-28 14:10:47
 */
@Service("reportCadreService")
@CacheConfig(cacheNames = Constants.ENTITY_CACHE_NAMESPACE + "reportCadre")
public class ReportCadreService extends BaseServiceImpl<ReportCadreEntity, ReportCadreMapper> {

    private final static String excel2003 = ".xls";
    private final static String excel2007 = ".xlsx";
    @Resource
    private AllUserService allUserService;
    @Resource
    private ReportCadreService reportCadreService;
    @Resource
    private UnitMapper unitMapper;
    @Resource
    private UnitService unitService;
    @Resource
    private MemberService memberService;
    @Resource
    private CommunityClassService communityClassService;

    @Resource
    private HomeClassMapper homeClassMapper;
    @Resource
    private CommunityClassMapper communityClassMapper;
    @Resource
    private ReportCadreMapper reportCadreMapper;
    @Resource
    private MemberMapper memberMapper;
    /**
     * @param in
     * @param fileName ???????????????excel??????
     */
    public List<List<Object>> getBankListByExcel(InputStream in, String fileName) throws Exception {
        List<List<Object>> list = null;
        //??????Excel?????????
        Workbook work = this.getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("??????Excel??????????????????");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        list = new ArrayList<List<Object>>();
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }

            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if (row == null || row.getFirstCellNum() == j) {
                    continue;
                }

                List<Object> li = new ArrayList<Object>();
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    li.add(cell);
                }
            }
        }
        return list;
    }
     /**
     * @param file
     * ???????????????excel??????
     *
     * */
    public  List<ReportCadreEntity> getBankListByExcel(MultipartFile file) throws Exception{
        List<ReportCadreEntity> list = null;
        String title[] = {"name", "phone", "birthday", "belongingCommunity", "belongingUnit", "belongingHome", "address"};
        List<Map<String, Object>> excelData = ExcelUploadUtil.readExcelByMultipartFile(file, title, 2, 1);
        List<UnitEntity> unitList = unitMapper.selectList(new QueryWrapper<UnitEntity>());
        List<HomeClassEntity> homeList = homeClassMapper.selectList(new QueryWrapper<HomeClassEntity>());
        List<CommunityClassEntity> classList = communityClassMapper.selectList(new QueryWrapper<CommunityClassEntity>());
        List<ReportCadreEntity> result = new ArrayList<ReportCadreEntity>();
        Date now = new Date();
        for(Map<String, Object> map : excelData){
            ReportCadreEntity entity = new ReportCadreEntity();
            Long cadreId = UUIDUtil.getOrderIdByUUId();
            entity.setId(cadreId);
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
            entity.setEnable(Integer.valueOf(1));
            if(map.get("name") != null ){
                entity.setName(String.valueOf(map.get("name")));
            }
            if(map.get("phone") != null ){
                entity.setPhone(String.valueOf(map.get("phone")));
            }
            for(CommunityClassEntity classEntity : classList){
                if(map.get("belongingCommunity") != null && String.valueOf(map.get("belongingCommunity")).trim().equals(classEntity.getName())){
                    entity.setBelongingCommunity(classEntity.getId());
                }
            }
            for(HomeClassEntity homeEntity : homeList){
                if(map.get("belongingHome") != null && String.valueOf(map.get("belongingHome")).trim().equals(homeEntity.getName())){
                    entity.setBelongingHome(homeEntity.getId());
                }
            }
            for(UnitEntity unintEntity : unitList){
                if(map.get("belongingUnit") != null && String.valueOf(map.get("belongingUnit")).trim().equals(unintEntity.getName())){
                    entity.setBelongingUnit(unintEntity.getId());
                }
            }
            result.add(entity);
        }
        return result;
    }

    //??????excel???????????????
    public Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (excel2003.equals(fileType)) {
            wb = new HSSFWorkbook(inStr);
        } else if (excel2007.equals(fileType)) {
            wb = new XSSFWorkbook(inStr);
        } else {
            throw new Exception("??????????????????????????????");
        }
        return wb;
    }


    public int insert(ReportCadreEntity reportCadreEntity) {
        return super.mapper.insert(reportCadreEntity);
    }


    /**
     * ????????????
     *
     * @return
     */
    @Transactional
    public int getIsBind(UserVo userVo, Long userId) {
        int type = userVo.getType();

        switch (type) {
            //????????????
            case 1:
                AllUserEntity allUserEntity2 = new AllUserEntity();
                allUserEntity2.setId(userId);
                allUserEntity2.setPhone(userVo.getPhone());
                allUserEntity2.setType(userVo.getType());
                allUserService.update(allUserEntity2);
                MemberEntity memberEntity = new MemberEntity();
                memberEntity.setName(userVo.getName());
                memberEntity.setAddress(userVo.getAddress());
                memberEntity.setBelongingCommunity(userVo.getBelongingCommunity());
                memberEntity.setBelongingHome(userVo.getBelongingHome());
                memberEntity.setBelongingUnit(userVo.getBelongingUnit());
                memberEntity.setIdNumber(userVo.getIdNumber());
                memberEntity.setPhone(userVo.getPhone());
                memberEntity.setUserId(userId);
                memberEntity.setIsBind(IsBindEnum.IS_BIND_TRUE.getValue());
                if(memberMapper.getMemberEntityByPhone(userId) != null){
                    memberEntity.setId(memberMapper.getMemberEntityByPhone(userId).getId());
                }
                memberService.update(memberEntity);
                break;
            //???????????????
            case 2:
                AllUserEntity allUserEntity = new AllUserEntity();
                allUserEntity.setId(userId);
                allUserEntity.setPhone(userVo.getPhone());
                allUserEntity.setType(userVo.getType());
                allUserService.update(allUserEntity);
                ReportCadreEntity reportCadreEntity = super.mapper.getReportCadreEntityByPhone(userVo.getPhone());
                 if (reportCadreEntity != null) {
                     reportCadreEntity.setName(userVo.getName());
                     reportCadreEntity.setUserId(userId);
                     reportCadreEntity.setBelongingCommunity(userVo.getBelongingCommunity());
                     reportCadreEntity.setBelongingHome(userVo.getBelongingHome());
                     reportCadreEntity.setBelongingUnit(userVo.getBelongingUnit());
                     reportCadreEntity.setIsBind(IsBindEnum.IS_BIND_TRUE.getValue());
                     reportCadreEntity.setAddress(userVo.getAddress());
                    /* if(super.mapper.getReportCadreEntityByUserId(userId) != null){
                         reportCadreEntity.setId(super.mapper.getReportCadreEntityByUserId(userId).getId());
                     }*/
                     reportCadreService.update(reportCadreEntity);
                     break;
                 }else {
                     return 1;
                 }
            //??????
            case 3:
                AllUserEntity allUserEntity1 = new AllUserEntity();
                allUserEntity1.setId(userId);
                allUserEntity1.setPhone(userVo.getPhone());
                allUserEntity1.setType(userVo.getType());
                allUserService.update(allUserEntity1);
                UnitEntity unitEntity = unitMapper.getUnitEntityByPhone(userVo.getPhone());
                  if (unitEntity != null) {
                      unitEntity.setName(userVo.getName());
                      unitEntity.setUserId(userId);
                      unitEntity.setBelongingCommunity(userVo.getBelongingCommunity());
                     /* if(unitMapper.getUnitEntityByUserId(userId) != null){
                          unitEntity.setId(unitMapper.getUnitEntityByUserId(userId).getId());
                      }*/
                      unitService.update(unitEntity);
                      break;
                  }else {
                      return 2;
                  }

        }
      return 0;
    }

    /**
     * ????????????????????????
     * @param
     * @param name
     * @param phone
     * @param
     * @return
     */
    public Page<Map<String,Object>> getListHome(String name, String phone,Long userId,Long belongingCommunity ,Long belongingUnit){
        Page page = LayuiPageFactory.defaultPage();
        Long cid;
        if(userId == 1){
            cid = null;
        }else {
            CommunityClassEntity communityClassEntity = communityClassService.getCommunityClassEntityByUserId(userId);
            cid =  communityClassEntity.getId();
        }
        return super.mapper.getListHome(page,name,phone,cid,belongingCommunity,belongingUnit);
    }
    public Workbook createWorkbook(String name, String phone, String cid){
        List<Map<String, Object>> list = reportCadreMapper.exportListData(name, phone, cid);
        String titles[] = {"name","phone","integral","communityName","unitName","homeName", "idNumber"};
        String titleNames[] = {"??????","??????","??????","????????????","????????????","????????????", "????????????"};
        Workbook wb = ExportExcelUtil.createWorkbook(list, titles, titleNames);
        return wb;
    }
}