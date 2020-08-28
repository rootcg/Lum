package rootcg.lum.core.deserializers.impl;

import rootcg.lum.core.definitions.RelationDefinition;
import rootcg.lum.core.definitions.RelationType;
import rootcg.lum.core.deserializers.Deserializer;
import rootcg.lum.core.deserializers.exceptions.DeserializationException;

import java.util.List;
import java.util.regex.Pattern;

import static java.util.function.Predicate.not;

public class RelationDeserializer implements Deserializer<RelationDefinition> {

    private static final Pattern splitter = Pattern.compile(" ");

    @Override
    public boolean accept(List<String> block) {
        return block.size() == 1 && splitter.split(block.get(0)).length == 3;
    }

    @Override
    public RelationDefinition deserialize(List<String> block) throws DeserializationException {
        String[] expression = splitter.splitAsStream(block.get(0)).map(String::strip).filter(not(String::isEmpty)).toArray(String[]::new);
        RelationType type = RelationType.from(expression[1])
                                        .orElseThrow(() -> new DeserializationException(expression[1] + " is not a valid relation clause"));

        return RelationDefinition.builder().withSource(expression[0]).withType(type).withTarget(expression[2]).build();
    }

}
