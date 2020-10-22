package bimpl;

import b.B;

public class BImpl implements B {
    @Override
    public void hello() {
        System.out.printf("hello from %s\n", this.getClass().getName());
    }
}
