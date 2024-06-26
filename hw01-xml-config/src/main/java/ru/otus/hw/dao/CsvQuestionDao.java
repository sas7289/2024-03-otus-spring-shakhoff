package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;

import java.util.List;
import ru.otus.hw.exceptions.QuestionReadException;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        // Использовать CsvToBean
        // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings
        // Использовать QuestionReadException
        // Про ресурсы: https://mkyong.com/java/java-read-a-file-from-resources-folder/
        try (InputStream resourceAsStream = getClass().getClassLoader()
            .getResourceAsStream(fileNameProvider.getTestFileName())) {
            List<QuestionDto> questionDTOs = new CsvToBeanBuilder(new InputStreamReader(resourceAsStream))
                .withType(QuestionDto.class)
                .withSkipLines(1)
                .withSeparator(';')
                .build()
                .parse();

            return questionDTOs.stream()
                .map(QuestionDto::toDomainObject)
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new QuestionReadException("Fail while reading csv file", e);
        }
    }
}
