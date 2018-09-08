package com.oofgz.fight.service.impl;

import com.oofgz.fight.dto.user.User;
import com.oofgz.fight.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
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
    public void updateUserByName(String name, User user) {
        jdbcTemplate.update("update user set set name = ?, age = ?, phone = ?, profession = ? where name = ?", user.getName(), user.getAge(), user.getPhone(), user.getProfession(), name);
    }

    @Override
    public void deleteByName(String name) {
        jdbcTemplate.update("delete from user where name = ?", name);
    }

    @Override
    public void deleteAllUsers() {
        jdbcTemplate.update("delete from user");
    }
}
