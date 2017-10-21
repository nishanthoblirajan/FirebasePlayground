package com.zaptrapp.firebaseplayground;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class StorageActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 111;
    private EditText categoryEt;
    private EditText weightEt;
    private Button chooseImageBt;
    private Button uploadImageBt;
    private ImageView imageviewStorageActivity;
    Uri filePath;
    private ProgressBar progressStorageActivity;
    //creating reference to firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        initView();
    }

    public String stringFromEt(EditText et) {
        if (et.getText() != null) {
            return et.getText().toString();
        } else {
            return "0";
        }
    }

    public void chooseImage(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    public void uploadImage(View view) {

        if (filePath != null) {
            progressStorageActivity.setVisibility(View.VISIBLE);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference productStorage = database.getReference("product");
            final String productKey = productStorage.push().getKey();
            final DatabaseReference categoryStorage = database.getReference("category");

            //Save it to FirebaseStorage
            StorageReference childRef = storageRef.child(stringFromEt(categoryEt)).child(productKey + ".jpg");

            //uploading the image
            UploadTask uploadTask = childRef.putFile(filePath);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//                    Storage Schema
//                    category
//                            ----category.getName
//                                ----name: category.getName

//                    Product
//                            ----productKey
//                                ----id: productKey
//                                ----category: categoryFirebase.getName()
//                                ----weight: weightEt
//                                ----imageUrl: from onSuccessUpload
                    progressStorageActivity.setVisibility(View.INVISIBLE);
                    Toast.makeText(StorageActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                    CategoryFirebase categoryFirebase = new CategoryFirebase(stringFromEt(categoryEt));
                    categoryStorage.child(categoryFirebase.getName()).setValue(categoryFirebase);
                    ProductFirebase productFirebase = new ProductFirebase();
//

                    productFirebase.setId(productKey);
                    productFirebase.setCategory(categoryFirebase.getName());
                    //Working but the above normal String method is preferred
//                    productFirebase.setCategoryFirebase(categoryFirebase);
                    productFirebase.setWeight(stringFromEt(weightEt));
                    productFirebase.setImageUrl(taskSnapshot.getDownloadUrl().toString());
                    productStorage.child(productKey).setValue(productFirebase);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressStorageActivity.setVisibility(View.INVISIBLE);

                    Toast.makeText(StorageActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(StorageActivity.this, "Select an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        categoryEt = (EditText) findViewById(R.id.category_et);
        weightEt = (EditText) findViewById(R.id.weight_et);
        chooseImageBt = (Button) findViewById(R.id.choose_image_bt);
        uploadImageBt = (Button) findViewById(R.id.upload_image_bt);
        imageviewStorageActivity = (ImageView) findViewById(R.id.imageview_storage_activity);
        progressStorageActivity = (ProgressBar) findViewById(R.id.progress_storage_activity);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting image to ImageView
                imageviewStorageActivity.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
