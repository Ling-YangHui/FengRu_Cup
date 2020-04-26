/*
	Authored by YangHui on 2020.02.27
	This is a program for AT89C52.
	It is used to measure the frequency, calculate the power value and send the value massage by UART.
	For FengRu cup.
*/

#include <REG52.H>
#include <stdio.h>
#include <string.h>
#include "FengRuCup.h"

int16 frequency;
int8 start = 1;
int8 time = 0;
int8 flag = 0;
int16 power = 0;
int8 ip[] = "192.168.0.0";

void delay_ms(int16 k)
{
	int16 i;
	int16 j;
	for (i = k;i > 0;i --)
		for (j = 112;j > 0;j --);
}

void time_frequency() interrupt 3
{
	time ++;
    TH1 = 0x3c;
    TL1 = 0xb0;
    if (time == 20)
    {	
		int8 ss[5] = {0};
    	int8 i = 0;
		TR1 = 0;
        TR0 = 0;
        time = 0;
        frequency = (TH0 * 256 + TL0) / 1.031;
		power = frequency * 2.43 * 2.43 * 2 / 48 * 128 / 3579 * 200;
		uart_init();
		i = 0;
		do
		{
		    ss[3 - i] = power % 10 + '0';
		    i ++;
		    power /= 10;
		}while (i < 4);
		send_string(ss);
		time_init();
		start = 1;
    }
}

void main()
{
	num_init();
	get_init();
	uart_init();
	open();
	time_init();
	while(1)
    {
		if (start == 1)
		{
            TR1 = 1;
            TR0 = 1;
            start = 0;
        }
    }
}