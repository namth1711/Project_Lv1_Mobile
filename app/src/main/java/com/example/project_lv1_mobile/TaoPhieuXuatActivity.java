package com.example.project_lv1_mobile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_lv1_mobile.firebase.FirebaseCRUD;
import com.example.project_lv1_mobile.model.Member;
import com.example.project_lv1_mobile.model.PhieuXuat;
import com.example.project_lv1_mobile.model.PhieuXuatChiTiet;
import com.example.project_lv1_mobile.model.Product;
import com.example.project_lv1_mobile.tempDAO.PhieuXuatChiTietDAO;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class TaoPhieuXuatActivity extends AppCompatActivity {

    private Context context;
    private List<PhieuXuatChiTiet> xuatChiTietList;
    private PhieuXuatChiTietDAO phieuXuatChiTietDAO;
    private FirebaseCRUD crud;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_phieu_xuat);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String idMember = bundle.getString("idMember");

        context = TaoPhieuXuatActivity.this;
        xuatChiTietList = new ArrayList<>();
        phieuXuatChiTietDAO = new PhieuXuatChiTietDAO(context);
        firestore = FirebaseFirestore.getInstance();
        crud = new FirebaseCRUD(firestore, context);

        TextView txtMaSoPhieuXuat = findViewById(R.id.txtMaSoPhieuXuat);
        TextView txtNguoiTaoPhieuXuat = findViewById(R.id.txtNguoiTaoPhieuXuat);
        TextView txtNgayXuatPhieu = findViewById(R.id.txtNgayXuatPhieu);
        TextView txtTongSoSPXuatSub = findViewById(R.id.txtTongSoSPXuatSub);
        TextView txtTongTienXuatTruoc = findViewById(R.id.txtTongTienXuatTruoc);
        TextView txtTongTienXuatSau = findViewById(R.id.txtTongTienXuatSau);

        ProgressBar progressBarPhieuXuat = findViewById(R.id.progressBarPhieuXuat);
        Button btnTaoPhieuXuat = findViewById(R.id.btnTaoPhieuXuat);
        Button btnHuyTaoPhieuXuat = findViewById(R.id.btnHuyTaoPhieuXuat);

        txtMaSoPhieuXuat.setText(UUID.randomUUID().toString());

        DocumentReference reference = firestore.collection("MEMBER")
                .document(idMember);
        reference.get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();

            Member member = snapshot.toObject(Member.class);
            txtNguoiTaoPhieuXuat.setText(member.getLastName() + " " + member.getFirtName());
        });

        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1;
        int day = date.get(Calendar.DAY_OF_MONTH);
        txtNgayXuatPhieu.setText(String.format("%d/%d/%d", day, month, year));

        int tongSoSP = 0;
        int thanhToan = 0;

        xuatChiTietList.addAll(phieuXuatChiTietDAO.selectAllPXCT(idMember));
        for (int i = 0; i < xuatChiTietList.size(); i++) {
            tongSoSP++;
            thanhToan += xuatChiTietList.get(i).getSoTien();
        }

        txtTongSoSPXuatSub.setText(Integer.toString(tongSoSP));
        txtTongTienXuatTruoc.setText(Integer.toString(thanhToan));
        int thue = (int) (thanhToan * 0.1);
        int sauThue = (int) (thanhToan + thue);
        txtTongTienXuatSau.setText(Integer.toString(sauThue));

        btnTaoPhieuXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Xác nhận toàn bộ thông tin là chính xác");

                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        progressBarPhieuXuat.setVisibility(View.VISIBLE);
                        btnTaoPhieuXuat.setVisibility(View.INVISIBLE);

                        String idPhieuXuat = txtMaSoPhieuXuat.getText().toString();
                        String memberName = txtNguoiTaoPhieuXuat.getText().toString();
                        String ngayXuat = txtNgayXuatPhieu.getText().toString();
                        int tongSoSPXuat = Integer.parseInt(txtTongSoSPXuatSub.getText().toString());
                        int tongTien = Integer.parseInt(txtTongTienXuatSau.getText().toString());
                        PhieuXuat phieuXuat = new PhieuXuat(idPhieuXuat, idMember, memberName, ngayXuat, tongSoSPXuat, tongTien, thue);
                        crud.taoPhieuXuat(phieuXuat);

                        for (int i = 0; i < xuatChiTietList.size(); i++) {
                            PhieuXuatChiTiet phieuXuatChiTiet = new PhieuXuatChiTiet();
                            phieuXuatChiTiet.setIdPhieuXuatCT(xuatChiTietList.get(i).getIdPhieuXuatCT());
                            phieuXuatChiTiet.setIdPhieuXuat(idPhieuXuat);
                            phieuXuatChiTiet.setSoLuongXuat(xuatChiTietList.get(i).getSoLuongXuat());
                            phieuXuatChiTiet.setSoTien(xuatChiTietList.get(i).getSoTien());
                            int quantity = xuatChiTietList.get(i).getSoLuongXuat();
                            DocumentReference reference = firestore.collection("PRODUCT")
                                    .document(xuatChiTietList.get(i).getIdProduct());
                            reference.get().addOnCompleteListener(task -> {
                                DocumentSnapshot snapshot = task.getResult();

                                Product product = snapshot.toObject(Product.class);
                                phieuXuatChiTiet.setTenSP(product.getProductName());
                                crud.taoPhieuXuatChiTiet(phieuXuatChiTiet);
                                product.setQuantity(product.getQuantity() - quantity);
                                crud.updateProduct(product);
                            });

                            AlertDialog.Builder builderTB = new AlertDialog.Builder(context);
                            LayoutInflater inflater = getLayoutInflater();
                            View view = inflater.inflate(R.layout.item_thong_bao_nhap_hang_thanh_cong, null);
                            builderTB.setView(view);
                            Dialog dialogThongBao = builderTB.create();
                            dialogThongBao.show();

                            phieuXuatChiTietDAO.completeInsertFirebase(idMember);

                            Button btnCancelItemThongBaoNhap = view.findViewById(R.id.btnCancelItemThongBaoNhap);
                            btnCancelItemThongBaoNhap.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(TaoPhieuXuatActivity.this, XuatActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    dialogThongBao.dismiss();
                                    finish();
                                }
                            });
                        }
                    }
                });

                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBarPhieuXuat.setVisibility(View.INVISIBLE);
                        btnTaoPhieuXuat.setVisibility(View.VISIBLE);
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btnHuyTaoPhieuXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaoPhieuXuatActivity.this, XuatActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

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