package com.sysu.youtour.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Stop {
    // 站点顺序
    private int               num;

    // 站点名称
    private String            stopName;

    // 站点描述
    private String            stopDes;

    // 站点坐标 经度，纬度
    private double            locate[]   = new double[2];

    // 站点图片
    private String            stopThumbnail;

    // 站点音频
    private String            stopAudio;

    // 站点顺序
    private String            imageOrder;

    // 站点图片
    private ArrayList<String> stopImages = new ArrayList<String>();

    // 站点构造函数
    public Stop(JSONObject stop) {
        try {
            num = stop.getInt("num");
            stopName = stop.getString("stopName");
            stopDes = stop.getString("stopDes");
            JSONArray locatArray = stop.getJSONArray("locate");
            locate[0] = locatArray.getDouble(0);
            locate[0] = locatArray.getDouble(1);
            stopThumbnail = stop.getString("stopThumbnail");
            stopAudio = stop.getString("stopAudio");
            imageOrder = stop.getString("imageOrder");
            JSONArray imagesArray = stop.getJSONArray("stops");
            for (int i = 0; i < imagesArray.length(); i++) {
                stopImages.add(imagesArray.getString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @return the num
     */
    public int getNum() {
        return num;
    }

    /**
     * @param num
     *            the num to set
     */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     * @return the stopName
     */
    public String getStopName() {
        return stopName;
    }

    /**
     * @param stopName
     *            the stopName to set
     */
    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    /**
     * @return the stopDes
     */
    public String getStopDes() {
        return stopDes;
    }

    /**
     * @param stopDes
     *            the stopDes to set
     */
    public void setStopDes(String stopDes) {
        this.stopDes = stopDes;
    }

    /**
     * @return the locate
     */
    public double[] getLocate() {
        return locate;
    }

    /**
     * @param locate
     *            the locate to set
     */
    public void setLocate(double[] locate) {
        this.locate = locate;
    }

    /**
     * @return the stopThumbnail
     */
    public String getStopThumbnail() {
        return stopThumbnail;
    }

    /**
     * @param stopThumbnail
     *            the stopThumbnail to set
     */
    public void setStopThumbnail(String stopThumbnail) {
        this.stopThumbnail = stopThumbnail;
    }

    /**
     * @return the stopAudio
     */
    public String getStopAudio() {
        return stopAudio;
    }

    /**
     * @param stopAudio
     *            the stopAudio to set
     */
    public void setStopAudio(String stopAudio) {
        this.stopAudio = stopAudio;
    }

    /**
     * @return the imageOrder
     */
    public String getImageOrder() {
        return imageOrder;
    }

    /**
     * @param imageOrder
     *            the imageOrder to set
     */
    public void setImageOrder(String imageOrder) {
        this.imageOrder = imageOrder;
    }

    /**
     * @return the stopImages
     */
    public ArrayList<String> getStopImages() {
        return stopImages;
    }

    /**
     * @param stopImages
     *            the stopImages to set
     */
    public void setStopImages(ArrayList<String> stopImages) {
        this.stopImages = stopImages;
    }

}
