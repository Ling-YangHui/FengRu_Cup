#include <REG52.H>
#include "FengRuCup.h"

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
	send_char('\r');
	send_char('\n');
	P1 = 0x00;
	delay_ms(100);
	P1 = 0xff;
	return;
}