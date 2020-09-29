#!/usr/bin/env python3
# -*- coding: utf-8 -*-


from PIL import Image

colors = {
    (0,     255,    0   ) : "#",
    (0,     127,    0   ) : "Y",
    (0,     0,      255 ) : "~",
    (255,   255,    0   ) : "^",
    (255,   0,      0   ) : "h",
}

def distance(c1, c2):
    return (abs (c2[0]-c1[0])
        +   abs (c2[1]-c1[1])
        +   abs (c2[2]-c1[2])
    )

def closest(c, d) :
    color = ()
    dist = 3*255
    for c_test in d.keys() :
        new_d = distance(c, c_test)
        # print(f"Closest : c{color} vs {c_test} : {new_d}")
        if new_d < dist :
            dist = new_d
            color = c_test
    # print(f"Closest : {color}")
    return color

def img2terrain(imgfn):
    img = Image.open(imgfn)
    w, h = img.size
    pixels = img.load()

    dirname, filename= os.path.split(imgfn)
    name, ext= os.path.splitext(filename)
    newpathname= os.path.join(dirname, f"{name}_{w}x{h}.txt")
    print(newpathname)
    ter =  open(newpathname, "w")

    ter.write(str(w) + '\n')
    ter.write(str(h) + '\n')
    for y in range(h):
        for x in range(w):
            c = closest(pixels[x, y], colors)
            print(colors[c], end='')
            ter.write(colors[c])
        print()
        ter.write('\n')
    ter.close()

if __name__ == "__main__":
    import sys, os

    for imgfn in sys.argv[1:]:
        img2terrain(imgfn)
