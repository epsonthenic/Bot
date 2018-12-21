package com.spt.engine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HelloController {
    static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/hello")
    @ResponseBody
    public ResponseEntity<String> HelloWelcome(){

        LOGGER.info("in HelloController");
        return ResponseEntity.ok().body("Hello Word");
    }

}
