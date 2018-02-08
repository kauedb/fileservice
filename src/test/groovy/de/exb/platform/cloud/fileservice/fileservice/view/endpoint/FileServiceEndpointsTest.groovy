package de.exb.platform.cloud.fileservice.fileservice.view.endpoint

import io.restassured.http.ContentType
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import spock.lang.Specification

import static io.restassured.RestAssured.given
import static org.hamcrest.Matchers.hasItem
import static org.hamcrest.Matchers.is

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class FileServiceEndpointsTest extends Specification {

    def jsonDir = "src/test/resources"

    def "should list directories"() {
        expect: "directories"
        given().that()

        .when().get("/rs/directories")

        .then().assertThat()
                .statusCode(HttpStatus.OK.value())
        .and()
                .body("items.id", hasItem(1))
                .log().all(true)
    }

    def "should show directory"() {
        expect: "directory 2"
        given().that()

        .when().get("/rs/directories/{id}", 2)

        .then().assertThat()
                .statusCode(HttpStatus.OK.value())
        .and()
                .body("item.id", is(2))
                .log().all(true)
    }

    def "should create directory"() {
        expect: "directory 1 created"
        given().that()
                .accept(ContentType.JSON)
        .and()
                .contentType(ContentType.JSON)
        .and()
                .body(new File("${jsonDir}/directory1.json"))

        .when().post("/rs/directories")

        .then().assertThat()
                .contentType(ContentType.JSON)
        .and()
                .statusCode(HttpStatus.CREATED.value())
        .and()
                .body("item.id", is(1))
                .log().all(true)
    }

    def "should update directory"() {
        expect: "directory 1 updated"
        given().that()
                .accept(ContentType.JSON)
        .and()
                .contentType(ContentType.JSON)
        .and()
                .body(new File("${jsonDir}/directory1.json"))

        .when().put("/rs/directories/{id}", 1)

        .then().assertThat()
                .contentType(ContentType.JSON)
        .and()
                .statusCode(HttpStatus.OK.value())
        .and()
                .body("item.id", is(1))
                .log().all(true)
    }

    def "should delete directory"() {
        expect: "directory 1 deleted"
        given().that()
                .accept(ContentType.JSON)
        .and()
                .contentType(ContentType.JSON)
        .when()
                .delete("/rs/directories/{id}", 1)
        .then().assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all(true)

    }

    def "should list files"() {
        expect: "files"
        given().that()

        .when().get("/rs/directories/{id}/files", 1)

        .then().assertThat()
                .statusCode(HttpStatus.OK.value())
        .and()
                .body("items.id", hasItem(1))
        .log().all(true)
    }

    def "should show file"() {
        expect: "file 1"
        given().that()

        .when().get("/rs/directories/{directoryId}/files/{id}", 1, 1)

        .then().assertThat()
                .statusCode(HttpStatus.OK.value())
        .and()
                .body("item.id", is(1))
                .log().all(true)
    }

    def "should create file"() {
        expect: "file 1 created"
        given().that()
        .when()
                .accept(ContentType.JSON)
        .and()
                .contentType(ContentType.JSON)
        .and()
                .body(new File("${jsonDir}/file1.json"))
        .when().post("/rs/directories/{id}/files", 1)

        .then().assertThat()
                .contentType(ContentType.JSON)
        .and()
                .statusCode(HttpStatus.CREATED.value())
        .and()
                .body("item.id", is(1))
                .log().all(true)


    }

    def "should update file"() {

        expect: "file 1 updated"
        given().that()
                .accept(ContentType.JSON)
        .and()
                .contentType(ContentType.JSON)
        .and()
                .body(new File("${jsonDir}/file1.json"))

        .when().put("/rs/directories/{directoryId}/files/{id}", 1, 1)

        .then().assertThat()
                .contentType(ContentType.JSON)
                .and()
                .statusCode(HttpStatus.OK.value())
                .and()
                .body("item.id", is(1))
                .log().all(true)


    }

    def "should delete file"() {
        expect: "file 1 deleted"
        given().that()
                .accept(ContentType.JSON)
        .and()
                .contentType(ContentType.JSON)

        .when().delete("/rs/directories/{directoryId}/files/{id}", 1, 1)

        .then().assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all(true)
    }

}
