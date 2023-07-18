/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.lights;


import up.edu.isgc.raytracer.objects.Object3D;
import up.edu.isgc.raytracer.objects.utility.Vector3D;
import up.edu.isgc.raytracer.materials.LambertMaterial;
import up.edu.isgc.raytracer.tools.Intersection;

import java.awt.Color;

/**
 * Stores information for a generic light such as position, material and intensity
 * Has method to calculate the lambertian surface value (cosine of the angle)
 *
 * @author Homar Cano, Jafet Rodríguez
 */
public abstract class Light extends Object3D {
    private float intensity;

    /**
     * class constructor
     * @param position the position of the light
     * @param color the color of the light (used to create the material)
     * @param intensity the intensity of the light
     */
    public Light(Vector3D position, Color color, float intensity) {
        super(position, new LambertMaterial(color));
        setIntensity(intensity);
    }

    /**
     * calculates the value of the lambertian surface with the object normal and the light direction
     * @param intersection the intersection between the surface and the light
     * @return the lambertian surface value
     */
    public abstract double getLambertianSurfaceValue(Intersection intersection);

    /**
     * class getter
     * @return the intensity of the light
     */
    public float getIntensity() {
        return intensity;
    }
    /**
     * class setter
     * @param intensity the intensity of the light to set
     */
    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

}
