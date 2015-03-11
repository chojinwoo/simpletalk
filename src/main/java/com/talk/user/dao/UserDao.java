package com.talk.user.dao;

import com.talk.user.vo.UserVo;

import java.util.List;

/**
 * Created by psn14020 on 2015-03-11.
 */
public interface UserDao {
    public String register(UserVo userVo);
    public UserVo usersByUsernameQuery(String id);
    public List authoritiesByUsernameQuery(String id);
}
