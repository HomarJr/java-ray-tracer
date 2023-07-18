/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.objects;


import up.edu.isgc.raytracer.objects.utility.Ray;
import up.edu.isgc.raytracer.objects.utility.Vector3D;
import up.edu.isgc.raytracer.tools.IIntersectable;
import up.edu.isgc.raytracer.tools.Intersection;

/**
 * Stores the information for a triangle such as vertices and normals
 *
 * @author Homar Cano, Jafet Rodríguez
 */
public class Triangle extends Object3D implements IIntersectable {
    /**
     * private constant used as margin of error
     */
    private static final double EPSILON = 0.0000001d;

    private Vector3D[] vertices;
    private Vector3D[] normals;

    /**
     * class constructor
     * @param vertices the vertex array of the triangle (must be 3 vertices)
     * @param normals the normal array of the triangle (must be 3 normals)
     */
    public Triangle(Vector3D[] vertices, Vector3D[] normals) {
        super(null, null);
        if (vertices == null ||vertices.length != 3) setVertices(Vector3D.ZERO(), Vector3D.ZERO(), Vector3D.ZERO());
        else setVertices(vertices[0], vertices[1], vertices[2]);
        if (normals == null ||normals.length != 3) setNormals(null, null, null);
        else setNormals(normals[0], normals[1], normals[2]);
    }

    /**
     * function that calculates a ray-triangle intersection (only distance) with a given ray
     * @param ray the ray used to calculate the intersection
     * @return the intersection between the ray and the triangle or null if the intersection does not exists
     * @see <a href="https://cadxfem.org/inf/Fast%20MinimumStorage%20RayTriangle%20Intersection.pdf">Moller-Trumbore intersection algorithm</a>
     */
    @Override
    public Intersection getObjectIntersection(Ray ray) {
        Vector3D[] vertices = getVertices();
        Vector3D v2v0 = Vector3D.subtract(vertices[2], vertices[0]);
        Vector3D v1v0 = Vector3D.subtract(vertices[1], vertices[0]);
        Vector3D vectorP = Vector3D.crossProduct(ray.getDirection(), v1v0);
        double determinant = Vector3D.dotProduct(v2v0, vectorP);
        double invertedDeterminant = 1.0 / determinant;
        Vector3D vectorT = Vector3D.subtract(ray.getOrigin(), vertices[0]);
        double u = Vector3D.dotProduct(vectorT, vectorP) * invertedDeterminant;
        if(u < 0 || u > 1){
            return null;
        }

        Vector3D vectorQ = Vector3D.crossProduct(vectorT, v2v0);
        double v = Vector3D.dotProduct(ray.getDirection(), vectorQ) * invertedDeterminant;
        if(v < 0 || (u + v) > (1.0 + EPSILON)){
            return null;
        }

        double t = Vector3D.dotProduct(vectorQ, v1v0) * invertedDeterminant;
        return new Intersection(t, null, null, this);
    }

    /**
     * class getter
     * @return the normal of the face formed by the triangle
     */
    public Vector3D getNormal() {
        Vector3D normal = Vector3D.ZERO();

        Vector3D[] normals = this.normals;
        if (normals == null || (normals[0] == null && normals[1] == null && normals[2] == null)) {
            Vector3D[] vertices = getVertices();
            Vector3D v = Vector3D.subtract(vertices[1], vertices[0]);
            Vector3D w = Vector3D.subtract(vertices[2], vertices[0]);

            normal = Vector3D.scalarMultiplication(Vector3D.normalize(Vector3D.crossProduct(v, w)), -1.0);
        } else {
            for (Vector3D vector : normals) {
                normal.setX(normal.getX() + vector.getX());
                normal.setY(normal.getY() + vector.getY());
                normal.setZ(normal.getZ() + vector.getZ());
            }
            normal.setX(normal.getX() / normals.length);
            normal.setY(normal.getY() / normals.length);
            normal.setZ(normal.getZ() / normals.length);
        }

        return normal;
    }
    /**
     * class getter
     * @return the normals array of the triangle
     */
    public Vector3D[] getNormals() {
        if(normals == null || (normals[0] == null && normals[1] == null && normals[2] == null)){
            Vector3D normal = getNormal();
            setNormals(new Vector3D[]{normal, normal, normal});
        }
        return normals;
    }
    /**
     * class setter
     * @param normal1 the normal of vertex A in an ABC triangle to set
     * @param normal2 the normal of vertex B in an ABC triangle to set
     * @param normal3 the normal of vertex C in an ABC triangle to set
     */
    public void setNormals(Vector3D normal1, Vector3D normal2, Vector3D normal3) {
        setNormals(new Vector3D[]{normal1, normal2, normal3});
    }
    /**
     * class setter
     * @param normals the normals array of the triangle to set
     */
    public void setNormals(Vector3D[] normals) {
        if (normals.length == 3) this.normals = normals;
    }

    /**
     * class getter
     * @return the vertex array of the triangle
     */
    public Vector3D[] getVertices() {
        return vertices;
    }
    /**
     * class setter
     * @param vertex1 the vertex A in an ABC triangle to set
     * @param vertex2 the vertex B in an ABC triangle to set
     * @param vertex3 the vertex C in an ABC triangle to set
     */
    public void  setVertices(Vector3D vertex1, Vector3D vertex2, Vector3D vertex3) {
        setVertices(new Vector3D[]{vertex1, vertex2, vertex3});
    }
    /**
     * class setter
     * @param vertices the vertex array of the triangle to set
     */
    private void setVertices(Vector3D[] vertices) {
        this.vertices = vertices;
    }

}
