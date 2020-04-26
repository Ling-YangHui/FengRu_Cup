#include "FengRuCup.h"
#include <reg52.h>
/*
void ATsend()
{
	send_char('A');
	send_char('T');
	send_char('+');
}
*/

void open()
{
	//ATsend();
	send_string("AT+RST");
	delay_ms(5000);
	//ATsend();
	send_string("AT+CIPMODE=1");
	delay_ms(3000);
	//ATsend();
	send_string("AT+CWMODE=1");
	delay_ms(3000);
	//ATsend();
	send_string("AT+CIPSTART=\"TCP\",\"49.235.143.220\",7000");
	delay_ms(3000);
	//ATsend();
	send_string("AT+CIPSEND");
	delay_ms(3000);
	send_string("Murasame");
}