package sample;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.List;

@DynamoDbBean
@ToString
@EqualsAndHashCode
/*
    we have added Lombok toString and equalsHashCode mainly for purposes of the test code.
    We need a deep recursive comparator for the assertions and we want a meaningful toString representation
    in case one of the tests fail.
    Lombok is not required for the actual mapping.
 */
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
