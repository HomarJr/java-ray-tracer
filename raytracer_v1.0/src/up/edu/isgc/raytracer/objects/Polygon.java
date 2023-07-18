/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.objects;


import up.edu.isgc.raytracer.materials.Material;
import up.edu.isgc.raytracer.objects.utility.Ray;
import up.edu.isgc.raytracer.objects.utility.Vector3D;
import up.edu.isgc.raytracer.tools.Intersection;
import up.edu.isgc.raytracer.tools.utility.Barycentric;
import up.edu.isgc.raytracer.tools.IIntersectable;
import up.edu.isgc.raytracer.tools.utility.ObjFileReader;

import java.util.Arrays;
import java.util.Objects;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Stores the information for a polygon such as a triangle mesh, position and material
 *
 * @author Homar Cano, Jafet Rodríguez
 */
public class Polygon extends Object3D implements IIntersectable {
    private List<Triangle> triangleMesh;

    /**
     * class constructor
     * @param position the position of the polygon
     * @param objFile the .obj file of the polygon
     * @param material the material of the polygon
     */
    public Polygon(Vector3D position, String objFile, Material material) {
        super(position, material);
        setTriangleMesh(Objects.requireNonNull(ObjFileReader.readObjFile(objFile)));
    }

    /**
     * calculates a ray-object intersection with a given ray
     * @param ray the ray used to calculate the intersection
     * @return the intersection between the ray and the object or null if the intersection does not exists
     */
    @Override
    public Intersection getObjectIntersection(Ray ray) {
        double distance = -1;
        Vector3D normal = Vector3D.ZERO();
        Vector3D position = Vector3D.ZERO();
        Object3D obj = null;
        for (Triangle triangle : getTriangleMesh()) {
            Intersection intersection = triangle.getObjectIntersection(ray);

            if (intersection != null && intersection.getDistance() > 0 && (intersection.getDistance() < distance || distance < 0)) {
                normal = Vector3D.ZERO();
                distance = intersection.getDistance();
                position = Vector3D.add(ray.getOrigin(), Vector3D.scalarMultiplication(ray.getDirection(), distance));
                double[] uVw = Barycentric.CalculateBarycentricCoordinates(position, triangle);
                obj = intersection.getObject();
                Vector3D[] normals = triangle.getNormals();
                for(int i = 0; i < uVw.length; i++) {
                    normal = Vector3D.add(normal, Vector3D.scalarMultiplication(normals[i], uVw[i]));
                }
            }
        }

        if (distance == -1) {
            return null;
        }

        return new Intersection(distance, position, normal, this);
    }

    /**
     * class getter
     * @return the triangle mesh of the polygon
     */
    public List<Triangle> getTriangleMesh() {
        return triangleMesh;
    }

    /**
     * class setter
     * @param triangleMesh the triangle mesh of the polygon to set in array form
     */
    public void setTriangleMesh(Triangle[] triangleMesh) {
        Set<Vector3D> uniqueVertices = new HashSet<>();
        Vector3D position = getPosition();

        for(Triangle triangle : triangleMesh){
            triangle.setMaterial(this.getMaterial());
            triangle.setPosition(this.getPosition());
            uniqueVertices.addAll(Arrays.asList(triangle.getVertices()));
        }

        for(Vector3D vertex : uniqueVertices){
            vertex.setX(vertex.getX() + position.getX());
            vertex.setY(vertex.getY() + position.getY());
            vertex.setZ(vertex.getZ() + position.getZ());
        }

        this.triangleMesh = Arrays.asList(triangleMesh);
    }

}
