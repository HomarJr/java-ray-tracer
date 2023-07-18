/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.engine;


import up.edu.isgc.raytracer.lights.Light;
import up.edu.isgc.raytracer.objects.Object3D;
import up.edu.isgc.raytracer.objects.utility.Camera;

import java.util.ArrayList;

/**
 * Stores information for a scene such as the objects, lights and camera in it
 * Also has information like name and extension used when the scene is render and saved into a file
 *
 * @author Homar Cano, Jafet Rodríguez
 */
public class Scene {
    private ArrayList<Object3D> objects;
    private ArrayList<Light> lights;
    private Camera camera;
    private String extension;
    private String name;

    /**
     * class constructor
     * @param name name of the scene
     * @param fileExtension file extension of the scene
     */
    public Scene(String name, String fileExtension) {
        setObjects(new ArrayList<>());
        setLights(new ArrayList<>());
        setExtension(fileExtension);
        setName(name);
    }

    /**
     * class getter
     * @return the list of 3D objects in the scene
     */
    public ArrayList<Object3D> getObjects() {
        return this.objects;
    }
    /**
     * class setter
     * @param objects the list of 3D objects in the scene to set
     */
    public void setObjects(ArrayList<Object3D> objects) {
        this.objects = objects;
    }
    /**
     * adds an object to the scene
     * @param object Object3D to add
     */
    public void addObject(Object3D object){
        getObjects().add(object);
    }

    /**
     * class getter
     * @return the main camera of the scene
     */
    public Camera getCamera() {
        return this.camera;
    }
    /**
     * class setter
     * @param camera the camera of the scene to set
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * class getter
     * @return the list of lights in the scene
     */
    public ArrayList<Light> getLights() {
        return lights;
    }
    /**
     * class setter
     * @param lights the list of lights in the scene to set
     */
    public void setLights(ArrayList<Light> lights) {
        this.lights = lights;
    }
    /**
     * adds a light to the scene
     * @param light Light to add
     */
    public void addLight(Light light) {
        getLights().add(light);
    }

    /**
     * class getter
     * @return the name of the scene
     */
    public String getName() {
        return this.name;
    }
    /**
     * class setter
     * @param name the name of the scene to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * class getter
     * @return the file extension of the scene
     */
    public String getExtension() {
        return this.extension;
    }
    /**
     * class setter
     * @param extension the file extension of the scene to set
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

}
