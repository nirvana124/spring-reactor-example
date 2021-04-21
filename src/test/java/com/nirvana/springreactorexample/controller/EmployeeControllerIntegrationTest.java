package com.nirvana.springreactorexample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.nirvana.springreactorexample.model.Profile;
import com.nirvana.springreactorexample.repository.SalaryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.nirvana.springreactorexample.service.ProfileServiceImpl.convertToQueryString;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIntegrationTest {

    private WireMockServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private SalaryRepository salaryRepository;

    @BeforeEach
    public void init() {
        // Port 8181 is configured for profile service in application.properties for tests.
        server = new WireMockServer(WireMockConfiguration.options().port(8181));
        server.start();
    }

    @AfterEach
    public void end() {
        server.stop();
    }

    @Test
    public void shouldReturnStatusOkForGetEmployeeById() throws Exception {
        String employeeId = "1";
        server.stubFor(WireMock.get("/profiles/" + employeeId)
                .willReturn(WireMock.okJson(objectMapper.writeValueAsString(Profile.builder().id(employeeId).name("James")
                        .mobile("0987432").build()))));

        webTestClient.get().uri("/employees/" + employeeId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void shouldReturnEmployeeDetailsWithSalaryInResponseBodyForGetEmployeeById() throws Exception {
        String employeeId = "1";
        server.stubFor(WireMock.get("/profiles/" + employeeId)
                .willReturn(WireMock.okJson(objectMapper.writeValueAsString(Profile.builder().id(employeeId).name("James")
                        .mobile("0987432").build()))));

        webTestClient.get().uri("/employees/" + employeeId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody()
                .jsonPath("$.name").isEqualTo("James")
                .jsonPath("$.id").isEqualTo(employeeId)
                .jsonPath("$.salary").isEqualTo(20000.50);
    }

    @Test
    public void shouldReturnStatusNotFoundProvidedSalaryNotFoundForEmployee() throws Exception {
        String employeeId = "12345";

        server.stubFor(WireMock.get("/profiles/" + employeeId)
                .willReturn(WireMock.okJson(objectMapper.writeValueAsString(Profile.builder().id(employeeId).name("James")
                        .mobile("0987432").build()))));

        webTestClient.get().uri("/employees/" + employeeId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void shouldReturnStatusNotFoundProvidedProfileNotFoundForEmployee() throws Exception {
        String employeeId = "1";
        server.stubFor(WireMock.get("/profiles/" + employeeId)
                .willReturn(WireMock.notFound()));

        webTestClient.get().uri("/employees/" + employeeId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void shouldReturnStatusOkForGetAllEmployees() throws Exception {
        var employeeIds = List.of("1", "2", "3");
        server.stubFor(WireMock.get("/profiles?" + convertToQueryString(employeeIds))
                .willReturn(WireMock.okJson(objectMapper.writeValueAsString(List.of(
                        Profile.builder().id("1").name("James").mobile("0987432").build(),
                        Profile.builder().id("2").name("Pankaj").mobile("0987431").build(),
                        Profile.builder().id("3").name("Yadav").mobile("0987430").build()
                )))));

        webTestClient.get().uri("/employees?" + convertToQueryString(employeeIds))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void shouldReturnEmployeesDetailsWithSalaryInResponseBodyForGetAllEmployees() throws Exception {
        var employeeIds = List.of("1", "2", "3");
        server.stubFor(WireMock.get("/profiles?" + convertToQueryString(employeeIds))
                .willReturn(WireMock.okJson(objectMapper.writeValueAsString(List.of(
                        Profile.builder().id("1").name("James").mobile("0987432").build(),
                        Profile.builder().id("2").name("Pankaj").mobile("0987431").build(),
                        Profile.builder().id("3").name("Yadav").mobile("0987430").build()
                )))));

        webTestClient.get().uri("/employees?" + convertToQueryString(employeeIds))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("James")
                .jsonPath("$[1].name").isEqualTo("Pankaj")
                .jsonPath("$[2].name").isEqualTo("Yadav")
                .jsonPath("$[0].id").isEqualTo("1")
                .jsonPath("$[1].id").isEqualTo("2")
                .jsonPath("$[2].id").isEqualTo("3")
                .jsonPath("$[0].salary").isEqualTo(20000.50)
                .jsonPath("$[1].salary").isEqualTo(30000.50)
                .jsonPath("$[2].salary").isEqualTo(40000.50);
    }
}
