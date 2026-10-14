package com.generated.app;

public class Vehicle {
    public final int id;
    public int row;
    public int col;
    public final int length;
    public final boolean isHorizontal;
    public final int color;
    public final boolean isRedCar;

    public Vehicle(int id, int row, int col, int length, boolean isHorizontal, int color, boolean isRedCar) {
        this.id = id;
        this.row = row;
        this.col = col;
        this.length = length;
        this.isHorizontal = isHorizontal;
        this.color = color;
        this.isRedCar = isRedCar;
    }

    public Vehicle copy() {
        return new Vehicle(id, row, col, length, isHorizontal, color, isRedCar);
    }

    public boolean occupies(int r, int c) {
        if (isHorizontal) {
            return r == row && c >= col && c < col + length;
        } else {
            return c == col && r >= row && r < row + length;
        }
    }
}
