package unisanta.br.myapplication.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import unisanta.br.myapplication.R
import unisanta.br.myapplication.databinding.ItemFilmeBinding
import unisanta.br.myapplication.model.Filme

class FilmeAdapter(private val aoEditar: (Filme) -> Unit) :
    ListAdapter<Filme, FilmeAdapter.FilmeViewHolder>(Comparador) {

    class FilmeViewHolder(val binding: ItemFilmeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FilmeViewHolder(
        ItemFilmeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        val filme = getItem(position)
        with(holder.binding) {
            txtNome.text = filme.nome
            txtNota.text = root.context.getString(R.string.nota_filme, filme.nota)
            imgFoto.contentDescription = root.context.getString(R.string.foto_filme, filme.nome)
            imgFoto.load(filme.fotoUrl) {
                crossfade(true)
                placeholder(android.R.drawable.ic_menu_gallery)
                error(android.R.drawable.ic_menu_report_image)
            }
            btnEditar.setOnClickListener { aoEditar(filme) }
        }
    }

    private object Comparador : DiffUtil.ItemCallback<Filme>() {
        override fun areItemsTheSame(oldItem: Filme, newItem: Filme) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Filme, newItem: Filme) = oldItem == newItem
    }
}
