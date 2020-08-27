package rootcg.lum.core.definitions;

public enum Scope {

    PUBLIC('+'), PRIVATE('-');

    char representation;

    Scope(char representation) {
        this.representation = representation;
    }

    public char getRepresentation() {
        return representation;
    }

}
