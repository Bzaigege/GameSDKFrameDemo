package com.bzai.gamesdk.build;

public class Main {

    public static void main(String[] args){
        new Thread(new BuildJarTask()).start();
    }
}
