package rootcg.lum.representation.layouts;

import rootcg.lum.core.definitions.ObjectDefinition;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class GridLayout implements Layout {

    public static abstract class GridCell {
        private boolean locked;

        public GridCell() {
            locked = false;
        }

        public void lock() {
            locked = true;
        }

        public boolean isLocked() {
            return locked;
        }

        public abstract char[][] print();
    }

    public static class ObjectCell extends GridCell {

        private final ObjectDefinition content;

        public ObjectCell(ObjectDefinition content) {
            this.content = content;
        }

        public ObjectDefinition getContent() {
            return content;
        }

        @Override
        public char[][] print() {
            char[][] arr = new char[CELL_SIZE][CELL_SIZE];
            Arrays.stream(arr).forEach(line -> Arrays.fill(line, content.getName().charAt(0)));
            return arr;
        }
    }

    public static class EmptyCell extends GridCell {
        @Override
        public char[][] print() {
            char[][] content = new char[CELL_SIZE][CELL_SIZE];
            Arrays.stream(content).forEach(line -> Arrays.fill(line, '+'));
            return content;
        }
    }

    public static class Point {
        final int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point upper() {
            return new Point(x, y - 1);
        }

        public Point lower() {
            return new Point(x, y + 1);
        }

        public Point right() {
            return new Point(x + 1, y);
        }

        public Point left() {
            return new Point(x - 1, y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public static final int CELL_SIZE = 5;

    private final Map<String, Point> points;
    private final int rows;
    private final int columns;

    private GridCell[][] grid;

    public GridLayout(int rows, int columns) {
        this.points = new HashMap<>();
        this.grid = new GridCell[rows][columns];
        this.rows = rows;
        this.columns = columns;

        Arrays.stream(grid).forEach(line -> Arrays.fill(line, new EmptyCell()));
    }

    public void addGap() {
        GridCell[][] newGrid = new GridCell[rows * 2 + 1][columns * 2 + 1];

        AtomicInteger rowCount = new AtomicInteger(0);
        for (GridCell[] gridCells : grid) {
            Arrays.fill(newGrid[rowCount.getAndIncrement()], new EmptyCell());
            int newRow = rowCount.getAndIncrement();
            AtomicInteger columnsCount = new AtomicInteger(0);
            for (GridCell gridCell : gridCells) {
                newGrid[newRow][columnsCount.getAndIncrement()] = new EmptyCell();
                newGrid[newRow][columnsCount.getAndIncrement()] = gridCell;
            }
            newGrid[newRow][columnsCount.getAndIncrement()] = new EmptyCell();
        }
        Arrays.fill(newGrid[rowCount.getAndIncrement()], new EmptyCell());

        this.grid = newGrid;
    }

    public void putObject(Point point, ObjectDefinition content) {
        grid[point.y][point.x] = new ObjectCell(content);
        points.put(content.getName(), point);
    }

    public void interchange(String obj, Point point) {
        Point oldPoint = points.get(obj);
        if (oldPoint.equals(point)) {
            return;
        }

        GridCell aux = grid[point.y][point.x];
        grid[point.y][point.x] = grid[oldPoint.y][oldPoint.x];
        grid[oldPoint.y][oldPoint.x] = aux;
        points.put(obj, point);

        if (aux instanceof ObjectCell) {
            ObjectCell objCell = (ObjectCell) aux;
            points.put(objCell.content.getName(), oldPoint);
        }
    }

    public void moveOnTop(String bottom, String top) {
        if (!points.containsKey(bottom) || !points.containsKey(top))
            return;

        Point bottomPoint = points.get(bottom);
        Point topPoint = points.get(top);

        if (topPoint.x == bottomPoint.x) {
            grid[topPoint.y][topPoint.x].lock();
            return;
        }

        int row = bottomPoint.y - 1;
        IntStream.range(0, columns)
                 .filter(i -> !grid[row][i].locked)
                 .findFirst()
                 .ifPresent(i -> {
                     GridCell aux = grid[row][i];
                     grid[row][i] = grid[topPoint.y][topPoint.x];
                     grid[topPoint.y][topPoint.x] = aux;
                     points.put(top, new Point(i, row));

                     if (aux instanceof ObjectCell) {
                         ObjectCell objCell = (ObjectCell) aux;
                         points.put(objCell.content.getName(), topPoint);
                     }

                     grid[row][i].lock();
                 });
    }

    public Point getPoint(String point) {
        return points.get(point);
    }

    public Optional<ObjectDefinition> getObject(Point point) {
        GridCell cell = grid[point.y][point.x];

        if (cell instanceof ObjectCell) {
            return Optional.of(((ObjectCell) cell).content);
        } else {
            return Optional.empty();
        }
    }

    public GridCell getCell(Point point) {
        return grid[point.y][point.x];
    }

    public GridCell[][] getGrid() {
        return grid;
    }

}
