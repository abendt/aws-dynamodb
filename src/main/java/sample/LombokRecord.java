package sample;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Value
@Builder
@DynamoDbImmutable(builder = LombokRecord.LombokRecordBuilder.class)
public class LombokRecord {
    @Getter(onMethod_ = {@DynamoDbPartitionKey})
    private String partitionKey;

    @Getter(onMethod_ = {@DynamoDbSortKey})
    private int sortKey;

    private String stringAttribute;
}
