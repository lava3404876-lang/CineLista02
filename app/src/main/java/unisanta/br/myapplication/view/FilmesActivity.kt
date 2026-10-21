package unisanta.br.myapplication.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import unisanta.br.myapplication.R
import unisanta.br.myapplication.databinding.ActivityFilmesBinding
import unisanta.br.myapplication.model.Filme
import unisanta.br.myapplication.viewmodel.FilmesViewModel

class FilmesActivity : AppCompatActivity() {
    private lateinit var tela: ActivityFilmesBinding
    private val modelo by lazy { ViewModelProvider(this)[FilmesViewModel::class.java] }
    private val catalogo by lazy { FilmeAdapter(::abrirEdicao) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.getStringExtra(EXTRA_EMAIL).isNullOrBlank()) {
            finish()
            return
        }
        enableEdgeToEdge()
        tela = ActivityFilmesBinding.inflate(layoutInflater)
        setContentView(tela.root)
        prepararTela()
        acompanharColecao()
        configurarAcoes()
    }

    private fun prepararTela() {
        ViewCompat.setOnApplyWindowInsetsListener(tela.root) { view, insets ->
            val bordas = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
            )
            view.setPadding(bordas.left, bordas.top, bordas.right, bordas.bottom)
            insets
        }
        tela.listaFilmes.apply {
            layoutManager = LinearLayoutManager(this@FilmesActivity)
            adapter = catalogo
        }
    }

    private fun acompanharColecao() {
        modelo.filmes.observe(this) { colecao ->
            catalogo.submitList(colecao)
            tela.txtVazio.isVisible = colecao.isEmpty()
            tela.txtTitulo.text = getString(R.string.colecao_total, colecao.size)
        }
        modelo.editando.observe(this) { identificador ->
            tela.btnSalvar.setText(
                if (identificador == null) R.string.adicionar_filme else R.string.salvar_alteracoes
            )
            tela.btnCancelar.isVisible = identificador != null
        }
    }

    private fun configurarAcoes() {
        tela.btnSalvar.setOnClickListener { guardarFilme() }
        tela.btnCancelar.setOnClickListener {
            modelo.cancelarEdicao()
            limparCampos()
        }
    }

    private fun abrirEdicao(filme: Filme) = with(tela) {
        modelo.editar(filme)
        edtNome.setText(filme.nome)
        edtNota.setText(filme.nota.toString())
        edtFoto.setText(filme.fotoUrl)
        formulario.smoothScrollTo(0, 0)
        edtNome.requestFocus()
        Unit
    }

    private fun guardarFilme() {
        val mensagem = modelo.salvar(
            tela.edtNome.text.toString(),
            tela.edtNota.text.toString(),
            tela.edtFoto.text.toString()
        )
        if (mensagem != null) {
            Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show()
            return
        }
        limparCampos()
        Toast.makeText(this, R.string.filme_salvo, Toast.LENGTH_SHORT).show()
    }

    private fun limparCampos() = with(tela) {
        listOf(edtNome, edtNota, edtFoto).forEach { it.text.clear() }
        edtNome.clearFocus()
    }

    companion object {
        const val EXTRA_EMAIL = "email"
    }
}
