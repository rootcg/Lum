package rootcg.lum.core.deserializers.impl;

import rootcg.lum.core.definitions.Relation;
import rootcg.lum.core.definitions.RelationType;
import rootcg.lum.core.deserializers.Deserializer;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class RelationDeserializer implements Deserializer<Relation> {

    private static final Pattern splitter = Pattern.compile(" ");

    @Override
    public boolean accept(List<String> block) {
        if (block.size() != 1)
            return false;

        String[] expression = splitter.split(block.get(0));
        if (expression.length != 3)
            return false;

        return Arrays.stream(RelationType.values()).map(RelationType::getRepresentation).anyMatch(expression[1]::equals);
    }

    @Override
    public Relation deserialize(List<String> block) {
        throw new IllegalStateException("not implemented");
    }

}
