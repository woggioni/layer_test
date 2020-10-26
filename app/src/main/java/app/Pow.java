package app;

import a.A;
import b.B;
import bimpl.api.Bar;
import common.ServiceHub;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;

public class Pow implements Runnable {
    private final B b;

    @SneakyThrows
    public Pow(ServiceHub serviceHub) {
        b = serviceHub.loadFirst(B.class);
        ModuleLayer layer = b.getClass().getModule().getLayer();
        Module mod = layer.findModule("mod.bimpl").get();
        Class<Bar> cls = (Class<Bar>) Class.forName("bimpl.api.Bar", true, mod.getClassLoader());
        Constructor<Bar> constructor = cls.getConstructor();
        Object impl = constructor.newInstance();
        System.out.println(impl);
    }

    @Override
    public void run() {
        b.pow(3, 2);
    }
}
