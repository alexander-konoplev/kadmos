package io.kadmos.savingsservice.service

import io.kadmos.savingsservice.storage.BalanceRepository
import io.kadmos.savingsservice.storage.dto.BalanceEntity
import spock.lang.Specification
import spock.lang.Subject


class BalanceServiceTest extends Specification {
    BalanceRepository balanceRepository = Mock()
    @Subject
    BalanceService balanceService = new BalanceService(balanceRepository)

    def "balance can be received"() {
        given:
            def expectedBalance = new BalanceEntity()
            expectedBalance.id = BalanceRepository.BALANCE_ID
            expectedBalance.amount = 42
            balanceRepository.findById(BalanceRepository.BALANCE_ID) >> Optional.of(expectedBalance)
        when:
            def balance = balanceService.getBalance()
        then:
            balance == 42
    }
}
