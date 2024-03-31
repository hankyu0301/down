### DOWN

### Docker 실행 명령어

```docker
docker-compose build
docker-compose up
```

함께 운동할 팀원을 구하는 플랫폼입니다.

- 프로젝트 명칭 : down ( 다 같이 운동하자는 의미입니다. )
- 개발 인원 : 5명 (Backend 2인, FrontEnd 2인, Designer 1인)
- 개발 기간 : 2024.02.24~

📄 [API 명세](https://www.notion.so/DOWN-API-278abb59a6644c78aad0a370e0e6575b?pvs=21)

---

### 사용 Skills

- Java17, SpringBoot, Gradle
- MySQL, Redis
- Junit5, Mockito
- Github, Docker
- FCM, Swagger

---

### 담당기능 (채팅, 알림)

- 그룹 채팅
    - 커서 기반 페이징으로 채팅 메시지 목록을 조회할 수 있습니다.
    - 채팅 메시지를 삭제할 수 있습니다. (본인만 조회 불가)
    - 채팅방에 초대되기 이전 메시지는 조회할 수 없습니다.
    - 채팅방에 초대하거나 초대 코드를 사용해 입장 가능합니다.
    - 초대코드는 Redis에서 관리합니다.
- 개인 채팅
    - 커서 기반 페이징으로 채팅 메시지 목록을 조회할 수 있습니다.
    - 채팅 메시지를 삭제할 수 있습니다. (본인만 조회 불가)
    - 채팅방을 나간 경우 이전 메시지를 조회할 수 없습니다.
- 알림 기능
    - 채팅 메시지가 전송되면 수신자 모두에게 Web Push 알림이 발송됩니다.
    - 알림 기능에 사용되는 FCM 토큰은 Redis에서 관리합니다.

- 채팅 기능을 호출하기 위한 인증/인가 검증 로직을 작성하였습니다.
- 채팅 메시지를 다양한 조건으로 필터링하여 조회할 수 있도록 QueryDSL을 사용했습니다.
- 실시간 채팅 기능을 구현하기 위해 WebSocket과 Redis를 사용했습니다.
- 채팅 메시지 전송시 AOP를 사용해 userId를 주입했습니다.

---

### ERD

![DOWN_ERD](https://github.com/hankyu0301/down/assets/77604789/65753784-bfef-49f8-8a61-46ec24f15dd7)

---
