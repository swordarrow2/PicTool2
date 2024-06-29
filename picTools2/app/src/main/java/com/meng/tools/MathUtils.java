package com.meng.tools;

/**
 * Created by SJF on 2024/6/25.
 */

public class MathUtils {
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
