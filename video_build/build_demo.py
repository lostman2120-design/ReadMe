import os, math, subprocess, sys
from PIL import Image, ImageDraw, ImageFilter
sys.path.insert(0,"/home/user/ReadMe/video_build")
from lib import *

SRC="/root/.claude/uploads/674c214f-69fa-5d01-8268-0943419127c8/1ac3f3d9-PR__21.mp4"
WORK="/home/user/ReadMe/video_build"
OUT=f"{WORK}/out"; TMP="/tmp/demo"; os.makedirs(TMP,exist_ok=True); os.makedirs(OUT,exist_ok=True)
FPS=30
ACCENT=(15,118,110)      # teal
ACCENT2=(255,122,89)     # hubspot orange
LI_BLUE=(10,102,194)

# global timeline for progress bar (output seconds)
GLOBAL_START=5.0; GLOBAL_END=55.0; GLOBAL_TOTAL=60.0

def run(cmd):
    r=subprocess.run(cmd, shell=True, capture_output=True, text=True)
    if r.returncode!=0:
        print("ERR:",cmd); print(r.stderr[-1500:]); raise SystemExit(1)

# ---------- ACT DEFINITIONS ----------
# rect = (x,y,w,h) in 1920x1080 ; side = where callout goes ('left'/'right'/'top'/'bottom')
ACTS=[
 dict(name="act1", src=(3.5,9.5), dur=10.0, accent=ACCENT, label="STEP 1   ANALYZE",
   g0=5.0, g1=15.0,
   subs=[(0.3,4.6,"One click on any LinkedIn profile."),
         (4.8,9.8,"AI reads it and scores the lead — instantly.")],
   spots=[(0.9,4.2,(1504,160,172,50),"Click  Analyze Profile","left",ACCENT),
          (5.6,9.6,(1478,915,190,150),"Lead Score: 65","left",ACCENT)]),

 dict(name="act2", src=(9.5,22.0), dur=10.0, accent=ACCENT, label="STEP 2   INSIGHTS",
   g0=15.0, g1=25.0,
   subs=[(0.3,4.8,"A Lead Score, pain points & buying signals."),
         (5.0,9.8,"Plus a personalized DM — written for you.")],
   spots=[(0.6,4.6,(1478,748,430,135),"Pain points & signals","left",ACCENT),
          (5.2,9.6,(1478,398,430,262),"Ready-to-send DM","left",ACCENT)]),

 dict(name="act3", src=(22.0,31.3), dur=15.0, accent=ACCENT2, label="STEP 3   ADD TO HUBSPOT",
   g0=25.0, g1=40.0,
   subs=[(0.3,3.6,"Now push it straight to your CRM."),
         (3.8,7.6,"One click:  Add to HubSpot."),
         (7.8,11.0,"No retyping. No copy-paste."),
         (11.2,14.7,"Contact, note & task — all created for you.")],
   spots=[(0.8,6.2,(1578,857,215,55),"Add to HubSpot","left",ACCENT2)]),

 dict(name="act4", src=(36.5,55.0), dur=15.0, accent=ACCENT2, label="STEP 4   DONE",
   g0=40.0, g1=55.0, success=(0.25,3.0),
   subs=[(3.3,7.2,"An AI-written summary note, auto-generated."),
         (7.4,11.0,"A follow-up task, so nothing slips."),
         (11.2,14.8,"Your CRM — always up to date.")],
   spots=[(4.7,9.2,(300,118,1010,272),"AI summary note","bottom",ACCENT2)]),
]

# ---------- overlay drawing helpers ----------
def scrim_bottom():
    s=Image.new("RGBA",(W,H),(0,0,0,0)); d=ImageDraw.Draw(s)
    for i in range(260):
        y=H-260+i; a=int(150*(i/260)**1.6)
        d.line([(0,y),(W,y)],fill=(6,9,16,a))
    return s
SCRIM=scrim_bottom()

def glow_box(rect, accent, prog, pulse):
    """animated glowing rounded border around rect; prog 0..1 draw-in; pulse for breathing"""
    x,y,w,h=rect; pad=10
    layer=Image.new("RGBA",(W,H),(0,0,0,0)); d=ImageDraw.Draw(layer)
    rx0,ry0,rx1,ry1=x-pad,y-pad,x+w+pad,y+h+pad
    a=int(255*clamp(prog,0,1))
    # outer glow
    glow=Image.new("RGBA",(W,H),(0,0,0,0)); gd=ImageDraw.Draw(glow)
    gw=int(5+3*pulse)
    gd.rounded_rectangle([rx0,ry0,rx1,ry1], radius=14, outline=accent+(int(a*0.9),), width=gw)
    glow=glow.filter(ImageFilter.GaussianBlur(9))
    layer.alpha_composite(glow)
    d.rounded_rectangle([rx0,ry0,rx1,ry1], radius=14, outline=accent+(a,), width=4)
    return layer

def callout(rect, side, text, accent, prog):
    x,y,w,h=rect
    f=font("Bold",30)
    tmp=Image.new("RGBA",(10,10),(0,0,0,0)); td=ImageDraw.Draw(tmp)
    tw=td.textlength(text,font=f); padx,pady=22,14
    bw,bh=int(tw+padx*2), 30+pady*2
    chip=Image.new("RGBA",(bw,bh),(0,0,0,0)); cd=ImageDraw.Draw(chip)
    cd.rounded_rectangle([0,0,bw-1,bh-1], radius=bh//2, fill=accent+(255,))
    cd.text((bw//2,bh//2),text,font=f,fill=(255,255,255,255),anchor="mm")
    # position
    gap=22
    if side=="left":
        cx=x-gap-bw; cy=y+h//2-bh//2
        tri=[(cx+bw,cy+bh//2-10),(cx+bw,cy+bh//2+10),(cx+bw+12,cy+bh//2)]
    elif side=="bottom":
        cx=x+w//2-bw//2; cy=y+h+gap+10
        tri=[(cx+bw//2-10,cy),(cx+bw//2+10,cy),(cx+bw//2,cy-12)]
    elif side=="top":
        cx=x+w//2-bw//2; cy=y-gap-bh-10
        tri=[(cx+bw//2-10,cy+bh),(cx+bw//2+10,cy+bh),(cx+bw//2,cy+bh+12)]
    else: # right
        cx=x+w+gap; cy=y+h//2-bh//2
        tri=[(cx,cy+bh//2-10),(cx,cy+bh//2+10),(cx-12,cy+bh//2)]
    layer=Image.new("RGBA",(W,H),(0,0,0,0)); d=ImageDraw.Draw(layer)
    # slide-in offset
    off=int((1-ease_out(clamp(prog,0,1)))*40)
    dx = off if side=="left" else (-off if side=="right" else 0)
    dy = off if side=="top" else (-off if side=="bottom" else 0)
    a=clamp(prog,0,1)
    d.polygon([(px+dx,py+dy) for px,py in tri], fill=accent+(int(255*a),))
    cc=Image.new("RGBA",chip.size,(0,0,0,0))
    cc=Image.blend(cc,chip,a)
    layer.alpha_composite(cc,(cx+dx,cy+dy))
    return layer

def act_label(text, accent, prog_in, prog_out):
    f=font("ExtraBold",30)
    parts=text.split("   ")
    num=parts[0]; name=parts[1] if len(parts)>1 else ""
    layer=Image.new("RGBA",(W,H),(0,0,0,0)); d=ImageDraw.Draw(layer)
    nw=d.textlength(name,font=f);
    fn=font("Black",30)
    numw=d.textlength(num,font=fn)
    bw=int(numw+nw+ 30+30+24); bh=58
    x=64; y=54
    a=clamp(min(prog_in, prog_out),0,1)
    off=int((1-ease_out(clamp(prog_in,0,1)))*-60)
    chip=Image.new("RGBA",(bw,bh),(0,0,0,0)); cd=ImageDraw.Draw(chip)
    cd.rounded_rectangle([0,0,bw-1,bh-1],radius=14,fill=(10,15,25,235))
    cd.rounded_rectangle([0,0,int(numw)+24,bh-1],radius=14,fill=accent+(255,))
    cd.rectangle([int(numw)+12,0,int(numw)+24,bh-1],fill=accent+(255,))
    cd.text((12,bh//2),num,font=fn,fill=(255,255,255,255),anchor="lm")
    cd.text((int(numw)+24+16,bh//2),name,font=f,fill=(255,255,255,255),anchor="lm")
    chip2=Image.new("RGBA",chip.size,(0,0,0,0)); chip2=Image.blend(chip2,chip,a)
    layer.alpha_composite(chip2,(x+off,y))
    return layer

def progress_bar(gp, accent):
    layer=Image.new("RGBA",(W,H),(0,0,0,0)); d=ImageDraw.Draw(layer)
    y=H-8
    d.rectangle([0,y,W,H],fill=(255,255,255,40))
    d.rectangle([0,y,int(W*clamp(gp,0,1)),H],fill=accent+(235,))
    return layer

def success_card(t, s0, s1, accent):
    cw,ch=900,236
    card=Image.new("RGBA",(cw,ch),(0,0,0,0)); d=ImageDraw.Draw(card)
    d.rounded_rectangle([0,0,cw-1,ch-1],radius=28,fill=(255,255,255,255))
    # green check circle
    cy=ch//2; cxx=110; r=58
    d.ellipse([cxx-r,cy-r,cxx+r,cy+r],fill=(34,197,94,255))
    d.line([(cxx-26,cy+4),(cxx-6,cy+24),(cxx+30,cy-20)],fill=(255,255,255,255),width=12,joint="curve")
    # text
    d.text((cxx+r+34,cy-34),"Contact created in HubSpot",font=font("ExtraBold",46),fill=(15,23,42,255),anchor="lm")
    d.text((cxx+r+34,cy+24),"Name · title · company · LinkedIn URL — all synced",
           font=font("Medium",27),fill=(100,116,139,255),anchor="lm")
    # hubspot tag
    d.rounded_rectangle([cw-150,28,cw-30,70],radius=21,fill=accent+(255,))
    d.text((cw-90,49),"HubSpot",font=font("Bold",24),fill=(255,255,255,255),anchor="mm")
    card=drop_shadow(card,blur=30,alpha=120,offset=(0,16))
    # animate: pop in (0.3s overshoot), hold, slide-up+fade out (last 0.35s)
    if t<s0+0.3:
        p=clamp((t-s0)/0.3,0,1); sc=0.84+0.16*ease_out(p); a=p; dy=0
    elif t<s1:
        sc=1.0; a=1.0; dy=0
    else:
        p=clamp((t-s1)/0.35,0,1); sc=1.0; a=1-p; dy=int(-50*ease_out(p))
    cw2,ch2=int(card.width*sc),int(card.height*sc)
    cc=card.resize((cw2,ch2),Image.LANCZOS)
    if a<1:
        tmp=Image.new("RGBA",cc.size,(0,0,0,0)); cc=Image.blend(tmp,cc,clamp(a,0,1))
    layer=Image.new("RGBA",(W,H),(0,0,0,0))
    layer.alpha_composite(cc,(W//2-cw2//2, 360-ch2//2+dy))
    return layer

def subtitle(text, prog):
    layer=Image.new("RGBA",(W,H),(0,0,0,0)); d=ImageDraw.Draw(layer)
    size=64; f=font("Black",size)
    while d.textlength(text,font=f)>W-180 and size>40:
        size-=2; f=font("Black",size)
    a=clamp(prog,0,1)
    yoff=int((1-ease_out(a))*18)
    # render to temp for alpha blend
    tmp=Image.new("RGBA",(W,160),(0,0,0,0)); td=ImageDraw.Draw(tmp)
    td.text((W//2,80),text,font=f,fill=(255,255,255,255),anchor="mm",
            stroke_width=7,stroke_fill=(0,0,0,255))
    tmp2=Image.new("RGBA",tmp.size,(0,0,0,0)); tmp2=Image.blend(tmp2,tmp,a)
    layer.alpha_composite(tmp2,(0,H-150+yoff))
    return layer

def seg_prog(t,t0,t1,fade=0.3):
    """fade in/out envelope -> 0..1"""
    if t<t0 or t>t1: return 0.0
    return min((t-t0)/fade, (t1-t)/fade, 1.0)

def draw_prog(t,t0,fade=0.35):
    if t<t0: return 0.0
    return min((t-t0)/fade,1.0)

# ---------- build each act ----------
def build_act(act):
    name=act["name"]; (s0,s1)=act["src"]; dur=act["dur"]
    base=f"{TMP}/{name}_base.mp4"
    # trim + retime to dur, scale, 30fps
    src_len=s1-s0
    pts=dur/src_len
    vf=f"scale=1920:1080:force_original_aspect_ratio=increase,crop=1920:1080,setpts={pts:.5f}*PTS,fps={FPS}"
    run(f'ffmpeg -y -loglevel error -ss {s0} -to {s1} -i "{SRC}" -an -vf "{vf}" -r {FPS} -t {dur} '
        f'-c:v libx264 -crf 16 -pix_fmt yuv420p "{base}"')
    # overlay frames
    odir=f"{TMP}/{name}_ov"; os.makedirs(odir,exist_ok=True)
    nfr=int(dur*FPS)
    accent=act["accent"]
    for fi in range(nfr):
        t=fi/FPS
        L=Image.new("RGBA",(W,H),(0,0,0,0))
        # spotlights
        for (a0,a1,rect,ctext,side,acol) in act["spots"]:
            p=seg_prog(t,a0,a1,0.3)
            if p>0:
                pulse=0.5+0.5*math.sin((t-a0)*4.5)
                L.alpha_composite(glow_box(rect,acol,p,pulse))
                L.alpha_composite(callout(rect,side,ctext,acol,p))
        # scrim + subtitles
        L.alpha_composite(SCRIM)
        for (c0,c1,txt) in act["subs"]:
            p=seg_prog(t,c0,c1,0.3)
            if p>0: L.alpha_composite(subtitle(txt,p))
        # act label (in first 0.55s, out last 0.4s)
        pin=draw_prog(t,0.05,0.4); pout=1.0 if t<dur-0.4 else clamp((dur-t)/0.4,0,1)
        L.alpha_composite(act_label(act["label"],accent,pin,pout))
        # success card (act4)
        if "success" in act:
            s0,s1=act["success"]
            if s0<=t<=s1+0.4:
                L.alpha_composite(success_card(t,s0,s1,accent))
        # progress bar (global)
        gp=(act["g0"]+(act["g1"]-act["g0"])*(t/dur))/GLOBAL_TOTAL
        L.alpha_composite(progress_bar(gp,accent))
        L.save(f"{odir}/f{fi:04d}.png")
    # composite overlay onto base
    out=f"{OUT}/{name}.mp4"
    run(f'ffmpeg -y -loglevel error -i "{base}" -framerate {FPS} -i "{odir}/f%04d.png" '
        f'-filter_complex "[0:v][1:v]overlay=format=auto" -r {FPS} -t {dur} '
        f'-c:v libx264 -crf 17 -pix_fmt yuv420p "{out}"')
    print("built", out, f"{dur}s {nfr}f")

if __name__=="__main__":
    only=sys.argv[1] if len(sys.argv)>1 else None
    for a in ACTS:
        if only and a["name"]!=only: continue
        build_act(a)
