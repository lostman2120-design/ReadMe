// Electron wrapper so the game can ship as a desktop app (and on Steam).
// Dev:   npm install && npm start
// Build: npm run dist   (installers land in dist/)
const { app, BrowserWindow } = require("electron");

function createWindow() {
  const win = new BrowserWindow({
    width: 1280,
    height: 720,
    minWidth: 800,
    minHeight: 600,
    backgroundColor: "#0d1b14",
    fullscreenable: true,
    autoHideMenuBar: true,
    webPreferences: { contextIsolation: true },
  });
  win.removeMenu();
  win.loadFile("index.html");
  // F11 toggles fullscreen
  win.webContents.on("before-input-event", (e, input) => {
    if (input.type === "keyDown" && input.key === "F11") {
      win.setFullScreen(!win.isFullScreen());
    }
  });
}

app.whenReady().then(() => {
  createWindow();
  app.on("activate", () => {
    if (BrowserWindow.getAllWindows().length === 0) createWindow();
  });
});

app.on("window-all-closed", () => {
  if (process.platform !== "darwin") app.quit();
});
