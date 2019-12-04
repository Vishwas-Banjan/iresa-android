package com.vbanjan.iresa.Model;

public class Artist {
    String name;

    public Artist(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
