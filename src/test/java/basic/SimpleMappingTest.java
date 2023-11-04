package basic;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Testcontainers
public class SimpleMappingTest {

    @Container
    final LocalStackContainer localstack =
            new LocalStackContainer(DockerImageName.parse("localstack/localstack"));

    DynamoDbEnhancedClient enhancedClient;

    @BeforeEach
    void setUp() {
        var client =
                DynamoDbClient.builder()
                        .endpointOverride(localstack.getEndpoint())
                        .credentialsProvider(
                                StaticCredentialsProvider.create(
                                        AwsBasicCredentials.create(
                                                localstack.getAccessKey(),
                                                localstack.getSecretKey())))
                        .region(Region.of(localstack.getRegion()))
                        .build();

        enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(client).build();
    }

    @Test
    @DisplayName("can map Lombok value record (junit5)")
    void canMapLombokValueRecord() {

        var table =
                enhancedClient.table(
                        "junit-lombok-value-table",
                        TableSchema.fromClass(LombokImmutableRecord.class));
        table.createTable();

        var givenRecord =
                LombokImmutableRecord.builder()
                        .partitionKey("my-partition-key")
                        .sortKey(10)
                        .stringAttribute("my-string-value")
                        .build();

        var key = Key.builder().partitionValue("my-partition-key").sortValue(10).build();

        table.putItem(givenRecord);

        var actualRecord = table.getItem(key);

        assertThat(actualRecord).usingRecursiveComparison().isEqualTo(givenRecord);
    }
}
