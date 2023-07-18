/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.engine;


import up.edu.isgc.raytracer.lights.*;
import up.edu.isgc.raytracer.materials.*;
import up.edu.isgc.raytracer.objects.*;
import up.edu.isgc.raytracer.objects.utility.*;
import up.edu.isgc.raytracer.tools.*;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Renders a image with a given scene, camera and configuration
 * Shows a progress log in the console
 * Uses multi threading for better performance and lower render times
 *
 * @author Homar Cano, Jafet Rodríguez
 */
public class Raytracer {
    /**
     * constant for a initial bias value (used for shadows, reflection and refraction)
     */
    private static final double DEFAULT_BIAS = 0.000001d;

    private int nThreads;
    private int timeout;
    private float lightFalloffExponent;
    private boolean useClippingPlanes;
    private double shadowBias;
    private double reflectionBias;
    private double refractionBias;

    /**
     * class constructor
     * @param nThreads total number of threads available for the program to use, more threads leads to better performance
     * @param timeout the maximum time in ms for each render, if it is exceeded the render will stop
     * @param lightFalloffExponent the exponent used to evaluate the light intensity according to the distance
     * @param useClippingPlanes a boolean flag used to determine if the clipping planes of the camera should be used or not
     */
    public Raytracer(int nThreads, int timeout, float lightFalloffExponent, boolean useClippingPlanes) {
        setNumberOfThreads(nThreads);
        setTimeout(timeout);
        setLightFalloffExponent(lightFalloffExponent);
        setUseClippingPlanes(useClippingPlanes);
        setShadowBias(DEFAULT_BIAS);
        setReflectionBias(DEFAULT_BIAS);
        setRefractionBias(DEFAULT_BIAS);
    }

    /**
     * saves a BufferedImage into a file with a given name and extension
     * the file will be saved in the images/ folder
     * @param image the BufferedImage to save
     * @param name the name of the file
     * @param extension the extension of the file
     */
    public void saveImage(BufferedImage image, String name, String extension) {
        if(image == null) return;
        File outputFile = new File("images/" + name + "." + extension);
        try {
            ImageIO.write(image, extension, outputFile);
            System.out.println(new Date() + " finished saving the image '" + name + "." + extension + "'");
        } catch (IOException ioe) {
            System.out.println(new Date() + " !! an error occurred saving the image '" + name + "." + extension + "' !!");
            ioe.printStackTrace();
        }
    }

    /**
     * renders a scene and saves it with its the name and extension
     * shows a progress log in the console
     * @param scene the Scene to render (using the ray tracing algorithm)
     */
    public void raytraceScene(Scene scene) {
        if (scene == null) {
            System.out.println(new Date() + "!! there is no scene to raytrace !!");
            return;
        }
        Camera mainCamera = scene.getCamera();
        if(mainCamera == null) {
            System.out.println(new Date() + "!! there is no camera for the scene !!");
            return;
        }

        BufferedImage image = new BufferedImage(mainCamera.getResolutionWidth(), mainCamera.getResolutionHeight(), BufferedImage.TYPE_INT_RGB);
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(getNumberOfThreads(), getNumberOfThreads(), 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        Vector3D[][] screenPositions = mainCamera.calculateScreenPositions();
        String sceneName = scene.getName();

        System.out.println(new Date() + " assigning tasks to thread pool...");
        for (int x = 0; x < screenPositions.length; x++) {
            for (int y = 0; y < screenPositions[x].length; y++) {
                threadPool.execute(getPixelColor(x, y, screenPositions, mainCamera, scene.getObjects(), scene.getLights(), image));
            }
        }
        threadPool.shutdown();

        long startDateTime = new Date().getTime();
        int percentage = 0;
        while (percentage < 100) {
            int newPercentage = Math.round((float) threadPool.getCompletedTaskCount() / threadPool.getTaskCount() * 100f);
            if (percentage < newPercentage) {
                System.out.println(new Date() + " raytracing the scene '" + sceneName + "': " + percentage + "%");
                percentage = newPercentage;
            }

            if (new Date().getTime() - startDateTime > getTimeout()) {
                System.out.println("!! Execution Timeout - The raytracing of '" + sceneName + "' exceeded the time limit !!");
                System.out.println(new Date() + " attempting to interrupt all threads");
                try {
                    if (!threadPool.awaitTermination(30, TimeUnit.SECONDS)) threadPool.shutdownNow();
                } catch (InterruptedException ex) {
                    threadPool.shutdownNow();
                    Thread.currentThread().interrupt();
                }
                System.exit(-1);
            }
        }

        System.out.println(new Date() + " raytracing the scene '" + sceneName + "': 100%");
        saveImage(image, sceneName, scene.getExtension());
    }

    /**
     * calculates the pixel color of a given i, j coordinate pair of the image
     * @param i the width's pixel index of the image
     * @param j the height's pixel index of the image
     * @param screenPositions a bi-dimensional Vector3D array with all the (x, y, z) positions corresponding to a (i, j) pixel in the image
     * @param mainCamera the main Camera of the scene
     * @param objects a list with all the Object3D objects in the scene
     * @param lights a list with all the Light objects in the scene
     * @param image the image in which the pixel color will be written
     * @return lambda function of a Runnable task
     */
    private synchronized Runnable getPixelColor(int i, int j, Vector3D[][] screenPositions, Camera mainCamera, ArrayList<Object3D> objects, ArrayList<Light> lights, BufferedImage image) {
        return () -> {
            Vector3D cameraPosition = mainCamera.getPosition();
            double x = screenPositions[i][j].getX() + cameraPosition.getX();
            double y = screenPositions[i][j].getY() + cameraPosition.getY();
            double z = screenPositions[i][j].getZ() + cameraPosition.getZ();

            Vector3D screenPosition = new Vector3D(x, y, z);
            Ray primaryRay = new Ray(cameraPosition, screenPosition);
            Intersection closestIntersection = raycast(primaryRay, objects, mainCamera);

            if (closestIntersection != null) {
                Color pixelColor = shade(closestIntersection, objects, lights, mainCamera);
                image.setRGB(i, j, pixelColor.getRGB());
            } else {
                image.setRGB(i, j, Color.BLACK.getRGB());
            }
        };
    }

    /**
     * shades a point in the scene using Blinn-Phon, Reflection and Refraction
     * @param intersection the Intersection of the point to shade
     * @param objects a list with all the Object3D objects in the scene
     * @param lights a list with all the Light objects in the scene
     * @param camera the main Camera of the scene
     * @return the Color of the point in the scene according to the shading used
     * @see <a href="http://web.cse.ohio-state.edu/~shen.94/681/Site/Slides_files/reflection_refraction.pdf">Reflection and Refraction</a>
     */
    private Color shade(Intersection intersection, ArrayList<Object3D> objects, ArrayList<Light> lights, Camera camera) {
        Material objectMaterial = intersection.getObject().getMaterial();
        Color pixelColor = Color.BLACK;

        for (Light light : lights) {
            // BLINN-PHONG
            Color objectColor = getColor(intersection, objects, light, camera);
            pixelColor = addColor(pixelColor, objectColor);
            // REFLECTION
            if (objectMaterial instanceof ReflectiveMaterial) pixelColor = addColor(pixelColor, getReflectedColor(intersection, objectMaterial, objects, light, camera));
            // REFRACTION
            if (objectMaterial instanceof RefractiveMaterial) pixelColor = addColor(pixelColor, getRefractedColor(intersection, objectMaterial, objects, lights, camera));
        }

        return pixelColor;
    }

    /**
     * helper function for raycast() with fewer arguments used only with primary rays
     * checks each Object3D in the Scene to find the closest one in the direction of a given Ray
     * @param ray the Ray used to check an intersection with an object
     * @param objects a list with all the Object3D objects to be checked
     * @param camera the main Camera of the scene
     * @return the closest Intersection in the direction of the given ray or null if
     */
    private Intersection raycast(Ray ray, ArrayList<Object3D> objects, Camera camera) {
        return raycast(ray, objects, camera, true);
    }

    /**
     * checks each Object3D in the Scene to find the closest one in the direction of a given Ray
     * @param ray the Ray used to check an intersection with an object
     * @param objects a list with all the Object3D objects to be checked
     * @param camera the main Camera of the scene
     * @param primaryCast a boolean flag to determine if the ray cast is used with primary rays or not
     * @return the closest Intersection in the direction of the given ray or null if
     */
    private Intersection raycast(Ray ray, ArrayList<Object3D> objects, Camera camera, boolean primaryCast) {
        Intersection closestIntersection = null;
        for (Object3D object : objects) {
            Intersection intersection =  null;
            if (object instanceof up.edu.isgc.raytracer.objects.Polygon) intersection = ((Polygon) object).getObjectIntersection(ray);
            else if (object instanceof Sphere) intersection = ((Sphere) object).getObjectIntersection(ray);
            if (intersection != null) {
                double distance = intersection.getDistance();
                if (distance >= 0 && (closestIntersection == null || distance < closestIntersection.getDistance())) {
                    if (!isUsingClippingPlanes() || !primaryCast) closestIntersection = intersection;
                    else if (distance < camera.getFarClippingPlane() && distance > camera.getNearClippingPlane()) closestIntersection = intersection;
                }
            }
        }
        return closestIntersection;
    }

    /**
     * calculates the Color showed by a reflection in a given point of the scene
     * @param objectIntersection the Intersection of the point to shade
     * @param material the Material of the object that causes the reflection
     * @param objects a list with all the Object3D objects in the scene
     * @param light a light used to illuminate the reflected Color
     * @param camera the main Camera of the scene
     * @return the reflected Color of the given point
     */
    private Color getReflectedColor(Intersection objectIntersection, Material material, ArrayList<Object3D> objects, Light light, Camera camera) {
        Vector3D intersectionPosition = objectIntersection.getPosition();
        Vector3D intersectionNormal = objectIntersection.getNormal();

        Vector3D viewDirection = Vector3D.subtract(intersectionPosition, camera.getPosition());
        double normalValue = -2.0 * Vector3D.dotProduct(intersectionNormal, viewDirection);
        Vector3D reflection = Vector3D.scalarMultiplication(intersectionNormal, normalValue);
        reflection = Vector3D.add(viewDirection, reflection);

        Ray reflectionRay = new Ray(Vector3D.add(intersectionPosition, Vector3D.scalarMultiplication(reflection, getReflectionBias())), reflection);
        Intersection reflectionIntersection = raycast(reflectionRay, objects, camera, false);

        if (reflectionIntersection != null) {
            Color reflectionColor = getColor(reflectionIntersection, objects, light, camera);
            return multiplyColor(reflectionColor, ((ReflectiveMaterial) material).getReflectionCoefficient());
        } else {
            return Color.BLACK;
        }
    }

    /**
     * calculates the Color visible when a refraction occurs in a given point of the scene
     * @param objectIntersection the Intersection of the point to shade
     * @param material the Material of the object that causes the refraction
     * @param objects a list with all the Object3D objects in the scene
     * @param lights a list with all the Light objects in the scene
     * @param camera the main Camera of the scene
     * @return the Color visible through the refraction of the given point
     * @see <a href="https://www.scratchapixel.com/lessons/3d-basic-rendering/introduction-to-shading/reflection-refraction-fresnel">Reflection, Refraction (Transmission) and Fresnel</a>
     */
    private Color getRefractedColor(Intersection objectIntersection, Material material, ArrayList<Object3D> objects, ArrayList<Light> lights, Camera camera) {
        Vector3D viewDirection = Vector3D.subtract(objectIntersection.getPosition(), camera.getPosition());
        Vector3D intersectionNormal = objectIntersection.getNormal();

        double refractionCoefficient = ((RefractiveMaterial) material).getRefractionCoefficient();
        double refractionAngleValue = Vector3D.dotProduct(objectIntersection.getNormal(), Vector3D.normalize(viewDirection));

        if (refractionAngleValue < -1) refractionAngleValue = -1;
        else if (refractionAngleValue > 1) refractionAngleValue = 1;

        double mediumRefractionIndex;
        if (refractionAngleValue < 0) {
            mediumRefractionIndex = 1 / refractionCoefficient;
            refractionAngleValue *= -1;
        } else {
            intersectionNormal = Vector3D.scalarMultiplication(intersectionNormal, -1);
            mediumRefractionIndex = refractionCoefficient;
        }
        double totalInternalRefraction = 1 - mediumRefractionIndex * mediumRefractionIndex * (1 - refractionAngleValue * refractionAngleValue);
        if (totalInternalRefraction > 0) {
            Vector3D refractionVector = Vector3D.add(Vector3D.scalarMultiplication(viewDirection, mediumRefractionIndex), Vector3D.scalarMultiplication(intersectionNormal, (mediumRefractionIndex * refractionAngleValue - Math.sqrt(totalInternalRefraction))));
            Ray refractionRay = new Ray(Vector3D.add(objectIntersection.getPosition(), Vector3D.scalarMultiplication(refractionVector, getRefractionBias())), refractionVector);
            Intersection refraction = raycast(refractionRay, objects, camera, false);
            if (refraction != null) {
                Color refractionColor = shade(refraction, objects, lights, camera);
                return multiplyColor(refractionColor, ((RefractiveMaterial) material).getRefractionMultiplier());
            }
        }
        return Color.BLACK;
    }

    /**
     * calculates the Color of a given point using the Blinn-Phong model
     * @param objectIntersection the Intersection of the point to shade
     * @param objects a list with all the Object3D objects in the scene
     * @param light a light used to illuminate the given point
     * @param camera the main Camera of the scene
     * @return the Color of the given point
     * @see <a href="https://www.scratchapixel.com/lessons/3d-basic-rendering/phong-shader-BRDF">The Phong Model</a>
     * @see <a href="https://learnopengl.com/Advanced-Lighting/Advanced-Lighting">Blinn-Phong Lighting</a>
     */
    private Color getColor(Intersection objectIntersection, ArrayList<Object3D> objects, Light light, Camera camera) {
        Material objectMaterial = objectIntersection.getObject().getMaterial();

        Color objectColor = objectIntersection.getObject().getColor();
        Color pixelColor = Color.BLACK;

        double ambientCoefficient = 0d;
        double diffuseCoefficient = 1d;
        double specularCoefficient = 0d;

        if (objectMaterial instanceof BlinnPhongMaterial) {
            ambientCoefficient = ((BlinnPhongMaterial) objectMaterial).getAmbientCoefficient();
            diffuseCoefficient = ((BlinnPhongMaterial) objectMaterial).getDiffuseCoefficient();
            specularCoefficient = ((BlinnPhongMaterial) objectMaterial).getSpecularCoefficient();
        }

        pixelColor = addColor(pixelColor, multiplyColor(calculateAmbientColor(objectIntersection), ambientCoefficient));

        Vector3D shadowOrigin = Vector3D.add(objectIntersection.getPosition(), Vector3D.scalarMultiplication(objectIntersection.getNormal(), getShadowBias()));
        Ray shadowRay = new Ray(shadowOrigin, light.getPosition());
        Intersection shadowIntersection = raycast(shadowRay, objects, camera, false);

        if (shadowIntersection == null) {
            pixelColor = addColor(pixelColor, multiplyColor(calculateDiffuseColor(objectIntersection, light, objectColor), diffuseCoefficient));
            pixelColor = addColor(pixelColor, multiplyColor(calculateSpecularColor(objectIntersection, light, camera), specularCoefficient));
        }

        return pixelColor;
    }

    /**
     * calculates the ambient Color component of the Blinn-Phong model in a given point
     * @param intersection the Intersection of the point to shade
     * @return the ambient Color component
     */
    private Color calculateAmbientColor(Intersection intersection) {
        return intersection.getObject().getColor();
    }

    /**
     * calculates the diffuse Color component of the Blinn-Phong model in a given point
     * @param intersection the Intersection of the point to shade
     * @param light a light used to illuminate the given point
     * @param objectColor the Color of the object in the given point
     * @return the diffuse Color component
     */
    private Color calculateDiffuseColor(Intersection intersection, Light light, Color objectColor) {
        Color diffuseColor = Color.BLACK;
        float lambertianSurfaceValue = (float) light.getLambertianSurfaceValue(intersection);
        double lightIntensity = light.getIntensity();
        Color lightColor = light.getColor();
        double distanceFromPointToLight = Vector3D.magnitude(Vector3D.subtract(intersection.getPosition(), light.getPosition()));
        if (light instanceof DirectionalLight) distanceFromPointToLight = 1d;
        lightIntensity /= Math.pow(distanceFromPointToLight, getLightFalloffExponent());
        Color newColor = multiplyColor(objectColor, lightIntensity * lambertianSurfaceValue);
        newColor = multiplyColors(newColor, lightColor);
        diffuseColor = addColor(diffuseColor, newColor);
        return diffuseColor;
    }

    /**
     * calculates the specular Color component of the Blinn-Phong model in a given point
     * @param intersection the Intersection of the point to shade
     * @param light a light used to illuminate the given point
     * @param camera the main Camera of the scene
     * @return the specular Color component
     */
    private Color calculateSpecularColor(Intersection intersection, Light light, Camera camera) {
        Vector3D surfaceNormal = Vector3D.normalize(intersection.getNormal());
        Object3D object = intersection.getObject();
        Color objectColor = object.getColor();
        Color specularColor = Color.BLACK;
        Material objMaterial = object.getMaterial();
        double shininessFactor;
        if (objMaterial instanceof BlinnPhongMaterial) shininessFactor = ((BlinnPhongMaterial) objMaterial).getShininess();
        else shininessFactor = 0d;
        Vector3D lightDirection = Vector3D.normalize(Vector3D.subtract(light.getPosition(), object.getPosition()));
        Vector3D viewDirection = Vector3D.normalize(Vector3D.subtract(camera.getPosition(), object.getPosition()));
        Vector3D halfDirection = Vector3D.normalize(Vector3D.add(lightDirection, viewDirection));
        double blinnPhongValue = Math.pow(Math.max(Vector3D.dotProduct(surfaceNormal, halfDirection), 0d), shininessFactor);
        Color newColor = multiplyColor(objectColor, blinnPhongValue);
        specularColor = addColor(specularColor, newColor);
        return specularColor;
    }

    /**
     * multiplies a Color with a coefficient or scalar
     * @param color the Color to multiply
     * @param coefficient the scalar used in the multiplication
     * @return the Color multiplied
     */
    private Color multiplyColor(Color color, double coefficient) {
        float[] colorValues = new float[]{normalizeColor(color.getRed()), normalizeColor(color.getGreen()), normalizeColor(color.getBlue())};
        for (int colorIndex = 0; colorIndex < 3; colorIndex++) colorValues[colorIndex] *= coefficient;
        return new Color(clamp(colorValues[0]), clamp(colorValues[1]), clamp(colorValues[2]));
    }

    /**
     * multiplies the RGB values of two Colors
     * @param colorA the first Color to multiply
     * @param colorB the second Color to multiply
     * @return the Color multiplied
     */
    private Color multiplyColors(Color colorA, Color colorB) {
        float[] colorValuesA = new float[]{normalizeColor(colorA.getRed()), normalizeColor(colorA.getGreen()), normalizeColor(colorA.getBlue())};
        float[] colorValuesB = new float[]{normalizeColor(colorB.getRed()), normalizeColor(colorB.getGreen()), normalizeColor(colorB.getBlue())};
        for (int colorIndex = 0; colorIndex < 3; colorIndex++) colorValuesA[colorIndex] *= colorValuesB[colorIndex];
        return new Color(clamp(colorValuesA[0]), clamp(colorValuesA[1]), clamp(colorValuesA[2]));
    }

    /**
     * adds the RGB values of two Colors
     * @param colorA the first Color to add
     * @param colorB the second Color to add
     * @return the Color added
     */
    private Color addColor(Color colorA, Color colorB){
        float red = clamp(normalizeColor(colorA.getRed()) + normalizeColor(colorB.getRed()));
        float green = clamp(normalizeColor(colorA.getGreen()) + normalizeColor(colorB.getGreen()));
        float blue = clamp(normalizeColor(colorA.getBlue()) + normalizeColor(colorB.getBlue()));
        return new Color(red, green, blue);
    }

    /**
     * normalizes a RGB component of a Color
     * @param colorComponentValue the RGB value to normalize
     * @return the RGB value normalized
     */
    private float normalizeColor(int colorComponentValue) {
        return colorComponentValue / 255.0f;
    }

    /**
     * changes a given value to 1 or 0 if it is out of the range [0,1]
     * @param value the value to change
     * @return the valued changed if necessary
     */
    private float clamp(float value) {
        if (value < 0f) return 0f;
        return Math.min(value, 1f);
    }


    /**
     * class getter
     * @return the total number of threads available to use
     */
    public int getNumberOfThreads() {
        return nThreads;
    }
    /**
     * class setter
     * @param nThreads total number of threads to set
     */
    public void setNumberOfThreads(int nThreads) {
        this.nThreads = nThreads;
    }

    /**
     * class getter
     * @return the maximum time for each render
     */
    public int getTimeout() {
        return timeout;
    }
    /**
     * class setter
     * @param timeout the maximum time to set
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * class getter
     * @return the light falloff exponent
     */
    public float getLightFalloffExponent() {
        return lightFalloffExponent;
    }
    /**
     * class setter
     * @param lightFalloffExponent the light falloff exponent to set
     */
    public void setLightFalloffExponent(float lightFalloffExponent) {
        this.lightFalloffExponent = lightFalloffExponent;
    }

    /**
     * class getter
     * @return boolean flag if clipping planes are used or not
     */
    public boolean isUsingClippingPlanes() {
        return useClippingPlanes;
    }
    /**
     * class setter
     * @param useClippingPlanes boolean flag for using the clipping planes to set
     */
    public void setUseClippingPlanes(boolean useClippingPlanes) {
        this.useClippingPlanes = useClippingPlanes;
    }
    /**
     * class getter
     * @return the shadow bias value
     */
    public double getShadowBias() {
        return shadowBias;
    }
    /**
     * class setter
     * @param shadowBias the shadow bias value to set
     */
    public void setShadowBias(double shadowBias) {
        this.shadowBias = shadowBias;
    }

    /**
     * class getter
     * @return the reflection bias value
     */
    public double getReflectionBias() {
        return reflectionBias;
    }
    /**
     * class setter
     * @param reflectionBias the reflection bias value to set
     */
    public void setReflectionBias(double reflectionBias) {
        this.reflectionBias = reflectionBias;
    }

    /**
     * class getter
     * @return the refraction bias value
     */
    public double getRefractionBias() {
        return refractionBias;
    }
    /**
     * class setter
     * @param refractionBias the refraction bias value to set
     */
    public void setRefractionBias(double refractionBias) {
        this.refractionBias = refractionBias;
    }

}
