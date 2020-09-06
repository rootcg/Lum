package rootcg.lum.core.definitions;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class DiagramDefinition {

    private final List<ObjectDefinition> objects;
    private final List<RelationDefinition> relations;

    public DiagramDefinition(List<ObjectDefinition> objects, List<RelationDefinition> relations) {
        Set<String> names = objects.stream().map(ObjectDefinition::getName).collect(Collectors.toSet());
        Optional<String> invalidRef =
                relations.stream()
                         .flatMap(relation -> Stream.of(relation.getTarget(), relation.getSource()))
                         .filter(not(names::contains))
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
