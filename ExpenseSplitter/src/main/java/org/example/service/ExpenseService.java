package org.example.service;

import org.example.entity.Expense;
import org.example.entity.Group;
import org.example.repository.ExpenseRepository;
import org.example.repository.GroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.*;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepo;
    private final GroupRepository groupRepo;

    public ExpenseService(ExpenseRepository expenseRepo, GroupRepository groupRepo) {
        this.expenseRepo = expenseRepo;
        this.groupRepo = groupRepo;
    }

    public Expense addExpense(Long groupId, Expense e) {
        Group g = groupRepo.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));

        if (e.getAmount() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be positive");

        if (!g.getMembers().contains(e.getPaidBy()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PaidBy must be a member of the group");

        e.setGroup(g);
        return expenseRepo.save(e);
    }

    public List<Expense> getExpensesByGroup(Long groupId) {
        if (!groupRepo.existsById(groupId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
        return expenseRepo.findByGroupId(groupId);
    }

    public Expense updateExpense(Long id, Expense updated) {
        Expense existing = expenseRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));
        existing.setTitle(updated.getTitle());
        existing.setAmount(updated.getAmount());
        existing.setPaidBy(updated.getPaidBy());
        return expenseRepo.save(existing);
    }

    public void deleteExpense(Long id) {
        if (!expenseRepo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found");
        expenseRepo.deleteById(id);
    }

    public Map<String, Double> getSummary(Long groupId) {
        Group g = groupRepo.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));

        List<Expense> expenses = expenseRepo.findByGroupId(groupId);
        Map<String, Double> balance = new HashMap<>();

        // initialize everyone with 0
        for (String member : g.getMembers()) balance.put(member, 0.0);

        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double share = total / g.getMembers().size();

        for (Expense exp : expenses)
            balance.put(exp.getPaidBy(), balance.get(exp.getPaidBy()) + exp.getAmount());

        for (String member : balance.keySet())
            balance.put(member, balance.get(member) - share);

        return balance;
    }
}
