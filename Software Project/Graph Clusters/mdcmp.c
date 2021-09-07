/*
 * Module / file: mdcmp
 * Purpose: matrix decomposition - responsible for the qr decomposition / iteration algorithms
 * Interface: has one focal method which is used to return the matrix after the qr iteration algorithm
 */

#define PY_SSIZE_T_CLEAN

#include <Python.h>

typedef double frac; //The reason i used typedef is in order to decide whether i should keep it float or double

/*
 * Method: allocate_2d
 * Purpose: allocate 2d frac matrix, with n rows and m columns
 */
int allocate_2d(frac*** mat, int n, int m) {
    (*mat) = (frac**)calloc(n, sizeof(frac*));
    int i, j;
    if ((*mat) == NULL) {
        return -1;
    }
    for (i = 0; i < n; i++) {
        (*mat)[i] = (frac*)calloc(m, sizeof(frac));
        if ((*mat)[i] == NULL) { //something goes wrong in allocation
            for (j = 0; j < i; j++) {
                free((*mat)[j]);
            }
            free(*mat);
            return -1;
        }
    }
    return 0;
}

/*
 * Method: free_2d
 * Purpose: free 2d frac matrix, with n rows and n columns
 */
int free_2d(frac*** A, int n) {
    int i;
    if (*A != NULL) {
        for (i = 0; i < n; i++) {
            if ((*A)[i] != NULL) {
                free((*A)[i]);
            }
        }
        free(*A);
        return 0;
    }
    else { //something is wrong
        return -1;
    }
}

/*
 * Method: multiplyInto
 * Purpose: assigns X = AB
 */
void multiplyInto(frac** A, frac** B, frac** X, int n) {
    int i, j, k;
    frac cum;
    for (i = 0; i < n; i++) {
        for (j = 0; j < n; j++) {
            cum = 0;
            for (k = 0; k < n; k++) {
                cum += A[i][k] * B[k][j]; //definition of multiplication
            }
            X[i][j] = cum;
        }
    }
}

/*
 * Method: mgs
 * Purpose: Returns Q, R: the results of the MGS algorithm
 */
static frac*** mgs(frac** A, int n) {
    frac **U = NULL, **Q = NULL, **R = NULL;
    int i, j, r;
    frac tmp;
    if (allocate_2d(&U, n, n) == -1) { //something went wrong in allocation
        return NULL;
    }
    if (allocate_2d(&Q, n, n) == -1) {
        return NULL;
    }
    if (allocate_2d(&R, n, n) == -1) {
        return NULL;
    }
    for (i = 0; i < n; i++) { //During calculations, Q and U are transposed for cache locality
        for (j = 0; j < n; j++) {
            U[i][j] = A[j][i];
        }
    }
    for (i = 0; i < n; i++) {
        R[i][i] = 0;
        for (j = 0; j < n; j++) {
            R[i][i] += U[i][j] * U[i][j];
        }
        R[i][i] = sqrt(R[i][i]);
        if (R[i][i] == 0) {
            exit(1); //something wrong happened
        }
        for (j = 0; j < n; j++) {
            Q[i][j] = U[i][j] / R[i][i];
        }
        for (j = i + 1; j < n; j++) {
            frac cum = 0;
            for (r = 0; r < n; r++) {
                cum += Q[i][r] * U[j][r];
            }
            R[i][j] = cum;
            for (r = 0; r < n; r++) {
                U[j][r] -= R[i][j] * Q[i][r];
            }
        }
    }
    free_2d(&U, n);
    for (i = 0; i < n; i++) { //transpose Q
        for (j = 0; j < i; j++) {
            tmp = Q[i][j];
            Q[i][j] = Q[j][i];
            Q[j][i] = tmp;
        }
    }
    frac*** ret = (frac***)calloc(2, sizeof(frac**));
    ret[0] = Q;
    ret[1] = R;
    return ret;
}

/*
 * Method: qr_iteraion_c
 * Purpose: Returns result of qr iteration algorithm
 */
static frac*** qr_iteration_c(frac** A, int n) {
    frac** A_bar = NULL, **Q_bar = NULL, **Q = NULL, **R = NULL, **tmp_mat = NULL, ***ret = NULL;
    frac tol = 0.0001, tmp_max, tmp_frac;
    int i, j, r;
    if (allocate_2d(&A_bar, n, n)) {
        return NULL;
    }
    if (allocate_2d(&Q_bar, n, n)) {
        return NULL;
    }
    if (allocate_2d(&tmp_mat, n, n)) {
        return NULL;
    }
    for (i = 0; i < n; i++) {
        Q_bar[i][i] = 1;
        for (j = 0; j < n; j++) {
            A_bar[i][j] = A[i][j];
        }
    }
    for (i = 0; i < n; i++) { //simply follows the algorithm
        frac*** mgs_ret = mgs(A_bar, n);
        Q = mgs_ret[0];
        R = mgs_ret[1];
        free(mgs_ret);
        multiplyInto(R, Q, A_bar, n);
        multiplyInto(Q_bar, Q, tmp_mat, n);
        free_2d(&Q, n);
        free_2d(&R, n);
        tmp_max = 0;
        for (j = 0; j < n; j++) {
            for (r = 0; r < n; r++) {
                tmp_frac = Q_bar[j][r] - tmp_mat[j][r];
                if (tmp_frac < 0) {
                    tmp_frac = -tmp_frac;
                }
                if (tmp_frac > tmp_max) {
                    tmp_max = tmp_frac;
                }
            }
        }
        if (tmp_max <= tol) { //if difference less than epsilon, terminate
            free_2d(&tmp_mat, n);
            ret = (frac***)calloc(2, sizeof(frac**));
            ret[0] = A_bar;
            ret[1] = Q_bar;
            return ret;
        }
        for (j = 0; j < n; j++) {
            for (r = 0; r < n; r++) {
                Q_bar[j][r] = tmp_mat[j][r];
            }
        }
    }
    free_2d(&tmp_mat, n);
    ret = (frac***)calloc(2, sizeof(frac**));
    ret[0] = A_bar;
    ret[1] = Q_bar;
    return ret;
}

/*
 * Method: qr_iteration
 * Purpose: Parses python input, calculates QR iteration and converts to python output
 */
static PyObject* qr_iteration(PyObject *self, PyObject *args) {
    PyObject *pts_obj;
    int n, i, j;
    frac **pts, ***ret, **A_bar, **Q_bar;
    PyObject *A_bar_py, *Q_bar_py, *A_bar_row, *Q_bar_row, *element;
    if (!PyArg_ParseTuple(args, "O", &pts_obj)) {
        return NULL;
    }
    n = (int)PyObject_Length(pts_obj);
    if (n <= 0) {
        return NULL;
    }
    pts = (double **) calloc(n, sizeof(double *));
    if (pts == NULL) {
        return NULL;
    }
    for (i = 0; i < n; i++) {
        PyObject *pt_coords;
        pt_coords = PyList_GetItem(pts_obj, i);
        if (!PyList_Check(pt_coords)) {
            return NULL;
        }
        pts[i] = (double *) calloc(n, sizeof(double));
        if (pts[i] == NULL) {
            return NULL;
        }
        for (j = 0; j < n; j++) {
            PyObject *val;
            val = PyList_GetItem(pt_coords, j);
            pts[i][j] = PyFloat_AsDouble(val);
        }
    }
    ret = qr_iteration_c(pts, n);
    A_bar = ret[0];
    Q_bar = ret[1];
    free(ret);
    A_bar_py = PyList_New(n);
    for (i = 0; i < n; i++) {
        A_bar_row = PyList_New(n);
        for (j = 0; j < n; j++) {
            element = Py_BuildValue("d", A_bar[i][j]);
            PyList_SetItem(A_bar_row, j, element);
        }
        PyList_SetItem(A_bar_py, i, A_bar_row);
    }
    Q_bar_py = PyList_New(n);
    for (i = 0; i < n; i++) {
        Q_bar_row = PyList_New(n);
        for (j = 0; j < n; j++) {
            element = Py_BuildValue("d", Q_bar[i][j]);
            PyList_SetItem(Q_bar_row, j, element);
        }
        PyList_SetItem(Q_bar_py, i, Q_bar_row);
    }
    free_2d(&A_bar, n);
    free_2d(&Q_bar, n);
    free_2d(&pts, n);
    return Py_BuildValue("OO", A_bar_py, Q_bar_py);
}

static PyMethodDef capiMethods2[] = {
        {"qr_iteration_cpython",
                (PyCFunction) qr_iteration,
                     METH_VARARGS,
                PyDoc_STR("")},
        {NULL, NULL, 0, NULL}
};

static struct PyModuleDef moduledef = {
        PyModuleDef_HEAD_INIT, "mymdcmp", NULL, -1, capiMethods2
};

/*
 * Method: PyInit_mymdcmp
 * Purpose: Initializes the module.
 */
PyMODINIT_FUNC PyInit_mymdcmp(void) {
    PyObject *m;
    m = PyModule_Create(&moduledef);
    if (!m) {
        return NULL;
    }
    return m;
}