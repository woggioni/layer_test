
tasks.create("run", JavaExec::class) {
    val arguments = ArrayList<String>()
    arrayOf(":a", ":b", ":aimpl", ":bimpl", ":app").forEach {
        val jarTask = tasks.getByPath("$it:jar") as Jar
        dependsOn(jarTask)
        val key = "jar.${jarTask.archiveBaseName.get()}"
        val value = jarTask.archiveFile.get().asFile.toString()
        systemProperty(key, value)
        println("$key -> $value")
    }
    dependsOn("compileJava")
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("loader.Loader")
    mainModule.set("test.loaderloader")
}