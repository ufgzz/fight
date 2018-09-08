package com.oofgz.fight.repository.primary;

import com.oofgz.fight.domain.primary.MybatisUser;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface MybatisUserMapper {

    @Results({
            @Result(property = "username", column = "name"),
            @Result(property = "age", column = "age")
    })
    @Select("SELECT * FROM mybatis_user WHERE NAME = #{username}")
    MybatisUser findByName(@Param("username") String username);

    @Insert("INSERT INTO mybatis_user(NAME, AGE) VALUES(#{username}, #{age})")
    int insert(@Param("username") String username, @Param("age") Integer age);


    @Results({
            @Result(property = "username", column = "name"),
            @Result(property = "age", column = "age")
    })
    @Select("SELECT id, name, age FROM mybatis_user")
    List<MybatisUser> findAll();


    @Update("UPDATE mybatis_user SET age=#{age} WHERE name=#{username}")
    void update(MybatisUser mybatisUser);

    @Delete("DELETE FROM mybatis_user WHERE id =#{id}")
    void delete(Long id);

    @Insert("INSERT INTO mybatis_user(name, age) VALUES(#{username}, #{age})")
    int insertByUser(MybatisUser mybatisUser);

    @Insert("INSERT INTO mybatis_user(name, age) VALUES(#{username,jdbcType=VARCHAR}, #{age,jdbcType=INTEGER})")
    int insertByMap(Map<String, Object> map);


}
