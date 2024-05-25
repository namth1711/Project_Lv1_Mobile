package com.example.project_lv1_mobile.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_lv1_mobile.R;
import com.example.project_lv1_mobile.firebase.FirebaseCRUD;
import com.example.project_lv1_mobile.model.Member;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private final Context context;
    private List<Member> memberList;
    private final FirebaseCRUD crud;

    public MemberAdapter(Context context, List<Member> memberList, FirebaseCRUD crud) {
        this.context = context;
        this.memberList = memberList;
        this.crud = crud;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_recycler_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtHo.setText(memberList.get(position).getLastName());
        holder.txtTen.setText(memberList.get(position).getFirtName());

        String status = memberList.get(position).getStatus() == 0 ? "Kích hoạt" : "Vô hiệu hóa";
        holder.txtTrangThai.setText(status);
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtHo, txtTen, txtTrangThai;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHo = itemView.findViewById(R.id.txtHo);
            txtTen = itemView.findViewById(R.id.txtTen);
            txtTrangThai = itemView.findViewById(R.id.txtTrangThai);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Member member = memberList.get(getAdapterPosition());
                    openDialogFillIntroMember(member);
                }
            });
        }
    }

    public void openDialogFillIntroMember(Member member) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_fill_intro_member, null);
        builder.setView(view);
        Dialog fillIntroMemberDialog = builder.create();
        fillIntroMemberDialog.show();

        ImageView ivFillAvatar = view.findViewById(R.id.ivFillAvatar);
        TextView txtFillHo = view.findViewById(R.id.txtFillHo);
        TextView txtFillTen = view.findViewById(R.id.txtFillTen);
        TextView txtFillGioiTinh = view.findViewById(R.id.txtFillGioiTinh);
        TextView txtFillEmailMember = view.findViewById(R.id.txtFillEmailMember);
        TextView txtFillTrangThai = view.findViewById(R.id.txtFillTrangThai);
        TextView txtCancelFillIntroMember = view.findViewById(R.id.txtCancelFillIntroMember);

        TextView txtDisable = view.findViewById(R.id.txtDisable);
        TextView txtActivate = view.findViewById(R.id.txtActivate);

        if (member.getImageMember().equals("1")) {
            if (member.getGender().equals("Nam")) {
                ivFillAvatar.setImageResource(R.drawable.male_icon);
            } else if (member.getGender().equals("Nữ")) {
                ivFillAvatar.setImageResource(R.drawable.female_icon);
            }
        } else {
            Glide.with(context).load(member.getImageMember()).into(ivFillAvatar);
        }

        txtFillHo.setText(member.getLastName());
        txtFillTen.setText(member.getFirtName());
        txtFillGioiTinh.setText(member.getGender());
        txtFillEmailMember.setText(member.getEmail());

        String status = member.getStatus() == 0 ? "Kích hoạt" : "Vô hiệu hóa";
        txtFillTrangThai.setText(status);

        if (member.getStatus() == 0) {
            txtActivate.setVisibility(View.GONE);
        } else {
            txtDisable.setVisibility(View.GONE);
        }

        txtDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Cảnh báo");
                builder.setMessage("Xác nhận vô hiệu hóa tài khoản này!");

                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        member.setStatus(1);
                        crud.updateMember(member);

                        notifyDataSetChanged();
                        Toast.makeText(context, "Đã vô hiệu hóa tài khoản", Toast.LENGTH_SHORT).show();
                        fillIntroMemberDialog.dismiss();
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

        txtActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Cảnh báo");
                builder.setMessage("Xác nhận kích hoạt tài khoản này!");

                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        member.setStatus(0);
                        crud.updateMember(member);

                        notifyDataSetChanged();
                        Toast.makeText(context, "Đã kích hoạt tài khoản", Toast.LENGTH_SHORT).show();
                        fillIntroMemberDialog.dismiss();
                    }
                });

                builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialogActivate = builder.create();
                dialogActivate.show();
            }
        });

        txtCancelFillIntroMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillIntroMemberDialog.dismiss();
            }
        });

    }
}
