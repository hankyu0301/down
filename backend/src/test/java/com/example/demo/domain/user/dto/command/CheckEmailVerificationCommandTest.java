package com.example.demo.domain.user.dto.command;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

class CheckEmailVerificationCommandTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @DisplayName("O 성공 이메일 주소 테스트")
    @ParameterizedTest
    @CsvFileSource(resources = "/emails.csv", numLinesToSkip = 1)
    public void testValidEmail(String email) {
        CheckEmailVerificationCommand command = new CheckEmailVerificationCommand();
        command.setEmail(email);
        command.setCode("123456");

        assertTrue(validator.validate(command).isEmpty());
    }

    @DisplayName("X 실패 이메일 주소 테스트")
    @ParameterizedTest
    @CsvFileSource(resources = "/invalid-emails.csv", numLinesToSkip = 1)
    public void testInvalidEmail(String email) {
        CheckEmailVerificationCommand command = new CheckEmailVerificationCommand();
        command.setEmail(email);
        command.setCode("123456");

        assertFalse(validator.validate(command).isEmpty());
    }

    @DisplayName("인증코드 테스트 성공")
    @Test
    public void testValidCode() {
        CheckEmailVerificationCommand command = new CheckEmailVerificationCommand();
        command.setEmail("test@gmail.com");
        command.setCode("123456");

        assertTrue(validator.validate(command).isEmpty());
    }

    @DisplayName("인증코드 테스트 실패 - 5자리")
    @Test
    public void testInvalidCodeLength() {
        CheckEmailVerificationCommand command = new CheckEmailVerificationCommand();
        command.setEmail("test@gmail.com");
        command.setCode("12345");

        assertFalse(validator.validate(command).isEmpty());
    }

    @DisplayName("인증코드 테스트 실패 - 7자리")
    @Test
    public void testInvalidCodeLength2() {
        CheckEmailVerificationCommand command = new CheckEmailVerificationCommand();
        command.setEmail("test@gmail.com");
        command.setCode("1234567");

        assertFalse(validator.validate(command).isEmpty());
    }
}