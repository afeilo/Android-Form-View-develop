package com.xiefei.mylibrary;

/**
 * Created by Raquib-ul-Alam Kanak on 7/21/2014.
 * Website: http://april-shower.com
 */
public class FormEvent {
    private String text;
    private float x;
    private float y;
    private int backgroundColor;

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "FormEvent{" +
                "text='" + text + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", backgroundColor=" + backgroundColor +
                '}';
    }
}
