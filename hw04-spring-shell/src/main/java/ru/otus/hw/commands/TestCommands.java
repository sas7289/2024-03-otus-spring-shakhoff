package ru.otus.hw.commands;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.shell.Availability;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.security.StudentContext;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent
@Command
@RequiredArgsConstructor
public class TestCommands {

    private final StudentContext studentContext;

    private final TestRunnerService testRunnerService;

    private final ResultService resultService;

    private final LocalizedIOService localizedIOService;

    @ShellMethod(value = "Login", key = {"login", "l"})
    public void login(@ShellOption(value = {"--first-name", "-fn"}) String firstName,
                      @ShellOption(value = {"--last-name", "-ln"}) String lastName) {
        studentContext.login(firstName, lastName);
        localizedIOService.printFormattedLineLocalized("TestCommands.successfully.logged", firstName, lastName);
    }

    @ShellMethod(value = "Run test", key = {"run_test", "rt"})
    public void runTest() {
        testRunnerService.run();
        localizedIOService.printLineLocalized("TestCommands.test.completed");
    }

    @ShellMethod(value = "Show result", key = {"show_result", "sr"})
    public void showResult() {
        Set<TestResult> results = resultService.getResults(studentContext.getStudent());

        if (CollectionUtils.isEmpty(results)) {
            localizedIOService.printLineLocalized("TestCommands.need.complete.test.before.show.result");
            return;
        }

        results.forEach(resultService::showResult);
    }

    @ShellMethodAvailability({"run_test", "show_result"})
    public Availability isStudentLoggedIn() {
        return studentContext.isStudentLoggedIn() ? Availability.available()
            : Availability.unavailable("Please, log in");
    }

}