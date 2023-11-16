package complex

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

class ComplexMappingSpec : StringSpec({

    val localstack =
        install(ContainerExtension(LocalStackContainer(DockerImageName.parse("localstack/localstack")))) {
        }

    val dynamoClient =
        DynamoDbClient.builder()
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

    "can map complex JavaBean" {
        val table = enhancedClient.table("java-complex-record-table", TableSchema.fromClass(JavaComplexItem::class.java))
        table.createTable()

        checkAll(50, aJavaComplexItem) { givenItem ->
            val key = Key.builder().partitionValue(givenItem.partitionKey).sortValue(givenItem.sortKey).build()

            table.putItem(givenItem)

            val actualItem =
                table.getItem(key)

            actualItem shouldBe givenItem

            table.deleteItem(key)
        }
    }

    "can map complex Lombok @Value" {
        val table = enhancedClient.table("lombok-complex-value-table", TableSchema.fromClass(LombokComplexItem::class.java))
        table.createTable()

        checkAll(50, aLombokComplexItem) { givenItem ->
            val key = Key.builder().partitionValue(givenItem.partitionKey).sortValue(givenItem.sortKey).build()

            table.putItem(givenItem)

            val actualItem =
                table.getItem(key)

            actualItem shouldBe givenItem

            table.deleteItem(key)
        }
    }
})
