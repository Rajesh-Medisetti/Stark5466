package com.joveo.eqrtestsdk.models;

import java.util.List;
import javax.validation.Valid;

public class GroupingJobFilter implements Filter {

  private GroupOperator operator;
  @Valid private List<Filter> rules;

  public GroupingJobFilter(GroupOperator operator, @Valid List<Filter> rules) {
    this.operator = operator;
    this.rules = rules;
  }

  public GroupOperator getOperator() {
    return operator;
  }

  public void setOperator(GroupOperator operator) {
    this.operator = operator;
  }

  public List<Filter> getRules() {
    return rules;
  }

  public void setRules(List<Filter> rules) {
    this.rules = rules;
  }
}
