#include <REG52.H>
#include <string.h>
#include "FengRuCup.h"

void get_char(int8 *c)
{
	*c = SBUF;
	TI = 0;
	return;
}

void get_string() interrupt 5
{
	get_char(&get_cache[top ++]);
	if (get_cache[top - 1] == '\n')
		test();
	get_init();
	return;
}