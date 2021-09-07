#################################
# Your name: Omer Ronen
#################################

# Please import and use stuff only from the packages numpy, sklearn, matplotlib
import numpy as np
import matplotlib.pyplot as plt
from sklearn import svm
from sklearn.datasets import make_blobs

"""
Please use the provided functions signature for the SVM implementation.
Feel free to add functions and other code, and submit this file with the name svm.py
"""

# generate points in 2D
# return training_data, training_labels, validation_data, validation_labels
def get_points():
    X, y = make_blobs(n_samples=120, centers=2, random_state=0, cluster_std=0.88)
    return X[:80], y[:80], X[80:], y[80:]


def create_plot(X, y, clf):
    plt.clf()

    # plot the data points
    plt.scatter(X[:, 0], X[:, 1], c=y, s=30, cmap=plt.cm.PiYG)

    # plot the decision function
    ax = plt.gca()
    xlim = ax.get_xlim()
    ylim = ax.get_ylim()

    # create grid to evaluate model
    xx = np.linspace(xlim[0] - 2, xlim[1] + 2, 30)
    yy = np.linspace(ylim[0] - 2, ylim[1] + 2, 30)
    YY, XX = np.meshgrid(yy, xx)
    xy = np.vstack([XX.ravel(), YY.ravel()]).T
    Z = clf.decision_function(xy).reshape(XX.shape)

    # plot decision boundary and margins
    ax.contour(XX, YY, Z, colors='k', levels=[-1, 0, 1], alpha=0.5,
               linestyles=['--', '-', '--'])


def train_three_kernels(X_train, y_train, X_val, y_val):
    """
    Returns: np.ndarray of shape (3,2) :
                A two dimensional array of size 3 that contains the number of support vectors for each class(2) in the three kernels.
    """
    #rbf kernel
    clf_rbf = svm.SVC(C=1000,kernel='rbf')
    rbf_trained = clf_rbf.fit(X_train, y_train)
    supp_rbf = rbf_trained.n_support_
    create_plot(X_val, y_val, rbf_trained)
    #linear kernel
    clf_lin = svm.SVC(C=1000,kernel='linear')
    lin_trained = clf_lin.fit(X_train, y_train)
    supp_lin = lin_trained.n_support_
    #create_plot(X_val, y_val, lin_trained)
    #quadratic kernel
    clf_quad = svm.SVC(C=1000,kernel='poly',degree=2)
    quad_trained = clf_quad.fit(X_train, y_train)
    supp_quad = quad_trained.n_support_
    #create_plot(X_val, y_val, quad_trained)
    #plt.show()
    return np.array([supp_lin, supp_quad, supp_rbf])



def linear_accuracy_per_C(X_train, y_train, X_val, y_val):
    """
        Returns: np.ndarray of shape (11,) :
                    An array that contains the accuracy of the resulting model on the VALIDATION set.
    """
    c_vals = np.float_power(10, np.arange(-5, 6))
    validation_accuracy = []
    train_accuracy = []
    for c in c_vals:
        lin_rbf = svm.SVC(C=c, kernel='linear')
        trained_lin = lin_rbf.fit(X_train, y_train)
        validation_accuracy.append(trained_lin.score(X_val, y_val))
        train_accuracy.append(trained_lin.score(X_train, y_train))
    plt.plot(np.log10(c_vals), validation_accuracy, 'r')
    plt.plot(np.log10(c_vals), train_accuracy, 'b')
    plt.xlabel('log10(C)')
    plt.ylabel('Accuracy')
    #plt.show()
    return np.array(validation_accuracy)



def rbf_accuracy_per_gamma(X_train, y_train, X_val, y_val):
    """
        Returns: np.ndarray of shape (11,) :
                    An array that contains the accuracy of the resulting model on the VALIDATION set.
    """
    mult = 1
    gamma_exp = 0
    gamma_validation_accuracies = []
    gamma_training_accuracies = []
    for res in np.arange(4):
        best_del = 0
        best_accuracy = 0
        for tol in np.arange(-5, 6):
            cur_exp = gamma_exp + mult * tol
            cur_gamma = np.float_power(10, cur_exp)
            clf_rbf = svm.SVC(C=10, kernel='rbf', gamma=cur_gamma)
            rbf_trained = clf_rbf.fit(X_train, y_train)
            cur_accuracy = rbf_trained.score(X_val, y_val)
            if cur_accuracy >= best_accuracy:
                best_accuracy = cur_accuracy
                best_del = mult * tol
            if res == 0:
                gamma_validation_accuracies.append(cur_accuracy)
                gamma_training_accuracies.append(rbf_trained.score(X_train, Y_train))
        gamma_exp += best_del
        mult /= 10
    plt.plot(np.arange(-5, 6), gamma_validation_accuracies, 'r')
    plt.plot(np.arange(-5, 6), gamma_training_accuracies, 'b')
    plt.xlabel('log10(Gamma)')
    plt.ylabel('Accuracy')
    #plt.show()
    #print(gamma_exp) best gamma exponent
    return np.array(gamma_validation_accuracies)



if __name__ == '__main__':
    X_train, Y_train, X_val, Y_val = get_points()
    #print(train_three_kernels(X_train, Y_train, X_val, Y_val))
    #print(linear_accuracy_per_C(X_train, Y_train, X_val, Y_val))
    #print(rbf_accuracy_per_gamma(X_train, Y_train, X_val, Y_val))
