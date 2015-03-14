package com.talk.chat.service;

import com.talk.chat.dao.ChatDao;
import com.talk.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.Principal;

/**
 * Created by psn14020 on 2015-03-11.
 */
@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatDao chatDao;

    @Override
    public void remove() {
        this.chatDao.remove();
    }

    @Override
    public void rooms(String id, Model model) {
        this.chatDao.rooms(id, model);
    }

    @Override
    public String createRm(String id, String to) {
        return this.chatDao.createRm(id, to);
    }

    @Override
    public String roomMsg(String id, String roomname) {
        return this.chatDao.roomMsg(id, roomname);
    }

    @Override
    public String roomList(String id) {
        return this.chatDao.roomList(id);
    }

    @Override
    public String message(String id, String message) {
        return this.chatDao.message(id, message);
    }

    @Override
    public String syncChatList(String id, String message) {
        return this.chatDao.syncChatList(id, message);
    }

    @Override
    public void messageChk(String id, String to) {
        this.chatDao.messageChk(id, to);
    }

    @Override
    public String allWriterSync(String from) {
        return this.chatDao.allWriterSync(from);
    }

    @Scheduled(cron="* * 4 L * ?")
    public void remover() {
        System.out.println("asdf");
        this.chatDao.remove();
    }
}
