package loader;

import lombok.SneakyThrows;

import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Loader {


    private static ModuleLayer.Controller createLayer(Iterable<Path> jars, Iterable<ModuleLayer> parents, Iterable<String> roots) {
        List<Configuration> parentConfigurations = StreamSupport.stream(parents.spliterator(), false)
                    .map(ModuleLayer::configuration).collect(Collectors.toList());
        Configuration cfg = Configuration.resolveAndBind(
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
    private static void run(ModuleLayer layer) {
        Module appModule = layer.findModule("test.app").orElseThrow();
        Class<?> cls = Class.forName(appModule, "app.App");

        Constructor<Runnable> constructor = ((Class<Runnable>) cls).getConstructor();
        Runnable runnable = constructor.newInstance();
        runnable.run();
    }

    private static final Path aJar = Paths.get(System.getProperty("jar.a"));
    private static final Path bJar = Paths.get(System.getProperty("jar.b"));
    private static final Path aimplJar = Paths.get(System.getProperty("jar.aimpl"));
    private static final Path bimplJar = Paths.get(System.getProperty("jar.bimpl"));
    private static final Path appJar = Paths.get(System.getProperty("jar.app"));
    private static final ModuleLayer bootLayer = ModuleLayer.boot();

    private static void foo() {
        ModuleLayer allImplLayer = createLayer(
                Arrays.asList(aimplJar, bimplJar, aJar, bJar),
                Arrays.asList(bootLayer),
                Arrays.asList("test.aimpl", "test.bimpl", "test.a", "test.b")).layer();

        ModuleLayer appLayer = createLayer(
                Arrays.asList(appJar),
                Arrays.asList(allImplLayer),
                Arrays.asList("test.app")).layer();
        run(appLayer);
    }

    private static void bar() {
        ModuleLayer appLayer = createLayer(
                Arrays.asList(aJar, bJar, appJar),
                Arrays.asList(bootLayer),
                Arrays.asList("test.a", "test.b", "test.app")).layer();

        ModuleLayer aimplLayer = createLayer(
                Arrays.asList(aimplJar),
                Arrays.asList(appLayer),
                Arrays.asList("test.aimpl")).layer();

        ModuleLayer bimplLayer = createLayer(
                Arrays.asList(bimplJar),
                Arrays.asList(appLayer),
                Arrays.asList("test.bimpl")).layer();
        run(appLayer);
    }

    public static void main(String[] args) {
        bar();
    }
}
