#define DS_SIZE 256
#define QUEUE_SIZE 10
#define PRINTABLE_LOWER 32
#define PRINTABLE_UPPER 126
#include <stdlib.h>
#include <string.h>
#include <signal.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdio.h>
 
unsigned int pcc_total[DS_SIZE];
int shouldStop = 0; //detects sigints
int listenfd = -1, connfd = -1, curProcessing = 0;
 
void exit_error(char* errstr) {
    perror(errstr);
    if (listenfd >= 0) {
        close(listenfd);
    }
    if (connfd >= 0) {
        close(connfd);
    }
    exit(1);
}
 
void closeServer() {
    if (curProcessing) { //if processing a client
        shouldStop = 1;
    }
    else { //otherwise
        if (listenfd >= 0) {
            close(listenfd);
        }
        if (connfd >= 0) {
            close(connfd);
        }
        for (int c = PRINTABLE_LOWER; c <= PRINTABLE_UPPER; c++) {
            printf("char '%c': %u times\n", c, pcc_total[c]);
        }
        exit(0);
    }
}
 
void mySignalHandler(int signum, siginfo_t* info, void* ptr) {
    closeServer();
}
 
void prepHandler() { //override handler
    struct sigaction act;
    memset(&act, 0, sizeof(act));
    act.sa_sigaction = mySignalHandler;
    if (sigaction(SIGINT, &act, NULL)) {
        exit_error("Error setting up signal handler");
        exit(1);
    }
}
 
int serverSetup(int port) { //copied from recitation haha! thank you to who wrote this!
    struct sockaddr_in serv_addr;
    socklen_t addrsize = sizeof(struct sockaddr_in );
    if ((listenfd = socket(AF_INET,SOCK_STREAM, 0 )) < 0) {
        exit_error("Error setting up server");
        return -1;
    }
    memset( &serv_addr, 0, addrsize );
    serv_addr.sin_family = AF_INET;
    // INADDR_ANY = any local machine address
    serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    serv_addr.sin_port = htons(port);
    int dummy;
    if (setsockopt(listenfd, SOL_SOCKET, SO_REUSEADDR, &dummy, sizeof(dummy)) < 0) {
        exit_error("Error: setsockopt");
        return -1;
    }
    if (bind(listenfd, (struct sockaddr*) &serv_addr, addrsize)) {
        exit_error("Error: Bind Failed.");
        return -1;
    }
    if (listen(listenfd, QUEUE_SIZE)) {
        exit_error("Error: Listen failed");
        return -1;
    }
    return listenfd;
}
 
int sendInt(uint32_t num) { //send int byte byte
    for (int i = 0; i < 4; i++) {
        char b = (num & ((1 << 8) - 1));
        if (write(connfd, &b, sizeof(b)) < 0) {
            exit_error("Something wrong with sending int");
            exit(1);
        }
        num >>= 8;
    }
    return 0;
}
 
uint32_t readInt() { //read int byte byte
    uint32_t ans = 0, mul = 1;
    for (int i = 0; i < 4; i++) {
        char b;
        if (read(connfd, &b, sizeof(b)) < 0) {
            exit_error("Error reading int");
            exit(1);
        }
        int ib = (int)b;
        if (ib < 0) {
            ib = 256 + ib;
        }
        ans += ib * mul;
        mul <<= 8;
    }
    return ans;
}
 
int main(int argc, char* argv[]) {
    if (argc != 2) {
        exit_error("Wrong argument count!");
        return 1;
    }
    prepHandler();
    int port = atoi(argv[1]);
    if ((listenfd = serverSetup(port)) < 0) {
        return 1;
    }
    while (!shouldStop) {
        if ((connfd = accept(listenfd, NULL, NULL)) < 0) {
            exit_error("Error: accept failed");
            return 1;
        }
        curProcessing = 1; //processing client
        uint32_t N = -1;
        N = readInt();
        uint32_t currPrintables = 0;
        int shouldCount = 1;
        uint32_t cur_pcc[DS_SIZE]; //array for curretn file
        memset(cur_pcc, 0, sizeof(cur_pcc[0]) * DS_SIZE);
        for (int i = 0; i < N; i++) {
            char c;
            int retval = read(connfd, &c, sizeof(c));
            if (retval < 0) { //some error
                exit_error("Some tcp error...");
                return 1;
            }
            else if (retval == 0) { //client terminated
                perror("Client terminated");
                shouldCount = 0;
                break;
            }
            int ic = (int)c;
            if (ic >= PRINTABLE_LOWER && ic <= PRINTABLE_UPPER) { //update counter
                cur_pcc[ic]++;
                currPrintables++;
            }
        }
        if (shouldCount) { //if reached the end of file
            for (int c = PRINTABLE_LOWER; c <= PRINTABLE_UPPER; c++) {
                pcc_total[c] += cur_pcc[c];
            }
            sendInt(currPrintables);
        }
        close(connfd);
        curProcessing = 0;
        connfd = -1;
    }
    closeServer();
    return 0;
}
