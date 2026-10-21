package unisanta.br.myapplication

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import unisanta.br.myapplication.model.ValidadorFilme

class ValidadorFilmeTest {
    private val foto = "https://example.com/poster.jpg"

    @Test
    fun aceitaLimitesENotaComVirgula() {
        listOf("0", "10", "8,5", "7.2").forEach {
            assertNull(ValidadorFilme.validar("Filme", it, foto))
        }
    }

    @Test
    fun rejeitaNotasInvalidas() {
        listOf("", "abc", "-1", "10.1", "NaN", "Infinity").forEach {
            assertNotNull(ValidadorFilme.validar("Filme", it, foto))
        }
    }

    @Test
    fun rejeitaNomeVazioEFotoSemHttps() {
        assertNotNull(ValidadorFilme.validar("  ", "8", foto))
        listOf("", "imagem", "http://example.com/a.jpg", "https://", "file:///foto").forEach {
            assertNotNull(ValidadorFilme.validar("Filme", "8", it))
        }
    }
}
