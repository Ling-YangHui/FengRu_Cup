/*
	Authored by YangHui on 2020.02.27
	This is a program for 89C52.
	It is used to measure the frequency, calculate the power value and send the value massage by UART.
	For FengRu cup.
*/

#include <REG52.H>
#include <stdio.h>
#include <string.h>
typedef unsigned char int8;
typedef unsigned int int16;

int16 frequency;
int8 start = 1;
const int8 num[10] = {0x40,0x79,0x24,0x30,0x19,0x12,0x02,0x78,0x00,0x10};
const int8 sel[4] = {0x01,0x02,0x04,0x08};
int8 time = 0;
int8 flag = 0;
int16 power = 0;
int8 ip[] = "192.168.0.0";

void delay(int16 k)
{
    while(k --);
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
}

void show(int16 k)
{
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

void send_char(int8 c)
{
    SBUF = c;
    while(TI == 0);
	TI = 0;
    return;
}

void send_string(int8 c[])
{
	while(*c != '\0')
		send_char(*c ++);
	return;
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

		send_string(ip);
		send_char(':');
		send_char(' ');

		send_string(ss);
		send_char('w');
        send_char('\r');
		time_init();
		start = 1;
    }
}

void main()
{
	ES = 1;
    P1 = 0xff;
    P2 = 0x00;
    uart_init();
    time_init();
    while(1)
    {
        if (start == 1)
        {
            TR1 = 1;
            TR0 = 1;
            start = 0;
        }
        show(frequency);
    }
}