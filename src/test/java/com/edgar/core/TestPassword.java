package com.edgar.core;

import java.util.Random;

public class TestPassword {

    public static char[] getChar() {
        char[] passwordLit = new char[62];
        char fword = 'A';
        char mword = 'a';
        char bword = '0';
        for (int i = 0; i < 62; i++) {
            if (i < 26) {
                passwordLit[i] = fword;
                fword++;
            } else if (i < 52) {
                passwordLit[i] = mword;
                mword++;
            } else {
                passwordLit[i] = bword;
                bword++;
            }
        }
        return passwordLit;
    }

    public static void main(String[] args) {

        char[] r = getChar();
        Random rr = new Random();
        char[] pw = new char[6];
        for (int i = 0; i < pw.length; i++) {
            int num = rr.nextInt(62);
            pw[i] = r[num];
            System.out.println(pw[i]);
        }
    }
}