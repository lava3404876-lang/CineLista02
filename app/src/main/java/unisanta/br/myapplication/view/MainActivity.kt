package unisanta.br.myapplication.view

import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import unisanta.br.myapplication.databinding.ActivityMainBinding
import unisanta.br.myapplication.viewmodel.LoginViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnEntrar.setOnClickListener{
            val email = binding.edtEmailUsuario.text.toString()
            val senha = binding.edtSenhaUsuario.text.toString()
            val retorno = loginViewModel.entrar(email, senha)
            Toast.makeText(this, retorno, Toast.LENGTH_SHORT).show()
            if (loginViewModel.autenticado) {
                startActivity(Intent(this, FilmesActivity::class.java)
                    .putExtra(FilmesActivity.EXTRA_EMAIL, email.trim().lowercase(java.util.Locale.ROOT)))
            }
        }
        binding.btnCadastrar.setOnClickListener {
            val email = binding.edtEmailUsuario.text.toString()
            val senha = binding.edtSenhaUsuario.text.toString()
            val retorno = loginViewModel.cadastrar(email, senha)
            Toast.makeText(this, retorno, Toast.LENGTH_SHORT).show()
        }
    }
}
