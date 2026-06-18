# 引き寄せノート (Hikiyose) — Android

手書きワイヤーフレーム①〜⑤と「〇〇式」のメモをもとに作成した、引き寄せ
（Law of Attraction）アプリの Android 版です。v1 はすべて**端末内で完結**
（ネット接続・アカウント不要）します。

## 画面構成（5タブ）

| タブ | 画面 | 元ワイヤー | 内容 |
|------|------|-----------|------|
| ホーム | `HomeScreen` | ① | 左側に「〇〇式」選択タブ。選んだ式の引き寄せ方法コラムを右側に表示 |
| 記入 | `EntryScreen` | ② | 「必ず引き寄せること」「叶った自分からのメッセージ」「毎日唱えること（アファメーション）」＋TODOリスト |
| 日記 | `JournalScreen` | ③ | 上部に引き寄せること → 月カレンダー → 当日「今日はどんな日になれば最高？」＋メモ → 「今日よかった・嬉しかったこと」 |
| 達成 | `AchievementScreen` | ④ | 達成した引き寄せ・達成日・思っていることを記録 |
| 記録 | `RecordsScreen` | ⑤ | 過去に達成した引き寄せの記録を閲覧 |

## 「〇〇式」コラム（ホーム）

`data/MethodsData.kt` に5つの式を同梱（オフライン）:
**199式 / 登山家式 / マカロン式 / イウォーク式 / 701式**。
左タブで選ぶと、その式の引き寄せ方法の解説コラムが表示されます。選択内容は保存されます。

## 技術スタック

- **言語**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **ナビゲーション**: Navigation Compose（下部5タブ）
- **ローカル保存**: Room（引き寄せ／アファメーション／日記／ToDo）＋ DataStore（選択中の式・叶った自分からのメッセージ）
- **アーキテクチャ**: 画面ごとに ViewModel + StateFlow。DI は `HikiyoseApplication` での手動配線
- **最小SDK**: 24（`java.time` を core library desugaring で利用）/ ターゲット 35

## データモデル

- `Manifestation`（必ず引き寄せること）: active → achieved のライフサイクル。達成時に達成日・思っていることを保持。達成済みは記録タブに表示
- `Affirmation`（毎日唱えること）
- `JournalEntry`（日記: idealDay / body / goodThings、1日1件）
- `TodoItem`（TODO）

## ビルド方法

Android SDK が必要です（Android Studio 推奨）。

```bash
./gradlew assembleDebug      # APK をビルド
./gradlew installDebug       # 接続端末にインストール
```

CLI ビルドには `local.properties` に SDK パスが必要です:

```
sdk.dir=/path/to/Android/sdk
```

## 今後の拡張候補（v1 では未実装）

- コミュニティ機能 — サーバー/バックエンドが必要
- 毎朝の通知（アファメーション・引き寄せのリマインド）
- データのバックアップ/エクスポート
