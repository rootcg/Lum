package rootcg.lum.representation.layouts;

import rootcg.lum.core.definitions.DiagramDefinition;
import rootcg.lum.core.definitions.ObjectDefinition;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class TopToBottom {

    private static final int CELL_SIZE = 5;

    public static void create(DiagramDefinition diagramDefinition) {
        diagramDefinition.getRelationPaths().forEach((k, v) -> {
            System.out.println(k + ": ");
            v.forEach(path -> System.out.println(">> " + String.join(" -> ", path.toArray(new String[]{}))));
            System.out.println();
        });

        var incomingRelations = diagramDefinition.getIncomingRelations();
        List<List<ObjectDefinition>> levels =
                diagramDefinition.getObjects().stream().collect(groupingBy(obj -> incomingRelations.get(obj.getName()).size()))
                                 .entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey)).map(Map.Entry::getValue)
                                 .collect(toList());

        // Create grid
        int rows = Math.max(levels.size(), 1);
        int columns = levels.stream().mapToInt(List::size).max().orElse(1);
        GridLayout layout = new GridLayout(rows, columns);

        // Put objects
        for (int i = 0; i < levels.size(); i++) {
            for (int j = 0; j < levels.get(i).size(); j++) {
                layout.putObject(new GridLayout.Point(j, i), levels.get(i).get(j));
            }
        }

        // Organize
        IntStream.iterate(levels.size() - 1, n -> n > 0, n -> n - 1).forEach(n -> {
            List<String> level = levels.get(n).stream().map(ObjectDefinition::getName).collect(toList());
            List<String> previousLevel = levels.get(n - 1).stream().map(ObjectDefinition::getName).collect(toList());
            level.forEach(obj -> {
                List<String> relations = incomingRelations.get(obj);
                GridLayout.Point point = layout.getPoint(obj);
                if (!layout.getCell(point).isLocked()) {
                    GridLayout.Point upperPoint = point.upper();
                    GridLayout.GridCell topCell = layout.getCell(upperPoint);
                    while (topCell.isLocked() && layout.getObject(upperPoint).map(ObjectDefinition::getName)
                                                       .map(name -> !relations.contains(name)).orElse(false)) {
                        point = point.right();
                        upperPoint = point.upper();
                        topCell = layout.getCell(upperPoint);
                    }

                    layout.interchange(obj, point);
                }

                relations.stream().filter(previousLevel::contains).forEach(relatedObj -> layout.moveOnTop(obj, relatedObj));
            });
        });

        print(layout);
    }

    private static void print(GridLayout layout) {
        GridLayout.GridCell[][] grid = layout.getGrid();

        for (int i = 0; i < grid.length; i++) {
            for (int i2 = 0; i2 < CELL_SIZE; i2++) {
                for (int j = 0; j < grid[i].length; j++) {
                    char[][] chars = grid[i][j].print();
                    for (int j2 = 0; j2 < CELL_SIZE; j2++) {
                        System.out.print(chars[i2][j2] + " ");
                    }
                }
                System.out.println();
            }
        }
    }

}
