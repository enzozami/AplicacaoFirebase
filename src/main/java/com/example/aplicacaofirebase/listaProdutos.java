package com.example.aplicacaofirebase;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class listaProdutos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProdutosAdapter produtosAdapter;
    private List<Produto> listaProdutos;
    private List<Produto> listaProdutosFiltrados;
    private EditText editTextFiltro;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);

        recyclerView = findViewById(R.id.recyclerView);
        editTextFiltro = findViewById(R.id.editTextFiltro);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listaProdutos = new ArrayList<>();
        listaProdutosFiltrados = new ArrayList<>();
        produtosAdapter = new ProdutosAdapter(this, listaProdutos);
        recyclerView.setAdapter(produtosAdapter);

        carregarProdutosDoFirebase();

        editTextFiltro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aplicarFiltro();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void carregarProdutosDoFirebase(){
        FirebaseDatabase.getInstance().getReference("produtos")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listaProdutos.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Produto produto = snapshot.getValue(Produto.class);
                            if(produto != null){
                                listaProdutos.add(produto);
                            }
                        }
                        aplicarFiltro();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void aplicarFiltro(){
        String filtro = editTextFiltro.getText().toString().toLowerCase();
        listaProdutosFiltrados.clear();

        for (Produto produto : listaProdutos){
            if (produto.getDescricao() != null && produto.getDescricao().toLowerCase().contains(filtro)){
                listaProdutosFiltrados.add(produto);
            }
        }
        produtosAdapter.notifyDataSetChanged();
    }
}