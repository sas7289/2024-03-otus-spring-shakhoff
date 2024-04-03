package ru.otus.hw.dao.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.hw.domain.Answer;

class AnswerCsvConverterTest {


    public static final String ANSWER_TEXT = "equals(Object obj)";
    public static final boolean IS_CORRECT = false;
    public static final String CSV_ANSWER = ANSWER_TEXT + "%" + IS_CORRECT;

    @Test
    void correctConverting() {
        AnswerCsvConverter answerCsvConverter = new AnswerCsvConverter();
        Answer answer = (Answer) answerCsvConverter.convertToRead(CSV_ANSWER);
        Assertions.assertAll(() -> assertEquals(ANSWER_TEXT, answer.text()),
            () -> assertEquals(IS_CORRECT, answer.isCorrect()));
    }
}