# Name: Omer Ronen
# Mail: omerronen2@mail.tau.ac.il
# ID: 212775803
# Exercise: multivariable gaussian
import matplotlib
import numpy as np
import matplotlib.cm as cm
import matplotlib.pyplot as plt


def pdf(X, Y, mu, sigma):
    ret = np.zeros_like(X)
    coeff = 1 / (2 * np.pi * np.linalg.det(sigma))
    for i in range(len(X)):
        for j in range(len(X[i])):
            x, y = X[i][j], Y[i][j]
            vec = np.array([x-mu[0], y-mu[1]])
            inside_power = -0.5 * (vec.T @ np.linalg.inv(sigma) @ vec)
            exponent = np.exp(inside_power)
            ret[i][j] = coeff * exponent
    return ret


def decision_bound(x_vals, p):
    return -2 * x_vals + 2 * np.log((1-p)/p) + 1.5

# part b
delta = 0.025
left = -4
right = 6
x = np.arange(left, right, delta)
y = np.arange(left, right, delta)
X, Y = np.meshgrid(x, y)
mu=[1,1]
sigma=[[1,0],[0,2]]
d=2
Z = pdf(X,Y,mu,sigma)
plt.contour(X,Y,Z)
# part e
x_vals = np.linspace(left, right, 100)
y_vals1 = decision_bound(x_vals, 0.25)
y_vals2 = decision_bound(x_vals, 0.5)
y_vals3 = decision_bound(x_vals, 0.99)
plt.plot(x_vals, y_vals1, '-r')
plt.plot(x_vals, y_vals2, '-y')
plt.plot(x_vals, y_vals3, '-g')
plt.show()
