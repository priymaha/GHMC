/**
 * Copyright (C) 2016 KeepTrax, Inc. All Rights Reserved.
 * <p>
 * NOTICE:  All information contained herein is, and remains the property of
 * KeepTrax Incorporated.  The intellectual and technical concepts contained
 * herein are proprietary to KeepTrax Incorporated and may be covered by U.S.
 * and Foreign Patents, patents in process, and are protected by trade secret
 * or copyright law. Dissemination of this information or reproduction of this
 * material as a whole or in part is strictly forbidden unless prior written
 * permission is obtained from KeepTrax Incorporated.
 */

package com.example.priyanka.SmartCitizen.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Priyanka on 15-03-2016.
 */
public class CameraHelper {

    /*
     * Creating file uri to store image
     */
    public static Uri getOutputMediaFileUri(Context context) {
        return Uri.fromFile(getOutputMediaFile(context));
    }

    /*
     * returning image
     */
    private static File getOutputMediaFile(Context context) {

        File mediaStorageDir = createDirIfNotExists(Constants.IMAGE_DIRECTORY_NAME, context);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
    }

    /**
     * Create a directory local to the application so that the app can delete
     * the files it recorded when it is un-installed
     */
    public static File createDirIfNotExists(String path, Context context) {

        File file = new File(context.getExternalFilesDir(null), path);
        if (!file.exists()) {
            if (!file.mkdirs()) {

            }
        }
        return file;
    }

    public static void deleteLatestImageFromCam(Context context, long photoBeforeTime, long photoAfterTime) {
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE
        };
        final Cursor cursor = context.getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                        null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");


        if (cursor !=null && cursor.moveToFirst()) {
            int bucketNameIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int timeStampColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);


            String bucketName = cursor.getString(bucketNameIndex);
            long timeStamp = cursor.getLong(timeStampColumnIndex);
            if (bucketName.equalsIgnoreCase("camera") //samsung
                    || bucketName.contains("Camera") // moto
                    || bucketName.contains("100")  // htc
                    || bucketName.contains("dcim")) {
                String imageLocation = cursor.getString(1);
                File imageFile = new File(imageLocation);
                if (imageFile.exists()) {
                    if (timeStamp >= photoBeforeTime && timeStamp <= photoAfterTime) {
                        imageFile.delete();
                        broadcastScanFile(context, imageFile);
                    }
                }
            }
        }
        cursor.close();
    }

    private static void broadcastScanFile(Context context, File f) {
        Intent intentNotifyImgDeleted = new Intent();
        intentNotifyImgDeleted.setType("image");
        intentNotifyImgDeleted.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intentNotifyImgDeleted.setData(Uri.fromFile(f));
        context.sendBroadcast(intentNotifyImgDeleted);
    }
}
