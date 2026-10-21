package unisanta.br.myapplication.model

import java.net.URI

object ValidadorFilme {
    fun validar(nome: String, nota: String, fotoUrl: String): String? {
        if (nome.isBlank()) return "Informe o nome do filme"
        val valor = nota.trim().replace(',', '.').toDoubleOrNull()
        if (valor == null || !valor.isFinite() || valor !in 0.0..10.0) {
            return "Informe uma nota de 0 a 10"
        }
        val uri = runCatching { URI(fotoUrl.trim()) }.getOrNull()
        if (uri?.scheme != "https" || uri.host.isNullOrBlank()) {
            return "Informe uma URL HTTPS válida para a foto"
        }
        return null
    }
}
