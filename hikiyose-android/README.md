# 引き寄せノート (Hikiyose) — Android

手書きワイヤーフレーム①〜⑤をもとに作成した、引き寄せ（Law of Attraction）アプリの
Android 版です。v1 はすべて**端末内で完結**（ネット接続・アカウント不要）します。

## 画面構成

| タブ | 画面 | 元ワイヤー | 内容 |
|------|------|-----------|------|
| ホーム | `HomeScreen` | ② | 今日のアファメーション（日替わり）＋偉人の言葉（日替わり）＋TO DO リスト |
| ジャーナル | `JournalScreen` | ③ | 月カレンダー＋日付ごとの感謝・本文の記録。記録済みの日にはマーク |
| アファメーション | `AffirmationScreen` | ① | 自分のアファメーション文の追加・編集・お気に入り・削除 |
| 書式 | `TemplateScreen` | ④ | ジャーナルのテンプレート（感謝/引き寄せ/ふりかえり/フリー）を選択 |

## 技術スタック

- **言語**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **ナビゲーション**: Navigation Compose（下部タブ）
- **ローカル保存**: Room（アファメーション・ジャーナル・ToDo）＋ DataStore（選択中の書式）
- **アーキテクチャ**: 画面ごとに ViewModel + StateFlow。DI は `HikiyoseApplication` での手動配線
- **最小SDK**: 24（`java.time` を core library desugaring で利用）/ ターゲット 35

## ビルド方法

Android SDK が必要です（Android Studio 推奨）。

```bash
# プロジェクトを Android Studio で開くか、CLI で:
./gradlew assembleDebug      # APK をビルド
./gradlew installDebug       # 接続端末にインストール
```

CLI ビルドには `local.properties` に SDK パスが必要です:

```
sdk.dir=/path/to/Android/sdk
```

## 偉人の言葉について

`data/QuotesData.kt` に約30件の名言を同梱しています。日付をもとに決定的に
1件を選ぶため、その日のうちは固定で、毎日入れ替わります。

## 今後の拡張候補（v1 では未実装）

- コミュニティ機能（ワイヤー⑤）— サーバー/バックエンドが必要
- 毎朝の通知（アファメーションのリマインド）
- データのバックアップ/エクスポート
