package org.example;

import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {
       //create completable future
        CompletableFuture<String> completableFuture = new CompletableFuture<>(); //create completableFuture by using constructor
        boolean checkCompleted = completableFuture.complete("completed here....");
        System.out.println(checkCompleted);


    }
}