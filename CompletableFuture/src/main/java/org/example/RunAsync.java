package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.Employee;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RunAsync {
    public void saveEmployee(File json) throws ExecutionException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture
                .runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Employee> employees = objectMapper
                            .readValue(json, new TypeReference<List<Employee>>() {
                            });
                    //write logic to save list of employees to database
                    //here want to print the list of employees
                    System.out.println("Thread:" + Thread.currentThread().getName());
                    System.out.println(employees.size());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        voidCompletableFuture.get();
    }

    //using it by lamda expression
    public void saveEmployeesWithLamda(File json) throws ExecutionException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        Executor executor = Executors.newFixedThreadPool(5);
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(()->{
            try {
                List<Employee> employees = objectMapper
                        .readValue(json, new TypeReference<List<Employee>>() {
                        });
                //write logic t save list of employee to database
                //repository.saveAll(employees);
                System.out.println("Thread : " + Thread.currentThread().getName());
                System.out.println(employees.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
        },executor);
        completableFuture.get();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RunAsync runAsync = new RunAsync();
        runAsync.saveEmployee(new File("employees.json"));
        runAsync.saveEmployeesWithLamda(new File("employees.json"));
    }
}
