package com.oofgz.fight.domain.primary;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class JpaDept implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
