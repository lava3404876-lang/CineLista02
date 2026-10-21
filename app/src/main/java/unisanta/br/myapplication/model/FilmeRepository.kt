package unisanta.br.myapplication.model

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class FilmeRepository(context: Context) {
    private val preferencias = context.getSharedPreferences("filmes_favoritos", Context.MODE_PRIVATE)

    fun carregar(email: String): List<Filme> {
        val dados = JSONArray(preferencias.getString(email, "[]"))
        return List(dados.length()) { indice ->
            val filme = dados.getJSONObject(indice)
            Filme(
                filme.getString("id"), filme.getString("nome"),
                filme.getDouble("nota"), filme.getString("fotoUrl")
            )
        }
    }

    fun salvar(email: String, filmes: List<Filme>) {
        val dados = JSONArray()
        filmes.forEach { filme ->
            dados.put(JSONObject().apply {
                put("id", filme.id)
                put("nome", filme.nome)
                put("nota", filme.nota)
                put("fotoUrl", filme.fotoUrl)
            })
        }
        preferencias.edit().putString(email, dados.toString()).apply()
    }
}
