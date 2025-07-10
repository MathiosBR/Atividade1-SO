package edu.ufs.os;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SingleThreadTest {
    private ArrayList<Integer> arrList = new ArrayList<>();
    private ThreadSafeArrayList<Integer> threadSafe = new ThreadSafeArrayList<>();
    private Random random = new Random();

    String path = System.getProperty("user.dir") + "/SafeList/output/";

    private long begin = 0;
    private long end = 0;

    private ArrayList<Long> threadSafeAdd = new ArrayList<>();
    private ArrayList<Long> threadSafeContains = new ArrayList<>();
    private ArrayList<Long> threadSafeRemove = new ArrayList<>();
    private ArrayList<Long> arrListAdd = new ArrayList<>();
    private ArrayList<Long> arrListContains = new ArrayList<>();
    private ArrayList<Long> arrListRemove = new ArrayList<>();

    private void fillList(List<Integer> l, int n) {
        for (int i = 0; i < n; i++) l.add(0);
    }

    public static void main(String[] args) {
        final int MAX_N = 100000;
        SingleThreadTest s = new SingleThreadTest();

        // Testes de ArrayList
        for (int i = 0; i < MAX_N; i+=100) {
            s.arrList.clear();
            s.fillList(s.arrList, i);
            
            s.begin = System.nanoTime();
            s.arrList.add(0, s.random.nextInt(100));
            s.end = System.nanoTime();

            s.arrListAdd.add(s.end - s.begin);
        }

        for (int i = 0; i < MAX_N; i+=100) {
            s.arrList.clear();
            s.fillList(s.arrList, i);
            
            s.begin = System.nanoTime();
            s.arrList.contains(s.random.nextInt(100));
            s.end = System.nanoTime();

            s.arrListContains.add(s.end - s.begin);
        }

        for (int i = 0; i < MAX_N; i+=100) {
            s.arrList.clear();
            s.fillList(s.arrList, i+1);
            
            s.begin = System.nanoTime();
            s.arrList.remove(0);
            s.end = System.nanoTime();

            s.arrListRemove.add(s.end - s.begin);
        }

        // Testes de ThreadSafeArrayList
        // Testes de ArrayList
        for (int i = 0; i < MAX_N; i+=100) {
            s.arrList.clear();
            s.fillList(s.arrList, i);
            
            s.begin = System.nanoTime();
            s.threadSafe.add(0, s.random.nextInt(100));
            s.end = System.nanoTime();

            s.threadSafeAdd.add(s.end - s.begin);
        }

        for (int i = 0; i < MAX_N; i+=100) {
            s.arrList.clear();
            s.fillList(s.arrList, i);
            
            s.begin = System.nanoTime();
            s.threadSafe.contains(s.random.nextInt(100));
            s.end = System.nanoTime();

            s.threadSafeContains.add(s.end - s.begin);
        }

        for (int i = 0; i < MAX_N; i+=100) {
            s.arrList.clear();
            s.fillList(s.arrList, i+1);
            
            s.begin = System.nanoTime();
            s.threadSafe.remove(0);
            s.end = System.nanoTime();

            s.threadSafeRemove.add(s.end - s.begin);
        }

        // Impressão em CSV
        try (PrintWriter writer = new PrintWriter(new FileWriter(s.path + "SingleThread_Benchmark.csv"))) {
            int arrSizes = s.arrListAdd.size();

            writer.print("N,");
            for (int i = 0; i <= arrSizes; i++) {
                writer.print(i + (i == arrSizes ? "\n" : ","));
            }

            writer.print("ArrListAdd,");
            for (int i = 0; i < arrSizes; i++) {
                writer.print(s.arrListAdd.get(i) + (i == arrSizes-1 ? "\n" : ","));
            }

            writer.print("ArrListContains,");
            for (int i = 0; i < arrSizes; i++) {
                writer.print(s.arrListContains.get(i) + (i == arrSizes-1 ? "\n" : ","));
            }

            writer.print("ArrListRemove,");
            for (int i = 0; i < arrSizes; i++) {
                writer.print(s.arrListRemove.get(i) + (i == arrSizes-1 ? "\n" : ","));
            }

            writer.print("ThreadSafeAdd,");
            for (int i = 0; i < arrSizes; i++) {
                writer.print(s.threadSafeAdd.get(i) + (i == arrSizes-1 ? "\n" : ","));
            }

            writer.print("ThreadSafeContains,");
            for (int i = 0; i < arrSizes; i++) {
                writer.print(s.threadSafeContains.get(i) + (i == arrSizes-1 ? "\n" : ","));
            }

            writer.print("ThreadSafeRemove,");
            for (int i = 0; i < arrSizes; i++) {
                writer.print(s.threadSafeRemove.get(i) + (i == arrSizes-1 ? "\n" : ","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
