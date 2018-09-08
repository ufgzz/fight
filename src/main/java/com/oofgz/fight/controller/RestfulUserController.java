package com.oofgz.fight.controller;

import com.oofgz.fight.dto.restful.RestfulUser;
import com.oofgz.fight.service.IUserService;
import com.oofgz.fight.util.Pinyin4jUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping(value = "/RestfulUser")
public class RestfulUserController {

    @Autowired
    private IUserService userService;

    private static Map<String, RestfulUser> userMap = Collections.synchronizedMap(new HashMap<>());

    @ApiOperation(value = "获取用户列表", notes = "获取所有用户信息列表")
    @RequestMapping(value = {"", "/getUserList"}, method = RequestMethod.GET)
    public List<RestfulUser> getUserList() {
        return queryAllOrInitAddandUpdate();
    }

    @ApiOperation(value = "创建用户", notes = "根据User对象创建用户")
    @ApiImplicitParam(name = "restfulUser", value = "用户详细实体user", required = true, dataType = "RestfulUser")
    @RequestMapping(value = {"", "/postUser"}, method = RequestMethod.POST)
    public String postUser(@RequestBody RestfulUser restfulUser) {
        restfulUser.setNameSpell(Pinyin4jUtils.converterToFirstSpell(restfulUser.getName()));
        queryAllOrInitAddandUpdate();
        if (userMap.containsKey(restfulUser.getNameSpell())) {
            RestfulUser u = userMap.get(restfulUser.getNameSpell());
            u.setName(restfulUser.getName());
            u.setAge(restfulUser.getAge());
            u.setPhone(restfulUser.getPhone());
            u.setProfession(restfulUser.getProfession());
            //更新到数据库
            userService.updateUserByNameSpell(restfulUser.getNameSpell(), restfulUser);
        } else {
            userMap.put(restfulUser.getNameSpell(), restfulUser);
            //保存到数据库
            userService.create(restfulUser);
        }
        return "add success";
    }

    @ApiOperation(value = "获取用户详细信息", notes = "根据url的用户简拼来获取用户详细信息")
    @ApiImplicitParam(name ="nameSpell", value = "用户简拼", required = true, paramType = "path")
    @RequestMapping(value = "/{nameSpell}", method = RequestMethod.GET)
    public RestfulUser getUserByNameSpell(@PathVariable String nameSpell) {
        queryAllOrInitAddandUpdate();
        RestfulUser restfulUserM = userMap.get(nameSpell);
        return restfulUserM;
    }

    @ApiOperation(value = "更新用户详细信息", notes = "根据url的用户简拼来指定更新对象，并根据传过来的user信息来更新用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nameSpell", value = "用户名称", required = true, paramType = "path"),
            @ApiImplicitParam(name = "restfulUser", value = "用户实体user", required = true, dataType = "RestfulUser")
    })
    @RequestMapping(value = "/{nameSpell}", method = RequestMethod.PUT)
    public String putUser(@PathVariable String nameSpell, @RequestBody RestfulUser restfulUser) {
        queryAllOrInitAddandUpdate();
        RestfulUser u = userMap.get(nameSpell);
        if (u == null) {
            userMap.put(restfulUser.getNameSpell(), restfulUser);
            //保存到数据库
            userService.create(restfulUser);
        } else {
            u.setName(restfulUser.getName());
            u.setAge(restfulUser.getAge());
            u.setPhone(restfulUser.getPhone());
            u.setProfession(restfulUser.getProfession());
            userService.updateUserByNameSpell(nameSpell, u);
        }
        return "put update success";
    }

    @ApiOperation(value = "删除用户", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(name = "nameSpell", value = "用户简拼", required = true, paramType = "path")
    @RequestMapping(value = "/{nameSpell}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable String nameSpell) {
        userMap.remove(nameSpell);
        userService.deleteByNameSpell(nameSpell);
        return "delete success";
    }

    @ApiOperation(value = "删除所有用户", notes = "删除所有用户列表信息")
    @RequestMapping(value = "/deleteAllUsers", method = RequestMethod.DELETE)
    public String delettAllUsers() {
        userMap.clear();
        userService.deleteAllUsers();
        return "delete all success";
    }

    private List<RestfulUser> queryAllOrInitAddandUpdate() {
        List<RestfulUser> restfulUserListI = userService.getAllUsers();
        if (restfulUserListI.size() > 0) {
            userMap.clear();
            for (RestfulUser u : restfulUserListI) {
                userMap.put(u.getNameSpell(), u);
            }
            return restfulUserListI;
        } else {
            List<RestfulUser> restfulUserListM = new ArrayList<>(userMap.values());
            return restfulUserListM;
        }
    }
}
