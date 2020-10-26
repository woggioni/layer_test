package bimpl;

import b.B;

public class BImpl implements B {
    @Override
    public int pow(int base, int exp) {
        int result = 1;
        for (int i = 0; i < exp; i++) {
            result *= base;
        }
        return result;
    }
}
