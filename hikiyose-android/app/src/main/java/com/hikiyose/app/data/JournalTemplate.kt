package com.hikiyose.app.data

/**
 * A journaling template (書式) selectable on wireframe ④.
 * Each template provides a set of prompts that guide the entry, and a body
 * placeholder. Templates are bundled (no network) and identified by [id],
 * which is persisted on each [com.hikiyose.app.data.entity.JournalEntry].
 */
data class JournalTemplate(
    val id: String,
    val name: String,
    val description: String,
    val prompts: List<String>,
)

object JournalTemplates {
    val all: List<JournalTemplate> = listOf(
        JournalTemplate(
            id = "gratitude",
            name = "感謝ジャーナル",
            description = "今日感謝できることを書き出して、引き寄せ体質に。",
            prompts = listOf(
                "今日感謝できる3つのこと",
                "誰のおかげで今日を過ごせた？",
                "明日も続けたい良い習慣",
            ),
        ),
        JournalTemplate(
            id = "manifestation",
            name = "引き寄せノート",
            description = "願いを「すでに叶った前提」で記録する書式。",
            prompts = listOf(
                "叶えたい願い（現在形・完了形で）",
                "それが叶ったときの感情",
                "今日とった小さな一歩",
            ),
        ),
        JournalTemplate(
            id = "reflection",
            name = "今日のふりかえり",
            description = "出来事と気づきを整理してメンタルを整える。",
            prompts = listOf(
                "今日起きた出来事",
                "そこから得た気づき",
                "自分をほめたいこと",
            ),
        ),
        JournalTemplate(
            id = "free",
            name = "フリー",
            description = "見出しなし。自由に書きたいときに。",
            prompts = emptyList(),
        ),
    )

    val default: JournalTemplate get() = all.first()

    fun byId(id: String?): JournalTemplate =
        all.firstOrNull { it.id == id } ?: default
}
