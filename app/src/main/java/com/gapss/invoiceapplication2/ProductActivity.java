package com.gapss.invoiceapplication2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gapss.invoiceapplication2.db.MainData;
import com.gapss.invoiceapplication2.db.RoomDB;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    EditText etName, etCode;
    Button btnAdd;
    RecyclerView recyclerView;


    List<MainData> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RoomDB database;
    MainAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Bundle arguments = getIntent().getExtras();
        String code = arguments.get("code").toString();


        etName = findViewById(R.id.et_name);
        etCode = findViewById(R.id.et_code);
        btnAdd = findViewById(R.id.btn_add);
        recyclerView = findViewById(R.id.rec_view);

        if(code.equals("Scan something...")){
            etCode.setText("");
        } else {
            etCode.setText(code);
        }

        database = RoomDB.getInstance(this);

        dataList = database.mainDao().getAll();

        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MainAdapter(ProductActivity.this, dataList);

        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sText = etName.getText().toString().trim();
                String sCode = etCode.getText().toString().trim();

                if(!(sText.equals("")&&sCode.equals(""))){
                    MainData data = new MainData();
                    data.setText(sText);
                    data.setCode(sCode);
                    database.mainDao().insert(data);
                    etName.setText("");
                    etCode.setText("");
                    dataList.clear();
                    dataList.addAll(database.mainDao().getAll());
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(ProductActivity.this, "Please enter name and code", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}