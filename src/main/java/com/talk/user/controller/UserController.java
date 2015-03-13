package com.talk.user.controller;

import com.talk.common.encrypto.Sha256HAsh;
import com.talk.user.service.UserService;
import com.talk.user.vo.UserVo;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by psn14020 on 2015-03-11.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value="/register", method= RequestMethod.POST)
    @ResponseBody
    public String register(@ModelAttribute("command")UserVo userVo) {

        String flag = this.userService.register(userVo);

        return flag;
    }
}
