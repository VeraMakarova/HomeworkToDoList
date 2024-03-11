package ru.inno.course.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import ru.inno.course.web.model.Task;

import java.io.IOException;
import java.util.List;

public class TaskService {

    private HttpClient client;
    private final String URL;

    public TaskService(String url) {
        URL = url;
        this.client = HttpClientBuilder.create().build();
    }

    public HttpClient getClient() {
        return client;
    }

    public List<Task> getAllTasks() throws IOException {
        HttpGet allTasks = new HttpGet(URL);
        HttpResponse allTasksList = client.execute(allTasks);
        String responseBody = EntityUtils.toString(allTasksList.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(responseBody, new TypeReference<>() {
        });
    }

    public Task createEmptyTask() throws IOException {
        return createOneTask("some task to be deleted");
    }

    public Task createOneTask(String title) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        HttpPost oneTask = new HttpPost(URL);
        String requestBody = "{\"title\": \"" + title + "\"}";
        StringEntity someEntity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
        oneTask.setEntity(someEntity);
        HttpResponse createResponse = client.execute(oneTask);
        String responseBody = EntityUtils.toString(createResponse.getEntity());
        Task t = mapper.readValue(responseBody, Task.class);
        return t;
    }

    public void deleteAllTasks() throws IOException {
        HttpDelete deleteAll = new HttpDelete(URL);
        client.execute(deleteAll);
    }

    public void deleteOneTask(int id) throws IOException {
        HttpDelete deleteOne = new HttpDelete(URL + id);
        client.execute(deleteOne);
    }

    public Task editTaskName(Task taskForEdit, String newName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        int id = taskForEdit.getId();
        HttpPatch patchName = new HttpPatch(URL + id);
        String requestBody = "{\"title\": \"" + newName + "\"}";
        StringEntity someEntity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
        patchName.setEntity(someEntity);
        HttpResponse createResponse = client.execute(patchName);
        String responseBody = EntityUtils.toString(createResponse.getEntity());
        Task t = mapper.readValue(responseBody, Task.class);
        return t;
    }

    public Task makeTaskCompleted(Task notCompletedTask) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        int id = notCompletedTask.getId();
        HttpPatch patchCompleted = new HttpPatch(URL + id);
        String requestBody2 = "{\"completed\": \"true\"}";
        StringEntity someEntity = new StringEntity(requestBody2, ContentType.APPLICATION_JSON);
        patchCompleted.setEntity(someEntity);
        HttpResponse createResponse = client.execute(patchCompleted);
        String responseBody = EntityUtils.toString(createResponse.getEntity());
        Task t = mapper.readValue(responseBody, Task.class);
        return t;
    }

}
