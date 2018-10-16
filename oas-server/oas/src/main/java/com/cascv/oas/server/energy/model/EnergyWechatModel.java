package com.cascv.oas.server.energy.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@ToString
public class EnergyWechatModel{
    @Getter @Setter private String uuid;
    @Getter @Setter private String userUuid;
    @Getter @Setter private String wechatOpenid;
    @Getter @Setter private String created;
}