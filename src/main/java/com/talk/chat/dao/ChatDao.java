package com.talk.chat.dao;

import org.springframework.ui.Model;

import java.security.Principal;

/**
 * Created by psn14020 on 2015-03-11.
 */
public interface ChatDao {
    public void rooms(String id, Model model);
    public String createRm(String id, String to);
    public String roomMsg(String id, String roomname);
    public String roomList(String id);
    public String message(String id, String message);
    public String syncChatList(String id, String message);
    public void messageChk(String id, String to);
    public String allWriterSync(String from);
}
