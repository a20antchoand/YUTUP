package com.example.practica5;

import android.graphics.Rect;
import android.widget.ImageView;

public class itemTocable {

    int id;
    Rect rect;
    ImageView imageView;



    public itemTocable(int id, Rect rect, ImageView imageView) {
        this.id = id;
        this.rect = rect;
        this.imageView = imageView;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
