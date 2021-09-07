#
# Module: main
# Purpose: The skeleton of the project - used to link everything together
# Interface: The init function calls every other function, used to input, process, and output the data
#

import numpy as np
from sklearn.datasets import make_blobs
from kmeans_pp import kmeans_cluster
from spectral import spectral_cluster
from matplotlib.backends.backend_pdf import PdfPages
import matplotlib.pyplot as plt
import matplotlib.cm as cm

MAX_N = {2:545, 3:540}
MAX_K = {2:30, 3:30}
MAX_ITER = 300


#
# Method: print_max_capacity
# Purpose: Prints the max capacity
# Explanation (IMPORTANT!): I maximized over n, but over k I chose a logical value.
# This does not imply that my program isn't able to run on higher k values, it certainly can and with almost
# No effect on the time, but the clustering process is way worse with higher k values.
# For the past week, runs of nova have become extremely slow, so the max capacity might not be entirely accurate.
#
def print_max_capacity():
    print("The maximum capacity of my project:")
    for d in [2, 3]:
        print("For", d, "dimensions:")
        print("N =", MAX_N[d], ", K =", MAX_K[d])


#
# Method: generate_random_points
# Purpose: generates random points using make_blobs
#
def generate_random_points(n, k, d):
    return make_blobs(n_samples=n, centers=k, n_features=d, cluster_std=0.75)


#
# Method: calc_jaccard
# Purpose: calculates jaccard measure according to 2 labelings
#
def calc_jaccard(l1, l2):
    n = np.shape(l1)[0]
    pairs_in_one = 0
    pairs_in_both = 0
    for i in np.arange(n):
        for j in np.arange(i + 1, n):
            if l1[i] == l1[j] or l2[i] == l2[j]:
                pairs_in_one += 1
            if l1[i] == l1[j] and l2[i] == l2[j]:
                pairs_in_both += 1
    if pairs_in_one == 0:
        return 'No two points were clustered the same'
    else:
        return round(pairs_in_both / pairs_in_one, 2)


#
# Method: plot_2d
# Purpose: plots a 2d plot of the clustering result
#
def plot_2d(title, k, X, label_list):
    fig = plt.figure()
    ax = fig.add_subplot()
    colors = iter(cm.rainbow(np.linspace(0,1,k)))#generates colors
    for i in np.arange(k):
        ax.scatter(X[label_list[i],0], X[label_list[i],1], color=next(colors))
    ax.set_title(title)


#
# Method: plot_3d
# Purpose: plots a 3d plot of the clustering result
#
def plot_3d(title, k, X, label_list):
    fig = plt.figure()
    ax = fig.add_subplot(projection='3d')
    colors = iter(cm.rainbow(np.linspace(0,1,k)))#generates colors
    for i in np.arange(k):
        ax.scatter(X[label_list[i],0], X[label_list[i],1], X[label_list[i],2], color=next(colors))
    ax.set_title(title)


#
# Method: output_to_files
# Purpose: output results to according files
#
def output_to_files(k_clusters, k_gen, n, d, labels, labels_kmeans, labels_spectral, X):
    kmeans_labels_list = [[] for _ in np.arange(k_clusters)]
    spectral_labels_list = [[] for _ in np.arange(k_clusters)]
    for i in np.arange(n):#for each label, which data points are in it
        kmeans_labels_list[labels_kmeans[i]].append(i)
        spectral_labels_list[labels_spectral[i]].append(i)
    with open('data.txt', 'w') as f:
        for i in np.arange(n):
            for j in np.arange(d):
                f.write(str(X[i][j]) + ',')
            f.write(str(labels[i]))
            f.write('\n')
    with open('clusters.txt', 'w') as f:
        f.write(str(k_clusters))
        f.write('\n')
        for i in range(k_clusters):
            amt = np.shape(kmeans_labels_list[i])[0]
            for j in np.arange(amt):
                f.write(str(kmeans_labels_list[i][j]))
                if j != amt - 1:
                    f.write(',')
                else:
                    f.write('\n')
        for i in np.arange(k_clusters):
            amt = np.shape(spectral_labels_list[i])[0]
            for j in np.arange(amt):
                f.write(str(spectral_labels_list[i][j]))
                if j != amt - 1:
                    f.write(',')
                else:
                    f.write('\n')
    with PdfPages('clusters.pdf') as pdf:
        if d == 2:
            plt.subplot(2, 3, 1)
            plot_2d('K - Means', k_clusters, X, kmeans_labels_list)
            pdf.savefig()
            plt.subplot(2, 3, 3)
            plot_2d('Spectral Clustering', k_clusters, X, spectral_labels_list)
            pdf.savefig()
            plt.close()
        else:
            plt.subplot(2, 3, 1)
            plot_3d('K - Means', k_clusters, X, kmeans_labels_list)
            pdf.savefig()
            plt.subplot(2, 3, 1)
            plot_3d('Spectral Clustering', k_clusters, X, spectral_labels_list)
            pdf.savefig()
            plt.close()
        info_page = plt.figure(figsize=(10,8))
        info_page.clf()
        info_page.text(0.5, 0.5,'Data was generated from the values'
                               ' n = ' + str(n) + ',k = ' + str(k_gen) + '\nThe k used in both algorithms was ' +
                       str(k_clusters) +
                       '\nJaccard for spectral = ' + str(calc_jaccard(labels, labels_spectral)) +
                       '\nJaccard for kmeans = ' + str(calc_jaccard(labels, labels_kmeans)),size=16,ha='center',
                       va='top')
        pdf.savefig()
        plt.close()


#
# Method: solve
# Purpose: Converts the command line input to actual parameters,
# and calls the two clustering methods, and calls the output method.
#
def solve(cmd_k, cmd_n, d, is_random):
    if is_random:#generate random values
        k_gen = np.random.randint(MAX_K[d] // 2, MAX_K[d] + 1)
        n = np.random.randint(MAX_N[d] // 2, MAX_N[d] + 1)
    else:
        k_gen = cmd_k
        n = cmd_n
    X, labels = generate_random_points(n, k_gen, d)
    labels_spectral, k_clusters = spectral_cluster(X, n, k_gen, d, MAX_ITER, is_random)
    #if random, k_clusters will be the one calculated by eigengap heuristic
    labels_kmeans = kmeans_cluster(X, n, k_clusters, d, MAX_ITER)
    output_to_files(k_clusters, k_gen, n, d, labels, labels_kmeans, labels_spectral, X)


#
# Method: init
# Purpose: Generates d, and calls solve with according parameters
#
def init(cmd_k, cmd_n, is_random):
    print_max_capacity()
    d = np.random.choice([2, 3])
    if not is_random:
        valid = True
        if cmd_k <= 0:
            valid = False
            print('Invalid input - k is not positive')
        if cmd_n <= 0:
            valid = False
            print('Invalid input - n is not positive')
        if cmd_k > cmd_n:
            valid = False
            print('Invalid input - k > n')
        if valid:
            solve(cmd_k, cmd_n, d, is_random)
    else:
        solve(cmd_k, cmd_n, d, is_random)