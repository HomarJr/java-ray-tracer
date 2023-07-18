/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.materials;


import java.awt.Color;

/**
 * Stores information for a reflective material such as the Color, shininess and coefficients for ambient, diffuse, specular and diffuse
 *
 * @author Homar Cano
 */
public class ReflectiveMaterial extends BlinnPhongMaterial {
    private double reflectionCoefficient;

    /**
     * class constructor
     * @param color the Color of the material
     * @param ambientCoefficient the coefficient for the ambient component
     * @param diffuseCoefficient the coefficient for the diffuse component
     * @param specularCoefficient the coefficient for the specular component
     * @param shininess the shininess value
     * @param reflectionCoefficient the coefficient for the reflection
     */
    public ReflectiveMaterial(Color color, double ambientCoefficient, double diffuseCoefficient, double specularCoefficient, double shininess, double reflectionCoefficient) {
        super(color, ambientCoefficient, diffuseCoefficient, specularCoefficient, shininess);
        setReflectionCoefficient(reflectionCoefficient);
    }

    /**
     * class getter
     * @return the reflection coefficient
     */
    public double getReflectionCoefficient() {
        return reflectionCoefficient;
    }
    /**
     * class setter
     * @param reflectionCoefficient the reflection coefficient to set
     */
    public void setReflectionCoefficient(double reflectionCoefficient) {
        this.reflectionCoefficient = reflectionCoefficient;
    }

}
