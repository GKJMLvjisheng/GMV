package com.cascv.oas.server.energy.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@ToString
public class EnergyBallWithTime implements Serializable {
    private static final long serialVersionUID = 1L;
    @Getter @Setter private int id;
    @Getter @Setter private String time;

    public EnergyBallWithTime(int id, String time) {
        this.id = id;
        this.time = time;
    }

    public EnergyBallWithTime() {}
}
