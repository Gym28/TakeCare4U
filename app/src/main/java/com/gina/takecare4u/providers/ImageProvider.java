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

    StorageReference mStorage;
    public ImageProvider(){
      //firebaseStorage
        mStorage = FirebaseStorage.getInstance().getReference();

    }

    public UploadTask save (Context context, File file){
        byte[] imageByte= CompresorBitmatUtil.getImage(context, file.getPath(), 500, 500);
        StorageReference storage= mStorage.child(new Date() + ".jpg");
        UploadTask task = storage.putBytes(imageByte);
        return task;

    }

}
