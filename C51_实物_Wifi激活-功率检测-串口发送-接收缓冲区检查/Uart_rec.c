#include <REG52.H>
#include <string.h>
#include "FengRuCup.h"

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

void get_string() interrupt 5
{
	get_char(&get_cache[top ++]);
	if (get_cache[top - 1] == '\n')
	{
		get_init();
		return;
	}
	if (top == 48)
		get_init();
	return;
}