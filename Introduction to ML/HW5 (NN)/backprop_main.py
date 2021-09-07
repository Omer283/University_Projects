import backprop_dataimport backprop_networkimport numpy as npimport matplotlib.pyplot as pltimport matplotlib.cm as cmimport itertoolsdef foo_a():    rates = [0.001, 0.01, 0.1, 1, 10, 100]    training_data, test_data = backprop_data.load(train_size=10000, test_size=5000)    train_acc = []    train_loss = []    test_acc = []    tacc = []    tloss = []    teacc = []    for learn_rate in rates:        net = backprop_network.Network([784, 40, 10])        train_acc, train_loss, test_acc = \            net.SGD(training_data, epochs=30, mini_batch_size=10, learning_rate=learn_rate, test_data=test_data)        tacc.append(train_acc)        tloss.append(train_loss)        teacc.append(test_acc)    print(tacc)    print(tloss)    print(teacc)    colors = iter(cm.rainbow(np.linspace(0, 1, len(rates))))    for vec in tacc:        plt.plot(range(30), vec, color=next(colors))    plt.show()    plt.close()    colors = iter(cm.rainbow(np.linspace(0, 1, len(rates))))    for vec in tloss:        plt.plot(range(30), vec, color=next(colors))    plt.show()    plt.close()    colors = iter(cm.rainbow(np.linspace(0, 1, len(rates))))    for vec in teacc:        plt.plot(range(30), vec, color=next(colors))    plt.show()    plt.close()def foo_b():    training_data, test_data = backprop_data.load(train_size=50000, test_size=10000)    net = backprop_network.Network([784, 40, 10])    net.SGD(training_data, epochs=30, mini_batch_size=10, learning_rate=0.1, test_data=test_data)if __name__ == '__main__':    #part a    #foo_a()    #part b    #foo_b()    #part c    training_data, test_data = backprop_data.load(train_size=50000, test_size=10000)    net = backprop_network.Network([784, 40, 10])    net.SGD(training_data, epochs=30, mini_batch_size=10, learning_rate=0.05, test_data=test_data)