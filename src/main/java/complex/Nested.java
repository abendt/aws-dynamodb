package complex;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

/*
Lombok types are used here solely for easier testing. They are not required or used by DynamoDB.
 */
@EqualsAndHashCode
@ToString
@DynamoDbBean
public class Nested {
    private String stringAttribute;

    private List<Nested> nestedList;

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
