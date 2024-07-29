package com.example.foodorderingapp

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodorderingapp.databinding.ActivitySigninBinding
import com.example.foodorderingapp.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SigninActivity : AppCompatActivity() {

    //1. create variable to be used for sign up
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var username: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient


    private val binding: ActivitySigninBinding by lazy {
        ActivitySigninBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //2. initialze varibales firebase database and auth
        auth = Firebase.auth
        database = Firebase.database.reference

        //initialise google sign in client
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)



        binding.btnSignin.setOnClickListener {
            //variables ko values denge edit texts se
            username = binding.edtName.text.toString().trim()
            email = binding.edtEmail2.text.toString().trim()
            password = binding.edtPassword2.text.toString().trim()

            //check karenge field empty to nahi hn
            if (email.isEmpty() || password.isBlank() || username.isBlank()) {
                showToast("Please fill all details!")
            } else {
                createAccount(email, password)
            }
        }

        binding.tvLoginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            binding.tvLoginLink.setTextColor(Color.parseColor("#000000"))
            finish()
            binding.tvLoginLink.setTextColor(Color.parseColor("#1B14ED"))
        }

        binding.btnGoogle2.setOnClickListener {
            val signinIntent = googleSignInClient.signInIntent
            launcher.launch(signinIntent)
        }
    }

    //launcher for google signin
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                showToast("Account Created Successfully!")
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    val credentials = GoogleAuthProvider.getCredential(account?.idToken, null)
                    auth.signInWithCredential(credentials).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                            showToast("Signed in!")

                        } else {
                            showToast("Sign in failed!")
                        }
                    }
                } else {
                    showToast("Sign in failed")
                }
            }else{
                showToast("Account Creation Failed!")
            }
        }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                showToast("Account created successfully!")
                updateUIOnSuccess()
                saveUserData()
            } else {
                showToast("Account Creation Failed!")
                Log.d("Account", "createAccount: Failure", task.exception)
                updateUIOnFailure()
            }
        }
    }

    private fun saveUserData() {
        //retrieve data from input field
        username = binding.edtName.text.toString().trim()
        email = binding.edtEmail2.text.toString().trim()
        password = binding.edtPassword2.text.toString().trim()
        username = binding.edtName.text.toString().trim()

        val user = UserModel(username, email, password)

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        //ek node ya folder bnaenge child me jisme data store hoga and save data
        database.child("user").child(userId).child("profile").setValue(user)
    }

    private fun updateUIOnSuccess() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun updateUIOnFailure() {
        binding.edtName.text.clear()
        binding.edtEmail2.text.clear()
        binding.edtPassword2.text.clear()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}