package com.example.aplicacaofirebase;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class cadProduto extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("produtos");



    // Declaração das ImageView e URIs para armazenar as imagens selecionadas
    ImageView img1;
    ImageView img2;


    ImageView img3;
    Uri imageUri1, imageUri2, imageUri3;
    EditText desc, preco;
    Button Salvar ;

    String descricaoProd;
    double valorProd;
    private Produto produtos;
    //  private StorageReference storage;

    // ActivityResultLaunchers para cada ImageView
    //classe utilizada para lançar uma atividade e receber seu resultado
    ActivityResultLauncher<Intent> imagePickerLauncher1;
    ActivityResultLauncher<Intent> imagePickerLauncher2;
    ActivityResultLauncher<Intent> imagePickerLauncher3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_cad_produto);

        // Inicializa as ImageView e o Button
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        desc = findViewById(R.id.editDescricao);
        preco = findViewById(R.id.editValor);
        Salvar = findViewById(R.id.cadastrarProduto);
        //    storage = FirebaseStorage.getInstance().getReference();

        // Define os listeners de clique para as ImageView e o Button
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        Salvar.setOnClickListener(this);


        // Inicializa os ActivityResultLaunchers

        imagePickerLauncher1 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> handleImageResult(result, 1)
        );

        imagePickerLauncher2 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> handleImageResult(result, 2)
        );

        imagePickerLauncher3 = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> handleImageResult(result, 3)
        );


    }


    private void pickImageFromGallery(int imageNumber) {
        // Cria uma intent para selecionar uma imagem da galeria
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Lança o ActivityResultLauncher apropriado com base no número da imagem
        if (imageNumber == 1) {
            imagePickerLauncher1.launch(intent);
        } else if (imageNumber == 2) {
            imagePickerLauncher2.launch(intent);
        } else if (imageNumber == 3) {
            imagePickerLauncher3.launch(intent);
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        // Converte a URI da imagem em um Bitmap
        InputStream inputStream = getContentResolver().openInputStream(uri);
        return BitmapFactory.decodeStream(inputStream);
    }

    private void handleImageResult(ActivityResult result, int imageNumber) {
        // Processa o resultado da seleção de imagem
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                Uri selectedImage = data.getData();
                try {
                    // Converte a URI em Bitmap
                    Bitmap bitmap = getBitmapFromUri(selectedImage);
                    // Redimensiona o Bitmap
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);

                    // Define o Bitmap redimensionado na ImageView correspondente e armazena a URI
                    if (imageNumber == 1) {
                        imageUri1 = selectedImage;
                        img1.setImageBitmap(resizedBitmap);
                    } else if (imageNumber == 2) {
                        imageUri2 = selectedImage;
                        img2.setImageBitmap(resizedBitmap);
                    } else if (imageNumber == 3) {
                        imageUri3 = selectedImage;
                        img3.setImageBitmap(resizedBitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void enviarImagensParaFirebase() {
        List<Uri> uris = new ArrayList<>();
        if (imageUri1 != null) uris.add(imageUri1);
        if (imageUri2 != null) uris.add(imageUri2);
        if (imageUri3 != null) uris.add(imageUri3);

        if (!uris.isEmpty()) {
            List<String> urls = new ArrayList<>();
            for (int i = 0; i < uris.size(); i++) {
                int index = i;
                enviarImagemParaFirebase(uris.get(i), "image" + (i + 1) + ".jpg", uri -> {
                    urls.add(uri.toString());
                    if (urls.size() == uris.size()) {
                        salvarProdutoNoDatabase(urls);
                    }
                });
            }
        } else {
            salvarProdutoNoDatabase(new ArrayList<>());
        }
    }

    private void salvarProdutoNoDatabase(List<String> urlsImagens) {
        descricaoProd = desc.getText().toString();
        valorProd = Double.parseDouble(preco.getText().toString());

        Produto produto = new Produto(descricaoProd, valorProd, urlsImagens);
        reference.push().setValue(produto)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(cadProduto.this, "Produto salvo com sucesso", Toast.LENGTH_SHORT).show();
                    // Limpar campos e resetar UI se necessário
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(cadProduto.this, "Falha ao salvar produto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void enviarImagemParaFirebase(Uri imageUri, String imageName, OnSuccessListener<Uri> onSuccessListener) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + System.currentTimeMillis() + "_" + imageName);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl()
                        .addOnSuccessListener(onSuccessListener)
                        .addOnFailureListener(e -> {
                            Toast.makeText(cadProduto.this, "Falha ao obter URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }))
                .addOnFailureListener(e -> {
                    Toast.makeText(cadProduto.this, "Falha ao enviar imagem: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }



    @Override
    public void onClick(View v) {
        // Determina qual ImageView foi clicada e chama o método apropriado para selecionar a imagem
        if (v == img1) {
            pickImageFromGallery(1);
        } else if (v == img2) {
            pickImageFromGallery(2);
        } else if (v == img3) {
            pickImageFromGallery(3);
        } else if (v == Salvar) {
            // Envia as imagens selecionadas para o Firebase Storage quando o botão é clicado
            enviarImagensParaFirebase();
        }
    }

}

