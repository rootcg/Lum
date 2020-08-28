package rootcg.lum.core.deserializers.impl;

import rootcg.lum.core.definitions.RelationDefinition;
import rootcg.lum.core.deserializers.Deserializer;
import rootcg.lum.core.deserializers.exceptions.DeserializationException;

import java.util.ArrayList;
import java.util.List;

public class RelationBlockDeserializer implements Deserializer<List<RelationDefinition>> {

    private static final RelationDeserializer relationDeserializer = new RelationDeserializer();

    @Override
    public boolean accept(List<String> block) {
        return block.size() > 0 && block.stream().map(List::of).allMatch(relationDeserializer::accept);
    }

    @Override
    public List<RelationDefinition> deserialize(List<String> block) throws DeserializationException {
        List<RelationDefinition> relations = new ArrayList<>();

        for (String line : block) {
            relations.add(relationDeserializer.deserialize(List.of(line)));
        }

        return relations;
    }

}
