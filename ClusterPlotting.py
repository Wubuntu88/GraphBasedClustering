#!/usr/bin/env python
# coding: utf-8

import matplotlib.pyplot as plt
import numpy as np
import sys

fileName = str(sys.argv[1])
threshold = str(sys.argv[2])
colors = ["red", "blue", "brown", "orange", "teal", "grey", "pink", "yellow", "green", "lightblue"]
xLabel = "X"
yLabel = "Y"

f = open(fileName)

title = "Graph Based Clustering;"
title += "file: " + fileName
title += "; clusters = "
title += str(f.readline().split(" ")[0])
title += "; threshold: " + threshold
Clusters = ["Cluster "+str(i) for i in range(0,5)]

arr_of_xs = []
arr_of_ys = []
clusterNum = -1;


for line in f:
    comps = line.split(", ");
    if line[0] != "C":
        arr_of_xs[clusterNum].append(float(comps[1]))
        arr_of_ys[clusterNum].append(float(comps[2]))
    else:
        arr_of_xs.append([])
        arr_of_ys.append([])
        clusterNum += 1

for i in range(0, len(arr_of_xs)):
    xs = arr_of_xs[i]
    ys = arr_of_ys[i]
    plt.scatter(xs, ys, s=100, c=colors[i])
    
plt.title(title)
plt.xlabel(xLabel)
plt.ylabel(yLabel)
plt.show()