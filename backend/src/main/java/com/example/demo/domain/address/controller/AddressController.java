package com.example.demo.domain.address.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AddressController {

    @GetMapping("/address")
    public String home() {
        return "address.html";
    }

}
