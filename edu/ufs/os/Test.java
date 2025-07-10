package edu.ufs.os;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.function.BiFunction;
import java.util.Random;

public class Test {
    private ThreadSafeArrayList<Integer> tsArrList = new ThreadSafeArrayList<>();
    private Random random = new Random();

    private double elements = 100000;
    String path = System.getProperty("user.dir") + "/SafeList/output/";

    public class SingleThreadTest extends Thread {
        private ArrayList<Integer> arrList = new ArrayList<>();

        @Override
        public void run() {
            Test test = new Test();
            final int step = 1000;
    
            try (PrintWriter writer = new PrintWriter(new FileWriter(test.path + "ArrList_TSArrList_Benchmark.csv"))) {
                writer.print("n,");
                for (int i = step; i <= elements; i+=step) 
                    writer.print(i + (i == elements ? "" : ","));
                writer.println();

                test.writeCSVLine("ArrayList_add", writer, arrList, test::testAdd);
                test.writeCSVLine("TSArrayList_add", writer, tsArrList, test::testAdd);
                test.writeCSVLine("ArrayList_contains", writer, arrList, test::testContains);
                test.writeCSVLine("TSArrayList_contains", writer, tsArrList, test::testContains);
                test.writeCSVLine("ArrayList_remove", writer, arrList, test::testRemove);
                test.writeCSVLine("TSArrayList_remove", writer, tsArrList, test::testRemove);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class MultiThreadTest extends Thread {
        private Vector<Integer> vector = new Vector<>();

        @Override
        public void run() {
            Test test = new Test();
            final int step = 1000;
    
            try (PrintWriter writer = new PrintWriter(new FileWriter(test.path + "Vector_TSArrList_Benchmark.csv"))) {
                // "i" é o tamanho da lista em determinado teste
                writer.print("n,");
                
                for (int i = step; i <= elements; i+=step) 
                    writer.print(i + (i == elements ? "" : ","));
                
                writer.println();

                test.writeCSVLine("Vector_add", writer, vector, test::testAdd);
                test.writeCSVLine("TSArrayList_add", writer, tsArrList, test::testAdd);
                test.writeCSVLine("Vector_contains", writer, vector, test::testContains);
                test.writeCSVLine("TSArrayList_contains", writer, tsArrList, test::testContains);
                test.writeCSVLine("Vector_remove", writer, vector, test::testRemove);
                test.writeCSVLine("TSArrayList_remove", writer, tsArrList, test::testRemove);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeCSVLine(String name, PrintWriter w, List<Integer> l, BiFunction<List<Integer>, Integer, Long> f) {
        final int step = 1000;

        w.print(name + ",");

        for (int i = step; i <= elements; i+=step) {
            long time = f.apply(l, i);
            w.print(time + (i == elements ? "" : ","));
        }

        w.println();
    }

    private void adjustListSize(List<Integer> l, int n) {
        if (!l.isEmpty()) l.clear();

        for (int j = 0; j < n; j++) {
            l.add(random.nextInt(100));
        }
    }

    public long testAdd(List<Integer> list, int n) {
        adjustListSize(list, n); // Preenche a lista com n elementos

        long begin = System.nanoTime();
        list.add(0, random.nextInt(100));
        long end = System.nanoTime();
        
        return (end - begin);
    }

    public long testContains(List<Integer> list, int n) {
        adjustListSize(list, n); // Preenche a lista com n elementos

        long begin = System.nanoTime();
        list.contains(random.nextInt(100));
        long end = System.nanoTime();
        
        return (end - begin);
    }

    public long testRemove(List<Integer> list, int n) {
        adjustListSize(list, n); // Preenche a lista com n elementos

        long begin = System.nanoTime();
        list.remove(0);
        long end = System.nanoTime();
        
        return (end - begin);
    }

    public static void main(String [] args) throws InterruptedException {
        Test test = new Test();

        Thread tArr = test.new SingleThreadTest();
        tArr.start();
        tArr.join();

        Thread[] tVec = new Thread[16];
        
        for (int i = 0; i < 16; i++) {
            tVec[i] = test.new MultiThreadTest();
            tVec[i].start();
        }

        for (int i = 0; i < 16; i++) {
            tVec[i].join();
        }
    }
}
