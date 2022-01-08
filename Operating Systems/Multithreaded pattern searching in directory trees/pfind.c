#include <stdio.h>
#include <malloc.h>
#include <stdlib.h>
#include <dirent.h>
#include <string.h>
#include <sys/stat.h>
#include <unistd.h>
#include <pthread.h>
#include <stdatomic.h>

//TODO need to check malloc fail?

void usleep(int x);

//////////////////////////////////////////////////
//////////////Queue related///////////////////////
//////////////////////////////////////////////////

typedef struct QueueVal QueueVal;
struct QueueVal { //serves multi purpose.
    int idx;
    char* dir;
};

typedef atomic_int AtomicInteger;
typedef struct QueueNode QueueNode;
typedef struct Queue Queue;

struct QueueNode{
    QueueVal *val;
    QueueNode *next, *prev;
};

struct Queue{
    QueueNode *head, *tail;
    int elementCount;
};

Queue* createQueue() {
    Queue* q = (Queue*)malloc(sizeof(Queue));
    if (q != NULL) {
        q->head = NULL;
        q->tail = NULL;
        q->elementCount = 0;
    }
    return q;
}

QueueNode* createFromDir(char* dir) {
    QueueNode* node = (QueueNode*)malloc(sizeof(QueueNode));
    if (node != NULL) {
        node->val = (QueueVal*)malloc(sizeof(QueueVal));
        char* dirc = (char*)malloc((strlen(dir) + 1) * sizeof(char));
        strcpy(dirc, dir);
        node->next = NULL;
        node->prev = NULL;
        node->val->dir = dirc;
        node->val->idx = -1;
    }
    return node;
}

QueueNode* createFromIdx(int idx) {
    QueueNode* node = (QueueNode*)malloc(sizeof(QueueNode));
    if (node != NULL) {
        node->val = (QueueVal*)malloc(sizeof(QueueVal));
        node->next = NULL;
        node->prev = NULL;
        node->val->idx = idx;
        node->val->dir = NULL;
    }
    return node;
}

int isEmpty(Queue* q) {
    return (q->elementCount == 0);
}

int push(Queue* q, QueueNode* node) {
    if (q == NULL || node == NULL) {
        return 1;
    }
    else if (isEmpty(q)) {
        q->head = q->tail = node;
    }
    else {
        q->tail->next = node;
        node->prev = q->tail;
        q->tail->next = node;
        q->tail = node;
    }
    q->elementCount++;
    return 0;
}

QueueVal* peek(Queue* q) {
    return isEmpty(q) ? NULL : (q->head->val);
}

int pop(Queue* q) { //returns 0 if managed to pop
    if (isEmpty(q)) {
        return 1;
    }
    q->elementCount--;
    if (!q->elementCount) {
        q->head = q->tail = NULL;
    }
    else {
        q->head->next->prev = NULL;
        q->head = q->head->next;
    }
    return 0;
}

int t_pop(Queue* q, pthread_mutex_t* lock) { //pops with a lock
    pthread_mutex_lock(lock);
    int ans = pop(q);
    pthread_mutex_unlock(lock);
    return ans;
}

int t_isEmpty(Queue* q, pthread_mutex_t* lock) { //checks empty with a lock
    pthread_mutex_lock(lock);
    int ans = isEmpty(q);
    pthread_mutex_unlock(lock);
    return ans;
}

int t_push(Queue* q, QueueNode* node, pthread_mutex_t* lock, pthread_cond_t* emptyQueueCond) {
    //pushes with a lock, and updates a signal which updates if a queue became nonempty
    pthread_mutex_lock(lock);
    int em = isEmpty(q);
    int ans = push(q, node);
    pthread_mutex_unlock(lock);
    if (em) {
        pthread_cond_signal(emptyQueueCond);
    }
    return ans;
}

QueueVal* t_peek(Queue* q, pthread_mutex_t* lock) {
    //peeks with a lock
    pthread_mutex_lock(lock);
    QueueVal* ans = peek(q);
    pthread_mutex_unlock(lock);
    return ans;
}

//////////////////////////////////////////////////
//////////////Queue related end///////////////////
//////////////////////////////////////////////////

int valid(char* dir) {
    DIR *dr;
    if ((dr = opendir(dir)) == NULL) return 0;
    else {
        closedir(dr);
        return 1;
    }
}

long getIdx(void* param){
    return (long)param;
}

char* pat;

AtomicInteger matches = 0, remain;
Queue* queue, *tQueue;
pthread_t* trds;
pthread_mutex_t dirQueueMute, tQueueMute, syncMute;
pthread_cond_t* cThread, emptyQueueCond, syncCond;
AtomicInteger *isAwakeArray, curWorking = 0, isFinished = 0, errorThreads = 0;
int threads;

void processValue(char* path, char* name, int idx) {
    if (!strcmp(name, ".") || !strcmp(name, "..")) {
        return;
    }
    struct stat stats;
    if (stat(path, &stats)) { //if stat failed
        perror("Stat failed on thread");
        errorThreads++;
        pthread_exit((void*)EXIT_FAILURE);
    }
    if (S_ISDIR(stats.st_mode)) { //if directory
        if (!valid(path)) {  //no permission
            printf("Directory %s: Permission denied.\n", path);
        }
        else { //else push the dir
            t_push(queue, createFromDir(path), &dirQueueMute, &emptyQueueCond);
        }
    }
    else {
        if (strstr(name, pat) != NULL) { //if has pattern
            printf("%s\n", path);
            matches++;
        }
    }
}

void processNode(QueueVal* node, int idx) {
    DIR *dir;
    int dirlen = strlen(node->dir);
    struct dirent* entry;
    if ((dir = opendir(node->dir)) == NULL) { //cant open
        printf("Directory %s: Permission denied.\n", node->dir);
    }
    else {
        while ((entry = readdir(dir)) != NULL) { //go over all entries
            char* name = entry->d_name, *path = (char*)malloc((strlen(name) + dirlen + 2) * sizeof(char));
            strcpy(path, node->dir);
            strcat(path, "/");
            strcat(path, name);
            processValue(path, name, idx);
        }
        closedir(dir);
    }
}

//this function gets called at the beginning of each thread
void* thread_func(void* param) {
    int idx = getIdx(param);
    //responsible for synchronized start!
    pthread_mutex_lock(&syncMute);
    pthread_cond_wait(&syncCond, &syncMute);
    pthread_mutex_unlock(&syncMute);
    while (!isFinished) {
       pthread_mutex_lock(&tQueueMute);
       if (!isAwakeArray[idx]) {
            pthread_cond_wait(&cThread[idx], &tQueueMute);
       }
       if (isFinished) {
            pthread_mutex_unlock(&tQueueMute);
            for (int i = 0; i < threads; i++) {
                pthread_cond_signal(&cThread[i]);
            }
            break;
       }
       pthread_mutex_lock(&dirQueueMute);
       if (isEmpty(queue)) {
            pthread_cond_wait(&emptyQueueCond ,&dirQueueMute);
       }
       if (isFinished) {
            pthread_mutex_unlock(&dirQueueMute);
            pthread_mutex_unlock(&tQueueMute);
            for (int i = 0; i < threads; i++) {
                pthread_cond_signal(&cThread[i]);
            }
            break;
       }
       //there should only be one thread here, which is the thread on the top of the queue
       //it will take the directory on the top of the queue. the processing however will not happen inside the locks
       //of course. i might be dumb, but i'm not stupid.
       QueueVal* node = peek(queue);
       pop(queue);
       pop(tQueue);
       curWorking++;
       pthread_mutex_unlock(&dirQueueMute);
       pthread_mutex_unlock(&tQueueMute);
       processNode(node, idx); //here is the processing
       isAwakeArray[idx] = 0;
       pthread_mutex_lock(&tQueueMute);
       pthread_mutex_lock(&dirQueueMute);
       curWorking--;
       if (curWorking == 0 && isEmpty(queue)) { //queue is empty and no working threads, so no point to continue
            isFinished = 1;
            for (int i = 0; i < threads; i++) {
                isAwakeArray[i] = 1;
                pthread_cond_signal(&cThread[i]);
            }
            pthread_cond_signal(&emptyQueueCond);
       }
       else {
           push(tQueue, createFromIdx(idx));
           int topIdx = peek(tQueue)->idx;
           isAwakeArray[topIdx] = 1;
           pthread_cond_signal(&cThread[topIdx]);
       }
       pthread_mutex_unlock(&dirQueueMute);
       pthread_mutex_unlock(&tQueueMute);
    }
    pthread_exit((void*)0);
}

int checkType(char* dir) { //checks if directory with permissions, directory without permissions or file
    struct stat stats;
    if (stat(dir, &stats)) {
        perror("Stat failed in main thread");
        exit(EXIT_FAILURE);
    }
    if (S_ISDIR(stats.st_mode)) {
        if (!valid(dir)) {
            printf("Directory %s: Permission denied.\n", dir);
            return 1;
        }
        else {
            return 0;
        }
    }
    else {
        if (strstr(dir, pat) != NULL) {
            printf("%s\n", dir);
            matches++;
        }
        return 2;
    }
}

int init(char* dir) { //initial function
    switch (checkType(dir)) {
        case 1:
            return 1;
        case 2:
            return 2;
    }
    remain = threads;
    queue = createQueue();
    tQueue = createQueue();
    isAwakeArray = (AtomicInteger*)calloc(sizeof(AtomicInteger), threads);
    isAwakeArray[0] = 1;
    push(queue, createFromDir(dir));
    trds = (pthread_t*)malloc(sizeof(pthread_t) * threads);
    cThread = (pthread_cond_t*)malloc(sizeof(pthread_cond_t) * threads);
    if (pthread_mutex_init(&dirQueueMute, NULL) || pthread_mutex_init(&tQueueMute, NULL) || pthread_mutex_init(&syncMute, NULL)) {
        perror("Failed creating mutex");
        exit(EXIT_FAILURE);
    }
    if (pthread_cond_init(&emptyQueueCond, NULL) || pthread_cond_init(&syncCond, NULL)) {
        perror("Failed creating cond");
        exit(EXIT_FAILURE);
    }
    for (size_t i = 0; i < threads; i++) { //initializing the threads
        push(tQueue, createFromIdx(i));
        if (pthread_cond_init(&cThread[i], NULL)) {
            perror("Failed creating cond");
            exit(EXIT_FAILURE);
        }
        if (pthread_create(&trds[i], NULL, thread_func, (void*)i)) {
            perror("Failed creating thread");
            exit(EXIT_FAILURE);
        }
    }
    usleep(100000); //sleeping a little to sync the threads to start together.
    //on a personal note, i really need to sleep. i went through two straight all nighters, i want a vacation :D
    pthread_cond_broadcast(&syncCond);
    for (int i = 0; i < threads; i++) {
        pthread_join(trds[i], NULL);
    }
    for (int i = 0; i < threads; i++) {
        pthread_cond_destroy(&cThread[i]);
    }
    pthread_mutex_destroy(&dirQueueMute);
    pthread_mutex_destroy(&tQueueMute);
    pthread_mutex_destroy(&syncMute);
    pthread_cond_destroy(&emptyQueueCond); 
    pthread_cond_destroy(&syncCond);
    return errorThreads;
}

int main(int argc, char* argv[]) {
    if (argc != 4) {
        perror("Wrong argument amount");
        return 1;
    }
    char* dir = argv[1];
    pat = argv[2];
    threads = atoi(argv[3]);
    int exitCode = init(dir);
    printf("Done searching, found %d files\n", matches);
    return (exitCode == 0 ? 0 : 1); //zero if there will be no error exiting threads
}

//gcc -O3 -D_POSIX_C_SOURC=200809 -Wall -std=c11 -pthread pfind.c
//./a.out /home/student/CLionProjects/os4/stuff pat 10
//hi!