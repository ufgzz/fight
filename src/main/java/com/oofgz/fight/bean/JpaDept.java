package com.oofgz.fight.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class JpaDept {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @NonNull
    private String name;

    @NonNull
    private String description;

    @Column(nullable = false)
    @NonNull
    private Integer num;


}
