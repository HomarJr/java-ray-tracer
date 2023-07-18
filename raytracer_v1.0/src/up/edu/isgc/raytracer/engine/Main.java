/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.engine;


import up.edu.isgc.raytracer.lights.*;
import up.edu.isgc.raytracer.materials.*;
import up.edu.isgc.raytracer.objects.*;
import up.edu.isgc.raytracer.objects.utility.*;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Java Raytracer implementation built from scratch
 * Final project for the 2020-2 Multimedia and Computer Graphics class
 *
 * The program will output a progress log in the console through its execution. This program works by raytracing a
 * 3D scene with a camera and multiple 3D objects; each one with a particular material and position.
 * The generated images will be saved in the images/ folder
 * If the scene(s) use(s) a 3D model from an .obj file, it must be in the objects/ folder
 * <br>
 * The current version (1.0) supports:
 * <ul>
 *  <li>Multiple camera and render parameters</li>
 *  <li>Importing a 3D object with an .obj file</li>
 *  <li>Point and directional lights</li>
 *  <li>Shadows and light fall off</li>
 *  <li>Implementation of the Blinn-Phong model</li>
 *  <li>Refraction and Reflection</li>
 *  <li>Different types of materials</li>
 *  <li>Multi threading for better performance</li>
 *  <li>Progress and status updates are shown in console</li>
 * </ul>
 *
 * @author Homar Cano, Jafet Rodríguez
 * @version 1.0
 */
public class Main {

    // console log settings
    public static final String TIME_ZONE = "CDT";
    public static final String IMAGE_EXTENSION = "png";

    // raytracer settings
    public static final int N_THREADS = 12;
    public static final int RENDER_TIMEOUT = 21600000;
    public static final float LIGHT_FALLOFF_EXPONENT = 2f;
    public static final boolean USE_CLIPPING_PLANES = true;

    // camera settings
    public static final Vector3D CAMERA_POSITION = new Vector3D(0, 0, -8);
    public static final double FAR_CLIPPING_PLANE = 50;
    public static final double NEAR_CLIPPING_PLANE = 1;
    public static final double FOV = 160;
    public static final int IMAGE_SIZE = 600;

    /**
     * Driver function for the program
     * @param args unused
     */
    public static void main(String[] args) {
        Date startDate = new Date();
        System.out.println(startDate + " program started");
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH' hours, 'mm' minutes and 'ss' seconds'");
        dateFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));

        Raytracer raytracer = new Raytracer(N_THREADS, RENDER_TIMEOUT, LIGHT_FALLOFF_EXPONENT, USE_CLIPPING_PLANES);
        Scene scene = new Scene("Sample Scene DOMENIC", IMAGE_EXTENSION);

        //camera and lights
        scene.setCamera(new Camera(CAMERA_POSITION, FOV, FOV, IMAGE_SIZE, IMAGE_SIZE, FAR_CLIPPING_PLANE, NEAR_CLIPPING_PLANE));
        scene.addLight(new PointLight(new Vector3D(0f,0.5f,-0.1f), Color.WHITE, 4.5f));

        //materials
        Material lambertGray = new LambertMaterial(Color.GRAY);
        Material shinyRed = new BlinnPhongMaterial(Color.PINK, 0.05d, 0.75d, 0.2d, 100d);

        //objects
        scene.addObject(new Polygon(new Vector3D(0f,-2f,2f), "small_teapot", shinyRed));
        scene.addObject(new Polygon(new Vector3D(0f,-2f,5f), "floor", lambertGray));
        raytracer.raytraceScene(scene);

        // shadows and blinn-phong
        raytracer.setShadowBias(0.075d);
        //raytracer.raytraceScene(createSampleScene01());

        // reflection
        raytracer.setShadowBias(0.0000001d);
        //raytracer.raytraceScene(createSampleScene02());

        // more reflection
        raytracer.setShadowBias(0.0005d);
        //raytracer.raytraceScene(createSampleScene03());

        // refraction
        raytracer.setShadowBias(0.075d);
        //raytracer.raytraceScene(createSampleScene04());

        // more refraction
        raytracer.setShadowBias(0.0003d);
        //raytracer.raytraceScene(createSampleScene05());

        // table set
        raytracer.setShadowBias(0.0000001d);
        //raytracer.raytraceScene(createScene01());

        // multiple objects
        //raytracer.raytraceScene(createScene02());

        // bunny and other objects
        //raytracer.raytraceScene(createScene03());

        Date endDate = new Date();
        String totalTime = dateFormat.format(new Date(endDate.getTime() - startDate.getTime()));
        System.out.println(endDate + " the program finished in a total time of " + totalTime);
    }

    public static Scene createSampleScene01() {
        Scene scene = new Scene("Sample Scene 01", IMAGE_EXTENSION);

        //camera and lights
        scene.setCamera(new Camera(CAMERA_POSITION, FOV, FOV, IMAGE_SIZE, IMAGE_SIZE, FAR_CLIPPING_PLANE, NEAR_CLIPPING_PLANE));
        scene.addLight(new PointLight(new Vector3D(0f,1f,-0.1f), Color.WHITE, 4.5f));

        //materials
        Material lambertGray = new LambertMaterial(Color.GRAY);
        Material shinyRed = new BlinnPhongMaterial(Color.RED, 0.05d, 0.45d, 0.2d, 100d);

        //objects
        scene.addObject(new Polygon(new Vector3D(0f,-2f,2f), "small_teapot", shinyRed));
        scene.addObject(new Polygon(new Vector3D(0f,-2f,5f), "floor", lambertGray));

        return scene;
    }
    public static Scene createSampleScene02() {
        Scene scene = new Scene("Sample Scene 02", IMAGE_EXTENSION);

        //camera and lights
        scene.setCamera(new Camera(CAMERA_POSITION, FOV, FOV, IMAGE_SIZE, IMAGE_SIZE, FAR_CLIPPING_PLANE, NEAR_CLIPPING_PLANE));
        scene.addLight(new PointLight(new Vector3D(0f,1f,-2f), Color.WHITE, 8f));

        //materials
        Material reflectiveGray = new ReflectiveMaterial(Color.GRAY, 0d, .8d, 0d, 0, 0.25d);
        Material shinyRed = new BlinnPhongMaterial(Color.RED, 0.1d, 0.55d, 0.2d, 250d);
        Material shinyGreen = new BlinnPhongMaterial(Color.GREEN, 0.1d, 0.55d, 0.2d, 250d);

        //objects
        scene.addObject(new Sphere(new Vector3D(-2.5f,-1.5f,2.25f), 0.5d, shinyRed));
        scene.addObject(new Sphere(new Vector3D(2.5f,-1.5f,2.25f), 0.5d, shinyGreen));
        scene.addObject(new Polygon(new Vector3D(0f,-2f,5f), "floor", reflectiveGray));

        return scene;
    }
    public static Scene createSampleScene03() {
        Scene scene = new Scene("Sample Scene 03", IMAGE_EXTENSION);

        //camera and lights
        scene.setCamera(new Camera(CAMERA_POSITION, FOV, FOV, IMAGE_SIZE, IMAGE_SIZE, FAR_CLIPPING_PLANE, NEAR_CLIPPING_PLANE));
        scene.addLight(new PointLight(new Vector3D(0f,0.7f,-1f), Color.WHITE, 8f));

        //materials
        Material lambertGray = new LambertMaterial(Color.GRAY);
        Material reflectiveBlue = new ReflectiveMaterial(Color.BLUE, 0.05d, 0.25d, 0.75d, 600d, 0.15d);
        Material shinyRed = new BlinnPhongMaterial(Color.RED, 0.1d, 0.55d, 0.2d, 250d);

        //objects
        scene.addObject(new Sphere(new Vector3D(-2f,-1.5f,2.25f), 0.5d, reflectiveBlue));
        scene.addObject(new Sphere(new Vector3D(2f,-1.5f,2.25f), 0.5d, reflectiveBlue));
        scene.addObject(new Polygon(new Vector3D(0f,-1f,3.5f), "small_teapot", shinyRed));
        scene.addObject(new Polygon(new Vector3D(0f,-2f,5f), "floor", lambertGray));

        return scene;
    }
    public static Scene createSampleScene04() {
        Scene scene = new Scene("Sample Scene 04", IMAGE_EXTENSION);

        //camera and lights
        scene.setCamera(new Camera(CAMERA_POSITION, FOV, FOV, IMAGE_SIZE, IMAGE_SIZE, FAR_CLIPPING_PLANE, NEAR_CLIPPING_PLANE));
        scene.addLight(new DirectionalLight(Vector3D.ZERO(), new Vector3D(0f,-0.7f,1f), Color.WHITE, 0.8f));

        //materials
        Material shinyRed = new BlinnPhongMaterial(Color.RED, 0.05d, 0.45d, 0.2d, 100d);
        Material refractive = new RefractiveMaterial(Color.CYAN, 0.05d, 0.45d, 0.2d, 100d, 1.4d, 1.5d);

        //objects
        scene.addObject(new Polygon(new Vector3D(0f,-2f,5f), "small_teapot", shinyRed));
        scene.addObject(new Sphere(new Vector3D(0.25f,-1f,2f), 0.75, refractive));

        return scene;
    }
    public static Scene createSampleScene05() {
        Scene scene = new Scene("Sample Scene 05", IMAGE_EXTENSION);

        //camera and lights
        scene.setCamera(new Camera(CAMERA_POSITION, FOV, FOV, IMAGE_SIZE, IMAGE_SIZE, FAR_CLIPPING_PLANE, NEAR_CLIPPING_PLANE));
        scene.addLight(new PointLight(new Vector3D(0.15f,1f,-4.5f), Color.WHITE, 2.5f));

        //materials
        Material shinyRed = new BlinnPhongMaterial(Color.RED, 0.1d, 0.55d, 0.2d, 250d);
        Material refractive1 = new RefractiveMaterial(Color.CYAN, 0.05d, 0.35d, 0.2d, 10d, 1.3d, 0.85d);
        Material refractive2 = new RefractiveMaterial(Color.CYAN, 0.05d, 0.35d, 0.2d, 10d, 1.1d, 0.75d);
        Material refractive3 = new RefractiveMaterial(Color.CYAN, 0.05d, 0.35d, 0.2d, 10d, 1.7d, 0.95d);
        Material refractive4 = new RefractiveMaterial(Color.CYAN, 0.05d, 0.35d, 0.2d, 10d, 1.35d, 0.55d);
        Material refractive5 = new RefractiveMaterial(Color.CYAN, 0.05d, 0.35d, 0.2d, 10d, 1.4d, 0.45d);
        Material refractive6 = new RefractiveMaterial(Color.CYAN, 0.05d, 0.35d, 0.2d, 10d, 1.9d, 1.05d);

        //objects
        scene.addObject(new Sphere(new Vector3D(0.25f,-1f,-1f), 0.75d, refractive1));
        scene.addObject(new Sphere(new Vector3D(0.25f,1f,-1f), 0.75d, refractive2));
        scene.addObject(new Sphere(new Vector3D(2.25f,-1f,-1f), 0.75d, refractive3));
        scene.addObject(new Sphere(new Vector3D(-1.75f,-1f,-1f), 0.75d, refractive4));
        scene.addObject(new Sphere(new Vector3D(-1.75f,1f,-1f), 0.75d, refractive5));
        scene.addObject(new Sphere(new Vector3D(2.25f,1f,-1f), 0.75d, refractive6));
        scene.addObject(new Polygon(Vector3D.ZERO(), "boxes", shinyRed));


        return scene;
    }

    public static Scene createScene01() {
        Scene scene = new Scene("Scene 01", IMAGE_EXTENSION);

        //camera and lights
        scene.setCamera(new Camera(CAMERA_POSITION, FOV, FOV, IMAGE_SIZE, IMAGE_SIZE, FAR_CLIPPING_PLANE, NEAR_CLIPPING_PLANE));
        scene.addLight(new PointLight(new Vector3D(0.15f,1f,-4.5f), Color.WHITE, 6f));

        //materials
        Material metal = new ReflectiveMaterial(new Color(132, 127, 126, 198), 0.05d, 0.85d, 0.65d, 500d, 0.15d);
        Material plasticBlue = new BlinnPhongMaterial(new Color(76, 89,140), 0.05d, 0.95d, 0.15d, 60d);
        Material glass =  new RefractiveMaterial(new Color(224, 238, 230), 0.05d, 0.35d, 0.1d, 10d, 1.1d, 0.9d);
        Material wine = new ReflectiveMaterial(new Color(203, 29, 44, 245), 0.05d, 0.75d, 0.65d, 300d, 0.95d);

        //objects
        Vector3D setPosition = new Vector3D(0f,-2.75f,-3f);
        scene.addObject(new Polygon(setPosition, "set01/table", plasticBlue));
        scene.addObject(new Polygon(setPosition, "set01/cutlery", metal));
        scene.addObject(new Polygon(setPosition, "set01/glass", glass));
        scene.addObject(new Polygon(setPosition, "set01/liquid", wine));

        return scene;
    }
    public static Scene createScene02() {
        Scene scene = new Scene("Scene 02", IMAGE_EXTENSION);

        //camera and lights
        scene.setCamera(new Camera(CAMERA_POSITION, FOV, FOV, IMAGE_SIZE, IMAGE_SIZE, FAR_CLIPPING_PLANE, NEAR_CLIPPING_PLANE));
        scene.addLight(new PointLight(new Vector3D(-1.15f,2.75f,-2.3f), Color.WHITE, 2.6f));
        scene.addLight(new PointLight(new Vector3D(1.45f,2f,-1.3f), Color.WHITE, 2f));
        scene.addLight(new PointLight(new Vector3D(0.65f,2.25f,0.15f), Color.WHITE, 1.7f));

        //materials
        Material reflectiveWhite = new ReflectiveMaterial(Color.WHITE, 0d, .8d, 0d, 0, 0.25d);
        Material reflectiveBlue = new ReflectiveMaterial(Color.BLUE, 0.05d, 0.25d, 0.75d, 600d, 0.15d);
        Material reflective1 = new ReflectiveMaterial(new Color(193, 49, 199), 0.05d, 0.15d, 0.65d, 600d, 0.15d);
        Material reflective2 = new ReflectiveMaterial(new Color(47, 199, 171), 0.05d, 0.2d, 0.75d, 700d, 0.15d);
        Material reflective3 = new ReflectiveMaterial(new Color(158, 199, 174), 0.05d, 0.25d, 0.35d, 200d, 0.55d);

        Material shinyGreen = new BlinnPhongMaterial(Color.GREEN, 0.1d, 0.4, 0.2d, 500);
        Material shinyRed = new BlinnPhongMaterial(Color.RED, 0.1d, 0.55d, 0.3, 1450);
        Material shiny1 = new BlinnPhongMaterial(new Color(199, 195, 36), 0.1d, 0.6, 0.1, 600);
        Material shiny2 = new BlinnPhongMaterial(new Color(255, 120, 27), 0.1d, 0.35d, 0.15, 380);
        Material shiny3 = new BlinnPhongMaterial(new Color(255, 166, 137), 0.1d, 0.45, 0.2, 440);
        Material shiny4 = new BlinnPhongMaterial(new Color(89, 255, 98), 0.1d, 0.2, 0.01, 80);
        Material shiny5 = new BlinnPhongMaterial(new Color(190, 57, 255), 0.1d, 0.55d, 0.2d, 220);

        //objects
        scene.addObject(new Polygon(new Vector3D(0f,-2.15f,5f), "floor", reflectiveWhite));

        scene.addObject(new Polygon(new Vector3D(0f,-2f,5f), "set02/cylinder1", reflectiveBlue));
        scene.addObject(new Polygon(new Vector3D(0f,-2f,5f), "set02/cylinder2", reflective1));
        scene.addObject(new Polygon(new Vector3D(0f,-2f,5f), "set02/cylinder3", reflective2));
        scene.addObject(new Polygon(new Vector3D(0f,-2f,5f), "set02/gear", shiny3));
        scene.addObject(new Polygon(new Vector3D(0f,-2f,5f), "set02/pipe", shiny4));
        scene.addObject(new Polygon(new Vector3D(0f,-2f,5f), "set02/helix", shiny5));
        scene.addObject(new Polygon(new Vector3D(0f,-2f,5f), "set02/faces", reflective3));

        scene.addObject(new Sphere(new Vector3D(1.15f,-1f,3.2f), 0.25, shinyGreen));
        scene.addObject(new Sphere(new Vector3D(0.35f,1.25f,2.8f), 0.25, shiny1));
        scene.addObject(new Sphere(new Vector3D(-1.15f,0.9f,3.3f), 0.25, shinyRed));
        scene.addObject(new Sphere(new Vector3D(-2.45f,1.15f,3f), 0.25, shiny2));
        scene.addObject(new Sphere(new Vector3D(-0.65f,0.07f,3.15f), 0.25, shinyGreen));

        return scene;
    }
    public static Scene createScene03() { // 26 minutes and 45 seconds
        Scene scene = new Scene("Scene 03", IMAGE_EXTENSION);

        //camera and lights
        scene.setCamera(new Camera(CAMERA_POSITION, FOV, FOV, IMAGE_SIZE, IMAGE_SIZE, FAR_CLIPPING_PLANE, NEAR_CLIPPING_PLANE));
        scene.addLight(new PointLight(new Vector3D(0.15f,1f,-4f), Color.WHITE, 6f));

        //materials
        Material lambert = new LambertMaterial(Color.WHITE);
        Material material = new BlinnPhongMaterial(new Color(199, 195, 176), 0.05d, 0.95d, 0.15d, 60d);
        Material reflective1 = new ReflectiveMaterial(new Color(193, 49, 199), 0.05d, 0.15d, 0.65d, 600d, 0.15d);
        Material reflective2 = new ReflectiveMaterial(new Color(47, 199, 171), 0.05d, 0.2d, 0.75d, 700d, 0.15d);
        Material reflective3 = new ReflectiveMaterial(new Color(158, 199, 174), 0.05d, 0.25d, 0.35d, 200d, 0.55d);
        Material refractive = new RefractiveMaterial(Color.CYAN, 0.05d, 0.45d, 0.2d, 100d, 1.9d, 1.5d);

        //objects
        scene.addObject(new Polygon(new Vector3D(0f,-2.15f,5f), "floor", lambert));

        scene.addObject(new Polygon(new Vector3D(0f,-2f,5f), "set02/cylinder1", reflective1));
        scene.addObject(new Polygon(new Vector3D(0f,-2f,5f), "set02/cylinder2", reflective2));
        scene.addObject(new Polygon(new Vector3D(0f,-2f,5f), "set02/cylinder3", reflective3));

        scene.addObject(new Polygon(new Vector3D(0f,-2f,3.5f), "bunny", material));
        scene.addObject(new Sphere(new Vector3D(0.75f,-1f,2.75f), 0.85, refractive));


        return  scene;
    }

}
