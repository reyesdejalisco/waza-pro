
package com.reyes.wazapro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

data class Mensaje(val texto: String, val esMio: Boolean, val hora: String, val bomba: Boolean = false, val autodestruir: Int = 0)
data class Chat(val id: Int, val nombre: String, val ultimo: String, val avatar: String, val noLeidos: Int, val enLinea: Boolean)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { WazaProApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WazaProApp() {
    var chatSeleccionado by remember { mutableStateOf<Chat?>(null) }
    var modoFantasma by remember { mutableStateOf(false) }
    var tema by remember { mutableStateOf(0) }
    val colores = listOf(Color(0xFF25D366), Color(0xFF6C5CE7), Color(0xFFE84393))

    val chats = remember {
        listOf(
            Chat(1, "Jefa ❤️", "Ya llegaste?", "👩", 2, true),
            Chat(2, "Grupo Cotorreo Tlajo", "El compa: al rato las chelas", "🍻", 12, false),
            Chat(3, "Morra", "Te extraño bb", "😍", 1, true),
            Chat(4, "Compa del jale", "Ya quedó el jale", "👷", 0, true),
            Chat(5, "Mamá", "Hijo come", "👩‍🍳", 0, false),
            Chat(6, "Cliente", "Cuánto cuesta?", "💰", 3, true),
            Chat(7, "Bro", "On en el warzone?", "🎮", 0, true),
            Chat(8, "Barbero", "Te espero a las 5", "💈", 0, false)
        )
    }

    MaterialTheme(colorScheme = darkColorScheme(container = Color(0xFF0A0A0A), surface = Color(0xFF121212))) {
        if (chatSeleccionado == null) {
            // LISTA DE CHATS
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("WAZA+ PRO", fontWeight = FontWeight.Black, letterSpacing = 2.sp) },
                        actions = {
                            IconButton(onClick = { modoFantasma = !modoFantasma }) { Text(if(modoFantasma) "👻" else "👁️") }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0A0A0A))
                    )
                },
                bottomBar = {
                    NavigationBar(containerColor = Color(0xFF121212)) {
                        NavigationBarItem(selected = true, onClick = {}, icon = { Text("💬") }, label = { Text("Chats") })
                        NavigationBarItem(selected = false, onClick = {}, icon = { Text("🟢") }, label = { Text("Estados") })
                        NavigationBarItem(selected = false, onClick = {}, icon = { Text("📞") }, label = { Text("Llamadas") })
                        NavigationBarItem(selected = false, onClick = {}, icon = { Text("🤖") }, label = { Text("IA") })
                    }
                },
                containerColor = Color(0xFF0A0A0A)
            ) { pad ->
                LazyColumn(Modifier.padding(pad).fillMaxSize().background(Color(0xFF0A0A0A))) {
                    items(chats) { chat ->
                        Row(
                            Modifier.fillMaxWidth().clickable { chatSeleccionado = chat }.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(Modifier.size(56.dp).clip(CircleShape).background(colores[chat.id % 3]), contentAlignment = Alignment.Center) {
                                Text(chat.avatar, fontSize = 24.sp)
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(chat.nombre, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text(SimpleDateFormat("HH:mm").format(Date()), color = Color.Gray, fontSize = 12.sp)
                                }
                                Text(chat.ultimo, color = Color.Gray, maxLines = 1, fontSize = 13.sp)
                            }
                            if (chat.noLeidos > 0) {
                                Box(Modifier.size(22.dp).clip(CircleShape).background(Color(0xFF25D366)), contentAlignment = Alignment.Center) {
                                    Text("${chat.noLeidos}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // CHAT ABIERTO
            var texto by remember { mutableStateOf("") }
            var mensajes by remember { mutableStateOf(listOf(
                Mensaje("Qué onda ${chatSeleccionado!!.nombre}", false, "10:23"),
                Mensaje("Aquí andamos al tiro", true, "10:24"),
                Mensaje("Te mando la ubi", false, "10:24", bomba = true)
            )) }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { 
                            Column {
                                Text(chatSeleccionado!!.nombre, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text(if(chatSeleccionado!!.enLinea) "En línea • Modo fantasma ${if(modoFantasma) "ON" else "OFF"}" else "Visto hoy", fontSize = 11.sp, color = Color(0xFF25D366))
                            }
                        },
                        navigationIcon = { IconButton(onClick = { chatSeleccionado = null }) { Text("←") } },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF121212))
                    )
                },
                containerColor = Color(0xFF0A0A0A),
                bottomBar = {
                    Row(Modifier.fillMaxWidth().background(Color(0xFF121212)).padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        TextField(
                            value = texto,
                            onValueChange = { texto = it },
                            placeholder = { Text("Mensaje WAZA+...") },
                            modifier = Modifier.weight(1f).clip(RoundedCornerShape(24.dp)),
                            colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFF1E1E1E), unfocusedContainerColor = Color(0xFF1E1E1E))
                        )
                        Spacer(Modifier.width(8.dp))
                        Box(Modifier.size(48.dp).clip(CircleShape).background(Color(0xFF25D366)).clickable {
                            if(texto.isNotBlank()){
                                mensajes = mensajes + Mensaje(texto, true, SimpleDateFormat("HH:mm").format(Date()))
                                texto = ""
                            }
                        }, contentAlignment = Alignment.Center) { Text("➤") }
                    }
                }
            ) { pad ->
                LazyColumn(Modifier.padding(pad).fillMaxSize().padding(12.dp).background(Color(0xFF0A0A0A)), reverseLayout = true) {
                    items(mensajes.reversed()) { m ->
                        Box(Modifier.fillMaxWidth().padding(vertical = 4.dp), contentAlignment = if(m.esMio) Alignment.CenterEnd else Alignment.CenterStart) {
                            Surface(
                                shape = RoundedCornerShape(18.dp),
                                color = if(m.esMio) Color(0xFF25D366) else Color(0xFF1E1E1E),
                                modifier = Modifier.widthIn(max = 280.dp)
                            ) {
                                Column(Modifier.padding(12.dp)) {
                                    if(m.bomba) Text("💣 MENSAJE BOMBA", color = Color.Red, fontWeight = FontWeight.Black, fontSize = 10.sp)
                                    Text(m.texto, color = if(m.esMio) Color.Black else Color.White)
                                    Text(m.hora, fontSize = 10.sp, color = Color.Gray, modifier = Modifier.align(Alignment.End))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
