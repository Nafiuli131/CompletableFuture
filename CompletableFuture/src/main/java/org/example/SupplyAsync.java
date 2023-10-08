package org.example;

import org.example.database.EmployeeDatabase;
import org.example.dto.Employee;

import java.util.List;
import java.util.concurrent.*;

public class SupplyAsync {

    public List<Employee> getEmployees() throws ExecutionException, InterruptedException {
        Executor executor = Executors.newCachedThreadPool();
        CompletableFuture<List<Employee>> listCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("Executed by: "+Thread.currentThread().getName());
            return EmployeeDatabase.fetchEmployees();
        },executor);
        return listCompletableFuture.get();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SupplyAsync supplyAsync = new SupplyAsync();
        List<Employee> supplyAsyncEmployees = supplyAsync.getEmployees();
        System.out.println(supplyAsyncEmployees.size());

    }
}
