package com.yshi.toy.robot.entity;

import java.util.UUID;

public class Robot {

    private final UUID id;
    private final String name;

    public Robot(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Robot{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
