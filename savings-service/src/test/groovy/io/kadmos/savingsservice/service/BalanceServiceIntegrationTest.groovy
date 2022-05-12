package io.kadmos.savingsservice.service

import io.kadmos.savingsservice.ItBase
import io.kadmos.savingsservice.storage.BalanceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.testcontainers.spock.Testcontainers
import spock.lang.Subject

import static io.kadmos.savingsservice.storage.BalanceRepository.BALANCE_ID

@SpringBootTest
@Testcontainers
//clean things up
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class BalanceServiceIntegrationTest extends ItBase {
    @Subject
    @Autowired
    BalanceService balanceService

    @Autowired
    BalanceRepository balanceRepository

    def "balance initialized by the service startup"() {
        expect:
            def found = balanceRepository.findById(BALANCE_ID).orElseThrow()
            found.amount == BigDecimal.ZERO
    }



}
