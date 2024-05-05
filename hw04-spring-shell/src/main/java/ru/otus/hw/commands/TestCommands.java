package ru.otus.hw.commands;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.security.StudentContext;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent
@Command
@RequiredArgsConstructor
public class TestCommands {

    private final StudentContext studentContext;

    private final TestRunnerService testRunnerService;

    private final ResultService resultService;

    @ShellMethod(value = "Login", key = {"login", "l"})
    public String login(@ShellOption(value = {"--first-name", "-fn"}) String firstName,
                        @ShellOption(value = {"--last-name", "-ln"}) String lastName) {
        studentContext.login(firstName, lastName);
        return String.format("You have successfully logged in as %s %s", firstName, lastName);
    }

    @ShellMethod(value = "Run test", key = {"run_test", "rt"})
    public String runTest() {
        testRunnerService.run();
        return "The test is completed";
    }

    @ShellMethod(value = "Show result", key = {"show_result", "sr"})
    public void showResult() {
        TestResult result = resultService.getResult(studentContext.getStudent());

        if (Objects.isNull(result)) {
            throw new RuntimeException("You haven't taken the test yet, please run the Run command first");
        }

        resultService.showResult(result);
    }

    @ShellMethodAvailability({"run_test", "show_result"})
    public Availability isStudentLoggedIn() {
        return studentContext.isStudentLoggedIn() ? Availability.available()
            : Availability.unavailable("Please, log in");
    }

}