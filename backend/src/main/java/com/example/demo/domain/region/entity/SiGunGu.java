package com.example.demo.domain.region.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "si_gun_gu",
        indexes = {
                @Index(name = "idx_si_gun_gu_code", columnList = "code"),
                @Index(name = "idx_si_gun_gu_legal_address_code", columnList = "legal_address_code")
        }
)
public class SiGunGu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Comment("시/군/구 코드 3자리")
    @Column(name = "code", nullable = false)
    private Short code;

    @Comment("시/도 코드")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "si_do_id", nullable = false)
    private SiDo siDo;

    @Comment("시/군/구 이름")
    @Column(name = "name", nullable = false)
    private String name;

    @Comment("법정동 코드")
    @Column(name = "legal_address_code", nullable = false)
    private Long legalAddressCode;
}