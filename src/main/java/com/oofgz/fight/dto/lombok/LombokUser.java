package com.oofgz.fight.dto.lombok;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
public class LombokUser implements Serializable {

    @Setter
    @Getter
    private String userName;

    @Setter
    @Getter
    private LocalDate birthday;

}
