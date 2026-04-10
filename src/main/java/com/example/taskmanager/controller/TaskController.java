package com.example.taskmanager.controller;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin // 🔥 prevents request issues (important)
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    // GET all tasks
    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // GET task by ID
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable("id") Long id) {
        return taskRepository.findById(id).orElse(new Task());
    }

    // TEST API
    @GetMapping("/test")
    public String test() {
        return "Working";
    }

    // ADD task (🔥 FIXED)
    @PostMapping
    public Task addTask(@RequestBody Task task) {
        try {
            return taskRepository.save(task);
        } catch (Exception e) {
            e.printStackTrace(); // 🔥 shows real error in console
            return new Task();   // prevents crash
        }
    }

    // UPDATE task
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable("id") Long id, @RequestBody Task newTask) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(newTask.getTitle());
                    task.setDescription(newTask.getDescription());
                    task.setStatus(newTask.getStatus());
                    return taskRepository.save(task);
                })
                .orElse(new Task());
    }

    // DELETE task
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable("id") Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return "Deleted";
        } else {
            return "Not Found";
        }
    }
}