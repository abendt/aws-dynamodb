plugins {
    id("com.gtramontina.ghooks.gradle") version "2.0.0"
    id("module-conventions")
}

repositories {
    mavenCentral()

    // https://github.com/oharaandrew314/dynamodb-kotlin-module
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("software.amazon.awssdk:dynamodb-enhanced:2.21.15")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // https://github.com/oharaandrew314/dynamodb-kotlin-module
    // https://github.com/aws/aws-sdk-java-v2/issues/2096
    implementation("com.github.oharaandrew314:dynamodb-kotlin-module:0.3.0")

    testImplementation(platform("org.testcontainers:testcontainers-bom:1.19.1"))
    testImplementation("org.testcontainers:localstack")
    testImplementation("org.testcontainers:junit-jupiter")

    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:2.0.2")
}
