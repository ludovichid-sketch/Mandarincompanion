package com.example.mandarincompanion

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

private val Ink = Color(0xFF183B4E)
private val Teal = Color(0xFF218C83)
private val Pale = Color(0xFFF2F7F6)

class MainActivity : ComponentActivity() {
    private var tts: TextToSpeech? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) tts?.language = Locale.SIMPLIFIED_CHINESE
        }
        setContent { MandarinApp { text ->
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "mandarin")
        } }
    }
    override fun onDestroy() { tts?.shutdown(); super.onDestroy() }
}

@Composable
fun MandarinApp(speak: (String) -> Unit) {
    var page by remember { mutableStateOf("Home") }
    var words by remember { mutableIntStateOf(0) }
    MaterialTheme(colorScheme = lightColorScheme(primary = Teal, secondary = Ink, background = Pale)) {
        Scaffold(containerColor = Pale, bottomBar = {
            NavigationBar {
                listOf("Home", "Lessons", "Progress").forEach { item ->
                    NavigationBarItem(selected = page == item, onClick = { page = item },
                        icon = {}, label = { Text(item) })
                }
            }
        }) { pad ->
            when (page) {
                "Lessons" -> Lessons(Modifier.padding(pad)) { page = "Lesson 1" }
                "Lesson 1" -> Lesson(Modifier.padding(pad), speak, { page = "Lessons" }) { words++ }
                "Progress" -> Column(Modifier.padding(pad).padding(20.dp)) {
                    Text("Your progress", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Ink)
                    Text("Words reviewed: $words")
                    Text("Progress saving will be added in a later version.")
                }
                else -> Home(Modifier.padding(pad), { page = "Lesson 1" }, { page = "Lessons" })
            }
        }
    }
}

@Composable
fun Home(modifier: Modifier, start: () -> Unit, lessons: () -> Unit) {
    LazyColumn(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item { Text("你好！", fontSize = 34.sp, fontWeight = FontWeight.Bold, color = Ink); Text("Your Mandarin learning space") }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Ink), shape = RoundedCornerShape(22.dp)) {
                Column(Modifier.padding(20.dp)) {
                    Text("TODAY'S LEARNING GOAL", color = Color.White)
                    Text("30 minutes", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    Button(onClick = start) { Text("Start learning") }
                }
            }
        }
        item {
            Card(Modifier.fillMaxWidth().clickable { lessons() }) {
                Column(Modifier.padding(18.dp)) {
                    Text("HSK 2 · Lesson 1", color = Teal, fontWeight = FontWeight.Bold)
                    Text("九月去北京旅游最好", fontSize = 23.sp, color = Ink)
                    Text("September is the best time to visit Beijing")
                    Text("Open lesson →", color = Teal)
                }
            }
        }
        item { Text("Focus: speaking, listening, vocabulary and grammar. No handwriting drills.", color = Color.Gray) }
    }
}

@Composable
fun Lessons(modifier: Modifier, open: () -> Unit) {
    Column(modifier.fillMaxSize().padding(20.dp)) {
        Text("Your HSK 2 course", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Ink)
        Text("15 lessons · textbook order")
        Spacer(Modifier.height(16.dp))
        Card {
            Column(Modifier.padding(18.dp)) {
                Text("Lesson 1", color = Teal, fontWeight = FontWeight.Bold)
                Text("September is the best time to visit Beijing")
                Text("九月去北京旅游最好")
                Button(onClick = open) { Text("Open lesson") }
            }
        }
        Spacer(Modifier.height(12.dp))
        Text("Lessons 2–15 will be added after extracting and checking the scanned course materials.", color = Color.Gray)
    }
}

@Composable
fun Lesson(modifier: Modifier, speak: (String) -> Unit, back: () -> Unit, learned: () -> Unit) {
    var index by remember { mutableIntStateOf(0) }
    val cards = listOf(Triple("你好", "nǐ hǎo", "hello"), Triple("谢谢", "xièxie", "thank you"), Triple("再见", "zàijiàn", "goodbye"))
    Column(modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.Start) {
        TextButton(onClick = back) { Text("← Back to lessons") }
        Text("HSK 2 · Lesson 1", color = Teal, fontWeight = FontWeight.Bold)
        Text("Vocabulary practice", fontSize = 27.sp, color = Ink, fontWeight = FontWeight.Bold)
        Text("Demonstration cards; not yet extracted from your textbook.", color = Color.Gray, fontSize = 12.sp)
        Spacer(Modifier.height(20.dp))
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(cards[index].first, fontSize = 48.sp, color = Ink, fontWeight = FontWeight.Bold)
                Text(cards[index].second, fontSize = 22.sp, color = Teal)
                Text(cards[index].third, fontSize = 18.sp, color = Color.Gray)
                Button(onClick = { speak(cards[index].first) }) { Text("▶ Hear pronunciation") }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { index = (index - 1 + cards.size) % cards.size }) { Text("Previous") }
            Button(onClick = { index = (index + 1) % cards.size; learned() }) { Text("Next word") }
        }
    }
}
