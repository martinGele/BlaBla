package com.util;

public class globalVar {

    public int getMyVar() {
        return myVar;
    }

    public void setMyVar(int myVar) {
        this.myVar = myVar;
    }

    private int myVar = 0;
    private static globalVar instance;

    static {
        instance = new globalVar();
    }

    private globalVar() {
    }

    public static globalVar getInstance() {
        return globalVar.instance;
    }

}
