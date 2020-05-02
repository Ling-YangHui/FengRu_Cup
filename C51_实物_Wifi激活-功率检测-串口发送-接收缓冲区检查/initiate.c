#include <REG52.H>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "FengRuCup.h"

void num_init()
{
		wifi_done = 0;
		order_state = 0;
		top = 0;
}

void time_init()
{
    TMOD = 0x15;
    TH0 = 0;
    TL0 = 0;
    TR0 = 0;

    TH1 = 0x3c;
    TL1 = 0xb0;
    TR1 = 0;

    EA = 1;
    ET1 = 1;
	PT1 = 1;
}

void uart_init()
{
    SCON = 0x50;
    TMOD = 0x20;
    PCON = 0x80;
    TH1 = 0xfd;
    TL1 = 0xfd;
    TR1 = 1;
	EA = 1;
	ES = 1;
}

void get_init()
{
	top = 0;
	memset(get_cache,0,48);
}