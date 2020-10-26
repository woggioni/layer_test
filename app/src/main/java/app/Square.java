package app;

import a.A;
import common.ServiceHub;

public class Square implements Runnable {
    private final A a;

    public Square(ServiceHub serviceHub) {
        a = serviceHub.loadFirst(A.class);
    }

    @Override
    public void run() {
        a.square(4);
    }
}
