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
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.686")
//    implementation(platform("software.amazon.awssdk:bom:2.21.1"))
//    implementation("software.amazon.awssdk:s3:2.21.1")
//    implementation("software.amazon.awssdk:sso")
//    implementation("software.amazon.awssdk:ssooidc")

    // lombok 추가
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}