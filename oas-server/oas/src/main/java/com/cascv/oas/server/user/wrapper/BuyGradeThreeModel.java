package com.cascv.oas.server.user.wrapper;

import java.io.Serializable;
import java.util.List;

import com.cascv.oas.server.user.model.UserModel;

import lombok.Getter;
import lombok.Setter;

/**
 *  
 * @author Ming Yang
 * @20180923
 */
public class BuyGradeThreeModel implements Serializable{
  private static final long serialVersionUID = 1L;
  @Getter @Setter List<UserModel> userModelList;
}