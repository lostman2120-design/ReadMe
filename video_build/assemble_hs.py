import subprocess, sys
OUT="/home/user/ReadMe/video_build/out_hs"
BGM="/home/user/ReadMe/video_build/assets/hs_bgm.m4a"
X=0.4
FULL=[("s1_title",5),("s2_icpset",8),("s3_icpctx",11),("s4_analyze",5),
      ("s5_score",11),("s6_reason",9),("s7_dm",11),("s8_hub",6),("s9_end",4)]
SHORT=[("s1_title_sh",3),("s3_icpctx_sh",8),("s5_score_sh",7),
       ("s7_dm_sh",8),("s8_hub_sh",5),("s9_end_sh",3)]

def assemble(clips,outfile):
    inp="".join(f'-i {OUT}/{n}.mp4 ' for n,_ in clips)
    fc=[]; prev="0"; off=clips[0][1]
    for i in range(1,len(clips)):
        cur=f"v{i}"
        fc.append(f"[{prev}][{i}:v]xfade=transition=fade:duration={X}:offset={off-X:.3f}[{cur}]")
        off=off+clips[i][1]-X; prev=cur
    fd=off
    chain=";".join(fc)
    cmd=(f'ffmpeg -y -loglevel error {inp}-i {BGM} '
         f'-filter_complex "{chain};[{prev}]format=yuv420p[v];'
         f'[{len(clips)}:a]atrim=0:{fd:.3f},afade=t=out:st={fd-1.6:.3f}:d=1.6,volume=0.85[a]" '
         f'-map "[v]" -map "[a]" -r 30 -c:v libx264 -crf 19 -pix_fmt yuv420p -c:a aac -b:a 192k '
         f'-movflags +faststart {outfile}')
    subprocess.run(cmd,shell=True,check=True)
    print(outfile,"dur",round(fd,2))

assemble(FULL,"/home/user/ReadMe/video_build/alex_icp_demo_v023.mp4")
assemble(SHORT,"/home/user/ReadMe/video_build/alex_icp_demo_v023_short.mp4")
