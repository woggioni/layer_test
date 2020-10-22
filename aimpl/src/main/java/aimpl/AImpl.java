package aimpl;

import a.A;

public class AImpl implements A {
    @Override
    public void hello() {
        System.out.printf("hello from %s\n", this.getClass().getName());
    }
}