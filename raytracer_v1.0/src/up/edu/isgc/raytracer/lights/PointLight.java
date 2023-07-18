/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.lights;


import up.edu.isgc.raytracer.objects.utility.Vector3D;
import up.edu.isgc.raytracer.tools.Intersection;

import java.awt.Color;

/**
 * Stores information for a point light such as position, material and intensity
 * Has method to calculate the lambertian surface value (cosine of the angle)
 *
 * @author Homar Cano, Jafet Rodríguez
 */
public class PointLight extends Light {

    /**
     * class constructor
     * @param position the position of the light
     * @param color the color of the light (used to create the material)
     * @param intensity the intensity of the light
     */
    public PointLight(Vector3D position, Color color, float intensity) {
        super(position, color, intensity);
    }

    /**
     * calculates the value of the lambertian surface with the object normal and the light direction
     * @param intersection the intersection between the surface and the light
     * @return the lambertian surface value
     */
    @Override
    public double getLambertianSurfaceValue(Intersection intersection) {
        return Math.max(Vector3D.dotProduct(intersection.getNormal(), Vector3D.normalize(Vector3D.subtract(getPosition(), intersection.getPosition()))), 0.0);
    }

}
