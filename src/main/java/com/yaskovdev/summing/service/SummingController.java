package com.yaskovdev.summing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.Integer.parseInt;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
class SummingController {

    private static final String END_COMMAND = "end";

    private final SummingService summingService;

    @PostMapping("/numbers")
    ResponseEntity<Integer> numbers(@RequestBody final String value) {
        return ok(END_COMMAND.equals(value) ? summingService.release() : summingService.submitAndWait(parseInt(value)));
    }
}
