package rootcg.lum.core.parsers;

import rootcg.lum.core.definitions.DiagramDefinition;
import rootcg.lum.core.deserializers.exceptions.ParseException;

import java.io.IOException;
import java.nio.file.Path;

public interface LumParser {

    DiagramDefinition parse(Path filePath) throws IOException, ParseException;

}
