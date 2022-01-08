#include <stddef.h>
#include "os.h"

const int OFFSET_BITS = 12, VPN_BITS = 45, SIGN_EXT_BITS = 7;
const int UNUSED_BITS = 12, USED_BITS = 52;
const int PT_DEPTH = 5, SYMBOL_LEN = 9; //(64 - 7 - 12) // 9 == 5


void page_table_update(uint64_t pt, uint64_t vpn, uint64_t ppn) {
    uint64_t pageNum = vpn;
    int symbols[PT_DEPTH];
    for (int i = 0; i < PT_DEPTH; i++) {
        symbols[PT_DEPTH - i - 1] = (pageNum & ((1 << SYMBOL_LEN) - 1));
        pageNum >>= SYMBOL_LEN;
    }
    uint64_t currPPN = pt;
    for (int i = 0; i < PT_DEPTH; i++) {
        void* currListPtr = phys_to_virt(currPPN << UNUSED_BITS);
        uint64_t* currList = (uint64_t*)currListPtr;
        if (i != PT_DEPTH - 1) {
            if (!(currList[symbols[i]] & 1)) {
                if (ppn == NO_MAPPING) {
                    return;
                }
                else {
                    currList[symbols[i]] = (alloc_page_frame() << UNUSED_BITS) | 1;
                }
            }
        }
        else {
            if (ppn == NO_MAPPING) {
                currList[symbols[i]] = 0;
            }
            else {
                currList[symbols[i]] = (ppn << UNUSED_BITS) | 1;
            }
        }
        currPPN = currList[symbols[i]] >> UNUSED_BITS;
    }
}


uint64_t page_table_query(uint64_t pt, uint64_t vpn) {
    uint64_t pageNum = vpn;
    int symbols[PT_DEPTH];
    for (int i = 0; i < PT_DEPTH; i++) {
        symbols[PT_DEPTH - i - 1] = (pageNum & ((1 << SYMBOL_LEN) - 1));
        pageNum >>= SYMBOL_LEN;
    }
    uint64_t currPPN = pt;
    for (int i = 0; i < PT_DEPTH; i++) {
        void* currListPtr = phys_to_virt(currPPN << UNUSED_BITS);
        if (currListPtr == NULL) { //null for some reason
            return NO_MAPPING;
        }
        uint64_t* currList = (uint64_t*)currListPtr;
        if (!(currList[symbols[i]] & 1)) { //valid bit is off
            return NO_MAPPING;
        }
        currPPN = currList[symbols[i]] >> UNUSED_BITS;
    }
    return currPPN;
}