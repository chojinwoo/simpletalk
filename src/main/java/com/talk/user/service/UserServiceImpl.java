package com.talk.user.service;

import com.talk.user.dao.UserDao;
import com.talk.user.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by psn14020 on 2015-03-11.
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public String register(UserVo userVo) {
        return this.userDao.register(userVo);
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        UserVo userVo = this.userDao.usersByUsernameQuery(id);
        List<HashMap> list = this.userDao.authoritiesByUsernameQuery(id);
        HashSet<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        for(Map map : list) {
            final String role = (String) map.get("role");
            authorities.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return role;
                }
            });
        }
        userVo.setAuthorities(authorities);
        return userVo;
    }
}
