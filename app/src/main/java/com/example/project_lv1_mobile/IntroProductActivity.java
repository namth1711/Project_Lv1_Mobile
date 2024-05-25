package com.example.project_lv1_mobile;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project_lv1_mobile.adapter.ProductAdapter;
import com.example.project_lv1_mobile.adapter.ProductTypeAdapter;

import com.example.project_lv1_mobile.adapter.SpinnerTypeAdapter;
import com.example.project_lv1_mobile.firebase.FirebaseCRUD;
import com.example.project_lv1_mobile.model.Member;
import com.example.project_lv1_mobile.model.PhieuNhap;
import com.example.project_lv1_mobile.model.Product;
import com.example.project_lv1_mobile.model.ProductType;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class IntroProductActivity extends AppCompatActivity {

    private Context context;
    private FirebaseFirestore firestore;

    private final String COLLECTION_PRODUCT = "PRODUCT";
    private final String COLLECTION_TYPE = "TYPE";


    private BottomNavigationView bottomNavigationIntroProduct;
    private ImageView ivImageProductFill;
    private TextView txtFillProductName, txtFillProductType, txtFIllProductUnitPrice,
            txtFillSoLuong, txtFillTrangThai;

    private FirebaseCRUD crud;

    // Nhận dữ liệu
    private Intent intent;
    private Bundle bundle;
    private String idProduct;
    private int rank;
    private String productTyeName;

    private Product product;
    private ProductType productType;

    private SpinnerTypeAdapter spinnerTypeAdapter;
    private List<ProductType> productTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_product);

        context = IntroProductActivity.this;
        firestore = FirebaseFirestore.getInstance();
        crud = new FirebaseCRUD(firestore, context);

        intent = getIntent();
        bundle = intent.getExtras();

        idProduct = bundle.getString("idProduct");
        rank = bundle.getInt("rank");
        getDataProduct();

        bottomNavigationIntroProduct = findViewById(R.id.bottomNavigationIntroProduct);
        ivImageProductFill = findViewById(R.id.ivImageProductFill);
        txtFillProductName = findViewById(R.id.txtFillProductName);
        txtFillProductType = findViewById(R.id.txtFillProductType);
        txtFIllProductUnitPrice = findViewById(R.id.txtFIllProductUnitPrice);
        txtFillSoLuong = findViewById(R.id.txtFillSoLuong);
        txtFillTrangThai = findViewById(R.id.txtFillTrangThai);

    }

    private void getDataProduct() {
        DocumentReference reference = FirebaseFirestore.getInstance()
                .collection(COLLECTION_PRODUCT).document(idProduct);
        reference.get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();
            product = snapshot.toObject(Product.class);

            getDataProductType();
        });
    }

    public void getDataProductType() {
        DocumentReference reference = FirebaseFirestore.getInstance().collection(COLLECTION_TYPE)
                .document(product.getIdProductType());
        reference.get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();

            productType = snapshot.toObject(ProductType.class);
            productTyeName = productType.getNameProductType();

            fillData();
        });

    }

    public void fillData() {
        Glide.with(context).load(product.getProductImageUri()).into(ivImageProductFill);

        txtFillProductName.setText(product.getProductName());
        txtFillProductType.setText(productTyeName);
        txtFIllProductUnitPrice.setText(Integer.toString(product.getUnitPrice()));
        txtFillSoLuong.setText(Integer.toString(product.getQuantity()));

        String trangThai = product.getStatus() == 0 ? "Kinh doanh" : "Ngừng kinh doanh";
        txtFillTrangThai.setText(trangThai);

        bottomNavigationIntroProduct.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if (item.getItemId() == R.id.itemChinhSuaSP) {
                            openDialogUpdateProduct();
                        } else if (item.getItemId() == R.id.itemDong) {
                            Intent intent = new Intent(IntroProductActivity.this,
                                    ProductActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                        return false;
                    }
                });
    }

    private void openDialogUpdateProduct() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_update_product, null);
        builder.setView(view);
        Dialog dialogDialogUp = builder.create();
        dialogDialogUp.show();

        TextView txtFillProductTypeUp = view.findViewById(R.id.txtFillProductTypeUp);
        EditText edtProductNameUp = view.findViewById(R.id.edtProductNameUp);
        EditText edtDonGiaUp = view.findViewById(R.id.edtDonGiaUp);
        TextView txtNgungKinhDoanh = view.findViewById(R.id.txtNgungKinhDoanh);
        TextView txtKinhDoanh = view.findViewById(R.id.txtKinhDoanh);
        TextView txtUpdateProductSub = view.findViewById(R.id.txtUpdateProductSub);
        TextView txtCancelUpProduct = view.findViewById(R.id.txtCancelUpProduct);
        FrameLayout frameTrangThaiSP = view.findViewById(R.id.frameTrangThaiSP);

        if (rank != 0) {
            frameTrangThaiSP.setVisibility(View.GONE);
        }

        Spinner spinnerTypeUp = view.findViewById(R.id.spinnerTypeUp);
        productTypeList = new ArrayList<>();

        spinnerTypeAdapter = new SpinnerTypeAdapter(context, productTypeList);
        listenFirebaseType(product);
        spinnerTypeUp.setAdapter(spinnerTypeAdapter);

        txtFillProductTypeUp.setText(productType.getNameProductType());
        edtProductNameUp.setText(product.getProductName());
        edtDonGiaUp.setText(Integer.toString(product.getUnitPrice()));

        spinnerTypeUp.setAdapter(spinnerTypeAdapter);

        if (product.getStatus() == 0) {
            txtKinhDoanh.setVisibility(View.GONE);
        } else if (product.getStatus() == 1) {
            txtNgungKinhDoanh.setVisibility(View.GONE);
        }

        txtKinhDoanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.
                        AlertDialog.Builder(context);
                builder.setTitle("Lưu ý");
                builder.setMessage("Xác nhận thay đổi này!");

                builder.setPositiveButton("Xác nhận", new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        product.setStatus(0);
                        crud.updateProduct(product);

                        Toast.makeText(context, "Complete", Toast.LENGTH_SHORT).show();
                        dialogDialogUp.dismiss();
                        recreate();
                    }
                });

                builder.setNegativeButton("Hủy bỏ", new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                android.app.AlertDialog dialogDisable = builder.create();
                dialogDisable.show();
            }
        });

        spinnerTypeUp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                product.setIdProductType(productTypeList.get(position).getIdType());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtNgungKinhDoanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app
                        .AlertDialog.Builder(context);
                builder.setTitle("Lưu ý");
                builder.setMessage("Xác nhận thay đổi này!");

                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        product.setStatus(1);
                        crud.updateProduct(product);

                        Toast.makeText(context, "Complete", Toast.LENGTH_SHORT).show();
                        dialogDialogUp.dismiss();
                        recreate();
                    }
                });

                builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialogDisable = builder.create();
                dialogDisable.show();
            }
        });

        txtCancelUpProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDialogUp.dismiss();
            }
        });

        txtUpdateProductSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtProductNameUp.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Không được để trống tên sản phẩm",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (edtDonGiaUp.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Không được để trống đơn giá",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                String name = edtProductNameUp.getText().toString();
                int donGia;
                try {
                    donGia = Integer.parseInt(edtDonGiaUp.getText().toString());
                    if (donGia <= 0) {
                        Toast.makeText(context, "Đơn giá phải lớn hơn 0",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException ex) {
                    Toast.makeText(context, "Giá phải là số", Toast.LENGTH_SHORT).show();
                    return;
                }

                product.setProductName(name);
                product.setUnitPrice(donGia);

                crud.updateProduct(product);
                dialogDialogUp.dismiss();
                recreate();
            }
        });

    }

    private void listenFirebaseType(Product product) {
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("TYPE")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
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
                                        for (ProductType productType : productTypeList) {
                                            if (productType.getIdType().equals(product.getIdProductType())) {
                                                productTypeList.remove(productType);
                                                productTypeList.add(0, productType);
                                                break;
                                            }
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

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Bạn có chắc muốn thoát ứng dụng?")
                .setPositiveButton("Có", (dialog, which) -> startActivity(new Intent(context, WelcomeActivity.class)))
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show();
    }
}