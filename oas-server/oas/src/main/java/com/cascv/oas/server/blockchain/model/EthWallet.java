package com.cascv.oas.server.blockchain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSONArray;
import lombok.Getter;
import lombok.Setter;

public class EthWallet implements Serializable {

  private static final long serialVersionUID = 1L;
  @Getter @Setter private String uuid;
  @Getter @Setter private String userUuid;
  @Getter @Setter private String privateKey;
  @Getter @Setter private String publicKey;
  @Getter @Setter private String mnemonicList;
  @Getter @Setter private String mnemonicPath;
  @Getter @Setter private String address;
  @Getter @Setter private String created;
  @Getter @Setter private String updated;
  
  
  public static String toMnemonicList(List<String> mnemonic) {
    JSONArray jsonArray = new JSONArray();
    for (String s : mnemonic) {
      jsonArray.add(s);
    }
    return jsonArray.toJSONString();
  }
  
  public static List<String> fromMnemonicList(String mnemonic){
    if (mnemonic == null) {
      return null;
    }
    JSONArray jsonArray = JSONArray.parseArray(mnemonic);
    List<String> x = new ArrayList<>();
    for (Integer i = 0; i< jsonArray.size(); i++) {
      x.add(jsonArray.getString(i));
    }
    return x;
  }

}
