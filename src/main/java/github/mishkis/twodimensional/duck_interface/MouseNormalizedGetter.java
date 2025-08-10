package github.mishkis.twodimensional.duck_interface;

public interface MouseNormalizedGetter {
    default double twoDimensional$getNormalizedX() { return 0d; }
    default double twoDimensional$getNormalizedY() { return 0d; }
}
