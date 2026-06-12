import numpy as np, subprocess, os
SR=44100
# calm, professional pad BGM
def adsr(n,a,d,s,r,sl=0.7):
    e=np.ones(n); ai=int(a*SR); di=int(d*SR); ri=int(r*SR)
    if ai>0: e[:ai]=np.linspace(0,1,ai)
    if di>0: e[ai:ai+di]=np.linspace(1,sl,di)
    e[ai+di:n-ri]=sl
    if ri>0: e[n-ri:]=np.linspace(sl,0,ri)
    return e
def note(f,dur,amp=0.2,partials=(1,2,3)):
    n=int(dur*SR); t=np.linspace(0,dur,n,endpoint=False); w=np.zeros(n)
    for k,p in enumerate(partials):
        w+=(amp/(p**1.4))*np.sin(2*np.pi*f*p*t)
    return w*adsr(n,0.25,0.4,0.7,0.5,0.78)
NT={'C3':130.81,'D3':146.83,'E3':164.81,'F3':174.61,'G3':196.0,'A3':220.0,'B3':246.94,
    'C4':261.63,'D4':293.66,'E4':329.63,'F4':349.23,'G4':392.0,'A4':440.0,'B4':493.88,
    'C5':523.25,'E5':659.25,'G5':783.99}
# I - V - vi - IV  (C G Am F), 8 bars
prog=[['C4','E4','G4'],['G3','B3','D4'],['A3','C4','E4'],['F3','A3','C4'],
      ['C4','E4','G4'],['G3','B3','D4'],['A3','C4','E4'],['F3','A3','C4']]
BAR=6.8
total=int(BAR*len(prog)*SR)
buf=np.zeros(total+SR)
for i,ch in enumerate(prog):
    seg=np.zeros(int(BAR*SR)+int(0.6*SR))
    for nm in ch:
        s=note(NT[nm],BAR+0.5,amp=0.16); seg[:len(s)]+=s
    st=int(i*BAR*SR); buf[st:st+len(seg)]+=seg
# soft sparkle arpeggio every 2 bars
arp=['C5','G5','E5','G5']
for i in range(0,len(prog),2):
    base=i*BAR
    for j,nm in enumerate(arp):
        s=note(NT[nm],1.2,amp=0.05,partials=(1,2)); st=int((base+0.4+j*0.55)*SR)
        if st+len(s)<len(buf): buf[st:st+len(s)]+=s
# simple reverb (a few delayed taps)
rev=buf.copy()
for dl,g in [(0.05,0.3),(0.11,0.22),(0.19,0.15),(0.31,0.1)]:
    d=int(dl*SR); rev[d:]+=g*buf[:-d]
buf=0.8*buf+0.35*rev
# gentle low-pass (moving average) for warmth
k=8; buf=np.convolve(buf,np.ones(k)/k,mode='same')
buf/=np.max(np.abs(buf))+1e-9
buf*=0.5  # low volume bed
# global fade in/out
fi=int(1.2*SR); fo=int(2.0*SR)
buf[:fi]*=np.linspace(0,1,fi); buf[-fo:]*=np.linspace(1,0,fo)
st=np.column_stack([buf,buf]).astype(np.float32)
st.tofile('/tmp/srb/bgm.f32')
subprocess.run(f'ffmpeg -y -loglevel error -f f32le -ar {SR} -ac 2 -i /tmp/srb/bgm.f32 '
               f'-c:a aac -b:a 192k /home/user/ReadMe/video_build/assets/sr_bgm.m4a',shell=True,check=True)
print('bgm', len(buf)/SR,'s')
