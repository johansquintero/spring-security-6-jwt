package com.springsecurity.security.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/app")
@PreAuthorize("denyAll()")
public class AppController {
    @GetMapping("/hello")
    @PreAuthorize("permitAll()")
    public String hello() {
        return "hello world";
    }

    @GetMapping("/hello-secured")
    @PreAuthorize("hasAuthority('READ')")
    public String helloSecured() {
        return "hello secured";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE')")
    public ResponseEntity<String> create (@RequestBody Map<String,String> request){
        var response = "Created"+"\t"+request.get("name");
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('UPDATE')")
    public ResponseEntity<String> update (@RequestBody Map<String,String> request){
        var response = "Updated"+"\t"+request.get("name");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{name}")
    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<String> update (@PathVariable(name = "name") String name){
        var response = "Deleted"+"\t"+name;
        return ResponseEntity.ok(response);
    }

    @PatchMapping
    @PreAuthorize("hasAuthority('REFACTOR')")
    public ResponseEntity<String> refactor (@RequestBody Map<String,String> request){
        var response = "refactored"+"\t"+request.get("name");
        return ResponseEntity.ok(response);
    }
}
