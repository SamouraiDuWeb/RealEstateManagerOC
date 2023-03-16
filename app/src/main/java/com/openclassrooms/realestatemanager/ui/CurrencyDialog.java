package com.openclassrooms.realestatemanager.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.Utils;

public class CurrencyDialog extends Dialog {

    public Activity context;
    public Dialog d;
    public EditText etInput, etOutput;
    public ImageView ivChangeOrder;
    public Button btnValidate;
    int input;
    int output;

    public CurrencyDialog(Activity a) {
        super(a);
        this.context = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.convert_currency_dialog);
        initView();
        setListeners();
    }

    private void setListeners() {
        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input = Integer.parseInt(String.valueOf(etInput.getText()));
                if(etInput.getHint().equals("Euros")){
                    output = Utils.convertEuroToDollar(input);
                    etOutput.setText(""+output);
                } else if (etInput.getHint().equals("Dollars")){
                    output = Utils.convertDollarToEuro(input);
                    etOutput.setText(""+output);
                }
            }
        });
        ivChangeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etInput.setText("");
                etOutput.setText("");
                if(etInput.getHint().equals("Euros")){
                    etInput.setHint("Dollars");
                    etOutput.setHint("Euros");
                } else if (etInput.getHint().equals("Dollars")){
                    etInput.setHint("Euros");
                    etOutput.setHint("Dollars");
                }
            }
        });
    }

    private void initView() {
        etInput = findViewById(R.id.et_currency_input);
        etOutput = findViewById(R.id.et_currency_output);
        btnValidate = findViewById(R.id.btn_validate);
        ivChangeOrder = findViewById(R.id.iv_change_currency);
    }

    private void getView() {
    }
}
