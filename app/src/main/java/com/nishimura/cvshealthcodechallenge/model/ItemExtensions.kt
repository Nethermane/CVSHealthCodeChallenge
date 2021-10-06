package com.nishimura.cvshealthcodechallenge.model

fun Item?.getTitleOrUntitled(): String = this?.takeIf { it.title.isNotBlank() }?.title ?: "Untitled"

fun Item.parseDescriptionFromHtml(): String? =
    description
        .takeIf { "<p>".toRegex().findAll(it).count() > 2 }
        ?.lastIndexOf("<p>")
        ?.let { description.substring(it) }

fun Item.parseWidthFromHtml(): Int? =
    "width=\"\\d*\"".toRegex()
        .find(description)?.value?.replace(Regex("\\D"), "")?.toIntOrNull()

fun Item.parseHeightFromHtml(): Int? =
    "height=\"\\d*\"".toRegex()
        .find(description)?.value?.replace(Regex("\\D"), "")?.toIntOrNull()