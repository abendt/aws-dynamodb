package basic;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

/*
Lombok types are used here solely for easier testing. They are not required or used by DynamoDB.
 */
@EqualsAndHashCode
@ToString

// START example
@DynamoDbBean
public class JavaRecord {

    private String partitionKey;
    private int sortKey;

    private String stringAttribute;

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

    // END example

    public void setSortKey(int sortKey) {
        this.sortKey = sortKey;
    }

    public String getStringAttribute() {
        return stringAttribute;
    }

    public void setStringAttribute(String stringAttribute) {
        this.stringAttribute = stringAttribute;
    }
}
