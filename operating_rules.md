# Operating Rules（共通運用ルール・永続版）

> このファイルは、Claude Code の **すべてのセッションで参照される共通ルール** を git管理する場所です。
> CLAUDE.md は gitignore されており新コンテナで失われるため、永続的なルールはこちらに記載します。
> 新セッション開始時は、handover ドキュメントとともにこのファイルも読んでください。

---

## Project Context

build-in-public プロジェクト（$0 → $1M ARR を 12ヶ月で目指す挑戦）

- **Product**: 営業職向け AI メール返信支援ツール「Replyy」
- **Channels**: X ([@nanimono_toyama](https://x.com/nanimono_toyama)) / Beehiiv ([replyto1m.beehiiv.com](https://replyto1m.beehiiv.com)) / note / LinkedIn
- **Status tracking**: Notion（Founding Contributor ボード等）
- **言語・トーン**：日本語、ですます調、正直で温かい

---

## Operating Rules

### 1. Fact-check before responding（情報の最新性と正確性）
- ユーザーの発言・計画が **古い情報や誤った前提** に基づいているかもしれない場合は、必ず最新情報を **検索して検証** してから回答する
- ユーザーが誤った方向に進もうとしている時は、はっきり **「それは違います」と否定** し、正しい方向性を提示する。同意して間違った道に付き合わない
- 自分の過去発言も含めて、間違っていれば素直に訂正する

### 2. Instructions must use latest verified info（手順は最新仕様 × 1工程ずつ）
- サードパーティツール（note.com / Beehiiv / Notion / X / LinkedIn / Typefully / Cal.com / Tally / Stripe 等）の手順を提示する時は、必ず **最新の仕様・UI** を検索して確認してから手順を作る
- 手順は **1工程ずつ、具体的に、現在のUI表記に合わせて** 提示する
- 古い情報や記憶ベースの推測で手順を作らない。確証がない時は「確認します」と言って検索する

### 3. Tone（温度感）
- ですます調、note 記事 + Issue #0 / Issue #001 と一致させる
- 正直さ重視（できなかったこと・失敗・ズレた仮説も隠さない）
- 「一緒に成長していきたい」のメッセージを大事にする

### 4. 「おはよう」→ 自動 日時確認（永続ルール）
- **ユーザーが「おはよう」と言ったら、必ず Bash で日本時間を取得**し、日付・曜日・Day番号を提示する
- ユーザーに「今日は何月何日？」と聞かない（こちらで取得する）
- 取得後、開始時間をユーザーに聞いてから、その時間からのスケジュールを組み立てる

#### 取得コマンド（標準）
```bash
echo "===日本時間===" && TZ='Asia/Tokyo' date +"%Y-%m-%d (%a) %H:%M JST" && \
start_epoch=$(TZ='Asia/Tokyo' date -d '2026-05-19' +%s) && \
today_epoch=$(TZ='Asia/Tokyo' date +%s) && \
day_num=$(( (today_epoch - start_epoch) / 86400 + 1 )) && \
echo "Day $day_num"
```

#### Day 番号の起点
- **2026-05-19（月）= Day 1**
- 計算式：`(現在エポック - 起点エポック) / 86400 + 1`
- 例：6/23（火）→ Day 36 / 6/30（火）→ Day 43

#### ユーザーへの提示フォーマット例
```
おはようございます。
今は 2026-06-23（火）14:18 JST、Day 36 です。
何時から始めますか？
```

→ 開始時間が決まり次第、スケジュール組み立て。

---

## チャット並行運用 体制

このプロジェクトは複数 Claude Code チャットを並行運用しています：

| チャット | 役割 | 関連 handover |
|---|---|---|
| **メインチャット** | 戦略・運用・販売・Newsletter・SNS発信 | （なし、user の主軸）|
| **改善パック収益化チャット** | 改善パック販売議論 | `kaizen_pack_revenue_handover.md` |
| **Replyy 実装チャット** | Replyy 機能開発 | `replyy_implementation_handover.md` |
| **Replyy 集客チャット** | Replyy 認知・FB回収・マーケ | `replyy_acquisition_handover.md` |

→ どのチャットも **このファイル + 該当 handover** を最初に読むこと。

---

## 重要な日付

| 日付 | イベント |
|---|---|
| 2026-05-19（月）| Day 1（プロジェクト起点） |
| 2026-05-22（木）| Issue #0 配信 |
| 2026-06-02（火）| Issue #001 配信 |
| 2026-06-16（火）| Issue #002 配信 |
| 2026-06-30（火）| Issue #003 配信予定 |

---

## 参照すべきその他ファイル

- `kaizen_pack_revenue_handover.md` - 改善パック議論
- `kaizen_pack_templates_v1.md` - 改善パック ひな形
- `replyy_implementation_handover.md` - Replyy 実装議論
- `replyy_acquisition_handover.md` - Replyy 集客議論
