package io.kadmos.savingsservice

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import spock.lang.Shared
import spock.lang.Specification


class ItBase  extends Specification {
    @Shared
    static def postgreSQLContainer

    static {
        postgreSQLContainer = new PostgreSQLContainer('postgres:13.3')
                .withDatabaseName("db")
                .withUsername("root")
                .withPassword("root")
                .withReuse(true)
        postgreSQLContainer.start()
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }
}
