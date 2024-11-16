package com.example.aplicacaofirebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProdutosAdapter extends RecyclerView.Adapter<ProdutosAdapter.ProdutoViewHolder> {

    private List<Produto> listaProdutos;
    private Context context;

    public ProdutosAdapter(Context context, List<Produto> listaProdutos) {
        this.context = context;
        this.listaProdutos = listaProdutos;
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto, parent, false);
        return new ProdutoViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        Produto produto = listaProdutos.get(position);
        holder.bind(produto);
    }

    private void holder(Produto produto) {
    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    public static class ProdutoViewHolder extends RecyclerView.ViewHolder{
    private TextView txtDescricao;
    private TextView txtPreco;
    private ImageView imageView;
    private ImageView imageView2;
    private ImageView imageView3;

        public ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
            txtPreco = itemView.findViewById(R.id.txtPreco);
            imageView = itemView.findViewById(R.id.imageView);
            imageView2 = itemView.findViewById(R.id.imageView2);
            imageView3 = itemView.findViewById(R.id.imageView3);
        }

        public void bind(Produto produto){
            txtDescricao.setText(produto.getDescricao());
            txtPreco.setText(String.valueOf(produto.getValor()));

            List<String> urlsImagens = produto.getUrlsImagens();
            if(urlsImagens != null) {
                int numImages = urlsImagens.size();

                imageView.setImageResource(0);
                imageView2.setImageResource(0);
                imageView3.setImageResource(0);
                switch (numImages){
                    case 3:
                        Glide.with(itemView.getContext()).load(urlsImagens.get(2)).into(imageView3);
                    case 2:
                        Glide.with(itemView.getContext()).load(urlsImagens.get(1)).into(imageView2);
                    case 1:
                        Glide.with(itemView.getContext()).load(urlsImagens.get(0)).into(imageView);
                        break;
                    default:
                        imageView.setImageResource(R.drawable.ic_launcher_foreground);
                        imageView2.setImageResource(R.drawable.ic_launcher_foreground);
                        imageView3.setImageResource(R.drawable.ic_launcher_foreground);
                        break;
                }
            } else {
                imageView.setImageResource(R.drawable.ic_launcher_foreground);
                imageView2.setImageResource(R.drawable.ic_launcher_foreground);
                imageView3.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }
    }
}
