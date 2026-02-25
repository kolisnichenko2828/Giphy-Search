package com.kolisnichenko2828.giphysearch.network

import androidx.annotation.Keep
import com.kolisnichenko2828.giphysearch.screens.main.states.GifItemState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GiphyResponseDto(
    @SerialName("data")
    val data: List<GifDto>
)

@Keep
@Serializable
data class GifDto(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String? = null,
    @SerialName("images")
    val images: ImagesDto
)

fun GifDto.toItemState(): GifItemState {
    val width = this.images.fixedHeight.width?.toFloatOrNull() ?: 1f
    val height = this.images.fixedHeight.height?.toFloatOrNull() ?: 1f
    val ratio = if (height > 0f) width / height else 1f

    return GifItemState(
        id = this.id,
        title = this.title?.ifBlank { "No title" } ?: "No title",
        previewUrl = this.images.fixedHeight.url,
        originalUrl = this.images.original.url,
        stillUrl = this.images.fixedHeightStill.url,
        aspectRatio = ratio
    )
}

@Keep
@Serializable
data class ImagesDto(
    @SerialName("original")
    val original: ImageUrlDto,
    @SerialName("fixed_height")
    val fixedHeight: ImageUrlDto,
    @SerialName("fixed_height_still")
    val fixedHeightStill: ImageUrlDto
)

@Keep
@Serializable
data class ImageUrlDto(
    @SerialName("url")
    val url: String,
    @SerialName("width")
    val width: String? = null,
    @SerialName("height")
    val height: String? = null
)

