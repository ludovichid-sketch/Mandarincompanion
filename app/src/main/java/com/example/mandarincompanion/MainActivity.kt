package com.example.mandarincompanion

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

private val Ink = Color(0xFF173B4D)
private val Teal = Color(0xFF218C83)
private val Pale = Color(0xFFF3F7F6)
private val SoftTeal = Color(0xFFE2F1EE)
private val SoftBlue = Color(0xFFEAF0F5)
private val Good = Color(0xFF287A53)
private val Bad = Color(0xFFB34747)

class MainActivity : ComponentActivity() {
    private var tts: TextToSpeech? = null
    private var ttsAvailable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.SIMPLIFIED_CHINESE)
                ttsAvailable = result != TextToSpeech.LANG_MISSING_DATA &&
                    result != TextToSpeech.LANG_NOT_SUPPORTED
                tts?.setSpeechRate(0.88f)
            }
        }
        setContent {
            MandarinApp { phrase ->
                if (ttsAvailable) {
                    tts?.speak(phrase, TextToSpeech.QUEUE_FLUSH, null, "mandarin-${phrase.hashCode()}")
                } else {
                    Toast.makeText(this, "Install a Chinese (Mandarin) voice in Android Text-to-Speech settings.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}

private data class DialogueLine(val chinese: String, val pinyin: String, val english: String)
private data class Dialogue(val title: String, val scene: String, val lines: List<DialogueLine>)
private data class Word(
    val hanzi: String,
    val pinyin: String,
    val meaning: String,
    val example: String,
    val examplePinyin: String,
    val exampleEnglish: String,
    val core: Boolean = true
)
private data class GrammarPoint(
    val title: String,
    val explanation: String,
    val pattern: String,
    val examples: List<DialogueLine>
)
private data class QuizQuestion(
    val prompt: String,
    val options: List<String>,
    val answer: Int,
    val explanation: String,
    val source: String
)

private val dialogues = listOf(
    Dialogue(
        "1 · Planning a trip", "In the school · Textbook dialogue 01-1", listOf(
            DialogueLine("我 要 去 北京 旅游，你 觉得 什么 时候 去 最 好？", "Wǒ yào qù Běijīng lǚyóu, nǐ juéde shénme shíhou qù zuì hǎo?", "I'm thinking of a trip to Beijing. When do you think is the best time?"),
            DialogueLine("九 月 去 北京 旅游 最 好。", "Jiǔ yuè qù Běijīng lǚyóu zuì hǎo.", "September is the best time to visit Beijing."),
            DialogueLine("为 什么？", "Wèi shénme?", "Why?"),
            DialogueLine("九 月 的 北京 天气 不 冷 也 不 热。", "Jiǔ yuè de Běijīng tiānqì bù lěng yě bù rè.", "Because the weather in Beijing in September is neither cold nor hot.")
        )
    ),
    Dialogue(
        "2 · Favourite sport", "Looking at a picture · Textbook dialogue 01-2", listOf(
            DialogueLine("你 喜欢 什么 运动？", "Nǐ xǐhuan shénme yùndòng?", "What sport do you like?"),
            DialogueLine("我 最 喜欢 踢 足球。", "Wǒ zuì xǐhuan tī zúqiú.", "I like playing football the most."),
            DialogueLine("下 午 我们 一起 去 踢 足球 吧。", "Xiàwǔ wǒmen yìqǐ qù tī zúqiú ba.", "Let's go and play football together this afternoon."),
            DialogueLine("好 啊！", "Hǎo a!", "Great!")
        )
    ),
    Dialogue(
        "3 · Buying chairs", "At home · Textbook dialogue 01-3", listOf(
            DialogueLine("我们 要 不 要 买 几 个 新 的 椅子？", "Wǒmen yào bu yào mǎi jǐ ge xīn de yǐzi?", "Don't you think we should buy some new chairs?"),
            DialogueLine("好 啊。什么 时候 去 买？", "Hǎo a. Shénme shíhou qù mǎi?", "Yes. When shall we go to buy them?"),
            DialogueLine("明天 下 午 怎么 样？", "Míngtiān xiàwǔ zěnmeyàng?", "What about tomorrow afternoon?"),
            DialogueLine("你 明天 几 点 能 回来？", "Nǐ míngtiān jǐ diǎn néng huílai?", "What time can you come back tomorrow?"),
            DialogueLine("三 点 多。", "Sān diǎn duō.", "A little after three.")
        )
    ),
    Dialogue(
        "4 · The cat Huahua", "At home · Textbook dialogue 01-4", listOf(
            DialogueLine("桌子 下 面 有 个 猫。", "Zhuōzi xiàmiàn yǒu ge māo.", "There is a cat under the table."),
            DialogueLine("那 是 我 的 猫，它 叫 花花。", "Nà shì wǒ de māo, tā jiào Huāhua.", "That's my cat. Its name is Huahua."),
            DialogueLine("它 很 漂亮。", "Tā hěn piàoliang.", "It's beautiful."),
            DialogueLine("是 啊，我 觉得 它 的 眼睛 最 漂亮。", "Shì a, wǒ juéde tā de yǎnjing zuì piàoliang.", "Yes. I think its eyes are the most beautiful."),
            DialogueLine("它 多 大 了？", "Tā duō dà le?", "How old is it?"),
            DialogueLine("六 个 多 月。", "Liù ge duō yuè.", "More than six months.")
        )
    )
)

private val coreWords = listOf(
    Word("旅游", "lǚyóu", "to travel; to take a trip", "我 要 去 北京 旅游。", "Wǒ yào qù Běijīng lǚyóu.", "I want to travel to Beijing."),
    Word("觉得", "juéde", "to think; to feel", "你 觉得 什么 时候 去 最 好？", "Nǐ juéde shénme shíhou qù zuì hǎo?", "When do you think is the best time?"),
    Word("最", "zuì", "most; the -est", "我 最 喜欢 踢 足球。", "Wǒ zuì xǐhuan tī zúqiú.", "I like playing football the most."),
    Word("为什么", "wèishénme", "why", "为 什么？", "Wèi shénme?", "Why?"),
    Word("也", "yě", "also; too", "天 气 不 冷 也 不 热。", "Tiānqì bù lěng yě bù rè.", "The weather is neither cold nor hot."),
    Word("运动", "yùndòng", "sport; to exercise", "你 喜欢 什么 运动？", "Nǐ xǐhuan shénme yùndòng?", "What sport do you like?"),
    Word("踢足球", "tī zúqiú", "to play football", "我 喜欢 踢 足球。", "Wǒ xǐhuan tī zúqiú.", "I like playing football."),
    Word("一起", "yìqǐ", "together", "我们 一起 去。", "Wǒmen yìqǐ qù.", "Let's go together."),
    Word("要", "yào", "to want to; would like to", "我们 要 不 要 买 新 椅子？", "Wǒmen yào bu yào mǎi xīn yǐzi?", "Should we buy new chairs?"),
    Word("新", "xīn", "new", "新 的 椅子。", "Xīn de yǐzi.", "New chairs."),
    Word("它", "tā", "it (for an animal or thing)", "它 叫 花花。", "Tā jiào Huāhua.", "Its name is Huahua."),
    Word("眼睛", "yǎnjing", "eye; eyes", "它 的 眼睛 最 漂亮。", "Tā de yǎnjing zuì piàoliang.", "Its eyes are the most beautiful.")
)

private val extraWords = listOf(
    Word("猫", "māo", "cat", "那 是 我 的 猫。", "Nà shì wǒ de māo.", "That is my cat.", false),
    Word("椅子", "yǐzi", "chair", "买 几 个 新 的 椅子。", "Mǎi jǐ ge xīn de yǐzi.", "Buy some new chairs.", false),
    Word("天气", "tiānqì", "weather", "北京 的 天气 不 冷。", "Běijīng de tiānqì bù lěng.", "The weather in Beijing is not cold.", false),
    Word("花花", "Huāhua", "Huahua (the cat's name)", "它 叫 花花。", "Tā jiào Huāhua.", "Its name is Huahua.", false)
)

private val grammarPoints = listOf(
    GrammarPoint(
        "1. 要 — expressing a wish or intention",
        "要 is placed before a verb to say that someone wants or would like to do something. The textbook notes that its negative form is usually 不想.",
        "Subject + 要 + verb  /  Subject + 不想 + verb",
        listOf(
            DialogueLine("我 要 去 北京。", "Wǒ yào qù Běijīng.", "I want to go to Beijing."),
            DialogueLine("我 不 想 买 椅子。", "Wǒ bù xiǎng mǎi yǐzi.", "I don't want to buy chairs.")
        )
    ),
    GrammarPoint(
        "2. 最 — the highest degree",
        "最 marks the top degree within a group or in a particular respect. It commonly appears before a verb or adjective phrase.",
        "Subject + 最 + verb / adjective",
        listOf(
            DialogueLine("我 最 喜欢 踢 足球。", "Wǒ zuì xǐhuan tī zúqiú.", "I like playing football the most."),
            DialogueLine("它 的 眼睛 最 漂亮。", "Tā de yǎnjing zuì piàoliang.", "Its eyes are the most beautiful.")
        )
    ),
    GrammarPoint(
        "3. 几 and 多 — approximate numbers",
        "几 with a measure word can indicate an indefinite number below ten (for example 几个人). 十几 means a little over ten; 几十 means several tens. With a number below ten, 多 follows the measure word (六个多月). With a whole number above ten, 多 goes before the measure word (十多个月).",
        "几 + measure word + noun  /  number + measure word + 多 + noun",
        listOf(
            DialogueLine("我们 要 不 要 买 几 个 新 的 椅子？", "Wǒmen yào bu yào mǎi jǐ ge xīn de yǐzi?", "Should we buy a few new chairs?"),
            DialogueLine("六 个 多 月。", "Liù ge duō yuè.", "More than six months.")
        )
    ),

)

private val quizQuestions = listOf(
    QuizQuestion("According to Dialogue 1, when is the best time to visit Beijing?", listOf("January", "June", "September", "December"), 2, "九月去北京旅游最好。 September is best.", "Textbook lesson 1, dialogue 01-1"),
    QuizQuestion("Why is September recommended?", listOf("It is neither cold nor hot", "It always snows", "The city is empty", "It is the cheapest month"), 0, "九月的北京天气不冷也不热。 The weather is neither cold nor hot.", "Textbook lesson 1, dialogue 01-1"),
    QuizQuestion("What do the speakers plan to do this afternoon?", listOf("Go travelling", "Play football together", "Buy a cat", "Study English"), 1, "下午我们一起去踢足球吧 — let's go and play football together this afternoon.", "Textbook lesson 1, comprehension exercise 2"),
    QuizQuestion("When do they suggest buying the new chairs?", listOf("This evening", "Tomorrow morning", "Tomorrow afternoon", "Next week"), 2, "明天下午怎么样？ — What about tomorrow afternoon?", "Textbook lesson 1, comprehension exercise 3"),
    QuizQuestion("Where is Huahua?", listOf("On the chair", "Under the table", "At school", "In the garden"), 1, "桌子下面有个猫 — there is a cat under the table.", "Textbook lesson 1, comprehension exercise 4"),
    QuizQuestion("How old is Huahua?", listOf("Six days", "Exactly six months", "More than six months", "Six years"), 2, "六个多月 means a little over six months.", "Textbook lesson 1, comprehension exercise 5"),
    QuizQuestion("What does 觉得 (juéde) mean?", listOf("to travel", "to think / to feel", "together", "new"), 1, "觉得 means to think or to feel.", "Textbook lesson 1, new word 2"),
    QuizQuestion("Complete: 王方（___）买一个新杯子。", listOf("为什么", "要", "眼睛", "也"), 1, "要 is placed before a verb to express wanting to do something.", "Workbook lesson 1, fill-in exercise 21"),
    QuizQuestion("Complete: 昨天你（___）没来我家吃饭？", listOf("最", "新", "为什么", "一起"), 2, "为什么 asks 'why'.", "Workbook lesson 1, fill-in exercise 22"),
    QuizQuestion("Complete: 我（___）这个衣服太大了。", listOf("觉得", "旅游", "踢足球", "眼睛"), 0, "觉得 means 'I think / feel that…'.", "Workbook lesson 1, fill-in exercise 23"),
    QuizQuestion("Complete: 我的小猫两岁多了，大卫的小猫（___）两岁多了。", listOf("为什么", "也", "最", "新"), 1, "也 means 'also / too'.", "Workbook lesson 1, fill-in exercise 24"),
    QuizQuestion("Complete: 王老师（___）喜欢吃苹果。", listOf("最", "眼睛", "一起", "猫"), 0, "最 expresses the highest degree: likes something the most.", "Workbook lesson 1, fill-in exercise 25"),
    QuizQuestion("What does 六个多月 mean?", listOf("exactly six days", "less than six months", "more than six months", "six years"), 2, "For numbers below ten, 多 follows the measure word: 六个多月.", "Textbook lesson 1, dialogue 01-4"),
    QuizQuestion("What is the usual negative form given for 要 in this lesson?", listOf("不想", "没有", "不在", "不会"), 0, "The textbook says the negative form is usually 不想.", "Textbook lesson 1, grammar note 1"),
    QuizQuestion("True or false: The speaker says they can dance, but not very well. The statement 'I dance extremely well' matches that meaning.", listOf("True", "False"), 1, "不怎么样 means 'not very well', so the statement is false.", "Workbook lesson 1, true/false exercise"),
    QuizQuestion("True or false: 'Miss Wang's kitten is at my house; my kitten is at my mother's house.' The speaker says they have a kitten.", listOf("True", "False"), 0, "The sentence explicitly says 我的小猫在我妈妈家 — my kitten is at my mother's home.", "Workbook lesson 1, true/false exercise"),
    QuizQuestion("True or false: 'I don't want to buy a table; I want to buy some new chairs.' This means I want to go to the store to buy a table.", listOf("True", "False"), 1, "The speaker wants chairs, not a table.", "Workbook lesson 1, true/false exercise"),
    QuizQuestion("True or false: 'I heard you are learning to cook Chinese food. Let's cook together.' They want to cook Chinese food together.", listOf("True", "False"), 0, "我们一起做吧 means 'let's do it together'.", "Workbook lesson 1, true/false exercise"),
    QuizQuestion("True or false: 'August is hot in Beijing; September's weather is good. Come in September.' August is the best time to visit Beijing.", listOf("True", "False"), 1, "The recommendation is September, not August.", "Workbook lesson 1, true/false exercise"),
    QuizQuestion("Match the reply: 桌子上有一本新书。那是你的书吗？", listOf("不是我的，我没买书。", "三岁多。", "我没时间，明天吧。", "他已经走了，十分钟后能到。"), 0, "The reply 'It's not mine; I didn't buy a book' answers whether the book belongs to the speaker.", "Workbook lesson 1, matching exercise"),
    QuizQuestion("Match the reply: 王老师什么时候能到？", listOf("我不想开车。", "他已经走了，十分钟后能到。", "三岁多。", "不是我的。"), 1, "The reply gives an arrival estimate: he has left and can arrive in ten minutes.", "Workbook lesson 1, matching exercise"),
    QuizQuestion("Match the reply: 这个小猫很漂亮。它多大了？", listOf("我没时间，明天吧。", "三岁多。", "不是我的。", "我觉得坐出租车很好。"), 1, "三岁多 answers the question about the cat's age.", "Workbook lesson 1, matching exercise"),
    QuizQuestion("Match the reply: 你为什么不开车去学校？", listOf("我不想开，我觉得坐出租车很好。", "他已经走了。", "三岁多。", "不是我的。"), 0, "The reply gives a reason for not driving: the speaker prefers taking a taxi.", "Workbook lesson 1, matching exercise"),
    QuizQuestion("Match the reply: 他们都去踢足球了，你去不去？", listOf("不是我的。", "三岁多。", "我没时间，明天吧。", "他已经走了。"), 2, "我没时间，明天吧 means 'I'm busy; how about tomorrow?'.", "Workbook lesson 1, matching exercise")
)

@Composable
private fun MandarinApp(speak: (String) -> Unit) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("mandarin_companion", android.content.Context.MODE_PRIVATE) }
    var page by rememberSaveable { mutableStateOf("Home") }
    var lessonTab by rememberSaveable { mutableIntStateOf(0) }
    var completed by rememberSaveable { mutableStateOf(prefs.getBoolean("lesson1_complete", false)) }
    var bestScore by rememberSaveable { mutableIntStateOf(prefs.getInt("lesson1_score", 0)) }
    val knownWords = remember { mutableStateListOf<String>().apply { addAll(prefs.getStringSet("known_words", mutableSetOf<String>()) ?: emptySet()) } }

    MaterialTheme(colorScheme = lightColorScheme(primary = Teal, secondary = Ink, background = Pale, surface = Color.White)) {
        Scaffold(
            containerColor = Pale,
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
                    listOf("Home", "Lesson", "Progress").forEach { item ->
                        val selected = page == item || (item == "Lesson" && page == "Lesson")
                        NavigationBarItem(
                            selected = selected,
                            onClick = { page = item },
                            icon = { Text(when (item) { "Home" -> "⌂"; "Lesson" -> "书"; else -> "✓" }, fontSize = 18.sp) },
                            label = { Text(item) }
                        )
                    }
                }
            }
        ) { padding ->
            when (page) {
                "Lesson" -> LessonScreen(
                    Modifier.padding(padding), lessonTab, { lessonTab = it }, speak,
                    knownWords.toSet(), { word ->
                        if (knownWords.contains(word)) knownWords.remove(word) else knownWords.add(word)
                        prefs.edit().putStringSet("known_words", knownWords.toSet()).apply()
                    },
                    onQuizFinished = { score ->
                        completed = true
                        if (score > bestScore) bestScore = score
                        prefs.edit().putBoolean("lesson1_complete", true).putInt("lesson1_score", bestScore).apply()
                    }
                )
                "Progress" -> ProgressScreen(Modifier.padding(padding), completed, bestScore, coreWords.count { knownWords.contains(it.hanzi) }, { page = "Lesson" })
                else -> HomeScreen(Modifier.padding(padding), completed, bestScore, coreWords.count { knownWords.contains(it.hanzi) }, { page = "Lesson" })
            }
        }
    }
}

@Composable
private fun HomeScreen(modifier: Modifier, completed: Boolean, score: Int, knownCount: Int, openLesson: () -> Unit) {
    LazyColumn(modifier.fillMaxSize().padding(horizontal = 20.dp), contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text("MANDARIN COMPANION", color = Teal, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.5.sp)
            Text("你好！", color = Ink, fontSize = 36.sp, fontWeight = FontWeight.Bold)
            Text("Your HSK 2 course, built from your uploaded books.", color = Ink.copy(alpha = 0.72f), fontSize = 16.sp)
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Ink), shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("READY TO STUDY", color = Color.White.copy(alpha = 0.76f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("Lesson 1 · 九月去北京旅游最好", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("4 dialogues · 12 textbook words · 3 grammar points · textbook and workbook practice", color = Color.White.copy(alpha = 0.88f), fontSize = 14.sp)
                    Button(onClick = openLesson, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF70C9B7), contentColor = Ink)) { Text(if (completed) "Continue lesson" else "Start Lesson 1") }
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                MiniStat("Words known", "$knownCount / 12", Modifier.weight(1f))
                MiniStat("Best quiz", "$score / ${quizQuestions.size}", Modifier.weight(1f))
            }
        }
        item {
            SectionHeading("Course contents")
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp)) {
                    Text("01  September is the best time to visit Beijing", color = Ink, fontWeight = FontWeight.Bold)
                    Text("九月去北京旅游最好", color = Teal, fontSize = 18.sp)
                    Text(if (completed) "Lesson 1 completed" else "Available now · extracted from your PDFs", color = Color.Gray, fontSize = 13.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("Lessons 2–15 will be added after their textbook and workbook pages are extracted and checked. They are not demo lessons.", color = Color.Gray, fontSize = 13.sp)
                }
            }
        }
        item {
            SectionHeading("Learning approach")
            Text("Focus on character recognition, vocabulary, pinyin, grammar, listening and speaking. No handwriting or stroke-order drills.", color = Ink.copy(alpha = 0.78f))
        }
        item {
            Text("Pronunciation uses your phone's installed Mandarin Text-to-Speech voice. The scanned PDFs reference audio tracks, but the audio files themselves were not included.", color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
private fun MiniStat(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(14.dp)) {
            Text(title, color = Color.Gray, fontSize = 12.sp)
            Text(value, color = Ink, fontWeight = FontWeight.Bold, fontSize = 21.sp)
        }
    }
}

@Composable
private fun SectionHeading(text: String) {
    Text(text, color = Ink, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
}

@Composable
private fun LessonScreen(
    modifier: Modifier,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    speak: (String) -> Unit,
    knownWords: Set<String>,
    onToggleKnown: (String) -> Unit,
    onQuizFinished: (Int) -> Unit
) {
    val tabs = listOf("Dialogues", "Vocabulary", "Grammar", "Practice")
    Column(modifier.fillMaxSize()) {
        Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 8.dp)) {
            TextButton(onClick = { onTabSelected(0) }, contentPadding = PaddingValues(0.dp)) { Text("HSK 2  /  LESSON 1", color = Teal, fontWeight = FontWeight.Bold) }
            Text("September is the best time to visit Beijing", color = Ink, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text("九月去北京旅游最好", color = Teal, fontSize = 21.sp)
        }
        ScrollableTabRow(selectedTabIndex = selectedTab, edgePadding = 12.dp, containerColor = Pale) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedTab == index, onClick = { onTabSelected(index) }, text = { Text(title) })
            }
        }
        when (selectedTab) {
            0 -> DialoguesTab(Modifier.weight(1f), speak)
            1 -> VocabularyTab(Modifier.weight(1f), speak, knownWords, onToggleKnown)
            2 -> GrammarTab(Modifier.weight(1f), speak)
            else -> PracticeTab(Modifier.weight(1f), speak, onQuizFinished)
        }
    }
}

@Composable
private fun DialoguesTab(modifier: Modifier, speak: (String) -> Unit) {
    LazyColumn(modifier.fillMaxSize().padding(horizontal = 16.dp), contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            InfoPanel("How to use this section", "Read the Chinese line first, check the pinyin and English meaning, then tap Listen. Try saying the line aloud before playing it again.")
        }
        items(dialogues) { dialogue ->
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(dialogue.title, color = Ink, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(dialogue.scene, color = Teal, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    dialogue.lines.forEachIndexed { index, line ->
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(line.chinese, color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 19.sp)
                            Text(line.pinyin, color = Teal, fontSize = 14.sp)
                            Text(line.english, color = Ink.copy(alpha = 0.76f), fontSize = 14.sp)
                            TextButton(onClick = { speak(line.chinese) }, contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)) { Text("▶  Listen to line ${index + 1}") }
                        }
                        if (index != dialogue.lines.lastIndex) HorizontalDivider(color = Pale)
                    }
                }
            }
        }
        item {
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Speaking prompts", color = Ink, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    val prompts = listOf(
                        Triple("你 最 喜欢 什么 运动？", "Nǐ zuì xǐhuan shénme yùndòng?", "What sport do you like most?"),
                        Triple("你 最 想 去 什么 地方 旅游？", "Nǐ zuì xiǎng qù shénme dìfang lǚyóu?", "Where do you most want to travel?"),
                        Triple("为 什么？", "Wèi shénme?", "Why?")
                    )
                    prompts.forEach { (hanzi, pinyin, english) ->
                        Text(hanzi, color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                        Text(pinyin, color = Teal, fontSize = 13.sp)
                        Text(english, color = Color.Gray, fontSize = 13.sp)
                        TextButton(onClick = { speak(hanzi) }, contentPadding = PaddingValues(horizontal = 0.dp)) { Text("▶ Listen") }
                    }
                }
            }
        }
        item {
            InfoPanel("Audio note", "The workbook refers to audio tracks 01-1 and 01-2, but the uploaded PDFs contain scanned pages only. This app reads the textbook lines using your device's Mandarin TTS voice; it is not the original publisher recording.")
        }
        item { SourceNote() }
    }
}

@Composable
private fun VocabularyTab(modifier: Modifier, speak: (String) -> Unit, knownWords: Set<String>, onToggleKnown: (String) -> Unit) {
    LazyColumn(modifier.fillMaxSize().padding(horizontal = 16.dp), contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { InfoPanel("12 numbered new words", "These are the 12 new words listed on textbook pages 2–3 of Lesson 1. Tap Listen for pronunciation and Mark known to track recognition.") }
        items(coreWords, key = { it.hanzi }) { word -> WordCard(word, speak, knownWords.contains(word.hanzi), onToggleKnown) }
        item { SectionHeading("Additional words from the dialogues") }
        items(extraWords, key = { it.hanzi }) { word -> WordCard(word, speak, knownWords.contains(word.hanzi), onToggleKnown) }
        item { SourceNote() }
    }
}

@Composable
private fun WordCard(word: Word, speak: (String) -> Unit, isKnown: Boolean, onToggleKnown: (String) -> Unit) {
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(word.hanzi, color = Ink, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                    Text(word.pinyin, color = Teal, fontSize = 15.sp)
                }
                Text(word.meaning, color = Ink, fontSize = 14.sp, modifier = Modifier.weight(1.1f))
            }
            HorizontalDivider(color = Pale)
            Text(word.example, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Medium)
            Text(word.examplePinyin, color = Teal, fontSize = 13.sp)
            Text(word.exampleEnglish, color = Color.Gray, fontSize = 13.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { speak(word.hanzi) }, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)) { Text("▶ Listen") }
                TextButton(onClick = { onToggleKnown(word.hanzi) }, contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)) {
                    Text(if (isKnown) "✓ Known" else "Mark known", color = if (isKnown) Good else Teal)
                }
            }
        }
    }
}

@Composable
private fun GrammarTab(modifier: Modifier, speak: (String) -> Unit) {
    LazyColumn(modifier.fillMaxSize().padding(horizontal = 16.dp), contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { InfoPanel("Lesson 1 grammar", "The first lesson focuses on 要, 最, and approximate quantities with 几 / 多. These are the grammar points on the Lesson 1 notes pages in your textbook.") }
        items(grammarPoints.take(3)) { point -> GrammarCard(point, speak) }
        item {
            InfoPanel("Pronunciation focus", "The textbook also introduces stress in two-syllable words. Many words follow a medium + stressed pattern (for example 旅游 lǚyóu and 可能 kěnéng); a smaller group follows a stressed + light pattern (for example 时候 shíhou and 你们 nǐmen). Listen to the words with your phone's Mandarin voice.")
        }
        item {
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Pronunciation examples", color = Ink, fontWeight = FontWeight.Bold)
                    listOf("旅游" to "lǚyóu", "可能" to "kěnéng", "时候" to "shíhou", "你们" to "nǐmen").forEach { (hanzi, py) ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Text("$hanzi  ·  $py", color = Ink, modifier = Modifier.weight(1f))
                            TextButton(onClick = { speak(hanzi) }) { Text("Listen") }
                        }
                    }
                }
            }
        }
        item { SourceNote() }
    }
}

@Composable
private fun GrammarCard(point: GrammarPoint, speak: (String) -> Unit) {
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(point.title, color = Ink, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(point.explanation, color = Ink.copy(alpha = 0.8f), fontSize = 14.sp)
            Surface(color = SoftTeal, shape = RoundedCornerShape(10.dp)) {
                Text(point.pattern, color = Ink, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(10.dp), fontSize = 14.sp)
            }
            point.examples.forEach { example ->
                Text(example.chinese, color = Ink, fontWeight = FontWeight.Medium, fontSize = 17.sp)
                Text(example.pinyin, color = Teal, fontSize = 13.sp)
                Text(example.english, color = Color.Gray, fontSize = 13.sp)
                TextButton(onClick = { speak(example.chinese) }, contentPadding = PaddingValues(horizontal = 0.dp)) { Text("▶ Listen") }
            }
        }
    }
}

@Composable
private fun PracticeTab(modifier: Modifier, speak: (String) -> Unit, onFinished: (Int) -> Unit) {
    var index by rememberSaveable { mutableIntStateOf(0) }
    var selected by rememberSaveable { mutableIntStateOf(-1) }
    var score by rememberSaveable { mutableIntStateOf(0) }
    var saved by rememberSaveable { mutableStateOf(false) }
    val finished = index >= quizQuestions.size

    LazyColumn(modifier.fillMaxSize().padding(horizontal = 16.dp), contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { InfoPanel("Workbook-based practice", "Questions include comprehension and fill-in items adapted from Lesson 1 of your workbook. Choose an answer to see an explanation.") }
        if (!finished) {
            val question = quizQuestions[index]
            item {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text("Question ${index + 1} of ${quizQuestions.size}", color = Teal, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text("Score: $score", color = Ink, fontWeight = FontWeight.Bold)
                }
                LinearProgressIndicator(progress = { (index + 1).toFloat() / quizQuestions.size.toFloat() }, modifier = Modifier.fillMaxWidth(), color = Teal, trackColor = SoftTeal)
            }
            item {
                Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(question.prompt, color = Ink, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                        question.options.forEachIndexed { optionIndex, option ->
                            val isSelected = selected == optionIndex
                            val optionColor = when {
                                selected == -1 -> Color.White
                                optionIndex == question.answer -> SoftTeal
                                isSelected -> Color(0xFFF9E8E8)
                                else -> Color.White
                            }
                            Card(
                                modifier = Modifier.fillMaxWidth().clickable(enabled = selected == -1) {
                                    selected = optionIndex
                                    if (optionIndex == question.answer) score++
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = optionColor),
                            ) {
                                Text(option, color = Ink, modifier = Modifier.padding(14.dp), fontSize = 15.sp)
                            }
                        }
                        if (selected != -1) {
                            Text(if (selected == question.answer) "Correct" else "Not quite", color = if (selected == question.answer) Good else Bad, fontWeight = FontWeight.Bold)
                            Text(question.explanation, color = Ink, fontSize = 14.sp)
                            Text(question.source, color = Color.Gray, fontSize = 11.sp)
                            Button(onClick = { index++; selected = -1 }, modifier = Modifier.fillMaxWidth()) { Text(if (index == quizQuestions.lastIndex) "See result" else "Next question") }
                        }
                    }
                }
            }
        } else {
            item {
                Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.fillMaxWidth().padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("练习完成", color = Teal, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                        Text("$score / ${quizQuestions.size}", color = Ink, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                        Text("${(score * 100 / quizQuestions.size)}% correct", color = Ink)
                        Text(if (score >= 8) "Good result. Review any missed items, then repeat the dialogues aloud." else "Review the vocabulary and grammar, then try the practice again.", color = Color.Gray)
                        Button(onClick = { if (!saved) { onFinished(score); saved = true } }, enabled = !saved, modifier = Modifier.fillMaxWidth()) { Text(if (saved) "Progress saved" else "Save lesson progress") }
                        OutlinedButton(onClick = { index = 0; selected = -1; score = 0; saved = false }, modifier = Modifier.fillMaxWidth()) { Text("Try again") }
                    }
                }
            }
        }
        item { SourceNote() }
    }
}

@Composable
private fun ProgressScreen(modifier: Modifier, completed: Boolean, score: Int, knownCount: Int, openLesson: () -> Unit) {
    LazyColumn(modifier.fillMaxSize().padding(20.dp), contentPadding = PaddingValues(top = 20.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { Text("Your progress", color = Ink, fontSize = 28.sp, fontWeight = FontWeight.Bold) }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(if (completed) "Lesson 1 completed" else "Lesson 1 in progress", color = Teal, fontWeight = FontWeight.Bold)
                    Text("${knownCount.coerceAtMost(12)} of 12 core textbook words marked known", color = Ink)
                    LinearProgressIndicator(progress = { (knownCount.coerceAtMost(12) / 12f) }, modifier = Modifier.fillMaxWidth(), color = Teal, trackColor = SoftTeal)
                    Text("Best quiz score: $score / ${quizQuestions.size}", color = Ink)
                    Button(onClick = openLesson) { Text("Return to Lesson 1") }
                }
            }
        }
        item { Text("Your progress is stored locally on this phone. No account or paid API is required.", color = Color.Gray, fontSize = 13.sp) }
    }
}

@Composable
private fun InfoPanel(title: String, body: String) {
    Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = SoftBlue), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, color = Ink, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(body, color = Ink.copy(alpha = 0.8f), fontSize = 13.sp)
        }
    }
}

@Composable
private fun SourceNote() {
    Column(Modifier.fillMaxWidth().padding(top = 8.dp)) {
        HorizontalDivider(color = Color(0xFFD7E2E0))
        Spacer(Modifier.height(8.dp))
        Text("SOURCE CHECK", color = Teal, fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 1.sp)
        Text("Textbook: Lesson 1, printed pages 1–8 (PDF pages 17–24). Workbook: Lesson 1, printed pages 1–10 (PDF pages 9–18). Text and practice were transcribed/adapted from those pages. Workbook image-matching and character-copying tasks are not reproduced as handwriting drills.", color = Color.Gray, fontSize = 11.sp)
    }
}
