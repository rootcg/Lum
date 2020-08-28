package rootcg.lum.core.parsers.impl;

import rootcg.lum.core.deserializers.Deserializer;
import rootcg.lum.core.deserializers.exceptions.DeserializationException;
import rootcg.lum.core.deserializers.exceptions.ParseException;
import rootcg.lum.core.deserializers.impl.ObjectDeserializer;
import rootcg.lum.core.deserializers.impl.RelationDeserializer;
import rootcg.lum.core.parsers.LumParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class LumParserImpl implements LumParser {

    private static final List<Deserializer<?>> deserializers = List.of(new RelationDeserializer(), new ObjectDeserializer());

    @Override
    public void parse(Path filePath) throws IOException, ParseException {
        var lines = Files.lines(filePath).collect(Collectors.toList());

        int[] blankLinesIndexes =
                Stream.of(IntStream.of(0),
                          IntStream.range(0, lines.size()).filter(i -> lines.get(i).isBlank()),
                          IntStream.of(lines.size()))
                      .flatMapToInt(Function.identity())
                      .toArray();

        List<List<String>> blocks =
                IntStream.range(0, blankLinesIndexes.length - 1)
                         .mapToObj(i -> lines.subList(blankLinesIndexes[i], blankLinesIndexes[i + 1]))
                         .map(block -> block.stream().filter(not(String::isBlank)).collect(Collectors.toList()))
                         .collect(Collectors.toList());

        List<Object> components = new ArrayList<>();
        for (List<String> block : blocks) {
            Deserializer<?> deserializer =
                    deserializers.stream()
                                 .filter(des -> des.accept(block))
                                 .findFirst()
                                 .orElseThrow(() -> new ParseException((lines.indexOf(block.get(0)) + 1),
                                                                       " illegal expression " + block.get(0)));

            try {
                components.add(deserializer.deserialize(block));
            } catch (DeserializationException e) {
                throw new ParseException((lines.indexOf(block.get(0)) + 1), e.getMessage(), e);
            }
        }

        System.out.println(components);
    }

}
