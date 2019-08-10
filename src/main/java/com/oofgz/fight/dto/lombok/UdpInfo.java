package com.oofgz.fight.dto.lombok;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class UdpInfo implements Serializable {

    @Setter
    @Getter
    private String udpContent;

}
