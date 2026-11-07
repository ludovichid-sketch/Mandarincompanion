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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            MaterialTheme {
                MandarinApp(
                    speak = { phrase ->
                        if (ttsAvailable) {
                            tts?.speak(phrase, TextToSpeech.QUEUE_FLUSH, null, "mandarin-${phrase.hashCode()}")
                        } else {
                            Toast.makeText(this, "Install a Chinese (Mandarin) voice in Android Text-to-Speech settings.", Toast.LENGTH_LONG).show()
                        }
                    }
                )
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
    val exampleEnglish: String
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
    val exerciseType: String
)
private data class LessonContent(
    val number: Int,
    val title: String,
    val hanziTitle: String,
    val textbookPages: String,
    val workbookPages: String,
    val dialogues: List<Dialogue>,
    val words: List<Word>,
    val grammar: List<GrammarPoint>,
    val questions: List<QuizQuestion>,
    val pronunciation: String
)

private val lessons = listOf(
    LessonContent(
        number = 1,
        title = "September is the best time to visit Beijing",
        hanziTitle = "九月去北京旅游最好",
        textbookPages = "PDF pages 17–24",
        workbookPages = "PDF pages 9–18",
        dialogues = listOf(
            Dialogue("1 · Choosing a time to visit", "In the school · Textbook dialogue 01-1", listOf(
                DialogueLine("我 要 去 北京 旅游，你 觉得 什么 时候 去 最 好？", "Wǒ yào qù Běijīng lǚyóu, nǐ juéde shénme shíhou qù zuì hǎo?", "I want to travel to Beijing. When do you think is the best time?"),
                DialogueLine("九 月 去 北京 旅游 最 好。", "Jiǔ yuè qù Běijīng lǚyóu zuì hǎo.", "September is the best time to visit Beijing."),
                DialogueLine("为 什么？", "Wèi shénme?", "Why?"),
                DialogueLine("九 月 的 北京 天气 不 冷 也 不 热。", "Jiǔ yuè de Běijīng tiānqì bù lěng yě bù rè.", "The weather in Beijing in September is neither cold nor hot.")
            )),
            Dialogue("2 · Favourite sport", "Looking at a picture · Textbook dialogue 01-2", listOf(
                DialogueLine("你 喜欢 什么 运动？", "Nǐ xǐhuan shénme yùndòng?", "What sport do you like?"),
                DialogueLine("我 最 喜欢 踢 足球。", "Wǒ zuì xǐhuan tī zúqiú.", "I like playing football the most."),
                DialogueLine("下 午 我们 一起 去 踢 足球 吧。", "Xiàwǔ wǒmen yìqǐ qù tī zúqiú ba.", "Let's go and play football together this afternoon."),
                DialogueLine("好 啊！", "Hǎo a!", "Great!")
            )),
            Dialogue("3 · Buying chairs", "At home · Textbook dialogue 01-3", listOf(
                DialogueLine("我们 要 不 要 买 几 个 新 的 椅子？", "Wǒmen yào bu yào mǎi jǐ ge xīn de yǐzi?", "Should we buy a few new chairs?"),
                DialogueLine("好 啊。什么 时候 去 买？", "Hǎo a. Shénme shíhou qù mǎi?", "All right. When shall we buy them?"),
                DialogueLine("明天 下 午 怎么 样？", "Míngtiān xiàwǔ zěnmeyàng?", "How about tomorrow afternoon?"),
                DialogueLine("你 明天 几 点 能 回来？", "Nǐ míngtiān jǐ diǎn néng huílai?", "What time can you come back tomorrow?"),
                DialogueLine("三 点 多。", "Sān diǎn duō.", "A little after three.")
            )),
            Dialogue("4 · The cat Huahua", "At home · Textbook dialogue 01-4", listOf(
                DialogueLine("桌子 下 面 有 个 猫。", "Zhuōzi xiàmiàn yǒu ge māo.", "There is a cat under the table."),
                DialogueLine("那 是 我 的 猫，它 叫 花花。", "Nà shì wǒ de māo, tā jiào Huāhua.", "That is my cat. Its name is Huahua."),
                DialogueLine("它 很 漂亮。", "Tā hěn piàoliang.", "It is beautiful."),
                DialogueLine("是 啊，我 觉得 它 的 眼睛 最 漂亮。", "Shì a, wǒ juéde tā de yǎnjing zuì piàoliang.", "Yes. I think its eyes are the most beautiful."),
                DialogueLine("它 多 大 了？", "Tā duō dà le?", "How old is it?"),
                DialogueLine("六 个 多 月。", "Liù ge duō yuè.", "More than six months.")
            ))
        ),
        words = listOf(
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
        ),
        grammar = listOf(
            GrammarPoint("要 · wish or intention", "要 is placed before a verb to say that someone wants or intends to do something. The textbook contrasts it with 不想.", "Subject + 要 + verb  /  Subject + 不想 + verb", listOf(
                DialogueLine("我 要 去 北京。", "Wǒ yào qù Běijīng.", "I want to go to Beijing."),
                DialogueLine("我 不 想 买 椅子。", "Wǒ bù xiǎng mǎi yǐzi.", "I don't want to buy chairs.")
            )),
            GrammarPoint("最 · highest degree", "最 marks the top degree within a group or in a particular respect. It commonly appears before a verb or adjective phrase.", "Subject + 最 + verb / adjective", listOf(
                DialogueLine("我 最 喜欢 踢 足球。", "Wǒ zuì xǐhuan tī zúqiú.", "I like playing football the most."),
                DialogueLine("它 的 眼睛 最 漂亮。", "Tā de yǎnjing zuì piàoliang.", "Its eyes are the most beautiful.")
            )),
            GrammarPoint("几 and 多 · approximate quantities", "几 before a measure word indicates an indefinite small number. 多 expresses an approximate quantity: after the measure word with a number below ten, or before the measure word with numbers above ten.", "几 + measure word + noun  /  六个多月  /  十多个月", listOf(
                DialogueLine("我们 要 不 要 买 几 个 新 的 椅子？", "Wǒmen yào bu yào mǎi jǐ ge xīn de yǐzi?", "Should we buy a few new chairs?"),
                DialogueLine("六 个 多 月。", "Liù ge duō yuè.", "More than six months.")
            ))
        ),
        questions = listOf(
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
        ),
        pronunciation = "The textbook introduces stress patterns in two-syllable words. Use Mandarin TTS to listen to examples such as 旅游 lǚyóu, 可能 kěnéng, 时候 shíhou and 你们 nǐmen."
    ),
    LessonContent(
        number = 2,
        title = "I get up at six every day",
        hanziTitle = "我每天六点起床",
        textbookPages = "PDF pages 25–32",
        workbookPages = "PDF pages 19–28",
        dialogues = listOf(
            Dialogue("1 · Morning exercise", "On the playground · Textbook dialogue 02-1", listOf(
                DialogueLine("你很少生病，是不是喜欢运动？", "Nǐ hěn shǎo shēngbìng, shì bu shì xǐhuan yùndòng?", "You rarely get sick. Is it because you like sports?"),
                DialogueLine("是啊，我每天早上都要出去跑步。", "Shì a, wǒ měitiān zǎoshang dōu yào chūqù pǎobù.", "Yes. I go out jogging every morning."),
                DialogueLine("你每天几点起床？", "Nǐ měitiān jǐ diǎn qǐchuáng?", "What time do you get up every day?"),
                DialogueLine("我每天六点起床。", "Wǒ měitiān liù diǎn qǐchuáng.", "I get up at six every day.")
            )),
            Dialogue("2 · At the hospital", "In the hospital · Textbook dialogue 02-2", listOf(
                DialogueLine("吃药了吗？现在身体怎么样？", "Chī yào le ma? Xiànzài shēntǐ zěnmeyàng?", "Have you taken your medicine? How do you feel now?"),
                DialogueLine("吃了。现在好多了。", "Chī le. Xiànzài hǎo duō le.", "Yes. I feel much better now."),
                DialogueLine("什么时候能出院？", "Shénme shíhou néng chūyuàn?", "When can you leave the hospital?"),
                DialogueLine("医生说下个星期。", "Yīshēng shuō xià ge xīngqī.", "The doctor says next week.")
            )),
            Dialogue("3 · Asking about David", "On the playground · Textbook dialogue 02-3", listOf(
                DialogueLine("大卫今年多大？", "Dàwèi jīnnián duō dà?", "How old is David this year?"),
                DialogueLine("二十多岁。", "Èrshí duō suì.", "A little over twenty."),
                DialogueLine("他多高？", "Tā duō gāo?", "How tall is he?"),
                DialogueLine("一米八几。", "Yì mǐ bā jǐ.", "A little over 1.8 metres."),
                DialogueLine("你怎么知道这么多啊？", "Nǐ zěnme zhīdào zhème duō a?", "How do you know so much?"),
                DialogueLine("他是我同学。", "Tā shì wǒ tóngxué.", "He is my classmate.")
            )),
            Dialogue("4 · A busy teacher", "In the room · Textbook dialogue 02-4", listOf(
                DialogueLine("张老师星期六也不休息啊？", "Zhāng lǎoshī xīngqīliù yě bù xiūxi a?", "Does Teacher Zhang not rest even on Saturdays?"),
                DialogueLine("是啊，他这几天很忙，没有时间休息。", "Shì a, tā zhè jǐ tiān hěn máng, méiyǒu shíjiān xiūxi.", "Yes. He has been very busy these days and has no time to rest."),
                DialogueLine("那会很累吧？", "Nà huì hěn lèi ba?", "That must be tiring, right?"),
                DialogueLine("他每天回来都很累。", "Tā měitiān huílai dōu hěn lèi.", "He comes home tired every day.")
            ))
        ),
        words = listOf(
            Word("生病", "shēngbìng", "to fall ill; to be sick", "他很少生病。", "Tā hěn shǎo shēngbìng.", "He rarely gets sick."),
            Word("每", "měi", "every; each", "我每天六点起床。", "Wǒ měitiān liù diǎn qǐchuáng.", "I get up at six every day."),
            Word("早上", "zǎoshang", "morning", "我每天早上跑步。", "Wǒ měitiān zǎoshang pǎobù.", "I jog every morning."),
            Word("跑步", "pǎobù", "to run; to jog", "我出去跑步。", "Wǒ chūqù pǎobù.", "I go out jogging."),
            Word("起床", "qǐchuáng", "to get up; to get out of bed", "我六点起床。", "Wǒ liù diǎn qǐchuáng.", "I get up at six."),
            Word("药", "yào", "medicine; drug", "你吃药了吗？", "Nǐ chī yào le ma?", "Have you taken your medicine?"),
            Word("身体", "shēntǐ", "body; health", "现在身体怎么样？", "Xiànzài shēntǐ zěnmeyàng?", "How is your health now?"),
            Word("出院", "chūyuàn", "to leave hospital; to be discharged", "下个星期能出院。", "Xià ge xīngqī néng chūyuàn.", "I can leave hospital next week."),
            Word("高", "gāo", "tall; high", "他多高？", "Tā duō gāo?", "How tall is he?"),
            Word("米", "mǐ", "metre", "他一米八几。", "Tā yì mǐ bā jǐ.", "He is a little over 1.8 metres."),
            Word("知道", "zhīdào", "to know", "你怎么知道？", "Nǐ zěnme zhīdào?", "How do you know?"),
            Word("休息", "xiūxi", "to rest; to take a break", "星期六也不休息。", "Xīngqīliù yě bù xiūxi.", "He does not rest even on Saturday."),
            Word("忙", "máng", "busy", "他这几天很忙。", "Tā zhè jǐ tiān hěn máng.", "He has been very busy these days."),
            Word("时间", "shíjiān", "time", "没有时间休息。", "Méiyǒu shíjiān xiūxi.", "There is no time to rest.")
        ),
        grammar = listOf(
            GrammarPoint("是不是 · confirming a guess", "Use 是不是 when you have a fairly strong idea about a situation and want the other person to confirm it. It usually appears before the predicate, but it can also appear at the beginning or end of a question.", "Statement + 是不是 + predicate？", listOf(
                DialogueLine("你很少生病，是不是喜欢运动？", "Nǐ hěn shǎo shēngbìng, shì bu shì xǐhuan yùndòng?", "You rarely get sick; is it because you like exercise?"),
                DialogueLine("我们星期一去北京，是不是？", "Wǒmen xīngqīyī qù Běijīng, shì bu shì?", "We are going to Beijing on Monday, right?")
            )),
            GrammarPoint("每 · each / every", "每 is used before a measure word or time expression to refer to every member of a group: 每天 (every day), 每年 (every year), 每个星期 (every week).", "每 + measure word / time word", listOf(
                DialogueLine("我每天六点起床。", "Wǒ měitiān liù diǎn qǐchuáng.", "I get up at six every day."),
                DialogueLine("你每个星期六都工作吗？", "Nǐ měi ge xīngqīliù dōu gōngzuò ma?", "Do you work every Saturday?")
            )),
            GrammarPoint("多 + adjective · asking about degree", "The question word 多 comes before an adjective to ask about degree or measurement. The answer gives a number or quantity.", "Subject + 多 + adjective？", listOf(
                DialogueLine("你多大？", "Nǐ duō dà?", "How old are you?"),
                DialogueLine("他多高？", "Tā duō gāo?", "How tall is he?"),
                DialogueLine("一米八几。", "Yì mǐ bā jǐ.", "A little over 1.8 metres.")
            ))
        ),
        questions = listOf(
            QuizQuestion("What time does the speaker get up?", listOf("Five o'clock", "Six o'clock", "Seven o'clock", "Eight o'clock"), 1, "我每天六点起床。", "Workbook listening-style choice"),
            QuizQuestion("What does the speaker do every morning?", listOf("Swims", "Plays football", "Goes jogging", "Drives to work"), 2, "我每天早上都要出去跑步。", "Dialogue comprehension"),
            QuizQuestion("When can the patient leave hospital?", listOf("Tomorrow", "Next week", "Next month", "Today"), 1, "医生说下个星期。", "Dialogue comprehension"),
            QuizQuestion("How tall is David?", listOf("1.60 m", "1.70 m", "A little over 1.80 m", "2 metres"), 2, "一米八几 means a little over 1.8 metres.", "Listening-style choice"),
            QuizQuestion("Why does Teacher Zhang not rest on Saturday?", listOf("He is busy", "He is travelling", "He is ill", "He is studying Chinese"), 0, "他这几天很忙，没有时间休息。", "Dialogue comprehension"),
            QuizQuestion("In this lesson, 每 means…", listOf("Some", "Every / each", "Already", "Very"), 1, "每 is used in expressions such as 每天 and 每个星期.", "Vocabulary"),
            QuizQuestion("Choose the natural question asking someone's height.", listOf("他几高？", "他多高？", "他很高？", "他高多少吗？"), 1, "多 + adjective asks about degree: 他多高？", "Workbook grammar practice"),
            QuizQuestion("Choose the correct word: 医生说下个星期可以____。", listOf("起床", "跑步", "出院", "休息"), 2, "出院 means to be discharged from hospital.", "Workbook fill-in"),
            QuizQuestion("True or false: The speaker says Teacher Zhang comes home tired every day.", listOf("True", "False"), 0, "他每天回来都很累。", "Workbook true / false"),
            QuizQuestion("What does 是不是 do in this lesson?", listOf("Confirms a likely guess", "Marks the past tense", "Indicates possession", "Means because"), 0, "是不是 is used to ask for confirmation of a fairly confident guess.", "Grammar" )
        ),
        pronunciation = "Workbook pronunciation practice asks you to listen for stress in words and phrases. Practise hearing the three-syllable rhythm in examples such as 收音机 shōuyīnjī and 新加坡 Xīnjiāpō. Publisher audio was not included in the PDFs, so the Listen buttons use Android Mandarin TTS."
    ),
    LessonContent(
        number = 3,
        title = "The red one on the left is mine",
        hanziTitle = "左边那个红色的是我的",
        textbookPages = "PDF pages 33–40",
        workbookPages = "PDF pages 29–38",
        dialogues = listOf(
            Dialogue("1 · A watch", "In the room · Textbook dialogue 03-1", listOf(
                DialogueLine("这块手表是你的吗？", "Zhè kuài shǒubiǎo shì nǐ de ma?", "Is this watch yours?"),
                DialogueLine("不是我的，是我爸爸的。", "Bú shì wǒ de. Shì wǒ bàba de.", "No, it isn't mine. It is my father's."),
                DialogueLine("多少钱买的？", "Duōshao qián mǎi de?", "How much did it cost?"),
                DialogueLine("三千多块。", "Sānqiān duō kuài.", "More than 3,000 yuan.")
            )),
            Dialogue("2 · The newspaper", "At home · Textbook dialogue 03-2", listOf(
                DialogueLine("这是今天早上的报纸吗？", "Zhè shì jīntiān zǎoshang de bàozhǐ ma?", "Is this this morning's newspaper?"),
                DialogueLine("不是，是昨天的。", "Bú shì, shì zuótiān de.", "No, it is yesterday's."),
                DialogueLine("你听，是不是送报纸的来了？", "Nǐ tīng, shì bu shì sòng bàozhǐ de lái le?", "Listen. Is the newspaper delivery person here?"),
                DialogueLine("我看一下。不是，是送牛奶的。", "Wǒ kàn yíxià. Bú shì, shì sòng niúnǎi de.", "I'll take a look. No, it is the milk delivery person.")
            )),
            Dialogue("3 · Whose room?", "At home · Textbook dialogue 03-3", listOf(
                DialogueLine("这是谁的房间？", "Zhè shì shéi de fángjiān?", "Whose room is this?"),
                DialogueLine("这是我和我丈夫的，旁边那个小的房间是我女儿的。", "Zhè shì wǒ hé wǒ zhàngfu de, pángbiān nàge xiǎo de fángjiān shì wǒ nǚ'ér de.", "It is my husband's and mine. The small room beside it is my daughter's."),
                DialogueLine("你女儿的房间真漂亮！都是粉色的。", "Nǐ nǚ'ér de fángjiān zhēn piàoliang! Dōu shì fěnsè de.", "Your daughter's room is really beautiful! It is all pink."),
                DialogueLine("是啊，粉色是我女儿最喜欢的颜色。", "Shì a, fěnsè shì wǒ nǚ'ér zuì xǐhuan de yánsè.", "Yes. Pink is my daughter's favourite colour.")
            )),
            Dialogue("4 · The cups", "In the office · Textbook dialogue 03-4", listOf(
                DialogueLine("你看见我的杯子了吗？", "Nǐ kànjiàn wǒ de bēizi le ma?", "Have you seen my cup?"),
                DialogueLine("这里有几个杯子，哪个是你的？", "Zhèlǐ yǒu jǐ ge bēizi, nǎge shì nǐ de?", "There are several cups here. Which one is yours?"),
                DialogueLine("左边那个红色的是我的。", "Zuǒbian nàge hóngsè de shì wǒ de.", "The red one on the left is mine."),
                DialogueLine("给你。", "Gěi nǐ.", "Here you are.")
            ))
        ),
        words = listOf(
            Word("手表", "shǒubiǎo", "watch", "这块手表是你的吗？", "Zhè kuài shǒubiǎo shì nǐ de ma?", "Is this watch yours?"),
            Word("千", "qiān", "thousand", "三千多块。", "Sānqiān duō kuài.", "More than three thousand yuan."),
            Word("报纸", "bàozhǐ", "newspaper", "这是今天早上的报纸吗？", "Zhè shì jīntiān zǎoshang de bàozhǐ ma?", "Is this this morning's newspaper?"),
            Word("送", "sòng", "to send; to deliver", "送牛奶的来了。", "Sòng niúnǎi de lái le.", "The milk delivery person has arrived."),
            Word("一下", "yíxià", "briefly; once (after a verb)", "我看一下。", "Wǒ kàn yíxià.", "I'll take a look."),
            Word("牛奶", "niúnǎi", "milk", "这是送牛奶的。", "Zhè shì sòng niúnǎi de.", "This is the milk delivery person."),
            Word("房间", "fángjiān", "room", "这是谁的房间？", "Zhè shì shéi de fángjiān?", "Whose room is this?"),
            Word("丈夫", "zhàngfu", "husband", "这是我和我丈夫的。", "Zhè shì wǒ hé wǒ zhàngfu de.", "This belongs to my husband and me."),
            Word("旁边", "pángbiān", "beside; next to", "旁边那个小的房间。", "Pángbiān nàge xiǎo de fángjiān.", "The small room beside it."),
            Word("真", "zhēn", "really; indeed", "你女儿的房间真漂亮！", "Nǐ nǚ'ér de fángjiān zhēn piàoliang!", "Your daughter's room is really beautiful!"),
            Word("粉色", "fěnsè", "pink", "都是粉色的。", "Dōu shì fěnsè de.", "It is all pink."),
            Word("颜色", "yánsè", "colour", "粉色是我女儿最喜欢的颜色。", "Fěnsè shì wǒ nǚ'ér zuì xǐhuan de yánsè.", "Pink is my daughter's favourite colour."),
            Word("左边", "zuǒbian", "left side; on the left", "左边那个红色的是我的。", "Zuǒbian nàge hóngsè de shì wǒ de.", "The red one on the left is mine."),
            Word("红色", "hóngsè", "red", "红色的是我的。", "Hóngsè de shì wǒ de.", "The red one is mine.")
        ),
        grammar = listOf(
            GrammarPoint("的 phrase · leaving out the noun", "A pronoun, adjective or verb phrase followed by 的 can stand in for a noun phrase when the noun is already understood. 我的 can mean 我的杯子; 昨天的 can mean 昨天的报纸.", "Modifier + 的  →  understood noun phrase", listOf(
                DialogueLine("这本书不是我的。", "Zhè běn shū bú shì wǒ de.", "This book is not mine."),
                DialogueLine("这块手表是你的吗？", "Zhè kuài shǒubiǎo shì nǐ de ma?", "Is this watch yours?")
            )),
            GrammarPoint("一下 · a short or brief action", "一下 follows a verb to express one brief action or an attempt. The object can be omitted when the context is clear.", "Verb + 一下 (+ object)", listOf(
                DialogueLine("我看一下。", "Wǒ kàn yíxià.", "I'll have a look."),
                DialogueLine("你休息一下吧。", "Nǐ xiūxi yíxià ba.", "Have a short rest.")
            )),
            GrammarPoint("真 + adjective · exclamation", "真 before an adjective adds an exclamatory sense: really, indeed. It expresses the speaker's strong impression.", "真 + adjective", listOf(
                DialogueLine("你女儿的房间真漂亮！", "Nǐ nǚ'ér de fángjiān zhēn piàoliang!", "Your daughter's room is really beautiful!"),
                DialogueLine("今天天气真好！", "Jīntiān tiānqì zhēn hǎo!", "The weather is really nice today!")
            ))
        ),
        questions = listOf(
            QuizQuestion("How much did the watch cost?", listOf("300 yuan", "More than 3,000 yuan", "Exactly 3,000 yuan", "More than 300 yuan"), 1, "三千多块 means more than 3,000 yuan.", "Comprehension"),
            QuizQuestion("Is the newspaper from this morning?", listOf("Yes", "No, it is yesterday's", "It is tomorrow's", "The dialogue does not say"), 1, "不是，是昨天的。", "Workbook true / false"),
            QuizQuestion("Who delivered the item at the door?", listOf("The newspaper person", "The milk delivery person", "The postman", "A friend"), 1, "不是，是送牛奶的。", "Comprehension"),
            QuizQuestion("Whose room is the small room beside the couple's room?", listOf("Their son's", "Their daughter's", "Their guest's", "The husband's"), 1, "旁边那个小的房间是我女儿的。", "Comprehension"),
            QuizQuestion("What is the daughter's favourite colour?", listOf("Red", "White", "Pink", "Blue"), 2, "粉色是我女儿最喜欢的颜色。", "Vocabulary"),
            QuizQuestion("Which cup is the speaker's?", listOf("The red one on the left", "The white one on the right", "The black one", "The biggest one"), 0, "左边那个红色的是我的。", "Workbook picture-description style"),
            QuizQuestion("What does 一下 after a verb indicate?", listOf("A brief action or attempt", "A future plan", "A completed event", "A person's possession"), 0, "看一下 means to take a quick look.", "Grammar"),
            QuizQuestion("Choose the natural exclamation.", listOf("你女儿的房间很真漂亮。", "你女儿的房间真漂亮！", "真你女儿房间漂亮。", "你女儿真房间漂亮。"), 1, "真 comes before the adjective: 真漂亮.", "Workbook grammar practice"),
            QuizQuestion("What does 我的 mean in a context where 杯子 is already known?", listOf("My cup", "Your cup", "The cup's colour", "A new cup"), 0, "的 allows the understood noun 杯子 to be omitted.", "Grammar"),
            QuizQuestion("Choose the correct word for 'beside'.", listOf("左边", "旁边", "红色", "颜色"), 1, "旁边 means beside or next to.", "Vocabulary")
        ),
        pronunciation = "The textbook includes pronunciation and character-recognition sections. Listen to the dialogue words with Mandarin TTS and practise the contrasts 左边 zuǒbian / 右边 yòubian, 红色 hóngsè / 粉色 fěnsè. Character handwriting drills are intentionally omitted."
    ),
    LessonContent(
        number = 4,
        title = "He recommended me for this job",
        hanziTitle = "这个工作是他帮我介绍的",
        textbookPages = "PDF pages 41–48",
        workbookPages = "PDF pages 39–48",
        dialogues = listOf(
            Dialogue("1 · A birthday present", "In the classroom · Textbook dialogue 04-1", listOf(
                DialogueLine("生日快乐！这是送给你的！", "Shēngrì kuàilè! Zhè shì sòng gěi nǐ de!", "Happy birthday! This is for you."),
                DialogueLine("是什么？是一本书吗？", "Shì shénme? Shì yì běn shū ma?", "What is it? Is it a book?"),
                DialogueLine("对，这本书是我写的。", "Duì, zhè běn shū shì wǒ xiě de.", "Yes. This book was written by me."),
                DialogueLine("太谢谢你了！", "Tài xièxie nǐ le!", "Thank you so much!")
            )),
            Dialogue("2 · A telephone call", "At home · Textbook dialogue 04-2", listOf(
                DialogueLine("早上有你一个电话。", "Zǎoshang yǒu nǐ yí ge diànhuà.", "Someone called for you this morning."),
                DialogueLine("电话是谁打的？", "Diànhuà shì shéi dǎ de?", "Who made the phone call?"),
                DialogueLine("不知道，是儿子接的。", "Bù zhīdào, shì érzi jiē de.", "I don't know. Our son answered it."),
                DialogueLine("好，晚上我问一下儿子。", "Hǎo, wǎnshang wǒ wèn yíxià érzi.", "All right. I'll ask our son this evening.")
            )),
            Dialogue("3 · Playing football", "On the playground · Textbook dialogue 04-3", listOf(
                DialogueLine("你喜欢踢足球吗？", "Nǐ xǐhuan tī zúqiú ma?", "Do you like playing football?"),
                DialogueLine("非常喜欢。", "Fēicháng xǐhuan.", "I like it very much."),
                DialogueLine("你是什么时候开始踢足球的？", "Nǐ shì shénme shíhou kāishǐ tī zúqiú de?", "When did you start playing football?"),
                DialogueLine("我十一岁的时候开始踢足球，已经踢了十年了。", "Wǒ shíyī suì de shíhou kāishǐ tī zúqiú, yǐjīng tī le shí nián le.", "I started playing football when I was eleven. I have been playing for ten years.")
            )),
            Dialogue("4 · A job recommendation", "In the company · Textbook dialogue 04-4", listOf(
                DialogueLine("你在这儿工作多长时间了？", "Nǐ zài zhèr gōngzuò duō cháng shíjiān le?", "How long have you worked here?"),
                DialogueLine("已经两年多了，我是二〇一一年来的。", "Yǐjīng liǎng nián duō le, wǒ shì èr líng yīyī nián lái de.", "More than two years. I came in 2011."),
                DialogueLine("你认识谢先生吗？", "Nǐ rènshi Xiè xiānsheng ma?", "Do you know Mr Xie?"),
                DialogueLine("认识，我们是大学同学，这个工作是他帮我介绍的。", "Rènshi, wǒmen shì dàxué tóngxué, zhège gōngzuò shì tā bāng wǒ jièshào de.", "Yes. We were university classmates. He recommended me for this job.")
            ))
        ),
        words = listOf(
            Word("生日", "shēngrì", "birthday", "生日快乐！", "Shēngrì kuàilè!", "Happy birthday!"),
            Word("快乐", "kuàilè", "happy; glad", "生日快乐！", "Shēngrì kuàilè!", "Happy birthday!"),
            Word("给", "gěi", "to; for (after a verb)", "这是送给你的。", "Zhè shì sòng gěi nǐ de.", "This is for you."),
            Word("接", "jiē", "to receive; to answer (a call)", "是儿子接的。", "Shì érzi jiē de.", "Our son answered it."),
            Word("晚上", "wǎnshang", "evening; night", "晚上我问一下儿子。", "Wǎnshang wǒ wèn yíxià érzi.", "I'll ask our son this evening."),
            Word("问", "wèn", "to ask", "我问一下儿子。", "Wǒ wèn yíxià érzi.", "I'll ask our son."),
            Word("非常", "fēicháng", "very; extremely", "非常喜欢。", "Fēicháng xǐhuan.", "I like it very much."),
            Word("开始", "kāishǐ", "to begin; to start", "十一岁的时候开始踢足球。", "Shíyī suì de shíhou kāishǐ tī zúqiú.", "I started playing football at eleven."),
            Word("已经", "yǐjīng", "already", "已经踢了十年了。", "Yǐjīng tī le shí nián le.", "I have already played for ten years."),
            Word("长", "cháng", "long", "多长时间？", "Duō cháng shíjiān?", "How long?"),
            Word("两", "liǎng", "two (before a measure word)", "已经两年多了。", "Yǐjīng liǎng nián duō le.", "More than two years already."),
            Word("帮", "bāng", "to help; to assist", "他帮我介绍工作。", "Tā bāng wǒ jièshào gōngzuò.", "He helped recommend me for the job."),
            Word("介绍", "jièshào", "to introduce; to recommend", "这个工作是他帮我介绍的。", "Zhège gōngzuò shì tā bāng wǒ jièshào de.", "He recommended me for this job.")
        ),
        grammar = listOf(
            GrammarPoint("是……的 · emphasising the person who did an action", "When an action is already known to have happened, 是……的 can emphasise who performed it. In the negative, 不 is placed before 是.", "Object + 是 + person + verb + 的", listOf(
                DialogueLine("这本书是我写的。", "Zhè běn shū shì wǒ xiě de.", "This book was written by me."),
                DialogueLine("这个工作是他帮我介绍的。", "Zhège gōngzuò shì tā bāng wǒ jièshào de.", "He recommended me for this job."),
                DialogueLine("电话不是我接的。", "Diànhuà bú shì wǒ jiē de.", "I was not the one who answered the call.")
            )),
            GrammarPoint("……的时候 · indicating a time", "A time expression or a verb phrase followed by 的时候 indicates when something happens.", "Time / verb phrase + 的时候", listOf(
                DialogueLine("我十一岁的时候开始踢足球。", "Wǒ shíyī suì de shíhou kāishǐ tī zúqiú.", "I started playing football when I was eleven."),
                DialogueLine("我睡觉的时候，我妈妈在做饭。", "Wǒ shuìjiào de shíhou, wǒ māma zài zuò fàn.", "When I sleep, my mother is cooking.")
            )),
            GrammarPoint("已经 · already", "已经 indicates that an action has been completed or that a state has reached a certain degree. It commonly appears before the verb or adjective phrase.", "已经 + verb / adjective (+ 了)", listOf(
                DialogueLine("已经两年多了。", "Yǐjīng liǎng nián duō le.", "It has already been more than two years."),
                DialogueLine("已经踢了十年了。", "Yǐjīng tī le shí nián le.", "I have already played for ten years.")
            ))
        ),
        questions = listOf(
            QuizQuestion("What was the birthday present?", listOf("A watch", "A book", "A phone", "A football"), 1, "是一本书；这本书是我写的。", "Comprehension"),
            QuizQuestion("Who answered the telephone?", listOf("The father", "The mother", "The son", "A colleague"), 2, "是儿子接的。", "Comprehension"),
            QuizQuestion("How old was the speaker when he started football?", listOf("Nine", "Ten", "Eleven", "Twelve"), 2, "我十一岁的时候开始踢足球。", "Comprehension"),
            QuizQuestion("How long has he played football?", listOf("Five years", "Eight years", "Ten years", "Eleven years"), 2, "已经踢了十年了。", "Comprehension"),
            QuizQuestion("How long has the speaker worked there?", listOf("More than one year", "More than two years", "Five years", "Ten years"), 1, "已经两年多了。", "Comprehension"),
            QuizQuestion("Who recommended him for the job?", listOf("His teacher", "His father", "His university classmate Mr Xie", "His son"), 2, "这个工作是他帮我介绍的。", "Comprehension"),
            QuizQuestion("Choose the structure that emphasises who did an action.", listOf("是……的", "是不是", "有点儿", "每……"), 0, "是……的 can emphasise the agent of an action.", "Grammar"),
            QuizQuestion("What does 已经 express?", listOf("A future plan", "An action already completed or a state reached", "A question about height", "A location"), 1, "已经 indicates completion or reaching a degree.", "Vocabulary"),
            QuizQuestion("Complete: 我十一岁____开始踢足球。", listOf("的时候", "以后", "旁边", "一下"), 0, "……的时候 indicates the time when an action happens.", "Workbook fill-in"),
            QuizQuestion("In 这本书是我写的, who wrote the book?", listOf("The speaker", "The speaker's father", "The son", "Mr Xie"), 0, "我 is placed in the 是……的 structure to emphasise the agent.", "Workbook reading comprehension")
        ),
        pronunciation = "Workbook tasks include word listening and pronunciation practice. Listen to words such as 生日 shēngrì, 快乐 kuàilè, 开始 kāishǐ and 介绍 jièshào with the phone's Mandarin TTS."
    ),
    LessonContent(
        number = 5,
        title = "Take this one",
        hanziTitle = "就买这件吧",
        textbookPages = "PDF pages 49–56",
        workbookPages = "PDF pages 49–58",
        dialogues = listOf(
            Dialogue("1 · Dinner at home", "At home · Textbook dialogue 05-1", listOf(
                DialogueLine("晚上我们去饭馆吃饭，怎么样？", "Wǎnshang wǒmen qù fànguǎn chī fàn, zěnmeyàng?", "Shall we go to a restaurant for dinner tonight?"),
                DialogueLine("我不想去外面吃，我想在家吃。", "Wǒ bù xiǎng qù wàimiàn chī, wǒ xiǎng zài jiā chī.", "I don't want to eat out. I want to eat at home."),
                DialogueLine("那你准备做什么呢？", "Nà nǐ zhǔnbèi zuò shénme ne?", "Then what are you planning to cook?"),
                DialogueLine("就做你爱吃的鱼吧。", "Jiù zuò nǐ ài chī de yú ba.", "Then I'll make the fish you like.")
            )),
            Dialogue("2 · Choosing clothes", "In a store · Textbook dialogue 05-2", listOf(
                DialogueLine("帮我看一下这件衣服怎么样。", "Bāng wǒ kàn yíxià zhè jiàn yīfu zěnmeyàng.", "Help me take a look. What do you think of this garment?"),
                DialogueLine("颜色还可以，就是有点儿大。", "Yánsè hái kěyǐ, jiùshì yǒudiǎnr dà.", "The colour is acceptable; it is just a little big."),
                DialogueLine("这件小的怎么样？", "Zhè jiàn xiǎo de zěnmeyàng?", "What about this smaller one?"),
                DialogueLine("这件不错，就买这件吧。", "Zhè jiàn búcuò, jiù mǎi zhè jiàn ba.", "This one is pretty good. Let's buy this one.")
            )),
            Dialogue("3 · After an exam", "In the classroom · Textbook dialogue 05-3", listOf(
                DialogueLine("今天去不去打球？", "Jīntiān qù bu qù dǎ qiú?", "Are you going to play ball today?"),
                DialogueLine("这两天有点儿累，不去打球了。", "Zhè liǎng tiān yǒudiǎnr lèi, bú qù dǎ qiú le.", "I've been a little tired these two days, so I'm not going to play."),
                DialogueLine("你在做什么呢？是在想昨天的考试吗？", "Nǐ zài zuò shénme ne? Shì zài xiǎng zuótiān de kǎoshì ma?", "What are you doing? Are you thinking about yesterday's exam?"),
                DialogueLine("是啊，我觉得听和说还可以，读和写不好，很多字我都不知道是什么意思。", "Shì a, wǒ juéde tīng hé shuō hái kěyǐ, dú hé xiě bù hǎo, hěn duō zì wǒ dōu bù zhīdào shì shénme yìsi.", "Yes. Listening and speaking were acceptable, but reading and writing were not. I did not know the meaning of many characters.")
            )),
            Dialogue("4 · A coffee break", "In the company · Textbook dialogue 05-4", listOf(
                DialogueLine("休息一下吧，喝咖啡吗？", "Xiūxi yíxià ba, hē kāfēi ma?", "Let's take a break. Would you like some coffee?"),
                DialogueLine("不了，我已经喝两杯了。", "Bù le, wǒ yǐjīng hē liǎng bēi le.", "No thanks. I've already had two cups."),
                DialogueLine("是啊，咖啡喝多了对身体不好。", "Shì a, kāfēi hē duō le duì shēntǐ bù hǎo.", "Yes. Drinking too much coffee is bad for your health."),
                DialogueLine("以后我少喝一点儿，每天喝一杯。", "Yǐhòu wǒ shǎo hē yìdiǎnr, měitiān hē yì bēi.", "From now on I'll drink a little less, one cup a day.")
            ))
        ),
        words = listOf(
            Word("外面", "wàimiàn", "outside", "我不想去外面吃。", "Wǒ bù xiǎng qù wàimiàn chī.", "I don't want to eat out."),
            Word("准备", "zhǔnbèi", "to intend; to plan", "你准备做什么呢？", "Nǐ zhǔnbèi zuò shénme ne?", "What are you planning to do?"),
            Word("就", "jiù", "then; in that case", "就做你爱吃的鱼吧。", "Jiù zuò nǐ ài chī de yú ba.", "Then I'll make the fish you like."),
            Word("鱼", "yú", "fish", "就做鱼吧。", "Jiù zuò yú ba.", "Let's make fish."),
            Word("吧", "ba", "sentence-final particle for suggestions or requests", "就买这件吧。", "Jiù mǎi zhè jiàn ba.", "Let's buy this one."),
            Word("件", "jiàn", "measure word for clothes and some items", "这件衣服怎么样？", "Zhè jiàn yīfu zěnmeyàng?", "What do you think of this garment?"),
            Word("还", "hái", "fairly; passably; still", "颜色还可以。", "Yánsè hái kěyǐ.", "The colour is acceptable."),
            Word("可以", "kěyǐ", "okay; not bad; can", "颜色还可以。", "Yánsè hái kěyǐ.", "The colour is okay."),
            Word("不错", "búcuò", "pretty good; not bad", "这件不错。", "Zhè jiàn búcuò.", "This one is pretty good."),
            Word("考试", "kǎoshì", "exam; test", "昨天的考试。", "Zuótiān de kǎoshì.", "Yesterday's exam."),
            Word("意思", "yìsi", "meaning", "是什么意思？", "Shì shénme yìsi?", "What does it mean?"),
            Word("咖啡", "kāfēi", "coffee", "喝咖啡吗？", "Hē kāfēi ma?", "Would you like coffee?"),
            Word("对", "duì", "towards; with regard to", "对身体不好。", "Duì shēntǐ bù hǎo.", "Bad for your health."),
            Word("以后", "yǐhòu", "afterwards; from now on; later", "以后我少喝一点儿。", "Yǐhòu wǒ shǎo hē yìdiǎnr.", "From now on I'll drink a little less.")
        ),
        grammar = listOf(
            GrammarPoint("就 + verb · reaching a conclusion", "就 before a verb can introduce a conclusion or decision based on what has just been said. In a suggestion, it can sound like 'then / in that case'.", "Context + 就 + verb phrase", listOf(
                DialogueLine("你不想去，就在家休息吧。", "Nǐ bù xiǎng qù, jiù zài jiā xiūxi ba.", "If you don't want to go, then rest at home."),
                DialogueLine("就买这件吧。", "Jiù mǎi zhè jiàn ba.", "Then let's buy this one.")
            )),
            GrammarPoint("还 + adjective · acceptable / passable", "还 before an adjective can mean that something is just about acceptable or fairly good. In 颜色还可以，就是有点儿大, 就是 introduces a contrast or reservation.", "还 + adjective  /  A，就是 + B", listOf(
                DialogueLine("颜色还可以。", "Yánsè hái kěyǐ.", "The colour is acceptable."),
                DialogueLine("听和说还可以，读和写不好。", "Tīng hé shuō hái kěyǐ, dú hé xiě bù hǎo.", "Listening and speaking are acceptable, but reading and writing are not.")
            )),
            GrammarPoint("有点儿 + adjective / verb · a little (often negative)", "有点儿 commonly expresses a small amount with a negative, inconvenient or dissatisfied feeling, for example being a little tired or an item being a little too big.", "有点儿 + adjective / verb", listOf(
                DialogueLine("这两天有点儿累。", "Zhè liǎng tiān yǒudiǎnr lèi.", "I've been a little tired these two days."),
                DialogueLine("这件衣服有点儿大。", "Zhè jiàn yīfu yǒudiǎnr dà.", "This garment is a little too big.")
            ))
        ),
        questions = listOf(
            QuizQuestion("Where does the speaker want to eat dinner?", listOf("At a restaurant", "At home", "At the office", "At a café"), 1, "我想在家吃。", "Comprehension"),
            QuizQuestion("What does the speaker plan to cook?", listOf("Chicken", "Fish", "Noodles", "Rice"), 1, "就做你爱吃的鱼吧。", "Comprehension"),
            QuizQuestion("What is the problem with the first garment?", listOf("It is too small", "It is a little too big", "The colour is bad", "It is too expensive"), 1, "颜色还可以，就是有点儿大。", "Workbook picture-description style"),
            QuizQuestion("Which garment do they decide to buy?", listOf("The first one", "The smaller one", "The red one", "Neither"), 1, "这件不错，就买这件吧。", "Comprehension"),
            QuizQuestion("Why is the speaker not going to play ball?", listOf("He is busy", "He is a little tired", "He is ill", "He has no shoes"), 1, "这两天有点儿累。", "Comprehension"),
            QuizQuestion("Which skills does the speaker say are acceptable?", listOf("Reading and writing", "Listening and speaking", "Reading and listening", "Speaking and writing"), 1, "听和说还可以，读和写不好。", "Comprehension"),
            QuizQuestion("How many cups of coffee has the speaker already had?", listOf("One", "Two", "Three", "Four"), 1, "我已经喝两杯了。", "Comprehension"),
            QuizQuestion("What does 就 + verb express in this lesson?", listOf("A conclusion or decision", "A question about age", "Possession", "A past action only"), 0, "就做你爱吃的鱼吧 is a decision based on the previous statement.", "Grammar"),
            QuizQuestion("Choose the best completion: 这件衣服____大。", listOf("有点儿", "已经", "每", "旁边"), 0, "有点儿 + adjective expresses a small amount, often with a negative feeling.", "Workbook fill-in"),
            QuizQuestion("What does 以后 mean?", listOf("Before", "Beside", "Afterwards / from now on", "Every day"), 2, "以后 means afterwards, later or from now on.", "Vocabulary")
        ),
        pronunciation = "The textbook introduces sentence-level grammatical stress. Practise hearing which part of a sentence is emphasised, then listen to examples such as 这件衣服 zhè jiàn yīfu and 咖啡喝多了 kāfēi hē duō le."
    ),

    LessonContent(
        number = 6, title = "Why don't you eat more?", hanziTitle = "你怎么不吃了",
        textbookPages = "PDF pages 57–64", workbookPages = "PDF pages 51–58",
        dialogues = listOf(
            Dialogue("1 · At school", "Textbook 06-1", listOf(
                DialogueLine("你知道小王今天什么时候来学校吗？", "Nǐ zhīdào Xiǎo Wáng jīntiān shénme shíhou lái xuéxiào ma?", "Do you know when Xiao Wang will come to school today?"),
                DialogueLine("他已经来了。", "Tā yǐjīng lái le.", "He has already arrived."),
                DialogueLine("你怎么知道他来了？", "Nǐ zěnme zhīdào tā lái le?", "How do you know he has arrived?"),
                DialogueLine("我在门外看见他的自行车了。", "Wǒ zài mén wài kànjiàn tā de zìxíngchē le.", "I saw his bicycle outside the door.")
            )),
            Dialogue("2 · At a restaurant", "Textbook 06-2", listOf(
                DialogueLine("今天的羊肉很好吃，你怎么不吃了？", "Jīntiān de yángròu hěn hǎochī, nǐ zěnme bù chī le?", "The mutton is delicious today. Why have you stopped eating?"),
                DialogueLine("这个星期天天都吃羊肉，不想吃了。", "Zhège xīngqī tiāntiān dōu chī yángròu, bù xiǎng chī le.", "I've eaten mutton every day this week; I don't want any more."),
                DialogueLine("那你还想吃什么？", "Nà nǐ hái xiǎng chī shénme?", "Then what would you like to eat?"),
                DialogueLine("来一点儿面条吧。", "Lái yìdiǎnr miàntiáo ba.", "Some noodles, please.")
            )),
            Dialogue("3 · At the gym", "Textbook 06-3", listOf(
                DialogueLine("昨天你们怎么都没去打篮球？", "Zuótiān nǐmen zěnme dōu méi qù dǎ lánqiú?", "Why didn't you all go play basketball yesterday?"),
                DialogueLine("因为昨天下雨，所以我们都没去。", "Yīnwèi zuótiān xià yǔ, suǒyǐ wǒmen dōu méi qù.", "Because it rained yesterday, so none of us went."),
                DialogueLine("你经常游泳吗？", "Nǐ jīngcháng yóuyǒng ma?", "Do you swim often?"),
                DialogueLine("这个月我天天游泳。", "Zhège yuè wǒ tiāntiān yóuyǒng.", "I've been swimming every day this month.")
            )),
            Dialogue("4 · In the office", "Textbook 06-4", listOf(
                DialogueLine("这两天怎么没看见小张？", "Zhè liǎng tiān zěnme méi kànjiàn Xiǎo Zhāng?", "Why haven't I seen Xiao Zhang these last two days?"),
                DialogueLine("他去北京了。", "Tā qù Běijīng le.", "He has gone to Beijing."),
                DialogueLine("是去旅游吗？", "Shì qù lǚyóu ma?", "Is he there travelling?"),
                DialogueLine("不是，听说是去看他姐姐。", "Bú shì, tīngshuō shì qù kàn tā jiějie.", "No, I heard he's gone to visit his elder sister.")
            ))
        ),
        words = listOf(
            Word("门", "mén", "door; gate", "他在门外。", "Tā zài mén wài.", "He is outside the door."),
            Word("外", "wài", "outside; outer", "门外有一辆自行车。", "Mén wài yǒu yí liàng zìxíngchē.", "There is a bicycle outside the door."),
            Word("自行车", "zìxíngchē", "bicycle", "我骑自行车去学校。", "Wǒ qí zìxíngchē qù xuéxiào.", "I ride a bicycle to school."),
            Word("羊肉", "yángròu", "mutton", "我喜欢吃羊肉。", "Wǒ xǐhuan chī yángròu.", "I like eating mutton."),
            Word("好吃", "hǎochī", "delicious", "这个菜很好吃。", "Zhège cài hěn hǎochī.", "This dish is delicious."),
            Word("面条", "miàntiáo", "noodles", "我想吃面条。", "Wǒ xiǎng chī miàntiáo.", "I want to eat noodles."),
            Word("打篮球", "dǎ lánqiú", "play basketball", "他们常常打篮球。", "Tāmen chángcháng dǎ lánqiú.", "They often play basketball."),
            Word("因为", "yīnwèi", "because", "因为下雨，我没去。", "Yīnwèi xià yǔ, wǒ méi qù.", "Because it rained, I didn't go."),
            Word("所以", "suǒyǐ", "so; therefore", "下雨了，所以我在家。", "Xià yǔ le, suǒyǐ wǒ zài jiā.", "It rained, so I stayed home."),
            Word("游泳", "yóuyǒng", "swim", "我每天游泳。", "Wǒ měitiān yóuyǒng.", "I swim every day."),
            Word("经常", "jīngcháng", "often", "你经常运动吗？", "Nǐ jīngcháng yùndòng ma?", "Do you exercise often?"),
            Word("公斤", "gōngjīn", "kilogram", "我七十公斤。", "Wǒ qīshí gōngjīn.", "I weigh 70 kilograms."),
            Word("姐姐", "jiějie", "older sister", "我姐姐在北京。", "Wǒ jiějie zài Běijīng.", "My elder sister is in Beijing.")
        ),
        grammar = listOf(
            GrammarPoint("怎么 + verb/adjective · asking why", "怎么 can ask for a reason and often conveys surprise.", "怎么 + verb / adjective", listOf(DialogueLine("你怎么不吃了？", "Nǐ zěnme bù chī le?", "Why have you stopped eating?"))),
            GrammarPoint("Reduplicated measure/time words · every", "Repeating a measure word can mean each/every; it is often followed by 都.", "天天 / 件件 / 个个 + 都", listOf(DialogueLine("这个星期天天都吃羊肉。", "Zhège xīngqī tiāntiān dōu chī yángròu.", "I eat mutton every day this week."))),
            GrammarPoint("因为……所以…… · because…therefore…", "因为 introduces the reason; 所以 introduces the result. Either may be omitted when the relationship is clear.", "因为 + reason，所以 + result", listOf(DialogueLine("因为昨天下雨，所以我们都没去。", "Yīnwèi zuótiān xià yǔ, suǒyǐ wǒmen dōu méi qù.", "Because it rained yesterday, none of us went.")))
        ),
        questions = listOf(
            QuizQuestion("Where did Xiao Wang's bicycle appear?", listOf("Inside the classroom", "Outside the door", "At the gym", "At home"), 1, "我在门外看见他的自行车了。", "Textbook dialogue 06-1"),
            QuizQuestion("Why does the diner stop eating mutton?", listOf("It is cold", "He eats it every day this week", "He is late", "It is expensive"), 1, "这个星期天天都吃羊肉，不想吃了。", "Textbook dialogue 06-2"),
            QuizQuestion("What food does he ask for instead?", listOf("Rice", "Dumplings", "Noodles", "Fish"), 2, "来一点儿面条吧。", "Textbook dialogue 06-2"),
            QuizQuestion("Why didn't they play basketball?", listOf("It rained", "They were busy", "The gym was closed", "They were tired"), 0, "因为昨天下雨，所以我们都没去。", "Textbook dialogue 06-3"),
            QuizQuestion("What does 经常 mean?", listOf("Never", "Often", "Yesterday", "Already"), 1, "经常 means often or frequently.", "Vocabulary"),
            QuizQuestion("Who has Xiao Zhang gone to visit?", listOf("His teacher", "His elder sister", "His friend", "His parents"), 1, "听说是去看他姐姐。", "Textbook dialogue 06-4"),
            QuizQuestion("Choose the correct causal connector: ____下雨，____我们没去。", listOf("因为 / 所以", "虽然 / 但是", "如果 / 就", "先 / 再"), 0, "因为 introduces the cause; 所以 introduces the result.", "Grammar"),
            QuizQuestion("天天 most naturally means…", listOf("Every day", "Tomorrow", "Sometimes", "Last week"), 0, "天天 is a reduplicated time word meaning every day.", "Grammar"),
            QuizQuestion("What does 怎么 ask in 你怎么不吃了？", listOf("Where", "Why", "Who", "How many"), 1, "Here 怎么 asks why, often with surprise.", "Grammar"),
            QuizQuestion("公斤 is a unit of…", listOf("Distance", "Time", "Weight", "Temperature"), 2, "公斤 means kilogram.", "Vocabulary")
        ), pronunciation = "Practise the surprise intonation in 怎么不吃了？ and emphasise the cause/result clauses in 因为……所以……。"
    ),
    LessonContent(
        number = 7, title = "Do you live far from your company?", hanziTitle = "你家离公司远吗",
        textbookPages = "PDF pages 65–72", workbookPages = "PDF pages 59–68",
        dialogues = listOf(
            Dialogue("1 · At home", "Textbook 07-1", listOf(
                DialogueLine("大卫回来了吗？", "Dàwèi huílai le ma?", "Has David come back?"),
                DialogueLine("没有，他还在教室学习呢。", "Méiyǒu, tā hái zài jiàoshì xuéxí ne.", "No, he is still studying in the classroom."),
                DialogueLine("已经九点多了，他怎么还在学习？", "Yǐjīng jiǔ diǎn duō le, tā zěnme hái zài xuéxí?", "It's after nine; why is he still studying?"),
                DialogueLine("明天有考试，他说今天要好好准备。", "Míngtiān yǒu kǎoshì, tā shuō jīntiān yào hǎohāo zhǔnbèi.", "There is an exam tomorrow; he said he needs to prepare well today.")
            )),
            Dialogue("2 · On the way to the airport", "Textbook 07-2", listOf(
                DialogueLine("你现在在哪儿呢？", "Nǐ xiànzài zài nǎr ne?", "Where are you now?"),
                DialogueLine("在去机场的路上。你已经到了吗？", "Zài qù jīchǎng de lùshang. Nǐ yǐjīng dào le ma?", "I'm on the way to the airport. Have you arrived?"),
                DialogueLine("我下飞机了。你还有多长时间能到这儿？", "Wǒ xià fēijī le. Nǐ hái yǒu duō cháng shíjiān néng dào zhèr?", "I've got off the plane. How much longer until you arrive?"),
                DialogueLine("二十分钟就到。", "Èrshí fēnzhōng jiù dào.", "I'll be there in just 20 minutes.")
            )),
            Dialogue("3 · At the gym", "Textbook 07-3", listOf(
                DialogueLine("你家离公司远吗？", "Nǐ jiā lí gōngsī yuǎn ma?", "Do you live far from your company?"),
                DialogueLine("很远，坐公共汽车要一个多小时呢！", "Hěn yuǎn, zuò gōnggòng qìchē yào yí ge duō xiǎoshí ne!", "Very far; it takes over an hour by bus!"),
                DialogueLine("坐公共汽车太慢了，你怎么不开车？", "Zuò gōnggòng qìchē tài màn le, nǐ zěnme bù kāichē?", "The bus is too slow. Why don't you drive?"),
                DialogueLine("开车也不快，路上车太多了！", "Kāichē yě bú kuài, lùshang chē tài duō le!", "Driving isn't fast either; there are too many cars on the road!")
            )),
            Dialogue("4 · Making birthday plans", "Textbook 07-4", listOf(
                DialogueLine("今天晚上我们一起吃饭吧，给你过生日。", "Jīntiān wǎnshang wǒmen yìqǐ chīfàn ba, gěi nǐ guò shēngrì.", "Let's have dinner together tonight to celebrate your birthday."),
                DialogueLine("今天？离我的生日还有一个多星期呢！", "Jīntiān? Lí wǒ de shēngrì hái yǒu yí ge duō xīngqī ne!", "Today? My birthday is still more than a week away!"),
                DialogueLine("下个星期我要去北京，今天过吧。", "Xià ge xīngqī wǒ yào qù Běijīng, jīntiān guò ba.", "I'm going to Beijing next week, so let's celebrate today."),
                DialogueLine("好吧，离这儿不远有一个中国饭馆。", "Hǎo ba, lí zhèr bù yuǎn yǒu yí ge Zhōngguó fànguǎn.", "All right. There is a Chinese restaurant not far from here.")
            ))
        ),
        words = listOf(
            Word("教室", "jiàoshì", "classroom", "他在教室学习。", "Tā zài jiàoshì xuéxí.", "He is studying in the classroom."),
            Word("机场", "jīchǎng", "airport", "我去机场接你。", "Wǒ qù jīchǎng jiē nǐ.", "I'll go to the airport to meet you."),
            Word("路", "lù", "road; way", "路上车很多。", "Lùshang chē hěn duō.", "There are many cars on the road."),
            Word("离", "lí", "to be away from", "我家离学校很近。", "Wǒ jiā lí xuéxiào hěn jìn.", "My home is close to school."),
            Word("公司", "gōngsī", "company", "我在这家公司工作。", "Wǒ zài zhè jiā gōngsī gōngzuò.", "I work at this company."),
            Word("远", "yuǎn", "far", "机场很远。", "Jīchǎng hěn yuǎn.", "The airport is far."),
            Word("公共汽车", "gōnggòng qìchē", "bus", "我坐公共汽车上班。", "Wǒ zuò gōnggòng qìchē shàngbān.", "I take the bus to work."),
            Word("小时", "xiǎoshí", "hour", "要一个小时。", "Yào yí ge xiǎoshí.", "It takes one hour."),
            Word("慢", "màn", "slow", "公共汽车很慢。", "Gōnggòng qìchē hěn màn.", "The bus is slow."),
            Word("快", "kuài", "fast", "飞机很快。", "Fēijī hěn kuài.", "The plane is fast."),
            Word("过", "guò", "celebrate; pass", "我们一起过生日。", "Wǒmen yìqǐ guò shēngrì.", "Let's celebrate a birthday together."),
            Word("生日", "shēngrì", "birthday", "今天是我的生日。", "Jīntiān shì wǒ de shēngrì.", "Today is my birthday.")
        ),
        grammar = listOf(
            GrammarPoint("还在 / 还没 · still / not yet", "还在 + verb indicates an action is continuing. 还没 + verb indicates it has not happened yet.", "还在 + V；还没 + V", listOf(DialogueLine("他还在教室学习呢。", "Tā hái zài jiàoshì xuéxí ne.", "He is still studying in the classroom."))),
            GrammarPoint("就 · sooner than expected", "就 can emphasise that something happens early, quickly or with little delay.", "Time / duration + 就 + verb", listOf(DialogueLine("二十分钟就到。", "Èrshí fēnzhōng jiù dào.", "I'll arrive in just 20 minutes."))),
            GrammarPoint("A 离 B + distance", "离 expresses distance between places or points in time. Put the reference point after 离.", "A + 离 + B + 远 / 近 / distance", listOf(DialogueLine("你家离公司远吗？", "Nǐ jiā lí gōngsī yuǎn ma?", "Do you live far from your company?")))
        ),
        questions = listOf(
            QuizQuestion("Where is David still studying?", listOf("At home", "In the classroom", "At the airport", "At work"), 1, "他还在教室学习呢。", "Textbook dialogue 07-1"),
            QuizQuestion("Why is David preparing late?", listOf("He has an exam tomorrow", "He is travelling", "He is working", "He is ill"), 0, "明天有考试，他说今天要好好准备。", "Textbook dialogue 07-1"),
            QuizQuestion("Where is the traveller?", listOf("On the way to the airport", "At the office", "At home", "On the bus"), 0, "在去机场的路上。", "Textbook dialogue 07-2"),
            QuizQuestion("How soon will the person arrive?", listOf("10 minutes", "20 minutes", "One hour", "Tomorrow"), 1, "二十分钟就到。", "Textbook dialogue 07-2"),
            QuizQuestion("How long does the bus take to work?", listOf("20 minutes", "Half an hour", "Over an hour", "Two hours"), 2, "坐公共汽车要一个多小时。", "Textbook dialogue 07-3"),
            QuizQuestion("Why is driving not faster?", listOf("The car is old", "There are too many cars", "The road is closed", "It is raining"), 1, "路上车太多了。", "Textbook dialogue 07-3"),
            QuizQuestion("What does 离 express?", listOf("Possession", "Distance from", "Past tense", "Frequency"), 1, "离 indicates distance between places or times.", "Grammar"),
            QuizQuestion("还在学习 means…", listOf("Already studied", "Still studying", "Will study", "Never studies"), 1, "还在 indicates a continuing action.", "Grammar"),
            QuizQuestion("就到 in 二十分钟就到 suggests…", listOf("A long delay", "Arrival quite soon", "A cancelled trip", "A past event"), 1, "就 emphasises a quick or earlier-than-expected result.", "Grammar"),
            QuizQuestion("小时 is a unit of…", listOf("Minutes", "Hours", "Days", "Kilometres"), 1, "小时 means hour.", "Vocabulary")
        ), pronunciation = "Practise the rising tone of yes/no questions and falling tone in statements. Compare 你家离公司远吗？ with 很远。"
    ),
    LessonContent(
        number = 8, title = "Let me think about it and tell you later", hanziTitle = "让我想想再告诉你",
        textbookPages = "PDF pages 73–80", workbookPages = "PDF pages 69–78",
        dialogues = listOf(
            Dialogue("1 · In the classroom", "Textbook 08-1", listOf(
                DialogueLine("我们下午去看电影，好吗？", "Wǒmen xiàwǔ qù kàn diànyǐng, hǎo ma?", "Shall we go to see a film this afternoon?"),
                DialogueLine("今天下午我没有时间，明天下午再去吧。", "Jīntiān xiàwǔ wǒ méiyǒu shíjiān, míngtiān xiàwǔ zài qù ba.", "I don't have time this afternoon; let's go tomorrow afternoon instead."),
                DialogueLine("你想看什么电影？", "Nǐ xiǎng kàn shénme diànyǐng?", "What film would you like to see?"),
                DialogueLine("让我想想再告诉你。", "Ràng wǒ xiǎngxiang zài gàosu nǐ.", "Let me think about it and tell you later.")
            )),
            Dialogue("2 · Making a decision", "Lesson 08 practice dialogue", listOf(
                DialogueLine("你想什么时候去？", "Nǐ xiǎng shénme shíhou qù?", "When would you like to go?"),
                DialogueLine("让我考虑考虑。", "Ràng wǒ kǎolǜ kǎolǜ.", "Let me think it over."),
                DialogueLine("好，想好了再告诉我。", "Hǎo, xiǎng hǎo le zài gàosu wǒ.", "All right, tell me when you've decided.")
            ))
        ),
        words = listOf(
            Word("让", "ràng", "let; allow; make", "让我看看。", "Ràng wǒ kànkan.", "Let me have a look."),
            Word("想", "xiǎng", "think; want; miss", "我想一想。", "Wǒ xiǎng yi xiǎng.", "Let me think."),
            Word("再", "zài", "then; again; afterwards", "明天再说。", "Míngtiān zài shuō.", "Let's talk tomorrow."),
            Word("告诉", "gàosu", "tell; inform", "请告诉我。", "Qǐng gàosu wǒ.", "Please tell me."),
            Word("电影", "diànyǐng", "film; movie", "我喜欢看电影。", "Wǒ xǐhuan kàn diànyǐng.", "I like watching films."),
            Word("时间", "shíjiān", "time", "你有时间吗？", "Nǐ yǒu shíjiān ma?", "Do you have time?"),
            Word("考虑", "kǎolǜ", "consider; think over", "让我考虑一下。", "Ràng wǒ kǎolǜ yíxià.", "Let me consider it."),
            Word("运动", "yùndòng", "sport; exercise", "我喜欢运动。", "Wǒ xǐhuan yùndòng.", "I like sports."),
            Word("希望", "xīwàng", "hope; wish", "我希望你来。", "Wǒ xīwàng nǐ lái.", "I hope you come.")
        ),
        grammar = listOf(
            GrammarPoint("Verb reduplication · brief or casual action", "Repeating a verb often makes the action sound brief, tentative or relaxed.", "V → VV / V一V", listOf(DialogueLine("让我想想。", "Ràng wǒ xiǎngxiang.", "Let me think."))),
            GrammarPoint("再 + verb · do it later / then", "再 places an action after another action or at a later time.", "先……再…… / V + 再 + V", listOf(DialogueLine("让我想想再告诉你。", "Ràng wǒ xiǎngxiang zài gàosu nǐ.", "Let me think and tell you later."))),
            GrammarPoint("让 + person + verb · let / ask someone to", "让 introduces the person who is permitted, asked or caused to do something.", "让 + person + verb (+ object)", listOf(DialogueLine("让我想想。", "Ràng wǒ xiǎngxiang.", "Let me think.")))
        ),
        questions = listOf(
            QuizQuestion("What do they plan to do?", listOf("Watch a film", "Go swimming", "Study at home", "Go shopping"), 0, "我们下午去看电影，好吗？", "Textbook dialogue 08-1"),
            QuizQuestion("Why not this afternoon?", listOf("No money", "No time", "No tickets", "Too far"), 1, "今天下午我没有时间。", "Textbook dialogue 08-1"),
            QuizQuestion("When do they suggest going instead?", listOf("Tonight", "Tomorrow afternoon", "Next week", "Next month"), 1, "明天下午再去吧。", "Textbook dialogue 08-1"),
            QuizQuestion("再 in this lesson most often means…", listOf("Before", "Then / later", "Because", "Already"), 1, "再 places the next action later or after another action.", "Grammar"),
            QuizQuestion("想想 is an example of…", listOf("Verb reduplication", "A measure word", "A question particle", "A location phrase"), 0, "想想 is the reduplicated form of 想.", "Grammar"),
            QuizQuestion("What does 让我想想 mean?", listOf("Make me leave", "Let me think", "Tell me now", "I have forgotten"), 1, "让 + person + verb means let/ask someone to do something.", "Grammar"),
            QuizQuestion("告诉 means…", listOf("Forget", "Tell", "Wait", "Watch"), 1, "告诉 means to tell or inform.", "Vocabulary"),
            QuizQuestion("电影 means…", listOf("Music", "Movie", "Book", "Exercise"), 1, "电影 means film or movie.", "Vocabulary"),
            QuizQuestion("Choose the natural order: think, then tell me", listOf("告诉再想想", "想想再告诉我", "再我告诉想想", "想告诉再我"), 1, "想想再告诉我 places thinking before telling.", "Workbook-style sentence order"),
            QuizQuestion("Verb reduplication often makes an action sound…", listOf("Brief or casual", "Impossible", "Past only", "Very formal"), 0, "Reduplication can soften or limit the action.", "Grammar")
        ), pronunciation = "Listen for the repeated syllables in 想想 (xiǎngxiang) and 考虑考虑 (kǎolǜ kǎolǜ). Keep the second syllable light and natural."
    ),
    LessonContent(
        number = 9, title = "There were too many questions; I didn't finish them all", hanziTitle = "题太多了，我没做完",
        textbookPages = "PDF pages 81–90", workbookPages = "PDF pages 79–98",
        dialogues = listOf(
            Dialogue("1 · On the phone", "Textbook 09-1", listOf(
                DialogueLine("你好！请问张欢在吗？", "Nǐ hǎo! Qǐngwèn Zhāng Huān zài ma?", "Hello! May I speak to Zhang Huan?"),
                DialogueLine("你打错了，我们这儿没有人叫张欢的。", "Nǐ dǎ cuò le, wǒmen zhèr méiyǒu rén jiào Zhāng Huān de.", "You have the wrong number. Nobody here is called Zhang Huan."),
                DialogueLine("对不起。", "Duìbuqǐ.", "Sorry.")
            )),
            Dialogue("2 · At school", "Textbook 09-2", listOf(
                DialogueLine("您从几岁开始学习跳舞？", "Nín cóng jǐ suì kāishǐ xuéxí tiàowǔ?", "At what age did you start learning to dance?"),
                DialogueLine("我第一次跳舞是在七岁的时候。", "Wǒ dì yī cì tiàowǔ shì zài qī suì de shíhou.", "The first time I danced was when I was seven."),
                DialogueLine("我女儿今年也七岁了。我希望她能跟您学跳舞，可以吗？", "Wǒ nǚ'ér jīnnián yě qī suì le. Wǒ xīwàng tā néng gēn nín xué tiàowǔ, kěyǐ ma?", "My daughter is seven too. I hope she can learn dancing from you. Is that possible?"),
                DialogueLine("没问题，非常欢迎。", "Méi wèntí, fēicháng huānyíng.", "No problem. She is very welcome.")
            )),
            Dialogue("3 · At home", "Textbook 09-3", listOf(
                DialogueLine("你知道吗？大卫找到工作了。", "Nǐ zhīdào ma? Dàwèi zhǎodào gōngzuò le.", "Did you know? David has found a job."),
                DialogueLine("太好了！他从什么时候开始上班？", "Tài hǎo le! Tā cóng shénme shíhou kāishǐ shàngbān?", "That's great! When does he start work?"),
                DialogueLine("从下个星期一开始。", "Cóng xià ge xīngqīyī kāishǐ.", "Starting next Monday."),
                DialogueLine("这是他的第一个工作，希望他能喜欢。", "Zhè shì tā de dì yī ge gōngzuò, xīwàng tā néng xǐhuan.", "This is his first job; I hope he likes it.")
            )),
            Dialogue("4 · In the classroom", "Textbook 09-4", listOf(
                DialogueLine("昨天的考试怎么样？你都听懂了吗？", "Zuótiān de kǎoshì zěnmeyàng? Nǐ dōu tīngdǒng le ma?", "How was yesterday's exam? Did you understand everything?"),
                DialogueLine("听懂了。", "Tīngdǒng le.", "I understood it."),
                DialogueLine("你都做完了没有？", "Nǐ dōu zuòwán le méiyǒu?", "Did you finish all the questions?"),
                DialogueLine("题太多，我没做完。", "Tí tài duō, wǒ méi zuòwán.", "There were too many questions; I didn't finish them all.")
            ))
        ),
        words = listOf(
            Word("错", "cuò", "wrong; incorrect", "你打错电话了。", "Nǐ dǎ cuò diànhuà le.", "You called the wrong number."),
            Word("从", "cóng", "from; starting at", "我从家来。", "Wǒ cóng jiā lái.", "I came from home."),
            Word("跳舞", "tiàowǔ", "dance", "她喜欢跳舞。", "Tā xǐhuan tiàowǔ.", "She likes dancing."),
            Word("第一", "dì yī", "first", "这是第一课。", "Zhè shì dì yī kè.", "This is the first lesson."),
            Word("希望", "xīwàng", "hope; wish", "我希望你成功。", "Wǒ xīwàng nǐ chénggōng.", "I hope you succeed."),
            Word("问题", "wèntí", "question; problem", "我有一个问题。", "Wǒ yǒu yí ge wèntí.", "I have a question."),
            Word("欢迎", "huānyíng", "welcome", "欢迎你来。", "Huānyíng nǐ lái.", "You are welcome to come."),
            Word("上班", "shàngbān", "go to work; be at work", "我八点上班。", "Wǒ bā diǎn shàngbān.", "I start work at eight."),
            Word("懂", "dǒng", "understand", "我听懂了。", "Wǒ tīngdǒng le.", "I understood."),
            Word("做完", "zuòwán", "finish doing", "我做完作业了。", "Wǒ zuòwán zuòyè le.", "I finished my homework.")
        ),
        grammar = listOf(
            GrammarPoint("Result complements · V + result", "A result complement follows a verb to state the result. Examples: 听懂 understand by listening; 找到 find; 做完 finish.", "V + result complement", listOf(DialogueLine("我没做完。", "Wǒ méi zuòwán.", "I didn't finish."))),
            GrammarPoint("Negating a result complement", "Use 没(有) before the verb + complement. Do not add sentence-final 了 in this negative pattern.", "没(有) + V + result", listOf(DialogueLine("我没听懂。", "Wǒ méi tīngdǒng.", "I didn't understand what I heard."))),
            GrammarPoint("从……到…… and 第 + number", "从 marks a starting point and can pair with 到. 第 before a number marks order: 第一 (first), 第二 (second).", "从 A 到 B；第 + number", listOf(DialogueLine("从下个星期一开始。", "Cóng xià ge xīngqīyī kāishǐ.", "Starting next Monday.")))
        ),
        questions = listOf(
            QuizQuestion("What happened in the phone call?", listOf("Wrong number", "No signal", "Busy line", "Wrong time"), 0, "你打错了。", "Textbook dialogue 09-1"),
            QuizQuestion("At what age did the teacher first dance?", listOf("Five", "Six", "Seven", "Eight"), 2, "第一次跳舞是在七岁的时候。", "Textbook dialogue 09-2"),
            QuizQuestion("What does the mother hope for her daughter?", listOf("Learn dancing", "Learn English", "Get a job", "Go to Beijing"), 0, "我希望她能跟您学跳舞。", "Textbook dialogue 09-2"),
            QuizQuestion("When does David start work?", listOf("Today", "Next Monday", "Next month", "Tomorrow"), 1, "从下个星期一开始。", "Textbook dialogue 09-3"),
            QuizQuestion("Did the student finish all the exam questions?", listOf("Yes", "No, there were too many", "No, he arrived late", "The dialogue doesn't say"), 1, "题太多，我没做完。", "Textbook dialogue 09-4"),
            QuizQuestion("听懂 means…", listOf("Hear and understand", "Speak loudly", "Read quickly", "Write down"), 0, "懂 is a result complement meaning understand.", "Vocabulary / grammar"),
            QuizQuestion("How do you negate 做完?", listOf("不做完", "没做完", "做不没", "没了做完"), 1, "The negative pattern is 没 + verb + result complement.", "Grammar"),
            QuizQuestion("第一个 means…", listOf("The last one", "The first one", "One more", "Every one"), 1, "第 + number marks ordinal sequence.", "Grammar"),
            QuizQuestion("从 introduces…", listOf("A starting point", "A question", "A comparison", "A reason"), 0, "从 marks the starting point of time, distance or sequence.", "Grammar"),
            QuizQuestion("欢迎 means…", listOf("Welcome", "Wrong", "Finish", "Understand"), 0, "欢迎 means welcome.", "Vocabulary")
        ), pronunciation = "Practise the result complements as one spoken unit: tīng-dǒng, zhǎo-dào, zuò-wán. In questions ending with 吗, use the yes/no-question intonation from this lesson."
    ),
    LessonContent(
        number = 10, title = "Stop looking; your phone is on the desk", hanziTitle = "别找了，手机在桌子上呢",
        textbookPages = "PDF pages 91–96", workbookPages = "PDF pages 99–108",
        dialogues = listOf(
            Dialogue("1 · At home", "Textbook 10-1", listOf(
                DialogueLine("不要看电视了，明天上午还有汉语课呢。", "Bú yào kàn diànshì le, míngtiān shàngwǔ hái yǒu Hànyǔ kè ne.", "Stop watching TV; you have Chinese class tomorrow morning."),
                DialogueLine("看电视对学汉语有帮助。", "Kàn diànshì duì xué Hànyǔ yǒu bāngzhù.", "Watching TV helps with learning Chinese."),
                DialogueLine("明天的课你都准备好了吗？", "Míngtiān de kè nǐ dōu zhǔnbèi hǎo le ma?", "Have you prepared everything for tomorrow's lesson?"),
                DialogueLine("都准备好了。", "Dōu zhǔnbèi hǎo le.", "Everything is ready.")
            )),
            Dialogue("2 · At the hospital", "Textbook 10-2", listOf(
                DialogueLine("别看报纸了，医生说你要多休息。", "Bié kàn bàozhǐ le, yīshēng shuō nǐ yào duō xiūxi.", "Stop reading the newspaper. The doctor says you need more rest."),
                DialogueLine("好，不看了。给我一杯茶吧。", "Hǎo, bù kàn le. Gěi wǒ yì bēi chá ba.", "All right, I'll stop. Give me a cup of tea."),
                DialogueLine("医生说吃药后两个小时不要喝茶。", "Yīshēng shuō chī yào hòu liǎng ge xiǎoshí bú yào hē chá.", "The doctor says not to drink tea for two hours after taking medicine.")
            )),
            Dialogue("3 · At home", "Textbook 10-3", listOf(
                DialogueLine("你怎么买了这么多东西啊？", "Nǐ zěnme mǎi le zhème duō dōngxi a?", "Why did you buy so many things?"),
                DialogueLine("哥哥今天中午回来吃饭。", "Gēge jīntiān zhōngwǔ huílai chīfàn.", "Our elder brother is coming back for lunch today."),
                DialogueLine("我看看，羊肉、鸡蛋、面条、西瓜……真不少！妈妈呢？", "Wǒ kànkan, yángròu, jīdàn, miàntiáo, xīguā… zhēn bù shǎo! Māma ne?", "Let's see: mutton, eggs, noodles, watermelon… that's a lot! Where is Mom?"),
                DialogueLine("正在准备午饭呢！", "Zhèngzài zhǔnbèi wǔfàn ne!", "She's preparing lunch!")
            )),
            Dialogue("4 · Looking for a phone", "Textbook 10-4", listOf(
                DialogueLine("你在找什么？", "Nǐ zài zhǎo shénme?", "What are you looking for?"),
                DialogueLine("你看见我的手机了吗？白色的。", "Nǐ kànjiàn wǒ de shǒujī le ma? Báisé de.", "Have you seen my phone? The white one."),
                DialogueLine("别找了，手机在桌子上呢，电脑旁边。", "Bié zhǎo le, shǒujī zài zhuōzi shàng ne, diànnǎo pángbiān.", "Stop looking; the phone is on the desk, beside the computer."),
                DialogueLine("你看见我的衣服了吗？红色的那件。", "Nǐ kànjiàn wǒ de yīfu le ma? Hóngsè de nà jiàn.", "Have you seen my clothes? The red one."),
                DialogueLine("那件衣服我帮你洗了，在外边呢。", "Nà jiàn yīfu wǒ bāng nǐ xǐ le, zài wàibian ne.", "I washed that garment for you; it's outside.")
            ))
        ),
        words = listOf(
            Word("课", "kè", "class; lesson", "明天有汉语课。", "Míngtiān yǒu Hànyǔ kè.", "There is Chinese class tomorrow."),
            Word("帮助", "bāngzhù", "help; assist", "看电视对学习有帮助。", "Kàn diànshì duì xuéxí yǒu bāngzhù.", "Watching TV helps learning."),
            Word("别", "bié", "don't; stop", "别着急。", "Bié zháojí.", "Don't worry."),
            Word("哥哥", "gēge", "older brother", "我哥哥今天回来。", "Wǒ gēge jīntiān huílai.", "My elder brother is coming back today."),
            Word("鸡蛋", "jīdàn", "egg", "我买了鸡蛋。", "Wǒ mǎi le jīdàn.", "I bought eggs."),
            Word("西瓜", "xīguā", "watermelon", "西瓜很好吃。", "Xīguā hěn hǎochī.", "Watermelon is delicious."),
            Word("正在", "zhèngzài", "in the process of", "妈妈正在做饭。", "Māma zhèngzài zuòfàn.", "Mom is cooking."),
            Word("手机", "shǒujī", "mobile phone", "我的手机在桌子上。", "Wǒ de shǒujī zài zhuōzi shàng.", "My phone is on the desk."),
            Word("桌子", "zhuōzi", "table; desk", "书在桌子上。", "Shū zài zhuōzi shàng.", "The book is on the desk."),
            Word("旁边", "pángbiān", "beside; next to", "电脑在手机旁边。", "Diànnǎo zài shǒujī pángbiān.", "The computer is beside the phone."),
            Word("衣服", "yīfu", "clothes; garment", "红色的衣服在外边。", "Hóngsè de yīfu zài wàibian.", "The red garment is outside."),
            Word("正在", "zhèngzài", "currently; in progress", "我正在学习。", "Wǒ zhèngzài xuéxí.", "I am studying right now.")
        ),
        grammar = listOf(
            GrammarPoint("不要……了 / 别……了 · stop doing", "These patterns tell someone to stop or not continue an action.", "不要 / 别 + verb (+ object) + 了", listOf(DialogueLine("别找了。", "Bié zhǎo le.", "Stop looking."))),
            GrammarPoint("对 + noun + 有帮助 · be helpful for", "对 identifies the target or area affected; 有帮助 means be helpful.", "A + 对 + B + 有帮助", listOf(DialogueLine("看电视对学汉语有帮助。", "Kàn diànshì duì xué Hànyǔ yǒu bāngzhù.", "Watching TV helps with learning Chinese."))),
            GrammarPoint("正在 + verb · action in progress", "正在 before a verb indicates that an action is happening right now. 呢 can appear at the end for emphasis.", "正在 + verb (+ object) + 呢", listOf(DialogueLine("妈妈正在准备午饭呢！", "Māma zhèngzài zhǔnbèi wǔfàn ne!", "Mom is preparing lunch!")))
        ),
        questions = listOf(
            QuizQuestion("Why should the student stop watching TV?", listOf("Chinese class is tomorrow", "The TV is broken", "It is too late for dinner", "The programme is finished"), 0, "明天上午还有汉语课呢。", "Textbook dialogue 10-1"),
            QuizQuestion("What does the speaker say TV helps with?", listOf("Cooking", "Learning Chinese", "Sleeping", "Driving"), 1, "看电视对学汉语有帮助。", "Textbook dialogue 10-1"),
            QuizQuestion("What does the doctor recommend?", listOf("More exercise", "More rest", "More tea", "Watch TV"), 1, "医生说你要多休息。", "Textbook dialogue 10-2"),
            QuizQuestion("How long should the patient avoid tea after medicine?", listOf("One hour", "Two hours", "Three hours", "All day"), 1, "吃药后两个小时不要喝茶。", "Textbook dialogue 10-2"),
            QuizQuestion("Why did the family buy so much food?", listOf("Guests are coming for lunch", "They are moving house", "A party is tomorrow", "The food is on sale"), 0, "哥哥今天中午回来吃饭。", "Textbook dialogue 10-3"),
            QuizQuestion("What is Mom doing?", listOf("Sleeping", "Preparing lunch", "Shopping", "Reading"), 1, "正在准备午饭呢。", "Textbook dialogue 10-3"),
            QuizQuestion("Where is the phone?", listOf("In the bag", "On the desk beside the computer", "Outside", "In the kitchen"), 1, "手机在桌子上呢，电脑旁边。", "Textbook dialogue 10-4"),
            QuizQuestion("What does 别找了 mean?", listOf("Keep looking", "Stop looking", "Look outside", "Find the table"), 1, "别 + verb + 了 tells someone to stop the action.", "Grammar"),
            QuizQuestion("正在 indicates…", listOf("An action in progress", "A completed action", "A future plan", "A question"), 0, "正在 before a verb marks an action happening now.", "Grammar"),
            QuizQuestion("对……有帮助 means…", listOf("Be harmful to", "Be helpful for", "Be far from", "Be similar to"), 1, "对 + target + 有帮助 means helpful for the target.", "Grammar")
        ), pronunciation = "Practise the specific-question intonation with 你在找什么？ and the falling statement intonation in 手机在桌子上呢。"
    )

    LessonContent(
        number = 11, title = "He is three years older than me", hanziTitle = "他比我大三岁",
        textbookPages = "PDF pages 99–106", workbookPages = "PDF pages 109–118",
        dialogues = listOf(
            Dialogue("1 · At a karaoke bar", "Textbook 11-1", listOf(
                DialogueLine("王芳，昨天和你一起唱歌的人是谁？", "Wáng Fāng, zuótiān hé nǐ yìqǐ chànggē de rén shì shéi?", "Wang Fang, who was the person singing with you yesterday?"),
                DialogueLine("一个朋友。", "Yí ge péngyou.", "A friend."),
                DialogueLine("什么朋友？是不是男朋友？", "Shénme péngyou? Shì bu shì nán péngyou?", "What kind of friend? Is he your boyfriend?"),
                DialogueLine("不是不是，我同学介绍的，昨天第一次见。", "Bú shì bú shì, wǒ tóngxué jièshào de, zuótiān dì-yī cì jiàn.", "No, no. A classmate introduced us; we met for the first time yesterday.")
            )),
            Dialogue("2 · In the dormitory", "Textbook 11-2", listOf(
                DialogueLine("左边这个看报纸的女孩子是你姐姐吗？", "Zuǒbian zhège kàn bàozhǐ de nǚháizi shì nǐ jiějie ma?", "Is the girl on the left reading a newspaper your older sister?"),
                DialogueLine("是，右边写字的那个人是我哥哥。", "Shì, yòubian xiězì de nà ge rén shì wǒ gēge.", "Yes. The person on the right writing is my older brother."),
                DialogueLine("你哥哥多大？", "Nǐ gēge duō dà?", "How old is your brother?"),
                DialogueLine("二十五岁，他比我大三岁。", "Èrshíwǔ suì, tā bǐ wǒ dà sān suì.", "He is 25; he is three years older than me.")
            ))
        ),
        words = listOf(
            Word("唱歌", "chànggē", "to sing", "昨天我们一起唱歌。", "Zuótiān wǒmen yìqǐ chànggē.", "We sang together yesterday."),
            Word("男", "nán", "male; man", "他是男孩子。", "Tā shì nán háizi.", "He is a boy."),
            Word("女", "nǚ", "female; woman", "她是女孩子。", "Tā shì nǚ háizi.", "She is a girl."),
            Word("孩子", "háizi", "child", "那个孩子很可爱。", "Nà ge háizi hěn kě'ài.", "That child is cute."),
            Word("左边", "zuǒbian", "left side", "我坐在左边。", "Wǒ zuò zài zuǒbian.", "I sit on the left."),
            Word("右边", "yòubian", "right side", "他在右边。", "Tā zài yòubian.", "He is on the right."),
            Word("比", "bǐ", "than; compared with", "他比我高。", "Tā bǐ wǒ gāo.", "He is taller than me."),
            Word("岁", "suì", "years old", "我二十岁。", "Wǒ èrshí suì.", "I am twenty years old."),
            Word("介绍", "jièshào", "to introduce", "同学介绍我们认识。", "Tóngxué jièshào wǒmen rènshi.", "A classmate introduced us."),
            Word("第一次", "dì-yī cì", "the first time", "这是我第一次来。", "Zhè shì wǒ dì-yī cì lái.", "This is my first time here.")
        ),
        grammar = listOf(
            GrammarPoint("比 · comparisons", "比 introduces the standard of comparison. The adjective follows the second noun; do not add 很 before it.", "A + 比 + B + adjective (+ difference)", listOf(DialogueLine("他比我大三岁。", "Tā bǐ wǒ dà sān suì.", "He is three years older than me."))),
            GrammarPoint("的 · verb phrase as modifier", "A verb phrase can describe a noun. Put 的 between the describing phrase and the noun.", "Verb phrase + 的 + noun", listOf(DialogueLine("看报纸的女孩子", "Kàn bàozhǐ de nǚháizi", "the girl who is reading a newspaper")))
        ),
        questions = listOf(
            QuizQuestion("How did Wang Fang meet the friend?", listOf("At work", "A classmate introduced them", "At school years ago", "Online"), 1, "我同学介绍的。", "Textbook 11-1"),
            QuizQuestion("Was it the first time they met?", listOf("Yes", "No, many times", "They are relatives", "Not stated"), 0, "昨天第一次见。", "Textbook 11-1"),
            QuizQuestion("Who is reading a newspaper?", listOf("The older brother", "The girl on the left", "The speaker", "A teacher"), 1, "左边这个看报纸的女孩子。", "Textbook 11-2"),
            QuizQuestion("How old is the older brother?", listOf("22", "23", "25", "28"), 2, "二十五岁。", "Textbook 11-2"),
            QuizQuestion("What does 比 do in a sentence?", listOf("Shows possession", "Makes a comparison", "Marks past experience", "Shows location"), 1, "比 introduces a comparison.", "Grammar"),
            QuizQuestion("他比我大三岁 means…", listOf("I am three years older", "He is three years older", "We are both three", "He is three years younger"), 1, "大三岁 = three years older.", "Grammar"),
            QuizQuestion("Choose the correct phrase for 'the person writing'.", listOf("写字的人", "人写字", "写的人字", "字写人"), 0, "Verb phrase + 的 + noun.", "Grammar"),
            QuizQuestion("第一次 means…", listOf("Every time", "The first time", "Last time", "Next time"), 1, "第一次 means the first time.", "Workbook lesson 11 review")
        ), pronunciation = "Practise the comparison sentence as one unit: tā bǐ wǒ dà sān suì. Keep 比 clear and unstressed."
    ),
    LessonContent(
        number = 12, title = "You wear too little", hanziTitle = "你穿得太少了",
        textbookPages = "PDF pages 107–114", workbookPages = "PDF pages 119–128",
        dialogues = listOf(
            Dialogue("1 · In the classroom", "Textbook 12-1", listOf(
                DialogueLine("你每天早上几点起床？", "Nǐ měitiān zǎoshang jǐ diǎn qǐchuáng?", "What time do you get up every morning?"),
                DialogueLine("六点多。", "Liù diǎn duō.", "A little after six."),
                DialogueLine("你比我早起一个小时。", "Nǐ bǐ wǒ zǎo qǐ yí ge xiǎoshí.", "You get up one hour earlier than me."),
                DialogueLine("我睡得也早，我每天晚上十点就睡觉。", "Wǒ shuì de yě zǎo, wǒ měitiān wǎnshang shí diǎn jiù shuìjiào.", "I also go to bed early; I go to sleep at ten every night.")
            )),
            Dialogue("2 · At a friend's home", "Textbook 12-2", listOf(
                DialogueLine("再来点儿米饭吧，你吃得太少了。", "Zài lái diǎnr mǐfàn ba, nǐ chī de tài shǎo le.", "Have some more rice; you have eaten too little."),
                DialogueLine("不少了，今天吃得很好，太谢谢你了。", "Bù shǎo le, jīntiān chī de hěn hǎo, tài xièxie nǐ le.", "That's enough. I've eaten very well today; thank you."),
                DialogueLine("你做饭做得怎么样？", "Nǐ zuòfàn zuò de zěnmeyàng?", "How well do you cook?"),
                DialogueLine("不怎么样，我妻子比我做得好。", "Bù zěnmeyàng, wǒ qīzi bǐ wǒ zuò de hǎo.", "Not very well. My wife cooks better than I do.")
            ))
        ),
        words = listOf(
            Word("得", "de", "structural particle introducing a complement", "他说得很好。", "Tā shuō de hěn hǎo.", "He speaks very well."),
            Word("早", "zǎo", "early", "我今天起得很早。", "Wǒ jīntiān qǐ de hěn zǎo.", "I got up early today."),
            Word("起床", "qǐchuáng", "to get up", "我六点起床。", "Wǒ liù diǎn qǐchuáng.", "I get up at six."),
            Word("小时", "xiǎoshí", "hour", "我学习两个小时。", "Wǒ xuéxí liǎng ge xiǎoshí.", "I study for two hours."),
            Word("睡觉", "shuìjiào", "to sleep; go to bed", "我十点睡觉。", "Wǒ shí diǎn shuìjiào.", "I go to bed at ten."),
            Word("健康", "jiànkāng", "healthy; health", "早睡早起很健康。", "Zǎo shuì zǎo qǐ hěn jiànkāng.", "Going to bed and getting up early is healthy."),
            Word("再", "zài", "again; another", "请再说一次。", "Qǐng zài shuō yí cì.", "Please say it once more."),
            Word("米饭", "mǐfàn", "cooked rice", "我喜欢吃米饭。", "Wǒ xǐhuan chī mǐfàn.", "I like eating rice."),
            Word("太", "tài", "too; excessively", "今天太冷了。", "Jīntiān tài lěng le.", "It is too cold today."),
            Word("妻子", "qīzi", "wife", "他的妻子是老师。", "Tā de qīzi shì lǎoshī.", "His wife is a teacher.")
        ),
        grammar = listOf(
            GrammarPoint("得 · complement of degree or manner", "得 follows a verb and introduces information about how the action is performed. With an object, repeat the verb or place the object before the verb.", "Verb + 得 + adjective / phrase", listOf(DialogueLine("他说得很好。", "Tā shuō de hěn hǎo.", "He speaks very well."))),
            GrammarPoint("太……了 · too / very", "太 before an adjective and 了 at the end expresses a high degree, often 'too'. Context determines whether it is criticism or emphasis.", "太 + adjective + 了", listOf(DialogueLine("你吃得太少了。", "Nǐ chī de tài shǎo le.", "You have eaten too little.")))
        ),
        questions = listOf(
            QuizQuestion("What time does the speaker get up?", listOf("5:00", "A little after 6:00", "7:30", "8:00"), 1, "六点多。", "Textbook 12-1"),
            QuizQuestion("How much earlier does the other person get up?", listOf("30 minutes", "One hour", "Two hours", "Three hours"), 1, "早起一个小时。", "Textbook 12-1"),
            QuizQuestion("What time does the speaker go to bed?", listOf("9 pm", "10 pm", "11 pm", "Midnight"), 1, "每天晚上十点就睡觉。", "Textbook 12-1"),
            QuizQuestion("What does the host say about the guest's meal?", listOf("You ate too much", "You ate too little", "The food is cold", "There is no rice"), 1, "你吃得太少了。", "Textbook 12-2"),
            QuizQuestion("Who cooks better?", listOf("The speaker", "The speaker's wife", "The speaker's mother", "A friend"), 1, "我妻子比我做得好。", "Textbook 12-2"),
            QuizQuestion("What does 得 introduce after a verb?", listOf("A manner or degree complement", "A location", "A future time", "A plural noun"), 0, "得 introduces a complement describing the action.", "Grammar"),
            QuizQuestion("太少了 means…", listOf("A little more", "Too little", "Very early", "Not enough time"), 1, "太 + adjective + 了 expresses a high degree.", "Grammar"),
            QuizQuestion("小时 means…", listOf("Minute", "Day", "Hour", "Week"), 2, "小时 is an hour.", "Workbook lesson 12 review")
        ), pronunciation = "Practise 得 as a light structural syllable: chī-de tài shǎo-le. Do not give 得 a full fourth tone."
    ),
    LessonContent(
        number = 13, title = "The door is open", hanziTitle = "门开着呢",
        textbookPages = "PDF pages 115–122", workbookPages = "PDF pages 129–138",
        dialogues = listOf(
            Dialogue("1 · In the office", "Textbook 13-1", listOf(
                DialogueLine("门开着呢，请进。", "Mén kāi zhe ne, qǐng jìn.", "The door is open. Please come in."),
                DialogueLine("请问，张先生在吗？", "Qǐngwèn, Zhāng xiānsheng zài ma?", "Excuse me, is Mr Zhang here?"),
                DialogueLine("他出去了。你下午再来吧。", "Tā chūqu le. Nǐ xiàwǔ zài lái ba.", "He has gone out. Please come again this afternoon."),
                DialogueLine("好的，谢谢！", "Hǎo de, xièxie!", "All right, thank you!")
            )),
            Dialogue("2 · On the playground", "Textbook 13-2", listOf(
                DialogueLine("听说你有女朋友了？我认识她吗？", "Tīngshuō nǐ yǒu nǚpéngyou le? Wǒ rènshi tā ma?", "I heard you have a girlfriend. Do I know her?"),
                DialogueLine("就是我们班那个长着两只大眼睛、非常爱笑的女孩儿。", "Jiù shì wǒmen bān nà ge zhǎng zhe liǎng zhī dà yǎnjing, fēicháng ài xiào de nǚháir.", "She is the girl in our class with big eyes who smiles a lot."),
                DialogueLine("她不是有男朋友吗？", "Tā bú shì yǒu nánpéngyou ma?", "Didn't she already have a boyfriend?"),
                DialogueLine("那个已经是她的前男友了。", "Nà ge yǐjīng shì tā de qián nányǒu le.", "That person is already her ex-boyfriend.")
            ))
        ),
        words = listOf(
            Word("着", "zhe", "particle marking a continuing state", "门开着。", "Mén kāi zhe.", "The door is open."),
            Word("开", "kāi", "to open; be open", "窗户开着。", "Chuānghu kāi zhe.", "The window is open."),
            Word("关", "guān", "to close; shut", "请关门。", "Qǐng guān mén.", "Please close the door."),
            Word("请进", "qǐng jìn", "please come in", "门开着，请进。", "Mén kāi zhe, qǐng jìn.", "The door is open; please come in."),
            Word("出去", "chūqu", "to go out", "他出去了。", "Tā chūqu le.", "He has gone out."),
            Word("再", "zài", "again; another time", "下午再来。", "Xiàwǔ zài lái.", "Come again this afternoon."),
            Word("听说", "tīngshuō", "to hear that; it is said", "听说他今天来。", "Tīngshuō tā jīntiān lái.", "I heard he is coming today."),
            Word("长", "zhǎng", "to grow; have (physical feature)", "她长着大眼睛。", "Tā zhǎng zhe dà yǎnjing.", "She has big eyes."),
            Word("眼睛", "yǎnjing", "eye(s)", "她的眼睛很大。", "Tā de yǎnjing hěn dà.", "Her eyes are big."),
            Word("前男友", "qián nányǒu", "ex-boyfriend", "他是她的前男友。", "Tā shì tā de qián nányǒu.", "He is her ex-boyfriend.")
        ),
        grammar = listOf(
            GrammarPoint("着 · continuing state", "着 follows a verb to describe a state that continues, such as a door remaining open. To negate this state, use 没有 before the verb.", "Verb + 着 (+ object)", listOf(DialogueLine("门开着呢。", "Mén kāi zhe ne.", "The door is open."))),
            GrammarPoint("不是……吗 · rhetorical reminder", "不是……吗 can remind someone of information they are expected to know, or express surprise or confusion.", "不是 + clause + 吗？", listOf(DialogueLine("她不是有男朋友吗？", "Tā bú shì yǒu nánpéngyou ma?", "Didn't she have a boyfriend?"))),
            GrammarPoint("往 · direction", "往 indicates the direction in which someone moves. It is commonly followed by a direction word.", "往 + direction + verb", listOf(DialogueLine("往前走。", "Wǎng qián zǒu.", "Walk straight ahead.")))
        ),
        questions = listOf(
            QuizQuestion("What does 门开着呢 mean?", listOf("The door is opening", "The door is open", "The door is closed", "The door is broken"), 1, "开着 describes the continuing open state.", "Textbook 13-1"),
            QuizQuestion("Where is Mr Zhang?", listOf("At home", "He has gone out", "In the classroom", "At the station"), 1, "他出去了。", "Textbook 13-1"),
            QuizQuestion("When should the visitor come back?", listOf("This morning", "This afternoon", "Tomorrow", "Next week"), 1, "你下午再来吧。", "Textbook 13-1"),
            QuizQuestion("What physical feature is mentioned about the girl?", listOf("Long hair", "Big eyes", "Tall height", "Small hands"), 1, "长着两只大眼睛。", "Textbook 13-2"),
            QuizQuestion("着 after a verb indicates…", listOf("A continuing state", "A completed past event", "A comparison", "A question"), 0, "Verb + 着 marks a continuing state.", "Grammar"),
            QuizQuestion("听说 means…", listOf("To tell someone", "To hear that", "To listen carefully", "To ask"), 1, "听说 introduces something heard or reported.", "Vocabulary"),
            QuizQuestion("往前走 means…", listOf("Walk backwards", "Walk straight ahead", "Turn left", "Stop walking"), 1, "往 + 前 + 走 indicates forward direction.", "Grammar"),
            QuizQuestion("再来 means…", listOf("Come again", "Come first", "Don't come", "Go outside"), 0, "再 means again or another time.", "Workbook lesson 13 review")
        ), pronunciation = "In 门开着呢，请进, keep 着 light and connect it smoothly to 呢. Practise the polite invitation 请进 with a gentle falling tone."
    ),
    LessonContent(
        number = 14, title = "Have you seen that movie?", hanziTitle = "你看过那个电影吗",
        textbookPages = "PDF pages 123–130", workbookPages = "PDF pages 139–148",
        dialogues = listOf(
            Dialogue("1 · In the classroom", "Textbook 14-1", listOf(
                DialogueLine("你看过那个电影没有？", "Nǐ kànguo nà ge diànyǐng méiyǒu?", "Have you seen that movie?"),
                DialogueLine("没看过，听说很有意思。", "Méi kànguo, tīngshuō hěn yǒu yìsi.", "I haven't seen it. I heard it is interesting."),
                DialogueLine("那我们下个星期一起去看吧？", "Nà wǒmen xià ge xīngqī yìqǐ qù kàn ba?", "Then let's go and see it together next week."),
                DialogueLine("可以，但是我女朋友也想去。", "Kěyǐ, dànshì wǒ nǚpéngyou yě xiǎng qù.", "Sure, but my girlfriend also wants to go.")
            )),
            Dialogue("2 · In the office", "Textbook 14-2", listOf(
                DialogueLine("听说你去过中国，还想去吗？", "Tīngshuō nǐ qùguo Zhōngguó, hái xiǎng qù ma?", "I heard you have been to China. Would you like to go again?"),
                DialogueLine("我虽然去过好几次，但是还想再去玩儿。", "Wǒ suīrán qùguo hǎo jǐ cì, dànshì hái xiǎng zài qù wánr.", "Although I have been several times, I still want to go again for a visit."),
                DialogueLine("那我们一起去吧。", "Nà wǒmen yìqǐ qù ba.", "Then let's go together."),
                DialogueLine("好啊，到时候我给你打电话。", "Hǎo a, dào shíhou wǒ gěi nǐ dǎ diànhuà.", "Great. I'll call you then.")
            ))
        ),
        words = listOf(
            Word("有意思", "yǒu yìsi", "interesting; enjoyable", "这个电影很有意思。", "Zhège diànyǐng hěn yǒu yìsi.", "This movie is interesting."),
            Word("但是", "dànshì", "but; however", "我想去，但是今天很忙。", "Wǒ xiǎng qù, dànshì jīntiān hěn máng.", "I want to go, but I am busy today."),
            Word("虽然", "suīrán", "although", "虽然很冷，但是我想出去。", "Suīrán hěn lěng, dànshì wǒ xiǎng chūqu.", "Although it is cold, I want to go out."),
            Word("次", "cì", "time; occurrence (measure word)", "我去过两次。", "Wǒ qùguo liǎng cì.", "I have been twice."),
            Word("玩儿", "wánr", "to play; have fun", "周末我想出去玩儿。", "Zhōumò wǒ xiǎng chūqu wánr.", "I want to go out and have fun at the weekend."),
            Word("电影", "diànyǐng", "movie; film", "我们去看电影吧。", "Wǒmen qù kàn diànyǐng ba.", "Let's go to see a movie."),
            Word("电影院", "diànyǐngyuàn", "cinema", "电影院在学校旁边。", "Diànyǐngyuàn zài xuéxiào pángbiān.", "The cinema is beside the school."),
            Word("下个星期", "xià ge xīngqī", "next week", "下个星期我有时间。", "Xià ge xīngqī wǒ yǒu shíjiān.", "I have time next week."),
            Word("好几", "hǎo jǐ", "quite a few; several", "我看过好几次。", "Wǒ kànguo hǎo jǐ cì.", "I have seen it several times."),
            Word("打电话", "dǎ diànhuà", "to make a phone call", "我晚上给你打电话。", "Wǒ wǎnshang gěi nǐ dǎ diànhuà.", "I'll call you tonight.")
        ),
        grammar = listOf(
            GrammarPoint("过 · past experience", "过 after a verb indicates that someone has had an experience at some time in the past. The negative form is 没(有) + verb + 过.", "Verb + 过 (+ object)", listOf(DialogueLine("我去过中国。", "Wǒ qùguo Zhōngguó.", "I have been to China."))),
            GrammarPoint("虽然……但是…… · although…but", "Use 虽然 to introduce a concession and 但是 to introduce the contrasting main point. In casual speech, one may sometimes be omitted.", "虽然 + situation， 但是 + contrasting result", listOf(DialogueLine("虽然去过好几次，但是还想再去。", "Suīrán qùguo hǎo jǐ cì, dànshì hái xiǎng zài qù.", "Although I have been several times, I still want to go again."))),
            GrammarPoint("次 · number of occurrences", "次 is a measure word for how many times an action happens. It usually follows the verb; with a person object it follows the object.", "Verb + (object) + number + 次", listOf(DialogueLine("我去过两次。", "Wǒ qùguo liǎng cì.", "I have been twice.")))
        ),
        questions = listOf(
            QuizQuestion("Has the speaker seen the movie?", listOf("Yes", "No", "Twice", "Not sure"), 1, "没看过。", "Textbook 14-1"),
            QuizQuestion("What has the speaker heard about the movie?", listOf("It is too long", "It is interesting", "It is expensive", "It is old"), 1, "听说很有意思。", "Textbook 14-1"),
            QuizQuestion("When do they plan to see it?", listOf("Tomorrow", "Next week", "Next month", "Tonight"), 1, "下个星期一起去看。", "Textbook 14-1"),
            QuizQuestion("How many times has the traveller been to China?", listOf("Once", "Several times", "Never", "Ten times"), 1, "去过好几次。", "Textbook 14-2"),
            QuizQuestion("What does 过 after a verb express?", listOf("A past experience", "An action happening now", "A command", "A comparison"), 0, "Verb + 过 marks experience.", "Grammar"),
            QuizQuestion("虽然……但是…… expresses…", listOf("Cause and effect", "Although…but", "If…then", "Before and after"), 1, "It introduces a concession and contrast.", "Grammar"),
            QuizQuestion("次 is used to count…", listOf("People", "Occurrences of an action", "Objects by size", "Time duration only"), 1, "次 counts how many times an action happens.", "Grammar"),
            QuizQuestion("打电话 means…", listOf("To write a letter", "To make a phone call", "To watch a film", "To send a ticket"), 1, "打电话 means to make a phone call.", "Workbook lesson 14 review")
        ), pronunciation = "Practise the experience question: 你看过那个电影没有？ Keep 过 light. In 虽然……但是……, make the contrast easy to hear."
    ),
    LessonContent(
        number = 15, title = "The New Year is coming", hanziTitle = "新年就要到了",
        textbookPages = "PDF pages 131–138", workbookPages = "PDF pages 149–158",
        dialogues = listOf(
            Dialogue("1 · At a friend's home", "Textbook 15-1", listOf(
                DialogueLine("今天是十二月二十日，新年就要到了。", "Jīntiān shì shí'èr yuè èrshí rì, xīnnián jiù yào dào le.", "Today is December 20th. The New Year is coming soon."),
                DialogueLine("新年你准备做什么？", "Xīnnián nǐ zhǔnbèi zuò shénme?", "What are you planning to do for the New Year?"),
                DialogueLine("我想去北京旅游，北京很不错，我去过一次。", "Wǒ xiǎng qù Běijīng lǚyóu, Běijīng hěn búcuò, wǒ qùguo yí cì.", "I want to travel to Beijing. It is a very nice place; I have been once."),
                DialogueLine("你买票了吗？", "Nǐ mǎi piào le ma?", "Have you bought a ticket?"),
                DialogueLine("还没有呢，明天就去火车站买票。", "Hái méiyǒu ne, míngtiān jiù qù huǒchēzhàn mǎi piào.", "Not yet. I'll go to the railway station to buy one tomorrow.")
            )),
            Dialogue("2 · At the company", "Textbook 15-2", listOf(
                DialogueLine("时间过得真快，新的一年快要到了！", "Shíjiān guò de zhēn kuài, xīn de yì nián kuài yào dào le!", "Time passes so quickly. The new year is almost here!"),
                DialogueLine("是啊，谢谢大家这一年对我的帮助！", "Shì a, xièxie dàjiā zhè yì nián duì wǒ de bāngzhù!", "Yes. Thank you all for helping me this year!"),
                DialogueLine("希望我们的公司明年更好！", "Xīwàng wǒmen de gōngsī míngnián gèng hǎo!", "I hope our company will be even better next year!")
            ))
        ),
        words = listOf(
            Word("日", "rì", "day; date (formal)", "今天是十二月二十日。", "Jīntiān shì shí'èr yuè èrshí rì.", "Today is December 20th."),
            Word("站", "zhàn", "station; stop", "我去火车站。", "Wǒ qù huǒchēzhàn.", "I am going to the railway station."),
            Word("火车站", "huǒchēzhàn", "railway station", "火车站在前面。", "Huǒchēzhàn zài qiánmiàn.", "The railway station is ahead."),
            Word("票", "piào", "ticket", "我买了两张票。", "Wǒ mǎi le liǎng zhāng piào.", "I bought two tickets."),
            Word("准备", "zhǔnbèi", "to prepare; plan", "你准备做什么？", "Nǐ zhǔnbèi zuò shénme?", "What are you planning to do?"),
            Word("就", "jiù", "then; soon; as early as", "新年就要到了。", "Xīnnián jiù yào dào le.", "The New Year is coming soon."),
            Word("快要", "kuài yào", "be about to; soon", "火车快要开了。", "Huǒchē kuài yào kāi le.", "The train is about to leave."),
            Word("大家", "dàjiā", "everyone", "谢谢大家。", "Xièxie dàjiā.", "Thank you, everyone."),
            Word("更", "gèng", "even more; to a greater extent", "明年会更好。", "Míngnián huì gèng hǎo.", "Next year will be even better."),
            Word("希望", "xīwàng", "to hope; hope", "我希望你快乐。", "Wǒ xīwàng nǐ kuàilè.", "I hope you are happy.")
        ),
        grammar = listOf(
            GrammarPoint("快要 / 就要……了 · be about to", "快要 and 就要 before a verb indicate that something will happen soon. 了 at the end marks the approaching change.", "Subject + 快要 / 就要 + verb + 了", listOf(DialogueLine("新年就要到了。", "Xīnnián jiù yào dào le.", "The New Year is coming soon."))),
            GrammarPoint("更 · even more", "更 is placed before an adjective or verb phrase to indicate a greater degree or an additional increase.", "更 + adjective / verb phrase", listOf(DialogueLine("希望我们的公司明年更好。", "Xīwàng wǒmen de gōngsī míngnián gèng hǎo.", "I hope our company will be even better next year."))),
            GrammarPoint("对……的帮助 · help given to someone", "对 identifies the person or thing receiving the benefit; 帮助 means help or assistance.", "对 + person / thing + 的帮助", listOf(DialogueLine("谢谢大家这一年对我的帮助。", "Xièxie dàjiā zhè yì nián duì wǒ de bāngzhù.", "Thank you all for your help this year.")))
        ),
        questions = listOf(
            QuizQuestion("What date is mentioned?", listOf("December 10", "December 20", "January 1", "January 20"), 1, "今天是十二月二十日。", "Textbook 15-1"),
            QuizQuestion("What does the speaker plan to do for the New Year?", listOf("Stay at home", "Travel to Beijing", "Visit Shanghai", "Work at the company"), 1, "我想去北京旅游。", "Textbook 15-1"),
            QuizQuestion("Has the speaker been to Beijing before?", listOf("Never", "Once", "Twice", "Many times"), 1, "我去过一次。", "Textbook 15-1"),
            QuizQuestion("Where will the speaker buy the ticket?", listOf("At the airport", "At the railway station", "At home", "At school"), 1, "明天就去火车站买票。", "Textbook 15-1"),
            QuizQuestion("快要……了 indicates…", listOf("Something happened long ago", "Something is about to happen", "A repeated habit", "A comparison"), 1, "快要 / 就要……了 marks an imminent event.", "Grammar"),
            QuizQuestion("更好 means…", listOf("Not good", "Even better", "The best yesterday", "A little worse"), 1, "更 increases the degree: even better.", "Grammar"),
            QuizQuestion("大家 means…", listOf("Only me", "Everyone", "A company", "A station"), 1, "大家 means everyone.", "Vocabulary"),
            QuizQuestion("希望 means…", listOf("To prepare", "To hope", "To arrive", "To buy"), 1, "希望 means to hope or wish.", "Workbook lesson 15 review")
        ),
        pronunciation = "Practise the approaching-event pattern with a smooth rhythm: xīnnián jiù yào dào le. Keep 就要 connected and 了 light."
    )
)

@Composable
private fun MandarinApp(speak: (String) -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember { context.getSharedPreferences("mandarin_progress", android.content.Context.MODE_PRIVATE) }
    var selectedLesson by remember { mutableStateOf<LessonContent?>(null) }
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var rootPage by rememberSaveable { mutableStateOf("Course") }
    var bestScores by remember { mutableStateOf((1..15).associateWith { prefs.getInt("best_$it", -1) }) }
    var knownWords by remember { mutableStateOf(prefs.getStringSet("known_words", emptySet())?.toSet() ?: emptySet()) }

    MaterialTheme(colorScheme = lightColorScheme(primary = Teal, secondary = Ink, background = Pale)) {
        Scaffold(
            containerColor = Pale,
            bottomBar = {
                if (selectedLesson == null) {
                    NavigationBar(containerColor = Color.White) {
                        listOf("Course", "Progress").forEach { page ->
                            NavigationBarItem(
                                selected = rootPage == page,
                                onClick = { rootPage = page },
                                icon = { Text(if (page == "Course") "书" else "✓", fontSize = 18.sp) },
                                label = { Text(page) }
                            )
                        }
                    }
                }
            }
        ) { padding ->
            val openLesson: (LessonContent) -> Unit = { lesson -> selectedLesson = lesson; selectedTab = 0 }
            if (selectedLesson != null) {
                LessonScreen(
                    modifier = Modifier.padding(padding),
                    lesson = selectedLesson!!,
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    onBack = { selectedLesson = null },
                    speak = speak,
                    knownWords = knownWords,
                    onToggleKnown = { word ->
                        knownWords = if (knownWords.contains(word)) knownWords - word else knownWords + word
                        prefs.edit().putStringSet("known_words", knownWords).apply()
                    },
                    onQuizFinished = { lessonNumber, score ->
                        val old = bestScores[lessonNumber] ?: -1
                        if (score > old) {
                            bestScores = bestScores + (lessonNumber to score)
                            prefs.edit().putInt("best_$lessonNumber", score).apply()
                        }
                    }
                )
            } else if (rootPage == "Progress") {
                ProgressScreen(Modifier.padding(padding), bestScores, knownWords.size, onOpenLesson = openLesson)
            } else {
                CourseScreen(Modifier.padding(padding), bestScores, openLesson)
            }
        }
    }
}

@Composable
private fun CourseScreen(modifier: Modifier, bestScores: Map<Int, Int>, openLesson: (LessonContent) -> Unit) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 18.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("MANDARIN COMPANION", color = Teal, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.5.sp)
            Text("你好！", color = Ink, fontSize = 36.sp, fontWeight = FontWeight.Bold)
            Text("HSK 2 · Lessons 1–15", color = Ink, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Text("Based on your Standard Course 2 textbook and workbook scans.", color = Ink.copy(alpha = 0.72f), fontSize = 14.sp)
        }
        item { InfoPanel("Study offline", "All lesson text, vocabulary, grammar and quizzes are stored on this phone. Mandarin pronunciation uses Android Text-to-Speech; the publisher's audio files were not included in the PDFs.") }
        items(lessons) { lesson ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable { openLesson(lesson) },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("LESSON ${lesson.number}", color = Teal, fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 1.sp, modifier = Modifier.weight(1f))
                        val score = bestScores[lesson.number] ?: -1
                        if (score >= 0) Text("Best score: $score/${lesson.questions.size}", color = Good, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Text(lesson.title, color = Ink, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(lesson.hanziTitle, color = Teal, fontSize = 20.sp, fontWeight = FontWeight.Medium)
                    Text("${lesson.dialogues.size} dialogues · ${lesson.words.size} words · ${lesson.grammar.size} grammar points · ${lesson.questions.size} workbook questions", color = Color.Gray, fontSize = 12.sp)
                    Text("Textbook ${lesson.textbookPages}  ·  Workbook ${lesson.workbookPages}", color = Color.Gray, fontSize = 11.sp)
                    Button(onClick = { openLesson(lesson) }, modifier = Modifier.fillMaxWidth()) { Text("Open lesson") }
                }
            }
        }
        item {
            Text("Learning focus: character recognition, vocabulary, pinyin, grammar, listening and speaking. No handwriting or stroke-order drills.", color = Ink.copy(alpha = 0.75f), fontSize = 13.sp)
        }
    }
}

@Composable
private fun LessonScreen(
    modifier: Modifier,
    lesson: LessonContent,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onBack: () -> Unit,
    speak: (String) -> Unit,
    knownWords: Set<String>,
    onToggleKnown: (String) -> Unit,
    onQuizFinished: (Int, Int) -> Unit
) {
    val tabs = listOf("Dialogues", "Vocabulary", "Grammar", "Workbook")
    Column(modifier.fillMaxSize()) {
        Column(Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 6.dp)) {
            TextButton(onClick = onBack, contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)) { Text("← All lessons", color = Teal) }
            Text("HSK 2  /  LESSON ${lesson.number}", color = Teal, fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 1.sp)
            Text(lesson.title, color = Ink, fontWeight = FontWeight.Bold, fontSize = 21.sp)
            Text(lesson.hanziTitle, color = Teal, fontSize = 20.sp)
            Text("Textbook ${lesson.textbookPages} · Workbook ${lesson.workbookPages}", color = Color.Gray, fontSize = 10.sp)
        }
        ScrollableTabRow(selectedTabIndex = selectedTab, edgePadding = 8.dp, containerColor = Pale) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedTab == index, onClick = { onTabSelected(index) }, text = { Text(title) })
            }
        }
        when (selectedTab) {
            0 -> DialoguesTab(Modifier.weight(1f), lesson, speak)
            1 -> VocabularyTab(Modifier.weight(1f), lesson, speak, knownWords, onToggleKnown)
            2 -> GrammarTab(Modifier.weight(1f), lesson, speak)
            else -> WorkbookTab(Modifier.weight(1f), lesson, speak, onQuizFinished)
        }
    }
}

@Composable
private fun DialoguesTab(modifier: Modifier, lesson: LessonContent, speak: (String) -> Unit) {
    LazyColumn(modifier.fillMaxSize().padding(horizontal = 14.dp), contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { InfoPanel("Listen and repeat", "Read the Chinese line first, check pinyin and meaning, then tap Listen. Repeat the line aloud. The voice is generated by Android TTS, not the publisher's recording.") }
        items(lesson.dialogues) { dialogue ->
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                    Text(dialogue.title, color = Ink, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text(dialogue.scene, color = Teal, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    dialogue.lines.forEachIndexed { index, line ->
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(line.chinese, color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                            Text(line.pinyin, color = Teal, fontSize = 13.sp)
                            Text(line.english, color = Ink.copy(alpha = 0.78f), fontSize = 13.sp)
                            TextButton(onClick = { speak(line.chinese) }, contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)) { Text("▶ Listen to line ${index + 1}") }
                        }
                        if (index != dialogue.lines.lastIndex) HorizontalDivider(color = Pale)
                    }
                }
            }
        }
        item {
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Pronunciation focus", color = Ink, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text(lesson.pronunciation, color = Ink.copy(alpha = 0.8f), fontSize = 13.sp)
                    TextButton(onClick = { speak(lesson.hanziTitle) }, contentPadding = PaddingValues(horizontal = 0.dp)) { Text("▶ Listen to lesson title") }
                }
            }
        }
        item { SourceNote(lesson) }
    }
}

@Composable
private fun VocabularyTab(
    modifier: Modifier,
    lesson: LessonContent,
    speak: (String) -> Unit,
    knownWords: Set<String>,
    onToggleKnown: (String) -> Unit
) {
    LazyColumn(modifier.fillMaxSize().padding(horizontal = 14.dp), contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
        item { InfoPanel("Textbook new words", "Mark a word as known when you can recognise it and understand it. Tap Listen to hear the word and its example.") }
        items(lesson.words) { word ->
            Card(shape = RoundedCornerShape(15.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(word.hanzi, color = Ink, fontWeight = FontWeight.Bold, fontSize = 26.sp, modifier = Modifier.weight(1f))
                        TextButton(onClick = { speak(word.hanzi) }) { Text("▶ Listen") }
                    }
                    Text(word.pinyin, color = Teal, fontSize = 14.sp)
                    Text(word.meaning, color = Ink.copy(alpha = 0.8f), fontSize = 14.sp)
                    HorizontalDivider(color = Pale)
                    Text(word.example, color = Ink, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                    Text(word.examplePinyin, color = Teal, fontSize = 12.sp)
                    Text(word.exampleEnglish, color = Color.Gray, fontSize = 12.sp)
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        TextButton(onClick = { speak(word.example) }, contentPadding = PaddingValues(horizontal = 0.dp)) { Text("▶ Example") }
                        Spacer(Modifier.weight(1f))
                        Text(if (knownWords.contains(word.hanzi)) "Known ✓" else "Mark known", color = if (knownWords.contains(word.hanzi)) Good else Teal, fontSize = 12.sp, modifier = Modifier.clickable { onToggleKnown(word.hanzi) }.padding(8.dp))
                    }
                }
            }
        }
        item { SourceNote(lesson) }
    }
}

@Composable
private fun GrammarTab(modifier: Modifier, lesson: LessonContent, speak: (String) -> Unit) {
    LazyColumn(modifier.fillMaxSize().padding(horizontal = 14.dp), contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { InfoPanel("Grammar from the textbook", "Each point includes a short explanation, a pattern and examples from the lesson or the textbook notes.") }
        items(lesson.grammar) { point ->
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(point.title, color = Ink, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text(point.explanation, color = Ink.copy(alpha = 0.82f), fontSize = 13.sp)
                    Surface(color = SoftTeal, shape = RoundedCornerShape(10.dp)) {
                        Text(point.pattern, color = Ink, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(10.dp), fontSize = 13.sp)
                    }
                    point.examples.forEach { example ->
                        Text(example.chinese, color = Ink, fontWeight = FontWeight.Medium, fontSize = 17.sp)
                        Text(example.pinyin, color = Teal, fontSize = 12.sp)
                        Text(example.english, color = Color.Gray, fontSize = 12.sp)
                        TextButton(onClick = { speak(example.chinese) }, contentPadding = PaddingValues(horizontal = 0.dp)) { Text("▶ Listen") }
                    }
                }
            }
        }
        item { SourceNote(lesson) }
    }
}

@Composable
private fun WorkbookTab(modifier: Modifier, lesson: LessonContent, speak: (String) -> Unit, onFinished: (Int, Int) -> Unit) {
    var index by remember(lesson.number) { mutableIntStateOf(0) }
    var selected by remember(lesson.number) { mutableIntStateOf(-1) }
    var score by remember(lesson.number) { mutableIntStateOf(0) }
    var saved by remember(lesson.number) { mutableStateOf(false) }
    val finished = index >= lesson.questions.size

    LazyColumn(modifier.fillMaxSize().padding(horizontal = 14.dp), contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { InfoPanel("Workbook practice · adapted", "These questions are adapted from the lesson content and exercise formats in your workbook: listening-style choices, comprehension, matching, fill-in and true/false. Publisher audio is not included; use the Listen button where shown.") }
        if (!finished) {
            val question = lesson.questions[index]
            item {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text("Question ${index + 1} of ${lesson.questions.size}", color = Teal, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text("Score: $score", color = Ink, fontWeight = FontWeight.Bold)
                }
                LinearProgressIndicator(progress = { (index + 1).toFloat() / lesson.questions.size.toFloat() }, modifier = Modifier.fillMaxWidth(), color = Teal, trackColor = SoftTeal)
            }
            item {
                Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(17.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(question.exerciseType.uppercase(), color = Teal, fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 1.sp)
                        Text(question.prompt, color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        if (question.prompt.any { it in '\u4e00'..'\u9fff' }) {
                            TextButton(onClick = { speak(question.prompt) }, contentPadding = PaddingValues(horizontal = 0.dp)) { Text("▶ Listen to question") }
                        }
                        question.options.forEachIndexed { optionIndex, option ->
                            val optionColor = when {
                                selected == -1 -> Color.White
                                optionIndex == question.answer -> SoftTeal
                                optionIndex == selected -> Color(0xFFF9E8E8)
                                else -> Color.White
                            }
                            Card(
                                modifier = Modifier.fillMaxWidth().clickable(enabled = selected == -1) {
                                    selected = optionIndex
                                    if (optionIndex == question.answer) score++
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = optionColor)
                            ) {
                                Text(option, color = Ink, modifier = Modifier.padding(13.dp), fontSize = 14.sp)
                            }
                        }
                        if (selected != -1) {
                            Text(if (selected == question.answer) "Correct" else "Not quite", color = if (selected == question.answer) Good else Bad, fontWeight = FontWeight.Bold)
                            Text(question.explanation, color = Ink, fontSize = 13.sp)
                            Button(onClick = { index++; selected = -1 }, modifier = Modifier.fillMaxWidth()) { Text(if (index == lesson.questions.lastIndex) "See result" else "Next question") }
                        }
                    }
                }
            }
        } else {
            item {
                Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.fillMaxWidth().padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("练习完成", color = Teal, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        Text("$score / ${lesson.questions.size}", color = Ink, fontSize = 34.sp, fontWeight = FontWeight.Bold)
                        Text("${score * 100 / lesson.questions.size}% correct", color = Ink)
                        Text(if (score * 100 / lesson.questions.size >= 70) "Good result. Review any missed items, then say the dialogues aloud." else "Review the vocabulary and grammar, then try the workbook practice again.", color = Color.Gray, fontSize = 13.sp)
                        Button(onClick = { if (!saved) { onFinished(lesson.number, score); saved = true } }, enabled = !saved, modifier = Modifier.fillMaxWidth()) { Text(if (saved) "Progress saved" else "Save lesson progress") }
                        OutlinedButton(onClick = { index = 0; selected = -1; score = 0; saved = false }, modifier = Modifier.fillMaxWidth()) { Text("Try again") }
                    }
                }
            }
        }
        item { SourceNote(lesson) }
    }
}

@Composable
private fun ProgressScreen(modifier: Modifier, bestScores: Map<Int, Int>, knownCount: Int, onOpenLesson: (LessonContent) -> Unit) {
    LazyColumn(modifier.fillMaxSize().padding(horizontal = 18.dp), contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text("Your progress", color = Ink, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Scores are stored locally on this phone.", color = Color.Gray, fontSize = 13.sp)
        }
        item { MiniStat("Words marked known", knownCount.toString()) }
        items(lessons) { lesson ->
            val score = bestScores[lesson.number] ?: -1
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("Lesson ${lesson.number}", color = Teal, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        Text(lesson.title, color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text(if (score >= 0) "Best workbook score: $score/${lesson.questions.size}" else "Not completed yet", color = Color.Gray, fontSize = 12.sp)
                    }
                    TextButton(onClick = { onOpenLesson(lesson) }) { Text("Open") }
                }
            }
        }
        item { Text("No account, cloud sync or paid API is required. Android TTS availability depends on the Mandarin voice installed on the device.", color = Color.Gray, fontSize = 12.sp) }
    }
}

@Composable
private fun MiniStat(title: String, value: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(title, color = Color.Gray, modifier = Modifier.weight(1f), fontSize = 13.sp)
            Text(value, color = Ink, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        }
    }
}

@Composable
private fun InfoPanel(title: String, body: String) {
    Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = SoftBlue), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, color = Ink, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(body, color = Ink.copy(alpha = 0.82f), fontSize = 12.sp)
        }
    }
}

@Composable
private fun SourceNote(lesson: LessonContent) {
    Column(Modifier.fillMaxWidth().padding(top = 7.dp)) {
        HorizontalDivider(color = Color(0xFFD7E2E0))
        Spacer(Modifier.height(7.dp))
        Text("SOURCE CHECK", color = Teal, fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 1.sp)
        Text("Textbook ${lesson.textbookPages}; workbook ${lesson.workbookPages}. Dialogues, vocabulary and grammar are transcribed/adapted from the scanned lesson pages. Workbook practice questions are adapted to work offline. Original publisher audio and image-matching pages are not embedded. Handwriting/stroke-order exercises are omitted.", color = Color.Gray, fontSize = 10.sp)
    }
}
