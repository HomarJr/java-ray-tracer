/*
  [1968] - [2020] Centros Culturales de Mexico A.C / Universidad Panamericana
  All Rights Reserved.
 */
package up.edu.isgc.raytracer.objects.utility;


import up.edu.isgc.raytracer.objects.Object3D;

/**
 * Stores information for a camera such as position, FOV, resolution, and a close uo value (defaultZ)
 */
public class Camera extends Object3D {
    private double[] fieldOfView = new double[2];
    private int[] resolution;
    private double defaultZ = 15d;
    private double[] clippingPlanes;

    /**
     * class constructor
     * @param position the position of the camera in the scene
     * @param fieldOfViewHorizontal the horizontal field of view in degrees
     * @param fieldOfViewVertical the vertical field of view in degrees
     * @param widthResolution the width of the image taken by the camera in pixels
     * @param heightResolution the height of the image taken by the camera in pixels
     * @param farClippingPlane the position (z coordinate) of the far clipping plane
     * @param nearClippingPlane the position (z coordinate) of the near clipping plane
     */
    public Camera(Vector3D position, double fieldOfViewHorizontal, double fieldOfViewVertical, int widthResolution, int heightResolution, double farClippingPlane, double nearClippingPlane) {
        super(position, null);
        setFieldOfView(new double[]{fieldOfViewHorizontal, fieldOfViewVertical});
        setResolution(new int[]{widthResolution, heightResolution});
        setClippingPlanes(new double[]{farClippingPlane, nearClippingPlane});
    }

    /**
     * calculates where all the rays should point to according to the resolution, FOV and close up values
     * @return a bi-dimensional array with all the positions, each position represents a pixel in the image
     */
    public Vector3D[][] calculateScreenPositions() {
        double angleMaxX = 90 - (getFieldOfViewHorizontal() / 2f);
        double radiusMaxX = getDefaultZ() / Math.cos(Math.toRadians(angleMaxX));

        double maxX = Math.sin(Math.toRadians(angleMaxX)) * radiusMaxX;
        double minX = -maxX;

        double angleMaxY = 90 - (getFieldOfViewVertical() / 2f);
        double radiusMaxY = getDefaultZ() / Math.cos(Math.toRadians(angleMaxY));

        double maxY = Math.sin(Math.toRadians(angleMaxY)) * radiusMaxY;
        double minY = -maxY;

        Vector3D[][] positions = new Vector3D[getResolutionWidth()][getResolutionHeight()];
        double posZ = getDefaultZ();
        for(int x = 0; x < positions.length; x++){
            for(int y = 0; y < positions[x].length; y++){
                double posX = minX + (((maxX - minX) / (double) getResolutionWidth()) * x);
                double posY = maxY - (((maxY - minY) / (double) getResolutionHeight()) * y);
                positions[x][y] = new Vector3D(posX, posY, posZ);
            }
        }
        return positions;
    }

    /**
     * class getter
     * @return the close up value
     */
    public double getDefaultZ() {
        return defaultZ;
    }
    /**
     * class setter
     * @param defaultZ the close up value to set
     */
    public void setDefaultZ(double defaultZ) {
        this.defaultZ = defaultZ;
    }

    /**
     * class getter
     * @return the FOV array
     */
    public double[] getFieldOfView() {
        return fieldOfView;
    }
    /**
     * class setter
     * @param fieldOfView the FOV array to set
     */
    public void setFieldOfView(double[] fieldOfView) {
        this.fieldOfView = fieldOfView;
    }

    /**
     * class getter
     * @return the horizontal FOV
     */
    public double getFieldOfViewHorizontal() {
        return getFieldOfView()[0];
    }
    /**
     * class setter
     * @param hFOV the horizontal FOV to set
     */
    public void setFieldOfViewHorizontal(double hFOV) {
        this.fieldOfView[0] = hFOV;
    }

    /**
     * class getter
     * @return the vertical FOV
     */
    public double getFieldOfViewVertical() {
        return getFieldOfView()[1];
    }
    /**
     * class setter
     * @param vFOV the vertical FOV to set
     */
    public void setFieldOfViewVertical(double vFOV) {
        this.fieldOfView[1] = vFOV;
    }

    /**
     * class getter
     * @return the resolution array
     */
    public int[] getResolution() {
        return resolution;
    }
    /**
     * class setter
     * @param resolution the resolution array to set
     */
    public void setResolution(int[] resolution) {
        this.resolution = resolution;
    }

    /**
     * class getter
     * @return the width
     */
    public int getResolutionWidth(){
        return getResolution()[0];
    }
    /**
     * class setter
     * @param width the width to set
     */
    public void setResolutionWidth(int width) {
        this.resolution[0] = width;
    }

    /**
     * class getter
     * @return the height
     */
    public int getResolutionHeight(){
        return getResolution()[1];
    }
    /**
     * class setter
     * @param height the height to set
     */
    public void setResolutionHeight(int height) {
        this.resolution[1] = height;
    }

    /**
     * class getter
     * @return the far and near clipping planes array
     */
    public double[] getClippingPlanes() {
        return clippingPlanes;
    }
    /**
     * class setter
     * @param clippingPlanes the far and near clipping planes array
     */
    public void setClippingPlanes(double[] clippingPlanes) {
        this.clippingPlanes = clippingPlanes;
    }

    /**
     * class getter
     * @return the far clipping plane
     */
    public double getFarClippingPlane(){
        return getClippingPlanes()[0];
    }
    /**
     * class setter
     * @param farClippingPlane the far clipping plane to set
     */
    public void setFarClippingPlane(double farClippingPlane) {
        this.clippingPlanes[0] = farClippingPlane;
    }

    /**
     * class getter
     * @return the near clipping plane
     */
    public double getNearClippingPlane(){
        return getClippingPlanes()[1];
    }
    /**
     * class setter
     * @param nearClippingPlane the near clipping plane to set
     */
    public void setNearClippingPlane(double nearClippingPlane) {
        this.clippingPlanes[1] = nearClippingPlane;
    }

}
