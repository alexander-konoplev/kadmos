package io.kadmos.savingsservice.storage.dto;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "balances")
@Data
public class BalanceEntity {
  @Id
  private int id;

  private BigDecimal amount;

}
