package utility;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class LocalCache {

    public static void setDate (Context context, ArrayList arrayList, String tag, int type) {
        File file = context.getCacheDir();
        File cache = null;
        String name;
        if(type == 0){
            name = "cache_info_"+tag;
            cache = new File(file,name);
        }

        if (cache.exists()){
            cache.delete();
        }
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    new FileOutputStream(cache));
            outputStream.writeObject(arrayList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList getData (Context context, String tag, int type) throws IllegalAccessException,InstantiationException {
        File file = context.getCacheDir();
        String name;
        File cache;
        ArrayList arrayList = null;
        if (type == 0) {
            name = "cache_info_"+tag;
            cache = new File(file, name);
            if (!cache.exists()){
                return null;
            }
            try {
                ObjectInputStream inputStream = new ObjectInputStream(
                        new FileInputStream(cache));
                arrayList = (ArrayList) inputStream.readObject();
                return arrayList;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
