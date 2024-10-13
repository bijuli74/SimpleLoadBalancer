package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @GetMapping(value = "/hello")
  public String getHello() throws IOException {
    URL url = new URL("http://checkip.amazonaws.com/");
    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
    String ip = br.readLine();
    return "Hello from IP: " + ip + " at time : " + LocalDateTime.now();
  }

}
