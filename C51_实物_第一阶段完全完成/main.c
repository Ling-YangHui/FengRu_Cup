#include <reg52.h>
#include <string.h>
#include <ctype.h>
#include <eeprom.h>
#define int8 unsigned char
#define int16 unsigned int

int8 top;
int8 order_state;
int8 emergency;
int8 useroff;
int8 get_cache[48];
int8 second;
int8 limit_second;
int16 frequency_count;
int16 PL;
int8 dot_mode;
int8 dot_count;
int8 dot_limit;
sfr T2MOD = 0xC9;

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
void count_init();

void count_init()
{
	TL0 = 0;
	TH0 = 0;
	TMOD |= 0x05;
	TR0 = 0;
}
void time_init()
{
    RCAP2H = (0xFFFF - 50000) / 256;
	RCAP2L = (0xFFFF - 50000) % 256;
	T2CON = 0;
	T2MOD = 0;
	ET2 = 1;
}
void time()  interrupt 5
{
	second ++;
    TF2 = 0;
}
void num_init()
{
    useroff = 0;
    emergency = 0;
	dot_limit = 0;
	dot_count = 0;
	dot_mode = 0;
	PL = 4000;
	order_state = 0;
	top = 0;
}
void uart_init()
{
    SCON = 0x50;
    TMOD |= 0x20;
    PCON = 0x00;
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
	if (get_cache[top - 1] == '\n' && dot_mode == 0)
	{
		get_cache[top] = 0;
		test();
		get_init();
		return;
	}
	else if (get_cache[top - 1] == '\n' && dot_mode == 1 && dot_count < dot_limit)
	{
		dot_count ++;
		top = 0;
	}
	else if (get_cache[top - 1] == '\n' && dot_mode == 1 && dot_count == dot_limit)
	{
		dot_count = 0;
		get_cache[top] = 0;
		test();
		top = 0;
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
	int16 num = 0;
    int8 top_p;
	if (strcmp(get_cache,"PW P SW\r\n") == 0)
		order_state = 1;
	else if (strcmp(get_cache,"OK\r\n") == 0)
		order_state = 2;
	else if (strcmp(get_cache,"WIFI GOT IP\r\n") == 0)
		order_state = 3;
	else if (strstr(get_cache,"PW M FRST ") != NULL)
	{
		top_p = 10;
		while(isdigit(get_cache[top_p]))
		{
			num *= 10;
			num += get_cache[top_p] - '0';
			top_p ++;
		}
		if (top_p != 10)
			limit_second = num;
	}
	else if (strstr(get_cache,"PW PL RST ") != NULL)
	{
        top_p = 10;
		while(isdigit(get_cache[top_p]))
		{
			num *= 10;
			num += get_cache[top_p] - '0';
			top_p ++;
		}
		if (top_p != 10)
			PL = num;
	}
	return;
}
void open()
{
	while(order_state != 3)
	{
		send_string("AT+RST");
		TR2 = 1;
		while(second < 150);
		TR2 = 0;
		second = 0;
	}
	order_state = 0;
	while(order_state != 2)
	{
		dot_mode = 1;
		dot_limit = 2;
		send_string("AT+CIPMODE=1");
		TR2 = 1;
		while(second < 50);
		TR2 = 0;
		second = 0;
	}
	order_state = 0;
	//ATsend();
	while(order_state != 2)
	{
		dot_limit = 2;
		send_string("AT+CWMODE=1");
		TR2 = 1;
		while(second < 50);
		TR2 = 0;
		second = 0;
	}
	//ATsend();
	order_state = 0;
	while(order_state != 2)
	{
		dot_limit = 3;
		send_string("AT+CIPSTART=\"TCP\",\"49.235.143.220\",7000");
		TR2 = 1;
		while(second < 50);
		TR2 = 0;
		second = 0;
	}
	dot_mode = 0;
	order_state = 0;
	//ATsend();
	send_string("AT+CIPSEND");
    top = 0;
	delay_ms(3000);
	send_string("Sakura");
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
	P0 = 0xFF;
	delay_ms(100);
	P0 = 0x00;
	return;
}
void main()
{
	int8 str[5] = {0,0,0,0,0};
	int8 i;
	limit_second = 60;
	P0 = 0X00;
	count_init();
	time_init();
	uart_init();
	num_init();
	get_init();
	open();
    TR0 = 1;
	TR2 = 1;
    P1 = 0x00;
	while(1)
	{
		if (second >= limit_second)
		{
            TR0 = 0;
			TR2 = 0;
			frequency_count = TH0 * 256 + TL0;
			frequency_count = frequency_count * 2.43 * 2.43 * 2 / 48 * 128 / 3579 * 200;
			frequency_count /= (limit_second / 20);
            TH0 = 0;
            TL0 = 0;
            if (useroff == 0)
            {
                if (frequency_count > PL)
                {
                    emergency = 1;
                    useroff = 1;
                    P1 = 0xFF;
                    send_string("PL exceeded");
                }
                else
                {
                    emergency = 0;
                    for (i = 0;i < 4;i ++)
                    {
                        str[3 - i] = frequency_count % 10 + '0';
                        frequency_count /= 10;
                    }		
                    send_string(str);
                }
            }
            else if (useroff == 1 && emergency == 0)
            {
                send_string("OFF");
            }
            else if (useroff == 1 && emergency == 1)
            {
                send_string("PL exceeded");
            }
			TR2 = 1;
            TR0 = 1;
			second = 0;
		}
		if (order_state == 1)
		{
            Act();
			useroff = (useroff + 1) % 2;
			order_state = 0;
		}
	}
}