package org.example.dev2;

import java.util.List;

public class ReadFromOrderListThread extends Thread {
    private final Object mutex;
    private final List<Order> orders;

    public ReadFromOrderListThread(Object mutex, List<Order> orders) {
        this.mutex = mutex;
        this.orders = orders;
    }

    @Override
    public void run() {
        synchronized (mutex) {
            while (orders.isEmpty()) {
                try {
                    mutex.wait(); // Wait until orders are added
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (Order order : orders) {
                System.out.println(order.toString());
            }
        }
    }
}
