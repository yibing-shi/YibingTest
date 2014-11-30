package com.yshi.toy.robot.entity;

import com.yshi.toy.robot.control.Position;

public class Tabletop {

    private final int width;
    private final int height;

    public Tabletop(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
