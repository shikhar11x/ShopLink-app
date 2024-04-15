package com.blaze.shoplink

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.blaze.shoplink.Model.UserModel
import com.blaze.shoplink.databinding.ActivityLogInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class LogIn : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var email :String
    private  var name:String?=null

    private lateinit var password :String
    private lateinit var database : DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)



        auth = Firebase.auth
        database= Firebase.database.reference

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        googleSignInClient= GoogleSignIn.getClient(this,googleSignInOptions)



        binding.signUpButton.setOnClickListener {

            email=binding.loginEmail.text.toString().trim()
            password=binding.loginPass.text.toString().trim()

            if(password.isBlank()||email.isBlank()){
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            }else{
                createUser(email,password)
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

            }
        }
        binding.dontHaveButton.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
        binding.googleSignup.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)

        }


    }
    private val launcher =registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->

        if (result.resultCode== Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                val account: GoogleSignInAccount? = task.result
                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Sign-in successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Google SignIn Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }else {
            Toast.makeText(this, "Google SignIn Failed", Toast.LENGTH_SHORT).show()
        }
    }

        private fun createUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user=auth.currentUser
                Toast.makeText(this, "LogIn Successfull ", Toast.LENGTH_SHORT).show()
                updateUi(user)
            }else{

                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
                    if (task.isSuccessful) {
                        val user=auth.currentUser
                        Toast.makeText(this, "New User created and LogIn Successful ", Toast.LENGTH_SHORT).show()
                        saveUserData()
                        updateUi(user)

                    }else{
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                        Log.d("Account", "createAccount:Authentication failed ",task.exception)
                    }
                }
            }
        }
    }
    private fun saveUserData() {
        email = binding.loginEmail.text.toString().trim()
        password = binding.loginPass.text.toString().trim()

        val user = UserModel(name, email, password)
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            database.child("user").child(it).setValue(user)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser=auth.currentUser
        if(currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()

        }
    }
        private fun updateUi(user: FirebaseUser?) {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
