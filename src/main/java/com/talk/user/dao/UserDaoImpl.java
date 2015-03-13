package com.talk.user.dao;

import com.talk.common.encrypto.Sha256HAsh;
import com.talk.user.vo.UserVo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by psn14020 on 2015-03-11.
 */
@Repository
public class UserDaoImpl implements UserDao {
    private SqlSessionTemplate sqlSessionTemplate;

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public String register(UserVo userVo) {
        String pw = userVo.getPassword();
        pw = Sha256HAsh.encrypt(pw);
        userVo.setPassword(pw);
        int phoneNum = Integer.parseInt(userVo.getPhoneNum());
        String phoneStr = String.format("%011d", phoneNum);
        userVo.setPhoneNum(phoneStr);
        int insertFlag = this.sqlSessionTemplate.insert("user.register", userVo);
//        int insertFlag = this.jdbcTemplate.update("insert into users(id, password, name, regid, phonenum) values(?, ?, ?, ?, ?)",
//                new Object[]{userVo.getUsername(), pw, userVo.getName(), userVo.getRegId(), phoneStr});

        return String.valueOf(insertFlag);
    }

    public UserVo usersByUsernameQuery(String id) {
        return this.sqlSessionTemplate.selectOne("user.usersByUsernameQuery", id);
//        return this.jdbcTemplate.queryForObject("SELECT id, password, name, enabled, regid FROM users WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<UserVo>(UserVo.class));
    }

    public List authoritiesByUsernameQuery(String id) {
        return this.sqlSessionTemplate.selectList("user.authoritiesByUsernameQuery", id);
//        return this.jdbcTemplate.queryForList("select b.role from users a inner join user_roles b on a.id = b.id and a.id = ?", new Object[]{id});
    }
}
