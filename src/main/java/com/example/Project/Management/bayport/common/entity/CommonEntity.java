package com.example.Project.Management.bayport.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@MappedSuperclass
public class CommonEntity {

    @Column(name="created_date", nullable=false,updatable = false)
    @CreationTimestamp
    private Date createdDate;

    @Column(name="updated_date")
    private Date updatedDate;

//    @Column(name="created_by")
//    private Long createdBy;


}