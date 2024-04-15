package ru.otus.hw.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.InputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StreamsIOServiceTest {


    @Mock
    private PrintStream printStream;

    @Mock
    private InputStream inputStream;

    private IOService streamsIOService;

    @BeforeEach
    void initStreamIOService() {
        streamsIOService = new StreamsIOService(printStream, inputStream);
    }

    @Test
    @DisplayName("")
    void printLineTest() {
        String expectedText = "Expected text";

        var captor = ArgumentCaptor.forClass(String.class);

        streamsIOService.printLine(expectedText);

        verify(printStream, times(1)).println(captor.capture());

        Assertions.assertEquals(expectedText, captor.getValue());
    }

}