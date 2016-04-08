#!/usr/bin/env python
# coding: utf-8

import matplotlib.pyplot as plt
import numpy as np
import sys

fileName = str(sys.argv[1])
print(fileName)
colors = ["red", "blue", "brown", "orange", "teal", "grey", "pink", "yellow"]
xLabel = "X"
yLabel = "Y"
title = "K means clustering; clusters = "
f = open(fileName)
title += str(f.readline().split(" ")[0])
Clusters = ["Cluster "+str(i) for i in range(0,5)]
arr_of_xs = []
arr_of_ys = []
clusterNum = -1;
for line in f:
    if line[0] != "C":
        comps = line.split(", ");
        arr_of_xs[clusterNum].append(float(comps[0]))
        arr_of_ys[clusterNum].append(float(comps[1]))
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