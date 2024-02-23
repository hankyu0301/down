package com.example.demo.domain.user.dto.command;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckEmailCommandTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @DisplayName("O 성공 이메일 주소 테스트")
    @ParameterizedTest
    @CsvFileSource(resources = "/emails.csv", numLinesToSkip = 1)
    public void testValidEmail(String email) {
        CheckEmailCommand command = createCommand();
        command.setEmail(email);
        assertTrue(validator.validate(command).isEmpty());
    }

    @DisplayName("X 실패 이메일 주소 테스트")
    @ParameterizedTest
    @CsvFileSource(resources = "/invalid-emails.csv", numLinesToSkip = 1)
    public void testInvalidEmail(String email) {
        CheckEmailCommand command = createCommand();
        command.setEmail(email);
        assertFalse(validator.validate(command).isEmpty());
    }

    private CheckEmailCommand createCommand() {
        CheckEmailCommand cmd = new CheckEmailCommand();
        cmd.setEmail("test@gmail.com");
        return cmd;
    }
}