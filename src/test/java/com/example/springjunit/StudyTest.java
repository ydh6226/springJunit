package com.example.springjunit;

import com.example.springjunit.domain.Study;
import com.example.springjunit.domain.StudyStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StudyTest {
    
    @Test
    @DisplayName("테스트1")
    void create1() throws Exception {
        Study study = new Study(4);

//        assertThat(study).isNotNull();
//        assertThat(study.getStatus()).isEqualTo(StudyStatus.DRAFT);
//        assertThat(study.getLimit()).isGreaterThan(0);

        //assertAll()을 사용하면 중간에 위치한 테스트가 깨지더라도 그 다음의 테스트 까지 실행함.
        assertAll(
                () -> assertThat(study).isNotNull(),
                () -> assertThat(study.getStatus()).isEqualTo(StudyStatus.DRAFT),
                () -> assertThat(study.getLimit()).isGreaterThan(0)
        );

    }


    @Test
    @DisplayName("스터디 만들기")
    void create2() throws Exception {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Study(-10));
        assertThat(exception.getMessage()).isEqualTo("limit은 0보다 커야한다");

        /*
         * 테스트가 특정시간안에 끝나야한다.
         * 예상치 못한 에러 발생가능성 있음.
         * assertTimeOut()을 사용하는것이 안전
        * */
        assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
            new Study(10);
            Thread.sleep(10);
        });
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    @EnabledOnJre({JRE.JAVA_11, JRE.JAVA_8})
    @DisplayName("환경에 따라 다르게 테스트")
    void test1() throws Exception {
//        assumeThat(System.getenv("TEST_ENV")).isEqualTo("LOCAL");

        System.out.println("hello");
    }


    @Test
    @Tag("fast")
    void tag1() {
        System.out.println("fast");
    }

    @Test
    @Tag("slow")
    void tag2() {

    }

    /**
     * after: 테스트당 한번 호출
     * each: 테스타마다 호출
     */

//    @BeforeAll
//    static void beforeAll() {
//        System.out.println("before all");
//    }
//
//    @AfterAll
//    static void afterAll() {
//        System.out.println("after all");
//    }
//
//    @BeforeEach
//    void beforeEach() {
//        System.out.println("before each");
//    }
//
//    @AfterEach
//    void afterEach() {
//        System.out.println("after each");
//    }
}