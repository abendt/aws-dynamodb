package complex;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

/*
Lombok annotations are used here solely for easier testing. They are not required or used by DynamoDB.
 */
@EqualsAndHashCode
@ToString
@DynamoDbBean
public class JavaComplexItem {

    private String partitionKey;
    private int sortKey;

    private String stringAttribute;

    private List<Nested> nestedList;

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

    public List<Nested> getNestedList() {
        return nestedList;
    }

    public void setNestedList(List<Nested> nestedList) {
        this.nestedList = nestedList;
    }
}
