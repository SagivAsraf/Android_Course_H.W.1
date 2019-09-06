package com.example.whackamole;

public class Player {

    private String name;
    private int points;
    private int misses;

    public Player(String name, int points, int misses) {
        this.name = name;
        this.points = points;
        this.misses = misses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getMisses() {
        return misses;
    }

    public void setMisses(int misses) {
        this.misses = misses;
    }


}
