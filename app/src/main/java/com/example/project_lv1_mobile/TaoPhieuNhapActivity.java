package com.example.project_lv1_mobile;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import com.example.project_lv1_mobile.adapter.ChonSPAdapter;
import com.example.project_lv1_mobile.firebase.FirebaseCRUD;
import com.example.project_lv1_mobile.model.Member;
import com.example.project_lv1_mobile.model.PhieuNhap;
import com.example.project_lv1_mobile.model.PhieuNhapChiTiet;
import com.example.project_lv1_mobile.model.Product;
import com.example.project_lv1_mobile.tempDAO.PhieuNhapChiTietDAO;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class TaoPhieuNhapActivity extends AppCompatActivity {

    private Context context;
    private List<PhieuNhapChiTiet> nhapChiTietList;
    private PhieuNhapChiTietDAO phieuNhapChiTietDAO;
    private FirebaseCRUD crud;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_phieu_nhap);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String idMember = bundle.getString("idMember");

        context = TaoPhieuNhapActivity.this;
        nhapChiTietList = new ArrayList<>();
        phieuNhapChiTietDAO = new PhieuNhapChiTietDAO(context);
        firestore = FirebaseFirestore.getInstance();
        crud = new FirebaseCRUD(firestore, context);


        TextView txtMaSoPhieuNhap = findViewById(R.id.txtMaSoPhieuNhap);
        TextView txtNguoiTaoPhieuNhap = findViewById(R.id.txtNguoiTaoPhieuNhap);
        TextView txtNgayTaoPhieu = findViewById(R.id.txtNgayTaoPhieu);
        TextView txtTongSoSPNhapSub = findViewById(R.id.txtTongSoSPNhapSub);
        TextView txtTongTienNhap = findViewById(R.id.txtTongTienNhap);

        ProgressBar progressBarPhieuNhap = findViewById(R.id.progressBarPhieuNhap);
        Button btnTaoPhieuNhap = findViewById(R.id.btnTaoPhieuNhap);
        Button btnHuyTaoPhieuNhap = findViewById(R.id.btnHuyTaoPhieuNhap);

        txtMaSoPhieuNhap.setText(UUID.randomUUID().toString());

        DocumentReference reference = firestore.collection("MEMBER")
                .document(idMember);
        reference.get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();

            Member member = snapshot.toObject(Member.class);
            txtNguoiTaoPhieuNhap.setText(member.getLastName() + " " + member.getFirtName());
        });

        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1;
        int day = date.get(Calendar.DAY_OF_MONTH);
        txtNgayTaoPhieu.setText(String.format("%d/%d/%d", day, month, year));

        int tongSoSP = 0;
        int thanhToan = 0;

        nhapChiTietList.addAll(phieuNhapChiTietDAO.selectAllPNCT(idMember));
        for (int i = 0; i < nhapChiTietList.size(); i++) {
            tongSoSP++;
            thanhToan += nhapChiTietList.get(i).getSoTien();
        }

        txtTongSoSPNhapSub.setText(Integer.toString(tongSoSP));
        txtTongTienNhap.setText(Integer.toString(thanhToan));

        btnTaoPhieuNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Xác nhận toàn bộ thông tin là chính xác");

                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        progressBarPhieuNhap.setVisibility(View.VISIBLE);
                        btnTaoPhieuNhap.setVisibility(View.INVISIBLE);

                        String idPhieuNhap = txtMaSoPhieuNhap.getText().toString();
                        String memberName = txtNguoiTaoPhieuNhap.getText().toString();
                        String ngayNhap = txtNgayTaoPhieu.getText().toString();
                        int tongSoSPNhap = Integer.parseInt(txtTongSoSPNhapSub.getText().toString());
                        int tongTien = Integer.parseInt(txtTongTienNhap.getText().toString());
                        PhieuNhap phieuNhap = new PhieuNhap(idPhieuNhap, idMember, memberName, ngayNhap, tongSoSPNhap, tongTien);
                        crud.taoPhieuNhap(phieuNhap);

                        for (int i = 0; i < nhapChiTietList.size(); i++) {
                            PhieuNhapChiTiet phieuNhapChiTiet = new PhieuNhapChiTiet();
                            phieuNhapChiTiet.setIdPhieuNhapCT(nhapChiTietList.get(i).getIdPhieuNhapCT());
                            phieuNhapChiTiet.setIdPhieuNhap(idPhieuNhap);
                            phieuNhapChiTiet.setSoLuongNhap(nhapChiTietList.get(i).getSoLuongNhap());
                            phieuNhapChiTiet.setSoTien(nhapChiTietList.get(i).getSoTien());
                            int quantity = nhapChiTietList.get(i).getSoLuongNhap();
                            DocumentReference reference = firestore.collection("PRODUCT")
                                    .document(nhapChiTietList.get(i).getIdProduct());
                            reference.get().addOnCompleteListener(task -> {
                                DocumentSnapshot snapshot = task.getResult();

                                Product product = snapshot.toObject(Product.class);
                                phieuNhapChiTiet.setTenSP(product.getProductName());
                                crud.taoPhieuNhapChiTiet(phieuNhapChiTiet);
                                product.setQuantity(product.getQuantity() + quantity);
                                crud.updateProduct(product);
                            });
                        }

                        AlertDialog.Builder builderTB = new AlertDialog.Builder(context);
                        LayoutInflater inflater = getLayoutInflater();
                        View view = inflater.inflate(R.layout.item_thong_bao_nhap_hang_thanh_cong, null);
                        builderTB.setView(view);
                        Dialog dialogThongBao = builderTB.create();
                        dialogThongBao.show();

                        phieuNhapChiTietDAO.completeInsertFirebase(idMember);

                        Button btnCancelItemThongBaoNhap = view.findViewById(R.id.btnCancelItemThongBaoNhap);
                        btnCancelItemThongBaoNhap.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(TaoPhieuNhapActivity.this, NhapActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                dialogThongBao.dismiss();
                                finish();
                            }
                        });
                    }
                });

                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBarPhieuNhap.setVisibility(View.INVISIBLE);
                        btnTaoPhieuNhap.setVisibility(View.VISIBLE);
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        btnHuyTaoPhieuNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaoPhieuNhapActivity.this, NhapActivity.class);
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