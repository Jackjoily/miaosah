package com.jack.miaosha.util;

public class Test11 {
    public static void main(String[] args) {

        TestString ts = new TestString();
        StringBuilder sb = new StringBuilder();
        StringBuffer sb1 = new StringBuffer();
        for (int i = 0; i < 1000; i++) {
            ts.append(1);
            sb.append("1");
            sb1.append("1");
            System.out.println(ts.getNum()==sb.length());
            System.out.println(ts.getNum()==sb1.length());
            System.out.println(sb.length()==sb1.length());
            System.out.println("=================");
        }
    }


}

class TestString {

    private Integer n = 0;


    public Integer getNum() {
        return n;
    }

    public void setNum(Integer n) {
        this.n = n;
    }

    public synchronized void append(Integer n) {
        this.n = this.n + n;
    }


}
