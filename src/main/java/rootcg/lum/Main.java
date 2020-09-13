package rootcg.lum;

import rootcg.lum.core.definitions.DiagramDefinition;
import rootcg.lum.core.deserializers.exceptions.ParseException;
import rootcg.lum.core.parsers.LumParser;
import rootcg.lum.core.parsers.impl.LumParserImpl;
import rootcg.lum.representation.drawers.Drawer;
import rootcg.lum.representation.drawers.impl.CommandLineDrawer;
import rootcg.lum.representation.layouts.GridLayout;
import rootcg.lum.representation.layouts.TopToBottom;

import java.io.IOException;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        LumParser lumParser = new LumParserImpl();
        DiagramDefinition diagram = lumParser.parse(Path.of("/Users/cristian/Workspace/lum/src/main/resources/test.lum"));
        Drawer<GridLayout> drawer = new CommandLineDrawer();
        drawer.draw(TopToBottom.of(diagram));
    }

}
