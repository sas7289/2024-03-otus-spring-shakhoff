package ru.otus.hw.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StreamsIOServiceTest {

    private StreamsIOService streamsIOService;

    private static Stream<Arguments> inputValidValues() {
        return Stream.of(
            Arguments.of(1),
            Arguments.of(2),
            Arguments.of(3)
        );
    }

    @Test
    public void shouldThrowIllegalArgumentException() {
        int minRangeValue = 1;
        int maxRangeValue = 3;
        String outOfRangeValues = "5\n5\n5\n5\n5\n5\n5\n5\n5\n5\n";

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(os);
        InputStream inputStream = new ByteArrayInputStream(outOfRangeValues.getBytes());

        this.streamsIOService = new StreamsIOService(printStream, inputStream);

        Assertions.assertThrows(IllegalArgumentException.class, () -> streamsIOService.readIntForRange(minRangeValue, maxRangeValue, "ErrorMessage"));
    }

    @ParameterizedTest
    @MethodSource("inputValidValues")
    public void shouldValidAndReturnValueFromScanner(int inputValue) {
        int minRangeValue = 1;
        int maxRangeValue = 3;

        String outOfRangeValues = inputValue + "\n";
        PrintStream printStream = new PrintStream(new ByteArrayOutputStream());
        InputStream inputStream = new ByteArrayInputStream(outOfRangeValues.getBytes());

        this.streamsIOService = new StreamsIOService(printStream, inputStream);

        Assertions.assertEquals(inputValue, streamsIOService.readIntForRange(minRangeValue, maxRangeValue, "ErrorMessage"));
    }
}