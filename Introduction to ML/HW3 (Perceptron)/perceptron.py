#################################
# Your name: Omer Ronen
#################################

# Please import and use stuff only from the packages numpy, sklearn, matplotlib

import numpy as np
import matplotlib.pyplot as plt
import numpy.random
from sklearn.datasets import fetch_openml
import sklearn.preprocessing
"""
Please use the provided function signature for the perceptron implementation.
Feel free to add functions and other code, and submit this file with the name perceptron.py
"""


def helper():
    mnist = fetch_openml('mnist_784', as_frame=False)
    data = mnist['data']
    labels = mnist['target']

    neg, pos = "0", "8"
    train_idx = numpy.random.RandomState(0).permutation(np.where((labels[:60000] == neg) | (labels[:60000] == pos))[0])
    test_idx = numpy.random.RandomState(0).permutation(np.where((labels[60000:] == neg) | (labels[60000:] == pos))[0])

    train_data_unscaled = data[train_idx[:6000], :].astype(float)
    train_labels = (labels[train_idx[:6000]] == pos) * 2 - 1

    validation_data_unscaled = data[train_idx[6000:], :].astype(float)
    validation_labels = (labels[train_idx[6000:]] == pos) * 2 - 1

    test_data_unscaled = data[60000 + test_idx, :].astype(float)
    test_labels = (labels[60000 + test_idx] == pos) * 2 - 1

    # Preprocessing
    train_data = sklearn.preprocessing.scale(train_data_unscaled, axis=0, with_std=False)
    validation_data = sklearn.preprocessing.scale(validation_data_unscaled, axis=0, with_std=False)
    test_data = sklearn.preprocessing.scale(test_data_unscaled, axis=0, with_std=False)
    return train_data, train_labels, validation_data, validation_labels, test_data, test_labels


train_data, train_labels, validation_data, validation_labels, test_data, test_labels = helper()


def perceptron(data, labels):
    n = data.shape[0]
    d = data[0].shape[0]
    w = np.zeros(d)
    for i in np.arange(n):
        normalized_data = data[i] / np.linalg.norm(data[i])
        if np.dot(normalized_data, w) >= 0:
            predict = 1
        else:
            predict = -1
        if predict != labels[i]:
            w -= predict * normalized_data
    return w
    """
	returns: nd array of shape (data.shape[1],) or (data.shape[1],1) representing the perceptron classifier
    """
# TODO: Implement me

#################################

# Place for additional code


def calc_accuracy(w, test_data, test_labels):
    n = test_data.shape[0]
    correct = 0
    for i in range(n):
        prod = np.dot(w, test_data[i])
        if prod >= 0:
            predict = 1
        else:
            predict = -1
        if predict == test_labels[i]:
            correct += 1
    return correct / n


def get_misclassified(w, test_data, test_labels, idx=1):
    n = test_data.shape[0]
    cnt = 0
    for i in range(n):
        prod = np.dot(w, test_data[i])
        if prod >= 0:
            predict = 1
        else:
            predict = -1
        if predict != test_labels[i]:
            cnt += 1
            if cnt == idx:
                return test_data[i], test_labels[i]
    return None


def part_a():
    n_values = np.array([5, 10, 50, 100, 500, 1000, 5000])
    table_cell = []
    for n in n_values:
        my_train = train_data[:n]
        my_train_label = train_labels[:n]
        pair_arr = np.array([[my_train[i], my_train_label[i]] for i in np.arange(n)],dtype=object)
        accuracy_arr = np.zeros(100)
        for test_id in np.arange(100):
            np.random.shuffle(pair_arr)
            data_arr = pair_arr[:,0]
            labels_arr = pair_arr[:,1]
            w = perceptron(data_arr, labels_arr)
            acc = calc_accuracy(w, test_data,test_labels)
            accuracy_arr[test_id]=acc
        res = [np.mean(accuracy_arr), np.percentile(accuracy_arr, 5), np.percentile(accuracy_arr, 95)]
        table_cell.append(res)
    rows = ['5', '10', '50', '100', '500', '1000', '5000']
    columns = ['avg', '5%', '95%']
    fig = plt.figure(1)
    fig.subplots_adjust(left=0.2,top=0.8,wspace=1)
    ax = plt.subplot2grid((4,3),(0,0),colspan=5,rowspan=5)
    ax.table(cellText=table_cell,rowLabels=rows,colLabels=columns,loc="upper center")
    ax.axis("off")
    plt.show()


def part_b():
    w = perceptron(train_data, train_labels)
    plt.imshow(np.reshape(w, (28, 28)), interpolation='nearest')
    plt.show()


def part_c():
    w = perceptron(train_data, train_labels)
    print(calc_accuracy(w, test_data, test_labels))


def part_d():
    w = perceptron(train_data, train_labels)
    im, la = get_misclassified(w, test_data, test_labels, 3) #I chose the third mistake, as it illustrates my point more clearly
    plt.imshow(np.reshape(im, (28, 28)), interpolation='nearest')
    plt.show()


#################################


if __name__ == '__main__':
    #part_a()
    #part_b()
    #part_c()
    #part_d()
    print("If you have reached this part, it means that you have run the main of the file without running any of part_a/b/c/d")