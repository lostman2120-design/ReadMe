import os, math, random
from PIL import Image, ImageDraw, ImageFilter
import sys
sys.path.insert(0,"/home/user/ReadMe/video_build")
from lib import *

random.seed(7)
OUT="/tmp/intro"; os.makedirs(OUT, exist_ok=True)
A="/home/user/ReadMe/video_build/assets/"
FPS=30; DUR=5.0; N=int(FPS*DUR)

# ---- pre-build cards ----
li_shot=Image.open(A+"li_crop.png").convert("RGBA")
hs_shot=Image.open(A+"hs_crop.png").convert("RGBA")
CARD_W,CARD_H=812,902
li_card=browser_card(li_shot, CARD_W, CARD_H, "LinkedIn", accent=(10,102,194,255))
hs_card=browser_card(hs_shot, CARD_W, CARD_H, "HubSpot", accent=(255,122,89,255))
li_sh=drop_shadow(li_card, blur=26, alpha=90)
hs_sh=drop_shadow(hs_card, blur=26, alpha=90)

# card anchor positions (top-left of the card image including shadow margin)
M=26*2
LI_X, LI_Y = 118-M, 150-M
HS_X, HS_Y = 990-M, 150-M
# field anchors (center) on the right card for chips to land
li_field=(LI_X+M+406, LI_Y+M+470)
hs_fields=[(HS_X+M+406, HS_Y+M+230+i*150) for i in range(4)]
CHIPS=["Full name","Job title","Company","Email"]

def bg_gradient():
    g=Image.new("RGBA",(W,H),(0,0,0,0))
    d=ImageDraw.Draw(g)
    for y in range(H):
        t=y/H
        r=int(238-18*t); gg=int(242-14*t); b=int(248-8*t)
        d.line([(0,y),(W,y)], fill=(r,gg,b,255))
    return g
BG=bg_gradient()

def draw_chip(layer, x, y, text, alpha=255, scale=1.0):
    d=ImageDraw.Draw(layer)
    f=font("Bold",int(30*scale))
    tw=d.textlength(text,font=f); pad=int(22*scale)
    w=tw+pad*2; h=int(54*scale)
    box=Image.new("RGBA",(int(w)+8,h+8),(0,0,0,0))
    bd=ImageDraw.Draw(box)
    bd.rounded_rectangle([4,4,w+3,h+3], radius=h//2, fill=(15,118,110,alpha))
    bd.text((4+pad,4+h//2), text, font=f, fill=(255,255,255,alpha), anchor="lm")
    layer.alpha_composite(box,(int(x-w/2), int(y-h/2)))

def draw_stamp(layer, x, y, text, col, rot, scale=1.0, alpha=255):
    f=font("ExtraBold",int(34*scale))
    tmp=Image.new("RGBA",(260,90),(0,0,0,0))
    d=ImageDraw.Draw(tmp)
    tw=d.textlength(text,font=f)
    d.rounded_rectangle([130-tw/2-16,18,130+tw/2+16,72], radius=10, outline=col+(alpha,), width=4)
    d.text((130,45), text, font=f, fill=col+(alpha,), anchor="mm")
    tmp=tmp.rotate(rot, expand=True, resample=Image.BICUBIC)
    layer.alpha_composite(tmp,(int(x-tmp.width/2),int(y-tmp.height/2)))

stamps=[]  # (x,y,text,col,rot,born_frame)

for fi in range(N):
    t=fi/FPS
    frame=BG.copy()
    over=Image.new("RGBA",(W,H),(0,0,0,0))  # content layer (gets shaken)
    d=ImageDraw.Draw(over)

    # shake intensity ramps through chaos, peaks at the X slam
    if t<2.0:
        shake=2+ t*4
    elif t<2.5:
        shake=14*(1-(t-2.0)/0.5)+2
    else:
        shake=1.2
    sx=random.uniform(-shake,shake); sy=random.uniform(-shake,shake)

    # paste the two cards
    over.alpha_composite(li_sh,(LI_X,LI_Y))
    over.alpha_composite(hs_sh,(HS_X,HS_Y))

    # connecting arrow between cards
    d.line([(LI_X+M+CARD_W-10, 600),(HS_X+M+10,600)], fill=(148,163,184,160), width=4)

    # stress headline (chaos phase): "Copy. Paste. Repeat." jittering
    if t<2.05:
        hj=random.randint(-3,3)
        hf=font("Black",58)
        d.text((W//2+hj, 64), "Copy.  Paste.  Repeat.", font=hf, fill=(220,38,38,255),
               anchor="mm", stroke_width=5, stroke_fill=(255,255,255,255))
        # wasted-time pill
        secs=int(t*47)
        pill=f"{secs//60:02d}:{secs%60:02d} wasted on manual entry"
        pf=font("Bold",26)
        pw=d.textlength(pill,font=pf)
        d.rounded_rectangle([W//2-pw/2-22,104,W//2+pw/2+22,150], radius=23, fill=(15,23,42,235))
        d.text((W//2,127), pill, font=pf, fill=(255,255,255,255), anchor="mm")

    # ---- chaotic copy-paste cycles (0..2.0s): 5 fast cycles ----
    if t<2.05:
        cycle_len=0.40
        ci=int(t/cycle_len)
        ct=(t-ci*cycle_len)/cycle_len  # 0..1 within cycle
        chip=CHIPS[ci%len(CHIPS)]
        dst=hs_fields[ci%4]
        src=li_field
        e=ease_in_out(clamp(ct,0,1))
        cx=src[0]+(dst[0]-src[0])*e
        cy=src[1]+(dst[1]-src[1])*e - math.sin(e*math.pi)*120
        # chip flying
        draw_chip(over, cx, cy, chip, alpha=255, scale=1.0)
        # on copy (start) add C stamp; on paste(end) add V stamp + flash
        if ct<0.12:
            draw_stamp(over, src[0]+random.randint(-40,40), src[1]+random.randint(-120,-40),
                       "Ctrl+C", (10,102,194), random.uniform(-12,12))
        if ct>0.86:
            # paste flash on right card field
            fl=Image.new("RGBA",(W,H),(0,0,0,0))
            fld=ImageDraw.Draw(fl)
            fld.rounded_rectangle([dst[0]-280,dst[1]-34,dst[0]+280,dst[1]+34], radius=16,
                                  fill=(39,201,99,120))
            over.alpha_composite(fl)
            stamps.append((HS_X+M+random.randint(120,560), HS_Y+M+random.randint(120,720),
                           "Ctrl+V",(255,122,89), random.uniform(-16,16), fi))
        # cursor follows chip
        cursor(d, cx-6, cy-6, scale=2.2)

    # accumulated paste stamps (jittering, persist)
    for (px,py,txt,col,rot,born) in stamps:
        age=fi-born
        a=int(clamp(255-age*4,40,255))
        jx=random.randint(-2,2); jy=random.randint(-2,2)
        draw_stamp(over, px+jx,py+jy, txt, col, rot, scale=0.9, alpha=a)

    # ---- RED X slam phase (2.0s -> 5s) ----
    if t>=1.95:
        # dim overlay
        dim_a=int(clamp((t-1.95)/0.3,0,1)*150)
        dim=Image.new("RGBA",(W,H),(8,11,20,dim_a))
        over.alpha_composite(dim)
        # red X strokes draw-on with overshoot
        def stroke(p0,p1,prog,width=46):
            prog=clamp(prog,0,1); e=ease_out(prog)
            x=p0[0]+(p1[0]-p0[0])*e; y=p0[1]+(p1[1]-p0[1])*e
            xl=Image.new("RGBA",(W,H),(0,0,0,0)); xd=ImageDraw.Draw(xl)
            xd.line([p0,(x,y)], fill=(229,30,30,255), width=width)
            # glow
            xl=xl.filter(ImageFilter.GaussianBlur(0))
            over.alpha_composite(xl)
        cx0,cy0=W//2,H//2; R=300
        stroke((cx0-R,cy0-R),(cx0+R,cy0+R), (t-2.05)/0.28)
        stroke((cx0+R,cy0-R),(cx0-R,cy0+R), (t-2.34)/0.28)
        # impact ring
        if 2.05<t<2.9:
            for (ct0,base) in [(2.33,0),(2.62,0)]:
                rp=(t-ct0)/0.35
                if 0<rp<1:
                    rr=int(80+260*rp); aa=int(180*(1-rp))
                    rl=Image.new("RGBA",(W,H),(0,0,0,0)); rd=ImageDraw.Draw(rl)
                    rd.ellipse([cx0-rr,cy0-rr,cx0+rr,cy0+rr], outline=(229,30,30,aa), width=8)
                    over.alpha_composite(rl)
        # OUTDATED stamp slams in ~2.7s
        if t>2.55:
            sp=clamp((t-2.55)/0.25,0,1)
            sc=1.0+ (1-ease_out(sp))*0.8
            draw_stamp(over, W//2+360, 250, "OUTDATED", (229,30,30), -14, scale=sc*1.4)

    # composite content with shake
    frame.alpha_composite(over,(int(sx),int(sy)))

    # ---- subtitle (bottom) appears with red X ----
    if t>2.3:
        sa=int(clamp((t-2.3)/0.3,0,1)*255)
        line="Still copy-pasting to HubSpot by hand?"
        sf=font("Black",64)
        fd=ImageDraw.Draw(frame)
        tw=fd.textlength(line,font=sf)
        # subtle pop scale
        fd.text((W//2, H-90), line, font=sf, fill=(255,255,255,sa),
                anchor="mm", stroke_width=7, stroke_fill=(0,0,0,sa))

    frame.convert("RGB").save(f"{OUT}/f{fi:04d}.png")

print("intro frames:", N)
