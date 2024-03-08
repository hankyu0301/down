package com.example.demo.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class regionTest {

//    @Test
//    @DisplayName("지역 데이터 전처리")
//    public void regionPreprocessing() {
//        // resources 경로 파일 읽어오기
//        String path = "C:\\Users\\gksxo\\Desktop\\DownProject\\backend\\src\\test\\resources\\";
//        String fileName = "data.txt";
//
//        try (BufferedReader br = new BufferedReader(new FileReader(path + fileName))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] parts = line.split("\t"); // 탭으로 구분하여 문자열 배열로 분할
//                // parts 배열에 각 열의 데이터가 들어 있음
//
//                if (Objects.equals(parts[2], "폐지")) { continue; }
//                String id = parts[0];
//                String cityCode = parts[0].substring(0, 2);
//
//                // 시군구 데이터
//                if (Objects.equals(parts[0].substring(2, 10), "00000000")) {
//                    String cityName = parts[1];
//                    System.out.println(getInsertSiDoQuery(id.substring(0, 2), cityName));
//                    continue;
//
//                }
//
//                if (Objects.equals(parts[0].substring(5, 10), "00000")) {
//                    String guCode = parts[0].substring(2, 5);
//                    int guindex = parts[1].split(" ").length;
//                    String guName = parts[1].split(" ")[guindex - 1];
//                    System.out.println(getInsertSiGunGuQuery(guCode, cityCode, guName, id));
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private String getInsertSiDoQuery(String id, String sidoName) {
        return "INSERT INTO si_do (code, name) VALUES (" + id + ", '" + sidoName + "');";
    }

    private String getInsertSiGunGuQuery(String id, String siDoId, String guName, String legalAddressCode) {
        return "INSERT INTO si_gun_gu (code, si_do_id, name, legal_address_code) VALUES (" + id + ", " + siDoId + ", '" + guName + "', " + legalAddressCode + ");";
    }
}