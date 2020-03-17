#include <stdio.h>
#include <REG52.h>
#include <string.h>
#include "FengRuCup.h"

void show(int16 k)
{
	const int8 sel[4] = {0x01,0x02,0x04,0x08};
	const int8 num[10] = {0x40,0x79,0x24,0x30,0x19,0x12,0x02,0x78,0x00,0x10};
    int8 i = 0,j;
    int16 ss[4];
    do
    {
        ss[i] = k % 10;
        i ++;
		k /= 10;
    }while(i < 4);
    for (j = 0;j < 4;j ++)
    {
        P1 = num[ss[j]];
        P2 = sel[3 - j];
        delay(1000);
        P1 = 0xff;
        P2 = 0X00;
    }
}