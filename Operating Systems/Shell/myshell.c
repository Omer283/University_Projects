#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>
#include <fcntl.h>

//taken from http://www.microhowto.info/howto/reap_zombie_processes_using_a_sigchld_handler.html
void handle_sigchld(int sig) {
    int saved_errno = errno;
    while (waitpid((pid_t)(-1), 0, WNOHANG) > 0) {}
    errno = saved_errno;
}

//taken from http://www.microhowto.info/howto/reap_zombie_processes_using_a_sigchld_handler.html
void zombie_killer(int on) {
    if (on) {
        struct sigaction sa;
        sa.sa_handler = &handle_sigchld;
        sigemptyset(&sa.sa_mask);
        sa.sa_flags = SA_RESTART | SA_NOCLDSTOP;
        if (sigaction(SIGCHLD, &sa, 0) == -1) {
            perror(0);
            exit(1);
        }
    }
    else {
        signal(SIGCHLD, SIG_DFL);
    }
}

int prepare(void) {
    signal(SIGINT, SIG_IGN);
    zombie_killer(1);
    return 0;
}

int finalize(void) {
    return 0;
}

//returns a position if there is output redirection, otherwise -1
int outCommand(int count, char **arglist) {
    for (int i = 0; i < count; i++) {
        if (!strcmp(arglist[i], ">")) {
            return i;
        }
    }
    return -1;
}

//returns a position if there is piping, otherwise -1
int pipeCommand(int count, char **arglist) {
    for (int i = 0; i < count; i++) {
        if (!strcmp(arglist[i], "|")) {
            return i;
        }
    }
    return -1;
}

//returns 1 iff it is a background command
int bgCommand(int count, char **arglist) {
    return (strcmp(arglist[count - 1], "&") == 0 ? 1 : 0);
}

//responsible for processing commands with '>'
int process_out(int count1, char** arglist1, int count2, char** arglist2) {
    if (count2 != 1) { //something is wrong
        perror("Specify a file to output to");
        return 0;
    }
    char* foutput = arglist2[0]; //output file
    int fd = open(foutput, O_WRONLY | O_CREAT, 0777);
    int pid = fork();
    if (pid < 0) {
        perror("Failed forking");
        close(fd);
        return 0;
    }
    else if (pid == 0) { //child
        signal(SIGINT, SIG_DFL); //sigint per usual
        if (dup2(fd, 1) == -1) {
            perror("dup2 failed");
            exit(EXIT_FAILURE);
        }
        close(fd);
        if (execvp(arglist1[0], arglist1) == -1) {
            perror("Failed executing command");
            exit(EXIT_FAILURE); //error in execvp
        }
    }
    else { //parent
        signal(SIGINT, SIG_IGN); //sigint ignored
        close(fd);
        int status = 0;
        waitpid(pid, &status, WUNTRACED); //waits for child
    }
    return 1;
}

//responsible for processing commands with '|'
int process_pipe(int count1, char** arglist1, int count2, char** arglist2) {
    //copied code from recitation partially
    int pipefds[2];
    if (pipe(pipefds) == -1) {
        perror("Failed creating pipe");
        return 0;
    }
    int readerfd = pipefds[0];
    int writerfd = pipefds[1];
    int pid1 = fork();
    if (pid1 < 0) {
        perror("Failed forking");
        close(readerfd);
        close(writerfd);
        return 0;
    } else if (pid1 == 0) {
        // Child
        signal(SIGINT, SIG_DFL);
        close(readerfd); // close read side
        if (dup2(writerfd, STDOUT_FILENO) == -1) {
            perror("dup2 failed");
            exit(EXIT_FAILURE);
        }
        if (execvp(arglist1[0], arglist1) == -1) {
            perror("Failed executing command");
            close(writerfd);
            exit(EXIT_FAILURE);
        }
        close(writerfd);
    } else {
        // Parent
        signal(SIGINT, SIG_IGN);
        close(writerfd); // close write side
        int pid2 = fork();
        if (pid2 < 0) {
            perror("Failed forking\n");
            close(readerfd);
            return 0;
        }
        else if (pid2 == 0) {
            signal(SIGINT, SIG_DFL);
            if (dup2(readerfd, STDIN_FILENO) == -1) {
                perror("dup2 failed");
                exit(EXIT_FAILURE);
            }
            close(readerfd);
            if (execvp(arglist2[0], arglist2) == -1) {
                perror("Failed executing command");
                close(writerfd);
                exit(EXIT_FAILURE);
            }
        }
        else { //main process
            signal(SIGINT, SIG_IGN); //ignore sigint
            int s1 = 0, s2 = 0; //wait for both children to end
            waitpid(pid1, &s1, WUNTRACED);
            waitpid(pid2, &s2, WUNTRACED);
            close(readerfd); //close
        }
    }
    return 1;
}

//handles regular commands or background commands (specified by flag)
int process_reg_bg(int count, char** arglist, int isBg)  {
    int pid = fork();
    if (pid < 0) { //fork fail
        perror("Failed forking");
        return 0;
    }
    else if (pid == 0) { //child
        signal(SIGINT, (isBg ? SIG_IGN : SIG_DFL)); //determine whether it can get interrupted
        if (execvp(arglist[0], arglist) == -1) { //failed execution
            perror("Failed executing command");
            exit(EXIT_FAILURE);
        }
    }
    else {
        signal(SIGINT, SIG_IGN);
        int status = 0;
        if (!isBg) {
            waitpid(pid, &status, WUNTRACED);
        }
    }
    return 1;
}

int process_arglist(int count, char** arglist) {
    int pos = -1, isBg = 0;
    if ((pos = pipeCommand(count, arglist)) != -1) { //if it has piping
        arglist[pos] = NULL;
        return process_pipe(pos, arglist, count - pos - 1, arglist + pos + 1);
    }
    else if ((pos = outCommand(count, arglist)) != -1) { //if it has output redirection
        arglist[pos] = NULL;
        return process_out(pos, arglist, count - pos - 1, arglist + pos + 1);
    }
    else { //nothing or background
        if (bgCommand(count, arglist)) {
            isBg = 1;
            arglist[count - 1] = NULL;
            count--;
        }
        return process_reg_bg(count, arglist, isBg);
    }
}
