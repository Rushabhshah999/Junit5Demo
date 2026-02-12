import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("JUnit 5")
class AppTest {



    @Test
    void simpleAssertion() {
        assertEquals(4, 2 + 2);
        assertTrue(10 > 5);
        assertNotNull("JUnit5");
    }
    /* --------- Grouped Assertions --------- */
    @Test
    void groupedAssertions() {
        assertAll("Numbers",
                () -> assertEquals(2, 1 + 1),
                () -> assertTrue(5 > 1),
                () -> assertNotNull("Hello"),
                () -> assertTrue( 1 > 0)

        );
    }
    /* --------- Exception Testing --------- */

    @Test
    void exceptionTest() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Integer.parseInt("abc")
        );
        assertTrue(ex.getMessage().contains("For input"));
    }

    /* --------- Timeouts --------- */

    @Test
    void timeoutTest() {
        assertTimeout(Duration.ofMillis(100), () -> Thread.sleep(50));
    }

    /* --------- Assumptions --------- */

    @Test
    void assumptionsExample() {
        assumeTrue(System.getProperty("os.name").contains("Windows"));
        assumeFalse(System.getProperty("os.name").contains("linux"));
        assertTrue(true); // runs only if assumption holds
    }

    /* --------- Conditional Execution --------- */

    @Test
    @EnabledOnOs(OS.LINUX)
    void linuxOnlyTest() {
        assertTrue(true);
    }

    /* --------- Parameterized Tests --------- */

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void valueSourceTest(int value) {
        assertTrue(value > 0);
    }

    @ParameterizedTest
    @MethodSource("stringProvider")
    void methodSourceTest(String input) {
        assertNotNull(input);
    }

    static Stream<String> stringProvider() {
        return Stream.of("JUnit", "Jupiter", "Rushabh");
    }

    /* --------- Dynamic Tests --------- */

    @TestFactory
    Stream<DynamicTest> dynamicTestsFromList() {
        List<Integer> numbers = List.of(1, 2, 3, 40);

        return numbers.stream()
                .map(n -> DynamicTest.dynamicTest(
                        "Number > 0: " + n,
                        () -> assertTrue(n > 0)
                ));
    }

    @TestFactory
    Stream<DynamicTest> testsFromFile() throws Exception {

        List<String> numbers = Files
                .lines(Path.of("src/test/resources/test-data.txt"))
                .collect(Collectors.toList());

         return numbers.stream()
                 .map( n -> DynamicTest.dynamicTest(
                         "Test for input: " + n,
                         () -> assertTrue(Integer.parseInt(n) > 0)
                 ));


    }
}
