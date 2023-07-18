/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.objects;


import up.edu.isgc.raytracer.materials.Material;
import up.edu.isgc.raytracer.objects.utility.Ray;
import up.edu.isgc.raytracer.objects.utility.Vector3D;
import up.edu.isgc.raytracer.tools.IIntersectable;
import up.edu.isgc.raytracer.tools.Intersection;

/**
 * Stores the information for a sphere such as radius, position and material
 *
 * @author Homar Cano, Jafet Rodríguez
 */
public class Sphere extends Object3D implements IIntersectable {
    private double radius;

    /**
     * class constructor
     * @param position the position of the sphere
     * @param radius the radius of the sphere
     * @param material the material of the sphere
     */
    public Sphere(Vector3D position, double radius, Material material) {
        super(position, material);
        setRadius(radius);
    }

    /**
     * function that calculates a ray-sphere intersection with a given ray
     * @param ray the ray used to calculate the intersection
     * @return the intersection between the ray and the sphere or null if the intersection does not exists
     * @see <a href="https://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-sphere-intersection">ray-sphere intersection</a>
     */
    @Override
    public Intersection getObjectIntersection(Ray ray) {
        double distance;
        Vector3D normal;
        Vector3D position;

        Vector3D directionSphereRay = Vector3D.subtract(ray.getOrigin(), getPosition());
        double firstP = Vector3D.dotProduct(ray.getDirection(), directionSphereRay);
        double secondP = Math.pow(Vector3D.magnitude(directionSphereRay), 2);
        double intersection = Math.pow(firstP, 2) - secondP + Math.pow(getRadius(), 2);

        if (intersection >= 0) {
            double sqrtIntersection = Math.sqrt(intersection);

            double part1 = -firstP + sqrtIntersection;
            double part2 = -firstP - sqrtIntersection;

            distance = Math.min(part1, part2);
            position = Vector3D.add(ray.getOrigin(), Vector3D.scalarMultiplication(ray.getDirection(), distance));
            normal = Vector3D.normalize(Vector3D.subtract(position, getPosition()));
        } else {
            return null;
        }

        return new Intersection(distance, position, normal, this);
    }

    /**
     * class getter
     * @return the sphere radius
     */
    public double getRadius() {
        return this.radius;
    }
    /**
     * class setter
     * @param radius the sphere radius to set
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

}
