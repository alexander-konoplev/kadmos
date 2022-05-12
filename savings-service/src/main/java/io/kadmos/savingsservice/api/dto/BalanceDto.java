package io.kadmos.savingsservice.api.dto;

import java.math.BigDecimal;
import javax.validation.constraints.Min;

public record BalanceDto(@Min(value = 0) BigDecimal amount) {}
