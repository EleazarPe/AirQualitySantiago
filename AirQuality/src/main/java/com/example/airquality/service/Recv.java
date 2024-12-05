//package com.example.airquality.service;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Service;
//
//
//@Service
//public class Recv {
//    @RabbitListener(queues = "hello")
//    public void receiveMessage(String message) {
//        System.out.println(" [*] Waiting for messages. To exit press CTRL+C "+ message);
//    }
//}