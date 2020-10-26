package cloader;

import common.ServiceHub;
import lombok.SneakyThrows;

import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Loader {

    private interface ConfigurationResolver {
        Configuration resolve(ModuleFinder before,
                              List<Configuration> parents,
                              ModuleFinder after,
                              Collection<String> roots);
    }

    public static ModuleLayer.Controller createLayer(
            Iterable<Path> jars,
            Iterable<ModuleLayer> parents,
            Iterable<String> roots,
            boolean bind) {
        List<Configuration> parentConfigurations = StreamSupport.stream(parents.spliterator(), false)
                    .map(ModuleLayer::configuration).collect(Collectors.toList());

        Configuration cfg;
        ConfigurationResolver resolver;
        if(bind) {
            resolver = Configuration::resolveAndBind;
        } else {
            resolver = Configuration::resolve;
        }
        cfg = resolver.resolve(
                ModuleFinder.of(StreamSupport.stream(jars.spliterator(), false).toArray(Path[]::new)),
                parentConfigurations,
                ModuleFinder.of(),
                StreamSupport.stream(roots.spliterator(), false).collect(Collectors.toList()));

        List<ModuleLayer> parentLayers =
                StreamSupport.stream(parents.spliterator(), false).collect(Collectors.toList());

        ClassLoader cl = new URLClassLoader(StreamSupport.stream(jars.spliterator(), false)
                .map(new Function<Path, URL>() {
                    @Override
                    @SneakyThrows
                    public URL apply(Path path) {
                        return path.toUri().toURL();
                    }
                }).toArray(URL[]::new), null);
        return ModuleLayer.defineModulesWithOneLoader(cfg, parentLayers, cl);
    }

    @SneakyThrows
    public static void run(ModuleLayer serviceLayer, ModuleLayer appLayer, String moduleName, String className) {
        Module appModule = appLayer.findModule(moduleName).orElseThrow();
        Class<Runnable> cls = (Class<Runnable>) Class.forName(appModule, className);
        ServiceHub serviceHub = new ServiceHub(serviceLayer);
        try {
            Constructor<Runnable> constructor = null;
            Runnable runnable = null;
            try {
                constructor = cls.getConstructor(ServiceHub.class);
                runnable = constructor.newInstance(serviceHub);
            } catch (NoSuchMethodException nsme) {
            }
            if(constructor == null) {
                constructor = cls.getConstructor();
                runnable = constructor.newInstance();
            }
            runnable.run();
        } catch(InvocationTargetException ice) {
            throw ice.getTargetException();
        }
    }

    public static void main(String[] args) {
        Path aJar = Paths.get(System.getProperty("jar.a"));
        Path bJar = Paths.get(System.getProperty("jar.b"));
        Path aimplJar = Paths.get(System.getProperty("jar.aimpl"));
        Path bimplJar = Paths.get(System.getProperty("jar.bimpl"));
        Path appJar = Paths.get(System.getProperty("jar.app"));
        ModuleLayer bootLayer = ModuleLayer.boot();

        ModuleLayer serviceLayer = Loader.createLayer(
                Arrays.asList(aimplJar, bimplJar),
                Arrays.asList(bootLayer),
                Arrays.asList("mod.aimpl", "mod.bimpl"),
                false
        ).layer();

        ModuleLayer appLayer = Loader.createLayer(
                Arrays.asList(appJar),
                Arrays.asList(bootLayer),
                Arrays.asList("mod.app"),
                false
        ).layer();
        Loader.run(serviceLayer, appLayer, "mod.app", "app.Pow");
    }
}
