#
# Module: kmeans_pp
# Purpose: Make up for the ground work for the main kmeans algorithm -
# get initial centroids (kmeans++) and call the c function
# Interface: 2 functions - one that finds centroids and the other calls the c implementation
#

import numpy as np
import mykmeanssp


#
# Method: k_means_pp
# Purpose: Returns initial centroids using kmeans++ algorithm
#
def k_means_pp(pts, n, k, d):
    np.random.seed(0)
    if k == 0:
        return np.array([], np.float64)
    lst = np.zeros((k, d))
    ind = [0 for i in np.arange(k)]
    ind[0] = int(np.random.choice(n, 1))
    lst[0] = pts[ind[0]]
    d_arr = np.full(n, np.inf, dtype=np.float64)
    for i in np.arange(1, k):
        tmpmat = pts - lst[i - 1]
        d_arr = np.minimum(d_arr, np.sum(np.multiply(tmpmat, tmpmat), axis=1))
        d_sum = np.sum(d_arr)
        ind[i] = int(np.random.choice(n, 1, p=np.array([d_arr[i] / d_sum for i in np.arange(0, n)], np.float64)))
        lst[i] = pts[ind[i]]
    return lst


#
# Method: kmeans_cluster
# Purpose: Returns clustering of data according to kmeans, kmeans++ algorithms.
#
def kmeans_cluster(X, n, k, d, MAX_ITER):
    cents = k_means_pp(X, n, k, d)
    return mykmeanssp.kmeans_cpython(X.tolist(), cents.tolist(), MAX_ITER)
