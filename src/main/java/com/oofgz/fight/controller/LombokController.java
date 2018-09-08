package com.oofgz.fight.controller;

import com.oofgz.fight.dto.lombok.LombokUser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Lombok")
public class LombokController {

    @ApiOperation(value = "测试Lombok的用法", notes = "Spring Boot以及Feign使用Java 8中LocalDate等时间日期类的问题解决")
    @ApiImplicitParam(name = "lombokUser", value = "LombokUser实体", required = true, dataType = "LombokUser")
    @RequestMapping(value = "/getLombokUser", method = RequestMethod.POST)
    public LombokUser getLombokUser(@RequestBody LombokUser lombokUser) {
        //lombokUser.setUserName("zfgican");
        return lombokUser;
    }

    /**
     * com.fasterxml.jackson.datatype:jackson-datatype-jsr310
     * 注：在设置了spring boot的parent的情况下不需要指定具体的版本，也不建议指定某个具体版本
     * 应用主类中增加这个序列化模块，并禁用对日期以时间戳方式输出的特性
     * @return ObjectMapper
     */
    /*@Bean
    public ObjectMapper serializingObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }*/

}
