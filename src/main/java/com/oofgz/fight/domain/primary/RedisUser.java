package com.oofgz.fight.domain.primary;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Data
public class RedisUser implements Serializable {

    private static final long serialVersionUID = -1L;

    @NonNull
    private String username;

    @NonNull
    private Integer age;

}
