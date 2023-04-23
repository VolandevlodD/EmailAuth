package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInAct : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
// ...
// Initialize Firebase Auth
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        } else {
            setContent {
                LoginScreen()
            }
        }
    }

    private fun reload() {
        setContent {
            Greeting("U are alredy logged in")
        }
    }

    // функція реєстрації користувача з електронною поштою та паролем
    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                setContent{
                    SuccessDialog()
                }
            } else {
                setContent{
                    ErrorDialog()
                }
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        // Регулярний вираз для перевірки формату електронної пошти
        val pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        // Перевірка, чи відповідає рядок формату електронної пошти
        return email.matches(pattern.toRegex())
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun RegistrationScreen() {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }

        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Registration",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                visualTransformation = PasswordVisualTransformation()
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                onClick = {
                    if (isValidEmail(email) && password == confirmPassword) {
                        registerUser(email, password)
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(text = "Register")
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EmailAuthScreen(onLoginClicked: (email: String, password: String) -> Unit) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                visualTransformation = PasswordVisualTransformation(),
            )

            Button(
                onClick = { onLoginClicked(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Login")
            }
        }
    }
    @Composable
    fun ErrorDialog() {
        AlertDialog(
            onDismissRequest = { /* пусто */ },
            title = { Text("Помилка реєстрації") },
            text = { Text("Виникла помилка під час реєстрації. Будь ласка, спробуйте ще раз.") },
            confirmButton = {
                TextButton(onClick = { /* закрити вікно */ }) {
                    Text("Закрити")
                }
            }
        )
    }
    @Composable
    fun SuccessDialog() {
        AlertDialog(
            onDismissRequest = { /* пусто */ },
            title = { Text("Реєстрація успішна") },
            text = { Text("Ви успішно зареєструвались") },
            confirmButton = {
                TextButton(onClick = { /* закрити вікно */ }) {
                    Text("Закрити")
                }
            }
        )
    }
    @Composable
    fun LoginScreen() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { 
                          setContent{
                              RegistrationScreen()
                          }
                },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Реєстрація")
            }
            Button(
                onClick = { setContent{
                    EmailAuthScreen(onLoginClicked = )
                }
                }
            ) {
                Text("Вхід")
            }
        }
    }



}
