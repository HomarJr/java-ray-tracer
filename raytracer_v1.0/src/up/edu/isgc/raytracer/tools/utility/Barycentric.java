/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.tools.utility;


import up.edu.isgc.raytracer.objects.Triangle;
import up.edu.isgc.raytracer.objects.utility.Vector3D;

/**
 * Utility class used to calculate barycentric coordinates
 *
 * @author Jafet Rodríguez
 */
public abstract class Barycentric {
    /**
     * calculates the barycentric values of a given point in a given triangle
     * @param point the point in the triangle
     * @param triangle the triangle used for the barycentric coordinates
     * @return an array with the values of the barycentric coordinates
     */
    public static double[] CalculateBarycentricCoordinates(Vector3D point, Triangle triangle) {
        double u, v, w;
        Vector3D[] vertices = triangle.getVertices();
        Vector3D a = vertices[0];
        Vector3D b = vertices[1];
        Vector3D c = vertices[2];

        Vector3D v0 = Vector3D.subtract(b, a);
        Vector3D v1 = Vector3D.subtract(c, a);
        Vector3D v2 = Vector3D.subtract(point, a);
        double d00 = Vector3D.dotProduct(v0, v0);
        double d01 = Vector3D.dotProduct(v0, v1);
        double d11 = Vector3D.dotProduct(v1, v1);
        double d20 = Vector3D.dotProduct(v2, v0);
        double d21 = Vector3D.dotProduct(v2, v1);
        double denominator = d00 * d11 - d01 * d01;
        v = (d11 * d20 - d01 * d21) / denominator;
        w = (d00 * d21 - d01 * d20) / denominator;
        u = 1.0 - v - w;

        return new double[]{u, v, w};
    }

}
