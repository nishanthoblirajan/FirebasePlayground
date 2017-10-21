package com.zaptrapp.firebaseplayground;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

    DatabaseReference productStorage;
    FirebaseDatabase database;
    DatabaseReference categoryStorage;

    //FirebaseRecyclerAdapter
    FirebaseRecyclerAdapter<ProductFirebase, ProductHolder> recyclerAdapter;
    private RecyclerView recyclerView;

    public static final String TAG = StorageActivity.class.getSimpleName();

    @Override
    protected void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        initView();
        database = FirebaseDatabase.getInstance();

        productStorage = database.getReference("product");
        categoryStorage = database.getReference("category");


        //For display by categories
        Query query = productStorage.child("category").equalTo("fing");

        //For display by weight
        Query query2 = productStorage.orderByChild("weight");


        Log.d(TAG, "onCreate: "+query.toString());
        FirebaseRecyclerOptions<ProductFirebase> options =
                new FirebaseRecyclerOptions.Builder<ProductFirebase>()
                        .setQuery(query, ProductFirebase.class)
                        .build();
        recyclerAdapter = new FirebaseRecyclerAdapter<ProductFirebase, ProductHolder>
                (options) {
            @Override
            protected void onBindViewHolder(ProductHolder holder, int position, ProductFirebase model) {
                holder.mProductName.setText(model.toString());
//                holder.setName(model.getWeight());
                Log.d(TAG, "onBindViewHolder: "+model.toString());
            }

            @Override
            public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                Log.d(TAG, "onCreateViewHolder: ");
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.recycler_items, parent, false);
                return new ProductHolder(view);
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
        Log.d(TAG, "onCreate: recyclerViewAdapterSet");
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

            final String productKey;
            productKey = productStorage.push().getKey();


            //Save it to FirebaseStorage
            StorageReference childRef = storageRef.child(stringFromEt(categoryEt)).child(productKey + ".jpg");

            //uploading the image
            UploadTask uploadTask = childRef.putFile(filePath);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//                    Storage Schema
//                    category
//                        --category_name1
//                          --productKey1.jpg

                    //TODO Change the schema to
//                    Category
//                         ---category_name1
//                             --productKey1
//                                ----id: productKey1
//                                ----weight: weightEt1
//                                ----imageUrl: from onSuccessUpload
//                             --productKey2
//                                ----id: productKey2
//                                ----weight: weightEt2
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
                    productFirebase.setWeight(Integer.parseInt(stringFromEt(weightEt)));
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
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
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
