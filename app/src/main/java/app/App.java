package app;

import a.A;
import b.B;

import java.util.ServiceLoader;

public class App implements Runnable {
    private final A a;
    private final B b;

    public App() {
        a = ServiceLoader.load(getClass().getModule().getLayer(), A.class).findFirst().orElseThrow();
        b = ServiceLoader.load(getClass().getModule().getLayer(), B.class).findFirst().orElseThrow();
    }

    @Override
    public void run() {
        a.hello();
        b.hello();
    }
}
