package com.cascv.oas.server.blockchain.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.cascv.oas.server.blockchain.config.MultiTransferQuota;

import lombok.Getter;
import lombok.Setter;

public class UserWalletMultiTransfer implements Serializable {
  private static final long serialVersionUID = 1L;
	@Getter @Setter private String toUserName;
    @Getter @Setter private BigDecimal value;
    @Getter @Setter private String remark;
    @Getter @Setter private String changeUserName;
    @Getter @Setter private String comment;
    @Getter @Setter private List<MultiTransferQuota> multiTransferQuota;
}
