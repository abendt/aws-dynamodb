package sample;

import lombok.EqualsAndHashCode;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDbBean
@EqualsAndHashCode
public class JavaRecord {
    private String partitionKey;
    private int sortKey;

    // supported types: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/HowItWorks.NamingRulesDataTypes.html#HowItWorks.DataTypes

    // Scalar Types – A scalar type can represent exactly one value. The scalar types are number, string, binary, Boolean, and null.

    private String stringAttribute;

    private boolean booleanAttribute;

    private int intAttribute;

    private byte[] byteAttribute;

    // Document Types – A document type can represent a complex structure with nested attributes, such as what you would find in a JSON document. The document types are list and map.
    private List<String> stringList;

    private Map<String, String> stringStringMap;

    // The document types are list and map. These data types can be nested within each other, to represent complex data structures up to 32 levels deep.

    private List<Nested> nestedList;

    private Map<String, Nested> nestedMap;

    // Set Types – A set type can represent multiple scalar values. The set types are string set, number set, and binary set.

    private Set<String> stringSet;

    @DynamoDbPartitionKey
    public String getPartitionKey() {
        return partitionKey;
    }

    public void setPartitionKey(String partitionKey) {
        this.partitionKey = partitionKey;
    }

    @DynamoDbSortKey
    public int getSortKey() {
        return sortKey;
    }

    public void setSortKey(int sortKey) {
        this.sortKey = sortKey;
    }

    public String getStringAttribute() {
        return stringAttribute;
    }

    public void setStringAttribute(String stringAttribute) {
        this.stringAttribute = stringAttribute;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public List<Nested> getNestedList() {
        return nestedList;
    }

    public void setNestedList(List<Nested> nestedList) {
        this.nestedList = nestedList;
    }

    public Map<String, String> getStringStringMap() {
        return stringStringMap;
    }

    public void setStringStringMap(Map<String, String> stringStringMap) {
        this.stringStringMap = stringStringMap;
    }

    public Map<String, Nested> getNestedMap() {
        return nestedMap;
    }

    public void setNestedMap(Map<String, Nested> nestedMap) {
        this.nestedMap = nestedMap;
    }

    public Set<String> getStringSet() {
        return stringSet;
    }

    public void setStringSet(Set<String> stringSet) {
        this.stringSet = stringSet;
    }

    public boolean isBooleanAttribute() {
        return booleanAttribute;
    }

    public void setBooleanAttribute(boolean booleanAttribute) {
        this.booleanAttribute = booleanAttribute;
    }

    public int getIntAttribute() {
        return intAttribute;
    }

    public void setIntAttribute(int intAttribute) {
        this.intAttribute = intAttribute;
    }

    public byte[] getByteAttribute() {
        return byteAttribute;
    }

    public void setByteAttribute(byte[] byteAttribute) {
        this.byteAttribute = byteAttribute;
    }
}
