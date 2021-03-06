/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.px.sys.modular.system.controller;

import cn.px.base.auth.context.LoginContextHolder;
import cn.px.base.auth.exception.AuthException;
import cn.px.base.auth.exception.enums.AuthExceptionEnum;
import cn.px.base.auth.jwt.JwtTokenUtil;
import cn.px.base.auth.jwt.payload.JwtPayLoad;
import cn.px.base.auth.model.LoginUser;
import cn.px.sys.core.util.DefaultImages;
import cn.px.sys.modular.system.service.UserService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static cn.px.base.consts.ConstantsContext.getJwtSecretExpireSec;
import static cn.px.base.consts.ConstantsContext.getTokenHeaderName;

/**
 * ?????????????????????
 *
 * @author fengshuonan
 * @Date 2017???1???10??? ??????8:25:24
 */
@Controller
public class SsoLoginController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * ??????????????????
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:42 PM
     */
    @RequestMapping(value = "/ssoLogin", method = RequestMethod.GET)
    public String ssoLogin(@RequestParam("token") String token, HttpServletResponse response, Model model) {

        //???????????????token
        Claims claimFromToken = JwtTokenUtil.getClaimFromToken(token);
        System.out.println(claimFromToken);
        Long accountId = (Long) claimFromToken.get("accountId");
        System.out.println(accountId);

        //??????????????????
        //LogManager.me().executeLog(LogTaskFactory.loginLog(Long.valueOf(accountId), getIp()));

        String token1 = JwtTokenUtil.generateToken(new JwtPayLoad(1L, "admin", "1"));

        //??????token
        Cookie authorization = new Cookie(getTokenHeaderName(), token1);
        authorization.setMaxAge(getJwtSecretExpireSec().intValue());
        authorization.setHttpOnly(true);
        response.addCookie(authorization);

        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername("admin");
        } catch (UsernameNotFoundException e) {
            throw new AuthException(AuthExceptionEnum.LOGIN_EXPPIRED);
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //??????????????????????????????
        LoginUser user = LoginContextHolder.getContext().getUser();
        List<Long> roleList = user.getRoleList();

        if (roleList == null || roleList.size() == 0) {
            model.addAttribute("tips", "????????????????????????????????????");
            return "/login.html";
        }

        List<Map<String, Object>> menus = userService.getUserMenuNodes(roleList);
        model.addAttribute("menus", menus);
        model.addAttribute("avatar", DefaultImages.defaultAvatarUrl());
        model.addAttribute("name", user.getName());

        return "/index.html";
    }

    /**
     * ??????????????????
     *
     * @author fengshuonan
     * @Date 2018/12/23 5:42 PM
     */
    @RequestMapping(value = "/ssoLogout")
    public String ssoLogout(@RequestParam("token") String token, HttpServletResponse response, Model model) {
        return null;
    }

}