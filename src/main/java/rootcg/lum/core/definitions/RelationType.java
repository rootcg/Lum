package rootcg.lum.core.definitions;

import java.util.Arrays;
import java.util.Optional;

public enum RelationType {

    USE("use"), IMPLEMENTS("implements");

    String representation;

    RelationType(String representation) {
        this.representation = representation;
    }

    public String getRepresentation() {
        return representation;
    }

    public static Optional<RelationType> from(String representation) {
        return Arrays.stream(RelationType.values()).filter(type -> type.representation.equals(representation)).findFirst();
    }

}
