//package com.javatechie.aspect;
//
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class TransactionLogger {
//
//    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
//    public void logBeforeTransactionStart() {
//        System.out.println("Transaction started.");
//    }
//
//    @After("@annotation(org.springframework.transaction.annotation.Transactional)")
//    public void logAfterTransactionCommit() {
//        System.out.println("Transaction committed.");
//    }
//
//    @AfterThrowing("@annotation(org.springframework.transaction.annotation.Transactional)")
//    public void logAfterTransactionRollback() {
//        System.out.println("Transaction rolled back.");
//    }
//}
