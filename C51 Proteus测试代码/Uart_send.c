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
	return;
}