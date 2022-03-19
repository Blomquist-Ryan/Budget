package com.example.budget;

public class BudgetItem {
    float amount;
    float total;
    String name;


    public BudgetItem(String name, float amount)
    {
        this.name = name;
        this.amount = amount;
    }
    public void Add(float additionAmount)
    {
        this.total += additionAmount;
    };

    public void Subtract(float subtractAmount)
    {
        this.total -= subtractAmount;
    }

    public String toString()
    {
        return "$" + total;
    }


    public String getName() {
        return name;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) { this.amount = amount;}

    public float getTotal(){ return total; }
}