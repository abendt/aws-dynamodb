package sample;

import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import lombok.With;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Value
@Builder
@DynamoDbImmutable(builder = LombokImmutableRecord.LombokImmutableRecordBuilder.class)
public class LombokImmutableRecord {
    @Getter(onMethod_ = {@DynamoDbPartitionKey})
    private String partitionKey;

    @Getter(onMethod_ = {@DynamoDbSortKey})
    private int sortKey;

    // supported types:
    // https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/HowItWorks.NamingRulesDataTypes.html#HowItWorks.DataTypes

    // Scalar Types – A scalar type can represent exactly one value. The scalar types are number,
    // string, binary, boolean, and null.

    private String stringAttribute;

    private Boolean booleanAttribute;
    private boolean booleanPrimitiveAttribute;

    private Integer intAttribute;

    private Long longAttribute;

    private Float floatAttribute;

    private Double doubleAttribute;

    private Short shortAttribute;

    private byte[] byteAttribute;

    // Document Types – A document type can represent a complex structure with nested attributes,
    // such as what you would find in a JSON document. The document types are list and map.
    private List<String> stringList;

    private Map<String, String> stringStringMap;

    // The document types are list and map. These data types can be nested within each other, to
    // represent complex data structures up to 32 levels deep.

    private List<Nested> nestedList;

    private Map<String, Nested> nestedMap;

    private List<NestedLombok> nestedLombokList;

    // Set Types – A set type can represent multiple scalar values. The set types are string set,
    // number set, and binary set.

    private Set<String> stringSet;

    // when using @With dynamoDb needs to ignore it

    @With(onMethod_ = {@DynamoDbIgnore})
    private String fieldUsingWith;
}
