package com.example.project_lv1_mobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project_lv1_mobile.firebase.FirebaseCRUD;
import com.example.project_lv1_mobile.model.Member;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class QuanLyTaiKhoanActivity extends AppCompatActivity {

    private Context context;
    private ImageButton iBtnExitQLTK;
    private ImageView ivAvatarTaiKhoan;
    private TextView txtTenTaiKhoan, txtFullNameTaiKhoan, txtGioiTinhTaiKhoan, txtEmailTaiKhoan;
    private LinearLayout llUpdateTaiKhoan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_tai_khoan);

        context = QuanLyTaiKhoanActivity.this;

        iBtnExitQLTK = findViewById(R.id.iBtnExitQLTK);
        ivAvatarTaiKhoan = findViewById(R.id.ivAvatarTaiKhoan);
        txtTenTaiKhoan = findViewById(R.id.txtTenTaiKhoan);
        txtFullNameTaiKhoan = findViewById(R.id.txtFullNameTaiKhoan);
        txtGioiTinhTaiKhoan = findViewById(R.id.txtGioiTinhTaiKhoan);
        txtEmailTaiKhoan = findViewById(R.id.txtEmailTaiKhoan);
        llUpdateTaiKhoan = findViewById(R.id.llUpdateTaiKhoan);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String idMember = bundle.getString("idMember");

        DocumentReference reference = FirebaseFirestore.getInstance().collection("MEMBER").document(idMember);
        reference.get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();
            Member member = snapshot.toObject(Member.class);
            fillIntroAccount(member);

            llUpdateTaiKhoan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateAccount(member);
                }
            });

        });

        iBtnExitQLTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fillIntroAccount(Member member) {

        if (member.getImageMember().equals("0")) {
            ivAvatarTaiKhoan.setImageResource(R.drawable.addmin_icon);
        } else if (member.getImageMember().equals("1")) {
            if (member.getGender().equals("Nam")) {
                ivAvatarTaiKhoan.setImageResource(R.drawable.male_icon);
            } else if (member.getGender().equals("Nữ")) {
                ivAvatarTaiKhoan.setImageResource(R.drawable.female_icon);
            }
        } else {
            Glide.with(this).load(member.getImageMember()).into(ivAvatarTaiKhoan);
        }

        txtTenTaiKhoan.setText(member.getFirtName());
        txtFullNameTaiKhoan.setText(member.getLastName() + " " + member.getFirtName());
        txtGioiTinhTaiKhoan.setText(member.getGender());
        txtEmailTaiKhoan.setText(member.getEmail());
    }

    private void updateAccount(Member member) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.item_lua_chon_chinh_sua_tai_khoan, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        Button btnChinhSuaThongTin = view.findViewById(R.id.btnChinhSuaThongTin);
        Button btnDoiMatKhau = view.findViewById(R.id.btnDoiMatKhau);
        TextView txtCancelLuaChonEditTK = view.findViewById(R.id.txtCancelLuaChonEditTK);

        btnChinhSuaThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateIntroAccount(member);
            }
        });

        btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassWordAccount(member.getEmail());
            }
        });

        txtCancelLuaChonEditTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void updateIntroAccount(Member member) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.item_chinh_sua_tai_khoan, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        TextInputLayout txtILayoutUpHo = view.findViewById(R.id.txtILayoutUpHo);
        TextInputEditText txtIEdtUpHo = view.findViewById(R.id.txtIEdtUpHo);
        TextInputLayout txtILayoutUpTen = view.findViewById(R.id.txtILayoutUpTen);
        TextInputEditText txtIEdtUpTen = view.findViewById(R.id.txtIEdtUpTen);
        EditText edtUpGioiTinh = view.findViewById(R.id.edtUpGioiTinh);
        ProgressBar progressBarUpAccount = view.findViewById(R.id.progressBarUpAccount);
        Button btnUpAccountSubmit = view.findViewById(R.id.btnUpAccountSubmit);
        TextView txtCancelUpAccount = view.findViewById(R.id.txtCancelUpAccount);

        txtIEdtUpHo.setText(member.getLastName());
        txtIEdtUpTen.setText(member.getFirtName());
        edtUpGioiTinh.setText(member.getGender());

        edtUpGioiTinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gioiTinh[] = {"Nam", "Nữ"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Giới tính");
                builder.setItems(gioiTinh, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edtUpGioiTinh.setText(gioiTinh[which]);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        txtIEdtUpHo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    txtILayoutUpHo.setHelperText(null);
                }
                return false;
            }
        });

        txtIEdtUpTen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    txtILayoutUpTen.setHelperText(null);
                }
                return false;
            }
        });

        btnUpAccountSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean checkHo = true;
                Boolean checkTen = true;

                if (txtIEdtUpHo.getText().toString().isEmpty()) {
                    txtILayoutUpHo.setHelperText("Trống họ");
                    checkHo = false;
                }

                if (txtIEdtUpTen.getText().toString().isEmpty()) {
                    txtILayoutUpTen.setHelperText("Trống tên");
                    checkTen = false;
                }

                if (checkHo == false || checkTen == false) {
                    return;
                }

                progressBarUpAccount.setVisibility(View.VISIBLE);
                btnUpAccountSubmit.setVisibility(View.INVISIBLE);

                member.setLastName(txtIEdtUpHo.getText().toString());
                member.setFirtName(txtIEdtUpTen.getText().toString());
                member.setGender(edtUpGioiTinh.getText().toString());

                FirebaseCRUD crud = new FirebaseCRUD(FirebaseFirestore.getInstance(), context);
                crud.updateMember(member);
                recreate();
                dialog.dismiss();
            }
        });

        txtCancelUpAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void updatePassWordAccount(String emailAccount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.item_doi_mat_khau, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        LinearLayout llXacThucTK = view.findViewById(R.id.llXacThucTK);
        TextInputLayout txtILayoutPassOld = view.findViewById(R.id.txtILayoutPassOld);
        TextInputEditText txtIEdtPassOld = view.findViewById(R.id.txtIEdtPassOld);
        ProgressBar progressBarXacThuc = view.findViewById(R.id.progressBarXacThuc);
        Button btnXacThuc = view.findViewById(R.id.btnXacThuc);

        LinearLayout llDoiMK = view.findViewById(R.id.llDoiMK);
        TextInputLayout txtILayoutPassNew = view.findViewById(R.id.txtILayoutPassNew);
        TextInputEditText txtIEdtPassNew = view.findViewById(R.id.txtIEdtPassNew);
        TextInputLayout txtILayoutPassNewCon = view.findViewById(R.id.txtILayoutPassNewCon);
        TextInputEditText txtIEdtPassNewCon = view.findViewById(R.id.txtIEdtPassNewCon);
        ProgressBar progressBarXacNhan = view.findViewById(R.id.progressBarXacNhan);
        Button btnXacNhan = view.findViewById(R.id.btnXacNhan);

        TextView txtCancelEditPassword = view.findViewById(R.id.txtCancelEditPassword);

        txtIEdtPassOld.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    txtILayoutPassOld.setHelperText(null);
                }
                return false;
            }
        });

        txtIEdtPassNew.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    txtILayoutPassNew.setHelperText(null);
                }

                return false;
            }
        });

        txtIEdtPassNewCon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    txtILayoutPassNewCon.setHelperText(null);
                }

                return false;
            }
        });

        btnXacThuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean checkPassOld = true;
                if (txtIEdtPassOld.getText().toString().trim().isEmpty()) {

                    txtILayoutPassOld.setHelperText("Nhập mật khẩu tài khoản");
                    checkPassOld = false;
                }

                if (checkPassOld == false) {
                    return;
                }

                progressBarXacThuc.setVisibility(View.VISIBLE);
                btnXacThuc.setVisibility(View.INVISIBLE);

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String email = emailAccount;
                String currentPassword = txtIEdtPassOld.getText().toString().trim();

                mAuth.signInWithEmailAndPassword(email, currentPassword)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                llXacThucTK.setVisibility(View.GONE);
                                llDoiMK.setVisibility(View.VISIBLE);

                                btnXacNhan.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Boolean checkPassNew = true;
                                        Boolean checkPassNewCon = true;

                                        if (txtIEdtPassNew.getText().toString().trim().isEmpty()) {
                                            txtILayoutPassNew.setHelperText("Nhập mật khẩu mới");
                                            checkPassNew = false;
                                        } else if (txtIEdtPassNew.getText().toString().trim().length() <= 8) {
                                            txtILayoutPassNew.setHelperText("Mật khẩu quá ngắn");
                                            checkPassNew = false;
                                        }

                                        if (txtIEdtPassNewCon.getText().toString().trim().equals(txtIEdtPassNew.getText().toString().trim())) {

                                        } else {
                                            txtILayoutPassNewCon.setHelperText("Xác nhận không khớp");
                                            checkPassNewCon = false;
                                        }

                                        if (checkPassNew == false || checkPassNewCon == false) {
                                            return;
                                        }

                                        progressBarXacNhan.setVisibility(View.VISIBLE);
                                        btnXacNhan.setVisibility(View.INVISIBLE);

                                        String newPassword = txtIEdtPassNew.getText().toString().trim();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null) {
                                            user.updatePassword(newPassword)
                                                    .addOnCompleteListener(task1 -> {
                                                        if (task1.isSuccessful()) {
                                                            Toast.makeText(QuanLyTaiKhoanActivity.this, "Thay đổi mật khẩu thành công, Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(QuanLyTaiKhoanActivity.this, LoginActivity.class));
                                                            finish();
                                                        } else {
                                                            Toast.makeText(QuanLyTaiKhoanActivity.this, "Lỗi mạng! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                                            progressBarXacNhan.setVisibility(View.VISIBLE);
                                                            btnXacNhan.setVisibility(View.INVISIBLE);
                                                        }
                                                    });
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(context, "Mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                                progressBarXacThuc.setVisibility(View.INVISIBLE);
                                btnXacThuc.setVisibility(View.VISIBLE);
                            }
                        });
            }
        });

        txtCancelEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}