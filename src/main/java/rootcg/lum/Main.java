package rootcg.lum;

import rootcg.lum.core.deserializers.exceptions.ParseException;
import rootcg.lum.core.parsers.LumParser;
import rootcg.lum.core.parsers.impl.LumParserImpl;

import java.io.IOException;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        LumParser lumParser = new LumParserImpl();
        lumParser.parse(Path.of("/Users/cristian/Workspace/lum/src/main/resources/test.lum"));
    }

}
