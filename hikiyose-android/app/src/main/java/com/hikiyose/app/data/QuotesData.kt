package com.hikiyose.app.data

import java.time.LocalDate

/** A bundled quote from a notable figure (偉人の言葉). Offline, no network needed. */
data class Quote(
    val text: String,
    val author: String,
)

/**
 * Bundled quotes shown on the home screen (wireframe ②: ニーチェなどの偉人の言葉).
 * The quote of the day is chosen deterministically from the date so it stays
 * stable for the whole day and rotates daily.
 */
object QuotesData {
    val quotes: List<Quote> = listOf(
        Quote("脱皮できない蛇は滅びる。", "ニーチェ"),
        Quote("これが人生か、ならばもう一度。", "ニーチェ"),
        Quote("夢を見ることができれば、それは実現できる。", "ウォルト・ディズニー"),
        Quote("成功とは、失敗を重ねても情熱を失わないことだ。", "ウィンストン・チャーチル"),
        Quote("未来を予測する最善の方法は、それを創り出すことだ。", "ピーター・ドラッカー"),
        Quote("我思う、ゆえに我あり。", "デカルト"),
        Quote("変化を恐れるな。最大の危険は変化しないことだ。", "ジョン・F・ケネディ"),
        Quote("天才とは1%のひらめきと99%の努力である。", "エジソン"),
        Quote("人生で最も大切なのは、転んでも必ず起き上がることだ。", "ネルソン・マンデラ"),
        Quote("今日できることを明日に延ばすな。", "ベンジャミン・フランクリン"),
        Quote("幸福は、それを分かち合うことで二倍になる。", "アルベルト・シュヴァイツァー"),
        Quote("もう一歩。いつもただもう一歩ずつ。", "アンデルセン"),
        Quote("思考は現実化する。", "ナポレオン・ヒル"),
        Quote("人は習慣によってつくられる。優れた人とは習慣が優れている人だ。", "アリストテレス"),
        Quote("やってみせ、言って聞かせて、させてみせ、ほめてやらねば人は動かじ。", "山本五十六"),
        Quote("迷ったときは、いつも難しい道を選べ。", "稲盛和夫"),
        Quote("成功している人は皆、楽天家である。", "本田宗一郎"),
        Quote("人生は10%が出来事で、90%がそれにどう反応するかだ。", "チャールズ・スウィンドル"),
        Quote("最も強い者が生き残るのではなく、変化に最もよく適応した者が生き残る。", "ダーウィン"),
        Quote("あなたが世界に望む変化に、あなた自身がなりなさい。", "ガンジー"),
        Quote("不可能とは、可能性を試さない者の言い訳にすぎない。", "モハメド・アリ"),
        Quote("人生における最大の栄光は決して転ばないことではなく、転ぶたびに起き上がることにある。", "ネルソン・マンデラ"),
        Quote("行動しなければ、何も変わらない。", "アインシュタイン"),
        Quote("自分を信じることから、すべては始まる。", "ゲーテ"),
        Quote("情熱なくして、偉大なことは成し遂げられない。", "ヘーゲル"),
        Quote("チャンスは、それを求める準備のできた者にだけ訪れる。", "パスツール"),
        Quote("今この瞬間に、最善を尽くせ。", "エマーソン"),
        Quote("笑顔は、お金をかけずにできる最高の贈り物だ。", "マザー・テレサ"),
        Quote("失敗とは、より賢く再挑戦するための好機である。", "ヘンリー・フォード"),
        Quote("小さなことを積み重ねることが、とんでもないところへ行くただ一つの道だ。", "イチロー"),
    )

    /** Returns the quote of the day for [date], stable within the day. */
    fun quoteOfDay(date: LocalDate = LocalDate.now()): Quote {
        val index = (date.toEpochDay() % quotes.size).toInt()
        return quotes[index]
    }
}
