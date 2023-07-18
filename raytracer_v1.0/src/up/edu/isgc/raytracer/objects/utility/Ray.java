/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.objects.utility;

/**
 * Stores information for a ray such as the origin and direction
 *
 * @author Homar Cano, Jafet Rodríguez
 */
public class Ray {
    private Vector3D origin;
    private Vector3D direction;

    /**
     * class constructor
     * @param origin the origin of the ray
     * @param direction the direction of the ray
     */
    public Ray(Vector3D origin, Vector3D direction) {
        setOrigin(origin);
        setDirection(direction);
    }

    /**
     * class getter
     * @return the ray origin
     */
    public Vector3D getOrigin() {
        return this.origin;
    }
    /**
     * class setter
     * @param origin the ray origin to set
     */
    public void setOrigin(Vector3D origin) {
        this.origin = origin;
    }

    /**
     * class getter
     * @return the ray direction
     */
    public Vector3D getDirection() {
        return Vector3D.normalize(this.direction);
    }
    /**
     * class setter
     * @param direction the ray direction to set
     */
    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }

}
