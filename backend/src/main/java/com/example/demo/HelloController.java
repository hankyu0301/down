package com.example.demo;


import com.example.demo.global.redis.EmailRedisRepository;
import com.example.demo.global.redis.ChatRedisRepository;
import com.example.demo.global.socket.Greeting;
import com.example.demo.global.socket.HelloMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

@RequiredArgsConstructor
@RestController
public class HelloController {

    private final JdbcTemplate jdbcTemplate;
    private final ChatRedisRepository chatRedisService;
    private final EmailRedisRepository emailRedisService;

    @GetMapping("/")
    public String index() {
        return "Hellow world!";
    }

    @GetMapping("/check-mysql-connection")
    public String checkMysqlConnection() {
        try {
            jdbcTemplate.execute("SELECT 1");
            return "MySQL 연결 확인: 성공";
        } catch (Exception ex) {
            return "MySQL 연결 확인: 실패 - " + ex.getMessage();
        }
    }

    @PostMapping("/setAlarmValue/{key}/")
    public ResponseEntity<String> setAlarmValue(
            @PathVariable String key
    ) {
        emailRedisService.setVerification(key);
        return ResponseEntity.ok("Value set successfully in alarm Redis");
    }

    @GetMapping("/getAlarmValue/{key}")
    public ResponseEntity<Object> getAlarmValue(
            @PathVariable String key
    ) {
        Object value = emailRedisService.getVerification(key);
        return ResponseEntity.ok(value);
    }

    @PostMapping("/setChatValue/{key}/{value}")
    public ResponseEntity<String> setChatValue(
            @PathVariable String key,
            @PathVariable String value
    ) {
        chatRedisService.setValue(key, value);
        return ResponseEntity.ok("Value set successfully in alarm Redis");
    }

    @GetMapping("/getChatValue/{key}")
    public ResponseEntity<Object> getChatValue(
            @PathVariable String key
    ) {
        Object value = chatRedisService.getValue(key);
        return ResponseEntity.ok(value);
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(
            HelloMessage message
    ) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
}