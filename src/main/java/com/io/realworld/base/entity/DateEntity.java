package com.io.realworld.base.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.ZonedDateTime;


@Getter
@Setter
@MappedSuperclass
public class DateEntity {

    @Column(name = "created_at")
    private ZonedDateTime createdDate;

    @Column(name = "update_at")
    private ZonedDateTime modifiedDate;

    @PrePersist
    void prePersist() {
        this.createdDate = ZonedDateTime.now();
        this.modifiedDate = ZonedDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        createdDate = ZonedDateTime.now();
    }


}
