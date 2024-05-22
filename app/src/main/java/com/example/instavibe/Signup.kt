package com.example.instavibe

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.instavibe.Models.User
import com.example.instavibe.databinding.ActivitySignupBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class Signup : AppCompatActivity() {

        val binding by lazy{
            ActivitySignupBinding.inflate(layoutInflater)
        }
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        user = User()

        binding.signUpBtn.setOnClickListener{
            if(binding.name.editText?.text.toString().equals("") or
                binding.email.editText?.text.toString().equals("") or
                binding.password.editText?.text.toString().equals("")) {
                Toast.makeText(this, "Please fill the required field", Toast.LENGTH_SHORT).show()
            }
            else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.email.editText?.text.toString(),
                        binding.password.editText?.text.toString()
                ).addOnCompleteListener {
                    result->
                    if(result.isSuccessful){
                        user.name=binding.name.editText?.text.toString()
                        user.password=binding.name.editText?.text.toString()
                        user.email=binding.name.editText?.text.toString()

                        Firebase.firestore.collection("User")
                            .document(Firebase.auth.currentUser!!.uid).set(user)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show()
                            }
                    }
                    else{
                        Toast.makeText(this, result.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}