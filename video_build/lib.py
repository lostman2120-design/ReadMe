import os, math, random
from PIL import Image, ImageDraw, ImageFont, ImageFilter

FONTS="/home/user/ReadMe/video_build/fonts"
def font(w="Bold", size=48):
    m={"Regular":"Inter-Regular.ttf","Medium":"Inter-Medium.ttf","SemiBold":"Inter-SemiBold.ttf",
       "Bold":"Inter-Bold.ttf","ExtraBold":"Inter-ExtraBold.ttf","Black":"Inter-Black.ttf"}
    return ImageFont.truetype(os.path.join(FONTS,m[w]), size)

W,H=1920,1080

def rounded(size, radius, fill):
    im=Image.new("RGBA", size, (0,0,0,0))
    d=ImageDraw.Draw(im)
    d.rounded_rectangle([0,0,size[0]-1,size[1]-1], radius=radius, fill=fill)
    return im

def fit_cover(img, w, h):
    """scale+center-crop to exactly w,h"""
    iw,ih=img.size
    s=max(w/iw, h/ih)
    nw,nh=int(iw*s+0.5),int(ih*s+0.5)
    img=img.resize((nw,nh), Image.LANCZOS)
    x=(nw-w)//2; y=(nh-h)//2
    return img.crop((x,y,x+w,y+h))

def fit_contain(img, w, h, bg=(255,255,255,255)):
    iw,ih=img.size
    s=min(w/iw, h/ih)
    nw,nh=int(iw*s+0.5),int(ih*s+0.5)
    im=img.resize((nw,nh), Image.LANCZOS)
    canvas=Image.new("RGBA",(w,h),bg)
    canvas.paste(im,((w-nw)//2,(h-nh)//2))
    return canvas

def browser_card(screenshot, w, h, title="", accent=(15,118,110,255), cover=True):
    """A rounded browser-window card with a top chrome bar containing a title."""
    bar=64
    card=Image.new("RGBA",(w,h),(0,0,0,0))
    # window background (white) rounded
    bgrect=rounded((w,h), 22, (255,255,255,255))
    card.alpha_composite(bgrect)
    # top bar
    bar_img=Image.new("RGBA",(w,bar),(0,0,0,0))
    bd=ImageDraw.Draw(bar_img)
    bd.rounded_rectangle([0,0,w-1,bar+22], radius=22, fill=(244,247,251,255))
    card.alpha_composite(bar_img,(0,0))
    d=ImageDraw.Draw(card)
    # traffic lights
    for i,c in enumerate([(255,95,86),(255,189,46),(39,201,63)]):
        cx=26+i*26
        d.ellipse([cx,bar//2-7,cx+14,bar//2+7], fill=c)
    # title pill
    f=font("SemiBold",26)
    tw=d.textlength(title,font=f)
    pill_w=tw+72
    px=(w-pill_w)//2
    d.rounded_rectangle([px,bar//2-19,px+pill_w,bar//2+19], radius=19, fill=(255,255,255,255), outline=(219,227,239,255), width=2)
    # accent dot
    d.ellipse([px+18,bar//2-7,px+32,bar//2+7], fill=accent)
    d.text((px+44,bar//2), title, font=f, fill=(71,85,105,255), anchor="lm")
    # screenshot area
    inner_w,inner_h=w-0, h-bar
    shot = fit_cover(screenshot, inner_w, inner_h) if cover else fit_contain(screenshot, inner_w, inner_h)
    # mask bottom corners rounded
    mask=Image.new("L",(inner_w,inner_h),0)
    md=ImageDraw.Draw(mask)
    md.rounded_rectangle([0,0,inner_w-1,inner_h-1], radius=22, fill=255)
    md.rectangle([0,0,inner_w-1,30],fill=255)  # square top (under bar)
    card.paste(shot,(0,bar),mask)
    return card

def drop_shadow(img, blur=30, alpha=110, offset=(0,18)):
    base=Image.new("RGBA",(img.width+blur*4, img.height+blur*4),(0,0,0,0))
    sh=Image.new("RGBA", img.size, (0,0,0,0))
    a=img.split()[3].point(lambda p: alpha if p>0 else 0)
    shadow=Image.new("RGBA", img.size, (15,23,42,0))
    shadow.putalpha(a)
    base.alpha_composite(shadow,(blur*2+offset[0], blur*2+offset[1]))
    base=base.filter(ImageFilter.GaussianBlur(blur))
    base.alpha_composite(img,(blur*2,blur*2))
    return base

def cursor(draw, x, y, scale=1.0, color=(20,20,20), outline=(255,255,255)):
    """draw a classic arrow cursor with tip at (x,y)"""
    s=scale
    pts=[(0,0),(0,17),(4,13),(7,20),(10,19),(7,12),(13,12)]
    pts=[(x+px*s, y+py*s) for px,py in pts]
    draw.polygon(pts, fill=outline)
    pts2=[(x+px*s, y+py*s) for px,py in pts]
    draw.line(pts2+[pts2[0]], fill=color, width=max(1,int(2*s)))
    draw.polygon(pts, fill=color)
    draw.polygon(pts2, outline=outline)

def ease_out(t): return 1-(1-t)**3
def ease_in_out(t): return 3*t*t-2*t*t*t if 0<=t<=1 else (0 if t<0 else 1)
def clamp(v,a,b): return max(a,min(b,v))
