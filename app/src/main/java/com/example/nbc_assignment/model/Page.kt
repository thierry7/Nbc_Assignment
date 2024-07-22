package com.example.nbc_assignment.model

data class Page(
    val page: String,
    val shelves: List<Shelf>
)

data class Shelf(
    val title: String,
    val type: String,
    val items: List<Item>
)

sealed class Item {
    data class Live(
        val type: String,
        val tagline: String,
        val title: String,
        val subtitle: String,
        val image: String
    ) : Item()

    data class Episode(
        val type: String,
        val title: String,
        val subtitle: String,
        val image: String,
        val labelBadge: String?
    ) : Item()

    data class Show(
        val type: String,
        val title: String,
        val image: String
    ) : Item()
}