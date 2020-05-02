#include "FengRuCup.h"
#include <stdio.h>

void open()
{
	const char RST[] = "AT+RST\n";
	const char CIPM[] = "AT+CIPMODE=1\n";
	const char CWMP[] = "AT+CWMODE=1\n";
	const char WIFI[] = "AT+CWJAP=";
	const char TCP[] = "AT+CIPSTART=\"TCP\",";
	const char CIPS[] = "AT+CIPSEND\n";
	while(order_state == 0)
	{
		send_string(RST);
		delay_double(64);
	}
	order_state = 0;
	while(order_state == 0)
	{
		send_string(CWMP);
		delay_double(64);
	}
	order_state = 0;
	while(order_state == 0)
	{
		send_string(CIPM);
		delay_double(64);
	}
	order_state = 0;
	while(order_state == 0 && wifi_done == 0)
	{
		send_string(WIFI);
		send_string(default_ssid);
		send_char(',');
		send_string(default_psd);
		send_char('\n');
		delay_double(64);
	}
	order_state = 0;
	while(order_state == 0)
	{
		send_string(TCP);
		send_string(default_IP);
		send_char(',');
		send_string(default_PORT);
		send_char('\n');
		delay_double(64);
	}
	order_state = 0;
	send_string(CIPS);
	return;
}