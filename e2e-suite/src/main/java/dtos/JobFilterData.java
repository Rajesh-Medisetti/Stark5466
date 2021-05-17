package dtos;

import com.joveo.eqrtestsdk.core.entities.Client;
import java.util.List;
import java.util.Set;

public class JobFilterData {
  public Set<Client> getClientSet() {
    return clientSet;
  }

  public void setClientSet(Set<Client> clientSet) {
    this.clientSet = clientSet;
  }

  public List<List<Object>> getDpList() {
    return dpList;
  }

  public void setDpList(List<List<Object>> dpList) {
    this.dpList = dpList;
  }

  public boolean isIfSchedulerRan() {
    return ifSchedulerRan;
  }

  public void setIfSchedulerRan(boolean ifSchedulerRan) {
    this.ifSchedulerRan = ifSchedulerRan;
  }

  private Set<Client> clientSet;
  private List<List<Object>> dpList;
  private boolean ifSchedulerRan;

  /** . constructor */
  public JobFilterData(Set<Client> clientSet, List<List<Object>> dpList, boolean ifSchedulerRan) {
    this.clientSet = clientSet;
    this.dpList = dpList;
    this.ifSchedulerRan = ifSchedulerRan;
  }
}
