package org.example.service;

import org.example.entity.Group;
import org.example.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    private final GroupRepository repo;

    public GroupService(GroupRepository repo) {
        this.repo = repo;
    }

    public Group createGroup(Group g) {
        return repo.save(g);
    }

    public List<Group> getAllGroups() {
        return repo.findAll();
    }

    public Optional<Group> getGroupById(Long id) {
        return repo.findById(id);
    }
}
