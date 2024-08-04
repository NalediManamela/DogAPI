package com.sir.dogapi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FavouritesAdapter : RecyclerView.Adapter<FavouritesAdapter.FavouriteViewHolder>() {

    private var favourites: List<Favourite> = emptyList()

    fun submitList(favourites: List<Favourite>) {
        this.favourites = favourites
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favourite, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val favourite = favourites[position]
        Glide.with(holder.itemView.context).load(favourite.image.url).into(holder.imageView)
    }

    override fun getItemCount(): Int = favourites.size

    inner class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }
}
