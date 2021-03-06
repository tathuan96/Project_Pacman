package com.huantt.pacmangame.model;

import com.huantt.pacmangame.gui.GUI;
import manager.ImageLoader;

import java.awt.*;

/**
 * Created by Huan on 7/22/2016.
 */
public class Pacman extends GameObject {
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int RIGHT = 2;
    public static final int LEFT = 3;
    public static final int SIZE = GameObject.SIZE_GAME_OBJECT;
    private int numberOfImage;
    private int countDownImage;
    private int orient;
    private static final int MAX_COUNTDOWN = 60;
    private int delay;
    private int turn;


    public Pacman(int x, int y, int orient, int delay) {
        this.x = x;
        this.y = y;
        this.orient = orient;
        this.delay = delay;
        turn = 3;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getOrient() {
        return orient;
    }

    public void setOrient(int orient) {
        numberOfImage = 1; // Khi chuyển hướng thì hình bắt đầu là hình 1
        this.orient = orient;

    }

    public int getTurn() {
        return turn;
    }

    public int die() {
        return --turn;
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        switch (orient) {
            case LEFT:
                graphics2D.drawImage(ImageLoader.IMG_PACMAN_LEFT[numberOfImage], x, y, SIZE, SIZE, null);
                break;
            case DOWN:
                graphics2D.drawImage(ImageLoader.IMG_PACMAN_DOWN[numberOfImage], x, y, SIZE, SIZE, null);
                break;
            case RIGHT:
                graphics2D.drawImage(ImageLoader.IMG_PACMAN_RIGHT[numberOfImage], x, y, SIZE, SIZE, null);
                break;
            case UP:
                graphics2D.drawImage(ImageLoader.IMG_PACMAN_UP[numberOfImage], x, y, SIZE, SIZE, null);
                break;
        }

    }

    public void move(int count) {

        if (countDownImage == 0) {
            if (numberOfImage < 3) {
                numberOfImage++;
            } else numberOfImage = 0;
            countDownImage = MAX_COUNTDOWN;
        } else {
            countDownImage -= 10;
        }

        if (count % delay == 0) {
            switch (orient) {
                case LEFT:
                    x--;
                    if (x <= -SIZE) {
                        x = GUI.WIDTH_FRAME;
                    }
                    break;
                case DOWN:
                    y++;
                    if (y >= GUI.HEIGHT_FRAME) {
                        y = -SIZE;
                    }
                    break;
                case RIGHT:
                    x++;
                    if (x >= GUI.WIDTH_FRAME) {
                        x = -SIZE;
                    }
                    break;
                case UP:
                    y--;
                    if (y <= -SIZE) {
                        y = GUI.HEIGHT_FRAME;
                    }
                    break;

            }
        }
    }

    public boolean collision(Item iteam) {
        int xRec = x;
        int yRec = y;
        switch (orient) {
            case UP:
                yRec--;
                break;
            case DOWN:
                yRec++;
                break;
            case LEFT:
                xRec--;
                break;
            case RIGHT:
                xRec++;
                break;
        }
        Rectangle recPacman = new Rectangle(xRec, yRec, SIZE, SIZE);
        switch (iteam.getType()) {
            case Item.TYPE_STONE:
                boolean isIntersect = recPacman.intersects(iteam.getRItem());
                if (isIntersect) { // Nếu va đá thì để pacman hình 1 (Mở mồm)
                    numberOfImage = 1;
                }
                return isIntersect;
            case Item.TYPE_BEAN_NORMAL:
                Rectangle rNomal = recPacman.intersection(iteam.getRItem());
                return (Item.SIZE / 2 < rNomal.getWidth() && Item.SIZE / 3 < rNomal.getHeight());
            case Item.TYPE_BULLET:
                Rectangle rPower = recPacman.intersection(iteam.getRItem());
                return (Item.SIZE / 2 < rPower.getWidth() && Item.SIZE / 3 < rPower.getHeight());
        }
        return false;
    }
}
