package basic

import io.andrewohara.dynamokt.DataClassTableSchema
import io.kotest.assertions.json.shouldBeValidJson
import io.kotest.assertions.json.shouldContainJsonKeyValue
import io.kotest.core.extensions.install
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.testcontainers.ContainerExtension
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.utility.DockerImageName
import sample.KotlinRecord
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.enhanced.dynamodb.document.EnhancedDocument
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import software.amazon.awssdk.services.dynamodb.model.QueryRequest

class SimpleMappingSpec : StringSpec({

    // [START localstack]
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
    // [END localstack]

    "can use the low-level API" {
        // for simplicity we are here using the enhanced client to create the table instead of the low-level API
        val table = enhancedClient.table("sample-table", TableSchema.fromClass(JavaRecord::class.java))
        table.createTable()

        // START low-level-api
        // use the low-level API to put an item into the table
        val item =
            mapOf(
                "partitionKey" to AttributeValue.builder().s("my partition key").build(),
                "sortKey" to AttributeValue.builder().n("12345").build(),
                "stringAttribute" to AttributeValue.builder().s("my string value").build(),
            )

        dynamoClient.putItem(
            PutItemRequest.builder()
                .tableName("sample-table")
                .item(item).build(),
        )

        // use the low-level API to fetch the item from the table
        val key =
            mapOf(
                "partitionKey" to AttributeValue.builder().s("my partition key").build(),
                "sortKey" to AttributeValue.builder().n("12345").build(),
            )

        val request =
            GetItemRequest.builder()
                .key(key)
                .tableName("sample-table")
                .build()

        val getResponse = dynamoClient.getItem(request)
        getResponse.hasItem().shouldBeTrue()

        // this result contains the data from the table
        val result: Map<String, AttributeValue> = getResponse.item()
        result.shouldContain("stringAttribute", AttributeValue.builder().s("my string value").build())
        println(result)

        // alternatively we can do a query
        val queryResponse =
            dynamoClient.query(
                QueryRequest.builder()
                    .tableName("sample-table")
                    .keyConditionExpression("partitionKey = :pk")
                    .expressionAttributeValues(mapOf(":pk" to AttributeValue.builder().s("my partition key").build())).build(),
            )
        queryResponse.count() shouldBe 1
        println(queryResponse.items())

        // finally we can convert it to JSON or handle it as we need
        val json = EnhancedDocument.fromAttributeValueMap(result).toJson()
        json.shouldBeValidJson()
            .shouldContainJsonKeyValue("$.stringAttribute", "my string value")
        println(json)
        // END low-level-api
    }

    // START proptest-java
    "can map java pojo bean" {
        val table = enhancedClient.table("java-record-table", TableSchema.fromClass(JavaRecord::class.java))
        table.createTable()

        checkAll(50, aJavaRecord) { givenRecord ->
            val key = Key.builder().partitionValue(givenRecord.partitionKey).sortValue(givenRecord.sortKey).build()

            table.putItem(givenRecord)

            val actualRecord =
                table.getItem(key)

            actualRecord shouldBe givenRecord

            table.deleteItem(key)
        }
    }
    // END proptest-java

    "can map lombok data bean" {
        val table = enhancedClient.table("lombok-data-table", TableSchema.fromClass(LombokMutableRecord::class.java))
        table.createTable()

        checkAll(50, aMutableLombokRecord) { givenRecord ->

            val key = Key.builder().partitionValue(givenRecord.partitionKey).sortValue(givenRecord.sortKey).build()

            table.putItem(givenRecord)

            val actualRecord =
                table.getItem(key)

            actualRecord shouldBe givenRecord

            table.deleteItem(key)
        }
    }

    "can map lombok value bean" {
        val table = enhancedClient.table("lombok-value-table", TableSchema.fromClass(LombokImmutableRecord::class.java))
        table.createTable()

        checkAll(50, anImmutableLombokRecord) { givenRecord ->

            val key = Key.builder().partitionValue(givenRecord.partitionKey).sortValue(givenRecord.sortKey).build()

            table.putItem(givenRecord)

            val actualRecord =
                table.getItem(key)

            actualRecord shouldBe givenRecord

            table.deleteItem(key)
        }
    }

    "can map kotlin data class" {
        val table = enhancedClient.table("kotlin-record-table", DataClassTableSchema(KotlinRecord::class))
        table.createTable()

        checkAll(50, aKotlinRecord) { givenRecord ->
            val key = Key.builder().partitionValue(givenRecord.partitionKey).sortValue(givenRecord.sortKey).build()

            table.putItem(givenRecord)

            val actualRecord =
                table.getItem(key)

            actualRecord shouldBe givenRecord

            table.deleteItem(key)
        }
    }
})
