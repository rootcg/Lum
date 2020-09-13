package rootcg.lum.core.definitions;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.function.Function.identity;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.*;

public class DiagramDefinition {

    private final List<ObjectDefinition> objects;
    private final List<RelationDefinition> relations;
    private final Map<String, List<String>> outgoingRelations;
    private final Map<String, List<String>> incomingRelations;
    private final Map<String, List<List<String>>> relationPaths;

    public DiagramDefinition(List<ObjectDefinition> objects, List<RelationDefinition> relations) {
        Set<String> names = objects.stream().map(ObjectDefinition::getName).collect(Collectors.toSet());
        Optional<String> invalidRef =
                relations.stream()
                         .flatMap(relation -> Stream.of(relation.getTarget(), relation.getSource()))
                         .filter(not(names::contains))
                         .findFirst();

        if (invalidRef.isPresent())
            throw new IllegalArgumentException("Illegal reference to an object: " + invalidRef.get());

        // Base definitions
        this.objects = List.copyOf(objects);
        this.relations = List.copyOf(relations);

        // Relations
        this.outgoingRelations =
                relations.stream().collect(groupingBy(RelationDefinition::getSource, mapping(RelationDefinition::getTarget, toList())));
        this.incomingRelations =
                relations.stream().collect(groupingBy(RelationDefinition::getTarget, mapping(RelationDefinition::getSource, toList())));

        // Fill relation lists with remaining objects
        objects.stream().map(ObjectDefinition::getName).forEach(key -> {
            outgoingRelations.putIfAbsent(key, emptyList());
            incomingRelations.putIfAbsent(key, emptyList());
        });

        this.relationPaths =
                objects.stream()
                       .map(ObjectDefinition::getName)
                       .collect(toMap(identity(), name -> getRelationPaths(name, outgoingRelations)));

    }

    private static List<List<String>> getRelationPaths(String entity, Map<String, List<String>> allRelations) {
        List<String> relations = allRelations.get(entity);
        if (relations.isEmpty()) {
            return singletonList(singletonList(entity));
        }

        return relations.stream()
                        .map(relation -> getRelationPaths(relation, allRelations))
                        .map(paths -> paths.stream()
                                           .flatMap(path -> Stream.concat(Stream.of(entity), path.stream()))
                                           .collect(toList()))
                        .collect(toList());
    }

    public List<ObjectDefinition> getObjects() {
        return objects;
    }

    public List<RelationDefinition> getRelations() {
        return relations;
    }

    public Map<String, List<String>> getOutgoingRelations() {
        return outgoingRelations;
    }

    public Map<String, List<String>> getIncomingRelations() {
        return incomingRelations;
    }

    public Map<String, List<List<String>>> getRelationPaths() {
        return relationPaths;
    }

}
