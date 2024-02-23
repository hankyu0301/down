package com.example.demo.domain.user.dto.command;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

class UserLoginCommandTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @DisplayName("O 성공 테스트")
    @Test
    public void testValid() {
        UserLoginCommand cmd = createCommand();
        assertTrue(validator.validate(cmd).isEmpty());
    }

    @DisplayName("O 성공 이메일 주소 테스트")
    @ParameterizedTest
    @CsvFileSource(resources = "/emails.csv", numLinesToSkip = 1)
    public void testValidEmail(String email) {
        UserLoginCommand cmd = createCommand();
        cmd.setEmail(email);
        cmd.setPassword("password");

        assertTrue(validator.validate(cmd).isEmpty());
    }

    @DisplayName("X 실패 이메일 주소 테스트")
    @ParameterizedTest
    @CsvFileSource(resources = "/invalid-emails.csv", numLinesToSkip = 1)
    public void testInvalidEmail(String email) {
        UserLoginCommand cmd = createCommand();
        cmd.setEmail(email);
        assertFalse(validator.validate(cmd).isEmpty());
    }

    @DisplayName("X 실패 이메일 테스트")
    @Test
    public void testInvalidEmail() {
        UserLoginCommand cmd = createCommand();
        cmd.setEmail(null);
        assertFalse(validator.validate(cmd).isEmpty());
    }

    @DisplayName("X 실패 비밀번호 테스트 - 빈 값")
    @Test
    public void testInvalidPasswordEmpty() {
        UserLoginCommand cmd = createCommand();
        cmd.setPassword("");
        assertFalse(validator.validate(cmd).isEmpty());
    }

    @DisplayName("X 실패 비밀번호 테스트 - null")
    @Test
    public void testInvalidPasswordNull() {
        UserLoginCommand cmd = createCommand();
        cmd.setPassword(null);
        assertFalse(validator.validate(cmd).isEmpty());
    }

    private UserLoginCommand createCommand() {
        UserLoginCommand cmd = new UserLoginCommand();
        cmd.setEmail("test@gmail.com");
        cmd.setPassword("password");
        return cmd;
    }
}