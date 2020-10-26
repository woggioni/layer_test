package loader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceConfigurationError;

public class LoaderTest {

    private static final Path aJar = Paths.get(System.getProperty("jar.a"));
    private static final Path bJar = Paths.get(System.getProperty("jar.b"));
    private static final Path aimplJar = Paths.get(System.getProperty("jar.aimpl"));
    private static final Path bimplJar = Paths.get(System.getProperty("jar.bimpl"));
    private static final Path appJar = Paths.get(System.getProperty("jar.app"));
    private static final ModuleLayer bootLayer = ModuleLayer.boot();


    @Test
    public void test() {
        ModuleLayer serviceLayer = Loader.createLayer(
                Arrays.asList(aimplJar, bimplJar, aJar, bJar),
                Arrays.asList(bootLayer),
                Arrays.asList("mod.aimpl", "mod.bimpl"),
                false
        ).layer();

        ModuleLayer appLayer = Loader.createLayer(
                Arrays.asList(appJar, aJar, bJar),
                Arrays.asList(bootLayer),
                Arrays.asList("mod.app"),
                false
        ).layer();
        Loader.run(serviceLayer, appLayer, "mod.app", "app.Pow");
    }

//    @Test
//    public void test1() {
//        //Putting everything in the same layer just works,
//        // no service binding is required
//        ModuleLayer allImplLayer = Loader.createLayer(
//                Arrays.asList(aimplJar, bimplJar, aJar, bJar),
//                Arrays.asList(bootLayer),
//                Arrays.asList("mod.aimpl", "mod.bimpl"),
//                false
//        ).layer();
//
//        ModuleLayer appLayer = Loader.createLayer(
//                Arrays.asList(appJar),
//                Arrays.asList(allImplLayer),
//                Arrays.asList("mod.app"),
//                false
//        ).layer();
//        Loader.run(appLayer, "mod.app", "app.Pow");
//        Loader.run(appLayer, "mod.app", "app.Square");
//    }
//
//    @Test
//    public void test2() {
//        //separating services in different parent layer also works,
//        // no service binding is required since implementation and interface have to live in the same layer
//        // (because implementation has to `require` interface)
//
//        ModuleLayer aImplLayer = Loader.createLayer(
//                Arrays.asList(aimplJar, aJar),
//                Arrays.asList(bootLayer),
//                Arrays.asList("mod.aimpl"),
//                false
//        ).layer();
//
//        ModuleLayer bImplLayer = Loader.createLayer(
//                Arrays.asList(bimplJar, bJar),
//                Arrays.asList(bootLayer),
//                Arrays.asList("mod.bimpl"),
//                false
//        ).layer();
//
//        ModuleLayer appLayer = Loader.createLayer(
//                Arrays.asList(appJar),
//                Arrays.asList(aImplLayer, bImplLayer),
//                Arrays.asList("mod.app"),
//                false
//        ).layer();
//        Loader.run(appLayer, "mod.app", "app.Pow");
//        Loader.run(appLayer, "mod.app", "app.Square");
//    }
//
//    @Test
//    public void test3() {
//        List<Path> moduleJars = Arrays.asList(aimplJar, aJar, bimplJar, bJar, appJar);
//        //Creating the layer without service binding causes the implementations not to be discovered
//        {
//            ModuleLayer allInOneLayer = Loader.createLayer(moduleJars,
//                    Arrays.asList(bootLayer),
//                    Arrays.asList("mod.app"),
//                    false
//            ).layer();
//            Assertions.assertThrows(ServiceConfigurationError.class, () -> Loader.run(allInOneLayer, "mod.app", "app.Pow"));
//        }
//
//        //enabling service binding allows for mod.aimpl and mod.bimpl to be included in the new layer
//        {
//            ModuleLayer allInOneLayer = Loader.createLayer(moduleJars,
//                    Arrays.asList(bootLayer),
//                    Arrays.asList("mod.app"),
//                    true
//            ).layer();
//            Loader.run(allInOneLayer, "mod.app", "app.Pow");
//            Loader.run(allInOneLayer, "mod.app", "app.Square");
//        }
//    }
//
//    @Test
//    public void test4() {
//        //Service binding basically only helps for module discovery at configuration creation time,
//        // but it has no effect on parent layers where the binding
//        // hasn't happened because an implementation was missing
//        ModuleLayer appLayer = Loader.createLayer(
//                Arrays.asList(appJar, aJar, bJar),
//                Arrays.asList(bootLayer),
//                Arrays.asList("mod.app"), true).layer();
//
//        ModuleLayer bimplLayer = Loader.createLayer(
//                Arrays.asList(bimplJar),
//                Arrays.asList(appLayer),
//                Arrays.asList("mod.bimpl"), true).layer();
//        Assertions.assertThrows(ServiceConfigurationError.class, () -> Loader.run(bimplLayer, "mod.app", "app.Pow"));
//    }
//
//    @Test
//    public void test5() {
//        //Replicating the service interface module in the service user layer is also not possible
//        //because implementations in the parent layers have already been bound to a different interface
//        //module in their own layer that is now shadowed by the one in the user layer
//        ModuleLayer allImplLayer = Loader.createLayer(
//                Arrays.asList(aimplJar, bimplJar, aJar, bJar),
//                Arrays.asList(bootLayer),
//                Arrays.asList("mod.aimpl", "mod.bimpl"),
//                false
//        ).layer();
//
//        ModuleLayer appLayer = Loader.createLayer(
//                Arrays.asList(appJar, aJar, bJar),
//                Arrays.asList(allImplLayer),
//                Arrays.asList("mod.app"),
//                true
//        ).layer();
//        Assertions.assertThrows(ServiceConfigurationError.class, () -> Loader.run(appLayer, "mod.app", "app.Pow"));
//    }
//
//    @Test
//    public void test6() {
//        ModuleLayer aLayer = Loader.createLayer(
//                Arrays.asList(aJar),
//                Arrays.asList(bootLayer),
//                Arrays.asList("mod.a"),
//                false
//        ).layer();
//
//        ModuleLayer aImplLayer = Loader.createLayer(
//                Arrays.asList(aimplJar),
//                Arrays.asList(aLayer),
//                Arrays.asList("mod.aimpl"),
//                false
//        ).layer();
//
//        ModuleLayer bLayer = Loader.createLayer(
//                Arrays.asList(bJar),
//                Arrays.asList(bootLayer),
//                Arrays.asList("mod.b"),
//                false
//        ).layer();
//
//        ModuleLayer bImplLayer = Loader.createLayer(
//                Arrays.asList(bimplJar),
//                Arrays.asList(bLayer),
//                Arrays.asList("mod.bimpl"),
//                false
//        ).layer();
//
//        {
//            //Having the application layer child of the interface layers and having
//            // the implementation layers children of the api layers also doesn't work
//            ModuleLayer appLayer = Loader.createLayer(
//                    Arrays.asList(appJar),
//                    Arrays.asList(aLayer, bLayer),
//                    Arrays.asList("mod.app"),
//                    true
//            ).layer();
//            Assertions.assertThrows(ServiceConfigurationError.class, () -> Loader.run(appLayer, "mod.app", "app.Pow"));
//            Assertions.assertThrows(ServiceConfigurationError.class, () -> Loader.run(appLayer, "mod.app", "app.Square"));
//        }
//
//        {
//            //Having the application layer child of the implementation layers and having
//            // the implementation layers children of the api layers works instead
//            ModuleLayer appLayer = Loader.createLayer(
//                    Arrays.asList(appJar),
//                    Arrays.asList(aImplLayer, bImplLayer),
//                    Arrays.asList("mod.app"),
//                    true
//            ).layer();
//            Loader.run(appLayer, "mod.app", "app.Pow");
//            Loader.run(appLayer, "mod.app", "app.Square");
//        }
//        //Basically every layer can access its parents but not the other way around, there are no exceptions to this rule,
//        // not even for services because a module layer is left completely unware of being the parent of some other layer
//    }
//
//    @Test
//    public void test7() {
//        ModuleLayer allImplLayer = Loader.createLayer(
//                Arrays.asList(aimplJar, bimplJar, aJar, bJar),
//                Arrays.asList(bootLayer),
//                Arrays.asList("mod.aimpl", "mod.bimpl"),
//                false
//        ).layer();
//
//        ModuleLayer appLayer = Loader.createLayer(
//                Arrays.asList(appJar, aJar, bJar),
//                Arrays.asList(allImplLayer),
//                Arrays.asList("mod.app"),
//                true
//        ).layer();
//        Assertions.assertThrows(ServiceConfigurationError.class, () -> Loader.run(appLayer, "mod.app", "app.Pow"));
//    }
}
