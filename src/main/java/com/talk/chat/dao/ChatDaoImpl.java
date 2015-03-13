package com.talk.chat.dao;

import com.talk.common.gcm.GcmVo;
import com.talk.common.gcm.PostGcm;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by psn14020 on 2015-03-11.
 */
@Repository
public class ChatDaoImpl implements ChatDao {
    private Map rms = new HashMap();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void rooms(String id, Model model) {
        JSONObject jo = new JSONObject();
        List<Map<String, Object>> usersList = this.jdbcTemplate.queryForList("select id from users where id <> ? order by id", new Object[]{id});
        Map usersMap = new HashMap();
        usersMap.put("users", usersList);
        jo.putAll(usersMap);
        model.addAttribute("users", jo.toString());

        Map map = (HashMap) rms.get(id);
        if(map== null) {
            rms.put(id, new HashMap());
        }
    }

    @Override
    public String createRm(String id, String to) {
        roomSave(id, to);
        JSONObject jo = new JSONObject(rms);
        return jo.toJSONString();
    }

    @Override
    public String roomMsg(String id, String roomname) {
        HashMap rmList = (HashMap) rms.get(id);
        HashMap rm = (HashMap) rmList.get(roomname);
        JSONObject jo = new JSONObject();
        if(rm != null) {
            jo = new JSONObject(rm);
        }
        return jo.toJSONString();
    }

    @Override
    public String roomList(String id) {
        HashMap rmList = (HashMap) rms.get(id);
        JSONObject jo = new JSONObject();
        if(rmList != null) {
            jo = new JSONObject(rmList);
        }
        return jo.toString();
    }

    @Override
    public String message(String id, String message) {
        JSONParser jp = new JSONParser();
        JSONObject jo = null;
        try {
            jo = (JSONObject) jp.parse(message);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*시간 및 플래그 주입*/

        TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
        SimpleDateFormat sdf = new SimpleDateFormat("a h:m");
        sdf.setTimeZone(tz);
        String time = sdf.format(new Date());
        sdf = new SimpleDateFormat("M월 d일");
        sdf.setTimeZone(tz);
        String date = sdf.format(new Date());
        jo.put("date", date);
        jo.put("time", time);
        jo.put("flag", "1");
        sendGcm(jo);

        roomSave(id, (String) jo.get("to"));
        msgFromSave(jo);
        msgToSave(jo);
        return jo.toString();
    }

    @Override
    public String syncChatList(String id, String message) {
        JSONParser jp = new JSONParser();
        JSONObject parseJp = null;
        try {
            parseJp = (JSONObject) jp.parse(message);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String to = (String) parseJp.get("to");
        String chatStayFlag = (String) parseJp.get("chatStayFlag");

        HashMap rmList = (HashMap) rms.get(id);
        HashMap rm = (HashMap) rmList.get(to);
        List msgLIst = (List) rm.get("msg");
        int msgListLength = msgLIst.size() - 1;
        HashMap msg = (HashMap) msgLIst.get(msgListLength);
        msg.put("flag", chatStayFlag);
        msgLIst.remove(msgListLength);
        msgLIst.add(msg);
        rm.put("msg", msgLIst);
        rmList.put(to, rm);
        rms.put(id, rmList);

        JSONObject jo = new JSONObject();
        if(rmList != null) {
            jo = new JSONObject(rmList);
        }
        return jo.toString();
    }

    @Override
    public void messageChk(String id, String to) {
        HashMap rmList = (HashMap) rms.get(id);
        HashMap rm = (HashMap) rmList.get(to);
        if(rm != null) {
            List msgList = (List) rm.get("msg");
            Iterator iter = msgList.iterator();
            msgList = new LinkedList();
            while (iter.hasNext()) {
                HashMap msg = (HashMap) iter.next();
                msg.put("flag", 0);
                msgList.add(msg);
            }
            rm.put("msg", msgList);
            rmList.put(to, rm);
            rms.put(id, rmList);
        }
    }

    public void sendGcm(JSONObject jo) {
        String to = (String) jo.get("to");
        String msg1 = (String) jo.get("from");
        String msg2 = (String) jo.get("message");System.out.println(to);
        Map map = this.jdbcTemplate.queryForMap("select regid from users where id = ?", new Object[]{to});
        String regId = (String) map.get("regId");
//        System.out.println("regId null chk : " + regId == null);
//        System.out.println("regId empty chk : " + );

        String apiKey = "AIzaSyADK_frqsKz8Jb_SEgpkcvsGkIC9561LII";
        GcmVo gcmVo = new GcmVo();

        if(regId != "" && (!regId.equals(""))) {
            gcmVo.addRegId(regId);
            gcmVo.createData(msg1, msg2);
            PostGcm.post(apiKey, gcmVo);
        }


    }

    public void msgFromSave(JSONObject message) {
        String from = (String) message.get("from");
        String to = (String) message.get("to");
        String emoticon = (String) message.get("emoticon");
        String msg = (String) message.get("message");
        String date = (String) message.get("date");
        String time = (String) message.get("time");
        String flag = (String) message.get("flag");

        Map rmList = (HashMap) rms.get(from);
        Map rm = (HashMap) rmList.get(to);
        List msgList= (List) rm.get("msg");
        Map msgMap = new HashMap();
        msgMap.put("name" , from);
        msgMap.put("message", msg);
        msgMap.put("date", date);
        msgMap.put("time", time);
        msgMap.put("flag", "0");
        msgMap.put("emoticon", emoticon);
        msgList.add(msgMap);
        rm.put("msg", msgList);
        rm.put("this", from);
        rmList.put(to, rm);
        rms.put(from, rmList);
//        System.out.println(rms.toString());
    }

    public void msgToSave(JSONObject message) {
        String from = (String) message.get("from");
        String to = (String) message.get("to");
        String emoticon = (String) message.get("emoticon");
        String msg = (String) message.get("message");
        String date = (String) message.get("date");
        String time = (String) message.get("time");
        String flag = (String) message.get("flag");

        Map rmList = (HashMap) rms.get(to);
        Map rm = (HashMap) rmList.get(from);
        List msgList= (List) rm.get("msg");
        Map msgMap = new HashMap();
        msgMap.put("name" , from);
        msgMap.put("message", msg);
        msgMap.put("date", date);
        msgMap.put("time", time);
        msgMap.put("flag", flag);
        msgMap.put("emoticon", emoticon);
        msgList.add(msgMap);
        rm.put("msg", msgList);
        rm.put("this", to);
        rmList.put(from, rm);
        rms.put(to, rmList);
//        System.out.println(rms.toString());
    }


    public void roomSave(String id, String to) {
        Map rmsList = (HashMap) rms.get(id);
        Map msg = new HashMap();
        try {
            if(rmsList.get(to) == null) {
                msg.put("msg", new LinkedList());
                rmsList.put(to, msg);
                rms.put(id, rmsList);
            }
        } catch(Exception e) {
            msg.put("msg", new LinkedList());
            rmsList.put(to, msg);
            rms.put(id, rmsList);
        }

        msg = new HashMap();
        try {
            rmsList = (HashMap) rms.get(to);
            if(rmsList.get(id) == null) {
                msg.put("msg", new LinkedList());
                rmsList.put(id, msg);
                rms.put(to, rmsList);
            }
        } catch(Exception e) {
            msg.put("msg", new LinkedList());
            rmsList = new HashMap();
            rmsList.put(id, msg);
            rms.put(to, rmsList);
        }
//        System.out.println("roomsave : " + rms.toString());
    }
}
