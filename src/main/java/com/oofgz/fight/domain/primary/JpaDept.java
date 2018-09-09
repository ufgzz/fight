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
@Table(name = "jpa_dept")
public class JpaDept implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * nullable, length只有在创建表时起作用
     * .@Column(nullable = false, length = 5)
     */
    @Column(nullable = false)
    @NonNull
    private String name;

    @NonNull
    private String description;

    @Column(nullable = false)
    @NonNull
    private Integer num;


}
