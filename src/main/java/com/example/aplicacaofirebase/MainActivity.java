package com.example.aplicacaofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    usuario usu = new usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btnNovoUsu = findViewById(R.id.btnNovoUsuario);
        Button btnLogar = findViewById(R.id.btnLogar);
        // email e senha
        EditText LEmail = findViewById(R.id.editEmail);
        EditText LSenha = findViewById(R.id.editSenha);

        btnNovoUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, cadastroUsuario.class);
                startActivity(intent);
            }
        });

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(LEmail.getText().toString().trim().isEmpty() || LSenha.getText().toString().trim().isEmpty())
                    Toast.makeText(getApplicationContext(), "Informe Email e Senha!", Toast.LENGTH_SHORT).show();

                else {
                usu.setEmail(LEmail.getText().toString());
                usu.setSenha(LSenha.getText().toString());

                mAuth.signInWithEmailAndPassword(usu.getEmail(), usu.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Sucesso ao logar!", Toast.LENGTH_SHORT).show();

                            Intent opcoes = new Intent(MainActivity.this, opcoes.class);
                            startActivity(opcoes);
                        } else {
                            String excecao = "";

                            try {
                                throw task.getException();
                            }
                            catch (FirebaseAuthInvalidCredentialsException e){
                                excecao = "Email e Senha NÃO correspondem!";
                            }
                            catch (Exception e){
                                excecao = "Erro ao Logar!" + e.getMessage();
                                e.printStackTrace();
                            }
                            Toast.makeText(MainActivity.this, excecao, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}