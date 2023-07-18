/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.objects.utility;

/**
 * Represents a vector in a 3D space with x, y, z coordinates
 * Has methods for vectors operations such as addition, subtraction, scalar multiplication, cross product and dot product
 * Has methods for easy vector manipulation such as normalization, string conversion and others
 *
 * @author Homar Cano, Jafet Rodríguez
 */
public class Vector3D {
    private double x;
    private double y;
    private double z;

    /**
     * private constant for a 3D vector with values x=0, y=0, z=0
     */
    private static final Vector3D ZERO = new Vector3D(0d, 0d,0d);

    /**
     * class constructor
     * @param x value of the x coordinate
     * @param y value of the y coordinate
     * @param z value of the z coordinate
     */
    public Vector3D(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    /**
     * clones any given 3D vector
     * @param vector the vector to clone
     * @return the vector cloned
     */
    public static Vector3D clone(Vector3D vector) {
        return new Vector3D(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * calculates the magnitude of a 3D vector
     * @param vector the vector to calculate its magnitude
     * @return the magnitude of the vector
     */
    public static double magnitude(Vector3D vector) {
        return Math.sqrt(dotProduct(vector, vector));
    }

    /**
     * normalizes any given 3D vector
     * @param vector the vector to normalize
     * @return the vector normalized
     */
    public static Vector3D normalize(Vector3D vector) {
        double magnitude = Vector3D.magnitude(vector);
        return new Vector3D(vector.getX()/magnitude, vector.getY()/magnitude, vector.getZ()/magnitude);
    }

    /**
     * adds a 3D vector A with a 3D vector B
     * @param vectorA the A vector
     * @param vectorB the B vector
     * @return the A+B vector
     */
    public static Vector3D add(Vector3D vectorA, Vector3D vectorB) {
        return new Vector3D(vectorA.getX() + vectorB.getX(), vectorA.getY() + vectorB.getY(), vectorA.getZ() + vectorB.getZ());
    }

    /**
     * subtracts a 3D vector A with a 3D vector B
     * @param vectorA the A vector
     * @param vectorB the B vector
     * @return the A-B vector
     */
    public static Vector3D subtract(Vector3D vectorA, Vector3D vectorB) {
        return new Vector3D(vectorA.getX() - vectorB.getX(), vectorA.getY() - vectorB.getY(), vectorA.getZ() - vectorB.getZ());
    }

    /**
     * calculates the scalar multiplication of a 3D vector A and a scalar k
     * @param vector the A vector
     * @param scalar the k scalar
     * @return the A*k vector
     */
    public static Vector3D scalarMultiplication(Vector3D vector, double scalar) {
        return new Vector3D(vector.getX() * scalar, vector.getY() * scalar, vector.getZ() * scalar);
    }

    /**
     * calculates the cross product of a 3D vector A and a 3D vector B
     * @param vectorA the A vector
     * @param vectorB the B vector
     * @return the cross product of A and B
     */
    public static Vector3D crossProduct(Vector3D vectorA, Vector3D vectorB) {
        return new Vector3D((vectorA.getY() * vectorB.getZ()) - (vectorA.getZ() * vectorB.getY()),
                (vectorA.getZ() * vectorB.getX()) - (vectorA.getX() * vectorB.getZ()),
                (vectorA.getX() * vectorB.getY()) - (vectorA.getY() * vectorB.getX()));
    }

    /**
     * calculates the dot product of a 3D vector A and a 3D vector B
     * @param vectorA the A vector
     * @param vectorB the B vector
     * @return the dot product of A and B
     */
    public static double dotProduct(Vector3D vectorA, Vector3D vectorB) {
        return (vectorA.getX() * vectorB.getX()) + (vectorA.getY() * vectorB.getY()) + (vectorA.getZ() * vectorB.getZ());
    }

    /**
     * changes the default String conversion of a 3D vector and formats it to "(x, y, z)"
     * @return the String of a vector formatted
     */
    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ", " + getZ() + ")";
    }

    /**
     * returns a new 3D vector with the values of the ZERO vector
     * @return ZERO vector (0, 0, 0)
     */
    public static Vector3D ZERO() {
        return clone(Vector3D.ZERO);
    }

    /**
     * class getter
     * @return the x coordinate
     */
    public double getX() {
        return this.x;
    }
    /**
     * class setter
     * @param x the x coordinate to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * class getter
     * @return the y coordinate
     */
    public double getY() {
        return this.y;
    }
    /**
     * class setter
     * @param y the y coordinate to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * class getter
     * @return the z coordinate
     */
    public double getZ() {
        return this.z;
    }
    /**
     * class setter
     * @param z the y coordinate to set
     */
    public void setZ(double z) {
        this.z = z;
    }

}
