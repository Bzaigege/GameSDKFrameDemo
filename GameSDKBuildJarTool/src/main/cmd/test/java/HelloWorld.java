package com.bzai.demo;

public class HelloWorld {

    public static void main(String[] args){
        System.out.println("hello world");
        test();
    }

    public static void test(){
        TestA testA = new TestA();
        testA.Test();
    }
}
