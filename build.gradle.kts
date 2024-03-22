object Version {
    const val awsSdkVersion = "2.17.107"
    const val junitBomVersion = "5.8.2"
}

plugins {
    id("org.springframework.boot") version "2.7.12"
    id("io.spring.dependency-management") version "1.1.0"
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("software.amazon.awssdk:s3:${Version.awsSdkVersion}")
    implementation("software.amazon.awssdk:apache-client:${Version.awsSdkVersion}")

    // lombok 추가
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:${Version.junitBomVersion}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}