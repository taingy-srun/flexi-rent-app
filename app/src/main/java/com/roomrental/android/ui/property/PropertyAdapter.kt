package com.roomrental.android.ui.property

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.roomrental.android.R
import com.roomrental.android.data.model.Property
import com.roomrental.android.databinding.ItemPropertyBinding
import java.text.NumberFormat
import java.util.Locale

class PropertyAdapter(
    private val onPropertyClick: (Property) -> Unit
) : ListAdapter<Property, PropertyAdapter.PropertyViewHolder>(PropertyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val binding = ItemPropertyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PropertyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PropertyViewHolder(
        private val binding: ItemPropertyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(property: Property) {
            binding.apply {
                tvTitle.text = property.title
                tvAddress.text = "${property.address}, ${property.city}, ${property.state}"
                val formatter = NumberFormat.getNumberInstance(Locale.US).apply {
                    maximumFractionDigits = 0
                }
                tvPrice.text = "$${formatter.format(property.pricePerMonth)}/month"
                tvBedrooms.text = "${property.bedrooms} bed"
                tvBathrooms.text = "${property.bathrooms} bath"
                tvArea.text = "${formatter.format(property.areaSqft)} sqft"
                chipPropertyType.text = property.propertyType?.name?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "Unknown"

                // Set availability status
                if (property.available) {
                    chipAvailable.visibility = View.VISIBLE
                    chipAvailable.text = "Available"
                    chipAvailable.setChipBackgroundColorResource(R.color.green_light)
                } else {
                    chipAvailable.visibility = View.VISIBLE
                    chipAvailable.text = "Not Available"
                    chipAvailable.setChipBackgroundColorResource(R.color.red_light)
                }

                // Load property image
                val safeImageUrls = property.getSafeImageUrls()
                if (safeImageUrls.isNotEmpty()) {
                    Glide.with(itemView.context)
                        .load(safeImageUrls.first())
                        .placeholder(R.drawable.ic_home)
                        .error(R.drawable.ic_home)
                        .into(ivProperty)
                } else {
                    ivProperty.setImageResource(R.drawable.ic_home)
                }

                root.setOnClickListener {
                    onPropertyClick(property)
                }
            }
        }
    }

    private class PropertyDiffCallback : DiffUtil.ItemCallback<Property>() {
        override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem == newItem
        }
    }
}