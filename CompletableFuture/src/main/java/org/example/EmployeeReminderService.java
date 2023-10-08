package org.example;

import org.example.database.EmployeeDatabase;
import org.example.dto.Employee;

import java.util.concurrent.*;
import java.util.stream.Collectors;

public class EmployeeReminderService {
    //********TODO*********//
    //get all employees from the db
    //filter out all new joined employee
    //check if training activity is pending for employee
    //get employees email id
    //send reminder notification to employees
    public CompletableFuture<Void> sendReminderEmployee() {
        Executor executor = Executors.newFixedThreadPool(10);
        CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("fetchEmployee : " + Thread.currentThread().getName());
            return EmployeeDatabase.fetchEmployees();
        }, executor).thenApplyAsync(employees -> {
            System.out.println("filter new joiner employee  : " + Thread.currentThread().getName());
            return employees.stream()
                    .filter(employee -> "TRUE".equals(employee.getNewJoiner()))
                    .collect(Collectors.toList());
        }, executor).thenApplyAsync((employees) -> {
            System.out.println("filter training not complete employee  : " + Thread.currentThread().getName());
            return employees.stream()
                    .filter(employee -> "TRUE".equals(employee.getLearningPending()))
                    .collect(Collectors.toList());
        }, executor).thenApplyAsync((employees) -> {
            System.out.println("get emails  : " + Thread.currentThread().getName());
            return employees.stream().map(Employee::getEmail).collect(Collectors.toList());
        }, executor).thenAcceptAsync((emails) -> {
            System.out.println("send email  : " + Thread.currentThread().getName());
            emails.forEach(EmployeeReminderService::sendEmail);
        }, executor);
        return completableFuture;
    }

    public static void sendEmail(String email) {
        System.out.println("sending training reminder email to : " + email);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        EmployeeReminderService service = new EmployeeReminderService();
        service.sendReminderEmployee().get();
    }
}
