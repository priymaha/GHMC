package com.example.priyanka.ghmc.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Priyanka on 26/09/16.
 */

public class ChangeOrientationUtils {

    public static void changeOrientation(String path) {
        ExifInterface exif;
        int angle = 0;
//        Bitmap bitmap = null;
//
//
//        bitmap = BitmapFactory.decodeFile(path);
        try {
            exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);


            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                angle = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                angle = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                angle = 270;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Matrix matrix1 = new Matrix();

        //set image rotation value to 45 degrees in matrix.
        matrix1.postRotate(angle);
        try {

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(path), null, options);
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 700, 700); //My device pixel resolution
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap bmpPic = BitmapFactory.decodeStream(new FileInputStream(path), null, options);
            //Create bitmap with new values.
            Bitmap photo = Bitmap.createBitmap(bmpPic, 0, 0,
                    bmpPic.getWidth(), bmpPic.getHeight(), matrix1, true);

            OutputStream outStream;

            File file = new File(path);
            if (file.exists()) {
                file.delete();
                file = new File(path);
            }
            try {
                // make a new bitmap from your file
//            Bitmap bitmap = BitmapFactory.decodeFile(file.getName());

                outStream = new FileOutputStream(file);

                photo.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (photo != null) {
                    photo.recycle();
                }

                bmpPic.recycle();
            }
        } catch (Exception e) {

        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
