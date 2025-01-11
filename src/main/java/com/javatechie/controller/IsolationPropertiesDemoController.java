package com.javatechie.controller;

import com.javatechie.service.isolation.ReadCommittedDemo;
import com.javatechie.service.isolation.ReadUncommittedDemo;
import com.javatechie.service.isolation.RepeatableReadDemo;
import com.javatechie.service.isolation.SerializableIsolationDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/isolation")
public class IsolationPropertiesDemoController {

    @Autowired
    private ReadUncommittedDemo readUncommittedDemo;

    @Autowired
    private ReadCommittedDemo readCommittedDemo;

    @Autowired
    private RepeatableReadDemo repeatableReadDemo;

    @Autowired
    private SerializableIsolationDemo serializableIsolationDemo;

    @GetMapping("/readUncommitted/{id}")
    public String validateReadUncommittedData(@PathVariable int id) throws InterruptedException {
        readUncommittedDemo.testReadUncommitted(id);
        return "SUCCESS ! check the logs !";
    }

    @GetMapping("/readCommitted/{id}")
    public String validateReadCommittedData(@PathVariable int id) throws InterruptedException {
        readCommittedDemo.testReadCommitted(id);
        return "SUCCESS ! check the logs !";
    }

    @GetMapping("/repeatableRead/{id}")
    public String validateRepeatableRead(@PathVariable int id) throws InterruptedException {
        repeatableReadDemo.demonstrateRepeatableRead(id);
        return "SUCCESS ! check the logs !";
    }

    @GetMapping("/serializableRead/{id}")
    public String validateSerializableDemo(@PathVariable int id) throws InterruptedException {
        serializableIsolationDemo.testSerializableIsolation(id);
        return "SUCCESS ! check the logs !";
    }


}
