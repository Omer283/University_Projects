#include <sys/stat.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdio.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <fcntl.h>
 
int sockfd = -1, fd = -1;
 
void exit_error(char* errstr) {
    perror(errstr);
    if (sockfd >= 0) {
        close(sockfd);
    }
    if (fd >= 0) {
        close(fd);
    }
    exit(1);
}
 
int connect_to_serv(char* ip_addr, int port) { //copied from recitation (shoutout to who wrote this!)
    struct sockaddr_in serv_addr; // where we Want to get to
    if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        exit_error("Error creating socket");
    }
    memset(&serv_addr, 0, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(port); // Note: htons for endiannes
    int errCode = inet_pton(AF_INET, ip_addr, &serv_addr.sin_addr);
    if (errCode < 0) {
        exit_error("Something wrong happened with inet_pton");
    }
    else if (errCode == 0) {
        exit_error("Not in presentation format");
        exit(1);
    }
    // connect socket to the target address
    if (connect(sockfd, (struct sockaddr*) &serv_addr, sizeof(serv_addr)) < 0) {
        exit_error("Error: Connect Failed");
        exit(1);
    }
    return sockfd;
}
 
int sendInt(uint32_t num) { //sends int
    for (int i = 0; i < 4; i++) {
        char b = (num & ((1 << 8) - 1));
        if (write(sockfd, &b, sizeof(b)) < 0) {
            exit_error("Something wrong with sending int");
            exit(1);
        }
        num >>= 8;
    }
    return 0;
}
 
uint32_t readInt() { //reads int byte byte
    uint32_t ans = 0, mul = 1;
    for (int i = 0; i < 4; i++) {
        char b;
        if (read(sockfd, &b, sizeof(b)) < 0) {
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
    if (argc != 3 + 1) { //invalid argument count
        exit_error("Wrong argument count!");
        return 1;
    }
    int port = atoi(argv[2]);
    char* fname = argv[3], *ip_addr = argv[1];
    struct stat file_stat;
    if (stat(fname, &file_stat)) {
        exit_error("stat failed!");
        return 1;
    }
    uint32_t N = file_stat.st_size;
    //open the file
    if ((fd = open(fname, O_RDONLY)) < 0) {
        exit_error("Failed opening file");
        return 1;
    }
    //create tcp connection
    if ((sockfd = connect_to_serv(ip_addr, port)) < 0) {
        return 1;
    }
    //send N
    sendInt(N);
    //send bytes
    for (int i = 0; i < N; i++) {
        char c;
        if ((read(fd, &c, 1)) < 0) {
            exit_error("Failed reading from file");
        }
        if (write(sockfd, &c, 1) < 0) {
            exit_error("Error in sending a byte");
        }
    }
    //receive the answer
    uint32_t C = readInt();
    printf("# of printable characters: %u\n", C);
    close(sockfd);
    close(fd);
    return 0;
}