package com.example.foodorderingapp

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodorderingapp.databinding.ActivityLoginBinding
import com.example.foodorderingapp.databinding.ActivityStartBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    //variables for sigin in
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseDatabase
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding : ActivityLoginBinding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //initialize varibales for firebase auth and database
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        //initilaize google sign in client
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        binding.btnLogin.setOnClickListener{
            //get values from edit text
            email = binding.edtEmail.text.toString().trim()
            password = binding.edtPassword.text.toString().trim()

            //check if empty
            if(email.isBlank() || password.isBlank()){
                showToast("Please fill all details!")
            }else{
                login(email,password)
            }
        }

        binding.btnGoogle.setOnClickListener {
            val signinIntent = googleSignInClient.signInIntent
            launcher.launch(signinIntent)
        }

        binding.tvSignupLink.setOnClickListener{
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
            binding.tvSignupLink.setTextColor(Color.parseColor("#000000"))
            finish()
            binding.tvSignupLink.setTextColor(Color.parseColor("#1B14ED"))
        }

    }


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                val account: GoogleSignInAccount? = task.result
                val credential: AuthCredential =
                    GoogleAuthProvider.getCredential(account?.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        //successfully sigin with google
                        val user: FirebaseUser? = auth.currentUser
                        showToast("Google Sign-in Successful!")
                        updateUIOnSuccess()
                    } else {
                        showToast("Google Sign-in failed!")
                    }
                }
            } else {
                showToast("Google Sign-in failed!")
            }
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            task ->
            if(task.isSuccessful){
                updateUIOnSuccess()
                showToast("Logged In Successfully!")
            }else{
                updateUIOnFailure()
                showToast("Login Failed!")
                Log.d("Authentication", "loginAccount: Authentication Failed", task.exception)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUIOnSuccess()
        }
    }
    private fun updateUIOnSuccess() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun updateUIOnFailure() {
        binding.edtEmail.text.clear()
        binding.edtPassword.text.clear()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}