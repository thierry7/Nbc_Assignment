package com.example.nbc_assignment.utils

import com.example.nbc_assignment.model.Item
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class ItemDeserializer : JsonDeserializer<Item> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Item {
        val jsonObject = json?.asJsonObject
        val type = jsonObject?.get("type")?.asString

        return when (type) {
            "Live" -> context?.deserialize(jsonObject, Item.Live::class.java)!!
            "Episode" -> context?.deserialize(jsonObject, Item.Episode::class.java)!!
            "Show" -> context?.deserialize(jsonObject, Item.Show::class.java)!!
            else -> throw JsonParseException("Unknown element type: $type")
        }
    }
}