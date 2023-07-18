/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.materials;


import java.awt.Color;

/**
 * Stores information for a material using the Blinn-Phong model such as the Color, shininess and coefficients for ambient, diffuse and specular
 *
 * @author Homar Cano
 */
public class BlinnPhongMaterial extends Material {
    private double ambientCoefficient;
    private double diffuseCoefficient;
    private double specularCoefficient;
    private double shininess;

    /**
     * class constructor
     * @param color the Color of the material
     * @param ambientCoefficient the coefficient for the ambient component
     * @param diffuseCoefficient the coefficient for the diffuse component
     * @param specularCoefficient the coefficient for the specular component
     * @param shininess the shininess value
     */
    public BlinnPhongMaterial(Color color, double ambientCoefficient, double diffuseCoefficient, double specularCoefficient, double shininess) {
        super(color);
        setAmbientCoefficient(ambientCoefficient);
        setDiffuseCoefficient(diffuseCoefficient);
        setSpecularCoefficient(specularCoefficient);
        setShininess(shininess);
    }

    /**
     * class getter
     * @return the ambient coefficient
     */
    public double getAmbientCoefficient() {
        return this.ambientCoefficient;
    }
    /**
     * class setter
     * @param ambientCoefficient the ambient coefficient to set
     */
    public void setAmbientCoefficient(double ambientCoefficient) {
        this.ambientCoefficient = ambientCoefficient;
    }

    /**
     * class getter
     * @return the diffuse coefficient
     */
    public double getDiffuseCoefficient() {
        return this.diffuseCoefficient;
    }
    /**
     * class setter
     * @param diffuseCoefficient the diffuse coefficient to set
     */
    public void setDiffuseCoefficient(double diffuseCoefficient) {
        this.diffuseCoefficient = diffuseCoefficient;
    }

    /**
     * class getter
     * @return the specular coefficient
     */
    public double getSpecularCoefficient() {
        return this.specularCoefficient;
    }
    /**
     * class setter
     * @param specularCoefficient the specular coefficient to set
     */
    public void setSpecularCoefficient(double specularCoefficient) {
        this.specularCoefficient = specularCoefficient;
    }

    /**
     * class getter
     * @return the shininess value
     */
    public double getShininess() {
        return this.shininess;
    }
    /**
     * class setter
     * @param shininess the shininess value to set
     */
    public void setShininess(double shininess) {
        this.shininess = shininess;
    }

}
