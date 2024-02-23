package com.example.demo.domain.user.dto.command;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

class UserJoinCheckCommandTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @DisplayName("O 성공 이메일 주소 테스트")
    @ParameterizedTest
    @CsvFileSource(resources = "/emails.csv", numLinesToSkip = 1)
    public void testValidEmail(String email) {
        UserJoinCheckCommand command = new UserJoinCheckCommand();
        command.setEmail(email);
        command.setUserName("홍길동");

        assertTrue(validator.validate(command).isEmpty());
    }

    @DisplayName("X 실패 이메일 주소 테스트")
    @ParameterizedTest
    @CsvFileSource(resources = "/invalid-emails.csv", numLinesToSkip = 1)
    public void testInvalidEmail(String email) {
        UserJoinCheckCommand command = new UserJoinCheckCommand();
        command.setEmail(email);
        command.setUserName("홍길동");

        assertFalse(validator.validate(command).isEmpty());
    }

    @DisplayName("O 성공 이름 테스트")
    @ParameterizedTest
    @CsvFileSource(resources = "/name.csv", numLinesToSkip = 1)
    public void testValidUserName(String userName) {
        UserJoinCheckCommand command = new UserJoinCheckCommand();
        command.setEmail("test@gmail.com");
        command.setUserName(userName);

        assertTrue(validator.validate(command).isEmpty());
    }

    @DisplayName("X 실패 이름 테스트 - 빈 값")
    @Test
    public void testInvalidUserNameEmpty() {
        UserJoinCheckCommand command = new UserJoinCheckCommand();
        command.setEmail("test@gmail.com");
        command.setUserName("");

        assertFalse(validator.validate(command).isEmpty());
    }

    @DisplayName("X 실패 이름 테스트 - null")
    @Test
    public void testInvalidUserNameNull() {
        UserJoinCheckCommand command = new UserJoinCheckCommand();
        command.setEmail("test@gmail.com");
        command.setUserName(null);

        assertFalse(validator.validate(command).isEmpty());
    }
}