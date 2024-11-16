package com.example.aplicacaofirebase;

import android.content.Context;
import android.os.Bundle;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listaProdutos = new ArrayList<>();
        produtosAdapter = new ProdutosAdapter(this, listaProdutos);
        recyclerView.setAdapter(produtosAdapter);
        carregarProdutosDoFirebase();
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
                        produtosAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}