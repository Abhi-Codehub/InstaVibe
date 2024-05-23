package com.example.instavibe

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.instavibe.Models.User
import com.example.instavibe.databinding.ActivitySignupBinding
import com.example.instavibe.utils.USER_NODE
import com.example.instavibe.utils.USER_PROFILE_FOLDER
import com.example.instavibe.utils.uploadImage
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class Signup : AppCompatActivity() {

        val binding by lazy{
            ActivitySignupBinding.inflate(layoutInflater)
        }
    lateinit var user: User

    private val launcher= registerForActivityResult(ActivityResultContracts.GetContent()){
        uri->
        // uniform result identifire
        uri?.let{
            // to get image url
            uploadImage(uri, USER_PROFILE_FOLDER){
                if(it==null){

                } else {
                    user.image = it
                    binding.profileImage.setImageURI(uri)
                }
            }
        }
    }

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
                        user.email=binding.email.editText?.text.toString()
                        user.password=binding.password.editText?.text.toString()

                        Firebase.firestore.collection(USER_NODE)
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
        binding.addImage.setOnClickListener{
            launcher.launch("image/*")
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}