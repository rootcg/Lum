package rootcg.lum.representation.drawers.impl;

import rootcg.lum.core.definitions.DiagramDefinition;
import rootcg.lum.core.definitions.ObjectDefinition;
import rootcg.lum.representation.drawers.Drawer;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HtmlDrawer implements Drawer {

    private static final String HTML_TEMPLATES_PATH = "html_templates/";
    private static final String DIAGRAM_TEMPLATE_PATH = "diagram.html";
    private static final String OBJECT_TEMPLATE_PATH = "object.html";
    private static final String ATTRIBUTES_TEMPLATE_PATH = "attribute.html";
    private static final String METHOD_TEMPLATE_PATH = "method.html";
    private static final String PARAMETER_TEMPLATE_PATH = "parameter.html";
    private static final String OUTPUT = "/Users/cristian/Workspace/lum/src/main/resources/output.html";

    @Override
    public void draw(DiagramDefinition diagramDefinition) {
        List<String> diagramTemplate = readTemplate(DIAGRAM_TEMPLATE_PATH);
        List<String> objectTemplate = readTemplate(OBJECT_TEMPLATE_PATH);
        List<String> attributeTemplate = readTemplate(ATTRIBUTES_TEMPLATE_PATH);
        List<String> methodTemplate = readTemplate(METHOD_TEMPLATE_PATH);
        List<String> parameterTemplate = readTemplate(PARAMETER_TEMPLATE_PATH);

        List<String> diagram = new ArrayList<>();
        for (ObjectDefinition object : diagramDefinition.getObjects()) {
            List<String> objectData = new ArrayList<>(objectTemplate);

            if (object.getDefinition().isEmpty())
                objectData.removeIf(line -> line.contains("!definition"));
            if (object.getAttributes().isEmpty())
                objectData.removeIf(line -> line.contains("!lum-attributes"));
            if (object.getMethods().isEmpty())
                objectData.removeIf(line -> line.contains("!lum-methods"));

            String attributesData =
                    object.getAttributes()
                          .stream()
                          .map(att -> String.join("", attributeTemplate)
                                            .replace("!name", att.getName().orElse(""))
                                            .replace("!type", att.getType().orElse(""))).collect(Collectors.joining());

            String methodsData =
                    object.getMethods().stream().map(method -> {
                        String parametersData =
                                method.getParameters()
                                      .stream()
                                      .map(param -> String.join("", parameterTemplate)
                                                          .replace("!name", param.getName().orElse(""))
                                                          .replace("!type", param.getType().orElse(""))).collect(Collectors.joining());

                        return String.join("", methodTemplate)
                                     .replace("!name", method.getName().orElse(""))
                                     .replace("!type", method.getType().orElse(""))
                                     .replace("!lum-parameters", parametersData);
                    }).collect(Collectors.joining());

            String objectDataReduced =
                    String.join("", objectData)
                          .replace("!name", object.getName())
                          .replace("!definition", object.getDefinition().orElse(""))
                          .replace("!lum-attributes", attributesData)
                          .replace("!lum-methods", methodsData);

            diagram.add(objectDataReduced);
        }

        try {
            Files.writeString(Path.of(OUTPUT), String.join("", diagramTemplate).replace("!lum-objects", String.join("", diagram)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> readTemplate(String path) {
        URL file = getClass().getClassLoader().getResource(HTML_TEMPLATES_PATH.concat(path));

        try {
            return Files.readAllLines(Path.of(Objects.requireNonNull(file).getFile()));
        } catch (IOException | NullPointerException e) {
            throw new IllegalStateException("can't read template: " + path);
        }
    }

}
