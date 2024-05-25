package com.example.project_lv1_mobile;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_lv1_mobile.adapter.MemberAdapter;
import com.example.project_lv1_mobile.firebase.FirebaseCRUD;
import com.example.project_lv1_mobile.model.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class ManageMemberActivity extends AppCompatActivity {

    private Context context;
    private List<Member> memberList;
    private MemberAdapter adapter;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private FirebaseAuth auth;
    private StorageReference storageReference;
    private StorageReference imageRef;
    private FirebaseCRUD crud;

    private final String COLLECTION_MEMBER = "MEMBER";

    private FloatingActionButton floatBtnAddMember;
    private RecyclerView recyclerMember;

    private Uri imageUri;
    private Bitmap bitmap;
    private ActivityResultLauncher<String> imageMemberLauncher;

    private ImageView ivAddImageMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_member);

        context = ManageMemberActivity.this;
        memberList = new ArrayList<>();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        floatBtnAddMember = findViewById(R.id.floatBtnAddMember);
        recyclerMember = findViewById(R.id.recyclerMember);
        ImageButton iBtnExitQLTV = findViewById(R.id.iBtnExitQLTV);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        imageRef = storageReference.child("imagesMember/" + UUID.randomUUID().toString());

        crud = new FirebaseCRUD(firestore, context);

        listenFirebaseMember();

        imageMemberLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            imageUri = result;
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                                ivAddImageMember.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerMember.setLayoutManager(manager);

        adapter = new MemberAdapter(context, memberList, crud);

        recyclerMember.setAdapter(adapter);

        floatBtnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Open Dialog add
                openDialogAddMember();
            }
        });

        iBtnExitQLTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    private void listenFirebaseMember() {
        firestore.collection(COLLECTION_MEMBER).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                memberList.clear();
                                for (DocumentSnapshot snapshot : value.getDocuments()) {
                                    Member member = snapshot.toObject(Member.class);
                                    if (member.getRank() != 0) {
                                        memberList.add(member);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                break;
                            case MODIFIED:  //  Khi có 1 document được cập nhật
                                adapter.notifyDataSetChanged();
                                break;
                            case REMOVED:   // Khi có 1 document bị xóa khỏi collection
                                documentChange.getDocument().toObject(Member.class);
                                memberList.remove(documentChange.getOldIndex());
                                adapter.notifyDataSetChanged();
                                break;
                        }
                    }
                }
            }
        });
    }

    public void openDialogAddMember() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_add_member, null);
        builder.setView(view);
        Dialog dialogAddMember = builder.create();
        dialogAddMember.show();

        ivAddImageMember = view.findViewById(R.id.ivAddImageMember);

        TextInputLayout txtILayoutAddHo = view.findViewById(R.id.txtILayoutAddHo);
        TextInputEditText txtIEdtAddHo = view.findViewById(R.id.txtIEdtAddHo);

        TextInputLayout txtILayoutAddTen = view.findViewById(R.id.txtILayoutAddTen);
        TextInputEditText txtIEdtAddTen = view.findViewById(R.id.txtIEdtAddTen);

        TextInputLayout txtILayoutAddEmail = view.findViewById(R.id.txtILayoutAddEmail);
        TextInputEditText txtIEdtAddEmail = view.findViewById(R.id.txtIEdtAddEmail);

        EditText edtAddGioiTinh = view.findViewById(R.id.edtAddGioiTinh);

        TextInputLayout txtILayoutAddPass = view.findViewById(R.id.txtILayoutAddPass);
        TextInputEditText txtIEdtAddPass = view.findViewById(R.id.txtIEdtAddPass);

        TextInputLayout txtILayoutAddPassCon = view.findViewById(R.id.txtILayoutAddPassCon);
        TextInputEditText txtIEdtAddPassCon = view.findViewById(R.id.txtIEdtAddPassCon);

        ProgressBar progressBarAddMember = view.findViewById(R.id.progressBarAddMember);
        Button btnAddMemberSubmit = view.findViewById(R.id.btnAddMemberSubmit);
        TextView txtCancelAddMember = view.findViewById(R.id.txtCancelAddMember);

        ivAddImageMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageMemberLauncher.launch("image/*");
            }
        });

        txtIEdtAddHo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    txtILayoutAddHo.setHelperText(null);
                }
                return false;
            }
        });

        txtIEdtAddTen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    txtILayoutAddTen.setHelperText(null);
                }
                return false;
            }
        });

        txtIEdtAddEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    txtILayoutAddEmail.setHelperText(null);
                }
                return false;
            }
        });

        txtIEdtAddPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    txtILayoutAddPass.setHelperText(null);
                }
                return false;
            }
        });

        txtIEdtAddPassCon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    txtILayoutAddPassCon.setHelperText(null);
                }
                return false;
            }
        });


        edtAddGioiTinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gioiTinh[] = {"Nam", "Nữ"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Giới tính");
                builder.setItems(gioiTinh, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edtAddGioiTinh.setText(gioiTinh[which]);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btnAddMemberSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean checkHo = true;
                Boolean checkTen = true;
                Boolean checkEmail = true;
                Boolean checkPass = true;
                Boolean checkPassCon = true;

                if (txtIEdtAddHo.getText().toString().isEmpty()) {
                    txtILayoutAddHo.setHelperText("Trống họ");
                    checkHo = false;
                }

                if (txtIEdtAddTen.getText().toString().isEmpty()) {
                    txtILayoutAddTen.setHelperText("Trống tên");
                    checkTen = false;
                }

                if (txtIEdtAddEmail.getText().toString().trim().isEmpty()) {
                    txtILayoutAddEmail.setHelperText("Trống email");
                    checkEmail = false;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(txtIEdtAddEmail.getText().toString().trim()).matches()) {
                    txtILayoutAddEmail.setHelperText("Email không hợp lệ");
                    checkEmail = false;
                }

                if (txtIEdtAddPass.getText().toString().trim().isEmpty()) {
                    txtILayoutAddPass.setHelperText("Trống mật khẩu");
                    checkPass = false;
                } else if (txtIEdtAddPass.getText().toString().trim().length() <= 8) {
                    txtILayoutAddPass.setHelperText("Mật khẩu ngắn");
                    checkPass = false;
                }

                if (txtIEdtAddPassCon.getText().toString().trim().equals(txtIEdtAddPass.getText().toString().trim())) {

                } else {
                    txtILayoutAddPassCon.setHelperText("Mật khẩu không khớp");
                    checkPassCon = false;
                }

                if (checkHo == false || checkTen == false || checkEmail == false || checkPass == false || checkPassCon == false) {
                    return;
                }

                progressBarAddMember.setVisibility(View.VISIBLE);
                btnAddMemberSubmit.setVisibility(View.INVISIBLE);

                String email = txtIEdtAddEmail.getText().toString().trim();
                String pass = txtIEdtAddPass.getText().toString().trim();
                String idMember = UUID.randomUUID().toString();
                String ho = txtIEdtAddHo.getText().toString();
                String ten = txtIEdtAddTen.getText().toString();
                String gioiTinh = edtAddGioiTinh.getText().toString();

                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser userID = task.getResult().getUser();
                        String idAccount = userID.getUid();

                        if (imageUri == null) {
                            String imageMember = "1";

                            Member newMember = new Member(idMember, idAccount, ten, ho, email, gioiTinh, imageMember, 1, 0);

                            crud.addMember(newMember);
                            dialogAddMember.dismiss();
                        } else {
                            UploadTask uploadTask = imageRef.putFile(imageUri);
                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Lấy URL của ảnh tải lên từ taskSnapshot
                                    Task<Uri> downloadUrlTask = imageRef.getDownloadUrl();
                                    downloadUrlTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageMember = uri.toString();

                                            Member newMember = new Member(idMember, idAccount, ten, ho, email, gioiTinh, imageMember, 1, 0);

                                            crud.addMember(newMember);
                                            dialogAddMember.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Lỗi kết nối internet", Toast.LENGTH_SHORT).show();
                                            progressBarAddMember.setVisibility(View.INVISIBLE);
                                            btnAddMemberSubmit.setVisibility(View.VISIBLE);
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

                    } else {
                        Toast.makeText(context, "Email đã được sử dụng", Toast.LENGTH_SHORT).show();
                        progressBarAddMember.setVisibility(View.INVISIBLE);
                        btnAddMemberSubmit.setVisibility(View.VISIBLE);
                    }
                });

            }
        });

        txtCancelAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddMember.dismiss();
            }
        });

    }
}