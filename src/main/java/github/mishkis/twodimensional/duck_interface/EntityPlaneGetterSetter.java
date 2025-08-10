package github.mishkis.twodimensional.duck_interface;

import github.mishkis.twodimensional.utils.Plane;

public interface EntityPlaneGetterSetter {
    default Plane twoDimensional$getPlane() { return null; }
    default void twoDimensional$setPlane(Plane plane) {}
}
