package io.kadmos.gateway;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.reactive.server.WebTestClientBuilderCustomizer;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.cloud.gateway.routes[0].id=savingsA",
        "spring.cloud.gateway.routes[0].uri=http://localhost:${wiremock.server.port}",
        "spring.cloud.gateway.routes[0].predicates[0]=Path=/savings/a/**",
        "spring.cloud.gateway.routes[0].filters[0]=RewritePath=/savings/a/(?<postfix>.*), /$\\{postfix}",
        "spring.cloud.gateway.httpclient.response-timeout=5s"
    }
)
@AutoConfigureWireMock(port = 0)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class ApiTest {

  @Autowired
  private WebTestClient webClient;


  @Test
  public void getOk() {
    //configure stub for external service
    stubFor(get(urlEqualTo("/balance"))
        .willReturn(aResponse()
            .withBody("{\"amount\":\"10\"}")
            .withHeader("Content-Type", "application/json")));

    webClient.get().uri("/savings/a/balance").exchange().expectStatus().isOk()
        .expectBody()
        .jsonPath("$.amount").isEqualTo(10);
  }

  @Test
  public void delayIsHandled() {
    //configure stub for external service
    stubFor(get(urlEqualTo("/balance"))
        .willReturn(aResponse()
            .withBody("{\"amount\":\"10\"}")
            .withHeader("Content-Type", "application/json")
            //longer than the timeout
            .withFixedDelay(6000))
    );

    webClient.get().uri("/savings/a/balance").exchange().expectStatus().isEqualTo(HttpStatus.GATEWAY_TIMEOUT);
  }

  @TestConfiguration
  static class RestDocsParameterizedOutput {

    @Bean
    WebTestClientBuilderCustomizer restDocsParameterizedOutput() {
      return (builder) -> builder.entityExchangeResultConsumer(document("{method-name}"))
          .responseTimeout(Duration.ofMillis(10000));
    }

  }

}
