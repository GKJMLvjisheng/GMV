package com.cascv.oas.server.blockchain.wrapper;

import java.io.Serializable;
import java.util.List;
import com.cascv.oas.server.blockchain.config.MultiTransferQuota;
import lombok.Getter;
import lombok.Setter;

public class UserWalletMultiTransfer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Getter @Setter private String fromName;
    @Getter @Setter private List<MultiTransferQuota> multiTransferQuota;
}
