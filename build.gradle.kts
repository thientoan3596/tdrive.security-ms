import org.gradle.kotlin.dsl.*
plugins {
    id("java")
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}
group = "org.thluon.java"
version = "1.0-SNAPSHOT"
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))
    }
}
dependencies {
    implementation(libs.bundles.jjwt)
    implementation(libs.bundles.hibernate.jakarta.validator)
    implementation(libs.springdoc.webflux)
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation(libs.jug)
    implementation(libs.bundles.r2dbc)
    implementation(libs.thluon.converter)
    implementation(libs.thluon.rest)
    compileOnly(libs.bundles.mapstruct.lombok.compile)
    annotationProcessor(libs.bundles.mapstruct.lombok.annotation.processor)
}
tasks.bootJar {
    archiveFileName.set("security-ms.jar")
}
