
dependencies {
    implementation(project(":common"))
}

tasks.create("run", JavaExec::class) {
    arrayOf(":a", ":b", ":aimpl", ":bimpl", ":app").forEach {
        val jarTask = tasks.getByPath("$it:jar") as Jar
        dependsOn(jarTask)
        val key = "jar.${jarTask.archiveBaseName.get()}"
        val value = jarTask.archiveFile.get().asFile.toString()
        systemProperty(key, value)
    }
    dependsOn("compileJava")
    classpath = sourceSets.main.get().runtimeClasspath
    modularity.inferModulePath.set(true)
    mainClass.set("loader.Loader")
    mainModule.set("mod.loader")
}

tasks.named<Test>("test") {
    arrayOf(":a", ":b", ":aimpl", ":bimpl", ":app").forEach {
        val jarTask = tasks.getByPath("$it:jar") as Jar
        dependsOn(jarTask)
        val key = "jar.${jarTask.archiveBaseName.get()}"
        val value = jarTask.archiveFile.get().asFile.toString()
        systemProperty(key, value)
    }
    modularity.inferModulePath.set(true)
}