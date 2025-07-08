package com.mycompany.bookstore;

import java.util.*;

abstract class B {
    String i;
    String t;
    int y;
    double p;

    public B(String i, String t, int y, double p, String a) {
        this.i = i;
        this.t = t;
        this.y = y;
        this.p = p;
    }

    abstract boolean canBuy();

    abstract double buy(int q, String em, String ad);

    int getYear() {
        return y;
    }

    String getIsbn() {
        return i;
    }
}

class PBook extends B {
    int s;

    public PBook(String i, String t, int y, double p, String a, int s) {
        super(i, t, y, p, a);
        this.s = s;
    }

    @Override
    boolean canBuy() {
        return s > 0;
    }

    @Override
    double buy(int q, String em, String ad) {
        if (q > s) throw new RuntimeException("Quantum book store: Not enough stock for " + t);
        s -= q;
        Shipping.send(ad);
        return q * p;
    }
}

class EBook extends B {

    public EBook(String i, String t, int y, double p, String a, String f) {
        super(i, t, y, p, a);
    }

    @Override
    boolean canBuy() {
        return true;
    }

    @Override
    double buy(int q, String em, String ad) {
        Mail.send(em);
        return q * p;
    }
}

class DemoBook extends B {
    public DemoBook(String i, String t, int y, double p, String a) {
        super(i, t, y, p, a);
    }

    @Override
    boolean canBuy() {
        return false;
    }

    @Override
    double buy(int q, String em, String ad) {
        throw new RuntimeException("Quantum book store: This book is not for sale");
    }
}

class Mail {
    static void send(String e) {
        System.out.println("Quantum book store: Sent ebook to " + e);
    }
}

class Shipping {
    static void send(String a) {
        System.out.println("Quantum book store: Shipping to " + a);
    }
}

class Shop {
    List<B> all = new ArrayList<>();

    void add(B b) {
        all.add(b);
    }

    void delOld(int n) {
        int cur = Calendar.getInstance().get(Calendar.YEAR);
        List<B> gone = new ArrayList<>();
        for (B b : all) {
            if (cur - b.getYear() > n) {
                gone.add(b);
            }
        }
        all.removeAll(gone);
        for (B b : gone) {
            System.out.println("Quantum book store: Removed " + b.t);
        }
    }

    double buy(String i, int q, String em, String ad) {
        for (B b : all) {
            if (b.getIsbn().equals(i)) {
                if (!b.canBuy()) throw new RuntimeException("Quantum book store: Book can't be sold");
                return b.buy(q, em, ad);
            }
        }
        throw new RuntimeException("Quantum book store: Book not found");
    }
}

public class Bookstore {
    public static void main(String[] args) {
        Shop x = new Shop();

        B b1 = new PBook("p1", "Java 101", 2015, 100, "Ahmed", 3);
        B b2 = new EBook("e1", "Python Fast", 2021, 50, "Mona", "pdf");
        B b3 = new DemoBook("d1", "C++ Intro", 2005, 0, "Omar");

        x.add(b1);
        x.add(b2);
        x.add(b3);

        double paid1 = x.buy("p1", 2, "a@gmail.com", "Giza");
        System.out.println("Quantum book store: Paid " + paid1);

        double paid2 = x.buy("e1", 1, "b@yahoo.com", "");
        System.out.println("Quantum book store: Paid " + paid2);

        x.delOld(10);

        try {
            x.buy("d1", 1, "", "");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
