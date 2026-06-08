# Replyy 実装 引き継ぎドキュメント

> このドキュメントは、Claude Code の新セッションが Replyy（営業向けAIメール返信支援アプリ）の機能実装を担当するための引き継ぎ書です。
> **新セッションでは、まずこのファイルと CLAUDE.md（あれば）と kaizen_pack_revenue_handover.md を読んでください。**

---

## 1. プロジェクト概要

- **プロダクト名**：Replyy（"Reply faster. Reply safer."）
- **位置付け**：親プロジェクト「Reply（replyto1m）」build in public 内の **MVP 兼 検証プロダクト**
- **現状**：v0.1 プロトタイプ（Codex で作成済、ライブデプロイ済）
- **訴求**：Paste-based / プライバシー重視 / カスタマイズ可（Relationship・Goal・Tone・Language）
- **既存価格**：Free 5回/月 + Pro $9/月（モック決済）

### 親プロジェクトのミッション
**「営業職の時間を取り戻す」**
- 短期：メール返信時間の半減
- 長期：営業全体の時間泥棒（テレアポ準備・商談準備・後処理）を解く
- 12〜24ヶ月で $1M ARR を目指す（年4-5本SaaSのスタジオ構想の1本目）

---

## 2. 現在地（2026/6/8時点）

- Issue #001 配信完了（購読者2人、Open率100%）
- ヒアリング 5人完了（けんと/えいる/GMコンサル/M/大森）
- 「営業前準備」が痛みの本丸という仮説強化中
- 改善パック販売開始（けんとさんを最初のターゲット）
- Replyy v0.1 → **改善パック特典として配布**＋並行で機能拡張を行う方針

### 戦略的判断（重要）
- **汎用 Replyy（$9/月）単体では収益化困難**（市場リサーチ済、Superhuman/Writemail.ai/alfred_等の競合強い）
- → **業界特化・チーム機能・差別化機能を積み増す**ことで $19-29/月帯を狙う
- 並行して改善パックで早期収益化、Replyy は中長期投資

---

## 3. コードベース

### このリポジトリ
- **パス**：`/home/user/ReadMe`
- **ベース**：claudecodeui (siteboon/claudecodeui) v1.32.0 から派生
- **スタック**：Vite + Express + React + TypeScript
- **重要ディレクトリ**：
  - `src/` - フロントエンド React
  - `server/` - バックエンド Express
  - `replyy-lp/` - Replyy ランディングページ（Next.jsの可能性）
  - `shared/` - 共通型定義
  - `docker/` - Docker 設定

### ⚠️ Replyy 本体アプリの実体は要確認
ユーザーが Codex で作ったプロトタイプは、**このリポジトリ内 or 別プロジェクト**のどちらかです。新セッションは最初に user に確認してください：
- 「Replyy 本体のコード、このリポジトリの中にあるか、別 repo か？」
- 別 repo の場合、clone してから作業

### 既存スクリプト
```
npm run dev          # 開発（client + server 同時）
npm run client       # フロントだけ
npm run server:dev   # バックだけ
npm run build        # 本番ビルド
npm run typecheck    # 型チェック
npm run lint         # ESLint
```

---

## 4. 実装ロードマップ（5週間で4ティア完成）

### Week 1（6/9-6/15）：業界 Pack ⭐ 最優先

**目標：** 汎用ツール → 業界特化ツールへの差別化第一歩

**実装内容：**
- フロントエンドに「Industry / Role」セレクト追加
- 5パック作成：SaaS営業 / IS（インサイドセールス）/ 開発営業 / コンサル / カスタマーサポート
- 各 Pack を JSON で定義（プロンプト + 業界用語辞書 + シーン別 example）
- AI 呼び出し時に system prompt へ Pack 内容を注入

**素材：** `/home/user/ReadMe/kaizen_pack_templates_v1.md` のテンプレ・ヒアリングデータがそのまま使える

**価格設計案：** Free / Basic $9 / Industry Pro $19

**完了基準：** けんとさんに Pack 付きで使ってもらえる状態

---

### Week 2（6/16-6/22）：差別化機能 第1弾

#### ② マルチ返信生成（3 variants）
- 1回の Analyze で「フォーマル / カジュアル / 簡潔」を同時表示
- Pro 限定機能
- AI を 1回呼び出しで structured output として3パターン要求

#### ⓐ 敬語レベル スライダー（日本語特化）
- 4段階：くだけた / 標準 / 丁寧 / 超丁寧
- プロンプトに「敬語レベル: X」を注入するだけ

#### ⓒ History + お気に入り
- 過去の生成結果を保存・検索
- Pro限定で過去30日分保持
- DB スキーマ追加（user_id, email_input, reply_output, created_at）

---

### Week 3-4（6/23-7/6）：収益化の確実化

#### ③ Stripe 本番決済 + Credits 制
- モック決済 → Stripe Checkout 統合
- 料金モデル：$9/月 base + $0.10/email after 30 emails
- Customer Portal でアップグレード/解約
- Webhook で usage 追跡

#### ⓑ PII リダクター
- 返信を送る前に「会社名・人名・数字」を AI が検出 → ハイライト＆置換提案
- プライバシー軸の強化（企業ユーザーに刺さる）

---

### Week 5-8（7月）：高単価ティア確立

#### ① My Voice（自分の文体学習）⭐ 最強差別化
- 設定画面に「My Voice」セクション追加
- 過去メール 5-10通 を貼り付け
- AI が文体的特徴を JSON で抽出（敬語・絵文字頻度・締め方・業界用語）
- 全ての返信生成時、style profile を system prompt に注入
- これが Pro プランの「唯一無二の理由」になる

#### ④ Team Workspace
- Workspace モデル（Owner / Member）
- 共有テンプレ・共有 My Voice・共有 Industry Pack
- 管理ダッシュボード
- Stripe per-seat 課金（$29/user/月）

---

## 5. 価格設計の最終形（5週間後）

```
Free           : 5回/月、業界 Pack なし
Basic    $9/月 : 30回/月、業界 Pack なし
Industry Pro $19/月 : 100回/月、業界 Pack ✅、敬語スライダー、3 variants
Team    $29/user/月 : 無制限、Workspace、共有リソース、My Voice
```

---

## 6. 並行作業との連携

このプロジェクトは **2つのチャット** で並行運用します：

### 🔵 メインチャット（戦略・運用担当）
- 改善パック販売（けんと/えいる/大森/GM/M 対応）
- X / note / LinkedIn 発信
- Issue #002 配信（6/16予定）
- 新規ヒアリング獲得
- 数字管理・ふりかえり
- ピボット判断

### 🟢 Replyy 実装チャット（このセッション = 技術担当）
- 上記ロードマップの実装
- バグ修正
- デプロイ
- Stripe 統合
- 認証システム
- パフォーマンス改善

### 連携ルール
- 改善パック顧客（けんと等）から Replyy のFBが来たら、メインチャットで受け取り → このチャットの「改善トラッカー」（Notion）に反映
- 新機能リリース時はメインチャットに通知（X/note で発信するため）

---

## 7. 制約・ルール

### CLAUDE.md ルール準拠
1. **最新情報で fact-check**：実装前にライブラリの最新仕様確認（Stripe SDK、Anthropic SDK、Next.js等）
2. **手順は1工程ずつ**：user への説明・確認は具体的に
3. **ですます調・正直・温かい温度感**

### Git 運用
- 機能ごとに **branch 分離**：`feature/replyy-industry-pack` `feature/replyy-stripe` 等
- main へのマージは user 確認後
- commit メッセージは Conventional Commits

### API キー管理（user 提供）
- Anthropic API key
- Stripe Secret Key / Webhook Secret
- DB 接続情報（必要なら）
- → `.env` ファイルで管理、絶対に commit しない

### デプロイ
- 既存環境（user に確認：Vercel / Railway / 自前）
- ブランチデプロイ可能なら、機能ごとに preview URL 発行

---

## 8. 新セッション開始時の最初の3ステップ

1. **ファイル読み込み**：
   - `/home/user/ReadMe/CLAUDE.md`（gitignoreされてるので新コンテナにはない可能性あり）
   - `/home/user/ReadMe/replyy_implementation_handover.md`（このファイル）
   - `/home/user/ReadMe/kaizen_pack_revenue_handover.md`（参照用）

2. **user に最初の確認**：
   - 「Replyy 本体コードはこのリポジトリ内ですか？別 repo ですか？」
   - 「ライブの Replyy URL を教えてください」
   - 「Anthropic API キーと Stripe アカウントの準備状況は？」
   - 「Week 1 の業界 Pack から着手で良いですか？」

3. **コードベース把握**：
   - `src/` の構造を Read で確認
   - `server/` の API 構造を確認
   - 既存の AI 呼び出し箇所を Grep で発見
   - 既存の認証・課金まわりがあれば把握

---

## 9. ヒアリング協力者 5名（業界 Pack の素材源）

| # | 氏名 | 職種 | 痛みの中核 | 業界 Pack 該当 |
|---|---|---|---|---|
| 1 | けんとさん | 営業（見積〜納品） | 商談準備＞他社比較＋資料作成 | 開発営業 / 営業全般 |
| 2 | えいるさん | 開発営業 | 商談準備＞見積もり策定 | 開発営業 / IT |
| 3 | GMコンサルさん | コンサル業 | テレ準備 + Gmail テンプレ | コンサル |
| 4 | Mさん | 業種非公開（マーケ営業の問合せ窓口） | 知識外質問への対応 | CS / 問合せ |
| 5 | 大森さん（ZEALS） | インサイドセールス | テレアポ準備（Gem構築） | IS |

→ これらの肩書きと痛みを、業界 Pack 5本（営業 / IS / 開発営業 / コンサル / CS）に反映。

---

## 10. 関連ファイル

| ファイル | 内容 | git管理 |
|---|---|---|
| `/home/user/ReadMe/CLAUDE.md` | プロジェクト全体ルール | gitignore（新コンテナにない可能性）|
| `/home/user/ReadMe/replyy_implementation_handover.md` | このファイル | ✅ commit |
| `/home/user/ReadMe/kaizen_pack_revenue_handover.md` | 改善パック収益化議論 | ✅ commit |
| `/home/user/ReadMe/kaizen_pack_templates_v1.md` | 改善パック ひな形（業界Packの素材） | ✅ commit |

---

## 11. 重要：このチャットでやらないこと

以下はメインチャット（戦略担当）の管轄。**このチャットでは扱わない**：

- ❌ 改善パック販売の DM 作成・送信
- ❌ X / note / LinkedIn の文面作成・投稿戦略
- ❌ 新規ヒアリング協力者探し
- ❌ Issue #002 構想・執筆
- ❌ オファー文面・告知ツイの作成
- ❌ 数字晒しツイの作成
- ❌ ピボット判断

→ 上記が出てきたら「メインチャットでやってください」と user に案内。

---

## 12. 成功指標（5週間後＝7月中旬時点）

| 項目 | 目標 |
|---|---|
| 業界 Pack | 5本完成 |
| Stripe 本番稼働 | ✅ |
| Pro / Industry Pro / Team の3ティア | 完成 |
| My Voice 機能 | 実装完了 |
| 改善パック顧客からの Replyy FB | 3件以上回収 |
| Replyy 有料ユーザー | 5人以上（うちチームプラン1社） |
| Replyy MRR | $200-500 |

→ ここを達成すれば、年内 $1K MRR が射程。スタジオ戦略 2本目への準備が整います。
