/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.tools;


import up.edu.isgc.raytracer.objects.Object3D;
import up.edu.isgc.raytracer.objects.utility.Vector3D;

/**
 * Stores the information of an intersection between a ray and an object
 * The information stored is distance, normal, position and the 3D object
 *
 * @author Homar Cano, Jafet Rodríguez
 */
public class Intersection {
    private double distance;
    private Vector3D normal;
    private Vector3D position;
    private Object3D object;

    /**
     * class constructor
     * @param distance the distance from the ray to the object intersected
     * @param position the position of the point of intersection
     * @param normal the normal vector of the intersection
     * @param object the object intersected
     */
    public Intersection(double distance, Vector3D position, Vector3D normal, Object3D object) {
        setDistance(distance);
        setPosition(position);
        setNormal(normal);
        setObject(object);
    }

    /**
     * class getter
     * @return the intersection distance
     */
    public double getDistance() {
        return this.distance;
    }
    /**
     * class setter
     * @param distance the intersection distance to set
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * class getter
     * @return the intersection normal
     */
    public Vector3D getNormal() {
        return this.normal;
    }
    /**
     * class setter
     * @param normal the intersection normal to set
     */
    public void setNormal(Vector3D normal) {
        this.normal = normal;
    }

    /**
     * class getter
     * @return the intersection position
     */
    public Vector3D getPosition() {
        return this.position;
    }
    /**
     * class setter
     * @param position the intersection position to set
     */
    public void setPosition(Vector3D position) {
        this.position = position;
    }

    /**
     * class getter
     * @return the object intersected
     */
    public Object3D getObject() {
        return this.object;
    }

    /**
     * class setter
     * @param object the object intersected to set
     */
    public void setObject(Object3D object) {
        this.object = object;
    }

}
