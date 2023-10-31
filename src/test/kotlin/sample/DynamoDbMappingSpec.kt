package sample

import io.andrewohara.dynamokt.DataClassTableSchema
import io.kotest.core.extensions.install
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.testcontainers.ContainerExtension
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.utility.DockerImageName
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

class DynamoDbMappingSpec : StringSpec({

    val localstack = install(ContainerExtension(LocalStackContainer(DockerImageName.parse("localstack/localstack")))) {
    }

    val dynamoClient = DynamoDbClient.builder()
        .endpointOverride(localstack.endpoint)
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    localstack.accessKey,
                    localstack.secretKey,
                ),
            ),
        ).region(Region.of(localstack.region)).build()

    val enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoClient).build()

    "can map java bean" {
        val table = enhancedClient.table("java-record-table", TableSchema.fromClass(JavaRecord::class.java))
        table.createTable()

        checkAll(50, javaRecordArb) { givenRecord ->
            val key = Key.builder().partitionValue(givenRecord.partitionKey).sortValue(givenRecord.sortKey).build()

            table.putItem(givenRecord)

            val actualRecord =
                table.getItem(key)

            actualRecord shouldBe givenRecord

            table.deleteItem(key)
        }
    }

    "can map lombok bean" {
        val table = enhancedClient.table("lombok-record-table", TableSchema.fromClass(LombokRecord::class.java))
        table.createTable()

        checkAll(50, lombokRecordArb) { givenRecord ->
            val key = Key.builder().partitionValue(givenRecord.partitionKey).sortValue(givenRecord.sortKey).build()

            table.putItem(givenRecord)

            val actualRecord =
                table.getItem(key)

            actualRecord shouldBe givenRecord

            table.deleteItem(key)
        }
    }

    "can map kotlin bean" {
        val table = enhancedClient.table("kotlin-record-table", DataClassTableSchema(KotlinRecord::class))
        table.createTable()

        checkAll(50, kotlinRecordArb) { givenRecord ->
            val key = Key.builder().partitionValue(givenRecord.partitionKey).sortValue(givenRecord.sortKey).build()

            table.putItem(givenRecord)

            val actualRecord =
                table.getItem(key)

            actualRecord shouldBe givenRecord

            table.deleteItem(key)
        }
    }
})
