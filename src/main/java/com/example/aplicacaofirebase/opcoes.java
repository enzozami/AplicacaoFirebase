package com.example.aplicacaofirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class opcoes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcoes);

        Button btnCadProduto = findViewById(R.id.btnCadProd);
        Button btnListaProduto = findViewById(R.id.btnListProd);

        btnCadProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cadProd = new Intent(opcoes.this, cadProduto.class);
                startActivity(cadProd);
            }
        });

        btnListaProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listProd = new Intent(opcoes.this, listaProdutos.class);
                startActivity(listProd);
            }
        });
    }
}