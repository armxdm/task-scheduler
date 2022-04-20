package com.rbank;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.rbank.Task.TaskStatus.COMPLETED;
import static com.rbank.Task.TaskStatus.NOT_COMPLETED;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static final String DATE_FORMAT = "MM/dd/yyyy";
    private static List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {

        while (true) {
            System.out.println("Select a task:");
            System.out.println("(1) Create a task");
            System.out.println("(2) Add a task dependency");
            System.out.println("(3) Display tasks");
            System.out.println("(4) Complete a task");
            System.out.println("(5) Exit");
            String inputStr = scanner.next();

            if (inputStr.matches("\\d+")) {
                int input = Integer.parseInt(inputStr);
                switch (input) {
                    case 1: createTask();
                        break;
                    case 2: addTaskDependency();
                        break;
                    case 3: displayTasks();
                        break;
                    case 4: completeTask();
                        break;
                    case 5: System.exit(0);
                        break;
                    default: System.out.println("Error: Invalid input.");
                }
            } else {
                System.out.println("Error: Invalid input.\n");
            }
        }
    }

    private static void createTask() {
        System.out.print("Task name: ");
        String taskName = scanner.next();

        if(taskName.isEmpty()) {
            System.out.println("Task name must not be empty.");
            return;
        }

        if (tasks.stream().anyMatch(task -> task.getTaskName().equals(taskName))) {
            System.out.println("Task name already existing.");
            return;
        }

        LocalDate startDate, endDate;
        try {
            System.out.print("Start date (mm/dd/yyyy): ");
            String startDateStr = scanner.next();
            startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern(DATE_FORMAT));

            System.out.print("End date (mm/dd/yyyy): ");
            String endDateStr = scanner.next();
            endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern(DATE_FORMAT));
        } catch (Exception e) {
            System.out.println("Error: Invalid date format");
            return;
        }

        Task task = new Task();
        task.setTaskName(taskName);
        task.setStartDate(startDate);
        task.setEndDate(endDate);
        tasks.add(task);
        System.out.println("TASK " + taskName + " is added.");
    }


    private static void addTaskDependency() {
        if (tasks.isEmpty()) {
            System.out.println("No added tasks.");
            return;
        }

        Task task = getTaskName();
        if (task == null) {
            System.out.println("Error: Task name not found.");
            return;
        }

        Task taskDependency = null;
        System.out.print("Task name dependency: ");
        String taskNameDependency = scanner.next();

        if (taskNameDependency.equals(task.getTaskName())) {
            System.out.println("Error: Task cannot be depend to itself");
            return;
        }

        for (Task t: tasks) {
            if (t.getTaskName().equals(taskNameDependency)) {
                if (t.getStatus().equals(COMPLETED)) {
                    System.out.println("Error: Task dependency is already completed.");
                    return;
                }

                for (Task dependency : t.getTaskDependencies()) {
                    if (dependency.equals(task)) {
                        System.out.println("Error: Task cannot be depended to each other.");
                        return;
                    }
                }

                taskDependency = t;
                break;
            }
        }

        if (taskDependency == null) {
            System.out.println("Error: Task name not found.");
            return;
        }

        for (Task t: tasks) {
            if (t.equals(task)) {
                Set<Task> taskDependencies = t.getTaskDependencies();

                for (Task dependency : taskDependencies) {
                    if (dependency.getTaskName().equals(taskNameDependency)) {
                        System.out.println("Error: Task dependency is already added.");
                        return;
                    }
                }

                if (t.getStartDate().isBefore(taskDependency.getStartDate()) && t.getEndDate().isBefore(taskDependency.getEndDate())) {
                    System.out.println("Task dependency's schedule must be before task's schedule");
                    return;
                }

                taskDependencies.add(taskDependency);
                System.out.println("Task " + task.getTaskName() + " is now depended to task " + taskNameDependency);
            }
        }
    }

    private static void displayTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No added tasks.");
        }

        int i = 1;
        for (Task task : tasks) {
            System.out.println(i);
            System.out.println(task.printTaskDetails());
            i++;
        }
    }

    private static void completeTask() {
        if (tasks.isEmpty()) {
            System.out.println("No added tasks.");
            return;
        }

        Task task = getTaskName();
        if (task == null) {
            System.out.println("Error: Task name not found.");
            return;
        }

        for (Task t: tasks) {
            if (t.equals(task)) {
                Set<Task> dependencies = t.getTaskDependencies();
                for (Task dependency : dependencies) {
                    if (dependency.getStatus().equals(NOT_COMPLETED)) {
                        System.out.println("Error: Task cannot be completed because it has uncompleted task dependencies.");
                        return;
                    }
                }

                t.setStatus(COMPLETED);
            }
        }

        for (Task t : tasks) {
            for (Task dependency :t.getTaskDependencies()) {
                if (dependency.getTaskName().equals(task.getTaskName())) {
                    dependency.setStatus(COMPLETED);
                }
            }
        }

        System.out.println("Task " + task.getTaskName() + " is now completed");
    }

    private static Task getTaskName() {
        System.out.print("Task name: ");
        String taskName = scanner.next();

        return tasks.stream().filter(t -> t.getTaskName().equals(taskName)).findAny().orElse(null);
    }
}
