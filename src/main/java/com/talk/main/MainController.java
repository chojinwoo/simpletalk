package com.talk.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by psn14020 on 2015-03-11.
 */
@Controller
public class MainController {
    @RequestMapping(value="/")
    public String main() {
        return "login";
    }

    @RequestMapping(value="/mobile", method = RequestMethod.POST)
    public String mobile(@RequestParam("regId")String regId, @RequestParam("phoneNum")String phoneNum, Model model) {
        model.addAttribute("regId", regId);
        model.addAttribute("phoneNum", phoneNum);
        return "login";
    }

    @RequestMapping(value="/test", method = RequestMethod.GET)
    public String test(@RequestParam("regId")String regId, @RequestParam("phoneNum")String phoneNum, Model model) {
        model.addAttribute("regId", regId);
        model.addAttribute("phoneNum", phoneNum);
        return "login";
    }

}
