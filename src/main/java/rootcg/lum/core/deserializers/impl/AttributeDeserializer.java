package rootcg.lum.core.deserializers.impl;

import rootcg.lum.core.definitions.AttributeDefinition;
import rootcg.lum.core.deserializers.Deserializer;
import rootcg.lum.core.deserializers.exceptions.DeserializationException;

import java.util.List;
import java.util.regex.Pattern;

import static java.util.function.Predicate.not;

public class AttributeDeserializer implements Deserializer<AttributeDefinition> {

    private static final String EXPRESSION_SEPARATOR = " ";
    private static final Pattern expressionSplitter = Pattern.compile(EXPRESSION_SEPARATOR);

    @Override
    public boolean accept(List<String> block) {
        if (block.size() != 1 || !block.get(0).startsWith(EXPRESSION_SEPARATOR))
            return false;

        int terms = normalize(block.get(0)).length;
        return terms > 0 && terms <= 2;
    }

    @Override
    public AttributeDefinition deserialize(List<String> block) throws DeserializationException {
        String[] expression = normalize(block.get(0));

        AttributeDefinition.Builder attributeBuilder = AttributeDefinition.builder();
        if (expression.length == 1)
            attributeBuilder = attributeBuilder.withName(expression[0]);
        else if (expression.length == 2)
            attributeBuilder = attributeBuilder.withType(expression[0]).withName(expression[1]);
        else
            throw new DeserializationException(block.get(0));

        return attributeBuilder.build();
    }

    private String[] normalize(String source) {
        return expressionSplitter.splitAsStream(source).map(String::strip).filter(not(String::isEmpty)).toArray(String[]::new);
    }

}
