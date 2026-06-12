import subprocess, os
OUT="/home/user/ReadMe/video_build/out_sr"
clips=[("s1_title",5.0),("s2_lang",9.0),("s3_reply",9.0),("s4a_danger",6.0),
       ("s4b_safety",6.0),("s5_detail",10.0),("s6_dash",7.0),("s7_end",5.0)]
X=0.5
inp="".join(f'-i {OUT}/{n}.mp4 ' for n,_ in clips)
# xfade chain
fc=[]; prev="0"; off=clips[0][1]
for i in range(1,len(clips)):
    cur=f"v{i}"
    trans="fade"
    fc.append(f"[{prev}][{i}:v]xfade=transition={trans}:duration={X}:offset={off-X:.3f}[{cur}]")
    off=off+clips[i][1]-X
    prev=cur
final_dur=off
vchain=";".join(fc)
cmd=(f'ffmpeg -y -loglevel error {inp}-i /home/user/ReadMe/video_build/assets/sr_bgm.m4a '
     f'-filter_complex "{vchain};[{prev}]format=yuv420p[vout];'
     f'[{len(clips)}:a]atrim=0:{final_dur:.3f},afade=t=out:st={final_dur-1.8:.3f}:d=1.8,volume=0.9[aout]" '
     f'-map "[vout]" -map "[aout]" -r 30 -c:v libx264 -crf 19 -pix_fmt yuv420p -c:a aac -b:a 192k '
     f'-movflags +faststart /home/user/ReadMe/video_build/StayReplyAI_demo.mp4')
print("final_dur",round(final_dur,2))
subprocess.run(cmd,shell=True,check=True)
print("done")
