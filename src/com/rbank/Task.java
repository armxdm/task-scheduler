package com.rbank;

import java.time.LocalDate;
import java.util.*;

public class Task {

    private String taskName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Set<Task> taskDependencies = new HashSet<>();
    private TaskStatus status = TaskStatus.NOT_COMPLETED;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<Task> getTaskDependencies() {
        return taskDependencies;
    }

    public void setTaskDependencies(Set<Task> taskDependencies) {
        this.taskDependencies = taskDependencies;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public enum TaskStatus {
        COMPLETED,
        NOT_COMPLETED
    }

    @Override
    public String toString() {
        return taskName;
    }

    public String printTaskDetails() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Task Name: ").append(taskName).append("\n");
        sb.append("Start date: ").append(startDate).append("\n");
        sb.append("End date: ").append(endDate).append("\n");
        sb.append("Task Dependencies: ").append(taskDependencies.isEmpty() ? "None" : taskDependencies).append("\n");
        sb.append("Status: ").append(status).append("\n");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskName, task.taskName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName);
    }
}
