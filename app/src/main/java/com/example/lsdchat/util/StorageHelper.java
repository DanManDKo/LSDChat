package com.example.lsdchat.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StorageHelper {
    private static final String AVATAR_FILE_NAME = "avatar.jpg";
    private static final int IMAGE_QUALITY = 100;
    private static final int REQUERED_SIZE = 320;

    public static Uri decodeAndSaveUri(Context c, Uri uri)
            throws FileNotFoundException {
        //resizing...
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp / 2 < REQUERED_SIZE || height_tmp / 2 < REQUERED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap resizedBitmap = BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);

        //saving...
        File storageDir = c.getExternalFilesDir(String.valueOf(Environment.DIRECTORY_PICTURES));
        File file = new File(storageDir, AVATAR_FILE_NAME);
        try {
            FileOutputStream out = new FileOutputStream(file);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.parse(file.getAbsolutePath());
    }
}
