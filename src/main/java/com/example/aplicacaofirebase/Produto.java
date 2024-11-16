package com.example.aplicacaofirebase;

import java.util.List;

public class Produto {
    private String descricao;
    private double valor;
    private List<String> urlsImagens;

    public Produto(){
        // Necessario para o firebase
    }

    public Produto(String descricao, double valor, List<String> urlsImagens) {
        this.descricao = descricao;
        this.valor = valor;
        this.urlsImagens = urlsImagens;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public List<String> getUrlsImagens() {
        return urlsImagens;
    }

    public void setUrlsImagens(List<String> urlsImagens) {
        this.urlsImagens = urlsImagens;
    }
}
