package com.oofgz.fight.service.impl;

import com.oofgz.fight.dto.restful.RestfulUser;
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
    public void create(RestfulUser restfulUser) {
        jdbcTemplate.update("insert into restful_user(name, nameSpell, age, phone, profession) values(?, ?, ?, ?, ?)",
                restfulUser.getName(),
                restfulUser.getNameSpell(),
                restfulUser.getAge(),
                restfulUser.getPhone(),
                restfulUser.getProfession()
        );
    }


    @Override
    public List<RestfulUser> getAllUsers() {
        //queryForList，第二个参数只能是简单类型
        //return jdbcTemplate.queryForList("select name, age, phone, profession from user", RestfulUser.class);
        return jdbcTemplate.query("select * from restful_user", new Object[]{}, new BeanPropertyRowMapper<>(RestfulUser.class));
    }


    @Override
    public void updateUserByNameSpell(String nameSpell, RestfulUser restfulUser) {
        jdbcTemplate.update("update restful_user set name = ?, age = ?, phone = ?, profession = ? where nameSpell = ?",
                restfulUser.getName(),
                restfulUser.getAge(),
                restfulUser.getPhone(),
                restfulUser.getProfession(),
                nameSpell
        );
    }


    @Override
    public void deleteByNameSpell(String nameSpell) {
        jdbcTemplate.update("delete from restful_user where nameSpell = ?", nameSpell);
    }

    @Override
    public void deleteAllUsers() {
        jdbcTemplate.update("delete from restful_user");
    }
}
