package rootcg.lum.core.deserializers.impl;

import rootcg.lum.core.definitions.AttributeDefinition;
import rootcg.lum.core.definitions.MethodDefinition;
import rootcg.lum.core.definitions.ObjectDefinition;
import rootcg.lum.core.deserializers.Deserializer;
import rootcg.lum.core.deserializers.exceptions.DeserializationException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ObjectDeserializer implements Deserializer<ObjectDefinition> {

    private static final String EXPRESSION_SEPARATOR = " ";
    private static final List<Deserializer<?>> deserializers = List.of(new MethodDeserializer(), new AttributeDeserializer());
    private static final Pattern expressionSplitter = Pattern.compile(EXPRESSION_SEPARATOR);

    @Override
    public boolean accept(List<String> block) {
        if (block.isEmpty() || block.get(0).startsWith(EXPRESSION_SEPARATOR))
            return false;

        long terms = expressionSplitter.splitAsStream(block.get(0)).count();
        return terms > 0 && terms <= 2
                && block.stream()
                        .skip(1)
                        .map(expression -> deserializers.stream().anyMatch(deserializer -> deserializer.accept(List.of(expression))))
                        .reduce(Boolean::logicalOr)
                        .orElse(false);
    }

    @Override
    public ObjectDefinition deserialize(List<String> block) throws DeserializationException {
        List<String> safeBlock = new ArrayList<>(block);
        String[] objectHeader = expressionSplitter.splitAsStream(safeBlock.remove(0))
                                                  .map(String::strip)
                                                  .toArray(String[]::new);

        ObjectDefinition.Builder objectBuilder = ObjectDefinition.builder();
        if (objectHeader.length == 1) {
            objectBuilder = objectBuilder.withName(objectHeader[0]);
        } else if (objectHeader.length == 2) {
            objectBuilder = objectBuilder.withDefinition(objectHeader[0]).withName(objectHeader[1]);
        } else {
            throw new DeserializationException(block.get(0));
        }

        List<Object> components = new ArrayList<>();
        for (String line : safeBlock) {
            Deserializer<?> deserializer =
                    deserializers.stream()
                                 .filter(des -> des.accept(List.of(line)))
                                 .findFirst()
                                 .orElseThrow(() -> new DeserializationException("illegal expression: " + line));

            components.add(deserializer.deserialize(List.of(line)));
        }

        objectBuilder.withAttributes(components.stream()
                                               .filter(AttributeDefinition.class::isInstance)
                                               .map(AttributeDefinition.class::cast)
                                               .collect(Collectors.toList()));

        objectBuilder.withMethods(components.stream()
                                            .filter(MethodDefinition.class::isInstance)
                                            .map(MethodDefinition.class::cast)
                                            .collect(Collectors.toList()));

        return objectBuilder.build();
    }

}
