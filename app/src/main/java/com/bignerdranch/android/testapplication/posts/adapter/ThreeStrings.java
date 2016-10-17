package com.bignerdranch.android.testapplication.posts.adapter;

/*
 * Created by Lidchanin on 17.10.2016.
 */

public class ThreeStrings {
    private String left;
    private String right;
    private String centre;

    String getLeft() {
        return left;
    }

    String getRight() {
        return right;
    }

    String getCentre() {
        return centre;
    }

    public ThreeStrings(String left, String right, String centre) {
        this.left = left;
        this.right = right;
        this.centre = centre;
    }

}