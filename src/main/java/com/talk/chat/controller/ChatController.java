package com.talk.chat.controller;

import com.talk.chat.service.ChatService;
import com.talk.common.gcm.GcmVo;
import com.talk.common.gcm.PostGcm;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jinwoo on 2015-03-07.
 */
@Controller
public class ChatController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private Map rms = new HashMap();
    @Autowired
    private ChatService chatService;

    @RequestMapping(value="/remove", method=RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String remove() {
        this.chatService.remove();
        return "redirect:/rooms";
    }

    @RequestMapping(value = "/rooms", method = RequestMethod.GET)
    public String rooms(Principal principal, Model model) {
        this.chatService.rooms(principal.getName(), model);
        return "rooms";
    }

    @RequestMapping(value = "/createRm", method = RequestMethod.POST)
    @ResponseBody
    public String createRm(Principal principal, @RequestParam("to") String to) {
        return this.chatService.createRm(principal.getName(), to);
    }

    @RequestMapping(value = "/roomMsg", method = RequestMethod.POST)
    @ResponseBody
    public String roomMsg(Principal principal, @RequestParam("roomname") String roomname) {
        return this.chatService.roomMsg(principal.getName(), roomname);
    }

    @RequestMapping(value = "/roomList", method = RequestMethod.POST)
    @ResponseBody
    public String roomList(Principal principal) {
        return this.chatService.roomList(principal.getName());
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(MultipartRequest req) throws IOException {
        MultipartFile mf = req.getFile("file");
        String rootPath = new test().getClass().getResource("/").getPath();
        SimpleDateFormat sdf = new SimpleDateFormat("yMd");
        File dir = new File(rootPath);
        String basePath = dir.getParentFile().getParentFile().getPath();
        String path = basePath + File.separator + "resources" + File.separator + "upload" + File.separator + sdf.format(new Date());
        File pathDir = new File(path);
        if(!pathDir.exists()) {
            pathDir.mkdir();
        }
        String uuid = UUID.randomUUID().toString();
        File saveFile = new File(pathDir + File.separator + uuid + "." +mf.getContentType().split("/")[1]);
        mf.transferTo(saveFile);
        return File.separator + "resources" + File.separator + "upload" + File.separator + sdf.format(new Date()) + File.separator + uuid + "." +mf.getContentType().split("/")[1];
    }

    @RequestMapping(value = "/allWriterSync", method = RequestMethod.POST)
    @ResponseBody
    public String allWriterSync(Principal principal) {
        return this.chatService.allWriterSync(principal.getName());
    }

    @MessageMapping("/message")
    @SendTo("/topic/message")
    public String message(Principal principal, String message) throws ParseException {
        return this.chatService.message(principal.getName(), message);
    }


    @MessageMapping("/syncChatList")
    @SendTo("/topic/syncChatList")
    public String syncChatList(Principal principal, @RequestParam("message") String message) throws ParseException {
        return this.chatService.syncChatList(principal.getName(), message);
    }

    @MessageMapping("/messageChk")
    public void messageChk(Principal principal, @RequestParam("to") String to) throws ParseException {
        this.chatService.messageChk(principal.getName(), to);
    }

}