package io.kadmos.savingsservice.api

import io.kadmos.savingsservice.service.BalanceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BalanceController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class BalanceControllerTest extends Specification {
    @Autowired
    BalanceService balanceService

    @Autowired
    private MockMvc mvc

    def "balance can be fetched"() {
        given:
            balanceService.getBalance() >> new BigDecimal("10.5")
        and:
            def document = documentPrettyPrintReqResp("get-ok")
        expect:
            mvc.perform(
                    MockMvcRequestBuilders.get("/balance")
                            .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath('$.amount').value("10.5"))
            .andDo(document)
    }

    def "balance can be updated"() {
        given:
            def document = documentPrettyPrintReqResp("update-ok")
        expect:
        mvc.perform(
                MockMvcRequestBuilders.post("/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "amount": 11.05
                            }
                        """)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(document)
    }

    def "negative balance is not allowed"() {
        given:
        def document = documentPrettyPrintReqResp("update-nok")
        expect:
        mvc.perform(
                MockMvcRequestBuilders.post("/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "amount": -11.05
                            }
                        """)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andDo(document)
    }

    static RestDocumentationResultHandler documentPrettyPrintReqResp(String useCase) {
        return document(useCase,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()));
    }

    @TestConfiguration
    static class MockConfig {
        def detachedMockFactory = new DetachedMockFactory()

        @Bean
        BalanceService balanceService() {
            return detachedMockFactory.Mock(BalanceService)
        }

    }
}
