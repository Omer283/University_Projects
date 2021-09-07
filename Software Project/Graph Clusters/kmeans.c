/*
 * Module / file: kmeans
 * Purpose: cluster data according to the kmeans algorithm, with given centroids
 */

#define PY_SSIZE_T_CLEAN

#include <Python.h>
#include <math.h>

typedef double frac;

/*
 * Method: calcKMeans
 * Purpose: Returns labels of points, according to kmeans algorithm
 */
static int* calcKMeans(double **pts, double **cents, int n, int k, int d, int MAX_ITER) {
    int *counts, *clusters;
    int i, iter, j, bstIndex, r;
    double minDist, currDist, **tempCents, diffSum;
    const double tol = 0.0001; //epsilon
    counts = (int *) calloc(k, sizeof(int));
    tempCents = (double **) calloc(k, sizeof(double *));
    for (i = 0; i < k; i++) {
        tempCents[i] = (double *) calloc(d, sizeof(double));
    }
    for (iter = 0; iter < MAX_ITER; iter++) {
        for (i = 0; i < k; i++) {
            for (j = 0; j < d; j++) {
                tempCents[i][j] = 0;
            }
            counts[i] = 0;
        }
        for (i = 0; i < n; i++) {
            minDist = 1e38; //infinity
            bstIndex = -1;
            for (j = 0; j < k; j++) {
                currDist = 0;
                for (r = 0; r < d; r++) {
                    currDist += (pts[i][r] - cents[j][r]) * (pts[i][r] - cents[j][r]);
                }
                if (currDist < minDist) { //updates if the current distance is minimal
                    minDist = currDist;
                    bstIndex = j;
                }
            }
            counts[bstIndex]++;
            for (r = 0; r < d; r++) {
                tempCents[bstIndex][r] += pts[i][r];
            }
        }
        for (j = 0; j < k; j++) {
            for (r = 0; r < d; r++) {
                if (counts[j] != 0) {
                    tempCents[j][r] /= counts[j];
                }
            }
        }
        diffSum = 0; //difference between current and previous centroids
        for (j = 0; j < k; j++) {
            for (r = 0; r < d; r++) {
                diffSum += (cents[j][r] - tempCents[j][r]) * (cents[j][r] - tempCents[j][r]);
                cents[j][r] = tempCents[j][r];
            }
        }
        if (diffSum < tol) {
            break;
        }
    }
    clusters = (int*)calloc(n, sizeof(int));
    for (i = 0; i < n; i++) {
        minDist = 1e20; //infinity
        for (j = 0; j < k; j++) {
            currDist = 0;
            for (r = 0; r < d; r++) {
                currDist += (pts[i][r] - cents[j][r]) * (pts[i][r] - cents[j][r]);
            }
            if (currDist < minDist) {
                minDist = currDist;
                clusters[i] = j;
            }
        }
    }
    if (counts != NULL) {
        free(counts);
    }
    if (tempCents != NULL) {
        for (j = 0; j < k; j++) {
            if (tempCents[j] != NULL) {
                free(tempCents[j]);
            }
        }
        free(tempCents);
    }
    return clusters;
}

/*
 * Method: kmeans_py
 * Purpose: Calls the main kmeans function and converts result to python,
 * returns labels
 */
static PyObject *kmeans_py(PyObject *self, PyObject *args) {
    PyObject *pts_obj, *cents_obj;
    int n, k, d = -1, i, j, MAX_ITER;
    double **pts, **cents;
    if (!PyArg_ParseTuple(args, "OOi", &pts_obj, &cents_obj, &MAX_ITER)) { //fails to parse
        return NULL;
    }
    n = (int)PyObject_Length(pts_obj);
    k = (int)PyObject_Length(cents_obj);
    if (n <= 0 || k <= 0) {
        return NULL;
    }
    pts = (double **) calloc(n, sizeof(double *));
    if (pts == NULL) {
        return NULL;
    }
    cents = (double **) calloc(k, sizeof(double *));
    if (cents == NULL) {
        return NULL;
    }
    for (i = 0; i < n; i++) {//converts python pts to c
        PyObject *pt_coords;
        pt_coords = PyList_GetItem(pts_obj, i);
        if (!PyList_Check(pt_coords)) {
            return NULL;
        }
        if (d == -1) {
            d = (int)PyObject_Length(pt_coords);
        }
        pts[i] = (double *) calloc(d, sizeof(double));
        if (pts[i] == NULL) {
            return NULL;
        }
        for (j = 0; j < d; j++) {
            PyObject *val;
            val = PyList_GetItem(pt_coords, j);
            pts[i][j] = PyFloat_AsDouble(val);
        }
    }
    for (i = 0; i < k; i++) { //converts python centroids to c
        PyObject *pt_coords;
        pt_coords = PyList_GetItem(cents_obj, i);
        if (!PyList_Check(pt_coords)) {
            return NULL;
        }
        cents[i] = (double *) calloc(d, sizeof(double));
        if (cents[i] == NULL) {
            return NULL;
        }
        for (j = 0; j < d; j++) {
            PyObject *val;
            val = PyList_GetItem(pt_coords, j);
            cents[i][j] = PyFloat_AsDouble(val);
        }
    }
    int* clusters = calcKMeans(pts, cents, n, k, d, MAX_ITER);
    if (pts != NULL) {
        for (i = 0; i < n; i++) {
            if (pts[i] != NULL) {
                free(pts[i]);
            }
        }
        free(pts);
    }
    if (cents != NULL) {
        for (i = 0; i < k; i++) {
            if (cents[i] != NULL) {
                free(cents[i]);
            }
        }
        free(cents);
    }
    PyObject* clusters_py = PyList_New(n);
    for (i = 0; i < n; i++) {
        PyObject* element = Py_BuildValue("i", clusters[i]);
        PyList_SetItem(clusters_py, i, element);
    }
    free(clusters);
    return clusters_py;
}

static PyMethodDef capiMethods[] = {
        {"kmeans_cpython",
                (PyCFunction)kmeans_py,
                   METH_VARARGS,
                PyDoc_STR("")},
        {NULL,NULL,0,NULL}
};

static struct PyModuleDef moduledef = {
        PyModuleDef_HEAD_INIT, "mykmeanssp", NULL, -1, capiMethods
};

/*
 * Method: PyInit_mykmeanssp
 * Purpose: Initializes module
 */
PyMODINIT_FUNC PyInit_mykmeanssp(void) {
    PyObject *m;
    m = PyModule_Create(&moduledef);
    if (!m) {
        return NULL;
    }
    return m;
}