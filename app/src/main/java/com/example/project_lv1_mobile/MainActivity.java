package com.example.project_lv1_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private Context context;

    protected Toolbar toolBarMain;
    protected TextView txtTitleToolBarMain;
    protected ImageView iBtnTaiKhoan;
    protected FrameLayout frameLayoutMain;
    protected LinearLayout linearLayoutHome;
    protected BottomNavigationView bottomNavigationViewMain;

    protected boolean visibleItem = false;
    protected int nhapORxuat = -1;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;

        Intent intent = getIntent();
        bundle = intent.getExtras();
        int rank = bundle.getInt("rank");


        toolBarMain = findViewById(R.id.toolBarMain);
        txtTitleToolBarMain = findViewById(R.id.txtTitleToolBarMain);
        iBtnTaiKhoan = findViewById(R.id.iBtnTaiKhoan);
        frameLayoutMain = findViewById(R.id.frameLayoutMain);
        linearLayoutHome = findViewById(R.id.linearLayoutHome);
        bottomNavigationViewMain = findViewById(R.id.bottomNavigationViewMain);
        bottomNavigationViewMain.setItemIconTintList(null);

        iBtnTaiKhoan.setVisibility(View.VISIBLE);
        txtTitleToolBarMain.setText("Trang Chủ");

        Button btnQLLoaiSP = findViewById(R.id.btnQLLoaiSP);
        Button btnQLSP = findViewById(R.id.btnQLSP);
        Button btnQLTV = findViewById(R.id.btnQLTV);

        setSupportActionBar(toolBarMain);
        getSupportActionBar().setTitle("");


        if (rank == 0) {
            btnQLTV.setVisibility(View.VISIBLE);
        }

        btnQLSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnQLLoaiSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductTypeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnQLTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ManageMemberActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        iBtnTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuanLyTaiKhoanActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        bottomNavigationViewMain.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.itemHomeBottom) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.itemNhapBottom) {
                    Intent intent = new Intent(context, NhapActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.itemXuatBottom) {
                    Intent intent = new Intent(context, XuatActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.itemLichSuBottom) {
                    Intent intent = new Intent(context, LichSuActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.itemDoanhThuBottom) {
                    Intent intent = new Intent(context, DoanhThuActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.itemDangXuat) {
            startActivity(new Intent(context, LoginActivity.class));
            finish();
        } else if (item.getItemId() == R.id.itemThoat) {
            startActivity(new Intent(context, WelcomeActivity.class));
            finish();
        } else if (item.getItemId() == R.id.itemAdd) {
            if (nhapORxuat == 0){
                Intent toChonSP = new Intent(context, ChonSPNhapXuatActivity.class);
                bundle.putInt("keyNhap", 0);
                toChonSP.putExtras(bundle);
                startActivity(toChonSP);
                finish();
            } else if (nhapORxuat == 1) {
                Intent toChonSP = new Intent(context, ChonSPNhapXuatActivity.class);
                bundle.putInt("keyNhap", 1);
                toChonSP.putExtras(bundle);
                startActivity(toChonSP);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.itemAdd);

        item.setVisible(visibleItem);

        return super.onPrepareOptionsMenu(menu);
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