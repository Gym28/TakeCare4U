package com.gina.takecare4u.activities;

import static android.service.controls.ControlsProviderService.TAG;

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
import com.gina.takecare4u.modelos.Users;
import com.gina.takecare4u.providers.AuthProvider;
import com.gina.takecare4u.providers.ImageProvider;
import com.gina.takecare4u.providers.UsersProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class EditProfileActivity extends AppCompatActivity {

    CircleImageView mCircleImageViewBack;
    CircleImageView mCircleImageViewProfile;
    ImageView mImageViewCover;
    TextView mTexViewPassword;
    TextInputEditText mTextInputZipCode;
    TextInputEditText mTextInputPhone;

    Button mButtonEditProfile;

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
    //
    String mzipeCode="";
    String mPhone="";
    String mUsername="";
    String mImageProfile="";
    String mImageCover="";

    //
    private final int Image_gallery_request_code_profile = 1;
    private final int Image_gallery_request_code_cover = 2;
    private final int Image_gallery_request_code_3 = 3;
    File mImagenFile;
    File mImagenFile2;


    AlertDialog mDialog;
    ImageProvider mImageProvider;
    UsersProvider mUserProvider;

    AuthProvider mAuthProvider;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mTextInputZipCode=findViewById(R.id.textInputZipcodeEdit);
        mTextInputPhone=findViewById(R.id.textInputPhoneEdit);
        mCircleImageViewProfile=findViewById(R.id.circleImageProfile);
        mImageViewCover=findViewById(R.id.imageViewCover);
        mBuilderSelector = new AlertDialog.Builder(this);
        mTexViewPassword=findViewById(R.id.textViewCambiarContraseña);
        mBuilderSelector.setTitle("Selecciona una opción");
        mImageProvider = new ImageProvider();
        mUserProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();
        options = new CharSequence[] {"Imagen de galería", "Tomar fotografía"};
        mDialog= new  SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espera un momento")
                .setCancelable(false)
                .build();

        mCircleImageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(1);
            }
        });
        mTexViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Users user = new Users();
                String email = mAuthProvider.getEmail();
                mAuthProvider.cambiarContraseña(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(EditProfileActivity.this, "se envió correo para cambiar la contraseña", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(EditProfileActivity.this, "no se pudo enviar el correo para cambio de contraseña", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });

        mButtonEditProfile = findViewById(R.id.btnEditProfile);
        mButtonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickEditProfile();

            }
        });

        mImageViewCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(2);
            }
        });



        mCircleImageViewBack= findViewById(R.id.circleImageBack);
        mCircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getUser();
    }

    private void clickEditProfile() {
        mzipeCode = mTextInputZipCode.getText().toString();
        mPhone = mTextInputPhone.getText().toString();
        if(!mzipeCode .isEmpty() && !mPhone.isEmpty() ){
                    if (mzipeCode.length() == 5) {
                         if (mImagenFile != null && mImagenFile2 != null) {
                             //selecciona imagenes de la galeria
                             upDateComplete(mImagenFile, mImagenFile2);
                         }
                         //carga fotos de camara
                         else if (mPictureFile_1 != null && mPictureFile_2 != null) {
                             upDateComplete(mPictureFile_1, mPictureFile_2);
                         } else if (mImagenFile != null && mPictureFile_2 != null) {
                             upDateComplete(mImagenFile, mPictureFile_2);
                         } else if (mPictureFile_1 != null && mImagenFile2 != null) {
                             upDateComplete(mPictureFile_1, mImagenFile2);
                         }
                         else if(mPictureFile_1 !=null){
                             saveImage(mPictureFile_1, true);
                         }
                         else if (mPictureFile_2!=null){
                             saveImage(mPictureFile_2, false);
                         }
                         else if (mImagenFile != null){
                             saveImage(mImagenFile, true);
                         }
                         else if(mImagenFile2!=null){
                             saveImage(mImagenFile2, false);
                         }
                         else {
                             Users user = new Users();
                             user.setPhone(mPhone);
                             user.setZipCode(mzipeCode);
                             user.setId(mAuthProvider.getUid());
                             user.setNombre(mUsername);
                             user.setImageProfile(mImageProfile);
                             user.setImageCover(mImageCover);
                             updateInfo(user);

                         }
                         Log.d(TAG, "createUserWithEmail:success");


                     } else {
                         Toast.makeText(this, "Verifica el Código Postal ", Toast.LENGTH_SHORT).show();
                     }
              }else{

            Toast.makeText(this, "Debes completar todos los campos", Toast.LENGTH_SHORT).show();
        }




    }

    private void upDateComplete(File imageFile1, File imageFile2) {
        mDialog.show();
        mImageProvider.save(EditProfileActivity.this, imageFile1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String urlProfile = uri.toString();
                            //Para cargar la imagen 2
                            mImageProvider.save(EditProfileActivity.this, imageFile2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(Task<UploadTask.TaskSnapshot> taskImagen2) {
                                    if(taskImagen2.isSuccessful()){
                                        mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri2) {
                                                String urlCover = uri2.toString();
                                                String id = mAuthProvider.getUid();
                                                Users user = new Users();
                                                user.setPhone(mPhone);
                                                user.setZipCode(mzipeCode);
                                                user.setImageProfile(urlProfile);
                                                user.setImageCover(urlCover);
                                                user.setId(id);
                                                user.setNombre(mUsername);
                                                updateInfo(user);

                                            }
                                        });
                                    }
                                    else {
                                        mDialog.dismiss();
                                        Toast.makeText (EditProfileActivity.this , "La Segunda Imagen no se pudo guardar" , Toast.LENGTH_LONG).show();


                                    }

                                }
                            });

                        }
                    });


                }
                else {
                    mDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Error al guardar la imagen",Toast.LENGTH_LONG);
                }

            }
        });

    }




    private void getUser() {
    mUserProvider.getUsers(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("nombre")){
                        mUsername=documentSnapshot.getString("nombre");
                    }
                    if(documentSnapshot.contains("phone")){
                        mPhone = documentSnapshot.getString("phone");
                        mTextInputPhone.setText(mPhone);
                    }
                    if(documentSnapshot.contains("zipCode")){
                        mzipeCode=documentSnapshot.getString("zipCode");
                        mTextInputZipCode.setText(mzipeCode);
                    }
                    if(documentSnapshot.contains("imageProfile")) {
                        mImageProfile = documentSnapshot.getString("imageProfile");
                        if(mImageProfile!=null){
                            if(!mImageProfile.isEmpty()){
                                Picasso.with(EditProfileActivity.this).load(mImageProfile).into(mCircleImageViewProfile);
                            }
                        }

                    }
                    if(documentSnapshot.contains("imageCover")) {
                        mImageCover = documentSnapshot.getString("imageCover");
                        if (mImageCover != null) {
                            if (!mImageCover.isEmpty()) {
                                Picasso.with(EditProfileActivity.this).load(mImageCover).into(mImageViewCover);
                            }
                        }
                    }
                }

            }
        });

    }


    private void updateInfo(Users user){
        if(mDialog.isShowing()) {
            mDialog.show();
        }
        mUserProvider.upDate(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NotNull Task<Void> taskSave) {
                mDialog.dismiss();
                if(taskSave.isSuccessful()){

                    Toast.makeText(EditProfileActivity.this, "Tu usuario ha sido actualizado",Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(EditProfileActivity.this, "Error al guardar la actualización",Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    private void saveImage(File imageFile, boolean isProfileImage){
        mDialog.show();
        mImageProvider.save(EditProfileActivity.this, imageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            String id = mAuthProvider.getUid();
                            Users user = new Users();
                            user.setPhone(mPhone);
                            user.setZipCode(mzipeCode);
                            user.setNombre(mUsername);
                            if(isProfileImage){
                                user.setImageProfile(url);
                                user.setImageCover(mImageCover);
                            }
                            else {
                                user.setImageCover(url);
                                user.setImageProfile(mImageProfile);

                            }
                            user.setId(id);
                            user.setNombre(mUsername);
                            updateInfo(user);
                        }
                    });
               }
                else {
                    mDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Error al guardar la imagen",Toast.LENGTH_LONG);
                }

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
                    takePicture(Image_gallery_request_code_profile);

                } else if (which==1 && numeroImagen==2){
                    takePicture(Image_gallery_request_code_cover);

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
                Toast.makeText (EditProfileActivity.this , "Upss Ocurrió un error" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            if (pictureFile != null && request_code ==Image_gallery_request_code_profile) {

                Uri pictureUri = FileProvider.getUriForFile(EditProfileActivity.this, "com.gina.takecare4u", pictureFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);

                PictureLauncher.launch(takePictureIntent);

            }
            else if (pictureFile != null && request_code ==Image_gallery_request_code_cover){
                Uri pictureUri = FileProvider.getUriForFile(EditProfileActivity.this, "com.gina.takecare4u", pictureFile);
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
        if(requestCode == Image_gallery_request_code_profile) {
            mPicturePath = "file: " + mPhotoFile.getAbsolutePath();
            mAbsolutePath = mPhotoFile.getAbsolutePath();
            Log.d("ERROR", "picturePath" + mPicturePath);
        }
        else if (requestCode==Image_gallery_request_code_cover){
            mPicturePath_2= "file: " + mPhotoFile.getAbsolutePath();
            mAbsolutePath_2 = mPhotoFile.getAbsolutePath();
            Log.d("ERROR", "picturePath" + mPicturePath_2);
        }


        return mPhotoFile;

    }

    ActivityResultLauncher<Intent> ImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()== Activity.RESULT_OK){
                        try{
                            mPictureFile_1= null;
                            mImagenFile = FileUtils.from(EditProfileActivity.this, result.getData().getData());
                            mCircleImageViewProfile.setImageBitmap(BitmapFactory.decodeFile(mImagenFile.getAbsolutePath()));
                        }catch(Exception e){
                            Log.d("ERROR", "ha ocurrido un error al cargar la imagen"+ e.getMessage());
                            Toast.makeText (EditProfileActivity.this, "Upss Ocurrió un error" + e.getMessage(), Toast.LENGTH_LONG).show();

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
                            mImagenFile2 = FileUtils.from(EditProfileActivity.this, result.getData().getData());
                            mImageViewCover.setImageBitmap(BitmapFactory.decodeFile(mImagenFile2.getAbsolutePath()));

                        }catch(Exception e){
                            Log.d("ERROR", "ha ocurrido un error al cargar la imagen"+ e.getMessage());
                            Toast.makeText (EditProfileActivity.this , "Upss Ocurrió un error" + e.getMessage(), Toast.LENGTH_LONG).show();

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

                            Picasso.with(EditProfileActivity.this).load(mPhotoFile).fit().into(mCircleImageViewProfile);

                            Toast.makeText(EditProfileActivity.this, "Imagen Cargada correctamente", Toast.LENGTH_LONG).show();


                        }catch(Exception e){

                            Log.d("ERROR", "ha ocurrido un error al cargar la imagen"+ e.getMessage());
                            Toast.makeText (EditProfileActivity.this , "Upss Ocurrió un error" + e.getMessage(), Toast.LENGTH_LONG).show();

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
                            Picasso.with(EditProfileActivity.this).load(mPhotoFile).fit().into(mImageViewCover);

                            Toast.makeText(EditProfileActivity.this, "Imagen Cargada correctamente", Toast.LENGTH_LONG).show();


                        }catch(Exception e){

                            Log.d("ERROR", "ha ocurrido un error al cargar la imagen"+ e.getMessage());
                            Toast.makeText (EditProfileActivity.this , "Upss Ocurrió un error" + e.getMessage(), Toast.LENGTH_LONG).show();

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
        ViewedMessageHelper.updateOnline(true, EditProfileActivity.this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewedMessageHelper.updateOnline(false, EditProfileActivity.this);
    }

}