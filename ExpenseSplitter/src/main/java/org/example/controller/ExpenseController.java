package org.example.controller;

import org.example.entity.Expense;
import org.example.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class ExpenseController {
    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @PostMapping("/groups/{id}/expenses")
    public ResponseEntity<Expense> addExpense(@PathVariable Long id, @RequestBody Expense e) {
        return ResponseEntity.status(201).body(service.addExpense(id, e));
    }

    @GetMapping("/groups/{id}/expenses")
    public List<Expense> getExpenses(@PathVariable Long id) {
        return service.getExpensesByGroup(id);
    }

    @GetMapping("/groups/{id}/summary")
    public Map<String, Double> getSummary(@PathVariable Long id) {
        return service.getSummary(id);
    }

    @PutMapping("/expenses/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody Expense e) {
        return ResponseEntity.ok(service.updateExpense(id, e));
    }

    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        service.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
