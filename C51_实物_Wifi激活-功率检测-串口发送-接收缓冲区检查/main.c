#include <reg52.h>
#include <string.h>
#include "FengRuCup.h"
#define int8 unsigned char
#define int16 unsigned int

int8 order_state;
int8 wifi_done;
int8 get_cache[48];
int8 top;
int8 second;

void delay_ms(int16);
void num_init();
void get_init();
void uart_init();
void send_char(int8);
void send_string(int8[]);
void get_char(int8*);
void get_string();
void test();
void open();
void Act();
void time_init();
void time();


void time_init()
{
    TMOD |= 0x01;
	
    TH0 = 0x3c;
    TL0 = 0xb0;
    TR0 = 0;

    ET0 = 1;
	PT0 = 0;
}

void time()  interrupt 1
{
	second ++;
    TH0 = 0x3c;
    TL0 = 0xb0;
}

void num_init()
{
	wifi_done = 0;
	order_state = 0;
	top = 0;
}

void uart_init()
{
    SCON = 0x50;
    TMOD |= 0x20;
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

void get_char(int8 *c)
{
	if (SBUF == 0xFF)
	{
		top --;
		RI = 0;
		return;
	}
	*c = SBUF;
	RI = 0;
	return;
}

void get_string() interrupt 4
{
	get_char(&get_cache[top ++]);
	if (get_cache[top - 1] == '\n')
	{
		test();
		/*
		send_string(get_cache);
		P1 = 0x00;
		delay_ms(100);
		P1 = 0xff;
		*/
		get_init();
		return;
	}
	if (top == 48)
		get_init();
	return;
}

void delay_ms(int16 k)
{
	int16 i;
	int16 j;
	for (i = k;i > 0;i --)
		for (j = 112;j > 0;j --);
}

void Act()
{
	P1 = ~P1;
}

void test()
{
	if (strcmp(get_cache,"PW P SW\r\n") == 0)
		order_state = 1;
	else if (strcmp(get_cache,"OK\r\n") == 0)
		order_state = 2;
	else if (strcmp(get_cache,"WIFI GOT IP\r\n") == 0)
		order_state = 3;
	return;
}

void open()
{
	while(order_state != 3)
	{
		send_string("AT+RST");
		
		TR0 = 1;
		while(second < 150);
		TR0 = 0;
		second = 0;
	}
	order_state = 0;
	while(order_state != 2)
	{
		send_string("AT+CIPMODE=1");
		TR0 = 1;
		while(second < 50);
		TR0 = 0;
		second = 0;
	}
	order_state = 0;
	//ATsend();
	while(order_state != 2)
	{
		send_string("AT+CWMODE=1");
		TR0 = 1;
		while(second < 50);
		TR0 = 0;
		second = 0;
	}
	//ATsend();
	order_state = 0;
	while(order_state != 2)
	{
		send_string("AT+CIPSTART=\"TCP\",\"49.235.143.220\",7000");
		TR0 = 1;
		while(second < 50);
		TR0 = 0;
		second = 0;
	}
	order_state = 0;
	//ATsend();
	send_string("AT+CIPSEND");
	delay_ms(3000);
	send_string("Murasame");
}

void send_char(int8 c)
{
	ES = 0;
    SBUF = c;
    while(TI != 1);
	TI = 0;
	ES = 1;
    return;
}

void send_string(int8 c[])
{
	while(*c != '\0')
		send_char(*c ++);
	send_char('\r');
	send_char('\n');
	P1 = 0x00;
	delay_ms(100);
	P1 = 0xff;
	return;
}

void main()
{
	time_init();
	uart_init();
	num_init();
	get_init();
	open();
	while(1)
	{
		if (order_state == 1)
		{
			Act();
			order_state = 0;
		}
	}
}