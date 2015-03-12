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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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