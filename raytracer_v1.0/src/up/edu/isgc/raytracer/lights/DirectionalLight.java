/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.lights;


import up.edu.isgc.raytracer.objects.utility.Vector3D;
import up.edu.isgc.raytracer.tools.Intersection;

import java.awt.Color;

/**
 * Stores information for a directional light such as direction, position, material and intensity
 * Has method to calculate the lambertian surface value (cosine of the angle)
 *
 * @author Homar Cano, Jafet Rodríguez
 */
public class DirectionalLight extends Light {
    private Vector3D direction;

    /**
     * class constructor
     * @param position the position of the light
     * @param direction the direction of the light
     * @param color the color of the light (used to create the material)
     * @param intensity the intensity of the light
     */
    public DirectionalLight(Vector3D position, Vector3D direction, Color color, float intensity) {
        super(position, color, intensity);
        setDirection(direction);
    }

    /**
     * calculates the value of the lambertian surface with the object normal and the light direction
     * @param intersection the intersection between the surface and the light
     * @return the lambertian surface value
     */
    @Override
    public double getLambertianSurfaceValue(Intersection intersection) {
        return Math.max(Vector3D.dotProduct(intersection.getNormal(), Vector3D.scalarMultiplication(getDirection(), -1.0d)), 0.0d);
    }

    /**
     * class getter
     * @return the direction of the light
     */
    public Vector3D getDirection() {
        return direction;
    }
    /**
     * class setter
     * @param direction the direction of the light to set
     */
    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }

}
