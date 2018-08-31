package com.oofgz.fight.service;

import com.oofgz.fight.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void create(String name, String age, String phone, String profession) {
        jdbcTemplate.update("insert into user(name, age, phone, profession) values(?, ?, ?, ?)", name, age, phone, profession);
    }


    @Override
    public List<User> getAllUsers() {
        //queryForList，第二个参数只能是简单类型
        //return jdbcTemplate.queryForList("select name, age, phone, profession from user", User.class);
        return jdbcTemplate.query("select * from user", new Object[]{}, new BeanPropertyRowMapper<>(User.class));
    }


    @Override
    public void updateUserByName(String name, String age, String phone, String profession) {
        jdbcTemplate.update("update user set age = ?, phone = ?, profession = ? where name = ?", age, phone, profession, name);
    }

    @Override
    public void deleteByName(String name) {
        jdbcTemplate.update("delete from user where name = ?", name);
    }

}
