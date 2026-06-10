import os, math, subprocess, sys
from PIL import Image, ImageDraw, ImageFilter
sys.path.insert(0,"/home/user/ReadMe/video_build")
import lib
from lib import font, ease_out, clamp, rounded, drop_shadow
from build_demo import ACTS, ACCENT, ACCENT2, SRC

VW,VH=1080,1920
lib.W, lib.H = VW, VH          # not used by our own funcs but keep consistent
FPS=30
OUT="/home/user/ReadMe/video_build/out"; TMP="/tmp/vert"; os.makedirs(TMP,exist_ok=True)
GLOBAL_TOTAL=60.0

# footage panel box
PX,PY,PW,PH = 32,470,1016,762
# per-act crop of the 1920x1080 source into 4:3 (1440x1080)
VCROP={"act1":(480,0,1440,1080),"act2":(480,0,1440,1080),
       "act3":(480,0,1440,1080),"act4":(0,0,1440,1080)}
S=PW/1440.0  # scale source->panel

BG=Image.open("assets/bg_v.png").convert("RGBA")

def run(cmd):
    r=subprocess.run(cmd,shell=True,capture_output=True,text=True)
    if r.returncode!=0:
        print("ERR",cmd); print(r.stderr[-1500:]); raise SystemExit(1)

def xform(rect, cx0, cy0):
    x,y,w,h=rect
    return (PX+(x-cx0)*S, PY+(y-cy0)*S, w*S, h*S)

def seg(t,t0,t1,fade=0.3):
    if t<t0 or t>t1: return 0.0
    return min((t-t0)/fade,(t1-t)/fade,1.0)
def rin(t,t0,fade=0.35):
    return 0.0 if t<t0 else min((t-t0)/fade,1.0)

def glow_box(rect, accent, prog, pulse):
    x,y,w,h=rect; pad=8
    rx0,ry0,rx1,ry1=x-pad,y-pad,x+w+pad,y+h+pad
    a=int(255*clamp(prog,0,1))
    L=Image.new("RGBA",(VW,VH),(0,0,0,0))
    glow=Image.new("RGBA",(VW,VH),(0,0,0,0)); gd=ImageDraw.Draw(glow)
    gd.rounded_rectangle([rx0,ry0,rx1,ry1],radius=12,outline=accent+(int(a*0.9),),width=int(5+3*pulse))
    glow=glow.filter(ImageFilter.GaussianBlur(8))
    L.alpha_composite(glow)
    d=ImageDraw.Draw(L)
    d.rounded_rectangle([rx0,ry0,rx1,ry1],radius=12,outline=accent+(a,),width=4)
    return L

def callout(rect, side, text, accent, prog):
    x,y,w,h=rect
    f=font("Bold",30)
    tmp=Image.new("RGBA",(10,10)); td=ImageDraw.Draw(tmp)
    tw=td.textlength(text,font=f); padx=20
    bw,bh=int(tw+padx*2),58
    chip=Image.new("RGBA",(bw,bh),(0,0,0,0)); cd=ImageDraw.Draw(chip)
    cd.rounded_rectangle([0,0,bw-1,bh-1],radius=bh//2,fill=accent+(255,))
    cd.text((bw//2,bh//2),text,font=f,fill=(255,255,255,255),anchor="mm")
    gap=20
    if side=="left": cx=x-gap-bw; cy=y+h//2-bh//2; tri=[(cx+bw,cy+bh//2-9),(cx+bw,cy+bh//2+9),(cx+bw+11,cy+bh//2)]
    elif side=="bottom": cx=x+w//2-bw//2; cy=y+h+gap+10; tri=[(cx+bw//2-9,cy),(cx+bw//2+9,cy),(cx+bw//2,cy-11)]
    elif side=="top": cx=x+w//2-bw//2; cy=y-gap-bh-10; tri=[(cx+bw//2-9,cy+bh),(cx+bw//2+9,cy+bh),(cx+bw//2,cy+bh+11)]
    else: cx=x+w+gap; cy=y+h//2-bh//2; tri=[(cx,cy+bh//2-9),(cx,cy+bh//2+9),(cx-11,cy+bh//2)]
    cx=int(clamp(cx,8,VW-bw-8))
    L=Image.new("RGBA",(VW,VH),(0,0,0,0)); d=ImageDraw.Draw(L)
    off=int((1-ease_out(clamp(prog,0,1)))*36); a=clamp(prog,0,1)
    dx=off if side=="left" else (-off if side=="right" else 0)
    dy=off if side=="top" else (-off if side=="bottom" else 0)
    d.polygon([(px+dx,py+dy) for px,py in tri],fill=accent+(int(255*a),))
    cc=Image.new("RGBA",chip.size,(0,0,0,0)); cc=Image.blend(cc,chip,a)
    L.alpha_composite(cc,(int(cx+dx),int(cy+dy)))
    return L

def step_label(text, accent, pin, pout):
    f=font("ExtraBold",38); fn=font("Black",38)
    parts=text.split("   "); num=parts[0]; nm=parts[1] if len(parts)>1 else ""
    L=Image.new("RGBA",(VW,VH),(0,0,0,0)); d=ImageDraw.Draw(L)
    numw=d.textlength(num,font=fn); nw=d.textlength(nm,font=f)
    bw=int(numw+nw+30+34+24); bh=72
    a=clamp(min(pin,pout),0,1); off=int((1-ease_out(clamp(pin,0,1)))*-50)
    chip=Image.new("RGBA",(bw,bh),(0,0,0,0)); cd=ImageDraw.Draw(chip)
    cd.rounded_rectangle([0,0,bw-1,bh-1],radius=16,fill=(10,15,25,235))
    cd.rounded_rectangle([0,0,int(numw)+30,bh-1],radius=16,fill=accent+(255,))
    cd.rectangle([int(numw)+16,0,int(numw)+30,bh-1],fill=accent+(255,))
    cd.text((15,bh//2),num,font=fn,fill=(255,255,255,255),anchor="lm")
    cd.text((int(numw)+30+18,bh//2),nm,font=f,fill=(255,255,255,255),anchor="lm")
    c2=Image.new("RGBA",chip.size,(0,0,0,0)); c2=Image.blend(c2,chip,a)
    L.alpha_composite(c2,((VW-bw)//2+off,300))
    return L

def wrap(text,f,maxw,d):
    words=text.split(); lines=[]; cur=""
    for w in words:
        t=(cur+" "+w).strip()
        if d.textlength(t,font=f)<=maxw: cur=t
        else: lines.append(cur); cur=w
    if cur: lines.append(cur)
    return lines

def subtitle(text,prog):
    L=Image.new("RGBA",(VW,VH),(0,0,0,0)); d=ImageDraw.Draw(L)
    size=74; f=font("Black",size); maxw=VW-90
    lines=wrap(text,f,maxw,d)
    while len(lines)>2 and size>48:
        size-=3; f=font("Black",size); lines=wrap(text,f,maxw,d)
    a=clamp(prog,0,1); yoff=int((1-ease_out(a))*22)
    lh=size+18; total=lh*len(lines); y0=1500-total//2
    tmp=Image.new("RGBA",(VW,total+40),(0,0,0,0)); td=ImageDraw.Draw(tmp)
    for i,ln in enumerate(lines):
        td.text((VW//2,20+i*lh+lh//2),ln,font=f,fill=(255,255,255,255),anchor="mm",
                stroke_width=7,stroke_fill=(0,0,0,255))
    t2=Image.new("RGBA",tmp.size,(0,0,0,0)); t2=Image.blend(t2,tmp,a)
    L.alpha_composite(t2,(0,y0-20+yoff))
    return L

def progress_bar(gp,accent):
    L=Image.new("RGBA",(VW,VH),(0,0,0,0)); d=ImageDraw.Draw(L)
    d.rectangle([0,0,VW,10],fill=(255,255,255,40))
    d.rectangle([0,0,int(VW*clamp(gp,0,1)),10],fill=accent+(235,))
    return L

def panel_frame():
    L=Image.new("RGBA",(VW,VH),(0,0,0,0)); d=ImageDraw.Draw(L)
    d.rounded_rectangle([PX-3,PY-3,PX+PW+2,PY+PH+2],radius=10,outline=(255,255,255,55),width=3)
    return L
PFRAME=panel_frame()

def success_card(t,s0,s1,accent):
    cw,ch=940,250
    card=Image.new("RGBA",(cw,ch),(0,0,0,0)); d=ImageDraw.Draw(card)
    d.rounded_rectangle([0,0,cw-1,ch-1],radius=30,fill=(255,255,255,255))
    cy=ch//2; cxx=120; r=62
    d.ellipse([cxx-r,cy-r,cxx+r,cy+r],fill=(34,197,94,255))
    d.line([(cxx-28,cy+4),(cxx-6,cy+26),(cxx+32,cy-22)],fill=(255,255,255,255),width=13,joint="curve")
    d.text((cxx+r+30,cy-30),"Created in HubSpot",font=font("ExtraBold",48),fill=(15,23,42,255),anchor="lm")
    d.text((cxx+r+30,cy+28),"Name · title · company · URL — synced",font=font("Medium",28),fill=(100,116,139,255),anchor="lm")
    card=drop_shadow(card,blur=30,alpha=130,offset=(0,16))
    if t<s0+0.3: p=clamp((t-s0)/0.3,0,1); sc=0.84+0.16*ease_out(p); a=p; dy=0
    elif t<s1: sc=1.0;a=1.0;dy=0
    else: p=clamp((t-s1)/0.35,0,1); sc=1.0;a=1-p;dy=int(-50*ease_out(p))
    cw2,ch2=int(card.width*sc),int(card.height*sc); cc=card.resize((cw2,ch2),Image.LANCZOS)
    if a<1:
        tmp=Image.new("RGBA",cc.size,(0,0,0,0)); cc=Image.blend(tmp,cc,clamp(a,0,1))
    L=Image.new("RGBA",(VW,VH),(0,0,0,0))
    L.alpha_composite(cc,(VW//2-cw2//2, PY+PH//2-ch2//2+dy))
    return L

def build_act(act):
    name=act["name"]; (s0,s1)=act["src"]; dur=act["dur"]; accent=act["accent"]
    cx0,cy0,cw,ch=VCROP[name]
    base=f"{TMP}/{name}_base.mp4"
    pts=dur/(s1-s0)
    fg=f"[1:v]crop={cw}:{ch}:{cx0}:{cy0},setpts={pts:.5f}*PTS,fps={FPS},scale={PW}:{PH}[fg]"
    run(f'ffmpeg -y -loglevel error -loop 1 -i assets/bg_v.png -ss {s0} -to {s1} -i "{SRC}" '
        f'-filter_complex "{fg};[0:v][fg]overlay={PX}:{PY}:shortest=1[v]" -map "[v]" -t {dur} -r {FPS} '
        f'-c:v libx264 -crf 17 -pix_fmt yuv420p "{base}"')
    odir=f"{TMP}/{name}_ov"; os.makedirs(odir,exist_ok=True)
    nfr=int(dur*FPS)
    for fi in range(nfr):
        t=fi/FPS
        L=Image.new("RGBA",(VW,VH),(0,0,0,0))
        L.alpha_composite(PFRAME)
        for (a0,a1,rect,ctext,sideh,acol) in act["spots"]:
            p=seg(t,a0,a1,0.3)
            if p>0:
                r=xform(rect,cx0,cy0)
                pulse=0.5+0.5*math.sin((t-a0)*4.5)
                # choose callout side that stays on-panel
                side="bottom" if (r[1]-PY)<PH*0.5 else "top"
                if name=="act4": side="bottom"
                L.alpha_composite(glow_box(r,acol,p,pulse))
                L.alpha_composite(callout(r,side,ctext,acol,p))
        if "success" in act:
            ss0,ss1=act["success"]
            if ss0<=t<=ss1+0.4: L.alpha_composite(success_card(t,ss0,ss1,accent))
        for (c0,c1,txt) in act["subs"]:
            p=seg(t,c0,c1,0.3)
            if p>0: L.alpha_composite(subtitle(txt,p))
        pin=rin(t,0.05,0.4); pout=1.0 if t<dur-0.4 else clamp((dur-t)/0.4,0,1)
        L.alpha_composite(step_label(act["label"],accent,pin,pout))
        gp=(act["g0"]+(act["g1"]-act["g0"])*(t/dur))/GLOBAL_TOTAL
        L.alpha_composite(progress_bar(gp,accent))
        L.save(f"{odir}/f{fi:04d}.png")
    out=f"{OUT}/{name}_v.mp4"
    run(f'ffmpeg -y -loglevel error -i "{base}" -framerate {FPS} -i "{odir}/f%04d.png" '
        f'-filter_complex "[0:v][1:v]overlay=format=auto" -r {FPS} -t {dur} -c:v libx264 -crf 17 -pix_fmt yuv420p "{out}"')
    print("built",out)

if __name__=="__main__":
    only=sys.argv[1] if len(sys.argv)>1 else None
    for a in ACTS:
        if only and a["name"]!=only: continue
        build_act(a)
