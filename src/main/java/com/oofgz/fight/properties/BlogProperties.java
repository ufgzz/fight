package com.oofgz.fight.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class BlogProperties {

    @Value("${com.zfgoo.blog.name}")
    private String name;
    @Value("${com.zfgoo.blog.title}")
    private String title;
    @Value("${com.zfgoo.blog.desc}")
    private String desc;

    @Value("${com.zfgoo.blog.value}")
    private String value;
    @Value("${com.zfgoo.blog.number}")
    private String number;
    @Value("${com.zfgoo.blog.bignumber}")
    private String bigNumber;
    @Value("${com.zfgoo.blog.random-num-in-10}")
    private String randomNumIn10;
    @Value("${com.zfgoo.blog.random-num-between-10-20}")
    private String randomNumBetween1020;

}
