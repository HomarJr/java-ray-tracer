/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.objects;


import up.edu.isgc.raytracer.materials.Material;
import up.edu.isgc.raytracer.objects.utility.Vector3D;

import java.awt.Color;

/**
 * Stores information for a general 3D object such as position and material
 *
 * @author Homar Cano, Jafet Rodríguez
 */
public abstract class Object3D {
    private Vector3D position;
    private Material material;

    /**
     * class constructor
     * @param position the position of the object
     * @param material the Material of the object
     */
    public Object3D(Vector3D position, Material material) {
        setMaterial(material);
        setPosition(position);
    }

    /**
     * class getter
     * @return the object position
     */
    public Vector3D getPosition() {
        return this.position;
    }
    /**
     * class setter
     * @param position the object position to set
     */
    public void setPosition(Vector3D position) {
        this.position = position;
    }

    /**
     * class getter
     * @return the object color
     */
    public Color getColor() {
        return getMaterial().getColor();
    }
    /**
     * class setter
     * @param color the object color to set
     */
    public void setColor(Color color) {
        getMaterial().setColor(color);
    }

    /**
     * class getter
     * @return the object Material
     */
    public Material getMaterial() {
        return this.material;
    }
    /**
     * class setter
     * @param material the object Material to set
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

}
