#!/usr/bin/env python
# coding: utf-8

import matplotlib.pyplot as plt
import numpy as np
import sys

fileName = str(sys.argv[1])
print(fileName)
colors = ["red", "blue", "brown", "orange", "teal", "grey", "pink", "yellow", "green", "lightblue"]
xLabel = "X"
yLabel = "Y"
title = "Graph Based Clustering; clusters = "
f = open(fileName)
title += str(f.readline().split(" ")[0])
Clusters = ["Cluster "+str(i) for i in range(0,5)]
arr_of_xs = []
arr_of_ys = []
arr_of_zs = []
clusterNum = -1;
for line in f:
    comps = line.split(", ");
    if line[0] != "C":
        arr_of_xs[clusterNum].append(float(comps[0]))
        arr_of_ys[clusterNum].append(float(comps[1]))
        if (len(comps) > 2):
            if(len(arr_of_zs) < clusterNum + 1):
                arr_of_zs.append([])
            print("c: ", str(clusterNum))
            print("len: " + str(len(arr_of_zs)))
            arr_of_zs[clusterNum].append(float(comps[2]))
    else:
        arr_of_xs.append([])
        arr_of_ys.append([])
        clusterNum += 1
'''
fig = None
ax = None
if(len(arr_of_zs)):
    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
'''
for i in range(0, len(arr_of_xs)):
    xs = arr_of_xs[i]
    ys = arr_of_ys[i]
    
    if (len(arr_of_zs) > 0):
        zs = arr_of_zs[i]
        plt.scatter(xs, ys, zs, s=100, c=colors[i])
    else:
        plt.scatter(xs, ys, s=100, c=colors[i])
    
plt.title(title)
plt.xlabel(xLabel)
plt.ylabel(yLabel)
plt.show()