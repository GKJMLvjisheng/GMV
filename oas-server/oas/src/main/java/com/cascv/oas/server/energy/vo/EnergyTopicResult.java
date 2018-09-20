package com.cascv.oas.server.energy.vo;

import java.io.Serializable;
import java.util.List;
import com.cascv.oas.server.energy.model.QAModel.EnergyChoice;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class EnergyTopicResult  implements Serializable {
  private static final long serialVersionUID = 1L;
  @Getter @Setter private Integer questionId;
  @Getter @Setter private Integer questionDone;
  @Getter @Setter private String questionContent;
  @Getter @Setter private String created;
  @Getter @Setter private List<EnergyChoice> answers;
}
