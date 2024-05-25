package com.example.project_lv1_mobile;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_lv1_mobile.adapter.SpinnerTypeAdapter;
import com.example.project_lv1_mobile.firebase.FirebaseCRUD;
import com.example.project_lv1_mobile.fragment.FragmentKinhDoanh;
import com.example.project_lv1_mobile.fragment.FragmentNgungKinhDoanh;
import com.example.project_lv1_mobile.model.Product;
import com.example.project_lv1_mobile.model.ProductType;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
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

public class ProductActivity extends AppCompatActivity {

    private Context context;
    private List<ProductType> productTypeList;
    private SpinnerTypeAdapter spinnerTypeAdapter;
    private List<Product> productList;
    private FirebaseCRUD crud;

    //  Firebase
    private final String COLLECTION_TYPE = "TYPE";
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference imageRef;
    private Uri imageUri;
    private ActivityResultLauncher<String> imageProductLauncher;

    //  widget
    private BottomNavigationView bottomNavigationProduct;
    private FloatingActionButton floatBtnAddProduct;

    private ImageView ivAddImageProduct;
    private EditText edtAddTenSP;
    private EditText edtAddDonGia;
    private Spinner spinnerType;
    private TextView txtCancelAddProduct;
    private ProgressBar progressBarAddProduct;
    private Button btnAddProductSub;
    private String idType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        context = ProductActivity.this;
        productList = new ArrayList<>();
        productTypeList = new ArrayList<>();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        bottomNavigationProduct = findViewById(R.id.bottomNavigationProduct);
        floatBtnAddProduct = findViewById(R.id.floatBtnAddProduct);
        ImageButton iBtnExitQLSP = findViewById(R.id.iBtnExitQLSP);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        imageRef = storageReference.child("imagesProduct/" + UUID.randomUUID().toString());

        crud = new FirebaseCRUD(firestore, context);

        imageProductLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            imageUri = result;
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                                ivAddImageProduct.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        bottomNavigationProduct.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.itemKinhDoanh) {
                    FragmentKinhDoanh kinhDoanh = new FragmentKinhDoanh();
                    kinhDoanh.setArguments(bundle);
                    rePlaceFrag(kinhDoanh);
                } else if (item.getItemId() == R.id.itemNgungKinhDoanh) {
                    FragmentNgungKinhDoanh ngungKinhDoanh = new FragmentNgungKinhDoanh();
                    ngungKinhDoanh.setArguments(bundle);
                    rePlaceFrag(ngungKinhDoanh);
                }

                return false;
            }
        });

        floatBtnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogAddProduct();
            }
        });

        bottomNavigationProduct.setSelectedItemId(R.id.itemKinhDoanh);

        iBtnExitQLSP.setOnClickListener(new View.OnClickListener() {
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
                                spinnerTypeAdapter.notifyDataSetChanged();
                                break;
                            case MODIFIED:  //  Khi có 1 document được cập nhật
                                spinnerTypeAdapter.notifyDataSetChanged();
                                break;
                            case REMOVED:   // Khi có 1 document bị xóa khỏi collection
                                documentChange.getDocument().toObject(ProductType.class);
                                productTypeList.remove(documentChange.getOldIndex());
                                spinnerTypeAdapter.notifyDataSetChanged();
                                break;
                        }
                    }
                }
            }
        });
    }

    private void rePlaceFrag(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frameProduct, fragment).commit();
    }

    private void openDialogAddProduct() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_add_product, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        ivAddImageProduct = view.findViewById(R.id.ivAddImageProduct);
        edtAddTenSP = view.findViewById(R.id.edtAddTenSP);
        edtAddDonGia = view.findViewById(R.id.edtAddDonGia);
        spinnerType = view.findViewById(R.id.spinnerType);
        txtCancelAddProduct = view.findViewById(R.id.txtCancelAddProduct);
        progressBarAddProduct = view.findViewById(R.id.progressBarAddProduct);
        btnAddProductSub = view.findViewById(R.id.btnAddProductSub);

        spinnerTypeAdapter = new SpinnerTypeAdapter(context, productTypeList);
        listenFirebaseType();
        spinnerType.setAdapter(spinnerTypeAdapter);

        ivAddImageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageProductLauncher.launch("image/*");
            }
        });

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idType = productTypeList.get(position).getIdType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAddProductSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtAddTenSP.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Trống tên sản phẩm", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtAddDonGia.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Trống đơn giá sản phẩm", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (imageUri == null) {
                    Toast.makeText(context, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
                    return;
                }
                String tenSP = edtAddTenSP.getText().toString();
                String idSP = UUID.randomUUID().toString();
                int gia;
                try {
                    gia = Integer.parseInt(edtAddDonGia.getText().toString());
                    if (gia <= 0) {
                        Toast.makeText(context, "Giá phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(context, "Giá phải là số", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBarAddProduct.setVisibility(View.VISIBLE);
                btnAddProductSub.setVisibility(View.INVISIBLE);

                UploadTask uploadTask = imageRef.putFile(imageUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> downloadUrlTask = imageRef.getDownloadUrl();
                        downloadUrlTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageProduct = uri.toString();

                                Product product = new Product(idSP, idType, tenSP, imageProduct, 0, gia, 0);
                                crud.addProduct(product);
                                dialog.dismiss();
                                recreate();
                            }
                        }).addOnFailureListener(e -> {
                            Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
                            progressBarAddProduct.setVisibility(View.INVISIBLE);
                            btnAddProductSub.setVisibility(View.VISIBLE);
                        });
                    }
                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                });
            }
        });

        txtCancelAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


}