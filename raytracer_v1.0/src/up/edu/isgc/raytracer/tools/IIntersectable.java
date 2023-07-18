/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.tools;


import up.edu.isgc.raytracer.objects.utility.Ray;

/**
 * Interface that gives the ability to calculate the intersection between a 3D object and a ray
 *
 * @author Homar Cano, Jafet Rodríguez
 */
public interface IIntersectable {
    /**
     * method expected to calculate the intersection between an object and a ray
     *
     * @param ray the ray used to calculate the intersection
     * @return the Intersection object calculated
     */
    Intersection getObjectIntersection(Ray ray);
}
