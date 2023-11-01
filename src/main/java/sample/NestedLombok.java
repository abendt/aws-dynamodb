package sample;

import lombok.Builder;
import lombok.Value;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

import java.util.List;

@Value
@Builder
@DynamoDbImmutable(builder = NestedLombok.NestedLombokBuilder.class)
public class NestedLombok {
    private String stringAttribute;

    private List<NestedLombok> nestedList;
}
