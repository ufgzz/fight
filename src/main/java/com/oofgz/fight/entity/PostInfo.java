package com.oofgz.fight.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PostInfo implements Serializable {
    private String title;
    private String content;
}
