package com.gina.takecare4u.providers;

import android.content.Context;

import com.gina.takecare4u.activities.Utils.CompresorBitmatUtil;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;

public class ImageProvider {
//logica para almacenar en storage
    StorageReference mStorage;
    public ImageProvider(){
      //firebaseStorage
        mStorage = FirebaseStorage.getInstance().getReference();

    }

    /**
     * método para almacenar imagenes
     * @param context
     * @param file
     * @return
     */
    public UploadTask save (Context context, File file){
        byte[] imageByte= CompresorBitmatUtil.getImage(context, file.getPath(), 500, 500);
        StorageReference storage= FirebaseStorage.getInstance().getReference().child(new Date() + ".jpg");
        mStorage = storage;
        UploadTask task = storage.putBytes(imageByte);
        return task;

    }


    public StorageReference getStorage(){
        return mStorage;
    }

}
