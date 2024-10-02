package dev.doel.TDoh.task;


import dev.doel.TDoh.task.task_exceptions.TaskNotFoundException;
import dev.doel.TDoh.users.User;
import dev.doel.TDoh.users.user_exceptions.UserNotFoundException;
import dev.doel.TDoh.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Task task;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setScore(100);

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("This is a test task.");
        task.setDone(false);
        task.setUser(user);
        task.setSubTasks(Collections.emptyList());
    }

    @Test
    void testGetAllTasksForUser() {
        when(taskRepository.findAllByUserId(1L)).thenReturn(List.of(task));

        List<TaskDTO> tasks = taskService.getAllTasksForUser(1L);

        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
        verify(taskRepository).findAllByUserId(1L);
    }

    @Test
    void testGetTaskByIdForUser() {
        when(taskRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(task));

        TaskDTO foundTask = taskService.getTaskByIdForUser(1L, 1L);

        assertEquals("Test Task", foundTask.getTitle());
        verify(taskRepository).findByIdAndUserId(1L, 1L);
    }

    @Test
    void testGetTaskByIdForUser_NotFound() {
        when(taskRepository.findByIdAndUserId(2L, 1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskByIdForUser(2L, 1L);
        });

        assertEquals("Task not found for the user", exception.getMessage());
    }

    @Test
    void testCreateTaskForUser() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("New Task");
        taskDTO.setDescription("This is a new task.");
        taskDTO.setDone(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        TaskDTO createdTask = taskService.createTaskForUser(taskDTO, 1L);

        assertEquals("New Task", createdTask.getTitle());
        verify(userRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testCreateTaskForUser_UserNotFound() {
        TaskDTO taskDTO = new TaskDTO();

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            taskService.createTaskForUser(taskDTO, 1L);
        });

        assertEquals("User not found", exception.getMessage());
    }

    


    @Test
    void testUpdateTaskForUser_TaskNotFound() {
        TaskDTO taskDTO = new TaskDTO();

        when(taskRepository.findByIdAndUserId(2L, 1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.updateTaskForUser(2L, taskDTO, 1L);
        });

        assertEquals("Task not found for the user", exception.getMessage());
    }

    @Test
    void testDeleteTaskForUser() {
        when(taskRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(task));

        taskService.deleteTaskForUser(1L, 1L);

        verify(taskRepository).delete(task);
    }

    @Test
    void testDeleteTaskForUser_TaskNotFound() {
        when(taskRepository.findByIdAndUserId(2L, 1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.deleteTaskForUser(2L, 1L);
        });

        assertEquals("Task not found for the user", exception.getMessage());
    }
}
