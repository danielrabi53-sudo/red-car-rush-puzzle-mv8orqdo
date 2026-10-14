package com.generated.app;

import java.util.ArrayList;
import java.util.List;

public class LevelData {
    public static final int BOARD_SIZE = 6;
    public static final int EXIT_ROW = 2;
    public static final int EXIT_COL = 5;

    public static final int RED_CAR_COLOR = 0xFFE11D48;
    public static final int COLOR_AMBER = 0xFFF59E0B;
    public static final int COLOR_SKY = 0xFF0284C7;
    public static final int COLOR_EMERALD = 0xFF10B981;
    public static final int COLOR_PURPLE = 0xFF8B5CF6;
    public static final int COLOR_ORANGE = 0xFFEA580C;
    public static final int COLOR_PINK = 0xFFEC4899;
    public static final int COLOR_LIME = 0xFF84CC16;
    public static final int COLOR_INDIGO = 0xFF6366F1;
    public static final int COLOR_TEAL = 0xFF14B8A6;

    public static List<Vehicle> getLevel(int levelIndex) {
        List<Vehicle> list = new ArrayList<>();
        switch (levelIndex % 15) {
            case 0:
                // Level 1: Beginner tutorial (8 moves min)
                list.add(new Vehicle(0, 2, 0, 2, true, RED_CAR_COLOR, true));
                list.add(new Vehicle(1, 0, 2, 3, false, COLOR_SKY, false));
                list.add(new Vehicle(2, 4, 1, 2, true, COLOR_AMBER, false));
                list.add(new Vehicle(3, 3, 3, 3, false, COLOR_PURPLE, false));
                list.add(new Vehicle(4, 5, 4, 2, true, COLOR_EMERALD, false));
                break;
            case 1:
                // Level 2: Warm up
                list.add(new Vehicle(0, 2, 1, 2, true, RED_CAR_COLOR, true));
                list.add(new Vehicle(1, 1, 0, 3, false, COLOR_AMBER, false));
                list.add(new Vehicle(2, 0, 3, 3, false, COLOR_SKY, false));
                list.add(new Vehicle(3, 4, 3, 2, true, COLOR_EMERALD, false));
                list.add(new Vehicle(4, 3, 4, 2, false, COLOR_ORANGE, false));
                break;
            case 2:
                // Level 3: Cross paths
                list.add(new Vehicle(0, 2, 1, 2, true, RED_CAR_COLOR, true));
                list.add(new Vehicle(1, 0, 0, 2, true, COLOR_LIME, false));
                list.add(new Vehicle(2, 1, 0, 2, false, COLOR_SKY, false));
                list.add(new Vehicle(3, 3, 0, 3, false, COLOR_PURPLE, false));
                list.add(new Vehicle(4, 0, 3, 2, false, COLOR_AMBER, false));
                list.add(new Vehicle(5, 2, 3, 2, false, COLOR_TEAL, false));
                list.add(new Vehicle(6, 4, 3, 2, true, COLOR_ORANGE, false));
                break;
            case 3:
                // Level 4: Corridor
                list.add(new Vehicle(0, 2, 0, 2, true, RED_CAR_COLOR, true));
                list.add(new Vehicle(1, 0, 2, 2, false, COLOR_AMBER, false));
                list.add(new Vehicle(2, 0, 3, 2, true, COLOR_SKY, false));
                list.add(new Vehicle(3, 1, 4, 3, false, COLOR_PURPLE, false));
                list.add(new Vehicle(4, 3, 2, 2, true, COLOR_EMERALD, false));
                list.add(new Vehicle(5, 4, 2, 2, false, COLOR_ORANGE, false));
                list.add(new Vehicle(6, 5, 0, 2, true, COLOR_LIME, false));
                break;
            case 4:
                // Level 5: Traffic Wall
                list.add(new Vehicle(0, 2, 1, 2, true, RED_CAR_COLOR, true));
                list.add(new Vehicle(1, 0, 0, 2, true, COLOR_SKY, false));
                list.add(new Vehicle(2, 1, 0, 2, false, COLOR_AMBER, false));
                list.add(new Vehicle(3, 3, 0, 2, false, COLOR_PURPLE, false));
                list.add(new Vehicle(4, 0, 3, 3, false, COLOR_ORANGE, false));
                list.add(new Vehicle(5, 3, 3, 2, true, COLOR_EMERALD, false));
                list.add(new Vehicle(6, 4, 4, 2, false, COLOR_PINK, false));
                list.add(new Vehicle(7, 5, 2, 2, true, COLOR_TEAL, false));
                break;
            case 5:
                // Level 6: The Long Way
                list.add(new Vehicle(0, 2, 0, 2, true, RED_CAR_COLOR, true));
                list.add(new Vehicle(1, 0, 2, 3, false, COLOR_SKY, false));
                list.add(new Vehicle(2, 3, 2, 2, false, COLOR_EMERALD, false));
                list.add(new Vehicle(3, 0, 3, 2, true, COLOR_AMBER, false));
                list.add(new Vehicle(4, 1, 4, 3, false, COLOR_PURPLE, false));
                list.add(new Vehicle(5, 4, 3, 2, true, COLOR_ORANGE, false));
                list.add(new Vehicle(6, 5, 3, 3, true, COLOR_LIME, false));
                break;
            case 6:
                // Level 7: Tricky Turn
                list.add(new Vehicle(0, 2, 1, 2, true, RED_CAR_COLOR, true));
                list.add(new Vehicle(1, 0, 0, 3, false, COLOR_SKY, false));
                list.add(new Vehicle(2, 3, 0, 3, false, COLOR_AMBER, false));
                list.add(new Vehicle(3, 0, 3, 2, true, COLOR_PURPLE, false));
                list.add(new Vehicle(4, 1, 3, 2, false, COLOR_EMERALD, false));
                list.add(new Vehicle(5, 3, 3, 2, false, COLOR_ORANGE, false));
                list.add(new Vehicle(6, 5, 2, 3, true, COLOR_PINK, false));
                list.add(new Vehicle(7, 1, 5, 3, false, COLOR_TEAL, false));
                break;
            case 7:
                // Level 8: Double Block
                list.add(new Vehicle(0, 2, 0, 2, true, RED_CAR_COLOR, true));
                list.add(new Vehicle(1, 0, 2, 2, true, COLOR_AMBER, false));
                list.add(new Vehicle(2, 1, 2, 2, false, COLOR_SKY, false));
                list.add(new Vehicle(3, 3, 2, 2, false, COLOR_PURPLE, false));
                list.add(new Vehicle(4, 1, 3, 3, false, COLOR_ORANGE, false));
                list.add(new Vehicle(5, 4, 3, 2, true, COLOR_EMERALD, false));
                list.add(new Vehicle(6, 5, 0, 3, true, COLOR_LIME, false));
                list.add(new Vehicle(7, 0, 5, 3, false, COLOR_INDIGO, false));
                break;
            case 8:
                // Level 9: Parking Lot Rush
                list.add(new Vehicle(0, 2, 1, 2, true, RED_CAR_COLOR, true));
                list.add(new Vehicle(1, 0, 0, 2, false, COLOR_AMBER, false));
                list.add(new Vehicle(2, 0, 1, 2, true, COLOR_SKY, false));
                list.add(new Vehicle(3, 1, 3, 3, false, COLOR_PURPLE, false));
                list.add(new Vehicle(4, 4, 0, 2, true, COLOR_EMERALD, false));
                list.add(new Vehicle(5, 3, 2, 2, false, COLOR_ORANGE, false));
                list.add(new Vehicle(6, 4, 3, 2, true, COLOR_PINK, false));
                list.add(new Vehicle(7, 5, 2, 3, true, COLOR_TEAL, false));
                break;
            case 9:
                // Level 10: Tight Squeeze
                list.add(new Vehicle(0, 2, 0, 2, true, RED_CAR_COLOR, true));
                list.add(new Vehicle(1, 0, 2, 3, false, COLOR_SKY, false));
                list.add(new Vehicle(2, 0, 3, 2, true, COLOR_AMBER, false));
                list.add(new Vehicle(3, 1, 5, 3, false, COLOR_PURPLE, false));
                list.add(new Vehicle(4, 3, 2, 2, true, COLOR_EMERALD, false));
                list.add(new Vehicle(5, 4, 3, 2, false, COLOR_ORANGE, false));
                list.add(new Vehicle(6, 5, 0, 2, true, COLOR_LIME, false));
                break;
            case 10:
                // Level 11: Shift & Shuffle
                list.add(new Vehicle(0, 2, 1, 2, true, RED_CAR_COLOR, true));
                list.add(new Vehicle(1, 0, 0, 3, false, COLOR_SKY, false));
                list.add(new Vehicle(2, 0, 1, 2, true, COLOR_AMBER, false));
                list.add(new Vehicle(3, 1, 3, 2, false, COLOR_PURPLE, false));
                list.add(new Vehicle(4, 0, 4, 2, true, COLOR_EMERALD, false));
                list.add(new Vehicle(5, 1, 5, 3, false, COLOR_ORANGE, false));
                list.add(new Vehicle(6, 3, 1, 2, false, COLOR_PINK, false));
                list.add(new Vehicle(7, 3, 3, 2, true, COLOR_TEAL, false));
                list.add(new Vehicle(8, 4, 3, 2, false, COLOR_INDIGO, false));
                list.add(new Vehicle(9, 5, 1, 2, true, COLOR_LIME, false));
                break;
            case 11:
                // Level 12: Gridlock
                list.add(new Vehicle(0, 2, 1, 2, true, RED_CAR_COLOR, true));
                list.add(new Vehicle(1, 0, 1, 2, false, COLOR_SKY, false));
                list.add(new Vehicle(2, 0, 2, 2, true, COLOR_AMBER, false));
                list.add(new Vehicle(3, 1, 3, 3, false, COLOR_PURPLE, false));
                list.add(new Vehicle(4, 0, 5, 2, false, COLOR_EMERALD, false));
                list.add(new Vehicle(5, 3, 0, 2, false, COLOR_ORANGE, false));
                list.add(new Vehicle(6, 3, 1, 2, true, COLOR_PINK, false));
                list.add(new Vehicle(7, 4, 2, 2, false, COLOR_TEAL, false));
                list.add(new Vehicle(8, 4, 4, 2, false, COLOR_INDIGO, false));
                list.add(new Vehicle(9, 5, 0, 2, true, COLOR_LIME, false));
                break;
            case 12:
                // Level 13: The Maze
                list.add(new Vehicle(0, 2, 0, 2, true, RED_CAR_COLOR, true));
                list.add(new Vehicle(1, 0, 2, 3, false, COLOR_SKY, false));
                list.add(new Vehicle(2, 0, 3, 2, true, COLOR_AMBER, false));
                list.add(new Vehicle(3, 1, 4, 3, false, COLOR_PURPLE, false));
                list.add(new Vehicle(4, 3, 2, 2, false, COLOR_EMERALD, false));
                list.add(new Vehicle(5, 3, 3, 2, true, COLOR_ORANGE, false));
                list.add(new Vehicle(6, 4, 0, 2, false, COLOR_PINK, false));
                list.add(new Vehicle(7, 5, 2, 3, true, COLOR_TEAL, false));
                break;
            case 13:
                // Level 14: Expert Challenge
                list.add(new Vehicle(0, 2, 1, 2, true, RED_CAR_COLOR, true));
                list.add(new Vehicle(1, 0, 0, 2, true, COLOR_SKY, false));
                list.add(new Vehicle(2, 1, 0, 2, false, COLOR_AMBER, false));
                list.add(new Vehicle(3, 3, 0, 3, false, COLOR_PURPLE, false));
                list.add(new Vehicle(4, 0, 3, 2, false, COLOR_EMERALD, false));
                list.add(new Vehicle(5, 1, 4, 2, true, COLOR_ORANGE, false));
                list.add(new Vehicle(6, 2, 3, 2, false, COLOR_PINK, false));
                list.add(new Vehicle(7, 4, 3, 2, true, COLOR_TEAL, false));
                list.add(new Vehicle(8, 3, 5, 3, false, COLOR_INDIGO, false));
                list.add(new Vehicle(9, 5, 1, 2, true, COLOR_LIME, false));
                break;
            case 14:
            default:
                // Level 15: Mastermind
                list.add(new Vehicle(0, 2, 2, 2, true, RED_CAR_COLOR, true));
                list.add(new Vehicle(1, 0, 1, 3, false, COLOR_SKY, false));
                list.add(new Vehicle(2, 0, 2, 2, true, COLOR_AMBER, false));
                list.add(new Vehicle(3, 1, 4, 2, false, COLOR_PURPLE, false));
                list.add(new Vehicle(4, 0, 5, 3, false, COLOR_EMERALD, false));
                list.add(new Vehicle(5, 3, 1, 2, true, COLOR_ORANGE, false));
                list.add(new Vehicle(6, 4, 0, 2, false, COLOR_PINK, false));
                list.add(new Vehicle(7, 3, 4, 3, false, COLOR_TEAL, false));
                list.add(new Vehicle(8, 5, 1, 3, true, COLOR_LIME, false));
                break;
        }
        return list;
    }

    public static final int TOTAL_LEVELS = 15;
}
