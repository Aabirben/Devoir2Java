package org.example.dev2;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Object mutex = new Object();
        List<Order> orders = new ArrayList<>();

        InsertToOrdersListThread insertThread = new InsertToOrdersListThread(mutex, orders);
        ReadFromOrderListThread readThread = new ReadFromOrderListThread(mutex, orders);

        insertThread.start();
        readThread.start();
    }
}
