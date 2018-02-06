package de.exb.platform.cloud.fileservice.fileservice.view.endpoint

import de.exb.platform.cloud.fileservice.fileservice.view.resource.DirectoryResource
import de.exb.platform.cloud.fileservice.fileservice.view.resource.ItemResource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import static io.restassured.RestAssured.expect
import static io.restassured.RestAssured.get
import static io.restassured.RestAssured.given
import static io.restassured.RestAssured.post
import static org.hamcrest.Matchers.hasItem
import static org.hamcrest.Matchers.is

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class FileServiceEndpointsTest extends Specification {

    def jsonDir = "src/test/resources"

    def "should list directories"() {
        expect: "directories"
        get("/rs/directories").then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .and()
                .body("items.id", hasItem(1))
                .log()

    }

    def "should show directory"() {
        expect: "directory 1"
        get("/rs/directories/{id}", 1).then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .and()
                .body("item.id", is(1))
                .log()

    }

    def "shold create directory"() {

        RestTemplate restTemplate = new RestTemplate()
        def result = restTemplate.exchange("http://localhost:8080/rs/directories", HttpMethod.POST, null, new ParameterizedTypeReference<ItemResource<DirectoryResource>>() {
        })

        expect: "directory 1 created"
//        given().body(new File("${jsonDir}/directory1.json"))
//        post("/rs/directories").then()
//                .assertThat()
//                .statusCode(HttpStatus.CREATED.value())
//                .and()
//                .body("item.id", is(1))
//                .log()


    }

}
