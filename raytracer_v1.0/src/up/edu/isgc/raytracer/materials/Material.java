/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.materials;


import java.awt.Color;

/**
 * Stores information for a generic material such as the Color
 *
 * @author Homar Cano
 */
public abstract class Material {
    private Color color;

    /**
     * class constructor
     * @param color the Color of the material
     */
    public Material(Color color) {
        setColor(color);
    }

    /**
     * class getter
     * @return the Color of the material
     */
    public Color getColor() {
        return this.color;
    }
    /**
     * class setter
     * @param color the Color of the material to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

}
