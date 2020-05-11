plugins {
    java
    id("me.champeau.gradle.jmh") version "0.5.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testCompile("org.testng:testng:7.1.0")
    jmh ("org.openjdk.jmh:jmh-core:0.9")
    jmh ("org.openjdk.jmh:jmh-generator-annprocess:0.9")
}
tasks.getByName<Test>("test") {
    // enable TestNG support (default is JUnit)
    useTestNG()
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}