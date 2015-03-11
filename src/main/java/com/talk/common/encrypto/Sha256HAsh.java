package com.talk.common.encrypto;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by psn14020 on 2015-03-11.
 */
public class Sha256HAsh {

    public static String encrypt(String pw) {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("sha-256");
            byte[] b = md.digest(pw.getBytes("UTF-8"));

            for(int i = 0 ; i < b.length ; i++){
                sb.append(Integer.toString((b[i]&0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
