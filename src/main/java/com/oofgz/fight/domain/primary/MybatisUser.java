package com.oofgz.fight.domain.primary;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class MybatisUser {

    private Long id;

    @NonNull
    private String username;

    @NonNull
    private Integer age;


}
