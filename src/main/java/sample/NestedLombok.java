package sample;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

@Value
@Builder
@DynamoDbImmutable(builder = NestedLombok.NestedLombokBuilder.class)
public class NestedLombok {
    private String stringAttribute;

    private List<NestedLombok> nestedList;
}
