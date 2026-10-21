package unisanta.br.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import unisanta.br.myapplication.model.Usuario

class LoginViewModel: ViewModel() {
    private val usuarios = mutableListOf<Usuario>()
    var autenticado = false
        private set

    fun entrar(email:String, senha:String):String{
        val usuario = usuarios.find { it.email == email.trim().lowercase(java.util.Locale.ROOT)
                && it.senha == senha }
        autenticado = usuario != null
        if(usuario!=null){
            return "Usuário Pode Logar!"
        }
        return "Usuário Não Cadastrado"
    }
    fun cadastrar(email: String, senha: String) :String{
        val emailNormalizado = email.trim().lowercase(java.util.Locale.ROOT)
        if (emailNormalizado.isBlank() || senha.isBlank()) return "Preencha e-mail e senha"
        if (usuarios.any { it.email == emailNormalizado }) return "E-mail já cadastrado"
        val usuario = Usuario(emailNormalizado, senha)
        usuarios.add(usuario)
        return "Usuário Cadastrado";
    }

}
