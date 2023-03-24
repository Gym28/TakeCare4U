package com.gina.takecare4u.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gina.takecare4u.R;
import com.gina.takecare4u.activities.Utils.FileUtils;
import com.gina.takecare4u.providers.ImageProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import javax.annotation.Nullable;

public class SecondActivity extends AppCompatActivity {
    ImageView mImageviewCargar1;
    private final int Image_gallery_request_code = 1;
    File mImagenFile;
    Button mButtonPublicacion;
    ImageProvider mImageProvider;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mImageProvider = new ImageProvider();

        mButtonPublicacion= findViewById(R.id.btnPublicacion);

        mButtonPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });


        mImageviewCargar1= findViewById(R.id.imageViewCargar1);
        mImageviewCargar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });


    }

    private void saveImage() {
        mImageProvider.save(SecondActivity.this, mImagenFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SecondActivity.this, "La imagen se almacenó", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(SecondActivity.this, "Error al guardar la imagen",Toast.LENGTH_LONG);
                }

            }
        });

    }

    ActivityResultLauncher<Intent>ImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()== Activity.RESULT_OK){
                        try{
                            mImagenFile = FileUtils.from(SecondActivity.this, result.getData().getData());
                            mImageviewCargar1.setImageBitmap(BitmapFactory.decodeFile(mImagenFile.getAbsolutePath()));

                        }catch(Exception e){
                            Log.d("ERROR", "ha ocurrido un error al cargar la imagen"+ e.getMessage());
                            Toast.makeText (SecondActivity.this , "Upss Ocurrió un error" + e.getMessage(), Toast.LENGTH_LONG).show();

                        }


                    }
                }
            }
    );

    private void openImage() {

        Intent galleryIntent  = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
         ImageLauncher.launch(galleryIntent);


    }
}