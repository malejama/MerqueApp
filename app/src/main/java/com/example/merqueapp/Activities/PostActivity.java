package com.example.merqueapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

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

import com.example.merqueapp.Providers.AuthProviders;
import com.example.merqueapp.Providers.ImageProviders;
import com.example.merqueapp.Providers.PostProvider;
import com.example.merqueapp.R;
import com.example.merqueapp.models.Post;
import com.example.merqueapp.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class PostActivity extends AppCompatActivity {
    ImageView mImageViewPost1;
    ImageView mImageViewPost2;
    File mImageFile;
    File getmImageFile2;

    private final  int Gallery_REQUEST_CODE=1;
    private final  int Gallery_REQUEST_CODE_2=2;
    private final  int PHOTO_REQUEST_CODE=3;
    Button mButtonPost;
    ImageProviders mImageProvider;
    TextInputEditText mTextInputTitle;
    TextInputEditText mTextInputDescription;
    ImageView mImageViewVIVERES;
    ImageView mImageViewPAPELERIA;
    ImageView mImageViewLICORES;
    ImageView mImageViewASEO;
    TextView mTextViewCategory;
    String mCategory="";
    PostProvider mPostProvider;
    String mTitle="";
    String mDescription="";
    AuthProviders mAuthProviders;
    AlertDialog mDialog;
    CircleImageView mCircleImageView2;
    AlertDialog.Builder mBuilderSelector;
    CharSequence options[];

    String mAbsolutePhotoPatch;
    String mPhotoPatch;
    File mPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mImageViewPost1 = findViewById(R.id.ImageViewPost1);
        mImageViewPost2 = findViewById(R.id.ImageViewPost2);
        mTextInputTitle = findViewById(R.id.textInputnombreProducto);
        mTextInputDescription = findViewById(R.id.textInputDescription);
        mImageViewVIVERES = findViewById(R.id.ImageViewVIVERES);
        mImageViewPAPELERIA = findViewById(R.id.ImageViewPAPELERIA);
        mImageViewLICORES = findViewById(R.id.ImageViewLICORES);
        mImageViewASEO = findViewById(R.id.ImageViewASEO);
        mTextViewCategory = findViewById(R.id.textViewCategory);

        mButtonPost = findViewById(R.id.btnPost);
        mCircleImageView2= findViewById(R.id.circleimageback2);

        mImageProvider = new ImageProviders();
        mPostProvider=new PostProvider();
        mAuthProviders=new AuthProviders();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento...")
                .setCancelable(false).build();

        mBuilderSelector=new AlertDialog.Builder(this);
        mBuilderSelector.setTitle("Seleccione una opcion");
        options=new CharSequence[]{"Imagen de galeria","Tomar fotografia"};

        mCircleImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PostActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // saveImage(); antes
                clickPost();
            }
        });
        mImageViewPost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(Gallery_REQUEST_CODE);
                //openGallery(Gallery_REQUEST_CODE);
            }
        });

        mImageViewPost2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(Gallery_REQUEST_CODE_2);
                //openGallery(Gallery_REQUEST_CODE_2);
            }
        });

        mImageViewVIVERES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategory="VIVERES";
                mTextViewCategory.setText(mCategory);
            }
        });

        mImageViewPAPELERIA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategory="PAPELERIA";
                mTextViewCategory.setText(mCategory);
            }
        });

        mImageViewLICORES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategory="LICORES";
                mTextViewCategory.setText(mCategory);
            }
        });

        mImageViewASEO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategory="ASEO";
                mTextViewCategory.setText(mCategory);
            }
        });
    }

    private void selectOptionImage(int requestCode) {
        mBuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which ==0){
                    openGallery(requestCode);
                }else if (which==1){
                    takePhoto();
                }
            }
        });
        mBuilderSelector.show();
    }

    private void takePhoto() {
        //Toast.makeText(this, "Selecciono tomar foto", Toast.LENGTH_SHORT).show();
        Intent takePictureIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager())!=null){
            File photoFile =null;
            try {
                photoFile=createPhotoFile();
                
            }catch (Exception e){
                Toast.makeText(this, "Hunbo un error con el archivo"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if (photoFile !=null){
                Uri photoUri= FileProvider.getUriForFile(PostActivity.this,"com.example.merqueapp",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(takePictureIntent,PHOTO_REQUEST_CODE);
            }
        }
    }

    private File createPhotoFile() throws IOException {
        File storageDir= getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile= File.createTempFile(
                new Date()+ "photo",
                "jpg",
                storageDir
        );
        mPhotoPatch= "File:" + photoFile.getAbsolutePath();
        mAbsolutePhotoPatch= photoFile.getAbsolutePath();
        return photoFile;
    }

    private void clickPost() {
         mTitle=mTextInputTitle.getText().toString();
         mDescription=mTextInputDescription.getText().toString();
        if (!mTitle.isEmpty() && !mDescription.isEmpty() && !mCategory.isEmpty()){
             if (mImageFile !=null){
               saveImage(mImageFile,getmImageFile2);

             }else {
                 Toast.makeText(this, "Seleccione una imagen", Toast.LENGTH_SHORT).show();
             }
        }else{
            Toast.makeText(this, "Completa los campos para publicar", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImage(File mImageFile, File getmImageFile2) {
        mDialog.show();
        mImageProvider.save(PostActivity.this,mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String url=uri.toString();

                            mImageProvider.save(PostActivity.this,getmImageFile2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImage2) {
                                    if(taskImage2.isSuccessful()){
                                        mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri2) {
                                               String url2=uri2.toString();

                                                Post post=new Post();
                                                post.setImage1(url);
                                                post.setImage2(url2);
                                                post.setTitle(mTitle);
                                                post.setDescription(mDescription);
                                                post.setCategory(mCategory);
                                                post.setIdUser(mAuthProviders.getUid());

                                                mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> tasksave) {
                                                        mDialog.dismiss();
                                                        if (tasksave.isSuccessful()){
                                                            clearForm();
                                                            Toast.makeText(PostActivity.this, "La informacion se almaceno correctamente", Toast.LENGTH_SHORT).show();
                                                        }else {
                                                            Toast.makeText(PostActivity.this, "No se pudo almacenar la informacion", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }else {
                                        mDialog.dismiss();
                                        Toast.makeText(PostActivity.this, "Error al almacenar la imagen 2", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });
                    Toast.makeText(PostActivity.this, "La imagen se almaceno correctamente", Toast.LENGTH_SHORT).show();
                }else {
                    mDialog.dismiss();
                    Toast.makeText(PostActivity.this, "Error al almacenar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void clearForm() {
        mTextInputTitle.setText("");
        mTextInputDescription.setText("");
        mTextViewCategory.setText("");
        mImageViewPost1.setImageResource(R.drawable.subir_foto);
        mImageViewPost2.setImageResource(R.drawable.subir_foto);
        mTitle="";
        mDescription="";
        mCategory="";
        mImageViewPost1=null;
        mImageViewPost2=null;
    }

    private void openGallery(int requestCode) {
        Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,requestCode);

    }
    @Override
    protected void onActivityResult(int requestCode, int resulCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resulCode,data);
        //validacion de imagen con galeria
        if (requestCode == Gallery_REQUEST_CODE && resulCode == RESULT_OK) {
            try {
                mImageFile = FileUtil.from(this, data.getData());
                mImageViewPost1.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            } catch (Exception e) {
                Log.d("ERROR", "Se produjo un error" + e.getMessage());
                Toast.makeText(this, "Se produjo un error" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == Gallery_REQUEST_CODE_2 && resulCode == RESULT_OK){
            try {
                getmImageFile2 = FileUtil.from(this, data.getData());
                mImageViewPost2.setImageBitmap(BitmapFactory.decodeFile(getmImageFile2.getAbsolutePath()));
            } catch (Exception e) {
                Log.d("ERROR","Se produjo un error" + e.getMessage());
                Toast.makeText(this, "Se produjo un error" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        if(requestCode==PHOTO_REQUEST_CODE && resulCode==RESULT_OK){
            mImageFile=null;
            mPhotoFile=new File(mAbsolutePhotoPatch);
            Picasso.with(PostActivity.this).load(mPhotoPatch).into(mImageViewPost1);
        }
    }
  
}