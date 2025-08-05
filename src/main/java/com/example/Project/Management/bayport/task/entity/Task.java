package com.example.Project.Management.bayport.task.entity;

import com.example.Project.Management.bayport.auth.user.entity.User;
import com.example.Project.Management.bayport.common.entity.CommonEntity;
import com.example.Project.Management.bayport.common.entity.Status;
import com.example.Project.Management.bayport.project.entity.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tab_task")
public class Task extends CommonEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id_generator")
    @SequenceGenerator(name = "task_id_generator", sequenceName = "seq_task", initialValue = 1, allocationSize = 1)
    @Column(name = "id", nullable = false, precision = 11)
    private Long id;

    @GeneratedValue
    @UuidGenerator
    @Column(name="tasks_uid",nullable = false)
    private UUID tasksUid;

    @PrePersist
    protected void onCreate() {
        setTasksUid(java.util.UUID.randomUUID());
    }

    @Column(name = "task_name", nullable = false  ,length = 20)
    private String taskName;

    @Column(name = "task_description", length = 50, nullable = false)
    private String  taskDescription;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="project_id" , nullable=false)
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="user_id" , nullable=false)
    private User user;

    @Column(name = "start_date", length = 50, nullable = false)
    private Date startDate;
    @Column(name = "end_date", length = 50, nullable = false)
    private Date endDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status taskStatus= Status.IN_PROGRESS;




}
