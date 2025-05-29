package com.NadraWraper.API;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Time;
import java.time.LocalTime;

//@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TestController {
    @GetMapping("/echo")
    public String echo() {
        long min = Time.valueOf(LocalTime.now()).getTime();
        return "ALIVE - " + min;
    }
}
