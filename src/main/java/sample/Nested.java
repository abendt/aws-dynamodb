package sample;

import lombok.EqualsAndHashCode;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.List;

@DynamoDbBean
@EqualsAndHashCode
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
