#
# Module: tasks
# Purpose: Responsible for the invokes
#
import webbrowser
import time
from invoke import task, call
from timeit import timeit

@task
def build(c):
    c.run("python3.8.5 setup.py build_ext --inplace")


@task(aliases=['del'])
def delete(c):
    c.run("rm *mykmeanssp*.so")
    c.run("rm *mymdcmp*.so")


@task
def run(c, k=0, n=0, Random=True):
    build(c)
    import main as mn
    mn.init(k, n, Random)