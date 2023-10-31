plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))
    }
}

dependencies {
    val kotlinVersion = libs.versions.kotlin.get()
    val testLoggerPluginVersion = libs.versions.gradleTestLoggerPlugin.get()
    val ktlintPluginVersion = libs.versions.gradleKtlintPlugin.get()

    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation("com.adarshr:gradle-test-logger-plugin:$testLoggerPluginVersion")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:$ktlintPluginVersion")

    // workaround to be able to use the version catalog inside of gradle convention plugins
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
