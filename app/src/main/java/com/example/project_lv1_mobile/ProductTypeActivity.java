package com.example.project_lv1_mobile;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_lv1_mobile.adapter.ProductTypeAdapter;
import com.example.project_lv1_mobile.firebase.FirebaseCRUD;
import com.example.project_lv1_mobile.model.ProductType;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductTypeActivity extends AppCompatActivity {

    private Context context;
    private List<ProductType> productTypeList;
    private ProductTypeAdapter adapter;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference imageRef;
    private FirebaseCRUD crud;
    private final String COLLECTION_TYPE = "TYPE";

    private FloatingActionButton floatBtnAddType;
    private RecyclerView recyclerType;
    private ImageView ivImageType;

    private Uri imageUri;

    private ActivityResultLauncher<String> imageProductTypeLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_type);

        context = ProductTypeActivity.this;
        productTypeList = new ArrayList<>();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String idMember = bundle.getString("idMember");

        floatBtnAddType = findViewById(R.id.floatBtnAddType);
        recyclerType = findViewById(R.id.recyclerType);
        ImageButton iBtnExitQLLoaiSP = findViewById(R.id.iBtnExitQLLoaiSP);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        imageRef = storageReference.child("imagesType/" + UUID.randomUUID().toString());

        crud = new FirebaseCRUD(firestore, context);

        listenFirebaseType();

        imageProductTypeLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            imageUri = result;
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                                ivImageType.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerType.setLayoutManager(manager);

        adapter = new ProductTypeAdapter(context, productTypeList, crud, idMember);
        recyclerType.setAdapter(adapter);

        floatBtnAddType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogAddType();
            }
        });

        iBtnExitQLLoaiSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

    }

    private void listenFirebaseType() {
        firestore.collection(COLLECTION_TYPE).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e(TAG, "Fail", error);
                    return;
                }
                if (value != null) {
                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        switch (documentChange.getType()) {
                            case ADDED: //  Khi chỉ có document được thêm
                                productTypeList.clear();
                                for (DocumentSnapshot snapshot : value.getDocuments()) {
                                    ProductType type = snapshot.toObject(ProductType.class);
                                    productTypeList.add(type);
                                }
                                adapter.notifyDataSetChanged();
                                break;
                            case MODIFIED:  //  Khi có 1 document được cập nhật
                                adapter.notifyDataSetChanged();
                                break;
                            case REMOVED:   // Khi có 1 document bị xóa khỏi collection
                                documentChange.getDocument().toObject(ProductType.class);
                                productTypeList.remove(documentChange.getOldIndex());
                                adapter.notifyDataSetChanged();
                                break;
                        }
                    }
                }
            }
        });
    }

    public void openDialogAddType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_add_type, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        ivImageType = view.findViewById(R.id.ivImageType);
        Button btnAddTypeSub = view.findViewById(R.id.btnAddTypeSub);
        TextView txtCancelAddType = view.findViewById(R.id.txtCancelAddType);
        EditText edtAddNameType = view.findViewById(R.id.edtAddNameType);
        ProgressBar progressBarAddType = view.findViewById(R.id.progressBarAddType);

        ivImageType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageProductTypeLauncher.launch("image/*");
            }
        });

        btnAddTypeSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtAddNameType.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập tên loại sản phẩm", Toast.LENGTH_SHORT).show();
                    return;
                }
                String typeName = edtAddNameType.getText().toString();
                String idType = UUID.randomUUID().toString();
                if (imageUri == null) {
                    Toast.makeText(context, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBarAddType.setVisibility(View.VISIBLE);
                btnAddTypeSub.setVisibility(View.INVISIBLE);

                UploadTask uploadTask = imageRef.putFile(imageUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Lấy URL của ảnh tải lên từ taskSnapshot
                        Task<Uri> downloadUrlTask = imageRef.getDownloadUrl();
                        downloadUrlTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageType = uri.toString();

                                ProductType type = new ProductType(idType, typeName, imageType);
                                crud.addProductType(type);
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
                                progressBarAddType.setVisibility(View.VISIBLE);
                                btnAddTypeSub.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        txtCancelAddType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}