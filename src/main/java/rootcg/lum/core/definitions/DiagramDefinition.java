package rootcg.lum.core.definitions;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DiagramDefinition {

    private final List<ObjectDefinition> objects;
    private final List<RelationDefinition> relations;

    public DiagramDefinition(List<ObjectDefinition> objects, List<RelationDefinition> relations) {
        Optional<String> invalidRef =
                Stream.concat(relations.stream().map(RelationDefinition::getSource), relations.stream().map(RelationDefinition::getTarget))
                      .distinct()
                      .filter(objectRef -> objects.stream().map(ObjectDefinition::getName).noneMatch(objectRef::equals))
                      .findFirst();

        if (invalidRef.isPresent())
            throw new IllegalArgumentException("Illegal reference to an object: " + invalidRef.get());

        this.objects = objects;
        this.relations = relations;
    }

    public List<ObjectDefinition> getObjects() {
        return objects;
    }

    public List<RelationDefinition> getRelations() {
        return relations;
    }

}
