package org.example.dev2;

import java.util.Date;

public class Order  {
    public int id;
    public Date date;
    public  double amount ;
    public int customerId;
    public String status;
    public Order() {

    }
    public Order(int id, Date date, double amount, int customerId, String status) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.customerId = customerId;
        this.status = status;
    }
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public Date getDate() {return date;}
    public void setDate(Date date) {this.date = date;}
    public double getAmount() {return amount;}
    public void setAmount(double amount) {this.amount = amount;}
    public int getCustomerId() {return customerId;}
    public void setCustomerId(int customerId) {this.customerId = customerId;}
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    @Override
    public String toString() {
        return "Order [id=" + id + ", date=" + date + ", amount=" + amount + ", customerId="+ customerId + ", status=" + status + "]";
    }
}
