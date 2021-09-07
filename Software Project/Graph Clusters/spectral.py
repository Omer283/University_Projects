#
# Module: spectral
# Purpose: Responsible for the linear algebra part of the spectral clustering - without the actual kmeans,
# nor the decompositions.
# Interface: Methods generating the according matrices
#
import numpy as np
import kmeans_pp
import mymdcmp
import sys


#
# Method: get_weight_matrix
# Purpose: Gets weight matrix according to definition
#
def get_weight_matrix(X):
    N = np.shape(X)[0]
    W = np.zeros((N, N))
    for i in np.arange(N):
        for j in np.arange(N):
            if i == j:
                W[i][j] = 0
            else:
                norm = np.linalg.norm(X[i] - X[j])
                W[i][j] = np.exp(-0.5 * norm)
    return W


#
# Method: get_diagonal_matrix
# Purpose: Gets diagonal matrix according to definition
#
def get_diagonal_matrix(W):
    N = np.shape(W)[0]
    D = np.zeros_like(W)
    for i in np.arange(N):
        D[i][i] = np.sum(W[i])
    return D


#
# Method: get_laplacian
# Purpose: Gets L_Norm matrix according to definition
#
def get_laplacian(W):
    N = np.shape(W)[0]
    D = get_diagonal_matrix(W)
    D_invroot = D
    for i in np.arange(N):
        if D[i][i] == 0:
            print('One of the diagonal elements in D is zero, exiting now')
            sys.exit()
        D_invroot[i][i] = 1 / np.sqrt(D_invroot[i][i])
    L_norm = np.eye(N) - (D_invroot @ W @ D_invroot)
    return L_norm


#
# Method: get_k
# Purpose: Gets k according to eigengap heuristic
#
def get_k(A):
    N = np.shape(A)[0]
    eigenvals = np.array([A[i][i] for i in np.arange(N)])
    eigenvals = np.sort(eigenvals)
    bst = -1
    bst_index = -1
    for i in np.arange((N + 1) // 2):
        diff = np.abs(eigenvals[i] - eigenvals[i + 1])
        if diff > bst:
            bst = diff
            bst_index = i
    return bst_index + 1


#
# Method: spectral_cluster
# Purpose: returns the result of the spectral clustering algorithm, and the calculated k (if calculated)
#
def spectral_cluster(X, n, k, d, MAX_ITER, is_random):
    W = get_weight_matrix(X)
    L_norm = get_laplacian(W)
    A, Q = mymdcmp.qr_iteration_cpython(L_norm.tolist())
    A = np.array(A)
    Q = np.array(Q)
    ev = np.array([[A[i][i], i] for i in np.arange(n)])
    ev = ev[ev[:,0].argsort()]
    if is_random:
        k = get_k(A)
    U = np.zeros((n, k))
    for i in np.arange(k):
        U[:,i] = Q[:,int(ev[i][1])]
    T = np.zeros_like(U)
    for i in np.arange(n):
        T[i] = U[i] / np.linalg.norm(U[i])
    return kmeans_pp.kmeans_cluster(T, n, k, k, MAX_ITER), k