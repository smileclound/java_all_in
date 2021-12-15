package com.mmc.agent;

public class Main {

    public static void main1(String[] args) {
        System.out.println("start");
        new UserService().sayHello();
    }

    public static void main(String[] args) {
        System.out.println("start");

        String str = "qweerrtyuuiioo";
        System.out.println("str ==="+ str);
        char[] strArr = str.toCharArray();
        int length = strArr.length;
        for (int i = length-1, j = 0; i>j; i--,j++) {
            char lastChar = strArr[i];
            char firstChar = strArr[j];
            strArr[j] = lastChar;
            strArr[i] = firstChar;

        }

        String toStr = String.copyValueOf(strArr);
        System.out.println("toStr ==="+ toStr);
    }
}