package com.example.project_lv1_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.project_lv1_mobile.adapter.SearchProductAdapter;
import com.example.project_lv1_mobile.model.Product;
import com.example.project_lv1_mobile.tempDAO.PhieuNhapChiTietDAO;
import com.example.project_lv1_mobile.tempDAO.PhieuXuatChiTietDAO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class ChonSPNhapXuatActivity extends AppCompatActivity {

    private Context context;
    private List<Product> productList;
    private List<Product> searchList;
    private List<Product> saveList;
    private FirebaseFirestore firestore;
    private SearchProductAdapter adapter;
    private final String COLLECTION_PRODUCT = "PRODUCT";
    private PhieuNhapChiTietDAO phieuNhapChiTietDAO;
    private PhieuXuatChiTietDAO phieuXuatChiTietDAO;
    private RecyclerView recyclerDanhSachSP;
    private ImageButton iBtnThoatChonSP;

    private SearchView seachView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_spnhap_xuat);

        Bundle bundle = getIntent().getExtras();

        int checkRank = bundle.getInt("keyNhap");
        String idMember = bundle.getString("idMember");

        context = ChonSPNhapXuatActivity.this;
        productList = new ArrayList<>();
        searchList = new ArrayList<>();
        saveList = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();
        phieuNhapChiTietDAO = new PhieuNhapChiTietDAO(context);
        phieuXuatChiTietDAO = new PhieuXuatChiTietDAO(context);
        recyclerDanhSachSP = findViewById(R.id.recyclerDanhSachSP);
        iBtnThoatChonSP = findViewById(R.id.iBtnThoatChonSP);
        seachView = findViewById(R.id.seachView);

        TextView txtTimKiem = findViewById(R.id.txtTimKiem);

        adapter = new SearchProductAdapter(context, productList, phieuNhapChiTietDAO, phieuXuatChiTietDAO, checkRank, idMember);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerDanhSachSP.setLayoutManager(manager);
        recyclerDanhSachSP.setAdapter(adapter);

        txtTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTimKiem.setVisibility(View.INVISIBLE);
                seachView.setVisibility(View.VISIBLE);
            }
        });

        seachView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    exitSeacrh();
                } else {
                    fillQuery(newText);
                }
                return true;
            }
        });

        seachView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                txtTimKiem.setVisibility(View.VISIBLE);
                seachView.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        loadDataProduct();

        iBtnThoatChonSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkRank == 0) {
                    Intent intent = new Intent(ChonSPNhapXuatActivity.this, NhapActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else if (checkRank == 1) {
                    Intent intent = new Intent(ChonSPNhapXuatActivity.this, XuatActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void loadDataProduct() {
        firestore.collection(COLLECTION_PRODUCT).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                productList.clear();
                saveList.clear();
                for (DocumentSnapshot snapshot : task.getResult()) {
                    Product product = snapshot.toObject(Product.class);
                    if (product.getStatus() == 0) {
                        productList.add(product);
                        saveList.add(product);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void fillQuery(String query) {
        searchList.clear();
        for (Product item : productList) {
            if (containsIgnoreCaseAndAccent(item.getProductName(), query)) {
                searchList.add(item);
            }
        }
        adapter.fillSearch(searchList);
    }

    public void exitSeacrh() {
        productList.clear();
        productList.addAll(saveList);
        adapter.notifyDataSetChanged();
    }

    private boolean containsIgnoreCaseAndAccent(String original, String query) {
        String normalizedOriginal = Normalizer.normalize(original, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();

        String normalizedQuery = Normalizer.normalize(query, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();

        return normalizedOriginal.contains(normalizedQuery);
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có chắc muốn thoát ứng dụng?")
                .setPositiveButton("Có", (dialog, which) -> startActivity(new Intent(context, WelcomeActivity.class)))
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show();
    }
}