import numpy as np, wave, struct

SR=44100
DUR=58.0
N=int(SR*DUR)
t=np.arange(N)/SR
L=np.zeros(N); R=np.zeros(N)

def env(start,length,a=0.01,d=0.1,s=0.7,r=0.2,sus=None):
    """ADSR envelope array aligned to global t, returns full-length array"""
    e=np.zeros(N)
    i0=int(start*SR); i1=int((start+length)*SR); i1=min(i1,N)
    if i0>=N: return e
    n=i1-i0
    seg=np.zeros(n)
    ai=int(a*SR); di=int(d*SR); ri=int(r*SR)
    ai=min(ai,n)
    if ai>0: seg[:ai]=np.linspace(0,1,ai)
    rest=n-ai
    if rest>0:
        di2=min(di,rest)
        if di2>0: seg[ai:ai+di2]=np.linspace(1,s,di2)
        seg[ai+di2:]=s
    # release at the end
    if ri>0 and n>ri:
        seg[-ri:]*=np.linspace(1,0,ri)
    e[i0:i1]=seg
    return e

def tone(freq, start, length, amp, a=0.01,d=0.1,s=0.7,r=0.2, partials=((1,1.0),)):
    e=env(start,length,a,d,s,r)
    w=np.zeros(N)
    for mult,pa in partials:
        w+=pa*np.sin(2*np.pi*freq*mult*t)
    return amp*e*w

def pluck(freq,start,amp,length=0.5,pan=0.0):
    i0=int(start*SR); i1=min(int((start+length)*SR),N)
    if i0>=N: return
    n=i1-i0; tt=np.arange(n)/SR
    e=np.exp(-tt*6.0)
    w=(np.sin(2*np.pi*freq*tt)+0.4*np.sin(2*np.pi*freq*2*tt)+0.2*np.sin(2*np.pi*freq*3*tt))
    sig=amp*e*w
    lg=0.5*(1-pan); rg=0.5*(1+pan)
    L[i0:i1]+=sig*lg*2; R[i0:i1]+=sig*rg*2

def padchord(freqs,start,length,amp):
    out=np.zeros(N)
    for f in freqs:
        out+=tone(f,start,length,amp,a=0.25,d=0.3,s=0.85,r=0.5)
        out+=tone(f*2,start,length,amp*0.25,a=0.3,d=0.3,s=0.8,r=0.5)
    L[:]+=out; R[:]+=out

def whoosh(center,amp=0.5,width=0.5):
    i0=int((center-width)*SR); i1=int((center+width)*SR)
    i0=max(0,i0); i1=min(N,i1)
    n=i1-i0; tt=np.linspace(-1,1,n)
    e=np.exp(-(tt**2)*6)
    noise=np.random.randn(n)
    # simple moving-average lowpass that opens up (bandpass-ish sweep via cumulative)
    sig=noise*e
    # emphasize sweep
    sweep=np.sin(2*np.pi*(200+ (tt+1)*1500)*np.arange(n)/SR)*0.0
    sig=sig*amp
    L[i0:i1]+=sig; R[i0:i1]+=sig*0.95

def impact(center,amp=0.9):
    i0=int(center*SR);
    n=int(0.6*SR); i1=min(N,i0+n)
    if i0>=N: return
    n=i1-i0; tt=np.arange(n)/SR
    e=np.exp(-tt*7)
    boom=np.sin(2*np.pi*(60*np.exp(-tt*3))*tt)  # falling sub
    body=np.sin(2*np.pi*90*tt)*np.exp(-tt*9)
    noise=np.random.randn(n)*np.exp(-tt*25)*0.5
    sig=amp*e*(boom*0.9+body*0.5)+amp*noise
    L[i0:i1]+=sig; R[i0:i1]+=sig

def tick(center,amp=0.25):
    i0=int(center*SR); n=int(0.04*SR); i1=min(N,i0+n)
    if i0>=N: return
    tt=np.arange(i1-i0)/SR; e=np.exp(-tt*120)
    sig=amp*e*(np.sin(2*np.pi*1800*tt)+0.5*np.random.randn(i1-i0))
    L[i0:i1]+=sig; R[i0:i1]+=sig

def bell(freq,start,amp=0.5,length=2.2):
    i0=int(start*SR); i1=min(int((start+length)*SR),N)
    if i0>=N: return
    n=i1-i0; tt=np.arange(n)/SR
    e=np.exp(-tt*2.2)
    parts=[(1,1),(2.01,0.6),(2.99,0.4),(4.2,0.25),(5.4,0.15)]
    w=sum(pa*np.sin(2*np.pi*freq*m*tt) for m,pa in parts)
    sig=amp*e*w
    L[i0:i1]+=sig; R[i0:i1]+=sig

# ---------------- INTRO 0-4.6 : tension ----------------
# low pulsing A minor drone
for k in range(9):
    st=0.05+k*0.22
    a=0.18+0.02*k
    L[:]+=tone(110,st,0.2,a,a=0.005,d=0.05,s=0.6,r=0.1)  # A2 pulse
    R[:]+=tone(110,st,0.2,a,a=0.005,d=0.05,s=0.6,r=0.1)
# dissonant high shimmer rising
L[:]+=tone(440,0.2,1.9,0.05,a=1.5,d=0.2,s=0.6,r=0.3)
R[:]+=tone(523.25,0.2,1.9,0.05,a=1.5,d=0.2,s=0.6,r=0.3)
# accelerating ticks (manual copy-paste clock)
ti=0.1
while ti<1.95:
    tick(ti,0.22)
    ti+=max(0.07, 0.22-(ti*0.08))
# impact at red X slam (~2.05 and 2.34)
impact(2.05,0.95); impact(2.40,0.7)
# tense sustained chord 2.1-4.6 (Am add) low
for f in [110,130.81,164.81,220]:
    L[:]+=tone(f,2.1,2.5,0.06,a=0.3,d=0.4,s=0.7,r=0.6)
    R[:]+=tone(f,2.1,2.5,0.06,a=0.3,d=0.4,s=0.7,r=0.6)
# riser into transition 3.6-4.6
ri0=int(3.6*SR); ri1=int(4.62*SR); n=ri1-ri0; tt=np.linspace(0,1,n)
riser=np.sin(2*np.pi*(200+tt*1200)*np.arange(n)/SR)*(tt**2)*0.18
L[ri0:ri1]+=riser; R[ri0:ri1]+=riser

# ---------------- SOLUTION whoosh at 4.6 ----------------
whoosh(4.6,0.6,0.45)

# ---------------- DEMO bed 4.6-53 : light major ----------------
prog=[("C",[130.81,164.81,196.00]),("G",[196.00,246.94,293.66]),
      ("Am",[220.00,261.63,329.63]),("F",[174.61,220.00,261.63])]
arp_notes={"C":[261.63,329.63,392.00,523.25],"G":[392.00,293.66,493.88,392.00],
           "Am":[329.63,261.63,440.00,329.63],"F":[349.23,261.63,440.00,349.23]}
bar=2.4
start=4.7
bi=0
while start<53.0:
    name,chord=prog[bi%4]
    padchord(chord,start,bar+0.05,0.05)
    # arpeggio plucks, 8 per bar
    notes=arp_notes[name]
    for j in range(8):
        nt=notes[j%len(notes)]* (2 if j%4==3 else 1)
        pan=-0.4 if j%2==0 else 0.4
        pluck(nt, start+j*(bar/8), 0.10, length=bar/8*1.6, pan=pan)
    start+=bar; bi+=1
# act transition whooshes
for cx in [14.2,23.8,38.4]:
    whoosh(cx,0.32,0.30)
# success bells at HubSpot done (~38.6)
bell(523.25,38.5,0.32); bell(783.99,38.85,0.26); bell(659.25,39.1,0.22)

# ---------------- CTA 53-58 : uplift ----------------
whoosh(53.0,0.45,0.4)
for f in [261.63,329.63,392.00,523.25]:
    L[:]+=tone(f,53.1,4.6,0.06,a=0.4,d=0.5,s=0.85,r=1.2)
    R[:]+=tone(f,53.1,4.6,0.06,a=0.4,d=0.5,s=0.85,r=1.2)
bell(1046.50,53.2,0.28,length=3.5)

# ---------------- mix / normalize ----------------
def soft_lowpass(x,a=0.25):
    y=np.copy(x)
    for _ in range(2):
        y[1:]=y[1:]*(1-a)+y[:-1]*a
    return y
L=soft_lowpass(L); R=soft_lowpass(R)
peak=max(np.max(np.abs(L)),np.max(np.abs(R)),1e-6)
g=0.82/peak
L*=g; R*=g
# fade out last 1s
fo=int(1.0*SR); L[-fo:]*=np.linspace(1,0,fo); R[-fo:]*=np.linspace(1,0,fo)
L[:int(0.05*SR)]*=np.linspace(0,1,int(0.05*SR)); R[:int(0.05*SR)]*=np.linspace(0,1,int(0.05*SR))

stereo=np.empty((N,2)); stereo[:,0]=L; stereo[:,1]=R
data=(stereo*32767).astype(np.int16)
with wave.open("/home/user/ReadMe/video_build/out/soundtrack.wav","w") as w:
    w.setnchannels(2); w.setsampwidth(2); w.setframerate(SR)
    w.writeframes(data.tobytes())
print("soundtrack.wav written", DUR,"s peak",peak)
