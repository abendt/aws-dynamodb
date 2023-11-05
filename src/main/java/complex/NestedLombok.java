package complex;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

// [START example]
@Value
@Builder
@DynamoDbImmutable(builder = NestedLombok.NestedLombokBuilder.class)
public class NestedLombok {
    String stringAttribute;

    List<NestedLombok> nestedList;
}
// [END example]
