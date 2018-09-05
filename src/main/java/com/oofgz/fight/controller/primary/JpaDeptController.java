package com.oofgz.fight.controller.primary;

import com.oofgz.fight.domain.primary.JpaDept;
import com.oofgz.fight.repository.primary.JpaDeptRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/Dept")
public class JpaDeptController {

    @Autowired
    private JpaDeptRepository jpaDeptRepository;

    @ApiOperation(value = "创建部门", notes = "根据JpaDept对象创建部门")
    @ApiImplicitParam(name = "jpaDept", value = "部门详细实体jpaDept", required = true, dataType = "JpaDept")
    @RequestMapping(value = "/postJpaDept", method = RequestMethod.POST)
    public String postJpaDept(@RequestBody JpaDept jpaDept) {
        jpaDeptRepository.save(jpaDept);
        return "save success";
    }


    @ApiOperation(value = "获取部门列表", notes = "获取所有部门信息列表")
    @RequestMapping(value = "/getJpaDeptList", method = RequestMethod.GET)
    public List<JpaDept> getJpaDeptList() {
        List<JpaDept> jpaDeptList = jpaDeptRepository.findAll();
        return jpaDeptList;
    }

}
