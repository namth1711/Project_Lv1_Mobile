package com.example.project_lv1_mobile.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.project_lv1_mobile.R;

public class CustomQuantityPicker extends LinearLayout {

    private TextView txtAddition, txtSubtraction;
    private EditText edtQuantity;

    private TextView txtSetQuantity;

    private OnQuantityChangeListener onQuantityChangeListener;

    private int quantity = 0;
    private int maxQuantity;

    public CustomQuantityPicker(Context context) {
        super(context);
    }

    public CustomQuantityPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public CustomQuantityPicker setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
        return this;
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_quantity_picker, this, true);

        txtAddition = view.findViewById(R.id.txtAddition);
        txtSubtraction = view.findViewById(R.id.txtSubtraction);
        edtQuantity = view.findViewById(R.id.edtQuantity);

        txtAddition.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                decrement();
            }
        });

        txtSubtraction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                increment();
            }
        });


        edtQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    // Cập nhật giá trị số lượng với giá trị mới từ EditText
                    quantity = Integer.parseInt(s.toString());
                } catch (NumberFormatException e) {
                    // Nếu có lỗi khi chuyển đổi số, sử dụng giá trị mặc định là 1
                    edtQuantity.setText("1");
                    quantity = 1;
                }
            }
        });

    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        edtQuantity.setText(String.valueOf(quantity));
    }

    private void increment() {
        if (quantity < maxQuantity) {
            quantity++;
            edtQuantity.setText(String.valueOf(quantity));
            if (onQuantityChangeListener != null) {
                onQuantityChangeListener.onQuantityChanged(quantity);
            }
        }
    }

    private void decrement() {
        if (quantity > 1) {
            quantity--;
            edtQuantity.setText(String.valueOf(quantity));
            if (onQuantityChangeListener != null) {
                onQuantityChangeListener.onQuantityChanged(quantity);
            }
        }
    }

    public interface OnQuantityChangeListener {
        void onQuantityChanged(int newQuantity);
    }

    public void setOnQuantityChangeListener(OnQuantityChangeListener listener) {
        this.onQuantityChangeListener = listener;
    }

    public void setMainQuantityTextView(TextView textView, int price, TextView txtQuantity) {
        this.txtSetQuantity = textView;
        addTextChangedListenerForEditText(price, txtQuantity);
    }

    private void updateMainQuantityTextView() {
        if (txtSetQuantity != null) {
            txtSetQuantity.setText(String.valueOf(quantity));
        }
    }

    private void addTextChangedListenerForEditText(int price, TextView txtQuantity) {
        if (edtQuantity != null && txtSetQuantity != null) {
            edtQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        int newQuantity = Integer.parseInt(editable.toString());
                        String s = String.valueOf(editable);
                        txtSetQuantity.setText(String.valueOf(newQuantity * price));
                        txtQuantity.setText(s);
                    } catch (NumberFormatException e) {
                        // Xử lý nếu có lỗi chuyển đổi số
                        txtSetQuantity.setText("1");
                        txtQuantity.setText("1");
                    }
                }
            });
        }
    }
}
