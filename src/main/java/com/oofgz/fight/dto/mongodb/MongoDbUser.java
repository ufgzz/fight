package com.oofgz.fight.dto.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Id;

@AllArgsConstructor
@Data
public class MongoDbUser {

    @Id
    private Long id;
    private String username;
    private Integer age;



}
