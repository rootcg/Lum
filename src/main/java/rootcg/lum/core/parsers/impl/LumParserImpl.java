package rootcg.lum.core.parsers.impl;

import rootcg.lum.core.definitions.DiagramDefinition;
import rootcg.lum.core.definitions.ObjectDefinition;
import rootcg.lum.core.definitions.RelationDefinition;
import rootcg.lum.core.deserializers.exceptions.DeserializationException;
import rootcg.lum.core.deserializers.exceptions.ParseException;
import rootcg.lum.core.deserializers.impl.ObjectDeserializer;
import rootcg.lum.core.deserializers.impl.RelationBlockDeserializer;
import rootcg.lum.core.parsers.LumParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

public class LumParserImpl implements LumParser {

    private static final RelationBlockDeserializer relationBlockDeserializer = new RelationBlockDeserializer();
    private static final ObjectDeserializer objectDeserializer = new ObjectDeserializer();

    @Override
    public DiagramDefinition parse(Path filePath) throws IOException, ParseException {
        List<String> lines = Files.lines(filePath).collect(toList());

        int[] blankLinesIndexes =
                Stream.of(IntStream.of(0),
                          IntStream.range(0, lines.size()).filter(i -> lines.get(i).isBlank()),
                          IntStream.of(lines.size()))
                      .flatMapToInt(Function.identity())
                      .toArray();

        List<List<String>> blocks =
                IntStream.range(0, blankLinesIndexes.length - 1)
                         .mapToObj(i -> lines.subList(blankLinesIndexes[i], blankLinesIndexes[i + 1]))
                         .map(block -> block.stream().filter(not(String::isBlank)).collect(toList()))
                         .collect(toList());

        List<Object> components = new ArrayList<>();
        for (List<String> block : blocks) {
            try {
                if (relationBlockDeserializer.accept(block))
                    components.addAll(relationBlockDeserializer.deserialize(block));
                else if (objectDeserializer.accept(block))
                    components.add(objectDeserializer.deserialize(block));
                else
                    throw new ParseException((lines.indexOf(block.get(0)) + 1), " illegal expression starting with: " + block.get(0));
            } catch (DeserializationException e) {
                throw new ParseException((lines.indexOf(block.get(0)) + 1), e.getMessage(), e);
            }
        }

        return new DiagramDefinition(
                components.stream().filter(ObjectDefinition.class::isInstance).map(ObjectDefinition.class::cast).collect(toList()),
                components.stream().filter(RelationDefinition.class::isInstance).map(RelationDefinition.class::cast).collect(toList()));
    }

}
