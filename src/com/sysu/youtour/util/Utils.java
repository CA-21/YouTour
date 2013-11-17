package com.sysu.youtour.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils {

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (;;) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    /**
     * Function to convert milliseconds time to Timer Format Hours:Minutes:Seconds
     * */
    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    /**
     * Function to get Progress percentage
     * 
     * @param currentDuration
     * @param totalDuration
     * */
    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     * 
     * @param progress
     *            -
     * @param totalDuration
     *            returns current duration in milliseconds
     * */
    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    /**
     * return the url list for that haven't download in sdcard.
     * 
     * @param lineID
     * @return
     */
    public static ArrayList<String> checkLineDownloaded(String lineID) {
        ArrayList<String> urls = new ArrayList<String>();
        // check the json file exist
        File jsonFile = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
                + GlobalConst.SDCARD_CACHE_DIR + "/" + lineID.hashCode() + "/" + GlobalConst.JSON_FILE_NAME.hashCode());
        if (!jsonFile.exists()) {
            // TODO There may be a problem to phase this json for it's a jsonarray with only one object.
            urls.add(GlobalConst.JSON_IDENTIFY + "-" + GlobalConst.HOST + GlobalConst.URL_HAEDER_LINEID + lineID);
        }

        // check the json file format, phase all the url and check the resource file exist
        try {
            JSONObject lineJson = null;
            FileInputStream stream = new FileInputStream(jsonFile);
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            /* Instead of using default, pass in a decoder. */
            String result = Charset.defaultCharset().decode(bb).toString();
            JSONArray ja = new JSONArray(result);
            lineJson = ja.getJSONObject(0);
            stream.close();

            String coverThumbnail = lineJson.getString("coverThumbnail");
            File coverThumbnailFile = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
                    + GlobalConst.SDCARD_CACHE_DIR + "/" + GlobalConst.LINE_THUMBNAIL_DIR_NAME.hashCode() + "/"
                    + (GlobalConst.HOST + coverThumbnail).hashCode());
            if (!coverThumbnailFile.exists()) {
                urls.add(GlobalConst.LINE_THUNMNAIL_IDENTIFY + "-" + GlobalConst.HOST + coverThumbnail);
            }
            String authorImage = lineJson.getString("authorImage");
            File authorImagelFile = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
                    + GlobalConst.SDCARD_CACHE_DIR + "/" + lineID.hashCode() + "/"
                    + (GlobalConst.HOST + authorImage).hashCode());
            if (!authorImagelFile.exists()) {
                urls.add(GlobalConst.OTHER_IMAGE_IDENTIFY + "-" + GlobalConst.HOST + authorImage);
            }
            JSONArray stops = lineJson.getJSONArray("stops");
            for (int i = 0; i < stops.length(); i++) {
                JSONObject stopJsonObject = stops.getJSONObject(i);
                String stopThumbnail = stopJsonObject.getString("stopThumbnail");
                File stopThumbnailFile = new File(android.os.Environment.getExternalStorageDirectory()
                        .getAbsolutePath()
                        + GlobalConst.SDCARD_CACHE_DIR
                        + "/"
                        + lineID.hashCode()
                        + "/"
                        + (GlobalConst.HOST + stopThumbnail).hashCode());
                if (!stopThumbnailFile.exists()) {
                    urls.add(GlobalConst.OTHER_IMAGE_IDENTIFY + "-" + GlobalConst.HOST + stopThumbnail);
                }
                String stopAudio = stopJsonObject.getString("stopAudio");
                File stopAudioFile = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
                        + GlobalConst.SDCARD_CACHE_DIR + "/" + lineID.hashCode() + "/"
                        + (GlobalConst.HOST + stopAudio).hashCode());
                if (!stopAudioFile.exists()) {
                    urls.add(GlobalConst.AUDIO_IDENTIFY + "-" + GlobalConst.HOST + stopAudio);
                }
                JSONArray stopImages = stopJsonObject.getJSONArray("stopImages");
                for (int j = 0; j < stopImages.length(); j++) {
                    String stopImage = stopImages.getString(j);
                    File stopImageFile = new File(android.os.Environment.getExternalStorageDirectory()
                            .getAbsolutePath()
                            + GlobalConst.SDCARD_CACHE_DIR
                            + "/"
                            + lineID.hashCode()
                            + "/"
                            + (GlobalConst.HOST + stopImage).hashCode());
                    if (!stopImageFile.exists()) {
                        urls.add(GlobalConst.OTHER_IMAGE_IDENTIFY + "-" + GlobalConst.HOST + stopImage);
                    }
                }

            }
        } catch (Exception e) {
            // TODO There may be a problem to phase this json for it's a jsonarray with only one object.
            urls.add(GlobalConst.JSON_IDENTIFY + "-" + GlobalConst.HOST + GlobalConst.URL_HAEDER_LINEID + lineID);
            e.printStackTrace();
        }
        // if downloaded save a allDownLoaded file in line cache to indicate it's finish
        // File allDownLoaded = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
        // + GlobalConst.SDCARD_CACHE_DIR + "/" + lineID.hashCode() + "/" + GlobalConst.ALL_DOWNLOADED.hashCode());
        // if (urls.size() == 0) {
        // try {
        // allDownLoaded.createNewFile();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // } else {
        // if (allDownLoaded.exists())
        // allDownLoaded.delete();
        // }
        return urls;
    }
}