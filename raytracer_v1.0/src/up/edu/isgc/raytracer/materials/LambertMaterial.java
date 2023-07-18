/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.materials;


import java.awt.Color;

/**
 * Stores information for a material using lambertian surfaces such as the Color
 *
 * @author Homar Cano
 */
public class LambertMaterial extends Material {

    /**
     * class constructor
     * @param color the Color of the material
     */
    public LambertMaterial(Color color) {
        super(color);
    }

}
