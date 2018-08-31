package com.oofgz.fight.controller;

import com.oofgz.fight.bean.User;
import com.oofgz.fight.service.IUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping(value = "/User")
public class UserController {

    @Autowired
    private IUserService userService;

    private static Map<String, User> userMap = Collections.synchronizedMap(new HashMap<>());

    @ApiOperation(value = "获取用户列表", notes = "获取所有用户信息列表")
    @RequestMapping(value = {"", "/getUserList"}, method = RequestMethod.GET)
    public List<User> getUserList() {
        List<User> userListI = userService.getAllUsers();
        if (userListI.size() > 0) {
            userMap.clear();
            for (User u : userListI) {
                userMap.put(u.getName(), u);
            }
            return userListI;
        } else {
            List<User> userListM = new ArrayList<>(userMap.values());
            return userListM;
        }
    }

    @ApiOperation(value = "创建用户", notes = "根据User对象创建用户")
    @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User")
    @RequestMapping(value = {"", "/postUser"}, method = RequestMethod.POST)
    public String postUser(@RequestBody User user) {
        userMap.put(user.getName(), user);
        //保存到数据库
        userService.create(user.getName(), user.getAge(), user.getPhone(), user.getProfession());
        return "add success";
    }

    @ApiOperation(value = "获取用户详细信息", notes = "根据url的id来获取用户详细信息")
    @ApiImplicitParam(name ="name", value = "用户名称", required = true, paramType = "path")
    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public User getUserByID(@PathVariable String name) {
        User userM = userMap.get(name);
        return userM;
    }

    @ApiOperation(value = "更新用户详细信息", notes = "根据url的id来指定更新对象，并根据传过来的user信息来更新用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名称", required = true, paramType = "path"),
            @ApiImplicitParam(name = "user", value = "用户实体user", required = true, dataType = "User")
    })
    @RequestMapping(value = "/{name}", method = RequestMethod.PUT)
    public String putUser(@PathVariable String name, @RequestBody User user) {
        User u = userMap.get(name);
        u.setName(user.getName());
        u.setAge(user.getAge());
        userMap.put(name, u);
        userService.updateUserByName(name, user.getAge(), user.getPhone(), user.getProfession());
        return "put update success";
    }

    @ApiOperation(value = "删除用户", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(name = "name", value = "用户名称", required = true, paramType = "path")
    @RequestMapping(value = "/{name}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable String name) {
        userMap.remove(name);
        userService.deleteByName(name);
        return "delete success";
    }

}
