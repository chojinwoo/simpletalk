package com.talk.chat.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by psn14020 on 2015-03-12.
 */
public class test {
    public static void main(String[] args) {
        String rootPath = new test().getClass().getResource("/").getPath();
        SimpleDateFormat sdf = new SimpleDateFormat("yMd");
        File dir = new File(rootPath + File.separator + "upload_"+sdf.format(new Date()));
        dir.mkdir();
    }
}
