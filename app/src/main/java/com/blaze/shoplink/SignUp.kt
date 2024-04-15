package com.blaze.shoplink
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.blaze.shoplink.Model.UserModel
import com.blaze.shoplink.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUp : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var email :String
    private lateinit var password :String
    private lateinit var userName:String
    private lateinit var database : DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var binding:ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        auth = Firebase.auth
        database= Firebase.database.reference
        googleSignInClient= GoogleSignIn.getClient(this,googleSignInOptions)


        binding.signUpButton.setOnClickListener {
            email=binding.signupEmail.text.toString().trim()
            password=binding.signupPass.text.toString().trim()
            userName=binding.signupName.text.toString()

            if(userName.isBlank()||password.isBlank()||email.isBlank()){
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email,password)
            }
        }




        binding.alreadyButton.setOnClickListener{
            val intent= Intent(this,LogIn::class.java)
            startActivity(intent)

        }
        binding.googleSignup.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)

        }

    }
    private val launcher =registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if (result.resultCode==Activity.RESULT_OK){
            val task =GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful){
                val account:GoogleSignInAccount?=task.result
                val credential =GoogleAuthProvider.getCredential(account?.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener { task->
                    if (task.isSuccessful){
                        Toast.makeText(this, "Sign-in successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this, "Google SignIn Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "Google SignIn Failed", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {task->
            if (task.isSuccessful){
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                saveUserData()

                val intent= Intent(this,LogIn::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: failure ",task.exception)
            }
        }
    }

    private fun saveUserData() {
        email=binding.signupEmail.text.toString().trim()
        password=binding.signupPass.text.toString().trim()
        userName=binding.signupName.text.toString()

        val user=UserModel(userName,email,password)
        val userId=FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)
    }
}

