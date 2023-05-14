package com.gina.takecare4u.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gina.takecare4u.R;
import com.gina.takecare4u.activities.Utils.FileUtils;
import com.gina.takecare4u.activities.Utils.ViewedMessageHelper;
import com.gina.takecare4u.modelos.Publicaciones;
import com.gina.takecare4u.providers.AuthProvider;
import com.gina.takecare4u.providers.ImageProvider;
import com.gina.takecare4u.providers.PublicacionProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class SecondActivity extends AppCompatActivity {
    ImageView mImageviewCargar1;
    ImageView mImageviewCargar2;
    private final int Image_gallery_request_code = 1;
    private final int Image_gallery_request_code_2 = 2;
    private final int Image_gallery_request_code_3 = 3;
    File mImagenFile;
    File mImagenFile2;
    Button mButtonPublicacion;
    CircleImageView mCircleImageView;
    ImageProvider mImageProvider;
    PublicacionProvider mPublicacionProvider;
    AuthProvider mAuthProvider;
    TextInputEditText mTextInputNombre;
    TextInputEditText mTextInputDescripcion;
    TextInputEditText mTextInputPrecio;
    ImageView mImageViewNiños;
    ImageView mImageViewAncianos;
    ImageView mImageViewMascotas;
    TextView mTextViewServicio;
    SliderView mSliderView;
    String mServicio = "";
    String mTitle = "";
    String mDescription = "";
    String mPrice  = "";
    AlertDialog mDialog;

    AlertDialog.Builder mBuilderSelector;
    CharSequence options[];

    //variables para tomar fotografía
    String mAbsolutePath;
    String mPicturePath;
    File mPhotoFile;
    File mPictureFile_1;
// foto 2
    String mAbsolutePath_2;
    String mPicturePath_2;
    File mPictureFile_2;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mTextInputNombre= findViewById(R.id.textInputNombre);
        mTextInputDescripcion= findViewById(R.id.textInputDescription);
        mTextInputPrecio= findViewById(R.id.textInputPrice);
        mImageViewAncianos =findViewById(R.id.imageViewAncianos);
        mImageViewNiños =findViewById(R.id.imageViewNiños);
        mImageViewMascotas =findViewById(R.id.imageViewMascotas);
        mTextViewServicio = findViewById(R.id.textViewTipoServicio);
        mCircleImageView = findViewById(R.id.circleImageBack);



        mImageProvider = new ImageProvider();
        mAuthProvider = new AuthProvider();
        mPublicacionProvider = new PublicacionProvider();
        mDialog= new  SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espera un momento")
                .setCancelable(false)
                .build();

        mBuilderSelector = new AlertDialog.Builder(this);
        mBuilderSelector.setTitle("Selecciona una opción");
        options = new CharSequence[] {"Imagen de galería", "Tomar fotografía"};

        mCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mButtonPublicacion= findViewById(R.id.btnPublicacion);

        //eventos onclick de cada servicio
        mImageViewNiños.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mServicio = "NIÑOS";
              mTextViewServicio.setText("NIÑOS");
            }
        });

        mImageViewAncianos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mServicio = "ADULTO MAYOR";
                mTextViewServicio.setText("ADULTO MAYOR");


            }
        });

        mImageViewMascotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mServicio="MASCOTAS";
                mTextViewServicio.setText("MASCOTAS");

            }
        });

        mButtonPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickPublicacion();
            }
        });


        mImageviewCargar1= findViewById(R.id.imageViewCargar1);
        mImageviewCargar2=findViewById(R.id.imageViewCargar2);

        mImageviewCargar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectOptionImage(2);
            }
        });
        mImageviewCargar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectOptionImage(1);

            }
        });


    }

    private void selectOptionImage(int numeroImagen) {

        mBuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0 && numeroImagen==1){
                    openImage();
                }
                else if (which==0 && numeroImagen==2){
                    openImage2();
                }
                else if (which==1 && numeroImagen==1){
                    takePicture(Image_gallery_request_code);

                } else if (which==1 && numeroImagen==2){
                    takePicture(Image_gallery_request_code_2);

                }
            }
        });

        mBuilderSelector.show();
    }


    private void takePicture(int request_code) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager())!= null){
            File pictureFile = null;
            try {
                pictureFile = createPictureFile(request_code);

            }catch (Exception e){
                Toast.makeText (SecondActivity.this , "Upss Ocurrió un error" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            if (pictureFile != null && request_code ==Image_gallery_request_code) {

                Uri pictureUri = FileProvider.getUriForFile(SecondActivity.this, "com.gina.takecare4u", pictureFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);

                PictureLauncher.launch(takePictureIntent);

            }
             else if (pictureFile != null && request_code ==Image_gallery_request_code_2){
                Uri pictureUri = FileProvider.getUriForFile(SecondActivity.this, "com.gina.takecare4u", pictureFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);

                PictureLauncher2.launch(takePictureIntent);



            }
            }
        }


    private File createPictureFile(int requestCode) throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        mPhotoFile =  File.createTempFile(
                new Date() + "_picture", ".jpg",
                storageDir
        );
        if(requestCode == Image_gallery_request_code) {
            mPicturePath = "file: " + mPhotoFile.getAbsolutePath();
            mAbsolutePath = mPhotoFile.getAbsolutePath();
            Log.d("ERROR", "picturePath" + mPicturePath);
        }
        else if (requestCode==Image_gallery_request_code_2){
            mPicturePath_2= "file: " + mPhotoFile.getAbsolutePath();
            mAbsolutePath_2 = mPhotoFile.getAbsolutePath();
            Log.d("ERROR", "picturePath" + mPicturePath_2);
        }


        return mPhotoFile;

    }

    /**
     * Metodo para hacer la publicación
     */

    private void clickPublicacion() {

        mTitle = mTextInputNombre.getText().toString();
        mDescription = mTextInputDescripcion.getText().toString();
        mPrice = mTextInputPrecio.getText().toString();
            if(!mTitle.isEmpty() && !mDescription.isEmpty() && !mPrice.isEmpty() && !mServicio.isEmpty() ) {
                if (mImagenFile != null && mImagenFile2 != null) {
                    //selecciona imagenes de la galeria
                    saveImage(mImagenFile, mImagenFile2);
                }
                //carga fotos de camara
                else if (mPictureFile_1 != null && mPictureFile_2 != null) {
                    saveImage(mPictureFile_1, mPictureFile_2);
                } else if (mImagenFile != null && mPictureFile_2 != null) {
                    saveImage(mImagenFile, mPictureFile_2);
                } else if (mPictureFile_1 != null && mImagenFile2 != null) {
                    saveImage(mPictureFile_1, mImagenFile2);

                    Toast.makeText(SecondActivity.this, "La publicación ha sido guardada", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(SecondActivity.this, "Debes seleccionar una imagen", Toast.LENGTH_LONG).show();
                }
            }
                else {

                Toast.makeText(SecondActivity.this, "Debes Completar todos los campos", Toast.LENGTH_LONG).show();
            }




    }

    private void saveImage(File imageFile1, File imageFile2) {
        mDialog.show();
        mImageProvider.save(SecondActivity.this, imageFile1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String url = uri.toString();
                            //Para cargar la imagen 2
                            mImageProvider.save(SecondActivity.this, imageFile2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(Task<UploadTask.TaskSnapshot> taskImagen2) {
                                    if(taskImagen2.isSuccessful()){
                                        mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri2) {
                                                String url2 = uri2.toString();

                                                Publicaciones post = new Publicaciones();
                                                post.setImagen1(url);
                                                post.setImagen2(url2);
                                                post.setNombre(mTitle.toLowerCase());
                                                post.setDescripcion(mDescription);
                                                post.setPrecio(mPrice);
                                                post.setServicio(mServicio);
                                                post.setIdUser(mAuthProvider.getUid());
                                                post.setTimestamp(new Date().getTime());

                                                mPublicacionProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NotNull Task<Void> taskSave) {
                                                        mDialog.dismiss();
                                                        if(taskSave.isSuccessful()){
                                                            clearForm();
                                                            Toast.makeText(SecondActivity.this, "La publicación ha sido guardada",Toast.LENGTH_LONG).show();

                                                        } else {
                                                            Toast.makeText(SecondActivity.this, "Error al guardar la publicación",Toast.LENGTH_LONG).show();

                                                        }

                                                    }
                                                });


                                            }
                                        });
                                    }
                                    else {
                                        mDialog.dismiss();
                                        Toast.makeText (SecondActivity.this , "La Segunda Imagen no se pudo guardar" , Toast.LENGTH_LONG).show();


                                    }

                                }
                            });

                        }
                    });


                }
                else {
                    mDialog.dismiss();
                    Toast.makeText(SecondActivity.this, "Error al guardar la imagen",Toast.LENGTH_LONG);
                }

            }
        });

    }

    private void clearForm() {
        mTextInputNombre.setText("");
        mTextInputDescripcion.setText("");
        mTextInputPrecio.setText("");
        mTextViewServicio.setText("");
        mImageviewCargar1.setImageResource(R.drawable.camara_nino);
        mImageviewCargar2.setImageResource(R.drawable.camara_nino);
        String mTitle = "";
        String mDescription = "";
        String mPrice  = "";
        String mServicio = "";
        mImagenFile=null;
        mImagenFile2=null;

    }

    ActivityResultLauncher<Intent>ImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()== Activity.RESULT_OK){
                        try{
                            mPictureFile_1= null;
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
    ActivityResultLauncher<Intent>ImageLauncher2= registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()== Activity.RESULT_OK){
                        try{
                            mPictureFile_2= null;
                            mImagenFile2 = FileUtils.from(SecondActivity.this, result.getData().getData());
                            mImageviewCargar2.setImageBitmap(BitmapFactory.decodeFile(mImagenFile2.getAbsolutePath()));

                        }catch(Exception e){
                            Log.d("ERROR", "ha ocurrido un error al cargar la imagen"+ e.getMessage());
                            Toast.makeText (SecondActivity.this , "Upss Ocurrió un error" + e.getMessage(), Toast.LENGTH_LONG).show();

                        }


                    }



                }
            }
    );
//selección imagen para foto
    ActivityResultLauncher<Intent>PictureLauncher= registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode()== Activity.RESULT_OK){
                        try{

                            mImagenFile = null;
                            mPictureFile_1 = new File(mAbsolutePath);

                          Picasso.with(SecondActivity.this).load(mPhotoFile).fit().into(mImageviewCargar1);

                            Toast.makeText(SecondActivity.this, "Imagen Cargada correctamente", Toast.LENGTH_LONG).show();


                        }catch(Exception e){

                            Log.d("ERROR", "ha ocurrido un error al cargar la imagen"+ e.getMessage());
                            Toast.makeText (SecondActivity.this , "Upss Ocurrió un error" + e.getMessage(), Toast.LENGTH_LONG).show();

                        }


                    }



                }
            }
    );
    ActivityResultLauncher<Intent>PictureLauncher2= registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode()== Activity.RESULT_OK){
                        try{
                            mImagenFile2 = null;
                            mPictureFile_2= new File(mAbsolutePath_2);
                            Picasso.with(SecondActivity.this).load(mPhotoFile).fit().into(mImageviewCargar2);

                            Toast.makeText(SecondActivity.this, "Imagen Cargada correctamente", Toast.LENGTH_LONG).show();


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
    private void openImage2() {

        Intent galleryIntent  = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        ImageLauncher2.launch(galleryIntent);

    }
    @Override
    protected void onStart() {
        super.onStart();
        ViewedMessageHelper.updateOnline(true, SecondActivity.this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewedMessageHelper.updateOnline(false, SecondActivity.this);
    }
}

