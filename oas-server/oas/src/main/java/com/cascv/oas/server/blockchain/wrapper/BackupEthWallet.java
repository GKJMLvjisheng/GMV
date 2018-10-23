package com.cascv.oas.server.blockchain.wrapper;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class BackupEthWallet implements Serializable {
	private static final long serialVersionUID = 1L;
	@Getter @Setter List<String> mnemonicList;
	@Getter @Setter String privateKey;
}
