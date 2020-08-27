package rootcg.lum.core.definitions;

public enum RelationType {

    USE("use"), IMPLEMENTS("implements");

    String representation;

    RelationType(String representation) {
        this.representation = representation;
    }

    public String getRepresentation() {
        return representation;
    }

}
