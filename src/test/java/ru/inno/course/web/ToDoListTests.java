package ru.inno.course.web;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.inno.course.web.model.Task;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ToDoListTests {

    private static final String URL = "https://todo-app-sky.herokuapp.com/";
    private TaskService service;

    @BeforeEach
    public void setUp() {
        service = new TaskService(URL);
    }

    @Test
    @DisplayName("Получение пустого списка задач")
    public void canGetEmptyTaskList() throws IOException {
        service.deleteAllTasks();
        HttpGet getEmpty = new HttpGet(URL);
        HttpResponse emptyResponce = service.getClient().execute(getEmpty);
        assertEquals(200, emptyResponce.getStatusLine().getStatusCode());
        List<Task> emptyTaskList = service.getAllTasks();
        assertTrue(emptyTaskList.isEmpty());
    }

    @Test
    @DisplayName("Получение непустого списка задач")
    public void canGetTaskList() throws IOException {
        service.createEmptyTask();
        service.createEmptyTask();
        service.createEmptyTask();
        service.createEmptyTask();
        service.createEmptyTask();
        HttpGet getEmpty = new HttpGet(URL);
        HttpResponse emptyResponce = service.getClient().execute(getEmpty);
        assertEquals(200, emptyResponce.getStatusLine().getStatusCode());
        List<Task> emptyTaskList = service.getAllTasks();
        assertFalse(emptyTaskList.isEmpty());
    }

    @Test
    @DisplayName("Создание задачи")
    public void canCreateNewTask2() throws IOException {
        service.deleteAllTasks();
        Task myNewTask = service.createOneTask("SuperTask42");
        List<Task> myTaskList = service.getAllTasks();
        boolean x = myTaskList.contains(myNewTask);
        assertTrue(x);
    }

    @Test
    @DisplayName("Успешное удаление всех задач")
    public void canDeleteAllTasks3() throws IOException {
        service.createEmptyTask();
        service.createEmptyTask();
        service.createEmptyTask();
        service.createEmptyTask();
        service.createEmptyTask();
        service.deleteAllTasks();
        List<Task> testList = service.getAllTasks();
        assertEquals(0, testList.size());
    }

    @Test
    @DisplayName("Удаление одной задачи")
    public void canDeleteOneTask() throws IOException {
        Task taskForDeletion = service.createEmptyTask();
        int taskId = taskForDeletion.getId();
        service.deleteOneTask(taskId);
        List<Task> myTaskList = service.getAllTasks();
        boolean x = myTaskList.contains(taskForDeletion);
        assertFalse(x);
    }

    @Test
    @DisplayName("Редактирование названия задачи")
    public void canEditName() throws IOException {
        Task taskForEditName = service.createEmptyTask();
        int taskId = taskForEditName.getId();
        String newTaskTitle = "Title after edit";
        Task taskAfterEdit = service.editTaskName(taskForEditName, newTaskTitle);
        assertEquals(newTaskTitle, taskAfterEdit.getTitle());

    }

    @Test
    @DisplayName("Отметить задачу выполненной")
    public void canMakeTaskCompleted() throws IOException {
        Task taskToBeCompleted = service.createEmptyTask();
        int taskId = taskToBeCompleted.getId();
        Task taskIsCompleted = service.makeTaskCompleted(taskToBeCompleted);
        assertTrue(taskIsCompleted.isCompleted());
    }

    
}

