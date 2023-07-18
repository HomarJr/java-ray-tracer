/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.materials;


import java.awt.Color;

/**
 * Stores information for a refractive material such as the Color, shininess, refraction index and coefficients for ambient, diffuse, specular and refraction
 *
 * @author Homar Cano
 */
public class RefractiveMaterial extends BlinnPhongMaterial {
    private double refractionCoefficient;
    private double refractionMultiplier;

    /**
     * class constructor for a transparent material
     * @param refractionCoefficient the refraction index
     */
    public RefractiveMaterial(double refractionCoefficient) {
        super(Color.BLACK, 0, 0, 0, 0);
        if (refractionCoefficient < 1 && refractionCoefficient != 0) setRefractionCoefficient(1);
        else setRefractionCoefficient(refractionCoefficient);
        setRefractionMultiplier(1);
    }

    /**
     * class constructor
     * @param color the Color of the material
     * @param ambientCoefficient the coefficient for the ambient component
     * @param diffuseCoefficient the coefficient for the diffuse component
     * @param specularCoefficient the coefficient for the specular component
     * @param shininess the shininess value
     * @param refractionCoefficient the refraction index
     * @param refractionMultiplier the coefficient for the refraction
     */
    public RefractiveMaterial(Color color, double ambientCoefficient, double diffuseCoefficient, double specularCoefficient, double shininess, double refractionCoefficient, double refractionMultiplier) {
        super(color, ambientCoefficient, diffuseCoefficient, specularCoefficient, shininess);
        if (refractionCoefficient < 1 && refractionCoefficient != 0) setRefractionCoefficient(1);
        else setRefractionCoefficient(refractionCoefficient);
        setRefractionMultiplier(refractionMultiplier);
    }

    /**
     * class getter
     * @return the refraction index
     */
    public double getRefractionCoefficient() {
        return refractionCoefficient;
    }
    /**
     * class setter
     * @param refractionCoefficient the refraction index to set
     */
    public void setRefractionCoefficient(double refractionCoefficient) {
        this.refractionCoefficient = refractionCoefficient;
    }

    /**
     * class getter
     * @return the refraction coefficient
     */
    public double getRefractionMultiplier() {
        return refractionMultiplier;
    }
    /**
     * class setter
     * @param refractionMultiplier the refraction coefficient to set
     */
    public void setRefractionMultiplier(double refractionMultiplier) {
        this.refractionMultiplier = refractionMultiplier;
    }

}
