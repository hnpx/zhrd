package cn.px.sys.modular.system.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.px.base.auth.context.LoginContextHolder;
import cn.px.base.auth.model.LoginUser;
import cn.px.base.oauth2.entity.OauthUserInfo;
import cn.px.base.oauth2.service.OauthUserInfoService;
import cn.px.base.pojo.node.MenuNode;
import cn.px.base.pojo.page.LayuiPageFactory;
import cn.px.sys.core.constant.Const;
import cn.px.sys.core.constant.factory.ConstantFactory;
import cn.px.sys.core.constant.state.ManagerStatus;
import cn.px.sys.core.exception.enums.BizExceptionEnum;
import cn.px.sys.core.util.DefaultImages;
import cn.px.sys.core.util.SaltUtil;
import cn.px.sys.modular.system.entity.*;
import cn.px.sys.modular.system.factory.UserFactory;
import cn.px.sys.modular.system.mapper.UserMapper;
import cn.px.sys.modular.system.model.UserDto;
import cn.px.sys.modular.system.util.GetOrInsetrSqlutil;
import cn.px.sys.modular.system.util.HttpRequest;
import cn.px.sys.modular.system.vo.*;
import cn.stylefeng.roses.core.datascope.DataScope;
import cn.stylefeng.roses.core.util.SpringContextHolder;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * ???????????? ???????????????
 * </p>
 *
 * @author PXHLT
 * @since 2018-12-07
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private MenuService menuService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserPosService userPosService;
    @Autowired
    private SysConfigSystemService sysConfigService;

    /**
     * ????????????
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:51
     */
    @Caching(evict = {@CacheEvict(value = CacheKeyConstant.ROLE_USER_SELECTOR, allEntries = true),
            @CacheEvict(value = CacheKeyConstant.DEPT_USER_SELECTOR, allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserDto user) {

        // ????????????????????????
        User theUser = this.getByAccount(user.getAccount());
        if (theUser != null) {
            throw new ServiceException(BizExceptionEnum.USER_ALREADY_REG);
        }

        try {
            String s = HttpRequest.sendGet(GetOrInsetrSqlutil.zhyl_Url(), "password=" + user.getPassword() + "");
            GetOrInsetrSqlutil.getInsetrSqlutil("insert into sys_user(username,password,phone) values('" + user.getAccount() + "','" + s + "','" + user.getPhone() + "')");
            int userid = GetOrInsetrSqlutil.getSqlutil("SELECT user_id as userId FROM sys_user WHERE username='" + user.getAccount() + "' AND del_flag=0");
            GetOrInsetrSqlutil.getInsetrSqlutil("insert into sys_user_role(user_id,role_id) values('" + userid + "','" + 2 + "')");
        } catch (Throwable throwable) {
            System.out.println(throwable);
        }

        // ??????????????????
        String salt = SaltUtil.getRandomSalt();
        String password = SaltUtil.md5Encrypt(user.getPassword(), salt);

        User newUser = UserFactory.createUser(user, password, salt);
        this.save(newUser);

        //??????????????????
        addPosition(user.getPosition(), newUser.getUserId());
    }


    /**
     * ????????????
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:53
     */
    @Caching(evict = {@CacheEvict(value = CacheKeyConstant.ROLE_USER_SELECTOR, allEntries = true),
            @CacheEvict(value = CacheKeyConstant.DEPT_USER_SELECTOR, allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public void editUser(UserDto user) {
        User oldUser = this.getById(user.getUserId());

        if (LoginContextHolder.getContext().hasRole(Const.ADMIN_NAME)) {
            this.updateById(UserFactory.editUser(user, oldUser));
        } else {
            this.assertAuth(user.getUserId());
            LoginUser shiroUser = LoginContextHolder.getContext().getUser();
            if (shiroUser.getId().equals(user.getUserId())) {
                this.updateById(UserFactory.editUser(user, oldUser));
            } else {
                throw new ServiceException(BizExceptionEnum.NO_PERMITION);
            }
        }

        //??????????????????
        userPosService.remove(new QueryWrapper<UserPos>().eq("user_id", user.getUserId()));

        //??????????????????
        addPosition(user.getPosition(), user.getUserId());
    }

    /**
     * ????????????
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:54
     */
    @Caching(evict = {@CacheEvict(value = CacheKeyConstant.ROLE_USER_SELECTOR, allEntries = true),
            @CacheEvict(value = CacheKeyConstant.DEPT_USER_SELECTOR, allEntries = true)})
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long userId) {

        //???????????????????????????
        if (userId.equals(Const.ADMIN_ID)) {
            throw new ServiceException(BizExceptionEnum.CANT_DELETE_ADMIN);
        }
        User oldUser = this.getById(userId);
        try {
            int userid = GetOrInsetrSqlutil.getSqlutil("SELECT user_id as userId FROM sys_user WHERE username='" + oldUser.getAccount() + "' AND del_flag=0");
            GetOrInsetrSqlutil.getInsetrSqlutil("update sys_user set del_flag = 1  where user_id = " + userid + " and del_flag =0");
            GetOrInsetrSqlutil.getInsetrSqlutil("delete from sys_user_role where user_id = "+userid+"");
        } catch (Throwable throwable) {
            System.out.println(throwable);
        }
        this.assertAuth(userId);
        this.setStatus(userId, ManagerStatus.DELETED.getCode());
        //???????????????oauth2?????????
        OauthUserInfoService oauthUserInfoService = null;
        try {
            oauthUserInfoService = SpringContextHolder.getBean(OauthUserInfoService.class);
            oauthUserInfoService.remove(new QueryWrapper<OauthUserInfo>().eq("user_id", userId));
        } catch (Exception e) {
            //????????????oauth2??????????????????
        }

        //??????????????????
        userPosService.remove(new QueryWrapper<UserPos>().eq("user_id", userId));

    }

    /**
     * ??????????????????
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:45
     */
    public int setStatus(Long userId, String status) {
        return this.baseMapper.setStatus(userId, status);
    }

    /**
     * ????????????
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:45
     */
    public void changePwd(String oldPassword, String newPassword) {
        Long userId = LoginContextHolder.getContext().getUser().getId();
        User user = this.getById(userId);
        String oldMd5 = SaltUtil.md5Encrypt(oldPassword, user.getSalt());
        if (user.getPassword().equals(oldMd5)) {
            try {
                String s = HttpRequest.sendGet(GetOrInsetrSqlutil.zhyl_Url(), "password=" + newPassword + "");
                GetOrInsetrSqlutil.getInsetrSqlutil("update sys_user set password = '" + s + "' where username = '" + user.getAccount() + "' and del_flag =0");
            } catch (Throwable throwable) {
                System.out.println(throwable);
            }
            String newMd5 = SaltUtil.md5Encrypt(newPassword, user.getSalt());
            user.setPassword(newMd5);
            this.updateById(user);
        } else {
            throw new ServiceException(BizExceptionEnum.OLD_PWD_NOT_RIGHT);
        }
    }

    /**
     * ??????????????????????????????
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:45
     */
    public Page<Map<String, Object>> selectUsers(DataScope dataScope, String name, String beginTime, String endTime, Long deptId) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectUsers(page, dataScope, name, beginTime, endTime, deptId);
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @return
     */
    @Cacheable(value = CacheKeyConstant.ROLE_USER_SELECTOR)
    public List<RoleUserVO> selectRoleUser() {
        List<Role> list = roleService.list();
        return list.stream().map(role -> doSelectRoleUser(role)).collect(Collectors.toList());
    }

    @Cacheable(value = CacheKeyConstant.DEPT_USER_SELECTOR)
    public DeptUserVO selectDeptUser() {
        return doSelectDeptUser(DeptService.ROOT_DEPT_ID);
    }

    private RoleUserVO doSelectRoleUser(Role role) {
        RoleVO roleVO = new RoleVO(role);
        List<User> userList = this.baseMapper.selectList(new QueryWrapper<User>().eq("role_id", role.getRoleId()).orderByDesc("create_time"));
        List<UserVO> users = userList.stream().map(user -> new UserVO(user)).collect(Collectors.toList());
        RoleUserVO vo = new RoleUserVO();
        vo.setRole(roleVO);
        vo.setUsers(users);
        return vo;
    }

    private DeptUserVO doSelectDeptUser(Long deptId) {
        Dept dept = deptService.getById(deptId);
        DeptVO deptVO = new DeptVO(dept);
        List<User> userList = this.baseMapper.selectList(new QueryWrapper<User>().eq("dept_id", deptId).orderByDesc("create_time"));
        List<UserVO> userVOS = userList.stream().map(user -> new UserVO(user)).collect(Collectors.toList());
        DeptUserVO deptUserVO = new DeptUserVO();
        deptUserVO.setDept(deptVO);
        deptUserVO.setUsers(userVOS);
        List<Dept> deptList = deptService.list(new QueryWrapper<Dept>().eq("pid", deptId).orderByAsc("sort").orderByDesc("create_time"));
        if (deptList == null || deptList.size() == 0) {
            deptUserVO.setDepts(new ArrayList<>());
            return deptUserVO;
        }
        List<DeptUserVO> collect = deptList.stream().map(subDept -> doSelectDeptUser(subDept.getDeptId())).collect(Collectors.toList());
        deptUserVO.setDepts(collect);
        return deptUserVO;
    }

    /**
     * ?????????????????????
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:45
     */
    @CacheEvict(value = CacheKeyConstant.ROLE_USER_SELECTOR, allEntries = true)
    public int setRoles(Long userId, String roleIds) {
        return this.baseMapper.setRoles(userId, roleIds);
    }

    /**
     * ????????????????????????
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:46
     */
    public User getByAccount(String account) {
        return this.baseMapper.getByAccount(account);
    }

    /**
     * ????????????????????????
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:46
     */
    public List<Map<String, Object>> getUserMenuNodes(List<Long> roleList) {
        if (roleList == null || roleList.size() == 0) {
            return new ArrayList<>();
        } else {
            List<MenuNode> menus = menuService.getMenusByRoleIds(roleList);

            //???????????????????????????????????????
            ArrayList<Map<String, Object>> lists = new ArrayList<>();

            //??????????????????????????????????????????????????????????????????
            List<Map<String, Object>> systemTypes = LoginContextHolder.getContext().getUser().getSystemTypes();
            for (Map<String, Object> systemType : systemTypes) {

                //????????????????????????code
                String systemCode = (String) systemType.get("code");

                //???????????????????????????????????????
                ArrayList<MenuNode> originSystemTypeMenus = new ArrayList<>();
                for (MenuNode menu : menus) {
                    if (menu.getSystemType().equals(systemCode)) {
                        originSystemTypeMenus.add(menu);
                    }
                }

                //????????????key????????????????????????value??????????????????????????????map
                HashMap<String, Object> map = new HashMap<>();
                List<MenuNode> treeSystemTypeMenus = MenuNode.buildTitle(originSystemTypeMenus);
                map.put("systemType", systemCode);
                map.put("child", treeSystemTypeMenus);
                map.put("target", "_self");
                map.put("title", systemType.get("name"));
                map.put("href", "");
                lists.add(map);
            }

            return lists;
        }
    }

    /**
     * ???????????????????????????????????????????????????????????????
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:44
     */
    public void assertAuth(Long userId) {
        if (LoginContextHolder.getContext().isAdmin()) {
            return;
        }
        List<Long> deptDataScope = LoginContextHolder.getContext().getDeptDataScope();
        User user = this.getById(userId);
        Long deptId = user.getDeptId();
        if (deptDataScope.contains(deptId)) {
            return;
        } else {
            throw new ServiceException(BizExceptionEnum.NO_PERMITION);
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @author fengshuonan
     * @Date 2019/1/19 5:59 PM
     */
    public void refreshCurrentUser() {
        //TODO ??????
    }

    /**
     * ???????????????????????????
     *
     * @author fengshuonan
     * @Date 2019-05-04 17:12
     */
    public Map<String, Object> getUserInfo(Long userId) {
        User user = this.getById(userId);
        Map<String, Object> map = UserFactory.removeUnSafeFields(user);

        HashMap<String, Object> hashMap = CollectionUtil.newHashMap();
        hashMap.putAll(map);
        hashMap.put("roleName", ConstantFactory.me().getRoleName(user.getRoleId()));
        hashMap.put("deptName", ConstantFactory.me().getDeptName(user.getDeptId()));

        return hashMap;
    }

    /**
     * ????????????????????????
     *
     * @author fengshuonan
     * @Date 2019/10/17 16:18
     */
    public Map<String, Object> getUserIndexInfo() {

        //??????????????????????????????
        LoginUser user = LoginContextHolder.getContext().getUser();
        List<Long> roleList = user.getRoleList();

        //??????????????????????????????????????????
        if (roleList == null || roleList.size() == 0) {
            return null;
        }

        List<Map<String, Object>> menus = this.getUserMenuNodes(roleList);

        HashMap<String, Object> result = new HashMap<>();
        result.put("menuInfo", menus);
        result.put("avatar", DefaultImages.defaultAvatarUrl());
        result.put("name", user.getName());
        SysConfigSystemEntity sysConfig = this.sysConfigService.queryById(1L);
        Map<String, String> sysConfigMap = sysConfig.getMap();
        result.put("logoInfo", sysConfigMap);
        Map<String, String> homeInfoMap = new HashMap<>();
        homeInfoMap.put("href", "");
        homeInfoMap.put("title", "??????");
        result.put("homeInfo", homeInfoMap);
        return result;
    }

    /**
     * ??????????????????
     *
     * @author fengshuonan
     * @Date 2019-06-28 13:35
     */
    private void addPosition(String positions, Long userId) {
        if (ToolUtil.isNotEmpty(positions)) {
            String[] position = positions.split(",");
            for (String item : position) {

                UserPos entity = new UserPos();
                entity.setUserId(userId);
                entity.setPosId(Long.valueOf(item));

                userPosService.save(entity);
            }
        }
    }

    /**
     * ???????????????
     *
     * @author fengshuonan
     * @Date 2019-08-27 19:07
     */
    public IPage listUserAndRoleExpectAdmin(Page pageContext) {
        return baseMapper.listUserAndRoleExpectAdmin(pageContext);
    }
}
