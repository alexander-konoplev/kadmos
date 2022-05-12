package io.kadmos.savingsservice.service;

import static io.kadmos.savingsservice.storage.BalanceRepository.BALANCE_ID;

import io.kadmos.savingsservice.storage.BalanceRepository;
import io.kadmos.savingsservice.storage.dto.BalanceEntity;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BalanceService {
  private final BalanceRepository balanceRepository;

  public BalanceService(BalanceRepository balanceRepository) {
    this.balanceRepository = balanceRepository;
  }

  @PostConstruct
  public void init() {
    initBalanceIfEmpty();
  }

  public BigDecimal getBalance() {
    return balanceRepository.findById(BALANCE_ID).map(BalanceEntity::getAmount).orElseThrow();
  }

  @Transactional
  public void updateBalance(BigDecimal bigDecimal) {
    balanceRepository.updateBalance(bigDecimal);
  }

  private void initBalanceIfEmpty() {
    if (balanceRepository.count() == 0) {
      var balanceEntity = new BalanceEntity();
      balanceEntity.setId(BALANCE_ID);
      balanceEntity.setAmount(BigDecimal.ZERO);
      balanceRepository.save(balanceEntity);
    }
  }
}
