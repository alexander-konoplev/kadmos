package io.kadmos.savingsservice.storage

import io.kadmos.savingsservice.ItBase
import io.kadmos.savingsservice.storage.dto.BalanceEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.spock.Testcontainers
import spock.lang.Subject

import static io.kadmos.savingsservice.storage.BalanceRepository.BALANCE_ID

@SpringBootTest
@Testcontainers
class BalanceRepositoryTest extends ItBase {
    @Subject
    @Autowired
    BalanceRepository balanceRepository

    void setup() {
        balanceRepository.deleteAllInBatch()
    }

    def 'an entity can be stored'() {
        given: "entity id"
            def id = 0
        when: "we save something in DB"
            def entity = new BalanceEntity()
            entity.amount = BigDecimal.TEN
            entity.id = id
            balanceRepository.save(entity)
        then: "it can be fetched"
            balanceRepository.findById(id).isPresent()
    }

    @Transactional
    def 'balance can be updated'() {
        given:
            def balance = BigDecimal.valueOf(42L)
        and: "there is a user with 0 amount"
            def entity = new BalanceEntity()
            entity.id = BALANCE_ID
            entity.amount = 0;
            balanceRepository.save(entity)
        when:
            balanceRepository.updateBalance(balance)
        then:
            def found = balanceRepository.findById(BALANCE_ID).orElseThrow()
            noExceptionThrown()
            found.amount == balance
    }


}
