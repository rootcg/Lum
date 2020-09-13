package rootcg.lum.representation.drawers.impl;

import rootcg.lum.representation.drawers.Drawer;
import rootcg.lum.representation.layouts.GridLayout;

public class CommandLineDrawer implements Drawer<GridLayout> {

    @Override
    public void draw(GridLayout layout) {
        GridLayout.GridCell[][] grid = layout.getGrid();

        for (int i = 0; i < grid.length; i++) {
            for (int i2 = 0; i2 < GridLayout.CELL_SIZE; i2++) {
                for (int j = 0; j < grid[i].length; j++) {
                    String[][] chars = grid[i][j].print();
                    for (int j2 = 0; j2 < GridLayout.CELL_SIZE; j2++) {
                        System.out.print(chars[i2][j2]);
                    }
                }
                System.out.println();
            }
        }
    }

}
