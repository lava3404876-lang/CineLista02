package unisanta.br.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import unisanta.br.myapplication.model.Filme
import unisanta.br.myapplication.model.FilmeRepository
import unisanta.br.myapplication.model.ValidadorFilme
import java.util.UUID

class FilmesViewModel(application: Application, private val estado: SavedStateHandle) :
    AndroidViewModel(application) {
    private val repository = FilmeRepository(application)
    private val email: String = checkNotNull(estado["email"])
    private val _filmes = MutableLiveData(repository.carregar(email))
    val filmes: LiveData<List<Filme>> = _filmes
    val editando: LiveData<String?> = estado.getLiveData("filmeEditando", null)

    fun editar(filme: Filme) {
        estado["filmeEditando"] = filme.id
    }

    fun cancelarEdicao() {
        estado.set<String?>("filmeEditando", null)
    }

    fun salvar(nome: String, nota: String, fotoUrl: String): String? {
        ValidadorFilme.validar(nome, nota, fotoUrl)?.let { return it }
        val id: String? = estado["filmeEditando"]
        val filme = Filme(
            id ?: UUID.randomUUID().toString(), nome.trim(),
            nota.trim().replace(',', '.').toDouble(), fotoUrl.trim()
        )
        val atuais = _filmes.value.orEmpty()
        val atualizados = if (id == null) atuais + filme else atuais.map {
            if (it.id == id) filme else it
        }
        repository.salvar(email, atualizados)
        _filmes.value = atualizados
        cancelarEdicao()
        return null
    }
}
