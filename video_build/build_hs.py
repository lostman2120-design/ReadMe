import os, subprocess, sys
from PIL import Image, ImageDraw, ImageFont, ImageFilter

W,H=1920,1080; FPS=30
FD="/home/user/ReadMe/video_build/fonts"
ST="/home/user/ReadMe/video_build/hs_still"
OUT="/home/user/ReadMe/video_build/out_hs"; TMP="/tmp/hsb"
os.makedirs(OUT,exist_ok=True); os.makedirs(TMP,exist_ok=True)
ORANGE=(255,122,89); BLUE=(40,120,210); INK=(11,31,51)

def f(w,s):
    m={"R":"Inter-Regular.ttf","M":"Inter-Medium.ttf","SB":"Inter-SemiBold.ttf",
       "B":"Inter-Bold.ttf","EB":"Inter-ExtraBold.ttf"}
    return ImageFont.truetype(os.path.join(FD,m[w]),s)
def eo(t): return 1-(1-t)**3
def clamp(v,a,b): return max(a,min(b,v))
def run(c):
    r=subprocess.run(c,shell=True,capture_output=True,text=True)
    if r.returncode!=0: print("ERR",c[:160]);print(r.stderr[-1200:]);raise SystemExit(1)

def lerp(a,b,t): return a+(b-a)*t

def caption_lower(main,sub,accent,prog,pos="bottom"):
    L=Image.new("RGBA",(W,H),(0,0,0,0)); d=ImageDraw.Draw(L)
    a=clamp(prog,0,1)
    BH=250
    band=Image.new("RGBA",(W,BH),(0,0,0,0)); bd=ImageDraw.Draw(band)
    for i in range(BH):
        al=int(232*(i/BH)**0.7); bd.line([(0,i),(W,i)],fill=(8,20,34,al))
    if pos=="top":
        band=band.transpose(Image.FLIP_TOP_BOTTOM); L.alpha_composite(band,(0,0))
        baseY=70; mf=f("EB",52); sf=f("M",29)
        while d.textlength(main,font=mf)>W-300 and mf.size>34: mf=f("EB",mf.size-2)
        d.rounded_rectangle([86,baseY+2,96,baseY+94],radius=5,fill=accent+(255,))
        d.text((120,baseY-4),main,font=mf,fill=(255,255,255,255))
        if sub: d.text((122,baseY+74),sub,font=sf,fill=(190,212,233,255))
    else:
        L.alpha_composite(band,(0,H-BH))
        bx=86; mf=f("EB",52); sf=f("M",29)
        while d.textlength(main,font=mf)>W-300 and mf.size>34: mf=f("EB",mf.size-2)
        d.rounded_rectangle([bx,H-150,bx+10,H-58],radius=5,fill=accent+(255,))
        d.text((bx+34,H-156),main,font=mf,fill=(255,255,255,255))
        if sub: d.text((bx+36,H-78),sub,font=sf,fill=(190,212,233,255))
    if a<1: L=Image.blend(Image.new("RGBA",(W,H),(0,0,0,0)),L,a)
    return L

def caption_title(main,sub,prog,appear):
    L=Image.new("RGBA",(W,H),(0,0,0,0)); a=clamp(prog,0,1)
    sc=Image.new("RGBA",(W,H),(7,18,31,int(150*a))); L.alpha_composite(sc)
    d=ImageDraw.Draw(L)
    # version pill
    pf=f("B",26); pill="v0.2.3"
    pw=d.textlength(pill,font=pf)
    yo=int((1-eo(clamp(appear,0,1)))*24)
    cx=W//2; py=350+yo
    d.rounded_rectangle([cx-pw/2-22,py-8,cx+pw/2+22,py+40],radius=24,fill=(255,122,89,235))
    d.text((cx,py+16),pill,font=pf,fill=(255,255,255,255),anchor="mm")
    mf=f("EB",74); sf=f("M",38)
    d.text((cx,py+120),main,font=mf,fill=(255,255,255,255),anchor="mm",stroke_width=1,stroke_fill=(7,18,31,255))
    d.rounded_rectangle([cx-60,py+178,cx+60,py+184],radius=3,fill=(255,122,89,255))
    d.text((cx,py+228),sub,font=sf,fill=(206,224,240,255),anchor="mm")
    if a<1: L=Image.blend(Image.new("RGBA",(W,H),(0,0,0,0)),L,a)
    return L

def env(t,dur,fi=0.45,fo=0.45): return clamp(min(t/fi,(dur-t)/fo,1.0),0,1)

SEG=[
 dict(name="s1_title", still="titlebg", dur=5.0, kind="full",
      c0=(120,40,1680,945), c1=(60,20,1800,1013), cap="title",
      main="ICP-based scoring", sub="Not a generic AI lead score."),
 dict(name="s2_icpset", still="icpset", dur=8.0, kind="crop",
      c0=(0,300,1716,965), c1=(0,690,1716,965), cap="lower", accent=BLUE,
      main="Define your ICP once",
      sub="Roles · industries · offer · pain points · preferred tone"),
 dict(name="s3_icpctx", still="icpctx", dur=11.0, kind="crop",
      c0=(1486,330,430,242), c1=(1486,600,430,242), cap="lower", accent=BLUE,
      main="The sidebar shows what the score is based on",
      sub="Your ICP: roles, industries, company size, offer, pain points, tone"),
 dict(name="s4_analyze", still="analyze", dur=5.0, kind="full",
      c0=(40,20,1840,1035), c1=(700,40,1180,664), cap="lower", accent=BLUE,
      main="Analyze a visible LinkedIn profile",
      sub="Scored against your saved ICP"),
 dict(name="s5_score", still="score", dur=11.0, kind="crop",
      c0=(1486,300,430,242), c1=(1486,505,430,242), cap="lower", accent=ORANGE,
      main="ICP Fit Score",
      sub="Profile context: High  ·  Confidence: High"),
 dict(name="s6_reason", still="reason", dur=9.0, kind="crop",
      c0=(1486,236,430,242), c1=(1486,520,430,242), cap="lower", accent=ORANGE,
      main="See the persona, pain points & outreach angle",
      sub="Role fit · industry relevance · company context · outreach risk"),
 dict(name="s7_dm", still="dm", dur=11.0, kind="crop",
      c0=(1486,498,430,242), c1=(1486,572,430,242), cap="lower", accent=ORANGE,
      main="Personalized DM draft",
      sub="Uses your ICP + visible profile context  ·  manual review"),
 dict(name="s8_hub", still="hub", dur=6.0, kind="crop",
      c0=(1486,838,430,242), c1=(1499,840,406,228), cap="lower", cappos="top", accent=ORANGE,
      main="Save the context to HubSpot",
      sub="Add to HubSpot · Create HubSpot note · Create follow-up task"),
 dict(name="s9_end", still="endcard", dur=4.0, kind="full",
      c0=(40,22,1840,1035), c1=(0,0,1920,1080), cap="none"),
]

# short cutdown variants (shorter durations; same crops/captions)
def short_of(name,dur,**ov):
    base=[dict(s) for s in SEG if s["name"]==name][0]
    base=dict(base); base["name"]=name+"_sh"; base["dur"]=dur; base.update(ov)
    return base
SEG_SHORT=[
 short_of("s1_title",3.0),
 short_of("s3_icpctx",8.0),
 short_of("s5_score",7.0),
 short_of("s7_dm",8.0),
 short_of("s8_hub",5.0),
 short_of("s9_end",3.0),
]
SEG=SEG+SEG_SHORT

def build(seg):
    name=seg["name"]; dur=seg["dur"]; nfr=int(dur*FPS)
    img=Image.open(f"{ST}/{seg['still']}.png").convert("RGB")
    IW,IH=img.size
    odir=f"{TMP}/{name}"; os.makedirs(odir,exist_ok=True)
    c0=seg["c0"]; c1=seg["c1"]
    for fi in range(nfr):
        p=fi/max(nfr-1,1); pe=eo(p)
        bx=lerp(c0[0],c1[0],pe); by=lerp(c0[1],c1[1],pe)
        bw=lerp(c0[2],c1[2],pe); bh=lerp(c0[3],c1[3],pe)
        bx=clamp(bx,0,IW-bw); by=clamp(by,0,IH-bh)
        crop=img.crop((round(bx),round(by),round(bx+bw),round(by+bh))).resize((W,H),Image.LANCZOS)
        fr=crop.convert("RGBA")
        t=fi/FPS; e=env(t,dur)
        if seg["cap"]=="title":
            ap=clamp(t/0.7,0,1); ov=caption_title(seg["main"],seg["sub"],e,ap)
        elif seg["cap"]=="lower":
            ov=caption_lower(seg["main"],seg.get("sub"),seg["accent"],e,seg.get("cappos","bottom"))
        else: ov=None
        if ov is not None: fr.alpha_composite(ov)
        fr.convert("RGB").save(f"{odir}/f{fi:04d}.png")
    run(f'ffmpeg -y -loglevel error -framerate {FPS} -i "{odir}/f%04d.png" -t {dur} '
        f'-r {FPS} -c:v libx264 -crf 17 -pix_fmt yuv420p "{OUT}/{name}.mp4"')
    print("built",name)

if __name__=="__main__":
    only=sys.argv[1:]
    for s in SEG:
        if only and s["name"] not in only: continue
        build(s)
