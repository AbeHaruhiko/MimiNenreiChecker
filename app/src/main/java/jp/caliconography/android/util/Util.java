package jp.caliconography.android.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by abeharuhiko on 2014/08/14.
 */
public class Util {

    public  static File getScreenShotFile(View view , Context context) {
        return saveImageToCacheDir(getScreenBitmap(view, context), context);
    }

    private static Bitmap getScreenBitmap(View view, Context context){
        return getViewBitmap(view.getRootView(), context);
    }

    private static Bitmap getViewBitmap(View view, Context context){
        view.setDrawingCacheEnabled(true);
        Bitmap cache = view.getDrawingCache();
        if(cache == null){
            return null;
        }
//        Bitmap bitmap = Bitmap.createBitmap(cache);
        Bitmap bitmap = scaleDownBitmap(cache, 300, context);
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private static Bitmap scaleDownBitmap(Bitmap bitmap, int newHeight, Context context) {

        if (newHeight >= bitmap.getHeight()) {
            return bitmap;
        }

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h = (int) (newHeight * densityMultiplier);
        int w = (int) (h * bitmap.getWidth() / ((double) bitmap.getHeight()));

        bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);

        return bitmap;
    }

    private static File saveImageToCacheDir(Bitmap bmp, Context context){
        String fileName = String.valueOf(System.currentTimeMillis()) + ".png";

        File file = new File(context.getExternalCacheDir(), fileName);
        FileOutputStream outputStream;

        try {

            outputStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, outputStream);

            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}
