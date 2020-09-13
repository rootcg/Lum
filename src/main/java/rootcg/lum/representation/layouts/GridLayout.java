package rootcg.lum.representation.layouts;

import rootcg.lum.core.definitions.ObjectDefinition;

import java.util.*;

import static rootcg.lum.util.Validations.Arguments.check;

public class GridLayout {

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

        protected abstract char[][] print();

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

    private static final int CELL_SIZE = 5;

    private final Map<String, Point> points;
    private final GridCell[][] grid;

    private final int rows;
    private final int columns;

    public GridLayout(int rows, int columns) {
        this.points = new HashMap<>();
        this.grid = new GridCell[rows][columns];
        this.rows = rows;
        this.columns = columns;

        Arrays.stream(grid).forEach(line -> Arrays.fill(line, new EmptyCell()));
    }

    public GridLayout putObject(Point point, ObjectDefinition content) {
        grid[point.y][point.x] = new ObjectCell(content);
        points.put(content.getName(), point);
        return this;
    }

    public GridLayout interchange(String obj, Point point) {
        Point oldPoint = points.get(obj);
        if(oldPoint.equals(point)) {
            return this;
        }

        GridCell aux = grid[point.y][point.x];
        grid[point.y][point.x] = grid[oldPoint.y][oldPoint.x];
        grid[oldPoint.y][oldPoint.x] = aux;
        points.put(obj, point);

        if(aux instanceof ObjectCell) {
            ObjectCell objCell = (ObjectCell) aux;
            points.put(objCell.content.getName(), oldPoint);
        }

        return this;
    }

    public GridLayout moveOnTop(String bottom, String top) {
        check(points::containsKey, bottom);
        check(points::containsKey, top);

        Point bottomPoint = points.get(bottom);
        Point topPoint = points.get(top);

        if(topPoint.x == bottomPoint.x) {
            grid[topPoint.y][topPoint.x].lock();
            return this;
        }

        int row = bottomPoint.y - 1;
        for (int i = 0; i < columns; i++) {
            if(!grid[row][i].locked) {
                GridCell aux = grid[row][i];
                grid[row][i] = grid[topPoint.y][topPoint.x];
                grid[topPoint.y][topPoint.x] = aux;
                points.put(top, new Point(i, row));

                if(aux instanceof ObjectCell) {
                    ObjectCell objCell = (ObjectCell) aux;
                    points.put(objCell.content.getName(), topPoint);
                }

                grid[row][i].lock();
                break;
            }
        }

        return this;
    }

    public Point getPoint(String point) {
        return points.get(point);
    }

    public Optional<ObjectDefinition> getObject(Point point) {
        GridCell cell = grid[point.y][point.x];

        if(cell instanceof ObjectCell) {
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