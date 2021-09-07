# Name: Omer Ronen
# Mail: omerronen2@mail.tau.ac.il
# ID: 212775803
# Exercise: Hoeffding bounds
import numpy as np
import matplotlib.pyplot as plt

# part a
N = 200000
n = 20
X = np.random.binomial(1, 0.5, (N, n))
means = np.average(X, axis=1)
print(means)
# part b
eps = np.linspace(0,1,50)
probs = [np.count_nonzero(np.abs(means - 0.5) > tol) / N for tol in eps]
plt.plot(eps,probs,'-r')
# part c
hoeffding_values = 2 * np.exp(-2 * n * eps * eps)
plt.plot(eps,hoeffding_values)
plt.show()