package com.example.songme.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

const val ID = "id"
const val KING = "kind"
const val CREATE_AT = "created_at"
const val LAST_MODIfIED = "last_modified"
const val TITLE = "title"
const val DURATION = "duration"
const val STREAM_URL = "stream_url"
const val URI = "uri"
const val IMAGE_URL = "artwork_url"
const val GENRES = "genre"
const val TAG_LIST = "tag_list"
const val USER = "user"
const val AUTHOR = "username"

@Parcelize
data class Track(
    val id: String,
    val king: String,
    val createdAt: String,
    val lastModified: String,
    val title: String,
    val duration: Long,
    val streamUrl: String,
    val uri: String,
    val imageUrl: String?,
    val genres: String,
    val tagList: String,
    val author: String
) : Parcelable {
    constructor(jsonObject: JSONObject) : this(
        id = jsonObject.optString(ID),
        king = jsonObject.optString(KING),
        createdAt = jsonObject.optString(CREATE_AT),
        lastModified = jsonObject.optString(LAST_MODIfIED),
        title = jsonObject.optString(TITLE),
        duration = jsonObject.optString(DURATION).toLong(),
        streamUrl = jsonObject.optString(STREAM_URL),
        uri = jsonObject.optString(URI),
        imageUrl = jsonObject.optString(IMAGE_URL),
        genres = jsonObject.optString(GENRES),
        tagList = jsonObject.optString(TAG_LIST),
        author = jsonObject.getJSONObject(USER).optString(AUTHOR)
    )
}
