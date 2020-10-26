
allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }
    apply<JavaLibraryPlugin>()

    configure<JavaPluginExtension> {
        modularity.inferModulePath.set(true)
    }

    dependencies {
        addLombok("compileOnly", "annotationProcessor", "testCompileOnly", "testAnnotationProcessor")
        addJunitJupiter()
    }

    tasks.named<Test>("test") {
        useJUnitPlatform()
    }
}