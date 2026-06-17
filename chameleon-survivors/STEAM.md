# Steam で販売するまで — Chameleon Survivors

このゲームは依存ゼロの HTML5（`index.html` + `assets/`）です。ブラウザでそのまま動き、
**Electron でデスクトップアプリ化 → Steam で配信**できます。

---

## 1. ブラウザで遊ぶ（開発・共有）
- `index.html` を直接開く、または `python3 -m http.server` で配信
- 公開URL例（GitHub Pages / raw.githack）でそのまま共有可能

## 2. デスクトップアプリ化（Electron）
```bash
cd chameleon-survivors
npm install          # electron / electron-builder を取得
npm start            # 動作確認（ウィンドウ起動、F11で全画面）
npm run dist         # インストーラを dist/ に生成
# OS個別:  npm run dist:win  /  dist:mac  /  dist:linux
```
- 生成物（`dist/`）：Windows `.exe`(NSIS) / macOS `.dmg` / Linux `.AppImage`
- アイコンを付けるなら `build.icon`（256pxの.ico/.icns）を `package.json` に追加
- 軽量バイナリにしたい場合は **Tauri** でも可（Rust必要・容量小、Steam向きに有利）

## 3. Steam に登録・アップロード
1. **Steamworks 登録**：Steam Direct 手数料 **$100/作品**（作品が一定額売れると返金）
2. パートナーサイトで **App ID** を発行 → ストアページ作成（名前・説明・スクショ・トレーラー・価格）
3. **ビルドのアップロード**（SteamPipe / steamcmd）
   - `content/` に `npm run dist` の中身（実行ファイル一式）を入れる
   - `app_build_<AppID>.vdf` と `depot_build_<DepotID>.vdf` を用意
   - `steamcmd +login <user> +run_app_build app_build_<AppID>.vdf +quit`
   - Steamworth管理画面で対象ブランチ（default）に**ビルドを反映**
4. **起動設定（Launch Options）**：OSごとに実行ファイルを指定
5. ストア審査 → 価格・リリース日を設定 → 公開

## 4. 実績・ランキングを Steam に載せる（任意・推奨）
Electron から Steam API を叩くには [`steamworks.js`](https://github.com/ceifa/steamworks.js) が手軽です。
```bash
npm i steamworks.js
```
`electron-main.js` でアプリ起動時に初期化：
```js
const steamworks = require("steamworks.js");
const client = steamworks.init(<AppID>);          // steam_appid.txt も置く
// 実績: client.achievement.activate("BEAT_ONI");
// ランキング: client.leaderboard.uploadScore("highscore", score);
```
- **Steamリーダーボード**を作れば、ゲーム内のオンラインランキングをSteam公式に置き換え可能
  （`index.html` の `LB_ENDPOINT` を使う Web版ランキングと併用も可）

## 5. Web版のオンラインランキング（Steamを使わない場合）
`leaderboard-worker.js`（Cloudflare Worker）をデプロイし、
`index.html` 冒頭の `LB_ENDPOINT` にURLを設定すれば**グローバルランキング**が有効化されます。
（未設定なら自動で端末内ローカルランキングにフォールバック）

---

### 補足
- Steam の**オーバーレイ／クラウドセーブ／実績**は Steamworks 連携で対応
- まずは Electron ビルドを Steam にアップして動作確認 → 実績・ランキングを追加、が安全な順序
- ストア素材（カプセル画像・トレーラー）は審査・集客に効くので別途用意を
