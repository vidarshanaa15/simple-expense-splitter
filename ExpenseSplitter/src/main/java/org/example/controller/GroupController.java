package org.example.controller;

import org.example.entity.Group;
import org.example.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupService service;

    public GroupController(GroupService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group g) {
        return ResponseEntity.status(201).body(service.createGroup(g));
    }

    @GetMapping
    public List<Group> getAllGroups() {
        return service.getAllGroups();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        return service.getGroupById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
