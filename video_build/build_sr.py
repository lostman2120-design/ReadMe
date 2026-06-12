import os, math, subprocess, sys
from PIL import Image, ImageDraw, ImageFilter, ImageFont
sys.path.insert(0,"/home/user/ReadMe/video_build")

W,H=1920,1080; FPS=30
FD="/home/user/ReadMe/video_build/fonts"
UP="/root/.claude/uploads/674c214f-69fa-5d01-8268-0943419127c8"
V1=f"{UP}/f44d6bce-demo__1.mp4"   # multilingual form
V2=f"{UP}/787aca34-demo__2.mp4"   # form->reply->admin
V3=f"{UP}/38d6b05e-demo__3.mp4"   # LP
OUT="/home/user/ReadMe/video_build/out_sr"; TMP="/tmp/srb"
os.makedirs(OUT,exist_ok=True); os.makedirs(TMP,exist_ok=True)

NAVY=(19,49,78); TEAL=(95,202,174); AMBER=(214,120,40); RED=(206,60,55)

def jf(w,size):
    m={"R":"NotoSansJP-Regular.otf","M":"NotoSansJP-Medium.otf","B":"NotoSansJP-Bold.otf","K":"NotoSansJP-Black.otf"}
    return ImageFont.truetype(os.path.join(FD,m[w]),size)
def ease_out(t): return 1-(1-t)**3
def clamp(v,a,b): return max(a,min(b,v))

def run(cmd):
    r=subprocess.run(cmd,shell=True,capture_output=True,text=True)
    if r.returncode!=0:
        print("ERR",cmd[:120]); print(r.stderr[-1400:]); raise SystemExit(1)

# ---------- telop overlay ----------
def telop_lower(main, sub, accent, prog):
    """bottom band, left-aligned with accent bar"""
    L=Image.new("RGBA",(W,H),(0,0,0,0)); d=ImageDraw.Draw(L)
    a=clamp(prog,0,1)
    band=Image.new("RGBA",(W,210),(0,0,0,0)); bd=ImageDraw.Draw(band)
    for i in range(210):
        al=int(225*(i/210)**0.8)
        bd.line([(0,i),(W,i)],fill=(10,24,40,al))
    L.alpha_composite(band,(0,H-210))
    # accent bar
    bx=78
    d.rounded_rectangle([bx,H-152,bx+9,H-58],radius=4,fill=accent+(255,))
    mf=jf("K",58); sf=jf("M",31)
    # shrink main if too wide
    while d.textlength(main,font=mf)>W-260 and mf.size>40:
        mf=jf("K",mf.size-2)
    d.text((bx+30,H-156),main,font=mf,fill=(255,255,255,255))
    d.text((bx+32,H-74),sub,font=sf,fill=(196,216,234,255))
    if a<1:
        tmp=Image.new("RGBA",(W,H),(0,0,0,0)); L=Image.blend(tmp,L,a)
    return L

def telop_title(main, sub, prog, appear):
    L=Image.new("RGBA",(W,H),(0,0,0,0))
    a=clamp(prog,0,1)
    # soft full scrim
    scrim=Image.new("RGBA",(W,H),(8,20,34,int(150*a)))
    L.alpha_composite(scrim)
    d=ImageDraw.Draw(L)
    mf=jf("K",78); sf=jf("M",40)
    yoff=int((1-ease_out(clamp(appear,0,1)))*26)
    # main can be 1-2 lines; wrap on 、
    main_lines=[main] if d.textlength(main,font=mf)<=W-200 else None
    if main_lines is None:
        # split at 、
        if "、" in main:
            i=main.index("、")+1; main_lines=[main[:i],main[i:]]
        else: main_lines=[main]
    lh=96; total=lh*len(main_lines)
    y0=440-total//2+yoff
    for i,ln in enumerate(main_lines):
        d.text((W//2,y0+i*lh),ln,font=mf,fill=(255,255,255,255),anchor="mm",
               stroke_width=2,stroke_fill=(8,20,34,255))
    # accent rule
    d.rounded_rectangle([W//2-70,y0+total+18,W//2+70,y0+total+24],radius=3,fill=TEAL+(255,))
    d.text((W//2,y0+total+70),sub,font=sf,fill=(206,224,240,255),anchor="mm")
    if a<1:
        tmp=Image.new("RGBA",(W,H),(0,0,0,0)); L=Image.blend(tmp,L,a)
    return L

def env(t,dur,fin=0.4,fout=0.4):
    return min(t/fin,(dur-t)/fout,1.0)

# ---------- segment definitions ----------
SEG=[
 dict(name="s1_title", src=V1, ss=0.0, to=3.0, dur=5.0, crop=(0,11,3240,1823),
      kind="title", main="外国人ゲストからの問い合わせ対応を、もっと安全に。",
      sub="小規模宿泊施設向け　多言語問い合わせ一次対応AI"),
 dict(name="s2_lang", src=V1, ss=2.5, to=13.5, dur=9.0, crop=(740,130,1620,911),
      kind="lower", accent=TEAL, main="ゲストは自分の言語で問い合わせ",
      sub="日本語  /  English  /  简体中文  /  한국어"),
 dict(name="s3_reply", src=V2, ss=5.5, to=13.0, dur=9.0, crop=(340,1000,1500,844),
      kind="lower", accent=TEAL, main="よくある質問は、AIが返信候補を作成",
      sub="チェックイン時間・駐車場・Wi-Fi・アクセス など"),
 dict(name="s4a_danger", src=V2, ss=24.0, to=28.5, dur=6.0, crop=(320,820,1100,619),
      kind="lower", accent=RED, main="危険な問い合わせには、AIが勝手に返信しません",
      sub="返金 / 予約変更 / 鍵紛失 / 重度アレルギー / クレーム"),
 dict(name="s4b_safety", src=V3, ss=13.5, to=20.0, dur=6.0, crop=(60,720,1800,1012),
      kind="lower", accent=RED, main="該当する問い合わせは、スタッフ確認へ自動振り分け",
      sub="返信事故を防ぐ、安全な一次対応"),
 dict(name="s5_detail", src=V2, ss=26.0, to=35.0, dur=10.0, crop=(1300,430,1420,799),
      kind="lower", accent=NAVY if False else TEAL, main="スタッフは、日本語で内容を確認",
      sub="原文・日本語訳・ゲスト表示内容・通知履歴を管理"),
 dict(name="s6_dash", src=None, dur=7.0, crop=None, img="/tmp/srb/dash_still.png",
      kind="still", accent=TEAL, main="夜間・繁忙期の問い合わせ対応を軽く",
      sub="対応に追われる時間を、本来の接客へ。"),
 dict(name="s7_end", src=None, dur=5.0, kind="end"),
]

def build(seg):
    name=seg["name"]; dur=seg["dur"]; out=f"{OUT}/{name}.mp4"
    if seg["kind"]=="end":
        run(f'ffmpeg -y -loglevel error -loop 1 -i assets/sr_endcard.png '
            f'-vf "scale=2112:1188,zoompan=z=\'min(1.0+on*0.0004,1.055)\':x=\'iw/2-(iw/zoom/2)\':y=\'ih/2-(ih/zoom/2)\':d={int(dur*FPS)}:s={W}x{H}:fps={FPS},format=yuv420p" '
            f'-t {dur} -r {FPS} -c:v libx264 -crf 18 -pix_fmt yuv420p "{out}"')
        print("built",name); return
    base=f"{TMP}/{name}_base.mp4"
    if seg["kind"]=="still":
        run(f'ffmpeg -y -loglevel error -loop 1 -i "{seg["img"]}" '
            f'-vf "scale=2112:1188,zoompan=z=\'min(1.0+on*0.00035,1.05)\':x=\'iw/2-(iw/zoom/2)\':y=\'ih/2-(ih/zoom/2)\':d={int(dur*FPS)}:s={W}x{H}:fps={FPS},format=yuv420p" '
            f'-t {dur} -r {FPS} -c:v libx264 -crf 17 -pix_fmt yuv420p "{base}"')
    else:
        cx,cy,cw,ch=seg["crop"]; pts=dur/(seg["to"]-seg["ss"])
        vf=f"crop={cw}:{ch}:{cx}:{cy},setpts={pts:.5f}*PTS,fps={FPS},scale={W}:{H}:flags=lanczos"
        run(f'ffmpeg -y -loglevel error -ss {seg["ss"]} -to {seg["to"]} -i "{seg["src"]}" -an '
            f'-vf "{vf}" -t {dur} -r {FPS} -c:v libx264 -crf 16 -pix_fmt yuv420p "{base}"')
    # overlay frames
    odir=f"{TMP}/{name}_ov"; os.makedirs(odir,exist_ok=True)
    nfr=int(dur*FPS)
    for fi in range(nfr):
        t=fi/FPS; e=env(t,dur)
        if seg["kind"]=="title":
            ap=clamp(t/0.6,0,1)
            L=telop_title(seg["main"],seg["sub"],e,ap)
        else:
            L=telop_lower(seg["main"],seg["sub"],seg["accent"],e)
        L.save(f"{odir}/f{fi:04d}.png")
    run(f'ffmpeg -y -loglevel error -i "{base}" -framerate {FPS} -i "{odir}/f%04d.png" '
        f'-filter_complex "[0:v][1:v]overlay=format=auto" -r {FPS} -t {dur} -c:v libx264 -crf 17 -pix_fmt yuv420p "{out}"')
    print("built",name)

if __name__=="__main__":
    only=sys.argv[1:] if len(sys.argv)>1 else None
    for s in SEG:
        if only and s["name"] not in only: continue
        build(s)
