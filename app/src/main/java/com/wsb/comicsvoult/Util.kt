package com.wsb.comicsvoult

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.math.BigInteger
import java.security.MessageDigest
import coil.compose.AsyncImage


fun getHash(timestamp: String, privateKey: String, publicKey: String): String {
    val hashInput = "$timestamp$privateKey$publicKey"
    val md = MessageDigest.getInstance("MD5")
    val hashBytes = md.digest(hashInput.toByteArray(Charsets.UTF_8))
    return hashBytes.joinToString("") { "%02x".format(it) }
}

@Composable
fun AttributionText(text: String) {
    Text(text = text, modifier = Modifier.padding(start = 8.dp, top = 4.dp), fontSize = 12.sp)
}

@Composable
fun CharacterImage(
    url: String?,
    modifier: Modifier,
    contentScale: ContentScale = ContentScale.FillWidth
) {
   AsyncImage(
       model = url,
       contentDescription = null,
       modifier = modifier,
       contentScale = contentScale
   )
}

fun List<String>.comicsToString() = this.joinToString(separator = ", ")