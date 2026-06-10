import os, math, random, sys
from PIL import Image, ImageDraw, ImageFilter
sys.path.insert(0,"/home/user/ReadMe/video_build")
from lib import font, browser_card, drop_shadow, cursor, ease_out, ease_in_out, clamp

random.seed(7)
VW,VH=1080,1920
OUT="/tmp/intro_v"; os.makedirs(OUT,exist_ok=True)
A="/home/user/ReadMe/video_build/assets/"
FPS=30; DUR=5.0; N=int(FPS*DUR)
BG=Image.open(A+"bg_v.png").convert("RGBA")

li_shot=Image.open(A+"li_crop.png").convert("RGBA")
hs_shot=Image.open(A+"hs_crop.png").convert("RGBA")
CW=1004
li_card=browser_card(li_shot, CW, 620, "LinkedIn", accent=(10,102,194,255))
hs_card=browser_card(hs_shot, CW, 706, "HubSpot", accent=(255,122,89,255))
li_sh=drop_shadow(li_card,blur=24,alpha=110)
hs_sh=drop_shadow(hs_card,blur=24,alpha=110)
M=24*2
LI_X,LI_Y=38-M,250-M
HS_X,HS_Y=38-M,952-M
li_field=(LI_X+M+CW//2, LI_Y+M+330)
hs_fields=[(HS_X+M+260+i*180, HS_Y+M+250+ (i%2)*150) for i in range(4)]
CHIPS=["Full name","Job title","Company","Email"]

def draw_chip(layer,x,y,text,scale=1.0,alpha=255):
    d=ImageDraw.Draw(layer); f=font("Bold",int(34*scale))
    tw=d.textlength(text,font=f); pad=int(24*scale); w=tw+pad*2; h=int(60*scale)
    box=Image.new("RGBA",(int(w)+8,h+8),(0,0,0,0)); bd=ImageDraw.Draw(box)
    bd.rounded_rectangle([4,4,w+3,h+3],radius=h//2,fill=(15,118,110,alpha))
    bd.text((4+pad,4+h//2),text,font=f,fill=(255,255,255,alpha),anchor="lm")
    layer.alpha_composite(box,(int(x-w/2),int(y-h/2)))

def draw_stamp(layer,x,y,text,col,rot,scale=1.0,alpha=255):
    f=font("ExtraBold",int(36*scale))
    tmp=Image.new("RGBA",(300,100),(0,0,0,0)); d=ImageDraw.Draw(tmp)
    tw=d.textlength(text,font=f)
    d.rounded_rectangle([150-tw/2-18,20,150+tw/2+18,80],radius=10,outline=col+(alpha,),width=5)
    d.text((150,50),text,font=f,fill=col+(alpha,),anchor="mm")
    tmp=tmp.rotate(rot,expand=True,resample=Image.BICUBIC)
    layer.alpha_composite(tmp,(int(x-tmp.width/2),int(y-tmp.height/2)))

stamps=[]
for fi in range(N):
    t=fi/FPS
    frame=BG.copy()
    over=Image.new("RGBA",(VW,VH),(0,0,0,0)); d=ImageDraw.Draw(over)
    shake=(2+t*4) if t<2.0 else (14*(1-(t-2.0)/0.5)+2 if t<2.5 else 1.2)
    sx=random.uniform(-shake,shake); sy=random.uniform(-shake,shake)
    over.alpha_composite(li_sh,(LI_X,LI_Y))
    over.alpha_composite(hs_sh,(HS_X,HS_Y))
    # downward flow arrow between cards
    d.line([(VW//2,884),(VW//2,944)],fill=(148,163,184,170),width=5)
    d.polygon([(VW//2-14,936),(VW//2+14,936),(VW//2,956)],fill=(148,163,184,180))

    # headline
    if t<2.05:
        hj=random.randint(-3,3)
        d.text((VW//2+hj,96),"Copy.  Paste.  Repeat.",font=font("Black",62),fill=(220,38,38,255),
               anchor="mm",stroke_width=5,stroke_fill=(255,255,255,255))
        secs=int(t*47); pill=f"{secs//60:02d}:{secs%60:02d} wasted on manual entry"
        pf=font("Bold",30); pw=d.textlength(pill,font=pf)
        d.rounded_rectangle([VW//2-pw/2-24,150,VW//2+pw/2+24,202],radius=26,fill=(15,23,42,235))
        d.text((VW//2,176),pill,font=pf,fill=(255,255,255,255),anchor="mm")
        # copy-paste cycles
        cl=0.40; ci=int(t/cl); ct=(t-ci*cl)/cl
        chip=CHIPS[ci%4]; src=li_field; dst=hs_fields[ci%4]
        e=ease_in_out(clamp(ct,0,1))
        cx=src[0]+(dst[0]-src[0])*e; cy=src[1]+(dst[1]-src[1])*e - math.sin(e*math.pi)*60
        draw_chip(over,cx,cy,chip)
        if ct<0.12: draw_stamp(over,src[0]+random.randint(-60,60),src[1]+random.randint(-40,40),"Ctrl+C",(10,102,194),random.uniform(-12,12))
        if ct>0.86:
            fl=Image.new("RGBA",(VW,VH),(0,0,0,0)); fd=ImageDraw.Draw(fl)
            fd.rounded_rectangle([dst[0]-220,dst[1]-36,dst[0]+220,dst[1]+36],radius=16,fill=(39,201,99,120))
            over.alpha_composite(fl)
            stamps.append((HS_X+M+random.randint(150,820),HS_Y+M+random.randint(120,640),"Ctrl+V",(255,122,89),random.uniform(-16,16),fi))
        cursor(d,cx-6,cy-6,scale=2.4)
    for (px,py,txt,col,rot,born) in stamps:
        age=fi-born; a=int(clamp(255-age*4,40,255))
        draw_stamp(over,px+random.randint(-2,2),py+random.randint(-2,2),txt,col,rot,scale=0.95,alpha=a)

    # RED X phase
    if t>=1.95:
        dim_a=int(clamp((t-1.95)/0.3,0,1)*150)
        over.alpha_composite(Image.new("RGBA",(VW,VH),(8,11,20,dim_a)))
        cx0,cy0=VW//2,(250+1658)//2; R=440
        def stroke(p0,p1,prog,width=46):
            prog=clamp(prog,0,1); e=ease_out(prog)
            x=p0[0]+(p1[0]-p0[0])*e; y=p0[1]+(p1[1]-p0[1])*e
            xl=Image.new("RGBA",(VW,VH),(0,0,0,0)); ImageDraw.Draw(xl).line([p0,(x,y)],fill=(229,30,30,255),width=width)
            over.alpha_composite(xl)
        stroke((cx0-300,cy0-R),(cx0+300,cy0+R),(t-2.05)/0.28)
        stroke((cx0+300,cy0-R),(cx0-300,cy0+R),(t-2.34)/0.28)
        if t>2.55:
            sp=clamp((t-2.55)/0.25,0,1); sc=1.0+(1-ease_out(sp))*0.8
            draw_stamp(over,VW//2+260,300,"OUTDATED",(229,30,30),-14,scale=sc*1.5)

    frame.alpha_composite(over,(int(sx),int(sy)))
    if t>2.3:
        sa=int(clamp((t-2.3)/0.3,0,1)*255)
        f=font("Black",66); maxw=VW-80
        # wrap subtitle
        line="Still copy-pasting to HubSpot by hand?"
        words=line.split(); lines=[]; cur=""
        fd=ImageDraw.Draw(frame)
        for w in words:
            tt=(cur+" "+w).strip()
            if fd.textlength(tt,font=f)<=maxw: cur=tt
            else: lines.append(cur); cur=w
        lines.append(cur)
        lh=84; y0=1700-lh*len(lines)//2
        for i,ln in enumerate(lines):
            fd.text((VW//2,y0+i*lh),ln,font=f,fill=(255,255,255,sa),anchor="mm",stroke_width=7,stroke_fill=(0,0,0,sa))
    frame.convert("RGB").save(f"{OUT}/f{fi:04d}.png")
print("intro_v frames",N)
