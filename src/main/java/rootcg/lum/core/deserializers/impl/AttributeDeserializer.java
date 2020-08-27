package rootcg.lum.core.deserializers.impl;

import rootcg.lum.core.definitions.AttributeDefinition;
import rootcg.lum.core.deserializers.Deserializer;
import rootcg.lum.core.deserializers.exceptions.ParseException;

import java.util.List;
import java.util.regex.Pattern;

public class AttributeDeserializer implements Deserializer<AttributeDefinition> {

    private static final String EXPRESSION_SEPARATOR = " ";
    private static final Pattern expressionSplitter = Pattern.compile(EXPRESSION_SEPARATOR);

    @Override
    public boolean accept(List<String> block) {
        if (block.size() != 1 || block.get(0).startsWith(EXPRESSION_SEPARATOR))
            return false;

        long terms = expressionSplitter.splitAsStream(block.get(0)).count();
        return terms > 0 && terms <= 2;
    }

    @Override
    public AttributeDefinition deserialize(List<String> block) throws ParseException {
        String[] expression = expressionSplitter.splitAsStream(block.get(0))
                                                .map(String::strip)
                                                .toArray(String[]::new);

        AttributeDefinition.Builder attributeBuilder = AttributeDefinition.builder();
        if (expression.length == 1)
            attributeBuilder = attributeBuilder.withName(expression[0]);
        else if (expression.length == 2)
            attributeBuilder = attributeBuilder.withType(expression[0]).withName(expression[1]);
        else
            throw new ParseException(block.get(0));

        return attributeBuilder.build();
    }

}
