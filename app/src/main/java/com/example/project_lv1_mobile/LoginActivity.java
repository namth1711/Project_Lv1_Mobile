package com.example.project_lv1_mobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_lv1_mobile.model.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Context context;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private final String COLLECTION_MEMBER = "MEMBER";
    private TextInputLayout txtILayoutEmailLogin, txtILayoutPassLogin;
    private TextInputEditText txtIEdtEmailLogin, txtIEdtPassLogin;
    private Button btnLoginSubmit;
    private TextView txtForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = LoginActivity.this;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        txtILayoutEmailLogin = findViewById(R.id.txtILayoutEmailLogin);
        txtILayoutPassLogin = findViewById(R.id.txtILayoutPassLogin);

        txtIEdtEmailLogin = findViewById(R.id.txtIEdtEmailLogin);
        txtIEdtPassLogin = findViewById(R.id.txtIEdtPassLogin);

        ProgressBar progressBarLogin = findViewById(R.id.progressBarLogin);
        btnLoginSubmit = findViewById(R.id.btnLoginSubmit);
        txtForgot = findViewById(R.id.txtForgot);


        txtIEdtEmailLogin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    txtILayoutEmailLogin.setHelperText(null);
                    txtIEdtPassLogin.setEnabled(true);
                }
                return false;
            }
        });

        txtIEdtPassLogin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    txtILayoutPassLogin.setHelperText(null);

                    if (txtIEdtEmailLogin.getText().toString().trim().isEmpty()) {

                        txtIEdtPassLogin.setEnabled(false);
                        txtILayoutEmailLogin.setHelperText("Vui lòng nhập địa chỉ email");

                    } else if (!Patterns.EMAIL_ADDRESS.matcher(txtIEdtEmailLogin.getText().toString().trim()).matches()) {

                        txtIEdtPassLogin.setEnabled(false);
                        txtILayoutEmailLogin.setHelperText("Email không hợp lệ");
                    }
                }
                return false;
            }
        });


        btnLoginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean checkEmail = true;
                Boolean checkPass = true;
                if (txtIEdtPassLogin.getText().toString().trim().isEmpty()) {

                    txtIEdtPassLogin.setEnabled(true);
                    txtILayoutPassLogin.setHelperText("Vui lòng nhập mật khẩu");
                    checkPass = false;
                }

                if (txtIEdtEmailLogin.getText().toString().trim().isEmpty()) {

                    txtILayoutEmailLogin.setHelperText("Vui lòng nhập địa chỉ email");
                    checkEmail = false;

                } else if (!Patterns.EMAIL_ADDRESS.matcher(txtIEdtEmailLogin.getText().toString().trim()).matches()) {

                    txtILayoutEmailLogin.setHelperText("Email không hợp lệ");
                    checkEmail = false;
                }

                if (checkEmail == false || checkPass == false) {
                    return;
                }

                String emailLogin = txtIEdtEmailLogin.getText().toString().trim();
                String passLogin = txtIEdtPassLogin.getText().toString().trim();

                progressBarLogin.setVisibility(View.VISIBLE);
                btnLoginSubmit.setVisibility(View.INVISIBLE);

                auth.fetchSignInMethodsForEmail(emailLogin).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            List<String> signInEmail = result.getSignInMethods();

                            if (signInEmail != null && signInEmail.size() > 0) {

                                auth.signInWithEmailAndPassword(emailLogin, passLogin)
                                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    FirebaseUser getCurrentUser = auth.getCurrentUser();
                                                    String userId = getCurrentUser.getUid();

                                                    firestore.collection(COLLECTION_MEMBER).whereEqualTo("idAccount", userId)
                                                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    Member getMember = null;
                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                        Member member = document.toObject(Member.class);
                                                                        getMember = member;
                                                                    }

                                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                                    Bundle bundle = new Bundle();
                                                                    bundle.putString("idMember", getMember.getIdMember());
                                                                    bundle.putInt("rank", getMember.getRank());
                                                                    intent.putExtras(bundle);

                                                                    if (getMember.getStatus() == 1) {
                                                                        Toast.makeText(context, "Tài khoản của bạn đã bị vô hiệu hóa", Toast.LENGTH_SHORT).show();
                                                                        progressBarLogin.setVisibility(View.INVISIBLE);
                                                                        btnLoginSubmit.setVisibility(View.VISIBLE);
                                                                    } else {
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                }
                                                            });

                                                } else {
                                                    progressBarLogin.setVisibility(View.INVISIBLE);
                                                    btnLoginSubmit.setVisibility(View.VISIBLE);
                                                    Toast.makeText(context, "Thông tin tài khoản mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                progressBarLogin.setVisibility(View.INVISIBLE);
                                btnLoginSubmit.setVisibility(View.VISIBLE);
                                Toast.makeText(context, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogForgot();
            }
        });

    }

    public void openDialogForgot() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_forgot_password, null);
        builder.setView(view);
        Dialog dialogForgot = builder.create();
        dialogForgot.show();

        TextInputLayout txtILayoutEmailForgot = view.findViewById(R.id.txtILayoutEmailForgot);
        TextInputEditText txtIEdtEmailForgot = view.findViewById(R.id.txtIEdtEmailForgot);
        ProgressBar progressBarForgot = view.findViewById(R.id.progressBarForgot);
        Button btnForgotSub = view.findViewById(R.id.btnForgotSub);
        TextView txtCancelForgot = view.findViewById(R.id.txtCancelForgot);


        txtIEdtEmailForgot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    txtILayoutEmailForgot.setHelperText(null);
                }
                return false;
            }
        });

        txtCancelForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForgot.dismiss();
            }
        });

        btnForgotSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean checkEmailForgot = true;

                if (txtIEdtEmailForgot.getText().toString().trim().isEmpty()) {

                    txtILayoutEmailForgot.setHelperText("Vui lòng nhập địa chỉ email");
                    checkEmailForgot = false;

                } else if (!Patterns.EMAIL_ADDRESS.matcher(txtIEdtEmailForgot.getText().toString().trim()).matches()) {

                    txtILayoutEmailForgot.setHelperText("Email không hợp lệ");
                    checkEmailForgot = false;
                }

                if (checkEmailForgot == false) {
                    return;
                }

                String emailForgot = txtIEdtEmailForgot.getText().toString().trim();

                progressBarForgot.setVisibility(View.VISIBLE);
                btnForgotSub.setVisibility(View.INVISIBLE);

                auth.fetchSignInMethodsForEmail(emailForgot).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            List<String> forgotEmail = result.getSignInMethods();

                            if (forgotEmail != null && forgotEmail.size() > 0) {

                                auth.sendPasswordResetEmail(emailForgot).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Hệ thống đã gửi một liên kết đến email của bạn" +
                                                "vui lòng xác thực để lấy lại mật khẩu ", Toast.LENGTH_SHORT).show();
                                        dialogForgot.dismiss();
                                    }
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(context, "Đã xảy ra lỗi " + e, Toast.LENGTH_SHORT).show();
                                    progressBarForgot.setVisibility(View.INVISIBLE);
                                    btnForgotSub.setVisibility(View.VISIBLE);
                                });
                            } else {
                                progressBarForgot.setVisibility(View.INVISIBLE);
                                btnForgotSub.setVisibility(View.VISIBLE);
                                Toast.makeText(context, "Email chưa được đăng ký", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
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