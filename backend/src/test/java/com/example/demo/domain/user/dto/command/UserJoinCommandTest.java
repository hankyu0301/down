package com.example.demo.domain.user.dto.command;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

class UserJoinCommandTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @DisplayName("O 성공 이메일 주소 테스트")
    @ParameterizedTest
    @CsvFileSource(resources = "/emails.csv", numLinesToSkip = 1)
    public void testValidEmail(String email) {
        UserJoinCommand cmd = createCommand();
        cmd.setEmail(email);
        assertTrue(validator.validate(cmd).isEmpty());
    }

    @DisplayName("X 실패 이메일 주소 테스트")
    @ParameterizedTest
    @CsvFileSource(resources = "/invalid-emails.csv", numLinesToSkip = 1)
    public void testInvalidEmail(String email) {
        UserJoinCommand cmd = createCommand();
        cmd.setEmail(email);
        assertFalse(validator.validate(cmd).isEmpty());
    }

    @DisplayName("O 성공 이름 테스트")
    @ParameterizedTest
    @CsvFileSource(resources = "/name.csv", numLinesToSkip = 1)
    public void testValidUserName(String userName) {
        UserJoinCommand cmd = createCommand();
        cmd.setUserName(userName);
        assertTrue(validator.validate(cmd).isEmpty());
    }

    @DisplayName("X 실패 이름 테스트 - 빈 값")
    @Test
    public void testInvalidUserNameEmpty() {
        UserJoinCommand cmd = createCommand();
        cmd.setUserName("");
        assertFalse(validator.validate(cmd).isEmpty());
    }

    @DisplayName("X 실패 이름 테스트 - null")
    @Test
    public void testInvalidUserNameNull() {
        UserJoinCommand cmd = createCommand();
        cmd.setUserName(null);
        assertFalse(validator.validate(cmd).isEmpty());
    }

    @DisplayName("X 실패 사용자 닉네임 - 빈 값")
    @Test
    public void testInvalidNickNameEmpty() {
        UserJoinCommand cmd = createCommand();
        cmd.setNickName("");
        assertFalse(validator.validate(cmd).isEmpty());
    }

    @DisplayName("X 실패 사용자 닉네임 - null")
    @Test
    public void testInvalidNickNameNull() {
        UserJoinCommand cmd = createCommand();
        cmd.setNickName(null);
        assertFalse(validator.validate(cmd).isEmpty());
    }

    @DisplayName("X 실패 생년월일 - 빈 값")
    @Test
    public void testInvalidBirthEmpty() {
        UserJoinCommand cmd = createCommand();
        cmd.setBirth("");
        assertFalse(validator.validate(cmd).isEmpty());
    }

    @DisplayName("X 실패 생년월일 - null")
    @Test
    public void testInvalidBirthNull() {
        UserJoinCommand cmd = createCommand();
        cmd.setBirth(null);
        assertFalse(validator.validate(cmd).isEmpty());
    }

    @DisplayName("O 성공 이메일 인증코드 - 빈 값")
    @Test
    public void testInvalidCodeEmpty() {
        UserJoinCommand cmd = createCommand();
        cmd.setCode("");
        assertFalse(validator.validate(cmd).isEmpty());
    }

    @DisplayName("O 성공 이메일 인증코드 - null")
    @Test
    public void testInvalidCodeNull() {
        UserJoinCommand cmd = createCommand();
        cmd.setCode(null);
        assertFalse(validator.validate(cmd).isEmpty());
    }
    @DisplayName("O 성공 이용약관 동의 여부 - null")
    @Test
    public void testInvalidTermsAgreeNull() {
        UserJoinCommand cmd = createCommand();
        cmd.setTermsAgree(null);
        assertFalse(validator.validate(cmd).isEmpty());
    }

    @DisplayName("O 성공 이용약관 동의 여부 - false")
    @Test
    public void testInvalidTermsAgreeFalse() {
        UserJoinCommand cmd = createCommand();
        cmd.setTermsAgree(false);
        assertFalse(validator.validate(cmd).isEmpty());
    }

    private UserJoinCommand createCommand() {
        UserJoinCommand command = new UserJoinCommand();
        command.setEmail("test@gmail.com");
        command.setPassword("test1234");
        command.setNickName("페이커");
        command.setGender("male");
        command.setBirth("9999-99-99");
        command.setUserName("이상혁");
        command.setCode("1A3B5C");
        command.setTermsAgree(true);
        return command;
    }
}