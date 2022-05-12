package io.kadmos.savingsservice.storage;


import io.kadmos.savingsservice.storage.dto.BalanceEntity;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BalanceRepository extends JpaRepository<BalanceEntity, Integer> {
  int BALANCE_ID = 0;
  @Modifying(clearAutomatically = true)
  @Query("update BalanceEntity b set b.amount = :amount where b.id = " + BALANCE_ID)
  void updateBalance(@Param("amount") BigDecimal amount);
}
