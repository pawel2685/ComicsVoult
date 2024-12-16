package com.wsb.comicsvoult

import android.util.Log
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
    val hashStr = timestamp + privateKey + publicKey
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(hashStr.toByteArray()))
        .toString(16)
        .padStart(32,'0')
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
