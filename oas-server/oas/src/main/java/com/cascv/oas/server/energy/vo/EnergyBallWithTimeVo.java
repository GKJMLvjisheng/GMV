package com.cascv.oas.server.energy.vo;

import org.springframework.stereotype.Component;

@Component
public class EnergyBallWithTimeVo {
    private int id;
    private String time;

    public EnergyBallWithTimeVo(int id, String time) {
        this.id = id;
        this.time = time;
    }

    public EnergyBallWithTimeVo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    @Override
    public String toString() {
        return "EnergyBallWithTime{" +
                "id=" + id +
                ", time='" + time + '\'' +
                '}';
    }
}
