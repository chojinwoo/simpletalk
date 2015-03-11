package com.talk.user.vo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by psn14020 on 2015-03-11.
 */
public class UserVo implements UserDetails {
    private final boolean ACCOUNTNONEXPIRED = true;
    private final boolean ACCOUNTNONLOCKED = true;
    private final boolean CREDENTIALSNONEXPIRED = true;
    private final boolean ENABLED = true;

    private String id;
    private String password;
    private String name;
    private String regId;
    private String phoneNum;
    private HashSet<GrantedAuthority> authorities;

    public UserVo() {
    }

    public UserVo(String id, String password, String name, String regId, String phoneNum, HashSet<GrantedAuthority> authorities) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.regId = regId;
        this.phoneNum = phoneNum;
        this.authorities = authorities;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public void setAuthorities(HashSet<GrantedAuthority> authorities) {
        this.authorities= authorities;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.ACCOUNTNONEXPIRED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.ACCOUNTNONLOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.CREDENTIALSNONEXPIRED;
    }

    @Override
    public boolean isEnabled() {
        return this.ENABLED;
    }
}
