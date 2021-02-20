package com.example.springjunit.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class Study {

    private StudyStatus status;

    private int limit;
    private String name;

    private Member owner;
    private LocalDateTime createTime;

    public Study(int limit) {
        status = StudyStatus.DRAFT;
        if (limit <= 0) {
            throw new IllegalArgumentException("limit은 0보다 커야한다");
        }
        this.limit = limit;
    }

    public Study(int limit, String name) {
        status = StudyStatus.DRAFT;
        if (limit <= 0) {
            throw new IllegalArgumentException("limit은 0보다 커야한다");
        }
        this.limit = limit;
        this.name = name;
        this.createTime = LocalDateTime.now();
    }

    public void open() {
        status = StudyStatus.OPENED;
    }

}
