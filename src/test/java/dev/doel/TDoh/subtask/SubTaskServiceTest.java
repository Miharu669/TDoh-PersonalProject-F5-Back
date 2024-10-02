package dev.doel.TDoh.subtask;


import dev.doel.TDoh.subtask.subtask_exceptions.SubTaskNotFoundException;
import dev.doel.TDoh.task.Task;
import dev.doel.TDoh.task.TaskRepository;
import dev.doel.TDoh.task.task_exceptions.TaskNotFoundException;
import dev.doel.TDoh.users.User;
import dev.doel.TDoh.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubTaskServiceTest {

    @Mock
    private SubTaskRepository subTaskRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SubTaskService subTaskService;

    private User user;
    private Task task;
    private SubTask subTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L); 

        task = new Task();
        task.setId(1L);
        task.setUser(user);

        subTask = new SubTask();
        subTask.setId(1L);
        subTask.setTask(task);
        subTask.setTitle("Test SubTask");
        subTask.setDescription("This is a test subtask");
        subTask.setDone(false);
    }

    @Test
    void testCreateSubTask_Success() {
        SubTaskDTO subTaskDTO = new SubTaskDTO();
        subTaskDTO.setTaskId(1L);
        subTaskDTO.setTitle("Test SubTask");
        subTaskDTO.setDescription("This is a test subtask");
        subTaskDTO.setDone(false);

        when(taskRepository.findById(subTaskDTO.getTaskId())).thenReturn(Optional.of(task));
        when(subTaskRepository.save(any(SubTask.class))).thenReturn(subTask);

        SubTaskDTO createdSubTask = subTaskService.createSubTask(subTaskDTO, user.getId());

        assertNotNull(createdSubTask);
        assertEquals(subTask.getId(), createdSubTask.getId());
        assertEquals("Test SubTask", createdSubTask.getTitle());
        verify(subTaskRepository, times(1)).save(any(SubTask.class));
    }

    @Test
    void testCreateSubTask_TaskNotFound() {
        SubTaskDTO subTaskDTO = new SubTaskDTO();
        subTaskDTO.setTaskId(1L);

        when(taskRepository.findById(subTaskDTO.getTaskId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class, () -> {
            subTaskService.createSubTask(subTaskDTO, user.getId());
        });

        assertEquals("Task not found", exception.getMessage());
        verify(subTaskRepository, never()).save(any(SubTask.class));
    }

    @Test
    void testGetSubTasksByTaskId_Success() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(subTaskRepository.findByTaskId(task.getId())).thenReturn(Arrays.asList(subTask));

        List<SubTaskDTO> subTasks = subTaskService.getSubTasksByTaskId(task.getId(), user.getId());

        assertEquals(1, subTasks.size());
        assertEquals("Test SubTask", subTasks.get(0).getTitle());
        verify(subTaskRepository, times(1)).findByTaskId(task.getId());
    }

    @Test
    void testGetSubTasksByTaskId_TaskNotFound() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class, () -> {
            subTaskService.getSubTasksByTaskId(task.getId(), user.getId());
        });

        assertEquals("Task not found", exception.getMessage());
        verify(subTaskRepository, never()).findByTaskId(anyLong());
    }

    @Test
    void testGetSubTaskById_Success() {
        when(subTaskRepository.findById(subTask.getId())).thenReturn(Optional.of(subTask));

        SubTaskDTO foundSubTask = subTaskService.getSubTaskById(subTask.getId(), user.getId());

        assertNotNull(foundSubTask);
        assertEquals(subTask.getId(), foundSubTask.getId());
        verify(subTaskRepository, times(1)).findById(subTask.getId());
    }

    @Test
    void testGetSubTaskById_NotFound() {
        when(subTaskRepository.findById(subTask.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(SubTaskNotFoundException.class, () -> {
            subTaskService.getSubTaskById(subTask.getId(), user.getId());
        });

        assertEquals("SubTask not found", exception.getMessage());
        verify(subTaskRepository, times(1)).findById(subTask.getId());
    }

    

    @Test
    void testUpdateSubTask_NotFound() {
        SubTaskDTO subTaskDTO = new SubTaskDTO();
        when(subTaskRepository.findById(subTask.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(SubTaskNotFoundException.class, () -> {
            subTaskService.updateSubTask(subTask.getId(), subTaskDTO, user.getId());
        });

        assertEquals("SubTask not found", exception.getMessage());
        verify(subTaskRepository, never()).save(any(SubTask.class));
    }

    @Test
    void testDeleteSubTask_Success() {
        when(subTaskRepository.findById(subTask.getId())).thenReturn(Optional.of(subTask));

        subTaskService.deleteSubTask(subTask.getId(), user.getId());

        verify(subTaskRepository, times(1)).delete(subTask);
    }

    @Test
    void testDeleteSubTask_NotFound() {
        when(subTaskRepository.findById(subTask.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(SubTaskNotFoundException.class, () -> {
            subTaskService.deleteSubTask(subTask.getId(), user.getId());
        });

        assertEquals("SubTask not found", exception.getMessage());
        verify(subTaskRepository, never()).delete(any(SubTask.class));
    }
}
