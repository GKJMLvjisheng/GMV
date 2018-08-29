package com.cascv.oas.server.energy.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@ToString
public class EnergyBallWithTime implements Serializable {
    private static final long serialVersionUID = 1L;
    @Getter @Setter private String userUuid;
    @Getter @Setter private String time;

    public EnergyBallWithTime(String userUuid, String time) {
        this.userUuid = userUuid;
        this.time = time;
    }

    public EnergyBallWithTime() {}
}
