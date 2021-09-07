# Intro to ML
# Omer Ronen
# Name: Omer Ronen
# Mail: omerronen2@mail.tau.ac.il
# ID: 212775803
# Exercise: k nearest neighbors
import numpy as np
from sklearn.datasets import fetch_openml
import matplotlib.pyplot as plt
mnist = fetch_openml('mnist_784')
data = mnist['data'].values
labels = mnist['target']

idx = np.random.RandomState(0).choice(70000, 11000)
train = data[idx[:10000], :].astype(int)
train_labels = labels[idx[:10000]]
test = data[idx[10000:], :].astype(int)
test_labels = labels[idx[10000:]]
my_test_labels = test_labels.to_numpy()


# part a
def find_label_query(my_train_images, my_train_labels, query_image, k):
    if np.size(my_train_images,0) < k or np.size(my_train_images,0) != np.size(my_train_labels,0):# validity check
        return -1
    labels_distances = np.array([np.linalg.norm(np.subtract(my_train_images, query_image), axis=1), my_train_labels])
    sorted_labels = labels_distances[:,labels_distances[0].argsort()]
    return np.bincount(sorted_labels[1][:k].astype(np.intc)).argmax()


def find_accuracy(n, k):
    print(n)
    sample_train_images = train[:n]
    sample_train_labels = train_labels[:n].to_numpy()
    loss, tot = 0, 0
    for i in range(np.size(my_test_labels,0)):
        if find_label_query(sample_train_images, sample_train_labels, test[i], k) != int(my_test_labels[i]):
            loss += 1
        tot += 1
    return 100 * (1 - loss / tot)


# part b
print(find_accuracy(1000,10))
# part c
k_values = np.arange(1,101)
accuracy = np.array([find_accuracy(1000, k) for k in k_values])
plt.plot(k_values, accuracy)
plt.show()
# part d
n_values = np.arange(100, 5001,100)
accuracy = np.array([find_accuracy(n, 1) for n in n_values])
plt.plot(n_values, accuracy)
plt.show()
