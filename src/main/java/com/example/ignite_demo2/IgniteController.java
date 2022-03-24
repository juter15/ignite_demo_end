package com.example.ignite_demo2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IgniteController {
    private final TestRepository testRepository;

    @GetMapping("/get")
    public ResponseEntity cacheGet(){

         Iterable<test1> iterableTest1 =  testRepository.findAll();

         List<test1> listTest1;
         while (iterableTest1.iterator().hasNext()){
             log.info("get: {}", iterableTest1.iterator().next());
         }

        return ResponseEntity.ok(null);
    }

    @PostMapping("/insert")
    public ResponseEntity cacheInsert(@RequestBody test1 test1){
        log.info("insert: {}", test1);
        Map<Long, test1> test1s = new HashMap<>();

        test1s.put(test1.getId(), test1);
        testRepository.save(test1s);

        return ResponseEntity.ok(null);
    }
}
