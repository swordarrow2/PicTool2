package com.meng;

public class Main {

    public static void main(String[] args) {
        // write your code here
        String time = "00:11:22.33";
        System.out.println(Integer.parseInt(time.substring(time.lastIndexOf(":") + 1, time.indexOf("."))));
    }
}
