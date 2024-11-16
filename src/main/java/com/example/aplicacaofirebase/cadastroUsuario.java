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
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class cadastroUsuario extends AppCompatActivity {

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("usuario");

    String Snome;
    String Scpf;
    String Semail;
    String Ssenha;
    usuario usu;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        EditText nome = findViewById(R.id.textNome);
        EditText email = findViewById(R.id.textEmail);
        EditText cpf = findViewById(R.id.textCpf);
        EditText senha = findViewById(R.id.textSenha);
        Button btn = findViewById(R.id.btnSalvar);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if((nome.getText().toString().trim().isEmpty()) || (email.getText().toString().trim().isEmpty()) || (cpf.getText().toString().trim().isEmpty()) || (senha.getText().toString().trim().isEmpty())){
                    Toast.makeText(getApplicationContext(), "Insira os dados corretamente!", Toast.LENGTH_SHORT).show();
                } else {
                Snome = nome.getText().toString();
                Semail = email.getText().toString();
                Scpf = cpf.getText().toString();
                Ssenha = senha.getText().toString();

                mAuth.createUserWithEmailAndPassword(Semail, Ssenha)
                        .addOnCompleteListener(cadastroUsuario.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    usu = new usuario();
                                    usu.setNome(Snome);
                                    usu.setEmail(Semail);
                                    usu.setCpf(Scpf);
                                    usu.setSenha(Ssenha);

                                    reference.push().setValue(usu);

                                    Toast.makeText(cadastroUsuario.this, "Faça Login para Acesso as Funções", Toast.LENGTH_SHORT).show();
                                    Intent voltar = new Intent(cadastroUsuario.this, MainActivity.class);
                                    startActivity(voltar);
                                } else {
                                    String excecao="";
                                    try {
                                        throw task.getException();
                                    }
                                    catch (FirebaseAuthWeakPasswordException e){
                                        excecao = "Digite uma senha com 6 digitos.";
                                    }
                                    catch (FirebaseAuthInvalidCredentialsException e){
                                        excecao = "Por favor digite um email válido.";
                                    }
                                    catch (FirebaseAuthUserCollisionException e){
                                        excecao = "Essa conta já foi cadastrada.";
                                    }
                                    catch (Exception e){
                                        excecao = "Erro ao cadastrar o Usuário." +e.getMessage();
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(cadastroUsuario.this, excecao, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
            }
        });
    }
}