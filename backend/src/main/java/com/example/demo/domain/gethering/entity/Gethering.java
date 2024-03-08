package com.example.demo.domain.gethering.entity;


import com.example.demo.domain.sports.entity.Sports;
import com.example.demo.domain.user.entity.EnumGender;
import com.example.demo.domain.user.entity.EnumSportsCareer;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "gethering")
public class Gethering extends BaseEntity {

    @Id
    @org.hibernate.annotations.Comment("PK")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @org.hibernate.annotations.Comment("모임 이름")
    @Column(name = "title")
    private String title;

    @org.hibernate.annotations.Comment("모임 설명")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @org.hibernate.annotations.Comment("모임 시작일")
    @Column(name = "start_date")
    private LocalDate startDate;

    @org.hibernate.annotations.Comment("모임 종료일")
    @Column(name = "end_date")
    private LocalDate endDate;

    @org.hibernate.annotations.Comment("모임 시작시간")
    @Column(name = "start_time")
    private LocalTime startTime;

    @org.hibernate.annotations.Comment("모임 종료시간")
    @Column(name = "end_time")
    private LocalTime endTime;

    @org.hibernate.annotations.Comment("모임 요일")
    @Column(name = "days_of_week")
    private Integer daysOfWeek;

    @org.hibernate.annotations.Comment("모임 지도 상세 주소")
    @Column(name = "map_address")
    private String mapAddress;

    @org.hibernate.annotations.Comment("모임 지도 위치")
    @Column(name = "map_location")
    private String mapLocation;

    @org.hibernate.annotations.Comment("모임 위도")
    @Column(name = "latitude")
    private double latitude;

    @org.hibernate.annotations.Comment("모임 경도")
    @Column(name = "longitude")
    private double longitude;

    @org.hibernate.annotations.Comment("모임 장소")
    @Column(name = "location")
    private String location;

    @org.hibernate.annotations.Comment("모임 법정동 코드")
    @Column(name = "legal_address_code")
    private String legalAddressCode;

    @org.hibernate.annotations.Comment("모임 요구 경력")
    @Column(name = "career")
    @Enumerated(EnumType.ORDINAL)
    private EnumSportsCareer career;

    @org.hibernate.annotations.Comment("모임 성별")
    @Column(name = "gender")
    @Enumerated(EnumType.ORDINAL)
    private EnumGender gender;

    @org.hibernate.annotations.Comment("모임 최대 인원")
    @Column(name = "max_people_count")
    private Short maxPeopleCount;

    @org.hibernate.annotations.Comment("현재 참여 인원")
    @Column(name = "current_people_count")
    private Short currentPeopleCount;

    @org.hibernate.annotations.Comment("조회수")
    @Column(name = "view_count")
    private Integer viewCount;

    @org.hibernate.annotations.Comment("모임 생성자")
    @ManyToOne
    private User user;

    @org.hibernate.annotations.Comment("댓글")
    @OneToMany(mappedBy = "gethering")
    List<com.example.demo.domain.gethering.entity.Comment> comments;

    @org.hibernate.annotations.Comment("태그")
    @ManyToMany
    @JoinTable(
            name = "gethering_tag",
            joinColumns = @JoinColumn(name = "gethering_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @org.hibernate.annotations.Comment("모임 참가자")
    @ManyToMany
    @JoinTable(
            name = "gethering_user",
            joinColumns = @JoinColumn(name = "gethering_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants;

    @org.hibernate.annotations.Comment("모임 좋아요")
    @ManyToMany
    @JoinTable(
            name = "gethering_like",
            joinColumns = @JoinColumn(name = "gethering_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> likes;

    @org.hibernate.annotations.Comment("이미지")
    @ManyToMany
    @JoinTable(
            name = "gethering_image",
            joinColumns = @JoinColumn(name = "gethering_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private List<Image> images;

    @org.hibernate.annotations.Comment("운동 카테고리")
    @ManyToOne
    @JoinColumn(name = "sports_id")
    private Sports sports;

    @org.hibernate.annotations.Comment("모임 연령대")
    @Column(name = "age")
    @Enumerated(EnumType.ORDINAL)
    private EnumAge age;

    public String getDistance(double OthLatitude, double OthLongitude) {
        double dLat = Math.toRadians(OthLatitude - this.latitude);
        double dLng = Math.toRadians(OthLongitude - this.longitude);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(OthLatitude)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 6371 : 지구 반지름
        return String.format("%.2fKm", 6371 * c);
    }
}