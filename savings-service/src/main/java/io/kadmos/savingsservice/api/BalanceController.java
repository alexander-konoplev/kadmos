package io.kadmos.savingsservice.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.kadmos.savingsservice.api.dto.BalanceDto;
import io.kadmos.savingsservice.service.BalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {
  private final BalanceService balanceService;

  public BalanceController(BalanceService balanceService) {
    this.balanceService = balanceService;
  }

  @GetMapping(value = "/balance", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<BalanceDto> get() {
    return ResponseEntity.ok(new BalanceDto(balanceService.getBalance()));
  }

  @PostMapping(value = "/balance", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public void update(@RequestBody @Validated BalanceDto update) {
    balanceService.updateBalance(update.amount());
  }
}
