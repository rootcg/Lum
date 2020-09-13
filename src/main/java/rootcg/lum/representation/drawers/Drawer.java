package rootcg.lum.representation.drawers;

import rootcg.lum.representation.layouts.Layout;

public interface Drawer<T extends Layout> {

    void draw(T layout);

}
