#################################
# Your name: Omer Ronen
#################################

import numpy as np
import matplotlib.pyplot as plt
import intervals


class Assignment2(object):
    """Assignment 2 skeleton.

    Please use these function signatures for this assignment and submit this file, together with the intervals.py.
    """

    def sample_from_D(self, m):
        """Sample m data samples from D.
        Input: m - an integer, the size of the data sample.

        Returns: np.ndarray of shape (m,2) :
                A two dimensional array of size m that contains the pairs where drawn from the distribution P.
        """
        # TODO: Implement me
        data = np.zeros([m, 2])
        for i in range(m):
            x = np.random.uniform(0, 1)
            if 0 <= x <= 0.2 or 0.4 <= x <= 0.6 or 0.8 <= x <= 1:
                y = np.random.binomial(1, 0.8)
            else:
                y = np.random.binomial(1, 0.1)
            data[i] = [x, y]
        return data[np.argsort(data[:, 0])] #todo check


    def experiment_m_range_erm(self, m_first, m_last, step, k, T):
        """Runs the ERM algorithm.
        Calculates the empirical error and the true error.
        Plots the average empirical and true errors.
        Input: m_first - an integer, the smallest size of the data sample in the range.
               m_last - an integer, the largest size of the data sample in the range.
               step - an integer, the difference between the size of m in each loop.
               k - an integer, the maximum number of intervals.
               T - an integer, the number of times the experiment is performed.

        Returns: np.ndarray of shape (n_steps,2).
            A two dimensional array that contains the average empirical error
            and the average true error for each m in the range accordingly.
        """
        # TODO: Implement the loop
        eshavg = []
        ephavg = []
        for m in range(m_first, m_last + 1, step):
            eshtot = 0
            ephtot = 0
            for t in range(T):
                data = self.sample_from_D(m)
                hypo, esh = intervals.find_best_interval(data[:,0], data[:,1], k)
                esh /= m
                eph = self.find_true_error(hypo)
                eshtot += esh
                ephtot += eph
            eshavg.append(eshtot / T)
            ephavg.append(ephtot / T)
        #plt.plot(range(m_first, m_last + 1, step), eshavg)
        #plt.plot(range(m_first, m_last + 1, step), ephavg)
        #plt.show()
        return np.ndarray([eshavg, ephavg])

    def experiment_k_range_erm(self, m, k_first, k_last, step):
        """Finds the best hypothesis for k= 1,2,...,10.
        Plots the empirical and true errors as a function of k.
        Input: m - an integer, the size of the data sample.
               k_first - an integer, the maximum number of intervals in the first experiment.
               m_last - an integer, the maximum number of intervals in the last experiment.
               step - an integer, the difference between the size of k in each experiment.

        Returns: The best k value (an integer) according to the ERM algorithm.
        """
        data = self.sample_from_D(m)
        esh = []
        eph = []
        bsterm = 193149
        bst_k=-1
        for k in range(k_first, k_last + 1, step):
            hypo, err = intervals.find_best_interval(data[:, 0], data[:, 1], k)
            esh.append(err / m)
            if err/m<bsterm:
                bsterm=err/m
                bstk=k
            eph.append(self.find_true_error(hypo))
        #plt.plot(range(k_first, k_last + 1, step), esh)
        #plt.plot(range(k_first, k_last + 1, step), eph)
        #plt.show()
        #TODO: Implement the loop
        return bst_k

    def experiment_k_range_srm(self, m, k_first, k_last, step):
        """Run the experiment in (c).
        Plots additionally the penalty for the best ERM hypothesis.
        and the sum of penalty and empirical error.
        Input: m - an integer, the size of the data sample.
               k_first - an integer, the maximum number of intervals in the first experiment.
               m_last - an integer, the maximum number of intervals in the last experiment.
               step - an integer, the difference between the size of k in each experiment.

        Returns: The best k value (an integer) according to the SRM algorithm.
        """
        data = self.sample_from_D(m)
        esh = []
        eph = []
        pen = []
        pensum = []
        for k in range(k_first, k_last + 1, step):
            hypo, err = intervals.find_best_interval(data[:, 0], data[:, 1], k)
            esh.append(err / m)
            eph.append(self.find_true_error(hypo))
            pen.append(self.get_penalty(m, k, 0.1))
            pensum.append(err / m + self.get_penalty(m, k, 0.1))
        #plt.plot(range(k_first, k_last + 1, step), esh, 'r')
        #plt.plot(range(k_first, k_last + 1, step), eph, 'b')
        #plt.plot(range(k_first, k_last + 1, step), pen, 'g')
        #plt.plot(range(k_first, k_last + 1, step), pensum, 'o')
        #plt.show()
        i = 0
        mn=10000000
        bstk = -1
        for k in range(k_first, k_last + 1, step):
            if pensum[i] < mn:
                mn=pensum[i]
                bstk=k
            i+=1
        return bstk


    def cross_validation(self, m):
        """Finds a k that gives a good test error.
        Input: m - an integer, the size of the data sample.

        Returns: The best k value (an integer) found by the cross validation algorithm.
        """
        data = self.sample_from_D(m)
        train = data[:int(0.8*m)]
        holdout = data[int(0.8*m):]
        best_hypo = []
        best_esh = 1
        best_k = -1
        for k in range(1,11):
            hypo, err = intervals.find_best_interval(train[:, 0], train[:, 1], k)
            esh = self.find_empirical_error(hypo, holdout)
            if esh < best_esh:
                best_hypo = hypo
                best_esh = esh
                best_k = k
        # TODO: Implement me
        print(best_hypo)
        return best_k
        pass

    #################################
    # Place for additional methods
    def find_empirical_error(self, hypothesis, labeled_data):
        data_points = len(labeled_data)
        wrong = 0
        for pt in labeled_data:
            found = 0
            for t in hypothesis:
                if pt[0] >= t[0] and pt[0] <= t[1]:
                    found = 1
            if abs(found - pt[1]) >= 0.001: #wrong label
                wrong += 1
        return wrong / data_points

    def intersection_length(self, i1, i2):
        if i1[1] <= i2[0] or i2[1] <= i1[0]:  # no intersection / one point intersection
            return 0
        else:
            return min(i1[1], i2[1]) - max(i1[0], i2[0])

    def find_true_error(self, intervals):
        pos = np.array([[0, 0.2], [0.4, 0.6], [0.8, 1]])
        neg = np.array([[0.2, 0.4], [0.6, 0.8]])
        poslen = 0.6
        neglen = 0.4
        pos_intersection = 0
        neg_intersection = 0
        for x in intervals:
            for y in pos:
                pos_intersection += self.intersection_length(x, y)
            for y in neg:
                neg_intersection += self.intersection_length(x, y)
        cpos_intersection = poslen - pos_intersection
        cneg_intersection = neglen - neg_intersection
        ret = pos_intersection * 0.2 + neg_intersection * 0.9 + cpos_intersection * 0.8 + cneg_intersection * 0.1
        return ret

    def get_penalty(self, n, k, delta):
        return 2 * np.sqrt((2 * k + np.log(2 / delta)) / n)
    #################################


if __name__ == '__main__':
    ass = Assignment2()
    ass.experiment_m_range_erm(10, 100, 5, 3, 100)
    ass.experiment_k_range_erm(1500, 1, 10, 1)
    ass.experiment_k_range_srm(1500, 1, 10, 1)
    ass.cross_validation(1500)

